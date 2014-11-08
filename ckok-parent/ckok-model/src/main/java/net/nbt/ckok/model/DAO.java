package net.nbt.ckok.model;

import java.util.Collection;

public interface DAO<T> {

	void add(T entity);
	T update(T entity);
	void delete(T entity);
	Collection<T> getAll();

}
