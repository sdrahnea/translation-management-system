package com.tms.repository;

import com.tms.model.entity.Cat;
import com.tms.model.entity.Translator;
import com.tms.model.entity.TranslatorToCat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslatorToCatRepository extends AbstractRepository<TranslatorToCat, Integer> {

    List<TranslatorToCat> findByTranslatorIdAndCatId(Integer translatorId, Integer catId);

    long deleteByTranslatorId(Integer translatorId);

    default TranslatorToCat find(final Translator translator, final Cat cat) {
        List<TranslatorToCat> result = findByTranslatorIdAndCatId(translator.getId(), cat.getId());
        return result.isEmpty() ? new TranslatorToCat(translator, cat) : result.get(0);
    }

    default void removeBy(final Translator translator) {
        deleteByTranslatorId(translator.getId());
    }
}

