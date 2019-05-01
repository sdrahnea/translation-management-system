/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.TranslationArea;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class TranslationAreaDaoImp extends EntityDaoImp<TranslationArea> implements TranslationAreaDao {
    
//    public TranslationArea saveIfNotExist(String name) {
//        TranslationArea entity = findByName(name);
//        if (entity == null) {
//            entity = new TranslationArea(name);
//            saveOrUpdate(entity);
//        } 
//        return entity;
//    }
    
    
}
