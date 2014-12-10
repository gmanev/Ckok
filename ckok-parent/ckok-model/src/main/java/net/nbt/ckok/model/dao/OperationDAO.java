package net.nbt.ckok.model.dao;

import java.util.List;

import net.nbt.ckok.model.Operation;
import net.nbt.ckok.service.OrderBy;

public interface OperationDAO extends GenericDAO<Operation> {

	public List<Operation> search(int startIndex, int count, Integer optype, Integer product, Integer customer, String searchString, OrderBy orderBy);
	public int searchCount(Integer optype, Integer product, Integer customer, String searchString);

}
