package net.nbt.ckok.vaadin;

import net.nbt.ckok.service.MatchAnyFilter;
import net.nbt.ckok.service.QueryFilter;
import net.nbt.ckok.service.StringFilter;

import com.vaadin.data.Container.Filter;
import com.vaadin.data.util.filter.Or;
import com.vaadin.data.util.filter.SimpleStringFilter;
import com.vaadin.data.util.filter.UnsupportedFilterException;

public class QuickSearchQueryDefinition extends DefaultQueryDefinition {

	private QueryFilter queryFilter = null;

	public QueryFilter getQueryFilter() {
		return queryFilter;
	}

	public void addContainerFilter(Filter filter)
			throws UnsupportedFilterException {
		queryFilter = createFrom(filter);
	}

	private static QueryFilter createFrom(Filter filter) throws UnsupportedOperationException {

		if (filter instanceof SimpleStringFilter) {
			SimpleStringFilter f = (SimpleStringFilter) filter;
			return new StringFilter(f.getPropertyId().toString(), 
					f.getFilterString(), f.isIgnoreCase(), f.isOnlyMatchPrefix());		
		}
		else if (filter instanceof Or) {
			MatchAnyFilter result = new MatchAnyFilter();
			for (Filter f : ((Or) filter).getFilters()) {
				result.getFilters().add(createFrom(f));
			}
			return result;
		}

		throw new UnsupportedFilterException();
	}
	
	public void removeContainerFilter(Filter filter) {
		queryFilter = null;
	}

	public void removeAllContainerFilters() {
		queryFilter = null;
	}

}
