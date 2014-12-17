package net.nbt.ckok.vaadin.filter;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;

public class OperationFilter implements Filter {

	private Integer operationId;

	public OperationFilter(Integer operationId) {
		this.operationId = operationId;
	}

	@Override
	public boolean passesFilter(Object itemId, Item item)
			throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean appliesToProperty(Object propertyId) {
		return false;
	}

	public Integer getOperationId() {
		return this.operationId;
	}

}
