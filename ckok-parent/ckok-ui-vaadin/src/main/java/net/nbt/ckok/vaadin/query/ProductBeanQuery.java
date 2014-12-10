package net.nbt.ckok.vaadin.query;

import java.util.List;
import java.util.Map;

import net.nbt.ckok.model.Product;
import net.nbt.ckok.service.ProductsQuickSearch;
import net.nbt.ckok.service.ProductsQuickSearchCount;
import net.nbt.ckok.vaadin.filter.OpTypeFilter;
import net.nbt.ckok.vaadin.filter.OpTypeFilter.OpType;
import net.nbt.ckok.vaadin.filter.QuickSearchFilter;

import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

import com.vaadin.data.Container.Filter;

public class ProductBeanQuery extends CkokBeanQuery<Product> {

	private int size = -1;
	private Integer last;
	private String searchString = "";
	
	public ProductBeanQuery(QueryDefinition definition,
			Map<String, Object> queryConfiguration, Object[] sortPropertyIds,
			boolean[] sortStates) {
		super(definition, queryConfiguration, sortPropertyIds, sortStates);
		
		for (Filter filter : definition.getFilters()) {
			if (filter instanceof QuickSearchFilter) {
				searchString = ((QuickSearchFilter) filter).getSearchString();
			}
			else if (filter instanceof OpTypeFilter) {
				OpType optype = ((OpTypeFilter) filter).getOpType();
				if (optype != null)
					last = optype.ordinal();
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
						getOrderBy());
		return getService().productsQuickSearch(parameters).getReturn();
	}

	@Override
	protected void saveBeans(List<Product> addedProducts, List<Product> modifiedProducts,
			List<Product> removedProducts) {
        throw new UnsupportedOperationException();		
	}

	@Override
	public int size() {
		if (size == -1) {
			size = getService().productsQuickSearchCount(
					new ProductsQuickSearchCount(last, searchString)).getCount();
		}
		return size; 
	}

}
