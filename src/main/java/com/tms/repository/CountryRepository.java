package com.tms.repository;

import com.tms.model.entity.Country;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryRepository extends AbstractRepository<Country> {

    List<Country> findByName(final String name);

}
