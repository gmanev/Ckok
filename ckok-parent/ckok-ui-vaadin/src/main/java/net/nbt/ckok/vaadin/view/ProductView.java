package net.nbt.ckok.vaadin.view;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.vaadin.filter.ProductFilter;
import net.nbt.ckok.vaadin.filter.QuickSearchFilter;
import net.nbt.ckok.vaadin.query.ProductBeanQuery;

import org.vaadin.addons.lazyquerycontainer.BeanQueryFactory;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.AbstractTextField.TextChangeEventMode;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

public class ProductView extends VerticalLayout implements View {

	private TextField searchField = new TextField();
	private FormLayout editorLayout = new FormLayout();
	private FieldGroup editorFields = new FieldGroup();
	private Table productList = new Table();
	private TabSheet tabsheet = new TabSheet();	
	protected LazyQueryContainer container;
	private final ResourceBundle messages;
	private final CkokService service;
	private OpList oplist;
	private QuickSearchFilter filter = new QuickSearchFilter("");

	public ProductView(CkokService service, ResourceBundle messages) {
		this.service = service;
		this.messages = messages;
		oplist = new OpList(service, 50);
		initLayout();
		initProductList();
		initEditor();
		initSearch();
	}
	
	private static final Object[] tableFieldNames = new String[] {
		"productType.name", "productType.partnum",
		"serial", "supplier", "createdOn", "warranty", "notes"
	};

	private static final Object[] editorFieldNames = new String[] {
		"productType.name", "productType.partnum",
		"serial", "supplier",
		"createdOn", "warranty",
		"notes"
	};
	
	@Override
	public void enter(ViewChangeEvent event) {
	}
	
	public void initLayout() {
		setSizeFull();

		tabsheet.addTab(editorLayout, messages.getString("product.tab.details"));
		tabsheet.addTab(new OpHistoryTable(oplist, messages), messages.getString("product.tab.history"));
		
		VerticalSplitPanel splitPanel = new VerticalSplitPanel();
		splitPanel.setFirstComponent(productList);
		splitPanel.setSecondComponent(tabsheet);

		productList.setSizeFull();
		
		addComponent(searchField);
		addComponent(splitPanel);

		searchField.setWidth("100%");
		editorLayout.setMargin(true);
		tabsheet.setVisible(false);

		setExpandRatio(splitPanel, 1);
	}

	private void initProductList() {
		BeanQueryFactory<ProductBeanQuery> queryFactory = new
				BeanQueryFactory<ProductBeanQuery>(ProductBeanQuery.class);

		Map<String,Object> queryConfiguration = new HashMap<String,Object>();
		queryConfiguration.put("service", service);
		queryFactory.setQueryConfiguration(queryConfiguration);

		container = new LazyQueryContainer(queryFactory, "id", 50, false);
		container.getQueryView().getQueryDefinition().setMaxNestedPropertyDepth(1);
		container.addContainerFilter(filter);
		
		for (int i=0; i<tableFieldNames.length; i++) {
			container.addContainerProperty(tableFieldNames[i], String.class, "", true, true);
			productList.setColumnHeader(tableFieldNames[i], messages.getString("product." + tableFieldNames[i]));
		}
		
		productList.setContainerDataSource(container);
		productList.setSortContainerPropertyId("createdOn");
		productList.setSortAscending(false);
		productList.setSelectable(true);
		productList.setImmediate(true);
		productList.setRowHeaderMode(Table.RowHeaderMode.INDEX);

		productList.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				Object productId = productList.getValue();
				if (productId != null)
					editorFields.setItemDataSource(productList
							.getItem(productId));

				if (productId != null) {
					oplist.getContainer().removeAllContainerFilters();
					oplist.getContainer().addContainerFilter(
							new ProductFilter(Integer.parseInt(productId.toString())));
					oplist.getContainer().refresh();
				}

				tabsheet.setVisible(productId != null);
			}
		});
	}

	private void initSearch() {
		searchField.setInputPrompt(messages.getString("product.search"));
		searchField.setTextChangeEventMode(TextChangeEventMode.LAZY);
		searchField.addTextChangeListener(new TextChangeListener() {
			public void textChange(final TextChangeEvent event) {
				filter.setSearchString(event.getText());
				container.refresh();
			}
		});
	}


	private void initEditor() {
		for (Object fieldName : editorFieldNames) {
			TextField field = new TextField(messages.getString("product." + fieldName));
			editorLayout.addComponent(field);
			field.setWidth("50%");
			field.setNullRepresentation("");
			editorFields.bind(field, fieldName);
		}
		editorFields.setBuffered(true);
	}

}
