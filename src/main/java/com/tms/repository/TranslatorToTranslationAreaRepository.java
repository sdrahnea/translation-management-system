package com.tms.repository;

import com.tms.model.entity.Translator;
import com.tms.model.entity.TranslatorToTranslationArea;
import com.tms.model.entity.TranslationArea;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslatorToTranslationAreaRepository extends AbstractRepository<TranslatorToTranslationArea, Integer> {

    List<TranslatorToTranslationArea> findByTranslatorIdAndTranslationAreaId(Integer translatorId, Integer translationAreaId);

    long deleteByTranslatorId(Integer translatorId);

    default TranslatorToTranslationArea find(final Translator translator, final TranslationArea translationArea) {
        List<TranslatorToTranslationArea> result = findByTranslatorIdAndTranslationAreaId(translator.getId(), translationArea.getId());
        return result.isEmpty() ? new TranslatorToTranslationArea(translator, translationArea) : result.get(0);
    }

    default void removeBy(final Translator translator) {
        deleteByTranslatorId(translator.getId());
    }
}

