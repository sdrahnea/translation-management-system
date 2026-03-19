package com.tms.repository;

import com.tms.model.entity.ProjectType;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTypeRepository extends AbstractRepository<ProjectType, Integer> {

    List<ProjectType> findByName(String name);

    default ProjectType T() {
        return getRequiredByName("T");
    }

    default ProjectType PE() {
        return getRequiredByName("PE");
    }

    default ProjectType getRequiredByName(String name) {
        List<ProjectType> result = findByName(name);
        if (result.isEmpty()) {
            throw new RuntimeException("Here is no project type with entered name!");
        }
        return result.get(0);
    }
}

