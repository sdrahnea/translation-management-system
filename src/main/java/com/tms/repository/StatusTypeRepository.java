package com.tms.repository;

import com.tms.model.entity.StatusType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StatusTypeRepository extends AbstractRepository<StatusType, Integer> {

    List<StatusType> findByName(String name);

    default StatusType find(final String name) {
        List<StatusType> list = findByName(name);
        if (list.isEmpty()) {
            throw new RuntimeException("StatusType not found: " + name);
        }
        return list.get(0);
    }
}

