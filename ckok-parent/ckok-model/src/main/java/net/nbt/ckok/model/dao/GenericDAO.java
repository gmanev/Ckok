package net.nbt.ckok.model.dao;

import java.util.List;

import net.nbt.ckok.service.OrderBy;

public interface GenericDAO<T> {

	public void add(T product);
	public T update(T entity);
	public void delete(T entity);
	public List<T> getAll();
	public T find(int productId);
	public int count();
	public List<T> get(int startIndex, int count, List<OrderBy> sort);
}
