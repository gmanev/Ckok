package net.nbt.ckok.vaadin.filter;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;

public class ProductStateFilter implements Filter {

	private ProductState state;

	public enum ProductState {
		ON_STORE
	}

	public ProductStateFilter(ProductState state) {
		this.state = state;
	}

	@Override
	public boolean passesFilter(Object itemId, Item item)
			throws UnsupportedOperationException {
		return false;
	}

	@Override
	public boolean appliesToProperty(Object propertyId) {
		return false;
	}

	public ProductState getState() {
		return state;
	}

	public void setState(ProductState state) {
		this.state = state;
	}
	
}
