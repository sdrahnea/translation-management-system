/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.util.dataEntity;

import com.tms.model.entity.PersonType;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sdrahnea
 */
public class PersonTypeDataEntity {
    
    public static List<PersonType> getPersonType(){
        List<PersonType>  result = new LinkedList<>();     
        
        result.add(new PersonType("SALE_MANAGER"));
        result.add(new PersonType("CONTACT_PERSON"));
        result.add(new PersonType("MANAGER"));
        
        return result;
    }
    
}
