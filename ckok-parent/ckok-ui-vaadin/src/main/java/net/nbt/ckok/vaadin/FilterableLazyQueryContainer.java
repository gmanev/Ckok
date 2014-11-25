package net.nbt.ckok.vaadin;

import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;
import org.vaadin.addons.lazyquerycontainer.QueryFactory;

import com.vaadin.data.Container.Filterable;
import com.vaadin.data.util.filter.UnsupportedFilterException;

public class FilterableLazyQueryContainer extends LazyQueryContainer implements Filterable {

	private QuickSearchQueryDefinition queryDefinition;
	
	public FilterableLazyQueryContainer(QuickSearchQueryDefinition queryDefinition,
			QueryFactory queryFactory) {
		super(queryDefinition, queryFactory);
		this.queryDefinition = queryDefinition;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -275342519007039363L;
	
	@Override
	public void addContainerFilter(Filter filter)
			throws UnsupportedFilterException {
		queryDefinition.addContainerFilter(filter);
		refresh();
	}

	@Override
	public void removeContainerFilter(Filter filter) {
		queryDefinition.removeContainerFilter(filter);
		refresh();		
	}

	@Override
	public void removeAllContainerFilters() {
		queryDefinition.removeAllContainerFilters();
		refresh();		
	}

}
