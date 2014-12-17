package net.nbt.ckok.vaadin.view;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.vaadin.ui.Table;

public class ProductTable extends Table {

	public static final List<String> fields = Arrays.asList(
		"productType.name", "productType.partnum",
		"serial", "supplier", "createdOn", "warranty", "notes"
	);

	public ProductTable(ResourceBundle bundle) {
		for (String s : fields) {
			setColumnHeader(s, bundle.getString("product." + s));
		}
		
		setSizeFull();
		setSelectable(true);
		setImmediate(true);
		setRowHeaderMode(Table.RowHeaderMode.INDEX);
	}

}
