package net.nbt.ckok.vaadin;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.nbt.ckok.model.Product;
import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.service.GetProducts;
import net.nbt.ckok.service.GetProductsCount;
import net.nbt.ckok.service.OrderBy;
import net.nbt.ckok.service.QueryFilter;

import org.vaadin.addons.lazyquerycontainer.AbstractBeanQuery;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

public class ProductBeanQuery extends AbstractBeanQuery<Product> {

	private int size = -1;
	private List<OrderBy> sort = new ArrayList<OrderBy>();
	private final QueryFilter filter;
	
	public ProductBeanQuery(QueryDefinition definition,
			Map<String, Object> queryConfiguration, Object[] sortPropertyIds,
			boolean[] sortStates) {
		super(definition, queryConfiguration, sortPropertyIds, sortStates);

		for (int i = 0; i < sortPropertyIds.length; i++) {
			OrderBy orderBy = new OrderBy();
			orderBy.setAttributeName(sortPropertyIds[i].toString());
			orderBy.setAscending(sortStates[i]);
			sort.add(orderBy);
		}
		
		filter = ((QuickSearchQueryDefinition) definition).getQueryFilter();
	}
	
	@Override
	protected Product constructBean() {
		return new Product();
	}

	@Override
	protected List<Product> loadBeans(int startIndex, int count) {
		CkokService service =
				(CkokService)getQueryConfiguration().get("service");		
		GetProducts parameters = 
				new GetProducts(startIndex, count, filter, sort);
		return service.getProducts(parameters).getReturn();
	}

	@Override
	protected void saveBeans(List<Product> addedProducts, List<Product> modifiedProducts,
			List<Product> removedProducts) {
        throw new UnsupportedOperationException();		
	}

	@Override
	public int size() {
		if (size == -1) {
			CkokService service =
					(CkokService)getQueryConfiguration().get("service");
			GetProductsCount request = new GetProductsCount(filter);
			size = service.getProductsCount(request).getCount();
		}
		return size; 
	}

}
