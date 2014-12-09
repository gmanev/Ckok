package net.nbt.ckok.vaadin;

import java.util.List;
import java.util.Map;

import net.nbt.ckok.model.Customer;
import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.service.CustomersQuickSearch;
import net.nbt.ckok.service.CustomersQuickSearchCount;
import net.nbt.ckok.service.OrderBy;
import net.nbt.ckok.vaadin.filter.QuickSearchFilter;

import org.vaadin.addons.lazyquerycontainer.AbstractBeanQuery;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

public class CustomerBeanQuery extends AbstractBeanQuery<Customer> {

	private int size = -1;
	private OrderBy orderBy = null;
	private String searchString = "";
	private CkokService service;
	
	public CustomerBeanQuery(QueryDefinition definition,
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
		
		if (definition.getFilters().size() > 0) {
			QuickSearchFilter filter = (QuickSearchFilter) definition.getFilters().get(0);
			searchString = filter.getSearchString();
		}
	}
	
	@Override
	protected Customer constructBean() {
		return new Customer();
	}

	@Override
	public int size() {
		if (size == -1) {
			size = service.customersQuickSearchCount(
					new CustomersQuickSearchCount(searchString)).getCount();
		}
		return size;
	}

	@Override
	protected List<Customer> loadBeans(int startIndex, int count) {
		CustomersQuickSearch parameters = 
				new CustomersQuickSearch(
						startIndex,
						count,
						searchString,
						orderBy);
		return service.customersQuickSearch(parameters).getReturn();
	}

	@Override
	protected void saveBeans(List<Customer> addedBeans,
			List<Customer> modifiedBeans, List<Customer> removedBeans) {
        throw new UnsupportedOperationException();		
	}

}
