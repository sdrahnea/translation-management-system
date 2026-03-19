package com.tms.repository;

import com.tms.model.entity.Client;
import com.tms.model.entity.Project;
import com.tms.model.entity.Translator;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;

@Repository
public interface ProjectRepository extends AbstractRepository<Project, Integer> {

    @Query("FROM Project p WHERE not(p.status.name = 'ARCHIVED' OR p.status.name = 'INVOICED') ORDER BY p.id DESC")
    List<Project> getNotInvoicedOrArchivied();

    @Query("FROM Project p WHERE not(p.status.name = 'ARCHIVED' OR p.status.name = 'INVOICED') AND p.client.id = :clientId ORDER BY p.id DESC")
    List<Project> getNotInvoicedOrArchivied(@Param("clientId") Integer clientId);

    default List<Project> getNotInvoicedOrArchivied(final Client client) {
        return getNotInvoicedOrArchivied(client.getId());
    }

    default List<Project> getNotInvoicedOrArchivied(final Translator translator) {
        return Collections.emptyList();
    }

    @Query("FROM Project p WHERE p.status.name = 'INVOICED' OR p.status.name = 'PAID' ORDER BY p.id DESC")
    List<Project> getInvoived();

    @Query("FROM Project p WHERE p.status.name = 'ARCHIVED' ORDER BY p.id DESC")
    List<Project> getArchived();

    @Query("FROM Project p WHERE p.client.id = :clientId ORDER BY p.id DESC")
    List<Project> getProjects(@Param("clientId") Integer clientId);

    default List<Project> getProjects(final Client client) {
        return getProjects(client.getId());
    }
}

