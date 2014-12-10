package net.nbt.ckok.vaadin.query;

import java.util.List;
import java.util.Map;

import net.nbt.ckok.model.Operation;
import net.nbt.ckok.service.OperationsSearch;
import net.nbt.ckok.service.OperationsSearchCount;
import net.nbt.ckok.vaadin.filter.CustomerFilter;
import net.nbt.ckok.vaadin.filter.OpTypeFilter;
import net.nbt.ckok.vaadin.filter.ProductFilter;

import org.vaadin.addons.lazyquerycontainer.QueryDefinition;

import com.vaadin.data.Container.Filter;

public class OpBeanQuery extends CkokBeanQuery<Operation> {
	
	private int size = -1;
	private Integer optype;
	private String searchString = "";
	private Integer product;
	private Integer customer;

	public OpBeanQuery(QueryDefinition definition,
			Map<String, Object> queryConfiguration, Object[] sortPropertyIds,
			boolean[] sortStates) {
		super(definition, queryConfiguration, sortPropertyIds, sortStates);
		
		for (Filter filter : definition.getFilters()) {
			if (filter instanceof OpTypeFilter) {
				optype = ((OpTypeFilter) filter).getOpType().ordinal();
			}
			else if (filter instanceof ProductFilter) {
				product = ((ProductFilter) filter).getProductId();
			}
			else if (filter instanceof CustomerFilter) {
				customer = ((CustomerFilter) filter).getCustomerId();
			}
		}
	}

	@Override
	protected Operation constructBean() {
		return new Operation();
	}

	@Override
	protected List<Operation> loadBeans(int startIndex, int count) {
		OperationsSearch parameters = 
				new OperationsSearch(
						startIndex,
						count,
						optype, 
						product,
						customer,
						searchString,
						getOrderBy());
		return getService().operationsSearch(parameters).getReturn();
	}

	@Override
	protected void saveBeans(List<Operation> addedProducts, List<Operation> modifiedProducts,
			List<Operation> removedProducts) {
        throw new UnsupportedOperationException();		
	}

	@Override
	public int size() {
		if (size == -1) {
			size = getService().operationsSearchCount(
					new OperationsSearchCount(
							optype, 
							product,
							customer,
							searchString)).getCount();
		}
		return size; 
	}

}
