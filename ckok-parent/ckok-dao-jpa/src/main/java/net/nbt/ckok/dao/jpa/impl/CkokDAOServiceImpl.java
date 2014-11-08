package net.nbt.ckok.dao.jpa.impl;

import javax.persistence.EntityManager;

import net.nbt.ckok.model.CkokDAOService;
import net.nbt.ckok.model.DAO;
import net.nbt.ckok.model.Product;
import net.nbt.ckok.model.ProductType;

public class CkokDAOServiceImpl implements CkokDAOService {

    private EntityManager em;
    
    public void setEntityManager(EntityManager em) { 
        this.em = em;
    }

	@Override
	public DAO<ProductType> getProductTypeDAO() {
		return new JpaDAO<ProductType>(em, ProductType.class);
	}

	@Override
	public DAO<Product> getProductDAO() {
		return new JpaDAO<Product>(em, Product.class);		
	}
}
