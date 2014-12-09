package net.nbt.ckok.vaadin;

import java.util.List;
import java.util.Map;

import net.nbt.ckok.model.Product;
import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.service.OrderBy;
import net.nbt.ckok.service.ProductsQuickSearch;
import net.nbt.ckok.service.ProductsQuickSearchCount;
import net.nbt.ckok.vaadin.filter.ProductStateFilter;
import net.nbt.ckok.vaadin.filter.ProductStateFilter.ProductState;
import net.nbt.ckok.vaadin.filter.QuickSearchFilter;

import org.vaadin.addons.lazyquerycontainer.AbstractBeanQuery;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

import com.vaadin.data.Container.Filter;

public class ProductBeanQuery extends AbstractBeanQuery<Product> {

	private Integer last;
	private int size = -1;
	private OrderBy orderBy = null;
	private String searchString = "";
	private CkokService service;
	
	public ProductBeanQuery(QueryDefinition definition,
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
		
		for (Filter filter : definition.getFilters()) {
			if (filter instanceof QuickSearchFilter) {
				searchString = ((QuickSearchFilter) filter).getSearchString();
			}
			else if (filter instanceof ProductStateFilter) {
				ProductState productState = ((ProductStateFilter) filter).getState();
				if (productState != null)
					last = productState.ordinal();
			}
		}
	}
	
	@Override
	protected Product constructBean() {
		return new Product();
	}

	@Override
	protected List<Product> loadBeans(int startIndex, int count) {
		ProductsQuickSearch parameters = 
				new ProductsQuickSearch(
						startIndex,
						count,
						last,
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
			size = service.productsQuickSearchCount(
					new ProductsQuickSearchCount(last, searchString)).getCount();
		}
		return size; 
	}

}
