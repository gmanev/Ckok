package net.nbt.ckok.vaadin.view;

import java.util.ResourceBundle;

import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.vaadin.filter.QuickSearchFilter;

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

public class ProductTypeView extends VerticalLayout implements View {

	private TextField searchField = new TextField();
	private FormLayout editorLayout = new FormLayout();
	private ProductTypeTable pTable;
	private TabSheet tabsheet = new TabSheet();	
	private final ResourceBundle messages;
	private final CkokService service;
	protected ProductTypeData data;
	private QuickSearchFilter filter = new QuickSearchFilter("");

	public ProductTypeView(CkokService service, ResourceBundle messages) {
		this.service = service;
		this.messages = messages;
		data = new ProductTypeData(service, 50);		
		pTable = new ProductTypeTable(messages);
		data.getContainer().addContainerFilter(filter);
		initLayout();
		initSearch();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		pTable.setDataSource(data.getContainer());
	}

	public void initLayout() {
		setSizeFull();

		tabsheet.addTab(editorLayout, messages.getString("product.tab.details"));

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

	private void initSearch() {
		searchField.setInputPrompt(messages.getString("product.search"));
		searchField.setTextChangeEventMode(TextChangeEventMode.LAZY);
		searchField.addTextChangeListener(new TextChangeListener() {
			public void textChange(final TextChangeEvent event) {
				filter.setSearchString(event.getText());
				data.getContainer().refresh();
			}
		});
	}
}
