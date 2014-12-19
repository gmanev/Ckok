package net.nbt.ckok.vaadin.query;

import java.util.List;
import java.util.Map;

import net.nbt.ckok.model.ProductDetail;
import net.nbt.ckok.service.GetProductDetail;
import net.nbt.ckok.service.GetProductDetailCount;
import net.nbt.ckok.vaadin.filter.OpTypeFilter;
import net.nbt.ckok.vaadin.filter.OpTypeFilter.OpType;

import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

import com.vaadin.data.Container.Filter;

public class ProductDetailBeanQuery extends CkokBeanQuery<ProductDetail> {

	private int size = -1;
	private Integer last;
	
	public ProductDetailBeanQuery(QueryDefinition definition,
			Map<String, Object> queryConfiguration, Object[] sortPropertyIds,
			boolean[] sortStates) {
		super(definition, queryConfiguration, sortPropertyIds, sortStates);
		for (Filter filter : definition.getFilters()) {
			if (filter instanceof OpTypeFilter) {
				OpType optype = ((OpTypeFilter) filter).getOpType();
				if (optype != null)
					last = optype.ordinal();
			}
		}
	}

	@Override
	protected ProductDetail constructBean() {
		return new ProductDetail();
	}

	@Override
	public int size() {
		if (size == -1) {
			size = getService()
					.getProductDetailCount(new GetProductDetailCount(last))
					.getCount();
		}
		return size; 
	}

	@Override
	protected List<ProductDetail> loadBeans(int startIndex, int count) {
		GetProductDetail parameters = new GetProductDetail();
		parameters.setStartIndex(startIndex);
		parameters.setCount(count);
		parameters.setLast(last);
		return getService().getProductDetail(parameters).getReturn();
	}

	@Override
	protected void saveBeans(List<ProductDetail> addedBeans,
			List<ProductDetail> modifiedBeans,
			List<ProductDetail> removedBeans) {
        throw new UnsupportedOperationException();		
	}

}
