/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.service;

import com.tms.model.entity.Person;
import com.tms.model.entity.Role;
import com.tms.model.entity.User;
import com.tms.model.entity.dao.RoleDao;
import com.tms.model.entity.dao.UserDao;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
/**
 *
 * @author sdrahnea
 */
@Component
public class UserService {

    @Autowired
    private UserDao userDao;
    
    @Autowired
    private RoleDao roleDao;
    
    @Transactional
    public void createManager(final User user, final Person person) {
        Role manageRole = roleDao.find("MANAGER");
        user.setName(person.getName());
        user.setInsertDate(new Date());
        user.setRole(manageRole);
        userDao.merge(user);
    }

}
