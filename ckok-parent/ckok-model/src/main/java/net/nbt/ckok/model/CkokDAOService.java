package net.nbt.ckok.model;

public interface CkokDAOService {

	DAO<ProductType> getProductTypeDAO();
	DAO<Product> getProductDAO();
	
}
