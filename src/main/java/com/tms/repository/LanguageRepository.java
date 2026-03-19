package com.tms.repository;

import com.tms.model.entity.Language;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LanguageRepository extends AbstractRepository<Language, Integer> {

    List<Language> findByName(String name);
}

