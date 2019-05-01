/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Cat;
import com.tms.model.entity.Translator;
import com.tms.model.entity.TranslatorToCat;

/**
 *
 * @author sdrahnea
 */
public interface TranslatorToCatDao extends EntityDao<TranslatorToCat> {
    
    public void removeBy(Translator translator);
    
    public TranslatorToCat find(Translator translator, Cat cat);
    
}

