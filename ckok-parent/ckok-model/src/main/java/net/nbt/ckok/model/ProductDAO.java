package net.nbt.ckok.model;

import java.util.Collection;

public interface ProductDAO {

	void add(Product entity);
	Product update(Product entity);
	void delete(Product entity);
	Collection<Product> getAll();
	
}
