package com.tms.repository;

import com.tms.model.entity.TimeZone;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeZoneRepository extends AbstractRepository<TimeZone, Integer> {
}

