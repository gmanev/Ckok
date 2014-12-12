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

	@Override
	public List<Operation> search(int startIndex, int count,
			Integer optype, Integer product, Integer customer, String searchString, OrderBy orderBy) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Operation> cq = cb.createQuery(Operation.class);

		Root<Operation> from = cq.from(Operation.class);
		cq.where(quickSearchWhere(cb, from, optype, product, customer, searchString));

		if (orderBy != null) {
			Path<?> field;
			String name = orderBy.getAttributeName();
			if (name.equalsIgnoreCase("customer.name")) {
				field = from.get(Operation_.customer).get(Customer_.name);
			}
			else {
				field = from.get(name);
			}
			if (orderBy.isAscending()) {
				cq.orderBy(cb.asc(field));				
			}
			else {
				cq.orderBy(cb.desc(field));
			}
		}

		TypedQuery<Operation> typedQuery = em.createQuery(cq);
		typedQuery.setFirstResult(startIndex);
		typedQuery.setMaxResults(count);
        List<Operation> resultList = typedQuery.getResultList();
        return resultList;
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

		Predicate result =  cb.like(cb.lower(p.get(Operation_.customer).get(Customer_.name)), pattern);

		if (customer != null) {
			result = cb.equal(p.get(Operation_.customer).get(Customer_.id), customer.intValue());
		}
		
		if (optype != null) {
			result = cb.and(
				cb.equal(p.get(Operation_.optype), optype.intValue()), 
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
