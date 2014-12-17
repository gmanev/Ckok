package net.nbt.ckok.service.impl;

import net.nbt.ckok.model.dao.CustomerDAO;
import net.nbt.ckok.model.dao.OperationDAO;
import net.nbt.ckok.model.dao.ProductDAO;
import net.nbt.ckok.service.CkokService;
import net.nbt.ckok.service.CustomersQuickSearch;
import net.nbt.ckok.service.CustomersQuickSearchCount;
import net.nbt.ckok.service.CustomersQuickSearchCountResponse;
import net.nbt.ckok.service.CustomersQuickSearchResponse;
import net.nbt.ckok.service.DeleteProductById;
import net.nbt.ckok.service.GetAllProducts;
import net.nbt.ckok.service.GetAllProductsResponse;
import net.nbt.ckok.service.GetProductById;
import net.nbt.ckok.service.GetProductByIdResponse;
import net.nbt.ckok.service.GetProducts;
import net.nbt.ckok.service.GetProductsResponse;
import net.nbt.ckok.service.NoSuchProductException;
import net.nbt.ckok.service.OperationsSearch;
import net.nbt.ckok.service.OperationsSearchCount;
import net.nbt.ckok.service.OperationsSearchCountResponse;
import net.nbt.ckok.service.OperationsSearchResponse;
import net.nbt.ckok.service.ProductsSearch;
import net.nbt.ckok.service.ProductsSearchCount;
import net.nbt.ckok.service.ProductsSearchCountResponse;
import net.nbt.ckok.service.ProductsSearchResponse;
import net.nbt.ckok.service.UpdateProduct;
import net.nbt.ckok.service.UpdateProductResponse;

public class CkokServiceImpl implements CkokService {

	private ProductDAO productDAO;
	private OperationDAO operationDAO;
	private CustomerDAO customerDAO;
	
	public void setOperationDAO(OperationDAO dao) {
		this.operationDAO = dao;
	}

	public void setProductDAO(ProductDAO dao) {
		this.productDAO = dao;
	}

	public void setCustomerDAO(CustomerDAO dao) {
		this.customerDAO = dao;
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

	public ProductsSearchResponse productsSearch(ProductsSearch parameters) {
		ProductsSearchResponse response = new ProductsSearchResponse();
		response.setReturn(productDAO.search(parameters));
		return response;
	}

	public ProductsSearchCountResponse productsSearchCount(ProductsSearchCount parameters) {
		ProductsSearchCountResponse response = new ProductsSearchCountResponse();
		response.setCount(productDAO.searchCount(parameters));
		return response;
	}

	public CustomersQuickSearchResponse customersQuickSearch(
			CustomersQuickSearch parameters) {
		CustomersQuickSearchResponse response = 
				new CustomersQuickSearchResponse();
		response.setReturn(customerDAO.quickSearch(
				parameters.getStartIndex(),
				parameters.getCount(),
				parameters.getSearchString(),
				parameters.getOrderBy()));
		return response;
	}

	public CustomersQuickSearchCountResponse customersQuickSearchCount(CustomersQuickSearchCount parameters) {
		CustomersQuickSearchCountResponse response = new CustomersQuickSearchCountResponse();
		response.setCount(customerDAO.quickSearchCount(
				parameters.getSearchString()));
		return response;
	}

	public OperationsSearchResponse operationsSearch(OperationsSearch parameters) {
		OperationsSearchResponse response = new OperationsSearchResponse();
		response.setReturn(operationDAO.search(parameters));
		return response;
	}

	public OperationsSearchCountResponse operationsSearchCount(OperationsSearchCount parameters) {
		OperationsSearchCountResponse response = new OperationsSearchCountResponse();
		response.setCount(operationDAO.searchCount(parameters));
		return response;
	}

}
