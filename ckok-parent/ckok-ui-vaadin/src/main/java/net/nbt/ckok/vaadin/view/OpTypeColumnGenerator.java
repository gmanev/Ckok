package net.nbt.ckok.vaadin.view;

import java.util.ResourceBundle;

import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

public class OpTypeColumnGenerator implements ColumnGenerator {

	private ResourceBundle bundle;
	
	public OpTypeColumnGenerator(ResourceBundle bundle) {
		this.bundle = bundle;
	}
	
	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		Object value = source.getContainerProperty(itemId, columnId).getValue();
		return (value == null) ? 
				null : bundle.getString("optype." + value.toString());
	}

}
