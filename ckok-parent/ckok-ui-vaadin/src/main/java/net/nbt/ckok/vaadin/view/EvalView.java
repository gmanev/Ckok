package net.nbt.ckok.vaadin.view;

import java.util.ResourceBundle;

import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.vaadin.filter.OpTypeFilter;
import net.nbt.ckok.vaadin.filter.OpTypeFilter.OpType;
import net.nbt.ckok.vaadin.filter.ProductFilter;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

public class EvalView extends VerticalLayout implements View {

	private EvalTable pTable;
	private OpHistoryTable hTable;	
	private TabSheet tabsheet = new TabSheet();	
	private final ResourceBundle messages;
	private final CkokService service;
	protected ProductDetailData data;
	private OpList oplist;

	OpTypeFilter filter = new OpTypeFilter(OpType.TEST);

	public EvalView(CkokService service, ResourceBundle messages) {
		this.service = service;
		this.messages = messages;
		oplist = new OpList(service, 50);		
		pTable = new EvalTable(messages);
		hTable = new OpHistoryTable(messages);
		data = new ProductDetailData(service, 50);
		data.getContainer().addContainerFilter(filter);
		initLayout();
		initProductList();
	}

	@Override
	public void enter(ViewChangeEvent event) {
		pTable.setDataSource(data.getContainer());
		hTable.setDataSource(oplist.getContainer());
	}
	
	public void initLayout() {
		setSizeFull();

		tabsheet.addTab(hTable, messages.getString("product.tab.history"));

		VerticalSplitPanel splitPanel = new VerticalSplitPanel();
		splitPanel.setFirstComponent(pTable);
		splitPanel.setSecondComponent(tabsheet);
	
		addComponent(splitPanel);

		tabsheet.setVisible(false);

		setExpandRatio(splitPanel, 1);
	}

	private void initProductList() {
		pTable.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				Object productId = pTable.getValue();
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


}
