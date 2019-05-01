/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Client;
import com.tms.model.entity.Project;
import com.tms.model.entity.Translator;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class ProjectDaoImp extends EntityDaoImp<Project> implements ProjectDao {

    @Override
    public List<Project> getNotInvoicedOrArchivied() {
        return entityManager.createQuery("FROM Project p WHERE not(p.status.name = 'ARCHIVED' OR p.status.name = 'INVOICED') ORDER BY p.id DESC")
                .getResultList();
    }

    @Override
    public List<Project> getNotInvoicedOrArchivied(final Client client) {
        return entityManager.createQuery("FROM Project p WHERE not(p.status.name = 'ARCHIVED' OR p.status.name = 'INVOICED') AND p.client.id = :clientId  ORDER BY p.id DESC")
                .setParameter("clientId", client.getId())
                .getResultList();
    }

    @Override
    public List<Project> getNotInvoicedOrArchivied(final Translator translator) {
        List<Project> resultList = new LinkedList<>();
        for (Project project : getNotInvoicedOrArchivied()) {

        }
        return resultList;
    }

    @Override
    public List<Project> getInvoived() {
        return entityManager.createQuery("FROM Project p WHERE p.status.name = 'INVOICED' OR p.status.name = 'PAID' ORDER BY p.id DESC")
                .getResultList();
    }

    @Override
    public List<Project> getArchived() {
        return entityManager.createQuery("FROM Project p WHERE p.status.name = 'ARCHIVED' ORDER BY p.id DESC")
                .getResultList();
    }

    @Override
    public List<Project> getProjects(Client client) {
        return entityManager.createQuery("FROM Project p WHERE p.client.id = :clientId  ORDER BY p.id DESC")
                .setParameter("clientId", client.getId())
                .getResultList();
    }

}
