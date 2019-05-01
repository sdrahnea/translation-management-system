/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Role;
/**
 *
 * @author sdrahnea
 */
public interface RoleDao extends EntityDao<Role> {

    public Role find(String name);
    
}
