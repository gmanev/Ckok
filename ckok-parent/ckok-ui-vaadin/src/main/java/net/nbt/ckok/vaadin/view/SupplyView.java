package net.nbt.ckok.vaadin.view;

import java.util.ResourceBundle;

import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.vaadin.filter.OpTypeFilter;
import net.nbt.ckok.vaadin.filter.OpTypeFilter.OpType;
import net.nbt.ckok.vaadin.filter.OperationFilter;

import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

public class SupplyView extends VerticalLayout implements View {

	private OpList opList;
	private ProductList plist;
	private SupplyTable opTable;
	private SupplyProductTable spTable;
	private TabSheet tabsheet = new TabSheet();
	
	ResourceBundle bundle;

	public SupplyView(CkokService service, ResourceBundle bundle) {
		this.bundle = bundle;
		opList = new OpList(service, 50);
		opList.getContainer().addContainerFilter(new OpTypeFilter(OpType.STOCK));
		opTable = new SupplyTable(bundle);
		plist = new ProductList(service, 50);
		spTable = new SupplyProductTable(bundle);
		initLayout();
		
		opTable.addValueChangeListener(new Property.ValueChangeListener() {
			public void valueChange(ValueChangeEvent event) {
				Object operationId = opTable.getValue();
				if (operationId != null) {
					plist.getContainer().removeAllContainerFilters();
					plist.getContainer().addContainerFilter(
							new OperationFilter(Integer.parseInt(operationId.toString())));
					plist.getContainer().refresh();
				}
				tabsheet.setVisible(operationId != null);
			}
		});

	}

	@Override
	public void enter(ViewChangeEvent event) {
		opTable.setDataSource(opList.getContainer());
		spTable.setDataSource(plist.getContainer());
	}

	private void initLayout() {
		setSizeFull();

		tabsheet.addTab(spTable, bundle.getString("product.tab.details"));

		VerticalSplitPanel splitPanel = new VerticalSplitPanel();
		
		splitPanel.setFirstComponent(opTable);
		splitPanel.setSecondComponent(tabsheet);

		opTable.setSizeFull();
		
		addComponent(splitPanel);

		setExpandRatio(splitPanel, 1);
	}

}
