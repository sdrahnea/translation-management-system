/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Translator;
import com.tms.model.entity.User;

/**
 *
 * @author sdrahnea
 */
public interface TranslatorDao extends EntityDao<Translator> {
    
    public Translator getTranslator(final User user);
    
}
