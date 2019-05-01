/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Language;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class LanguageDaoImp extends EntityDaoImp<Language> implements LanguageDao {

    @Override
    public List<Language> findByName(final String name) {
        return entityManager.createQuery("FROM Language l WHERE lower(l.name) = lower(:name)")
                .setParameter("name", name)
                .getResultList();

    }

}
