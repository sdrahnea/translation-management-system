/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.TranslationArea;
import com.tms.model.entity.Translator;
import com.tms.model.entity.TranslatorToTranslationArea;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class TranslatorToTranslateAreaDaoImp extends EntityDaoImp<TranslatorToTranslationArea> implements TranslatorToTranslateAreaDao {

    @Override
    @Transactional(readOnly = true)
    public TranslatorToTranslationArea find(Translator translator, TranslationArea translationArea) {
        String query = "FROM TranslatorToTranslationArea c "
                + " WHERE c.translator.id = :tranlatorId AND c.translationArea.id = :translationAreaId";
        List<TranslatorToTranslationArea> result = entityManager.createQuery(query)
                .setParameter("tranlatorId", translator.getId())
                .setParameter("translationAreaId", translationArea.getId())
                .getResultList();
        return result.size() > 0 ? result.get(0) : new TranslatorToTranslationArea(translator, translationArea);
    }

    @Override
    public void removeBy(Translator translator) {
        String query = "DELETE FROM TranslatorToTranslationArea c "
                + " WHERE c.translator.id = :translatorId ";
        entityManager.createQuery(query)
                .setParameter("translatorId", translator.getId())
                .executeUpdate();
    }

}
