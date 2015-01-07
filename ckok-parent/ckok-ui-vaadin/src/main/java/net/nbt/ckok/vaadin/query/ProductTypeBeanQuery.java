package net.nbt.ckok.vaadin.query;

import java.util.List;
import java.util.Map;

import net.nbt.ckok.model.ProductType;
import net.nbt.ckok.service.GetProductTypes;
import net.nbt.ckok.service.GetProductTypesCount;
import net.nbt.ckok.vaadin.filter.QuickSearchFilter;

import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

import com.vaadin.data.Container.Filter;

public class ProductTypeBeanQuery extends CkokBeanQuery<ProductType> {

	private int size = -1;
	private String searchString = "";
	
	public ProductTypeBeanQuery(QueryDefinition definition,
			Map<String, Object> queryConfiguration, Object[] sortPropertyIds,
			boolean[] sortStates) {
		super(definition, queryConfiguration, sortPropertyIds, sortStates);
		for (Filter filter : definition.getFilters()) {
			if (filter instanceof QuickSearchFilter) {
				searchString = ((QuickSearchFilter) filter).getSearchString();
			}			
		}
	}

	@Override
	protected ProductType constructBean() {
		return new ProductType();
	}

	@Override
	public int size() {
		if (size == -1) {
			size = getService()
					.getProductTypesCount(new GetProductTypesCount(searchString))
					.getCount();
		}
		return size; 
	}

	@Override
	protected List<ProductType> loadBeans(int startIndex, int count) {
		GetProductTypes parameters = new GetProductTypes();
		parameters.setStartIndex(startIndex);
		parameters.setCount(count);
		parameters.setSearchString(searchString);
		parameters.setOrderBy(getOrderBy());
		return getService().getProductTypes(parameters).getReturn();
	}

	@Override
	protected void saveBeans(List<ProductType> addedBeans,
			List<ProductType> modifiedBeans,
			List<ProductType> removedBeans) {
        throw new UnsupportedOperationException();		
	}

}
