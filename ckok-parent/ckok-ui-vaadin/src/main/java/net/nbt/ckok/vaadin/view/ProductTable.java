package net.nbt.ckok.vaadin.view;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.vaadin.data.Container;
import com.vaadin.ui.Table;

public class ProductTable extends Table {

	private static final List<String> fields = Arrays.asList(
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

	public void setDataSource(Container newDataSource) {
		super.setContainerDataSource(newDataSource, fields);
		if (getSortContainerPropertyId() == null) {
			setSortContainerPropertyId("createdOn");
			setSortAscending(false);
		}
	}
}
