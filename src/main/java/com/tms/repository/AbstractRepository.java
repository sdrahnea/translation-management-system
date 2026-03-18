package com.tms.repository;

import com.tms.model.entity.CoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AbstractRepository<T extends CoreEntity> extends JpaRepository<T, Integer> {



}
