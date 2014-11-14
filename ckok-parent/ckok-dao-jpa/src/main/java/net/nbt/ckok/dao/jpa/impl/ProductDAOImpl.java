package net.nbt.ckok.dao.jpa.impl;

import java.util.List;

import javax.persistence.EntityManager;

import net.nbt.ckok.model.Product;
import net.nbt.ckok.model.dao.ProductDAO;

public class ProductDAOImpl implements ProductDAO {

    private EntityManager em;
    
    public void setEntityManager(EntityManager em) { 
        this.em = em;
    }

	@Override
	public void add(Product product) {
		em.persist(product);
		em.flush();
	}

	@Override
	public Product update(Product entity) {
		return em.merge(entity);
	}


	@Override
	public void delete(Product entity) {
		em.remove(entity);
		em.flush();		
	}

	@Override
	public List<Product> getAll() {
		return em.createQuery("SELECT p FROM Product p", Product.class).getResultList();
	}

	@Override
	public Product find(int productId) {
		return em.find(Product.class, productId);
	}
}
