/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Role;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class RoleDaoImp  extends EntityDaoImp<Role> implements RoleDao {
    
    @Override
    public Role find(String name) {
        List<Role> list = entityManager.createQuery(" FROM Role r WHERE r.name = :name ")
                .setParameter("name", name)
                .getResultList();
        if (list.isEmpty()) {
            throw new RuntimeException("Here is no role with enetered name!");
        }
        return list.get(0);
    }
    
}