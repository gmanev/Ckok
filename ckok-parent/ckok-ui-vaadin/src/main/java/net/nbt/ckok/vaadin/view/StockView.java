package net.nbt.ckok.vaadin.view;

import java.util.ResourceBundle;

import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.vaadin.filter.OpTypeFilter;
import net.nbt.ckok.vaadin.filter.OpTypeFilter.OpType;

public class StockView extends ProductView {

	OpTypeFilter filter = new OpTypeFilter(OpType.STOCK);
	
	public StockView(CkokService service, ResourceBundle messages) {
		super(service, messages);
		container.addContainerFilter(filter);
	}

}
