package net.nbt.ckok.vaadin.view;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.vaadin.query.ProductBeanQuery;

import org.vaadin.addons.lazyquerycontainer.BeanQueryFactory;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;
import org.vaadin.addons.lazyquerycontainer.LazyQueryDefinition;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

public class ProductList {

	protected LazyQueryContainer container;
	
	public ProductList(CkokService service, int batchSize) {
		BeanQueryFactory<ProductBeanQuery> queryFactory = new
				BeanQueryFactory<ProductBeanQuery>(ProductBeanQuery.class);

		Map<String,Object> queryConfiguration = new HashMap<String,Object>();
		queryConfiguration.put("service", service);
		queryFactory.setQueryConfiguration(queryConfiguration);
		
		QueryDefinition queryDefinition = new LazyQueryDefinition(false, batchSize, "id");
		queryDefinition.setMaxNestedPropertyDepth(1);
		
		queryDefinition.addProperty("productType.name", String.class, null, true, true);
		queryDefinition.addProperty("productType.partnum", String.class, null, true, true);
		queryDefinition.addProperty("serial", String.class, null, true, true);
		queryDefinition.addProperty("supplier", String.class, null, true, true);
		queryDefinition.addProperty("createdOn", Date.class, null, true, true);		
		queryDefinition.addProperty("warranty", Date.class, null, true, true);
		queryDefinition.addProperty("notes", String.class, null, true, true);		

		queryFactory.setQueryDefinition(queryDefinition);
		container = new LazyQueryContainer(queryDefinition, queryFactory);
	}
	
	public LazyQueryContainer getContainer() {
		return container;
	}

}
