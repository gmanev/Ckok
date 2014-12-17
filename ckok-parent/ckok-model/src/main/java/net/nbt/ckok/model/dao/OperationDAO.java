package net.nbt.ckok.model.dao;

import java.util.List;

import net.nbt.ckok.model.Operation;
import net.nbt.ckok.service.OperationsSearch;
import net.nbt.ckok.service.OperationsSearchCount;

public interface OperationDAO extends GenericDAO<Operation> {

	public List<Operation> search(OperationsSearch params);
	public int searchCount(OperationsSearchCount params);

}
