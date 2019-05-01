/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Person;
import java.util.LinkedList;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class PersonDaoImp extends EntityDaoImp<Person> implements PersonDao {

    @Override
    public List<Person> findAllManagers() {
        List<Person> result = new LinkedList<>();
        try {
            result = entityManager.createQuery("FROM Person p WHERE p.personType.name = 'MANAGER'").getResultList();
        } catch (Exception ex) {
        }
        return result;
    }

}
