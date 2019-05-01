/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Cat;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class CatDaoImp extends EntityDaoImp<Cat> implements CatDao {

//    public Cat saveIfNotExist(String name) {
//        Cat entity = findByName(name);
//        if (entity == null) {
//            entity = new Cat(name);
//            saveOrUpdate(entity);
//        } 
//        return entity;
//    }

}