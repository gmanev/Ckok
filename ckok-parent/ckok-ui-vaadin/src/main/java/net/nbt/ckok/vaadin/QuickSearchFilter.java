package net.nbt.ckok.vaadin;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.Item;

public class QuickSearchFilter implements Filter {

	private String searchString;
	
	public QuickSearchFilter(String searchString) {
		this.searchString = searchString;
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

	public String getSearchString() {
		return searchString;
	}
}
