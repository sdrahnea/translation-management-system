/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sdrahnea
 */
@Entity
@Table(name = "translator_servide_provided")
@XmlRootElement
public class TranslatorToServcieProvided implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "translator_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Translator translator;
    @JoinColumn(name = "service_provided_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private ServiceProvided serviceProvided;

    public TranslatorToServcieProvided() {
    }

    public TranslatorToServcieProvided(Translator translator, ServiceProvided serviceProvided) {
        this.translator = translator;
        this.serviceProvided = serviceProvided;
    }

    public Translator getTranslator() {
        return translator;
    }

    public void setTranslator(Translator translator) {
        this.translator = translator;
    }

    public ServiceProvided getServiceProvided() {
        return serviceProvided;
    }

    public void setServiceProvided(ServiceProvided serviceProvided) {
        this.serviceProvided = serviceProvided;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TranslatorToServcieProvided)) {
            return false;
        }
        TranslatorToServcieProvided other = (TranslatorToServcieProvided) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return serviceProvided == null ? "" : (serviceProvided.getName() == null ? "" : serviceProvided.getName());
    }
}
