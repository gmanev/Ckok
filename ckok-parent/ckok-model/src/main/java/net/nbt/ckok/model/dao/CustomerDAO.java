package net.nbt.ckok.model.dao;

import java.util.List;

import net.nbt.ckok.model.Customer;
import net.nbt.ckok.service.OrderBy;

public interface CustomerDAO extends GenericDAO<Customer> {

	public List<Customer> quickSearch(int startIndex, int count, String searchString, OrderBy orderBy);
	public int quickSearchCount(String searchString);

}
