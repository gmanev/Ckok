package net.nbt.ckok.vaadin.view;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.vaadin.ui.Table;

public class OpHistoryTable extends Table {

	public static final List<String> fields = Arrays.asList(
		"ts", "optype", "n", "customer.name"
	);

	public OpHistoryTable(ResourceBundle bundle) {
		for (String field : fields) {
			setColumnHeader(field, bundle.getString("oplist." + field));
		}
		addGeneratedColumn("optype", new OpTypeColumnGenerator(bundle));
		setSizeFull();
		setPageLength(5);
	}

}
