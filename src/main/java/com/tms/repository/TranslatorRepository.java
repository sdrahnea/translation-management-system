package com.tms.repository;

import com.tms.model.entity.Translator;
import com.tms.model.entity.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TranslatorRepository extends AbstractRepository<Translator, Integer> {

    Optional<Translator> findByUser(final User user);

    default Translator getTranslator(final User user) {
        return findByUser(user).orElse(null);
    }
}
