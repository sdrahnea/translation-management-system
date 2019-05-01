/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.util.dataEntity;

import com.tms.model.entity.ServiceProvided;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sdrahnea
 */
public class ServiceProvidedDataEntity {
    
    public static List<ServiceProvided> getServiceProvided(){
        List<ServiceProvided> result = new LinkedList<>();
        
        result.add(new ServiceProvided("T"));
        result.add(new ServiceProvided("PE"));
        result.add(new ServiceProvided("TEP"));
        result.add(new ServiceProvided("DTP"));
        result.add(new ServiceProvided("VO"));
        result.add(new ServiceProvided("CO"));
        result.add(new ServiceProvided("RE"));
        result.add(new ServiceProvided("TR"));
        result.add(new ServiceProvided("TR+T"));
        result.add(new ServiceProvided("ALL"));
        result.add(new ServiceProvided("SUB"));
        result.add(new ServiceProvided("TEST"));
        result.add(new ServiceProvided("LO"));
        result.add(new ServiceProvided("QA"));
        result.add(new ServiceProvided("M"));
        result.add(new ServiceProvided("TEPM"));
        
        return result;
    }
    
}
