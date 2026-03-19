package com.tms.repository;

import com.tms.model.entity.ServiceProvided;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceProvidedRepository extends AbstractRepository<ServiceProvided, Integer> {

    List<ServiceProvided> findByName(String name);
}

