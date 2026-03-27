/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sdrahnea
 */
@Entity
@Table(name = "client_contact_person")
@XmlRootElement
public class ClientToContactPerson extends CoreEntity implements Serializable{

    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @ManyToOne
    private Client client;
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    @ManyToOne
    private Person person;

    public ClientToContactPerson(){}
    
    public ClientToContactPerson(Client client, Person person){
        this.client = client;
        this.person = person;
    }
    
    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ClientToContactPerson)) {
            return false;
        }
        ClientToContactPerson other = (ClientToContactPerson) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tms.model.entity.ClientToContactPerson[ id=" + getId() + " ]";
    }
}