/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Currency;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class CurrencyDaoImp extends EntityDaoImp<Currency> implements CurrencyDao {

//    public Currency saveIfNotExist(String name) {
//        Currency entity = findByName(name);
//        if (entity == null) {
//            entity = new Currency(name);
//            saveOrUpdate(entity);
//        } 
//        return entity;
//    }
    
}
