package com.tms.repository;

import com.tms.model.entity.TranslationArea;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranslationAreaRepository extends AbstractRepository<TranslationArea, Integer> {

    List<TranslationArea> findByName(String name);
}
