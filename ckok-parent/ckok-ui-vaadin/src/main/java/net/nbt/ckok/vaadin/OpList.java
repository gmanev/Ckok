package net.nbt.ckok.vaadin;

import java.util.ResourceBundle;

import net.nbt.ckok.model.Operation;

import com.vaadin.data.util.BeanContainer;
import com.vaadin.ui.Table;

public class OpList {

	private ResourceBundle bundle;
	private Table table = new Table();
	private BeanContainer<Integer, Operation> beans = new BeanContainer<>(Operation.class);
	
	public OpList(ResourceBundle bundle) {
		this.bundle = bundle;
		initBinding();
	}
	
	private void initBinding() {
		beans.setBeanIdProperty("id");
		beans.addNestedContainerProperty("ts");		
		beans.addNestedContainerProperty("customer.name");
		beans.addNestedContainerProperty("optype");		
		table.setContainerDataSource(beans);
		table.setVisibleColumns("ts",
				"optype", "n", "customer.name");
		table.addGeneratedColumn("optype", new OpTypeColumnGenerator(bundle));
		table.setColumnHeaders(
				bundle.getString("oplist.ts"), 
				bundle.getString("oplist.optype"),
				bundle.getString("oplist.n"),
				bundle.getString("oplist.customer.name"));
		table.setSizeFull();
		table.setPageLength(0);
	}

	public Table getTable() {
		return table;
	}
	
	public BeanContainer<Integer, Operation> getContainer() {
		return beans;
	}
		
}
