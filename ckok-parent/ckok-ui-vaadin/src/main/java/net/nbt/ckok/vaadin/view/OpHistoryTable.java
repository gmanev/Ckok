package net.nbt.ckok.vaadin.view;

import java.util.ResourceBundle;

import com.vaadin.ui.Table;

public class OpHistoryTable extends Table {

	private ResourceBundle bundle;
	private OpList oplist;
	
	public OpHistoryTable(OpList oplist, ResourceBundle bundle) {
		this.oplist = oplist;
		this.bundle = bundle;
		init();
	}
	
	private void init() {
		setContainerDataSource(oplist.getContainer());
		setVisibleColumns("ts",
				"optype", "n", "customer.name");
		addGeneratedColumn("optype", new OpTypeColumnGenerator(bundle));
		//table.addGeneratedColumn("customer.name", new IdLinkColumnGenerator("#!customer/search=id-", "customer.id"));
		//table.addGeneratedColumn("n", new IdLinkColumnGenerator("#!oper/search=id-", "id"));		
		setColumnHeaders(
				bundle.getString("oplist.ts"), 
				bundle.getString("oplist.optype"),
				bundle.getString("oplist.n"),
				bundle.getString("oplist.customer.name"));
		setSizeFull();
		setPageLength(5);
		setSortAscending(false);
		setSortContainerPropertyId("id");
	}
}
