/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.TranslationArea;
import com.tms.model.entity.Translator;
import com.tms.model.entity.TranslatorToTranslationArea;

/**
 *
 * @author sdrahnea
 */
public interface TranslatorToTranslateAreaDao extends EntityDao<TranslatorToTranslationArea> {
    
    public void removeBy(Translator translator);
    
    public TranslatorToTranslationArea find(Translator translator, TranslationArea translationArea);
    
}
