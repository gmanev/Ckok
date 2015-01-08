package net.nbt.ckok.dao.jpa.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;

import net.nbt.ckok.model.ProductType;
import net.nbt.ckok.model.ProductType_;
import net.nbt.ckok.model.dao.ProductTypeDAO;
import net.nbt.ckok.service.GetProductTypes;
import net.nbt.ckok.service.GetProductTypesCount;
import net.nbt.ckok.service.OrderBy;

public class ProductTypeDAOImpl extends GenericDAOImpl<ProductType> implements
		ProductTypeDAO {

	public ProductTypeDAOImpl() {
		super(ProductType.class);
	}

	protected void setWhere(CriteriaBuilder cb, CriteriaQuery<?> cq, Root<ProductType> p, String searchString) {
		if (searchString != null && !searchString.equals("")) {
			String pattern = "%" + searchString.toLowerCase() + "%";
			cq.where(cb.or(
				cb.like(cb.lower(p.get(ProductType_.name)), pattern),
				cb.like(cb.lower(p.get(ProductType_.partnum)), pattern)
			));
		}
	}
	
	@Override
	public List<ProductType> getProductTypes(GetProductTypes parameters) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<ProductType> cq = cb.createQuery(ProductType.class);

		Root<ProductType> p = cq.from(ProductType.class);
		
		setWhere(cb, cq, p, parameters.getSearchString());

		OrderBy orderBy = parameters.getOrderBy();
		if (orderBy != null) {
			List<Order> o = new ArrayList<Order>();
			Path<?> field = p.get(orderBy.getAttributeName());
			o.add(parameters.getOrderBy().isAscending() ?
					cb.asc(field) : cb.desc(field));
			o.add(cb.asc(p.get(ProductType_.id)));
			cq.orderBy(o);
		}
		
		return em.createQuery(cq)
				.setFirstResult(parameters.getStartIndex())
				.setMaxResults(parameters.getCount())
				.getResultList();
	}

	@Override
	public int getProductTypesCount(GetProductTypesCount parameters) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
		
		Root<ProductType> from = countQuery.from(ProductType.class);
		countQuery.select(cb.count(from));
		setWhere(cb, countQuery, from, parameters.getSearchString());

		Long count = em.createQuery(countQuery).getSingleResult();		
        return count.intValue();		
	}
	
}
