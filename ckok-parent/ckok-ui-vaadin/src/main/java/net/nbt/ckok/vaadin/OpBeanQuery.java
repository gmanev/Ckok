package net.nbt.ckok.vaadin;

import java.util.List;
import java.util.Map;

import net.nbt.ckok.model.Operation;
import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.service.OrderBy;
import net.nbt.ckok.service.ProductsQuickSearch;
import net.nbt.ckok.service.ProductsQuickSearchCount;
import net.nbt.ckok.vaadin.filter.QuickSearchFilter;

import org.vaadin.addons.lazyquerycontainer.AbstractBeanQuery;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

public class OpBeanQuery extends AbstractBeanQuery<Operation> {

	private int size = -1;
	private OrderBy orderBy = null;
	private String searchString = "";
	private CkokService service;
	
	public OpBeanQuery(QueryDefinition definition,
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
	protected Operation constructBean() {
		return new Operation();
	}

	@Override
	protected List<Operation> loadBeans(int startIndex, int count) {
		ProductsQuickSearch parameters = 
				new ProductsQuickSearch(
						startIndex,
						count,
						null,
						searchString,
						orderBy);
	//	return service.productsQuickSearch(parameters).getReturn();
		return null;
	}

	@Override
	protected void saveBeans(List<Operation> addedProducts, List<Operation> modifiedProducts,
			List<Operation> removedProducts) {
        throw new UnsupportedOperationException();		
	}

	@Override
	public int size() {
		if (size == -1) {
			size = service.productsQuickSearchCount(
					new ProductsQuickSearchCount(null, searchString)).getCount();
		}
		return size; 
	}

}
