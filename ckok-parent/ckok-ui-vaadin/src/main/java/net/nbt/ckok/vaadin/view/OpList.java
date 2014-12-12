package net.nbt.ckok.vaadin.view;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.vaadin.query.OpBeanQuery;

import org.vaadin.addons.lazyquerycontainer.BeanQueryFactory;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;
import org.vaadin.addons.lazyquerycontainer.LazyQueryDefinition;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

public class OpList {

	protected LazyQueryContainer container;
	
	public OpList(CkokService service, int batchSize) {
		BeanQueryFactory<OpBeanQuery> queryFactory = new
				BeanQueryFactory<OpBeanQuery>(OpBeanQuery.class);

		Map<String,Object> queryConfiguration = new HashMap<String,Object>();
		queryConfiguration.put("service", service);
		queryFactory.setQueryConfiguration(queryConfiguration);
		
		QueryDefinition queryDefinition = new LazyQueryDefinition(false, batchSize, "id");
		queryDefinition.setMaxNestedPropertyDepth(1);
		
		queryDefinition.addProperty("ts", Date.class, null, true, true);
		queryDefinition.addProperty("ts2", Date.class, null, true, true);
		queryDefinition.addProperty("predal", String.class, null, true, true);
		queryDefinition.addProperty("priel", String.class, null, true, true);
		queryDefinition.addProperty("notes", String.class, null, true, true);		
		queryDefinition.addProperty("facType", String.class, null, true, true);
		queryDefinition.addProperty("facNum", String.class, null, true, true);		
		queryDefinition.addProperty("customer.id", Integer.class, null, true, true);
		queryDefinition.addProperty("customer.name", String.class, null, true, true);
		queryDefinition.addProperty("optype", String.class, null, true, true);		
		queryDefinition.addProperty("n", Integer.class, null, true, true);

		queryFactory.setQueryDefinition(queryDefinition);
		container = new LazyQueryContainer(queryDefinition, queryFactory);
	}
	
	public LazyQueryContainer getContainer() {
		return container;
	}

}
