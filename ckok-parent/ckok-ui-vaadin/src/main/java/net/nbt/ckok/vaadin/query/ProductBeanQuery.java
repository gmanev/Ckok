package net.nbt.ckok.vaadin.query;

import java.util.List;
import java.util.Map;

import net.nbt.ckok.model.Product;
import net.nbt.ckok.service.ProductsSearch;
import net.nbt.ckok.service.ProductsSearchCount;
import net.nbt.ckok.vaadin.filter.OpTypeFilter;
import net.nbt.ckok.vaadin.filter.OpTypeFilter.OpType;
import net.nbt.ckok.vaadin.filter.OperationFilter;
import net.nbt.ckok.vaadin.filter.QuickSearchFilter;

import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

import com.vaadin.data.Container.Filter;

public class ProductBeanQuery extends CkokBeanQuery<Product> {

	private int size = -1;
	private Integer last;
	private Integer operationId;
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
			else if (filter instanceof OperationFilter) {
				operationId = ((OperationFilter) filter).getOperationId();
			}
		}
	}
	
	@Override
	protected Product constructBean() {
		return new Product();
	}

	@Override
	protected List<Product> loadBeans(int startIndex, int count) {
		ProductsSearch parameters = 
				new ProductsSearch(
						startIndex,
						count,
						last,
						operationId,
						searchString,
						getOrderBy());
		return getService().productsSearch(parameters).getReturn();
	}

	@Override
	protected void saveBeans(List<Product> addedProducts, List<Product> modifiedProducts,
			List<Product> removedProducts) {
        throw new UnsupportedOperationException();		
	}

	@Override
	public int size() {
		if (size == -1) {
			size = getService().productsSearchCount(new ProductsSearchCount(last, operationId, searchString)).getCount();
		}
		return size; 
	}

}
