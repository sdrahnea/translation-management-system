/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.EducationDegree;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class EducationDegreeDaoImp extends EntityDaoImp<EducationDegree> implements EducationDegreeDao {

//    public EducationDegree saveIfNotExist(String name) {
//        EducationDegree entity = findByName(name);
//        if (entity == null) {
//            entity = new EducationDegree(name);
//            saveOrUpdate(entity);
//        } 
//        return entity;
//    }
}