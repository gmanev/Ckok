package net.nbt.ckok.vaadin;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;

public class IdLinkColumnGenerator implements ColumnGenerator  {

	private String uri;
	private String idPropertyId;
	
	public IdLinkColumnGenerator(String uri, String idPropertyId) {
		this.uri = uri;
		this.idPropertyId = idPropertyId;
	}

	@Override
	public Object generateCell(Table source, Object itemId, Object columnId) {
		String text = source.getContainerProperty(itemId, columnId).getValue().toString();
		String id = source.getContainerProperty(itemId, idPropertyId).getValue().toString();
		return new Label("<a href=\"" + uri + id + "\">" + text + "</a>", ContentMode.HTML);
	}

}
