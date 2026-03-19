package com.tms.repository;

import com.tms.model.entity.ServiceProvided;
import com.tms.model.entity.Translator;
import com.tms.model.entity.TranslatorToServcieProvided;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslatorToServcieProvidedRepository extends AbstractRepository<TranslatorToServcieProvided, Integer> {

    List<TranslatorToServcieProvided> findByTranslatorIdAndServiceProvidedId(Integer translatorId, Integer serviceProvidedId);

    long deleteByTranslatorId(Integer translatorId);

    default TranslatorToServcieProvided find(final Translator translator, final ServiceProvided serviceProvided) {
        List<TranslatorToServcieProvided> result = findByTranslatorIdAndServiceProvidedId(translator.getId(), serviceProvided.getId());
        return result.isEmpty() ? new TranslatorToServcieProvided(translator, serviceProvided) : result.get(0);
    }

    default void removeBy(final Translator translator) {
        deleteByTranslatorId(translator.getId());
    }
}
