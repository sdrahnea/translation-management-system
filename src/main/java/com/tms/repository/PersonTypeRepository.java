package com.tms.repository;

import com.tms.model.entity.PersonType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonTypeRepository extends AbstractRepository<PersonType, Integer> {

    List<PersonType> findByName(String name);

    default PersonType CONTACT_PERSON() {
        return getRequiredByName("CONTACT_PERSON");
    }

    default PersonType SALE_MANAGER() {
        return getRequiredByName("SALE_MANAGER");
    }

    default PersonType MANAGER() {
        return getRequiredByName("MANAGER");
    }

    default PersonType getRequiredByName(String name) {
        List<PersonType> result = findByName(name);
        if (result.isEmpty()) {
            throw new RuntimeException("Here is no person type with entered name!");
        }
        return result.get(0);
    }
}

