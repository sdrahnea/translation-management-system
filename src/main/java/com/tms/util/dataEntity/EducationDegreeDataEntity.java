/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.util.dataEntity;

import com.tms.model.entity.EducationDegree;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sdrahnea
 */
public class EducationDegreeDataEntity {

public static List<EducationDegree> getEducationDegreeList(){
    List<EducationDegree> result = new LinkedList<>();
    
    result.add(new EducationDegree("NA"));
    result.add(new EducationDegree("BA"));
    result.add(new EducationDegree("MA"));
    result.add(new EducationDegree("PhD"));
    
    return result;
}
    
}
