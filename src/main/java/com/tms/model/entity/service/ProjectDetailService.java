/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.service;

import com.tms.model.entity.Language;
import com.tms.model.entity.Project;
import com.tms.model.entity.ProjectDetail;
import com.tms.model.entity.ProjectDetailTranslatorStatus;
import com.tms.model.entity.ProjectType;
import com.tms.model.entity.Status;
import com.tms.model.entity.Translator;
import com.tms.repository.ProjectDetailRepository;
import com.tms.repository.ProjectDetailTranslatorStatusRepository;
import com.tms.repository.ProjectRepository;
import com.tms.repository.StatusRepository;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Service
public class ProjectDetailService implements Serializable {

    @Autowired
    private ProjectDetailRepository projectDetailRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private ProjectDetailTranslatorStatusRepository projectDetailTranslatorStatusRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Transactional
    public ProjectDetail create(Project project, final Status status) {
        ProjectDetail detail = new ProjectDetail();
        detail.setLogicalId(getNextLogicalId(project));
        detail.setProject(project);
        detail.setStatus(status);
        detail.setDeadlineDate(project.getDeadlineDate());
        detail.setDeadlineHour(project.getDeadlineHour());
        detail.setCurrency(project.getCurrency());

        detail = projectDetailRepository.save(detail);

        project.addProjectDetail(detail);
        projectRepository.saveAndFlush(project);

        return detail;
    }

    @Transactional
    public void create(Project project, final Status status, final String systemNotes) {
        ProjectDetail detail = new ProjectDetail();
        detail.setLogicalId(getNextLogicalId(project));
        detail.setProject(project);
        detail.setStatus(status);
        detail.setSystemNotes(systemNotes);

        detail.setUnits(project.getUnits());
        detail.setCurrency(project.getCurrency());
        detail.setNotes(project.getNotes());
        detail.setDestinatonLanguage(project.getDestinatonLanguage());
        detail.setSourceLanguage(project.getSourceLanguage());
        detail.setProjectType(project.getProjectType());
        detail.setDeadlineDate(project.getDeadlineDate());
        detail.setDeadlineHour(project.getDeadlineHour());
        projectDetailRepository.save(detail);
    }

    @Transactional
    public ProjectDetail create(final Project project, final String fileName, final String fileUUID,
            final Status status, final String systemNotes) {
        ProjectDetail detail = new ProjectDetail();
        detail.setLogicalId(getNextLogicalId(project));
        detail.setProject(project);
        detail.setStatus(status);
        detail.setFileName(fileName);
        detail.setFileUUID(fileUUID);
        detail.setSystemNotes(systemNotes);

        detail.setUnits(project.getUnits());
        detail.setCurrency(project.getCurrency());
        detail.setNotes(project.getNotes());
        detail.setDestinatonLanguage(project.getDestinatonLanguage());
        detail.setSourceLanguage(project.getSourceLanguage());
        detail.setProjectType(project.getProjectType());
        detail.setDeadlineDate(project.getDeadlineDate());
        detail.setDeadlineHour(project.getDeadlineHour());
        return projectDetailRepository.save(detail);
    }
    
    @Transactional
    public ProjectDetail create(final Project project, final String fileName, final String fileUUID,
            final Status status, final String systemNotes, final Language destinationLanguage, ProjectType projectType) {
        ProjectDetail detail = new ProjectDetail();
        detail.setLogicalId(getNextLogicalId(project));
        detail.setProject(project);
        detail.setStatus(status);
        detail.setFileName(fileName);
        detail.setFileUUID(fileUUID);
        detail.setSystemNotes(systemNotes);

        detail.setUnits(project.getUnits());
        detail.setCurrency(project.getCurrency());
        detail.setNotes(project.getNotes());
        detail.setDestinatonLanguage(destinationLanguage);
        detail.setSourceLanguage(project.getSourceLanguage());
        detail.setProjectType(projectType);
        detail.setDeadlineDate(project.getDeadlineDate());
        detail.setDeadlineHour(project.getDeadlineHour());
        return projectDetailRepository.save(detail);
    }

    @Transactional
    public void assignTranslator(final ProjectDetail projectDetail, final Translator translator) {
        final Status status = statusRepository.ASSIGN_TRANSLATOR();
        projectDetail.setStatus(status);
        projectDetailRepository.save(projectDetail);
        projectDetailTranslatorStatusRepository.save(new ProjectDetailTranslatorStatus(projectDetail, translator, status));
        checkAndUpdateStatus(projectDetail.getProject());
    }

    @Transactional
    public void assignTranslator(ProjectDetail projectDetail, ProjectDetailTranslatorStatus pdts) {
        final Status status = statusRepository.ASSIGN_TRANSLATOR();
        projectDetail.setStatus(status);
        projectDetail.setTranslator(pdts.getTranslator());
        projectDetail = projectDetailRepository.save(projectDetail);

        pdts.setProjectDetail(projectDetail);
        pdts.setStatus(status);
        projectDetailTranslatorStatusRepository.save(pdts);
        checkAndUpdateStatus(projectDetail.getProject());
    }

