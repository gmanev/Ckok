package net.nbt.ckok.dao.jpa.impl;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
			Path<?> field = p.get(orderBy.getAttributeName());
			if (parameters.getOrderBy().isAscending()) {
				cq.orderBy(cb.asc(field));				
			}
			else {
				cq.orderBy(cb.desc(field));
			}
		}
		
		TypedQuery<ProductType> typedQuery = em.createQuery(cq);
		typedQuery.setFirstResult(parameters.getStartIndex());
		typedQuery.setMaxResults(parameters.getCount());
        return typedQuery.getResultList();
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
