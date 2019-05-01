/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.ProjectDetail;
import com.tms.model.entity.ProjectDetailTranslatorStatus;
import com.tms.model.entity.Status;
import com.tms.model.entity.Translator;
import com.tms.util.entity.Property;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class ProjectDetailTranslatorStatusDaoImp extends EntityDaoImp<ProjectDetailTranslatorStatus> implements ProjectDetailTranslatorStatusDao {

    @Autowired
    private StatusDao statusDao;

    @Override
    public ProjectDetailTranslatorStatus createAsInformedTranslator(ProjectDetail projectDetail, Translator translator) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ProjectDetailTranslatorStatus createAsReadyToWorkTranslator(ProjectDetail projectDetail, Translator translator) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<ProjectDetailTranslatorStatus> getAll(ProjectDetail projectDetail) {
        return findByProperty("projectDetail.id", projectDetail.getId());
    }

    @Override
    @Transactional
    public List<Translator> getAllAssignedTranslators(ProjectDetail projectDetail) {
        Status status = statusDao.ASSIGN_TRANSLATOR();
        System.out.println("status.getId(): " + status.getId());
        System.out.println("projectDetail.getId(): " + projectDetail.getId());
        List<ProjectDetailTranslatorStatus> list
                = find("AND", new Property("projectDetail.id", projectDetail.getId()),
                        new Property("status.id", status.getId()));
        List<Translator> result = new LinkedList<>();
        for (final ProjectDetailTranslatorStatus element : list) {
            result.add(element.getTranslator());
        }
        System.out.println("getAllAssignedTranslators.result: " + result.size());
        return result;
    }
    
    @Override
    @Transactional
    public List<ProjectDetailTranslatorStatus> getAllInformedTranslators(ProjectDetail projectDetail) {
        Status status = statusDao.INFORM_TRANSLATOR();
        System.out.println("status.getId(): " + status.getId());
        System.out.println("projectDetail.getId(): " + projectDetail.getId());
        List<ProjectDetailTranslatorStatus> result
                = find("AND", new Property("projectDetail.id", projectDetail.getId()),
                        new Property("status.id", status.getId()));
        System.out.println("getAllInformedTranslators.result: " + result.size());
        return result;
    }

}
