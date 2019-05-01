/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
@Table(name = "project_detail")
@XmlRootElement
public class ProjectDetail implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 50)
    @Column(name = "logicalId")
    private String logicalId;
    @JoinColumn(name = "project_type_id", referencedColumnName = "id")
    @ManyToOne
    private ProjectType projectType;
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    @ManyToOne
    private Currency currency;
    @JoinColumn(name = "translator_id", referencedColumnName = "id")
    @ManyToOne
    private Translator translator;
    @Column(name = "units")
    private BigDecimal units;
    @Column(name = "price_per_unit")
    private BigDecimal pricePerUnit;
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @Column(name = "deadline_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deadlineDate;
    @Column(name = "deadline_hour")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deadlineHour;
    @Column(name = "paymentDelay")
    private BigDecimal paymentDelay;
    @JoinColumn(name = "source_language_id", referencedColumnName = "id")
    @ManyToOne
    private Language sourceLanguage;
    @JoinColumn(name = "destination_language_id", referencedColumnName = "id")
    @ManyToOne
    private Language destinatonLanguage;
    @Size(max = 4000)
    @Column(name = "system_notes")
    private String systemNotes;
    @Size(max = 4000)
    @Column(name = "notes")
    private String notes;
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Project project;    
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @ManyToOne
    private Status status;
    @Size(max = 4000)
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_uuid")
    private String fileUUID;
    @OneToMany(mappedBy = "projectDetail", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<ProjectDetailTranslatorStatus> projectDetailTranslatorStatuses;

    public ProjectDetail(){}
    
    public ProjectDetail(Project project, Status status){
        this.project = project;
        this.currency = project.getCurrency();
        this.deadlineDate = project.getDeadlineDate();
        this.deadlineHour = project.getDeadlineHour();
//        this.destinatonLanguage = project.getDestinatonLanguage();
        this.sourceLanguage = project.getSourceLanguage();
        this.units = project.getUnits();
        this.projectType = project.getProjectType();
        this.status = status;
    }
    
    public ProjectDetail(Project project, Translator translator, Status status){
        this(project, status);
        this.setTranslator(translator);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUUID() {
        return fileUUID;
    }

    public void setFileUUID(String fileUUID) {
        this.fileUUID = fileUUID;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(ProjectType projectType) {
        this.projectType = projectType;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getUnits() {
        return units;
    }

    public void setUnits(BigDecimal units) {
        this.units = units;
    }

    public BigDecimal getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(BigDecimal pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDeadlineDate() {
        return deadlineDate;
    }

    public void setDeadlineDate(Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public Date getDeadlineHour() {
        return deadlineHour;
    }

    public void setDeadlineHour(Date deadlineHour) {
        this.deadlineHour = deadlineHour;
    }

    public BigDecimal getPaymentDelay() {
        return paymentDelay;
    }

    public void setPaymentDelay(BigDecimal paymentDelay) {
        this.paymentDelay = paymentDelay;
    }

    public Language getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(Language sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public Language getDestinatonLanguage() {
        return destinatonLanguage;
    }

    public void setDestinatonLanguage(Language destinatonLanguage) {
        this.destinatonLanguage = destinatonLanguage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getLogicalId() {
        return logicalId;
    }

    public void setLogicalId(String logicalId) {
        this.logicalId = logicalId;
    }

    public String getSystemNotes() {
        return systemNotes;
    }

    public void setSystemNotes(String systemNotes) {
        this.systemNotes = systemNotes;
    }

    public Collection<ProjectDetailTranslatorStatus> getProjectDetailTranslatorStatuses() {
        return projectDetailTranslatorStatuses;
    }

    public void setProjectDetailTranslatorStatuses(Collection<ProjectDetailTranslatorStatus> projectDetailTranslatorStatuses) {
        this.projectDetailTranslatorStatuses = projectDetailTranslatorStatuses;
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
        if (!(object instanceof ProjectDetail)) {
            return false;
        }
        ProjectDetail other = (ProjectDetail) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tms.model.entity.ProjectDetail[ id=" + id + " ]";
    }
}