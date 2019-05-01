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
@Table(name = "translator_translation_area")
@XmlRootElement
public class TranslatorToTranslationArea implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "translator_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Translator translator;
    @JoinColumn(name = "translation_area_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private TranslationArea translationArea;

    public TranslatorToTranslationArea() {
    }

    public TranslatorToTranslationArea(Translator translator, TranslationArea translationArea) {
        this.translator = translator;
        this.translationArea = translationArea;
    }

    public Translator getTranslator() {
        return translator;
    }

    public void setTranslator(Translator translator) {
        this.translator = translator;
    }

    public TranslationArea getTranslationArea() {
        return translationArea;
    }

    public void setTranslationArea(TranslationArea translationArea) {
        this.translationArea = translationArea;
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
        if (!(object instanceof TranslatorToTranslationArea)) {
            return false;
        }
        TranslatorToTranslationArea other = (TranslatorToTranslationArea) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return translationArea == null ? "" : (translationArea.getName() == null ? "" : translationArea.getName());
    }
}
