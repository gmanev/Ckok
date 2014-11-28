package net.nbt.ckok.vaadin;

import java.util.List;
import java.util.Map;

import net.nbt.ckok.model.Product;
import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.service.OrderBy;
import net.nbt.ckok.service.ProductsQuickSearch;
import net.nbt.ckok.service.ProductsQuickSearchCount;

import org.vaadin.addons.lazyquerycontainer.AbstractBeanQuery;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

public class ProductBeanQuery extends AbstractBeanQuery<Product> {

	private int size = -1;
	private OrderBy orderBy = null;
	private String searchString = "";
	
	public ProductBeanQuery(QueryDefinition definition,
			Map<String, Object> queryConfiguration, Object[] sortPropertyIds,
			boolean[] sortStates) {
		super(definition, queryConfiguration, sortPropertyIds, sortStates);

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
	protected Product constructBean() {
		return new Product();
	}

	@Override
	protected List<Product> loadBeans(int startIndex, int count) {
		CkokService service =
				(CkokService)getQueryConfiguration().get("service");		
		ProductsQuickSearch parameters = 
				new ProductsQuickSearch(
						startIndex,
						count,
						searchString,
						orderBy);
		return service.productsQuickSearch(parameters).getReturn();
	}

	@Override
	protected void saveBeans(List<Product> addedProducts, List<Product> modifiedProducts,
			List<Product> removedProducts) {
        throw new UnsupportedOperationException();		
	}

	@Override
	public int size() {
		if (size == -1) {
			CkokService service = (CkokService)getQueryConfiguration().get("service");
			ProductsQuickSearchCount request = new ProductsQuickSearchCount(searchString);
			size = service.productsQuickSearchCount(request).getCount();
		}
		return size; 
	}

}
