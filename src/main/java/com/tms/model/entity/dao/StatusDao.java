/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Status;
import java.util.List;

/**
 *
 * @author sdrahnea
 */
public interface StatusDao extends EntityDao<Status> {
    
    public Status find(String name);
    
    public List<Status> findProjectStatus();
    
    public Status findByProjectStatusAndReadyToWork();
    
    public Status findByProjectStatusAndAssigned();
    
    public Status READY_TO_WORK();
    
    public Status ASSIGN_TRANSLATOR();
    
    public Status INFORM_TRANSLATOR();
    
    public Status DELIVERED();
    
    public Status PAID();
    
    public Status CLIENT_PAID();
    
    public Status TRANSLATOR_PAID();
    
    public Status INVOICED();
    
}