/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "translator_feedback")
@XmlRootElement
public class TranslatorFeedback implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    @ManyToOne
    private Project project;
    @JoinColumn(name = "translator_id", referencedColumnName = "id")
    @ManyToOne
    private Translator translator;
    @JoinColumn(name = "rating_id", referencedColumnName = "id")
    @ManyToOne
    private Rating rating;
    @Column(name = "rating_as_number")
    private Integer ratingAsNumber;
    @Size(max = 255)
    @Column(name = "comment")
    private String comment;
    @Column(name = "insert_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertDate;

    @PrePersist
    protected void onCreate() {
        if (insertDate == null) {
            insertDate = new Date();
        }
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Translator getTranslator() {
        return translator;
    }

    public void setTranslator(Translator translator) {
        this.translator = translator;
    }

    public Rating getRating() {
        if (rating == null) {
            return new Rating();
        }
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRatingAsNumber() {
        return ratingAsNumber;
    }

    public void setRatingAsNumber(Integer ratingAsNumber) {
        this.ratingAsNumber = ratingAsNumber;
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
        if (!(object instanceof TranslatorFeedback)) {
            return false;
        }
        TranslatorFeedback other = (TranslatorFeedback) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tms.model.entity.TranslatorFeedback[ id=" + id + " ]";
    }
}
