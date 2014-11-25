package net.nbt.ckok.model.dao;

import java.util.List;

import net.nbt.ckok.service.OrderBy;
import net.nbt.ckok.service.QueryFilters;

public interface GenericDAO<T> {

	public void add(T product);
	public T update(T entity);
	public void delete(T entity);
	public List<T> getAll();
	public T find(int productId);
	public int count();
	public int count(QueryFilters queryFilters);	
	public List<T> get(int startIndex, int count, QueryFilters queryFilters, List<OrderBy> sort);

}
