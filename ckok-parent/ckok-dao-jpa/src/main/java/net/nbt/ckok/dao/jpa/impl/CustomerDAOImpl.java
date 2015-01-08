package net.nbt.ckok.dao.jpa.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import net.nbt.ckok.model.Customer;
import net.nbt.ckok.model.Customer_;
import net.nbt.ckok.model.dao.CustomerDAO;
import net.nbt.ckok.service.OrderBy;

public class CustomerDAOImpl extends GenericDAOImpl<Customer> implements CustomerDAO {

	public CustomerDAOImpl() {
		super(Customer.class);
	}

	@Override
	public List<Customer> quickSearch(int startIndex, int count,
			String searchString, OrderBy orderBy) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Customer> cq = cb.createQuery(Customer.class);

		Root<Customer> p = cq.from(Customer.class);
		cq.where(quickSearchWhere(cb, p, searchString));

		if (orderBy != null) {
			List<Order> o = new ArrayList<Order>();
			Path<?> field;
			String name = orderBy.getAttributeName();
			field = p.get(name);
			o.add(orderBy.isAscending() ?
					cb.asc(field) : cb.desc(field));			
			o.add(cb.asc(p.get(Customer_.id)));
			cq.orderBy(o);
		}

		return em.createQuery(cq)
				.setFirstResult(startIndex)
				.setMaxResults(count)
				.getResultList();
	}

	@Override
	public int quickSearchCount(String searchString) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		
		Root<Customer> from = countQuery.from(Customer.class);
		countQuery.select(cb.count(from));
		countQuery.where(quickSearchWhere(cb, from, searchString));
		Long count = em.createQuery(countQuery).getSingleResult();		
        return count.intValue();	
	}

	private Predicate quickSearchWhere(CriteriaBuilder cb, Root<Customer> p, String searchString) {
		if (searchString.startsWith("id-")) {
			return cb.equal(p.get(Customer_.id), Integer.valueOf(searchString.substring(3)));
		}
		String pattern = "%" + searchString.toLowerCase() + "%";		
		return cb.like(cb.lower(p.get(Customer_.name)), pattern);
	}


}
