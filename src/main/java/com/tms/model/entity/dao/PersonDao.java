/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Person;
import java.util.List;

/**
 *
 * @author sdrahnea
 */
public interface PersonDao extends EntityDao<Person>  {
    
    public List<Person> findAllManagers();
    
}
