package com.tms.repository;

import com.tms.model.entity.Rating;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends AbstractRepository<Rating, Integer> {

    List<Rating> findByName(String name);
}
