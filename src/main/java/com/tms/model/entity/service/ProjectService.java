/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.service;

import com.tms.model.entity.Cat;
import com.tms.model.entity.Client;
import com.tms.model.entity.Currency;
import com.tms.model.entity.Language;
import com.tms.model.entity.Person;
import com.tms.model.entity.Project;
import com.tms.model.entity.ProjectDetail;
import com.tms.model.entity.ProjectType;
import com.tms.model.entity.Status;
import com.tms.model.entity.TimeZone;
import com.tms.model.entity.TranslationArea;
import com.tms.model.entity.Translator;
import com.tms.model.entity.User;
import com.tms.repository.ClientRepository;
import com.tms.repository.ProjectRepository;
import com.tms.repository.ProjectTypeRepository;
import com.tms.repository.StatusRepository;
import com.tms.repository.TranslatorRepository;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Service
public class ProjectService implements Serializable {

    @Autowired
    private ProjectDetailService projectDetailService;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TranslatorRepository translatorRepository;
    @Autowired
    private ProjectTypeRepository projectTypeRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<Project> search(Person assignedManager, String clientName,
            Date startDeadlineDate, Date endDeadlineDate,
            Language destinatonLanguage, Integer projectId,
            Status projectStatus, Date startReceiveDate, Date endReceiveDate,
            Person saleManager, Person secondAssisgnedManager, Language sourceLanguage,
            String translatorName, String translatorEmail, String fileName) {
        Map<String, Object> pars = new HashMap<>();
        pars.put("assignedManager", assignedManager);
        pars.put("clientName", clientName);
        pars.put("startDeadlineDate", startDeadlineDate);
        pars.put("endDeadlineDate", endDeadlineDate);
        pars.put("destinatonLanguage", destinatonLanguage);
        pars.put("projectId", projectId);
        pars.put("projectStatus", projectStatus);
        pars.put("startReceiveDate", startReceiveDate);
        pars.put("endReceiveDate", endReceiveDate);
        pars.put("saleManager", saleManager);
        pars.put("secondAssisgnedManager", secondAssisgnedManager);
        pars.put("sourceLanguage", sourceLanguage);
        pars.put("translatorName", translatorName);
        pars.put("translatorEmail", translatorEmail);
        pars.put("fileName", fileName);

        StringBuilder sql = new StringBuilder("FROM Project p WHERE (1 = 1) ");
        if (assignedManager != null) {
            sql.append(" AND (assignedManager = :assignedManager ");
        }
        if (clientName != null && !clientName.isEmpty()) {
            sql.append(" AND (client.name LIKE CONCAT('%', :clientName, '%'))");
        }
        if (startDeadlineDate != null) {
            sql.append(" AND (deadlineDate >= :startDeadlineDate)");
        }
        if (endDeadlineDate != null) {
            sql.append(" AND (deadlineDate <= :endDeadlineDate)");
        }
        if (destinatonLanguage != null) {
            sql.append(" AND (destinatonLanguage = :destinatonLanguage)");
        }
        if (projectId != null) {
            sql.append(" AND (id = :projectId)");
        }
        if (projectStatus != null) {
            sql.append(" AND (status = :projectStatus)");
        }
        if (startReceiveDate != null) {
            sql.append(" AND (insertDate >= :startReceiveDate)");
        }
        if (endReceiveDate != null) {
            sql.append(" AND (insertDate <= :endReceiveDate)");
        }
        if (saleManager != null) {
            sql.append(" AND (saleManager = :saleManager)");
        }
        if (secondAssisgnedManager != null) {
            sql.append(" AND (secondManager = :secondAssisgnedManager)");
        }
        if (sourceLanguage != null) {
            sql.append(" AND (sourceLanguage = :sourceLanguage)");
        }
        if (translatorName != null && !translatorName.isEmpty()) {
            sql.append(" AND (translator.name LIKE CONCAT('%', :translatorName, '%'))");
        }
        if (translatorEmail != null && !translatorEmail.isEmpty()) {
            sql.append(" AND (translator.email LIKE CONCAT('%', :translatorEmail, '%'))");
        }

        Query query = entityManager.createQuery(sql.toString());
        for (Parameter p : query.getParameters()) {
            query.setParameter(p, pars.get(p.getName()));
        }

        List<Project> resultList = query.getResultList();

        if (fileName != null && !fileName.isEmpty()) {
            List<Project> targetList = new LinkedList<>();
            for (Project p : resultList) {
                boolean addToList = false;
                for (ProjectDetail pd : p.getProjectDetails()) {
                    if (fileName.equalsIgnoreCase(pd.getFileName())) {
                        addToList = true;
                    }
                }
                if (addToList) {
                    targetList.add(p);
                }
            }
            resultList = targetList;
        }

        return resultList;
    }

