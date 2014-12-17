package net.nbt.ckok.vaadin.view;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.vaadin.ui.Table;

public class SupplyTable extends Table {

	public static final List<String> fields = Arrays.asList("ts", "n", "notes");
	
	public SupplyTable(ResourceBundle bundle) {
		for (String s : fields) {
			setColumnHeader(s, bundle.getString("oplist." + s));
		}
		setSizeFull();
		setSelectable(true);
		setSortAscending(false);
		setSortContainerPropertyId("ts");
	}

}
