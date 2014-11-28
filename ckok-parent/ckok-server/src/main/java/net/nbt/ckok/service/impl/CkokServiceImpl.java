package net.nbt.ckok.service.impl;

import net.nbt.ckok.model.dao.ProductDAO;
import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.service.DeleteProductById;
import net.nbt.ckok.service.GetAllProducts;
import net.nbt.ckok.service.GetAllProductsResponse;
import net.nbt.ckok.service.GetProductById;
import net.nbt.ckok.service.GetProductByIdResponse;
import net.nbt.ckok.service.GetProducts;
import net.nbt.ckok.service.GetProductsResponse;
import net.nbt.ckok.service.NoSuchProductException;
import net.nbt.ckok.service.ProductsQuickSearch;
import net.nbt.ckok.service.ProductsQuickSearchCount;
import net.nbt.ckok.service.ProductsQuickSearchCountResponse;
import net.nbt.ckok.service.ProductsQuickSearchResponse;
import net.nbt.ckok.service.UpdateProduct;
import net.nbt.ckok.service.UpdateProductResponse;

public class CkokServiceImpl implements CkokService {

	private ProductDAO productDAO;
	
	public void setProductDAO(ProductDAO dao) {
		this.productDAO = dao;
	}
	
	public void deleteProductById(DeleteProductById parameters) {
		// TODO Auto-generated method stub

	}

	public GetProductByIdResponse getProductById(GetProductById parameters)
			throws NoSuchProductException {
		GetProductByIdResponse response = new GetProductByIdResponse();
		response.setReturn(productDAO.find(parameters.getProductId()));
		return response;
	}

	public UpdateProductResponse updateProduct(UpdateProduct parameters) {
		UpdateProductResponse response = new UpdateProductResponse();
		productDAO.update(parameters.getProduct());
		return response;
	}

	public GetAllProductsResponse getAllProducts(GetAllProducts parameters) {
		GetAllProductsResponse response = new GetAllProductsResponse();
		response.setReturn(productDAO.getAll());
		return response;
	}

	public GetProductsResponse getProducts(GetProducts parameters) {
		GetProductsResponse r = new GetProductsResponse();
		r.setReturn(productDAO.get(parameters.getStartIndex(), parameters.getCount(), parameters.getFilter(), parameters.getSort()));
		return r;
	}

	public ProductsQuickSearchResponse productsQuickSearch(
			ProductsQuickSearch parameters) {
		ProductsQuickSearchResponse response = new ProductsQuickSearchResponse();
		response.setReturn(productDAO.quickSearch(
				parameters.getStartIndex(),
				parameters.getCount(),
				parameters.getSearchString(),
				parameters.getOrderBy()));
		return response;
	}

	public ProductsQuickSearchCountResponse productsQuickSearchCount(
			ProductsQuickSearchCount parameters) {
		ProductsQuickSearchCountResponse response = new ProductsQuickSearchCountResponse();
		response.setCount(productDAO.quickSearchCount(parameters.getSearchString()));
		return response;
	}

}
