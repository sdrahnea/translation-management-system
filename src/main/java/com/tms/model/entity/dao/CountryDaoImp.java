/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Country;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class CountryDaoImp extends EntityDaoImp<Country> implements CountryDao {

    @Override
    public List<Country> findByName(final String name) {
        return entityManager.createQuery("FROM Country c WHERE lower(c.name) = lower(:name)")
                .setParameter("name", name)
                .getResultList();
    }

}
