package com.tms.repository;

import com.tms.model.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends AbstractRepository<Role, Integer> {

    List<Role> findByName(String name);

    default Role find(final String name) {
        List<Role> list = findByName(name);
        if (list.isEmpty()) {
            throw new RuntimeException("Here is no role with enetered name!");
        }
        return list.get(0);
    }
}

