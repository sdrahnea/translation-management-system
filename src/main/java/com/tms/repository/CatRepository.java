package com.tms.repository;

import com.tms.model.entity.Cat;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatRepository extends AbstractRepository<Cat, Integer> {

    List<Cat> findByName(final String name);

}
