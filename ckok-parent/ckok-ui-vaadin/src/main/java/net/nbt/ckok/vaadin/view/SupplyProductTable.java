package net.nbt.ckok.vaadin.view;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.vaadin.ui.Table;

public class SupplyProductTable extends Table {

	public static final List<String> fields = Arrays.asList(
		"productType.name", "productType.partnum",
		"serial", "supplier", "createdOn", "warranty", "notes"
	);
	
	public SupplyProductTable(ResourceBundle bundle) {
		for (String field : fields) {
			setColumnHeader(field, bundle.getString("product." + field));
		}
		setSizeFull();
		setSortContainerPropertyId("createdOn");
		setSortAscending(false);
		setPageLength(5);
		setRowHeaderMode(Table.RowHeaderMode.INDEX);		
	}

}
