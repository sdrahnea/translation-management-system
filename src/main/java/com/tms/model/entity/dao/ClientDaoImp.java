/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Client;
import com.tms.model.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class ClientDaoImp extends EntityDaoImp<Client> implements ClientDao {

    @Override
    public Client getClient(final User user) {
        return findByProperty("user.id", user.getId()).get(0);
    }

}
