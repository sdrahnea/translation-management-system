package com.tms.model.entity.dao;

import com.tms.util.entity.Property;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javassist.bytecode.SignatureAttribute.TypeVariable;
import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

public class EntityDaoImp<E> implements EntityDao<E> {

    @PersistenceContext(unitName = "persistenceUnit")
    protected EntityManager entityManager;

    protected E instance;
    private Class<E> entityClass;

    @Transactional
    public void persist(E e) {
        getEntityManager().persist(e);
    }

    @Transactional
    public E merge(E e) {
        return getEntityManager().merge(e);
    }

    @Transactional
    public void remove(Object id) {
        getEntityManager().remove((E) getEntityManager().find(getEntityClass(), id));
    }

    @Transactional(readOnly = true)
    public E findById(Object id) {
        return (E) getEntityManager().find(getEntityClass(), id);
    }

    @Override
    public E mergerByName(String name) {
        try {
        E entity = getEntityClass().newInstance();
        } catch(Exception ex){}
        return null;
    }

    @Transactional(readOnly = true)
    public List<E> findAll() {
        try {
            return getEntityManager().createQuery("Select t from " + getEntityClass().getSimpleName() + " t").getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<E> findByProperty(String prop, Object val) {
        try {
            return (List<E>) getEntityManager().createQuery("select x from " + getEntityClass().getSimpleName() + " x where x." + prop + " = ?1").setParameter(1, val).getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<E> find(final String lo, Property... property) {
        try {
            StringBuilder hsqlQuery = new StringBuilder("select x from ")
                    .append(getEntityClass().getSimpleName())
                    .append(" x where (1 = 1) ");

            Map<String, Object> pars = new HashMap<>();
            for (Property prop : property) {
                pars.put(prop.getKey().replaceAll("\\.", ""), prop.getValue());
                hsqlQuery.append(lo);
                hsqlQuery.append(" (x.");
                hsqlQuery.append(prop.getKey());
                hsqlQuery.append(" = :");
                hsqlQuery.append(prop.getKey().replaceAll("\\.", ""));
                hsqlQuery.append(") ");
            }

            Query query = entityManager.createQuery(hsqlQuery.toString());

            for (Parameter p : query.getParameters()) {
                query.setParameter(p, pars.get(p.getName()));
            }
            return (List<E>) query.getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    public List<E> findByName(final String val) {
        try {
            return (List<E>) getEntityManager().createQuery("select x from " + getEntityClass().getSimpleName() + " x where lower(x.name) = lower(?1)").setParameter(1, val).getResultList();
        } catch (Exception ex) {
            return null;
        }
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    public List<E> findInRange(int firstResult, int maxResults) {
        return getEntityManager().createQuery("Select t from " + getEntityClass().getSimpleName() + " t").setFirstResult(firstResult).setMaxResults(maxResults).getResultList();
    }

    @Transactional(readOnly = true)
    public long count() {
        return (Long) getEntityManager().createQuery("Select count(t) from " + getEntityClass().getSimpleName() + " t").getSingleResult();
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }

    public void setEntityManager(EntityManager entityManager) throws Exception {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public Class<E> getEntityClass() {
        if (entityClass == null) {
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                ParameterizedType paramType = (ParameterizedType) type;
                if (paramType.getActualTypeArguments().length == 2) {
                    if (paramType.getActualTypeArguments()[1] instanceof TypeVariable) {
                        throw new IllegalArgumentException(
                                "Can't find class using reflection");
                    } else {
                        entityClass = (Class<E>) paramType.getActualTypeArguments()[1];
                    }
                } else {
                    entityClass = (Class<E>) paramType.getActualTypeArguments()[0];
                }
            } else {
                throw new RuntimeException("Can't find class using reflection");
            }
        }
        return entityClass;
    }

}
