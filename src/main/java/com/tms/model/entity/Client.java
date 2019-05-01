/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sdrahnea
 */
@Entity
@Table(name = "client")
@XmlRootElement
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "name")
    private String name;
    @JoinColumn(name = "segmentation_id", referencedColumnName = "id")
    @ManyToOne(fetch=FetchType.EAGER)
    private Segmentation segmentation;
    @JoinColumn(name = "coutry_id", referencedColumnName = "id")
    @ManyToOne
    private Country country;
    @Size(max = 255)
    @Column(name = "address")
    private String address;
    @Size(max = 255)
    @Column(name = "invoice_email")
    private String invoiceEmail;
    @Size(max = 255)
    @Column(name = "vat")
    private String vat;
    @Size(max = 255)
    @Column(name = "website")
    private String website;
    @Size(max = 255)
    @Column(name = "description")
    private String description;
    @JoinColumn(name = "sale_manager_id", referencedColumnName = "id")
    @ManyToOne(fetch=FetchType.EAGER)
    private Person saleManager;
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    @ManyToOne(fetch=FetchType.EAGER)
    private Person firstContactPerson;
    @OneToMany(mappedBy = "client", fetch=FetchType.EAGER)
    private Collection<ClientToContactPerson> clientToContactPersons;

    @Column(name = "insert_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertDate;

    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne(fetch=FetchType.EAGER)
    private User user;

    public Client() {
    }

    public Client(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Client(String name) {
        this.name = name;
    }

    @PrePersist
    protected void onCreate() {
        if (insertDate == null) {
            insertDate = new Date();
        }
    }

    public Person getFirstContactPerson() {
        return firstContactPerson;
    }

    public void setFirstContactPerson(Person firstContactPerson) {
        this.firstContactPerson = firstContactPerson;
    }


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Segmentation getSegmentation() {
        return segmentation;
    }

    public void setSegmentation(Segmentation segmentation) {
        this.segmentation = segmentation;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getInvoiceEmail() {
        return invoiceEmail;
    }

    public void setInvoiceEmail(String invoiceEmail) {
        this.invoiceEmail = invoiceEmail;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Person getSaleManager() {
        return saleManager;
    }

    public void setSaleManager(Person saleManager) {
        this.saleManager = saleManager;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    public Collection<ClientToContactPerson> getClientToContactPersons() {
        return clientToContactPersons;
    }

    public void setClientToContactPersons(Collection<ClientToContactPerson> clientToContactPersons) {
        this.clientToContactPersons = clientToContactPersons;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Client)) {
            return false;
        }
        Client other = (Client) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tms.model.entity.Client[ id=" + id + " ]";
    }
}
