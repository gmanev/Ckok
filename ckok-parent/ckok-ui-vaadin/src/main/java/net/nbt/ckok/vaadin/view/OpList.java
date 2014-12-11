package net.nbt.ckok.vaadin.view;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.vaadin.query.OpBeanQuery;

import org.vaadin.addons.lazyquerycontainer.BeanQueryFactory;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;

public class OpList {

	protected LazyQueryContainer container;
	
	public OpList(CkokService service) {
		init(service);
	}
	
	private void init(CkokService service) {
		BeanQueryFactory<OpBeanQuery> queryFactory = new
				BeanQueryFactory<OpBeanQuery>(OpBeanQuery.class);

		Map<String,Object> queryConfiguration = new HashMap<String,Object>();
		queryConfiguration.put("service", service);
		queryFactory.setQueryConfiguration(queryConfiguration);

		container = new LazyQueryContainer(queryFactory, "id", 50, false);
		container.getQueryView().getQueryDefinition().setMaxNestedPropertyDepth(1);
		
		container.addContainerProperty("ts", Date.class, "", true, true);
		container.addContainerProperty("ts2", Date.class, "", true, true);
		container.addContainerProperty("predal", String.class, "", true, true);
		container.addContainerProperty("priel", String.class, "", true, true);
		container.addContainerProperty("notes", String.class, "", true, true);		
		container.addContainerProperty("facType", String.class, "", true, true);
		container.addContainerProperty("facNum", String.class, "", true, true);		
		container.addContainerProperty("customer.id", String.class, "", true, true);
		container.addContainerProperty("customer.name", String.class, "", true, true);
		container.addContainerProperty("optype", String.class, "", true, true);		
		container.addContainerProperty("n", Integer.class, null, true, true);
	}
	
	public LazyQueryContainer getContainer() {
		return container;
	}

}
