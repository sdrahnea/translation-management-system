package com.tms.repository;

import com.tms.model.entity.ProjectDetail;
import com.tms.model.entity.ProjectDetailTranslatorStatus;
import com.tms.model.entity.Translator;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public interface ProjectDetailTranslatorStatusRepository extends AbstractRepository<ProjectDetailTranslatorStatus, Integer> {

    List<ProjectDetailTranslatorStatus> findByProjectDetailId(Integer projectDetailId);

    List<ProjectDetailTranslatorStatus> findByProjectDetailIdAndStatusName(Integer projectDetailId, String statusName);

    default ProjectDetailTranslatorStatus createAsInformedTranslator(final ProjectDetail projectDetail, final Translator translator) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    default ProjectDetailTranslatorStatus createAsReadyToWorkTranslator(final ProjectDetail projectDetail, final Translator translator) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    default List<ProjectDetailTranslatorStatus> getAll(final ProjectDetail projectDetail) {
        return findByProjectDetailId(projectDetail.getId());
    }

    default List<ProjectDetailTranslatorStatus> getAllInformedTranslators(final ProjectDetail projectDetail) {
        return findByProjectDetailIdAndStatusName(projectDetail.getId(), "INFORMED");
    }

    default List<Translator> getAllAssignedTranslators(final ProjectDetail projectDetail) {
        return findByProjectDetailIdAndStatusName(projectDetail.getId(), "ASSIGNED")
                .stream()
                .map(ProjectDetailTranslatorStatus::getTranslator)
                .collect(Collectors.toList());
    }
}

