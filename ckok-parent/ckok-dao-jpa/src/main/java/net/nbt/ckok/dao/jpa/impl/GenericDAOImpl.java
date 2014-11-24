package net.nbt.ckok.dao.jpa.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

import net.nbt.ckok.model.dao.GenericDAO;
import net.nbt.ckok.service.OrderBy;

public class GenericDAOImpl<T> implements GenericDAO<T> {

    private EntityManager em;
    private Class<T> cl;
    
    public GenericDAOImpl(Class<T> cl) { 
        this.cl = cl;
    }

    public void setEntityManager(EntityManager em) { 
        this.em = em;
    }

	@Override
	public void add(T product) {
		em.persist(product);
		em.flush();		
	}

	@Override
	public T update(T entity) {
		return em.merge(entity);
	}

	@Override
	public void delete(T entity) {
		em.remove(entity);
		em.flush();		
	}

	@Override
	public List<T> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public T find(int productId) {
		return em.find(cl, productId);
	}

	@Override
	public int count() {
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
		countQuery.select(criteriaBuilder.count(countQuery.from(cl)));
		Long count = em.createQuery(countQuery).getSingleResult();		
        return count.intValue();
	}

	@Override
	public List<T> get(int startIndex, int count, List<OrderBy> sort) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> criteriaQuery = cb.createQuery(cl);

		Root<T> from = criteriaQuery.from(cl);
		CriteriaQuery<T> select = criteriaQuery.select(from);
		for (OrderBy o : sort) {
			if (o.isAscending()) {
				select.orderBy(cb.asc(from.get(o.getAttributeName())));				
			}
			else {
				select.orderBy(cb.desc(from.get(o.getAttributeName())));
			}
		}
		 
		TypedQuery<T> typedQuery = em.createQuery(select);		
        typedQuery.setFirstResult(startIndex);
        typedQuery.setMaxResults(count);        
        return typedQuery.getResultList();
	}

}
