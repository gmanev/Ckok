package net.nbt.ckok.model.dao;

import java.util.List;

import net.nbt.ckok.model.Product;

public interface ProductDAO {

	public void add(Product product);
	public Product update(Product entity);
	public void delete(Product entity);
	public List<Product> getAll();
	public Product find(int productId);
	public int count();
	public List<Product> get(int startIndex, int count, String criteria);
}