    @Transactional
    public Project createOrUpdate(Project project, final boolean isEditOption, final ProjectType projectType,
            final TranslationArea translationArea, final Cat cat,
            final Client client, final Person contactPerson,
            final Person saleManager, final Person manager,
            final Person secondManager, final Currency currency,
            final Language sourceLanguage, final Language destinationLanguage,
            final TimeZone timeZone,
            final String fileName, final String fileUUID,
            final Language[] multiDestinationLanguages
    ) {
        /**
         * project initial status
         */
        Status status = null;
        if (!isEditOption) {
            status = statusRepository.find("NEW");
        }
        /**
         * set entered parameters and create a new project
         */
        project.setProjectType(projectType);
        project.setTranslationArea(translationArea);
        project.setCat(cat);
        project.setClient(client);
        project.setContactPerson(contactPerson);
        project.setSaleManager(saleManager);
        project.setAssignedManager(manager);
        project.setSecondManager(secondManager);
        project.setCurrency(currency);
        project.setSourceLanguage(sourceLanguage);
        //when multi langauge select then select first one
        boolean isMultiLanguageSelect = multiDestinationLanguages != null && multiDestinationLanguages.length > 0;
        project.setDestinatonLanguage(!isEditOption ? multiDestinationLanguages[0] : destinationLanguage);
        project.setTimeZone(timeZone);
        if (!isEditOption) {
            project.setStatus(status);
            project.setInsertDate(new Date());
        }
        project = projectRepository.save(project);

        if (!isEditOption) {
            List<ProjectDetail> details = new LinkedList<>();
            if (isMultiLanguageSelect) {
                for (Language language : multiDestinationLanguages) {
                    if (projectType.isTEP()) {
                        ProjectDetail pdt = projectDetailService.create(project, fileName, fileUUID, status, "Project was cerated at " + (new Date()) + ".", language, projectTypeRepository.T());
                        details.add(pdt);
                        project.setProjectDetails(details);

                        ProjectDetail pde = projectDetailService.create(project, fileName, fileUUID, status, "Project was cerated at " + (new Date()) + ".", language, projectTypeRepository.PE());
                        details.add(pde);
                        project.setProjectDetails(details);
                    } else {
                        ProjectDetail pd = projectDetailService.create(project, fileName, fileUUID, status, "Project was cerated at " + (new Date()) + ".", language, project.getProjectType());
                        details.add(pd);
                        project.setProjectDetails(details);
                    }
                }
            } else {
                if (projectType.isTEP()) {
                    ProjectDetail pdt = projectDetailService.create(project, fileName, fileUUID, status, "Project was cerated at " + (new Date()) + ".", project.getDestinatonLanguage(), projectTypeRepository.T());
                    details.add(pdt);
                    project.setProjectDetails(details);

                    ProjectDetail pde = projectDetailService.create(project, fileName, fileUUID, status, "Project was cerated at " + (new Date()) + ".", project.getDestinatonLanguage(), projectTypeRepository.PE());
                    details.add(pde);
                    project.setProjectDetails(details);
                } else {
                    details.add(projectDetailService.create(project, fileName, fileUUID, status, "Project was cerated at " + (new Date()) + "."));
                }
            }

            project.setProjectDetails(details);
        }

        return project;
    }

    @Transactional
    public List<Project> getProjects(final User user) {
        if (user.isClientRole()) {
            Client client = clientRepository.getClient(user);
            if (client != null) {
                return projectRepository.getNotInvoicedOrArchivied(client);
            }
        } else if (user.isTranslatorRole()) {
            List<Project> resultList = new LinkedList<>();
            Translator translator = translatorRepository.getTranslator(user);
            if (translator != null) {
                for (Project p : projectRepository.getNotInvoicedOrArchivied()) {
                    if (!p.getProjectDetail(translator).isEmpty()) {
                        p.setProjectDetails(p.getProjectDetail(translator));
                        resultList.add(p);
                    }
                }
            }
            return resultList;
        }
        return projectRepository.getNotInvoicedOrArchivied();
    }

    public List<Project> getInvoicedProjects() {
        return projectRepository.getInvoived();
    }

    public List<Project> getArchivedProjects() {
        return projectRepository.getArchived();
    }

