package net.nbt.ckok.vaadin.view;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.vaadin.data.Container;
import com.vaadin.ui.Table;

public class ProductTypeTable extends Table {

	private static final List<String> fields = Arrays.asList("partnum", "name", "measure");
	
	public ProductTypeTable(ResourceBundle bundle) {
		for (String s : fields) {
			setColumnHeader(s, bundle.getString("productType." + s));
		}
		setSizeFull();
		setSelectable(true);
	}

	public void setDataSource(Container newDataSource) {
		if (getContainerDataSource() != newDataSource) {
			super.setContainerDataSource(newDataSource, fields);
			if (getSortContainerPropertyId() == null) {
				setSortContainerPropertyId("name");
				setSortAscending(false);
			}
		}
	}
	
}
