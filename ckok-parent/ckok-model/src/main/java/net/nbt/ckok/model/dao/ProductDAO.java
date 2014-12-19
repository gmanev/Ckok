package net.nbt.ckok.model.dao;

import java.util.List;

import net.nbt.ckok.model.Product;
import net.nbt.ckok.model.ProductDetail;
import net.nbt.ckok.service.GetProductDetail;
import net.nbt.ckok.service.GetProductDetailCount;
import net.nbt.ckok.service.ProductsSearch;
import net.nbt.ckok.service.ProductsSearchCount;

public interface ProductDAO extends GenericDAO<Product> {

	public List<Product> search(ProductsSearch parameters);
	public int searchCount(ProductsSearchCount parameters);

	public List<ProductDetail> getProductDetail(GetProductDetail parameters);
	public int getProductDetailCount(GetProductDetailCount parameters);

}
