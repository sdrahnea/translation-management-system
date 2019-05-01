/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.util.dataEntity;

import com.tms.model.entity.Role;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sdrahnea
 */
public class RoleDataEntity {
    
    public static List<Role> list(){
        List<Role> result = new LinkedList<>();
        
        result.add(new Role("ADMIN"));
        result.add(new Role("CLIENT"));
        result.add(new Role("TRANSLATOR"));
        result.add(new Role("MANAGER"));
        
        return result;
    }
    
}
