/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.PersonType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class PersonTypeDaoImp extends EntityDaoImp<PersonType> implements PersonTypeDao {

    @Override
    public PersonType CONTACT_PERSON() {
        return findByName("CONTACT_PERSON").get(0);
    }

    @Override
    public PersonType SALE_MANAGER() {
        return findByName("SALE_MANAGER").get(0);
    }

    @Override
    public PersonType MANAGER() {
        return findByName("MANAGER").get(0);
    }

}
