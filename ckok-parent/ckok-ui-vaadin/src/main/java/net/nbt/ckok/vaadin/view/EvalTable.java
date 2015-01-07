package net.nbt.ckok.vaadin.view;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.vaadin.data.Container;
import com.vaadin.ui.Table;

public class EvalTable extends Table {

	private static final List<String> fields = Arrays.asList(
			"opTs2", "name", "serial",
			"customerName", "opN", "opTs");

	private static final List<String> headers = Arrays.asList(
			"eval.ts2", "eval.product", "product.serial",
			"eval.customer", "oplist.n", "oplist.ts");


	public EvalTable(ResourceBundle bundle) {
		for (int index = 0; index < fields.size(); index++) {
			setColumnHeader(fields.get(index), bundle.getString(headers.get(index)));
		}

		setSortEnabled(false);
		setSizeFull();
		setSelectable(true);
		setImmediate(true);
		setRowHeaderMode(Table.RowHeaderMode.INDEX);
	}

	public void setDataSource(Container newDataSource) {
		if (getContainerDataSource() != newDataSource) {
			super.setContainerDataSource(newDataSource, fields);
			setConverter("opTs", new CustomStringToDateConverter());
			setConverter("opTs2", new CustomStringToDateConverter());
		}
	}

}