    @Transactional
    public void readyToWorkTranslator(final ProjectDetail projectDetail, final Translator translator) {
        final Status status = statusRepository.READY_TO_WORK();
        projectDetail.setStatus(status);
        projectDetailRepository.save(projectDetail);
        projectDetailTranslatorStatusRepository.save(new ProjectDetailTranslatorStatus(projectDetail, translator, status));
        checkAndUpdateStatus(projectDetail.getProject());
    }

    @Transactional
    public void readyToWorkTranslator(final ProjectDetail projectDetail, ProjectDetailTranslatorStatus pdts) {
        final Status status = statusRepository.READY_TO_WORK();
        projectDetail.setStatus(status);
        pdts.setStatus(status);
        projectDetailRepository.save(projectDetail);
        projectDetailTranslatorStatusRepository.save(pdts);
        checkAndUpdateStatus(projectDetail.getProject());
    }

    @Transactional
    public void informTranslator(final ProjectDetail projectDetail, final Translator translator) {
        final Status status = statusRepository.INFORM_TRANSLATOR();
        projectDetail.setStatus(status);
        projectDetailRepository.save(projectDetail);
        projectDetailTranslatorStatusRepository.save(new ProjectDetailTranslatorStatus(projectDetail, translator, status));
        checkAndUpdateStatus(projectDetail.getProject());
    }

    @Transactional
    public void informTranslator(final Project project, final Translator translator,
            final Status status) {
        ProjectDetail detail = new ProjectDetail();
        detail.setLogicalId(getNextLogicalId(project));
        detail.setTranslator(translator);
        detail.setProject(project);
        detail.setUnits(project.getUnits());
        detail.setSourceLanguage(project.getSourceLanguage());
        detail.setDestinatonLanguage(project.getDestinatonLanguage());
        detail.setProjectType(project.getProjectType());
        detail.setSystemNotes("Project detail with status INFORM was created at " + (new Date()) + " date time. Translator " + translator.getName() + " was informed at " + (new Date()) + ".");
        detail.setStatus(status);

        projectDetailRepository.save(detail);
        checkAndUpdateStatus(project);
    }
    
    public void markAsDelivered(ProjectDetail pd){
        changeStaus(pd, statusRepository.DELIVERED());
        checkAndUpdateStatus(pd.getProject());
    }
    
    public void markAsPaid(ProjectDetail pd){
        changeStaus(pd, statusRepository.PAID());
        checkAndUpdateStatus(pd.getProject());
    }
    
    public void markAsClientPaid(ProjectDetail pd){
        changeStaus(pd, statusRepository.CLIENT_PAID());
        checkAndUpdateStatus(pd.getProject());
    }
    
    public void markAsTranslatorPaid(ProjectDetail pd){
        changeStaus(pd, statusRepository.TRANSLATOR_PAID());
        checkAndUpdateStatus(pd.getProject());
    }
    
    public void markAsInvoiced(ProjectDetail pd){
        changeStaus(pd, statusRepository.INVOICED());
        checkAndUpdateStatus(pd.getProject());
    }
    
    private void changeStaus(ProjectDetail pd, Status status){
        pd.setStatus(status);
        projectDetailRepository.save(pd);
    }
    
    

    public List<ProjectDetailTranslatorStatus> getAll(final ProjectDetail projectDetail) {
        return projectDetail == null ? null : projectDetailTranslatorStatusRepository.getAll(projectDetail);
    }
    
    public List<ProjectDetailTranslatorStatus> getAllInformedTranslators(final ProjectDetail projectDetail) {
        return projectDetail == null ? null : projectDetailTranslatorStatusRepository.getAllInformedTranslators(projectDetail);
    }
    
    
    public List<Translator> getAllAssignedTranslators(final ProjectDetail projectDetail) {
        return projectDetail == null ? null : projectDetailTranslatorStatusRepository.getAllAssignedTranslators(projectDetail);
    }

    private String getNextLogicalId(final Project project) {
        return project.getId() + "_" + (project.getProjectDetails() == null || project.getProjectDetails().isEmpty() ? 1 : project.getProjectDetails().size() + 1);
    }

    private void checkAndUpdateStatus(Project project) {
        if (project != null) {
            List<ProjectDetail> pdList = projectDetailRepository.findByProjectId(project.getId());
            if (pdList != null && !pdList.isEmpty()) {
                Status status = pdList.get(0).getStatus();
                for (ProjectDetail pd : pdList) {
                    if (status.getPriority() > pd.getStatus().getPriority()) {
                        status = pd.getStatus();
                    }
                }
                project.setStatus(status);
                projectRepository.save(project);
            }
        }
    }

}
