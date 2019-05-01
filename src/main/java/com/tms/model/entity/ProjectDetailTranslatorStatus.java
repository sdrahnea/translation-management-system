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
@Table(name = "pd_translator_status")
@XmlRootElement
public class ProjectDetailTranslatorStatus implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "project_detail_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private ProjectDetail projectDetail;
    @JoinColumn(name = "translator_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Translator translator;
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Status status;
    
    public ProjectDetailTranslatorStatus(){
    
    }
    
    public ProjectDetailTranslatorStatus(ProjectDetail projectDetail, Translator translator, Status status){
        this.projectDetail = projectDetail;
        this.translator = translator;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProjectDetail getProjectDetail() {
        return projectDetail;
    }

    public void setProjectDetail(ProjectDetail projectDetail) {
        this.projectDetail = projectDetail;
    }

    public Translator getTranslator() {
        return translator;
    }

    public void setTranslator(Translator translator) {
        this.translator = translator;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
}
