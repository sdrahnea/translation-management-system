package com.tms.controller.entity;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import org.primefaces.event.RowEditEvent;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
public class Controller<T> implements Serializable {

    @PersistenceContext(unitName = "persistenceUnit")
    private EntityManager em;

    private String name;

    private List<T> entityList = new LinkedList<>();

    @PostConstruct
    public void init() {
        refreshEntityList();
    }

    @Transactional
    public void addNewEntity() {
        try {
            em.createNativeQuery("INSERT INTO " + getTableName(getClassName()) + " (name) VALUES(:name)").setParameter("name", name).executeUpdate();
        } catch (Exception ex) {
            System.out.println(">>>" + ex);
        }
        this.name = "";
        refreshEntityList();
    }
    
    public void refreshEntityList() {
        try {
            this.entityList = em.createQuery("FROM " + getClassName() + " entity").getResultList();
        } catch (Exception ex) {

        }
    }

    private String getClassName() {
        String[] className = ("" + ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).split("\\.");
        return className[className.length - 1];
    }

    private String getTableName(final String className) {
        String entityTableName = null;
        Metamodel metamodel = em.getMetamodel();
        for (EntityType<?> e : metamodel.getEntities()) {
            Class<?> entityClass = e.getJavaType();
            if (entityClass.getSimpleName().equalsIgnoreCase(className)) {
                entityTableName = entityClass.getAnnotation(Table.class).name();
            }
        }
        return entityTableName;
    }

    @Transactional
    public void onRowEdit(RowEditEvent event) {
        T object = (T) event.getObject();
        em.merge(object);

        refreshEntityList();
        
        FacesMessage msg = new FacesMessage("Edited", "Record was modified successfully!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    @Transactional
    public void onRowCancel(RowEditEvent event) {
        T object = (T) event.getObject();
        em.remove(em.contains(object) ? object : em.merge(object));
        
        refreshEntityList();
        
        FacesMessage msg = new FacesMessage("Deleted", "Record was deleted successfully!");
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public List<T> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<T> entityList) {
        this.entityList = entityList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
