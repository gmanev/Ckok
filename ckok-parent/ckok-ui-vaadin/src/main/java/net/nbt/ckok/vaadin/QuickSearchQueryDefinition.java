package net.nbt.ckok.vaadin;

import net.nbt.ckok.service.QueryFilters;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.UnsupportedFilterException;

public class QuickSearchQueryDefinition extends DefaultQueryDefinition {

	private QueryFilters queryFilters = new QueryFilters();

	public QueryFilters getQueryFilters() {
		return queryFilters;
	}

	public void addContainerFilter(Filter filter)
			throws UnsupportedFilterException {
	}

	public void removeContainerFilter(Filter filter) {
		queryFilters.getMatchAll().clear();

	}

	public void removeAllContainerFilters() {
		queryFilters.getMatchAll().clear();
	}

}
