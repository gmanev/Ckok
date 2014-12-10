package net.nbt.ckok.vaadin.view;

import net.nbt.ckok.vaadin.filter.QuickSearchFilter;

public class ViewParameters {

	private QuickSearchFilter filter = null;
	
	public ViewParameters(String parameters) {
		String parts[] = parameters.split(";");
		for (int i=0; i<parts.length; i++) {
			String kv[] = parts[i].split("=");
			if (kv[0].equals("search")) {
				filter = new QuickSearchFilter(kv[1]);
			}
		}
	}
	
	public QuickSearchFilter getSearchFilter() {
		return filter;
	}
}
