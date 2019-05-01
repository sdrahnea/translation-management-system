/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.PaymentMethod;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class PaymentMethodDaoImp extends EntityDaoImp<PaymentMethod> implements PaymentMethodDao {

//    public PaymentMethod saveIfNotExist(String name) {
//        PaymentMethod entity = findByName(name);
//        if (entity == null) {
//            entity = new PaymentMethod(name);
//            saveOrUpdate(entity);
//        } 
//        return entity;
//    }
}
