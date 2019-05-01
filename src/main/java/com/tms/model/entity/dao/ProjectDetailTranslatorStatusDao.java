/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.ProjectDetail;
import com.tms.model.entity.ProjectDetailTranslatorStatus;
import com.tms.model.entity.Translator;
import java.util.List;

/**
 *
 * @author sdrahnea
 */
public interface ProjectDetailTranslatorStatusDao extends EntityDao<ProjectDetailTranslatorStatus>{
 
    public List<ProjectDetailTranslatorStatus> getAll(final ProjectDetail projectDetail);
    
    public List<ProjectDetailTranslatorStatus> getAllInformedTranslators(final ProjectDetail projectDetail);
    
    public ProjectDetailTranslatorStatus createAsInformedTranslator(final ProjectDetail projectDetail, final Translator translator);
    
    public ProjectDetailTranslatorStatus createAsReadyToWorkTranslator(final ProjectDetail projectDetail, final Translator translator);
    
    public List<Translator> getAllAssignedTranslators(final ProjectDetail projectDetail);
}
