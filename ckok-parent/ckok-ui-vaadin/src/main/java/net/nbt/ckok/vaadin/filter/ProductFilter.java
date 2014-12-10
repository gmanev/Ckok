package net.nbt.ckok.vaadin.filter;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;

public class ProductFilter implements Filter {

	private Integer productId;
	
	public ProductFilter(Integer productId) {
		this.productId = productId;
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

	public Integer getProductId() {
		return this.productId;
	}

}
