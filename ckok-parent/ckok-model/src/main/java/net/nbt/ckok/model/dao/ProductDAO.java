package net.nbt.ckok.model.dao;

import java.util.List;

import net.nbt.ckok.model.Product;
import net.nbt.ckok.service.OrderBy;

public interface ProductDAO extends GenericDAO<Product> {

	public List<Product> quickSearch(int startIndex, int count, String searchString, OrderBy orderBy);
	public int quickSearchCount(String searchString);

}
