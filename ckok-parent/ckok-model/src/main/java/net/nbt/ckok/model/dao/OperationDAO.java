package net.nbt.ckok.model.dao;

import java.util.List;

import net.nbt.ckok.model.Operation;

public interface OperationDAO extends GenericDAO<Operation> {

	public List<Operation> getProductOperations(int productId);
	public List<Operation> getCustomerOperations(int customerId);

}
