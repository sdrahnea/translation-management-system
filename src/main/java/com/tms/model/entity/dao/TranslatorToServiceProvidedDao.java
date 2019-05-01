/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.ServiceProvided;
import com.tms.model.entity.Translator;
import com.tms.model.entity.TranslatorToServcieProvided;

/**
 *
 * @author sdrahnea
 */
public interface TranslatorToServiceProvidedDao extends EntityDao<TranslatorToServcieProvided> {

    public void removeBy(Translator translator);
    
    public TranslatorToServcieProvided find(Translator translator, ServiceProvided serviceProvided);

}
