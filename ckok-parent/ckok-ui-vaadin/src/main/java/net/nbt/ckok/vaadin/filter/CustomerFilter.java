package net.nbt.ckok.vaadin.filter;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;

public class CustomerFilter implements Filter {
	
	private Integer customerId;
	
	public CustomerFilter(Integer customerId) {
		this.customerId = customerId;
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

	public Integer getCustomerId() {
		return this.customerId;
	}

}
