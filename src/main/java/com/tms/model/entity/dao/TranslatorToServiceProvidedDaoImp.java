/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.ServiceProvided;
import com.tms.model.entity.Translator;
import com.tms.model.entity.TranslatorToServcieProvided;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class TranslatorToServiceProvidedDaoImp extends EntityDaoImp<TranslatorToServcieProvided> implements TranslatorToServiceProvidedDao {

    @Override
    @Transactional(readOnly = true)
    public TranslatorToServcieProvided find(Translator translator, ServiceProvided serviceProvided) {
        String query = "FROM TranslatorToServcieProvided ttsp "
                + " WHERE ttsp.translator.id = :translatorId AND ttsp.serviceProvided.id = :serviceProvidedId";
        List<TranslatorToServcieProvided> result = entityManager.createQuery(query)
                .setParameter("translatorId", translator.getId())
                .setParameter("serviceProvidedId", serviceProvided.getId())
                .getResultList();
        return result.size() > 0 ? result.get(0) : new TranslatorToServcieProvided(translator, serviceProvided);
    }

    @Override
    public void removeBy(Translator translator) {
        String query = "DELETE FROM TranslatorToServcieProvided ttsp "
                + " WHERE ttsp.translator.id = :translatorId ";
        entityManager.createQuery(query)
                .setParameter("translatorId", translator.getId())
                .executeUpdate();
    }

}
