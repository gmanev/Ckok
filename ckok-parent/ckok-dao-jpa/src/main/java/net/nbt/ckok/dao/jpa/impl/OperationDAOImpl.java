package net.nbt.ckok.dao.jpa.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ListJoin;
import javax.persistence.criteria.Root;

import net.nbt.ckok.model.Customer_;
import net.nbt.ckok.model.Operation;
import net.nbt.ckok.model.Operation_;
import net.nbt.ckok.model.Product;
import net.nbt.ckok.model.Product_;
import net.nbt.ckok.model.dao.OperationDAO;

public class OperationDAOImpl extends GenericDAOImpl<Operation> implements OperationDAO {

	public OperationDAOImpl() {
		super(Operation.class);
	}

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
	}
}
