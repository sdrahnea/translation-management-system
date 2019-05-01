/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.ServiceProvided;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class ServiceProvidedDaoImp extends EntityDaoImp<ServiceProvided> implements ServiceProvidedDao {

//    public ServiceProvided saveIfNotExist(String name) {
//        ServiceProvided entity = findByName(name);
//        if (entity == null) {
//            entity = new ServiceProvided(name);
//            saveOrUpdate(entity);
//        } 
//        return entity;
//    }
//    
    
}
