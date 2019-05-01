/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Client;
import com.tms.model.entity.ClientToContactPerson;
import com.tms.model.entity.Person;
/**
 *
 * @author sdrahnea
 */
public interface ClientToContactPersonDao extends EntityDao<ClientToContactPerson> {
    
    public void removeBy(Client client);
    
    public ClientToContactPerson findBy(Client client, Person person);
    
}
