/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author sdrahnea
 */
@Entity
@Table(name = "education_degree")
@XmlRootElement
public class EducationDegree extends CoreEntity implements Serializable {

    @Size(max = 50)
    @Column(name = "name")
    private String name;
    @Column(name = "rank")
    private Integer rank;

    public EducationDegree() {
    }

    public EducationDegree(String name) {
        this.name = name;
    }
    
    public EducationDegree(String name, Integer rank) {
        this.name = name;
        this.rank = rank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EducationDegree)) {
            return false;
        }
        EducationDegree other = (EducationDegree) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tms.model.entity.EducationDegree[ id=" + getId() + " ]";
    }
}
