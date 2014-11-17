package net.nbt.ckok.vaadin;

import java.util.List;
import java.util.Map;

import net.nbt.ckok.model.Product;
import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.service.GetProducts;
import net.nbt.ckok.service.GetProductsCount;

import org.vaadin.addons.lazyquerycontainer.AbstractBeanQuery;
import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

public class ProductBeanQuery extends AbstractBeanQuery<Product> {

	public ProductBeanQuery(QueryDefinition definition,
			Map<String, Object> queryConfiguration, Object[] sortPropertyIds,
			boolean[] sortStates) {
		super(definition, queryConfiguration, sortPropertyIds, sortStates);
	}
	
	@Override
	protected Product constructBean() {
		return new Product();
	}

	@Override
	protected List<Product> loadBeans(int startIndex, int count) {
		CkokService service =
				(CkokService)getQueryConfiguration().get("service");		
		GetProducts parameters = new GetProducts();
		parameters.setStartIndex(startIndex);
		parameters.setCount(count);
		parameters.setCriteria("");
/*
		List<Product> items = new ArrayList<Product>();
		for (Product product : service.getProducts(parameters).getReturn()) {
			items.add(new BeanItem<Product>(product));
		}*/
		return service.getProducts(parameters).getReturn();
	}

	@Override
	protected void saveBeans(List<Product> addedProducts, List<Product> modifiedProducts,
			List<Product> removedProducts) {
        throw new UnsupportedOperationException();		
	}

	@Override
	public int size() {
		CkokService service =
				(CkokService)getQueryConfiguration().get("service");		
		return service.getProductsCount(new GetProductsCount()).getCount();
	}

}
