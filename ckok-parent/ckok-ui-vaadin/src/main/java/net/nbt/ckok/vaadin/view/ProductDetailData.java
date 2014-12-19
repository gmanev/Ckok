package net.nbt.ckok.vaadin.view;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.vaadin.query.ProductDetailBeanQuery;

import org.vaadin.addons.lazyquerycontainer.BeanQueryFactory;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;
import org.vaadin.addons.lazyquerycontainer.LazyQueryDefinition;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

public class ProductDetailData {

	protected LazyQueryContainer container;
	
	public ProductDetailData(CkokService service, int batchSize) {
		BeanQueryFactory<ProductDetailBeanQuery> queryFactory = new
				BeanQueryFactory<ProductDetailBeanQuery>(ProductDetailBeanQuery.class);

		Map<String,Object> queryConfiguration = new HashMap<String,Object>();
		queryConfiguration.put("service", service);
		queryFactory.setQueryConfiguration(queryConfiguration);
		
		QueryDefinition queryDefinition = new LazyQueryDefinition(false, batchSize, "id");
		queryDefinition.setMaxNestedPropertyDepth(0);
		
		queryDefinition.addProperty("name", String.class, null, true, true);
		queryDefinition.addProperty("partnum", String.class, null, true, true);
		queryDefinition.addProperty("serial", String.class, null, true, true);
		queryDefinition.addProperty("supplier", String.class, null, true, true);
		queryDefinition.addProperty("createdOn", Date.class, null, true, true);		
		queryDefinition.addProperty("warranty", Date.class, null, true, true);
		queryDefinition.addProperty("notes", String.class, null, true, true);		
		queryDefinition.addProperty("opTs", Date.class, null, true, true);
		queryDefinition.addProperty("opTs2", Date.class, null, true, true);
		queryDefinition.addProperty("opPredal", String.class, null, true, true);
		queryDefinition.addProperty("opPriel", String.class, null, true, true);
		queryDefinition.addProperty("opNotes", String.class, null, true, true);		
		queryDefinition.addProperty("opFacType", String.class, null, true, true);
		queryDefinition.addProperty("opFacNum", String.class, null, true, true);		
		queryDefinition.addProperty("customerId", Integer.class, null, true, true);
		queryDefinition.addProperty("customerName", String.class, null, true, true);
		queryDefinition.addProperty("opType", String.class, null, true, true);		
		queryDefinition.addProperty("opN", Integer.class, null, true, true);

		queryFactory.setQueryDefinition(queryDefinition);
		container = new LazyQueryContainer(queryDefinition, queryFactory);
	}
	
	public LazyQueryContainer getContainer() {
		return container;
	}

}
