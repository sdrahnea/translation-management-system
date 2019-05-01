/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.service;

import com.tms.model.entity.Person;
import com.tms.model.entity.dao.PersonDao;
import java.io.Serializable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Service
public class PersonService implements Serializable {
    
    @Autowired
    private PersonDao personDao;
    
    @Transactional
    public List<Person> getManagerList(){
        return personDao.findAllManagers();
    }
    
}
