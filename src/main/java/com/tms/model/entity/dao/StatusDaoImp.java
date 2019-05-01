/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Status;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class StatusDaoImp extends EntityDaoImp<Status> implements StatusDao {

    /**
     * get Status object by name. Return the first found. Is no result throw a
     * runtime exception
     *
     * @param name
     * @return
     */
    @Override
    public Status find(String name) {
        List<Status> list = entityManager.createQuery(
                "FROM Status s WHERE s.statusType.name = 'PROJECT_STATUS' AND s.name = :name")
                .setParameter("name", name)
                .getResultList();
        if (list.isEmpty()) {
            throw new RuntimeException("Here is no status with enetered name!");
        }
        return list.get(0);
    }

    @Override
    public List<Status> findProjectStatus() {
        return entityManager.createQuery("FROM Status s WHERE s.statusType.name = 'PROJECT_STATUS'").getResultList();
    }

    @Override
    public Status findByProjectStatusAndReadyToWork() {
        return (Status) entityManager.createQuery("FROM Status s WHERE s.statusType.name = 'PROJECT_STATUS' AND s.name = 'READY_TO_WORK'")
                .getResultList().get(0);
    }

    @Override
    public Status findByProjectStatusAndAssigned() {
        return (Status) entityManager.createQuery("FROM Status s WHERE s.statusType.name = 'PROJECT_STATUS' AND s.name = 'ASSIGNED'")
                .getResultList().get(0);
    }

    @Override
    public Status READY_TO_WORK() {
        return (Status) entityManager.createQuery("FROM Status s WHERE s.statusType.name = 'PROJECT_STATUS' AND s.name = 'READY_TO_WORK'")
                .getResultList().get(0);
    }

    @Override
    public Status ASSIGN_TRANSLATOR() {
        return (Status) entityManager.createQuery("FROM Status s WHERE s.statusType.name = 'PROJECT_STATUS' AND s.name = 'ASSIGNED'")
                .getResultList().get(0);
    }

    @Override
    public Status INFORM_TRANSLATOR() {
        return (Status) entityManager.createQuery("FROM Status s WHERE s.statusType.name = 'PROJECT_STATUS' AND s.name = 'INFORMED'")
                .getResultList().get(0);
    }

    @Override
    public Status DELIVERED() {
        return (Status) entityManager.createQuery("FROM Status s WHERE s.statusType.name = 'PROJECT_STATUS' AND s.name = 'DELIVERED'")
                .getResultList().get(0);
    }

    @Override
    public Status PAID() {
        return (Status) entityManager.createQuery("FROM Status s WHERE s.statusType.name = 'PROJECT_STATUS' AND s.name = 'PAID'")
                .getResultList().get(0);
    }

    @Override
    public Status CLIENT_PAID() {
        return (Status) entityManager.createQuery("FROM Status s WHERE s.statusType.name = 'PROJECT_STATUS' AND s.name = 'CLIENT_PAID'")
                .getResultList().get(0);
    }

    @Override
    public Status TRANSLATOR_PAID() {
        return (Status) entityManager.createQuery("FROM Status s WHERE s.statusType.name = 'PROJECT_STATUS' AND s.name = 'TRANSLATOR_PAID'")
                .getResultList().get(0);
    }

    @Override
    public Status INVOICED() {
        return (Status) entityManager.createQuery("FROM Status s WHERE s.statusType.name = 'PROJECT_STATUS' AND s.name = 'INVOICED'")
                .getResultList().get(0);
    }

}
