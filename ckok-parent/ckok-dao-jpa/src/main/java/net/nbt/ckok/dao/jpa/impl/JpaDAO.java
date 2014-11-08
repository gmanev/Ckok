package net.nbt.ckok.dao.jpa.impl;

import java.util.Collection;

import javax.persistence.EntityManager;

import net.nbt.ckok.model.DAO;

public class JpaDAO<T> implements DAO<T> {

    private EntityManager em;
    private Class<T> persistentClass;
    
    public JpaDAO(EntityManager em, Class<T> persistentClass) { 
        this.em = em;
        this.persistentClass = persistentClass;
    }
	
	@Override
	public void add(T entity) {
		em.persist(entity);
		em.flush();
	}

	@Override
	public T update(T entity) {
		return em.merge(entity);
	}

	@Override
	public void delete(T entity) {
		em.remove(entity);
	}

	@Override
	public Collection<T> getAll() {
		return em.createQuery("SELECT p FROM " + persistentClass.getSimpleName() + " p", persistentClass).getResultList();
	}

}