    /**
     * change the project's status to archived and create a project detail with
     * archived status
     *
     * @param project
     */
    @Transactional
    public void moveToArchive(Project project) {
        if (project == null) {
            throw new RuntimeException("Please, select a project!");
        }
        Status status = statusRepository.find("ARCHIVED");
        changeProjectStatusTo(project, status);
//        projectDetailService.create(project, status, "Project was moved in Archives on " + (new Date()) + ".");
    }

    /**
     * change project status to restored and create project detail with restore
     * status
     *
     * @param project
     */
    @Transactional
    public void restoreProject(Project project) {
        if (project == null) {
            throw new RuntimeException("Please, select a project!");
        }
        final Status status = statusRepository.find("RESTORED");
        changeProjectStatusTo(project, status);
//        projectDetailService.create(project, status, "Project was retored on " + (new Date()) + ".");
    }

    /**
     * change project status to client paid and create a project detail with
     * client paid status.
     *
     * @param project
     */
    @Transactional
    public void moveToClientPaidStatus(Project project) {
        if (project == null) {
            throw new RuntimeException("Please, select a project!");
        }
        final Status status = statusRepository.find("CLIENT_PAID");
        changeProjectStatusTo(project, status);
//        projectDetailService.create(project, status, "Project was marked as CLIENT PAID status at " + (new Date())
//                + " date time. Expected payment from client is " + project.getClientExpectedPayment()
//                + ". Actual payment from cclient is " + project.getClientActualPayment() + ".");
    }

    /**
     * change project status to translator paid and create a detail with
     * translator paid status
     *
     * @param project
     */
    @Transactional
    public void moveToTranslatorPaidStatus(Project project) {
        if (project == null) {
            throw new RuntimeException("Please, select a project!");
        }
        final Status status = statusRepository.find("TRANSLATOR_PAID");
        changeProjectStatusTo(project, status);
//        projectDetailService.create(project, status, "Project was marked as TRANSLATOR PAID status at " + (new Date())
//                + " date time. Translator actual payment is " + project.getTranslatorActualPayment() + ".");
    }

    /**
     * change project status to invoiced and create a detail with invoiced
     * status
     *
     * @param project
     */
    @Transactional
    public void moveToProjectInvoicedStatus(Project project) {
        if (project == null) {
            throw new RuntimeException("Please, select a project!");
        }
        final Status invoicedStatus = statusRepository.find("INVOICED");
        changeProjectStatusTo(project, invoicedStatus);
        //projectDetailService.create(project, invoicedStatus, "Project was marked as INVOICED status at " + (new Date()) + " date time.");
    }

    @Transactional
    public void moveToProjectDelivedStatus(Project project) {
        if (project == null) {
            throw new RuntimeException("Please, select a project!");
        }
        final Status invoicedStatus = statusRepository.find("DELIVERED");
        changeProjectStatusTo(project, invoicedStatus);
    }

    @Transactional
    public void moveToProjectInformedStatus(Project project) {
        if (project == null) {
            throw new RuntimeException("Please, select a project!");
        }
        final Status invoicedStatus = statusRepository.find("INFORMED");
        changeProjectStatusTo(project, invoicedStatus);
    }

    @Transactional
    public void moveToProjectAssignedStatus(Project project) {
        if (project == null) {
            throw new RuntimeException("Please, select a project!");
        }
        final Status invoicedStatus = statusRepository.find("ASSIGNED");
        changeProjectStatusTo(project, invoicedStatus);
    }

    /**
     * change project status to paid and create a new project detail with paid
     * status value
     *
     * @param project
     */
    @Transactional
    public void moveToProjectPaidStatus(Project project) {
        if (project == null) {
            throw new RuntimeException("Please, select a project!");
        }
        final Status paidStatus = statusRepository.find("PAID");
        changeProjectStatusTo(project, paidStatus);
        //projectDetailService.create(project, paidStatus, "Project was marked as PAID status at " + (new Date()) + " date time.");
    }

    /**
     * change project status to entered status
     *
     * @param project target project
     * @param status new status value
     */
    private void changeProjectStatusTo(Project project, final Status status) {
        project.setStatus(status);
        projectRepository.save(project);
    }

    public ProjectDetail createNewEmptyDetail(Project project) {
        final Status status = statusRepository.find("NEW");
        return projectDetailService.create(project, status);
    }

    @Transactional(readOnly = true)
    public List<Project> getProjects(Translator translator){
        List<Project> allProject = projectRepository.findAll();
        List<Project> result = new LinkedList<>();
        for (Project project : allProject){
            if (project.isProjectHandledBy(translator)){
                result.add(project);
            }
        }
        return result;
    } 
    
}
