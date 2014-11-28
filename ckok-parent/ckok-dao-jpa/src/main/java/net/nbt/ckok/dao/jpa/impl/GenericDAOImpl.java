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
import net.nbt.ckok.service.MatchAnyFilter;
import net.nbt.ckok.service.OrderBy;
import net.nbt.ckok.service.QueryFilter;
import net.nbt.ckok.service.StringFilter;

public class GenericDAOImpl<T> implements GenericDAO<T> {

    protected EntityManager em;
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

	private Predicate createFrom(CriteriaBuilder cb, Root<T> from, QueryFilter filter) {
		if (filter instanceof MatchAnyFilter) {
			List<Predicate> predicates = new ArrayList<Predicate>();
			for (QueryFilter f : ((MatchAnyFilter) filter).getFilters()) {
				predicates.add(createFrom(cb, from, f));
			}
			return cb.or(predicates.toArray(new Predicate[]{}));
		}
		else if (filter instanceof StringFilter) {
			StringFilter stringFilter = (StringFilter) filter;
			Expression<String> ex = from.get(stringFilter.getAttributeName()).as(String.class);
			if (stringFilter.isOnlyMatchPrefix()) {
				return stringFilter.isIgnoreCase() ?
						cb.like(cb.lower(ex), stringFilter.getFilterString().toLowerCase() + "%") :
						cb.like(ex, stringFilter.getFilterString() + "%");
			}
			else {
				return stringFilter.isIgnoreCase() ?
						cb.like(cb.lower(ex), "%" + stringFilter.getFilterString().toLowerCase() + "%") :
						cb.like(ex, "%" + stringFilter.getFilterString() + "%");
			}
		}
		// everything else
		return null;
	}

	@Override
	public List<T> get(int startIndex, int count, QueryFilter filter, List<OrderBy> sort) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<T> q = cb.createQuery(cl);

		Root<T> from = q.from(cl);
		CriteriaQuery<T> select = q.select(from);

		select.where(createFrom(cb, from, filter));
		
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
	public int count(QueryFilter filter) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		
		Root<T> from = countQuery.from(cl);
		countQuery.select(cb.count(from)).where(createFrom(cb, from, filter));
		Long count = em.createQuery(countQuery).getSingleResult();		
        return count.intValue();
	}

}
