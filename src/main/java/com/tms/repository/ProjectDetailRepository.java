package com.tms.repository;

import com.tms.model.entity.ProjectDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectDetailRepository extends AbstractRepository<ProjectDetail, Integer> {

    List<ProjectDetail> findByProjectId(Integer projectId);
}

