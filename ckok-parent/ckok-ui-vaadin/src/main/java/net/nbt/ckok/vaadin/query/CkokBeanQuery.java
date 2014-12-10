package net.nbt.ckok.vaadin.query;

import java.util.Map;

import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.service.OrderBy;

import org.vaadin.addons.lazyquerycontainer.AbstractBeanQuery;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

public abstract class CkokBeanQuery<T> extends AbstractBeanQuery<T> {

	private OrderBy orderBy;
	private CkokService service;
	
	public CkokBeanQuery(QueryDefinition definition,
			Map<String, Object> queryConfiguration, Object[] sortPropertyIds,
			boolean[] sortStates) {
		super(definition, queryConfiguration, sortPropertyIds, sortStates);

		service = (CkokService)getQueryConfiguration().get("service");
		
		if (sortPropertyIds != null)
		for (int i = 0; i < sortPropertyIds.length;) {
			orderBy = new OrderBy();
			orderBy.setAttributeName(sortPropertyIds[i].toString());
			orderBy.setAscending(sortStates[i]);
			break;
		}

	}

	public OrderBy getOrderBy() {
		return orderBy;
	}

	public CkokService getService() {
		return service;
	}

}
