/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Translator;
import com.tms.model.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class TranslatorDaoImp extends EntityDaoImp<Translator> implements TranslatorDao {

    @Override
    public Translator getTranslator(final User user) {
        return findByProperty("user.id", user.getId()).get(0);
    }
}
