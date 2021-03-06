package net.nbt.ckok.dao.jpa.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import net.nbt.ckok.model.Customer_;
import net.nbt.ckok.model.Operation;
import net.nbt.ckok.model.Operation_;
import net.nbt.ckok.model.Product;
import net.nbt.ckok.model.ProductDetail;
import net.nbt.ckok.model.ProductType_;
import net.nbt.ckok.model.Product_;
import net.nbt.ckok.model.dao.ProductDAO;
import net.nbt.ckok.service.GetProductDetail;
import net.nbt.ckok.service.GetProductDetailCount;
import net.nbt.ckok.service.ProductsSearch;
import net.nbt.ckok.service.ProductsSearchCount;

public class ProductDAOImpl extends GenericDAOImpl<Product> implements ProductDAO  {
	
	public ProductDAOImpl() {
		super(Product.class);
	}

	@Override
	public List<Product> search(ProductsSearch parameters) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Product> cq = cb.createQuery(Product.class);

		Root<Product> p = cq.from(Product.class);

		if (parameters.getOperation() != null) {
			Subquery<Integer> sq = cq.subquery(Integer.class);
			Root<Operation> operation = sq.from(Operation.class);
			ListJoin<Operation, Product> sqProd = operation.join(Operation_.products);
			sq.select(sqProd.get(Product_.id)).where(
					cb.equal(operation.get(Operation_.id), parameters.getOperation()));
			cq.where(cb.in(p.get(Product_.id)).value(sq));
		}
		else {
			cq.where(quickSearchWhere(cb,
					p, 
					parameters.getLast(),
					parameters.getSearchString()));
		}

		if (parameters.getOrderBy() != null) {
			List<Order> o = new ArrayList<Order>();
			Path<?> field;
			String name = parameters.getOrderBy().getAttributeName();
			if (name.equalsIgnoreCase("productType.name")) {
				field = p.get(Product_.productType).get(ProductType_.name);
			}
			else if (name.equalsIgnoreCase("productType.partnum")) {
				field = p.get(Product_.productType).get(ProductType_.partnum);
			}
			else {
				field = p.get(name);
			}
			o.add(parameters.getOrderBy().isAscending() ?
					cb.asc(field) : cb.desc(field));
			o.add(cb.asc(p.get(Product_.id)));
			cq.orderBy(o);
		}

		return em.createQuery(cq)
				.setFirstResult(parameters.getStartIndex())
				.setMaxResults(parameters.getCount())
				.getResultList();
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
			countQuery.where(quickSearchWhere(cb,
					from,
					params.getLast(),
					params.getSearchString()));
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
	
	@Override
	public List<ProductDetail> getProductDetail(GetProductDetail parameters) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ProductDetail> cq = cb.createQuery(ProductDetail.class);
		Root<Operation> operation = cq.from(Operation.class);
		ListJoin<Operation, Product> opProd = operation.join(Operation_.products);

		Subquery<Integer> sq = cq.subquery(Integer.class);
		Root<Operation> op = sq.from(Operation.class);
		ListJoin<Operation, Product> p = op.join(Operation_.products);
		sq.select(cb.max(op.get(Operation_.id)));
		sq.where(cb.in(p.get(Product_.id)).value(opProd.get(Product_.id)));
		
		cq.where(cb.and(
					cb.equal(opProd.get(Product_.last), parameters.getLast()),
					cb.equal(operation.get(Operation_.id), sq)
				));
		cq.distinct(true);

		if (parameters.getOrderBy() != null) {
			List<Order> o = new ArrayList<Order>();
			Path<?> field = p.get(parameters.getOrderBy().getAttributeName());
			o.add(parameters.getOrderBy().isAscending() ?
					cb.asc(field) : cb.desc(field));
			o.add(cb.asc(p.get(Product_.id)));
			cq.orderBy(o);
		}

		cq.select(cb.construct(ProductDetail.class, 
				opProd.get(Product_.id),
				opProd.get(Product_.quantity),
				opProd.get(Product_.serial),
				opProd.get(Product_.supplier),
				opProd.get(Product_.notes),
				opProd.get(Product_.createdOn),
				opProd.get(Product_.warranty),
				opProd.get(Product_.productType).get(ProductType_.name),
				opProd.get(Product_.productType).get(ProductType_.partnum),
				opProd.get(Product_.productType).get(ProductType_.measure),
				operation.get(Operation_.optype),
				operation.get(Operation_.n),
				operation.get(Operation_.ts),
				operation.get(Operation_.ts2),
				operation.get(Operation_.priel),
				operation.get(Operation_.predal),
				operation.get(Operation_.notes),
				operation.get(Operation_.service),
				operation.get(Operation_.facType),
				operation.get(Operation_.facNum),				
				operation.get(Operation_.customer).get(Customer_.id),
				operation.get(Operation_.customer).get(Customer_.name)
				));

		return em.createQuery(cq)
				.setFirstResult(parameters.getStartIndex())
				.setMaxResults(parameters.getCount())
				.getResultList();
	}

	@Override
	public int getProductDetailCount(GetProductDetailCount parameters) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		
		Root<Product> from = countQuery.from(Product.class);
		countQuery.select(cb.count(from));
		countQuery.where(cb.equal(from.get(Product_.last), parameters.getLast()));

		Long count = em.createQuery(countQuery).getSingleResult();		
        return count.intValue();		

	}
	
}
