package net.nbt.ckok.vaadin.view;

import java.util.HashMap;
import java.util.Map;

import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.vaadin.query.ProductTypeBeanQuery;

import org.vaadin.addons.lazyquerycontainer.BeanQueryFactory;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;
import org.vaadin.addons.lazyquerycontainer.LazyQueryDefinition;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

public class ProductTypeData {

	protected LazyQueryContainer container;
	
	public ProductTypeData(CkokService service, int batchSize) {
		BeanQueryFactory<ProductTypeBeanQuery> queryFactory = new
				BeanQueryFactory<ProductTypeBeanQuery>(ProductTypeBeanQuery.class);

		Map<String,Object> queryConfiguration = new HashMap<String,Object>();
		queryConfiguration.put("service", service);
		queryFactory.setQueryConfiguration(queryConfiguration);
		
		QueryDefinition queryDefinition = new LazyQueryDefinition(false, batchSize, "id");
		queryDefinition.setMaxNestedPropertyDepth(0);
		
		queryDefinition.addProperty("name", String.class, null, true, true);
		queryDefinition.addProperty("partnum", String.class, null, true, true);
		queryDefinition.addProperty("measure", String.class, null, true, true);		

		queryFactory.setQueryDefinition(queryDefinition);
		container = new LazyQueryContainer(queryDefinition, queryFactory);
	}
	
	public LazyQueryContainer getContainer() {
		return container;
	}

}
