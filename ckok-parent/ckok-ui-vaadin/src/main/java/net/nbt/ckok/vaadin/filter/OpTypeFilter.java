package net.nbt.ckok.vaadin.filter;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;

public class OpTypeFilter implements Filter {

	public enum OpType {
		   STOCK,
		   TEST,
		   SALE,
		   REPAIR,
		   WARRANTY 
	}
	
	private OpType opType;

	public OpTypeFilter(OpType opType) {
		this.opType = opType;
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

	public OpType getOpType() {
		return opType;
	}
	
}
