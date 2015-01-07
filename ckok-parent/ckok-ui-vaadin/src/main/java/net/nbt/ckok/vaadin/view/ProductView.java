package net.nbt.ckok.vaadin.view;

import java.util.ResourceBundle;

import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.vaadin.filter.ProductFilter;
import net.nbt.ckok.vaadin.filter.QuickSearchFilter;

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
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

public class ProductView extends VerticalLayout implements View {

	private TextField searchField = new TextField();
	private FormLayout editorLayout = new FormLayout();
	private FieldGroup editorFields = new FieldGroup();
	private ProductTable pTable;
	private OpHistoryTable hTable;
	private TabSheet tabsheet = new TabSheet();	
	private final ResourceBundle messages;
	private final CkokService service;
	private OpList oplist;
	protected ProductList plist;
	private QuickSearchFilter filter = new QuickSearchFilter("");

	public ProductView(CkokService service, ResourceBundle messages) {
		this.service = service;
		this.messages = messages;
		oplist = new OpList(service, 50);
		pTable = new ProductTable(messages);
		hTable = new OpHistoryTable(messages);
		plist = new ProductList(service, 50);
		plist.getContainer().addContainerFilter(filter);
		initLayout();
		initProductList();
		initEditor();
		initSearch();
	}

	private static final Object[] editorFieldNames = new String[] {
		"productType.name", "productType.partnum",
		"serial", "supplier",
		"createdOn", "warranty",
		"notes"
	};
	
	@Override
	public void enter(ViewChangeEvent event) {
		pTable.setDataSource(plist.getContainer());
		hTable.setDataSource(oplist.getContainer());
	}
	
	public void initLayout() {
		setSizeFull();

		tabsheet.addTab(editorLayout, messages.getString("product.tab.details"));
		tabsheet.addTab(hTable, messages.getString("product.tab.history"));

		VerticalSplitPanel splitPanel = new VerticalSplitPanel();
		splitPanel.setFirstComponent(pTable);
		splitPanel.setSecondComponent(tabsheet);
	
		addComponent(searchField);
		addComponent(splitPanel);

		searchField.setWidth("100%");
		editorLayout.setMargin(true);
		tabsheet.setVisible(false);

		setExpandRatio(splitPanel, 1);
	}

	private void initProductList() {
		pTable.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				Object productId = pTable.getValue();
				if (productId != null)
					editorFields.setItemDataSource(pTable
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
				plist.getContainer().refresh();
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
