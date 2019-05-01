/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Cat;
import com.tms.model.entity.Translator;
import com.tms.model.entity.TranslatorToCat;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class TranslatorToCatDaoImp extends EntityDaoImp<TranslatorToCat> implements TranslatorToCatDao {

    @Override
    public TranslatorToCat find(Translator translator, Cat cat) {
        String query = "FROM TranslatorToCat ttc "
                + " WHERE ttc.translator.id = :translatorId AND ttc.cat.id = :catId";
        List<TranslatorToCat> result = entityManager.createQuery(query)
                .setParameter("translatorId", translator.getId())
                .setParameter("catId", cat.getId())
                .getResultList();
        return result.size() > 0 ? result.get(0) : new TranslatorToCat(translator, cat);
    }

    @Override
    public void removeBy(Translator translator) {
        String query = "DELETE FROM TranslatorToCat ttc "
                + " WHERE ttc.translator.id = :translatorId";
        entityManager.createQuery(query)
                .setParameter("translatorId", translator.getId())
                .executeUpdate();
    }

}
