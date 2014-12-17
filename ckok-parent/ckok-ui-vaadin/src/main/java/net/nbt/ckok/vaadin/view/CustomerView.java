package net.nbt.ckok.vaadin.view;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.vaadin.filter.CustomerFilter;
import net.nbt.ckok.vaadin.filter.QuickSearchFilter;
import net.nbt.ckok.vaadin.query.CustomerBeanQuery;

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

public class CustomerView extends VerticalLayout implements View {

	private final ResourceBundle messages;
	private final CkokService service;

	private Table cTable = new Table();
	private TextField searchField = new TextField();
	private FormLayout editorLayout = new FormLayout();
	private FieldGroup editorFields = new FieldGroup();
	private TabSheet tabsheet = new TabSheet();	
	private LazyQueryContainer container;
	private OpList oplist;
	private OpHistoryTable hTable;

	private static final Object[] tableFieldNames = new String[] {
		"name", "notes", "createdOn"
	};

	private static final Object[] editorFieldNames = new String[] {
		"name", "notes", "createdOn"		
	};

	public CustomerView(CkokService service, ResourceBundle messages) {
		this.service = service;
		this.messages = messages;
		oplist = new OpList(service, 50);
		hTable = new OpHistoryTable(messages);
		initLayout();
		initCustomerList();
		initEditor();
		initSearch();
	}
	
	@Override
	public void enter(ViewChangeEvent event) {
		cTable.setContainerDataSource(container);
		hTable.setContainerDataSource(oplist.getContainer(), OpHistoryTable.fields);
		
		if (cTable.getSortContainerPropertyId() == null) {
			cTable.setSortContainerPropertyId("createdOn");
			cTable.setSortAscending(false);
		}
		if (hTable.getSortContainerPropertyId() == null) {
			hTable.setSortContainerPropertyId("id");
			hTable.setSortAscending(false);
		}
		/*
		ViewParameters p = new ViewParameters(event.getParameters());
		if (p.getSearchFilter() != null) {			
			searchField.setValue(p.getSearchFilter().getSearchString());
			container.removeAllContainerFilters();
			container.addContainerFilter(p.getSearchFilter());
			container.refresh();
		}*/
	}

	public void initLayout() {
		setSizeFull();

		tabsheet.addTab(editorLayout, messages.getString("customer.tab.details"));
		tabsheet.addTab(hTable, messages.getString("product.tab.history"));
		
		VerticalSplitPanel splitPanel = new VerticalSplitPanel();
		splitPanel.setFirstComponent(cTable);
		splitPanel.setSecondComponent(tabsheet);

		cTable.setSizeFull();
		
		addComponent(searchField);
		addComponent(splitPanel);

		searchField.setWidth("100%");
		editorLayout.setMargin(true);
		tabsheet.setVisible(false);

		setExpandRatio(splitPanel, 1);
	}

	private void initCustomerList() {
		BeanQueryFactory<CustomerBeanQuery> queryFactory = new
				BeanQueryFactory<CustomerBeanQuery>(CustomerBeanQuery.class);

		Map<String,Object> queryConfiguration = new HashMap<String,Object>();
		queryConfiguration.put("service", service);
		queryFactory.setQueryConfiguration(queryConfiguration);

		container = new LazyQueryContainer(queryFactory, "id", 50, false);
		container.getQueryView().getQueryDefinition().setMaxNestedPropertyDepth(1);

		for (int i=0; i<tableFieldNames.length; i++) {
			container.addContainerProperty(tableFieldNames[i], String.class, "", true, true);
			cTable.setColumnHeader(tableFieldNames[i], messages.getString("customer." + tableFieldNames[i]));
		}

		cTable.setSelectable(true);
		cTable.setImmediate(true);

		cTable.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				Object id = cTable.getValue();
				if (id != null)
					editorFields.setItemDataSource(cTable
							.getItem(id));

				if (id != null) {
					oplist.getContainer().removeAllContainerFilters();
					oplist.getContainer().addContainerFilter(
							new CustomerFilter(Integer.parseInt(id.toString())));
					oplist.getContainer().refresh();
				}
		
				tabsheet.setVisible(id != null);
			}
		});
	}

	private void initSearch() {
		searchField.setInputPrompt(messages.getString("customer.search"));
		searchField.setTextChangeEventMode(TextChangeEventMode.LAZY);
		searchField.addTextChangeListener(new TextChangeListener() {
			public void textChange(final TextChangeEvent event) {
				container.removeAllContainerFilters();
				container.addContainerFilter(new QuickSearchFilter(event
						.getText()));
				container.refresh();
			}
		});
	}

	private void initEditor() {
		for (Object fieldName : editorFieldNames) {
			TextField field = new TextField(messages.getString("customer." + fieldName));
			editorLayout.addComponent(field);
			field.setWidth("50%");
			field.setNullRepresentation("");
			editorFields.bind(field, fieldName);
		}
		editorFields.setBuffered(true);
	}
}
