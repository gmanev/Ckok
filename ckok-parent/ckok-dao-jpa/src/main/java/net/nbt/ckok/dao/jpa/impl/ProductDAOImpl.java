package net.nbt.ckok.dao.jpa.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import net.nbt.ckok.model.Operation;
import net.nbt.ckok.model.Operation_;
import net.nbt.ckok.model.Product;
import net.nbt.ckok.model.ProductType_;
import net.nbt.ckok.model.Product_;
import net.nbt.ckok.model.dao.ProductDAO;
import net.nbt.ckok.service.ProductsSearch;
import net.nbt.ckok.service.ProductsSearchCount;

public class ProductDAOImpl extends GenericDAOImpl<Product> implements ProductDAO  {
	
	public ProductDAOImpl() {
		super(Product.class);
	}

	@Override
	public List<Product> search(ProductsSearch params) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Product> q = cb.createQuery(Product.class);

		Root<Product> p = q.from(Product.class);
		CriteriaQuery<Product> select = q.select(p);

		if (params.getOperation() != null) {
			Subquery<Integer> sq = select.subquery(Integer.class);
			Root<Operation> operation = sq.from(Operation.class);
			ListJoin<Operation, Product> sqProd = operation.join(Operation_.products);
			sq.select(sqProd.get(Product_.id)).where(
					cb.equal(operation.get(Operation_.id), params.getOperation()));
			select.where(cb.in(p.get(Product_.id)).value(sq));
		}
		else {
			select.where(quickSearchWhere(cb, p, 
					params.getLast(), params.getSearchString()));
		}


		if (params.getOrderBy() != null) {
			Path<?> field;
			String name = params.getOrderBy().getAttributeName();
			if (name.equalsIgnoreCase("productType.name")) {
				field = p.get(Product_.productType).get(ProductType_.name);
			}
			else if (name.equalsIgnoreCase("productType.partnum")) {
				field = p.get(Product_.productType).get(ProductType_.partnum);
			}
			else {
				field = p.get(name);
			}
			if (params.getOrderBy().isAscending()) {
				select.orderBy(cb.asc(field));				
			}
			else {
				select.orderBy(cb.desc(field));
			}
		}

		TypedQuery<Product> typedQuery = em.createQuery(select);
		typedQuery.setFirstResult(params.getStartIndex());
		typedQuery.setMaxResults(params.getCount());
        return typedQuery.getResultList();
	}

	@Override
	public int searchCount(ProductsSearchCount params) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		
		Root<Product> from = countQuery.from(Product.class);
		countQuery.select(cb.count(from));

		if (params.getOperation() != null) {
			Subquery<Integer> sq = countQuery.subquery(Integer.class);
			Root<Operation> operation = sq.from(Operation.class);
			ListJoin<Operation, Product> sqProd = operation.join(Operation_.products);
			sq.select(sqProd.get(Product_.id)).where(
					cb.equal(operation.get(Operation_.id), params.getOperation()));
			countQuery.where(cb.in(from.get(Product_.id)).value(sq));
		}
		else {
			countQuery.where(quickSearchWhere(cb, from, 
				params.getLast(), params.getSearchString()));
		}

		Long count = em.createQuery(countQuery).getSingleResult();		
        return count.intValue();		
	}

	private Predicate quickSearchWhere(CriteriaBuilder cb, Root<Product> p, Integer last, String searchString) {
		String pattern = "%" + searchString.toLowerCase() + "%";
		Predicate searchPredicate =  cb.or(
				cb.like(cb.lower(p.get(Product_.productType).get(ProductType_.name)), pattern),
				cb.like(cb.lower(p.get(Product_.productType).get(ProductType_.partnum)), pattern),
				cb.like(cb.lower(p.get(Product_.serial)), pattern),						
				cb.like(cb.lower(p.get(Product_.notes)), pattern),
				cb.like(cb.lower(p.get(Product_.supplier)), pattern)					
			);
		return (last == null) ?
				searchPredicate : cb.and(
						cb.equal(p.get(Product_.last), last.intValue()), 
						searchPredicate);
	}
}
