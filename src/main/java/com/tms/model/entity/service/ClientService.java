/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.service;

import com.tms.model.entity.Client;
import com.tms.model.entity.ClientToContactPerson;
import com.tms.model.entity.Country;
import com.tms.model.entity.Person;
import com.tms.model.entity.PersonType;
import com.tms.model.entity.Segmentation;
import com.tms.repository.ClientRepository;
import com.tms.repository.ClientToContactPersonRepository;
import com.tms.repository.PersonRepository;
import com.tms.repository.PersonTypeRepository;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Service
public class ClientService implements Serializable {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PersonTypeRepository personTypeRepository;

    @Autowired
    private ClientToContactPersonRepository clientToContactPersonRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<Client> search(String clientName, String clientAddress, Country country,
            String contactPersonName, String contactPersonEmail, String contactPersonPhone) {
        Map<String, Object> pars = new HashMap<>();
        pars.put("clientName", clientName);
        pars.put("clientAddress", clientAddress);
        pars.put("country", country);
        pars.put("startDate", contactPersonName);
        pars.put("startDate", contactPersonEmail);
        pars.put("startDate", contactPersonPhone);

        StringBuilder sql = new StringBuilder("FROM Client c WHERE (1 = 1) ");
        if (clientName != null && !clientName.isEmpty()) {
            sql.append(" AND (name LIKE CONCAT('%', :clientName, '%'))");
        }
        if (clientAddress != null && !clientAddress.isEmpty()) {
            sql.append(" AND (address LIKE CONCAT('%', :clientAddress, '%'))");
        }
        if (country != null) {
            sql.append(" AND (country = :country)");
        }
        if (contactPersonName != null && !contactPersonName.isEmpty()) {
            sql.append(" AND (firstContactPerson.name LIKE CONCAT('%', :contactPersonName, '%'))");
        }
        if (contactPersonEmail != null && !contactPersonEmail.isEmpty()) {
            sql.append(" AND (firstContactPerson.email LIKE CONCAT('%', :contactPersonEmail, '%'))");
        }
        if (contactPersonPhone != null && !contactPersonPhone.isEmpty()) {
            sql.append(" AND (firstContactPerson.phone LIKE CONCAT('%', :contactPersonPhone, '%'))");
        }

        Query query = entityManager.createQuery(sql.toString());
        for (Parameter p : query.getParameters()) {
            query.setParameter(p, pars.get(p.getName()));
        }

        return query.getResultList();
    }

    @Transactional
    public void create(Client client, final Person saleManager, final Segmentation segmentation, final Country country) {
        client = checkClientForInssertDate(client);

        client.setSaleManager(saleManager);
        client.setCountry(country);
        client.setSegmentation(segmentation);
        clientRepository.save(client);
    }

    @Transactional
    public Client updateContactPerson(Client client, Person oldPerson, Person newPerson) {
        newPerson = personRepository.save(newPerson);

        ClientToContactPerson ctcp = clientToContactPersonRepository.findBy(client, oldPerson);
        ctcp.setPerson(newPerson);
        clientToContactPersonRepository.save(ctcp);

        return clientRepository.findOne(client.getId());
    }

    public List<Person> getContactPerson(Client client) {
        List<Person> result = new LinkedList<>();
        for (ClientToContactPerson ctcp : client.getClientToContactPersons()) {
            result.add(ctcp.getPerson());
        }
        return result;
    }

    @Transactional
    public void create(Client client, Person saleManager, Segmentation segmentation, Country country, List<Person> contacts) {
        client = checkClientForInssertDate(client);

        PersonType CONTACT_TYPE = personTypeRepository.CONTACT_PERSON();

        client.setSaleManager(saleManager);
        client.setCountry(country);
        client.setSegmentation(segmentation);
        client = clientRepository.save(client);

        /**
         * set first contact
         */
        if (contacts.size() > 0) {
            Person firstContact = contacts.get(0);
            firstContact.setPersonType(CONTACT_TYPE);
            firstContact = personRepository.save(firstContact);
            ClientToContactPerson ctcp = clientToContactPersonRepository.findBy(client, firstContact);
            clientToContactPersonRepository.save(ctcp);
            client.setFirstContactPerson(firstContact);
        }

        for (int i = 1; i < contacts.size(); i++) {
            Person contact = contacts.get(i);
            if (contact.getId() == null) {
                contact.setPersonType(CONTACT_TYPE);
                contact = personRepository.save(contact);
            }
            ClientToContactPerson ctcp = clientToContactPersonRepository.findBy(client, contact);
            clientToContactPersonRepository.save(ctcp);
        }

        clientRepository.save(client);
    }

    @Transactional
    public List<Client> getList() {
        try {
            return clientRepository.findAll();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * if client insertDate field is null then update it with new Date() value
     *
     * @param client
     * @return
     */
    private Client checkClientForInssertDate(Client client) {
        if (client.getInsertDate() == null) {
            client.setInsertDate(new Date());
        }
        return client;
    }

}
