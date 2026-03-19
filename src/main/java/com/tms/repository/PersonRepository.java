package com.tms.repository;

import com.tms.model.entity.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends AbstractRepository<Person, Integer> {

    List<Person> findByPersonTypeName(String personTypeName);

    default List<Person> findAllManagers() {
        return findByPersonTypeName("MANAGER");
    }
}

