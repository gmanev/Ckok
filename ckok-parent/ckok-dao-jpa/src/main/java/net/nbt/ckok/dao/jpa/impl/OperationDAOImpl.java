package net.nbt.ckok.dao.jpa.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.nbt.ckok.model.Customer_;
import net.nbt.ckok.model.Operation;
import net.nbt.ckok.model.Operation_;
import net.nbt.ckok.model.Product;
import net.nbt.ckok.model.Product_;
import net.nbt.ckok.model.dao.OperationDAO;
import net.nbt.ckok.service.OrderBy;

public class OperationDAOImpl extends GenericDAOImpl<Operation> implements OperationDAO {

	public OperationDAOImpl() {
		super(Operation.class);
	}
/*
	@Override
	public List<Operation> getProductOperations(int productId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Operation> cq = cb.createQuery(Operation.class);
		
		Root<Operation> operation = cq.from(Operation.class);
		ListJoin<Operation, Product> o = operation.join(Operation_.products);
		cq.where(cb.equal(o.get(Product_.id), productId));
		cq.orderBy(cb.desc(operation.get(Operation_.id)));
		
		TypedQuery<Operation> typedQuery = em.createQuery(cq);
		return typedQuery.getResultList();
	}

	@Override
	public List<Operation> getCustomerOperations(int customerId) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Operation> cq = cb.createQuery(Operation.class);
		
		Root<Operation> operation = cq.from(Operation.class);
		cq.where(cb.equal(operation.get(Operation_.customer).get(Customer_.id), customerId));
		cq.orderBy(cb.desc(operation.get(Operation_.id)));
		
		TypedQuery<Operation> typedQuery = em.createQuery(cq);
		return typedQuery.getResultList();
	}*/

	@Override
	public List<Operation> search(int startIndex, int count,
			Integer optype, Integer product, Integer customer, String searchString, OrderBy orderBy) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Operation> q = cb.createQuery(Operation.class);

		Root<Operation> p = q.from(Operation.class);
		CriteriaQuery<Operation> select = q.select(p);

		select.where(quickSearchWhere(cb, p, optype, product, customer, searchString));

		if (orderBy != null) {
			Path<?> field;
			String name = orderBy.getAttributeName();
			if (name.equalsIgnoreCase("customer.name")) {
				field = p.get(Operation_.customer).get(Customer_.name);
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

		TypedQuery<Operation> typedQuery = em.createQuery(select);
		typedQuery.setFirstResult(startIndex);
		typedQuery.setMaxResults(count);
        return typedQuery.getResultList();
	}

	@Override
	public int searchCount(Integer optype, Integer product, Integer customer, String searchString) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		
		Root<Operation> from = countQuery.from(Operation.class);
		countQuery.where(quickSearchWhere(cb, from, optype, product, customer, searchString));		
		countQuery.select(cb.count(from));
		Long count = em.createQuery(countQuery).getSingleResult();		
        return count.intValue();
	}

	private Predicate quickSearchWhere(CriteriaBuilder cb, Root<Operation> p, Integer optype, Integer product, Integer customer, String searchString) {
		String pattern = "%" + searchString.toLowerCase() + "%";

		Predicate result =  cb.or(
				cb.like(cb.lower(p.get(Operation_.customer).get(Customer_.name)), pattern)
		);
		
		if (optype != null) {
			result = cb.and(
					cb.equal(p.get(Operation_.optype), optype.intValue()), 
					result);
		}

		if (customer != null) {
			result = cb.and(
					cb.equal(p.get(Operation_.customer).get(Customer_.id), customer.intValue()), 
					result);
		}

		if (product != null) {
			ListJoin<Operation, Product> o = p.join(Operation_.products);
			result = cb.and(
					cb.equal(o.get(Product_.id), product),
					result);			
		}
		return result;
	}
	

}
