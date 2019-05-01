/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.util.dataEntity;

import com.tms.model.entity.ProjectType;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sdrahnea
 */
public class ProjectTypeDataEntity {
    
    public static List<ProjectType> list(){
        List<ProjectType> result = new LinkedList<>();
        
        result.add(new ProjectType("T"));
        result.add(new ProjectType("PE"));
        result.add(new ProjectType("TEP"));
        result.add(new ProjectType("DTP"));
        result.add(new ProjectType("VO"));
        result.add(new ProjectType("CO"));
        result.add(new ProjectType("RE"));
        result.add(new ProjectType("TR"));
        result.add(new ProjectType("TR+T"));
        result.add(new ProjectType("ALL"));
        result.add(new ProjectType("SUB"));
        result.add(new ProjectType("TEST"));
        result.add(new ProjectType("LO"));
        result.add(new ProjectType("QA"));
        result.add(new ProjectType("M"));
        result.add(new ProjectType("TEPM"));
        
        return result;
    }
    
}
