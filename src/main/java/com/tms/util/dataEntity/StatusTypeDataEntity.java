/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.util.dataEntity;

import com.tms.model.entity.StatusType;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sdrahnea
 */
public class StatusTypeDataEntity {
    
    public static List<StatusType> list(){
        List<StatusType> result = new LinkedList<>();
        
        result.add(new StatusType("PROJECT_STATUS"));
        
        return result;
    }
    
}
