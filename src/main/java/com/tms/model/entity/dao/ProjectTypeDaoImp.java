/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.ProjectType;
import com.tms.model.entity.Status;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class ProjectTypeDaoImp extends EntityDaoImp<ProjectType> implements ProjectTypeDao {

    @Override
    public ProjectType T() {
        return (ProjectType) entityManager.createQuery("FROM ProjectType pt WHERE pt.name = 'T'")
                .getResultList().get(0);
    }

    @Override
    public ProjectType PE() {
        return (ProjectType) entityManager.createQuery("FROM ProjectType pt WHERE pt.name = 'PE'")
                .getResultList().get(0);
    }
    
}