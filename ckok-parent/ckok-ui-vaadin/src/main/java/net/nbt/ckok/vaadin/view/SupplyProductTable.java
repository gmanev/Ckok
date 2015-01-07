package net.nbt.ckok.vaadin.view;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.vaadin.data.Container;
import com.vaadin.ui.Table;

public class SupplyProductTable extends Table {

	private static final List<String> fields = Arrays.asList(
		"productType.name", "productType.partnum",
		"serial", "supplier", "createdOn", "notes"
	);
	
	public SupplyProductTable(ResourceBundle bundle) {
		for (String field : fields) {
			setColumnHeader(field, bundle.getString("product." + field));
		}
		setSizeFull();
		setPageLength(5);
		setRowHeaderMode(Table.RowHeaderMode.INDEX);		
	}

	public void setDataSource(Container newDataSource) {
		if (getContainerDataSource() != newDataSource) {
			super.setContainerDataSource(newDataSource, fields);
			if (getSortContainerPropertyId() == null) {
				setSortContainerPropertyId("createdOn");
				setSortAscending(false);
			}
			setConverter("createdOn", new CustomStringToDateConverter());
		}
	}
}
