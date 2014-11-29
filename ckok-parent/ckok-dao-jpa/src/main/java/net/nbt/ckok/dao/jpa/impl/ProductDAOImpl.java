package net.nbt.ckok.dao.jpa.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.nbt.ckok.model.Product;
import net.nbt.ckok.model.ProductType_;
import net.nbt.ckok.model.Product_;
import net.nbt.ckok.model.dao.ProductDAO;
import net.nbt.ckok.service.OrderBy;

public class ProductDAOImpl extends GenericDAOImpl<Product> implements ProductDAO  {
	
	public ProductDAOImpl() {
		super(Product.class);
	}

	@Override
	public List<Product> quickSearch(int startIndex, int count, String searchString, OrderBy orderBy) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Product> q = cb.createQuery(Product.class);

		Root<Product> p = q.from(Product.class);
		CriteriaQuery<Product> select = q.select(p);

		select.where(quickSearchWhere(cb, p, searchString));

		if (orderBy != null) {
			Path<?> field;
			String name = orderBy.getAttributeName();
			if (name.equalsIgnoreCase("productType.name")) {
				field = p.get(Product_.productType).get(ProductType_.name);
			}
			else if (name.equalsIgnoreCase("productType.partnum")) {
				field = p.get(Product_.productType).get(ProductType_.partnum);
			}
			else {
				field = p.get(name);
			}
			if (orderBy.isAscending()) {
				select.orderBy(cb.asc(field));				
			}
			else {
				select.orderBy(cb.desc(field));
			}
		}

		TypedQuery<Product> typedQuery = em.createQuery(select);
		typedQuery.setFirstResult(startIndex);
		typedQuery.setMaxResults(count);
        return typedQuery.getResultList();
	}

	@Override
	public int quickSearchCount(String searchString) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		
		Root<Product> from = countQuery.from(Product.class);
		countQuery.select(cb.count(from));
		countQuery.where(quickSearchWhere(cb, from, searchString));
		Long count = em.createQuery(countQuery).getSingleResult();		
        return count.intValue();		
	}

	private Predicate quickSearchWhere(CriteriaBuilder cb, Root<Product> p, String searchString) {
		String pattern = "%" + searchString.toLowerCase() + "%";		
		return cb.or(
				cb.like(cb.lower(p.get(Product_.productType).get(ProductType_.name)), pattern),
				cb.like(cb.lower(p.get(Product_.productType).get(ProductType_.partnum)), pattern),
				cb.like(cb.lower(p.get(Product_.serial)), pattern),						
				cb.like(cb.lower(p.get(Product_.notes)), pattern),
				cb.like(cb.lower(p.get(Product_.supplier)), pattern)					
			);
	}
}
