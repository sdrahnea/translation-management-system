/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Client;
import com.tms.model.entity.User;

/**
 *
 * @author sdrahnea
 */
public interface ClientDao extends EntityDao<Client>{
    
    public Client getClient(final User user);
    
}