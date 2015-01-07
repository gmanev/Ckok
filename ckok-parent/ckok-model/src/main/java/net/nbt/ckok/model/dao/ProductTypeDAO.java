package net.nbt.ckok.model.dao;

import java.util.List;

import net.nbt.ckok.model.ProductType;
import net.nbt.ckok.service.GetProductTypes;
import net.nbt.ckok.service.GetProductTypesCount;

public interface ProductTypeDAO extends GenericDAO<ProductType> {

	public List<ProductType> getProductTypes(GetProductTypes parameters);
	public int getProductTypesCount(GetProductTypesCount parameters);

}
