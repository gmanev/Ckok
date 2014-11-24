package net.nbt.ckok.dao.jpa.impl;

import net.nbt.ckok.model.Product;
import net.nbt.ckok.model.dao.ProductDAO;

public class ProductDAOImpl extends GenericDAOImpl<Product> implements ProductDAO  {
	
	public ProductDAOImpl() {
		super(Product.class);
	}
}
