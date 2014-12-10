package net.nbt.ckok.vaadin.view;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.vaadin.query.OpBeanQuery;

import org.vaadin.addons.lazyquerycontainer.BeanQueryFactory;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;

import com.vaadin.ui.Table;

public class OpList {

	private ResourceBundle bundle;
	private Table table = new Table();
	protected LazyQueryContainer container;
	
	public OpList(CkokService service, ResourceBundle bundle) {
		this.bundle = bundle;
		initBinding(service);
	}
	
	private void initBinding(CkokService service) {
		BeanQueryFactory<OpBeanQuery> queryFactory = new
				BeanQueryFactory<OpBeanQuery>(OpBeanQuery.class);

		Map<String,Object> queryConfiguration = new HashMap<String,Object>();
		queryConfiguration.put("service", service);
		queryFactory.setQueryConfiguration(queryConfiguration);

		container = new LazyQueryContainer(queryFactory, "id", 50, false);
		container.getQueryView().getQueryDefinition().setMaxNestedPropertyDepth(1);
		
		container.addContainerProperty("ts", String.class, "", true, true);
		container.addContainerProperty("customer.id", String.class, "", true, true);
		container.addContainerProperty("customer.name", String.class, "", true, true);
		container.addContainerProperty("optype", String.class, "", true, true);		
		container.addContainerProperty("n", Integer.class, null, true, true);		

		table.setContainerDataSource(container);
		table.setVisibleColumns("ts",
				"optype", "n", "customer.name");
		table.addGeneratedColumn("optype", new OpTypeColumnGenerator(bundle));
		//table.addGeneratedColumn("customer.name", new IdLinkColumnGenerator("#!customer/search=id-", "customer.id"));
		//table.addGeneratedColumn("n", new IdLinkColumnGenerator("#!oper/search=id-", "id"));		
		table.setColumnHeaders(
				bundle.getString("oplist.ts"), 
				bundle.getString("oplist.optype"),
				bundle.getString("oplist.n"),
				bundle.getString("oplist.customer.name"));
		table.setSizeFull();
		table.setRowHeaderMode(Table.RowHeaderMode.INDEX);
	}
	
	public LazyQueryContainer getContainer() {
		return container;
	}

	public Table getTable() {
		return table;
	}
	
}
