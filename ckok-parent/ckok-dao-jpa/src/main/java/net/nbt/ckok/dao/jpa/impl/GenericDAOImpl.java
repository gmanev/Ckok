package net.nbt.ckok.dao.jpa.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.nbt.ckok.model.dao.GenericDAO;
import net.nbt.ckok.service.OrderBy;
import net.nbt.ckok.service.QueryFilter;
import net.nbt.ckok.service.QueryFilters;
import net.nbt.ckok.service.StringFilter;

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
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		countQuery.select(cb.count(countQuery.from(cl)));
		Long count = em.createQuery(countQuery).getSingleResult();		
        return count.intValue();
	}

	private List<Predicate> asPredicates(CriteriaBuilder cb, Root<T> from, QueryFilters queryFilters) {
		List<Predicate> predicates = new ArrayList<Predicate>();
		for (QueryFilter queryFilter : queryFilters.getMatchAll()) {
			StringFilter filter = (StringFilter) queryFilter;
			Expression<String> ex = from.get(filter.getAttributeName()).as(String.class);
			if (filter.isOnlyMatchPrefix()) {
				predicates.add(
						filter.isIgnoreCase() ?
								cb.like(cb.lower(ex), String.format("%%%s%%", filter.getFilterString().toLowerCase())) :
								cb.like(ex, String.format("%%%s%%", filter.getFilterString())));
			}
			else {
				predicates.add(
						filter.isIgnoreCase() ?
								cb.equal(cb.lower(ex), filter.getFilterString().toLowerCase()) :
								cb.equal(ex, filter.getFilterString()));
			}
		}
	    return predicates;
	}

	private Predicate asPredicate(CriteriaBuilder cb, Root<T> from, QueryFilters queryFilters) {
		List<Predicate> predicates = asPredicates(cb, from, queryFilters);
		return cb.and(predicates.toArray(new Predicate[]{}));
	}
	
	@Override
	public List<T> get(int startIndex, int count, QueryFilters queryFilters, List<OrderBy> sort) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(cl);

		Root<T> from = q.from(cl);
		CriteriaQuery<T> select = q.select(from);

		select.where(asPredicate(cb, from, queryFilters));
		
		for (OrderBy o : sort) {
			if (o.isAscending()) {
				select.orderBy(cb.asc(from.get(o.getAttributeName())));				
			}
			else {
				select.orderBy(cb.desc(from.get(o.getAttributeName())));
			}
		}

		TypedQuery<T> typedQuery = em.createQuery(select);
		if (count > 0) {
			typedQuery.setFirstResult(startIndex);
			typedQuery.setMaxResults(count);
		}
        return typedQuery.getResultList();
	}

	@Override
	public int count(QueryFilters queryFilters) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		
		Root<T> from = countQuery.from(cl);
		countQuery.select(cb.count(from)).where(asPredicate(cb, from, queryFilters));
		Long count = em.createQuery(countQuery).getSingleResult();		
        return count.intValue();
	}

}
