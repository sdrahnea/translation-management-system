/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Client;
import com.tms.model.entity.Project;
import com.tms.model.entity.Translator;
import java.util.List;

/**
 *
 * @author sdrahnea
 */
public interface ProjectDao extends EntityDao<Project> {
    
    public List<Project> getNotInvoicedOrArchivied();
    
    public List<Project> getNotInvoicedOrArchivied(final Client client);
    
    public List<Project> getNotInvoicedOrArchivied(final Translator translators);
    
    public List<Project> getInvoived();
    
    public List<Project> getArchived();
    
    public List<Project> getProjects(Client client);
    
}