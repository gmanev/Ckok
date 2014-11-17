package net.nbt.ckok.dao.jpa.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

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

	@Override
	public int count() {
		Query query = em.createQuery(
                "SELECT count(t) from Product as t");           
        return (int)((Long) query.getSingleResult()).longValue();
	}

	@Override
	public List<Product> get(int startIndex, int count, String criteria) {
		 Query query = em.createQuery(
                 "SELECT p FROM Product p" + criteria);
         query.setFirstResult(startIndex);
         query.setMaxResults(count);
        
         return query.getResultList();
	}
}
