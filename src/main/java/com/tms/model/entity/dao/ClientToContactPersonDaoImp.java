/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.dao;

import com.tms.model.entity.Client;
import com.tms.model.entity.ClientToContactPerson;
import com.tms.model.entity.Person;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
@Transactional
public class ClientToContactPersonDaoImp extends EntityDaoImp<ClientToContactPerson> implements ClientToContactPersonDao {
    
    @Override
    @Transactional(readOnly = true)
    public ClientToContactPerson findBy(Client client, Person person){
        String query = "FROM ClientToContactPerson c "
                + " WHERE c.client.id = :clientId AND c.person.id = :personId";
        List<ClientToContactPerson> result = entityManager.createQuery(query)
                .setParameter("clientId", client.getId())
                .setParameter("personId", person.getId())
                .getResultList();
        return result.size() > 0 ? result.get(0) : new ClientToContactPerson(client, person);
    }

    @Override
    public void removeBy(Client client) {
        String query = "DELETE FROM ClientToContactPerson c "
                + " WHERE c.client.id = :clientId ";
        entityManager.createQuery(query)
                .setParameter("clientId", client.getId())
                .executeUpdate();
        
    }
    
}