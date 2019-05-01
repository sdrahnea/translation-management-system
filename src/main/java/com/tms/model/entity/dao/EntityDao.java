package com.tms.model.entity.dao;

import com.tms.util.entity.Property;
import java.util.List;
import javax.persistence.EntityManager;

public interface EntityDao<E> {

    void persist(E e);
    
    E mergerByName(final String name);

    E merge(E e);

    void remove(Object id);

    E findById(Object id);

    List<E> findAll();

    List<E> findByProperty(String prop, Object val);
    
    List<E> findByName(final String val);

    List<E> findInRange(int firstResult, int maxResults);

    long count();
    
    public EntityManager getEntityManager();
    /**
     * 
     * @param lo - logical operator one of OR / AND
     * @param property - list of properties which will be part of condition 
     * @return 
     */
    public List<E> find(final String lo, Property... property);

}
