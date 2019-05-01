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
import java.util.LinkedList;
import java.util.List;
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
@Table(name = "project")
@XmlRootElement
public class Project implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "parent_id")
    private Integer parentId;
    @JoinColumn(name = "project_type_id", referencedColumnName = "id")
    @ManyToOne
    private ProjectType projectType;
    @JoinColumn(name = "translation_area_id", referencedColumnName = "id")
    @ManyToOne
    private TranslationArea translationArea;
    @JoinColumn(name = "cat_id", referencedColumnName = "id")
    @ManyToOne
    private Cat cat;
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    @ManyToOne
    private Client client;
    @JoinColumn(name = "contact_person_id", referencedColumnName = "id")
    @ManyToOne
    private Person contactPerson;
    @JoinColumn(name = "manager_id", referencedColumnName = "id")
    @ManyToOne
    private Person saleManager;
    @Column(name = "deadline_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deadlineDate;
    @Column(name = "deadline_hour")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deadlineHour;
    @JoinColumn(name = "assigned_manager_id", referencedColumnName = "id")
    @ManyToOne
    private Person assignedManager;
    @JoinColumn(name = "second_manager_id", referencedColumnName = "id")
    @ManyToOne
    private Person secondManager;
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    @ManyToOne
    private Currency currency;
    @JoinColumn(name = "time_zone_id", referencedColumnName = "id")
    @ManyToOne
    private TimeZone timeZone;
    @Column(name = "units")
    private BigDecimal units;
    @Column(name = "price_per_unit")
    private BigDecimal pricePerUnit;
    @Column(name = "total_price")
    private BigDecimal totalPrice;
    @Column(name = "client_expected_payment")
    private BigDecimal clientExpectedPayment;
    @Column(name = "expected_payment")
    private BigDecimal expectedPayment;
    @Column(name = "actual_payment")
    private BigDecimal actualPayment;
    @Column(name = "loss")
    private BigDecimal loss;
    @Column(name = "translator_payment")
    private BigDecimal translatorPayment;
    @Column(name = "client_actual_payment")
    private BigDecimal clientActualPayment;
    @Column(name = "translator_actual_payment")
    private BigDecimal translatorActualPayment;
    @Size(max = 255)
    @Column(name = "client_po")
    private String clientPONumber;
    @Column(name = "paymentDelay")
    private BigDecimal paymentDelay;
    @JoinColumn(name = "source_language_id", referencedColumnName = "id")
    @ManyToOne
    private Language sourceLanguage;
    @JoinColumn(name = "destination_language_id", referencedColumnName = "id")
    @ManyToOne
    private Language destinatonLanguage;
    @Size(max = 4000)
    @Column(name = "notes")
    private String notes;
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<ProjectDetail> projectDetails;
    @Column(name = "insert_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertDate;
    @JoinColumn(name = "status_id", referencedColumnName = "id")
    @ManyToOne
    private Status status;
    @Column(name = "name")
    private String name;
    @JoinColumn(name = "industry_id", referencedColumnName = "id")
    @ManyToOne
    private Industry industry;
    @Column(name = "client_payment_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date clientPaymentDate;
    @Column(name = "translator_payment_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date translatorPaymentDate;
    @JoinColumn(name = "translator_id", referencedColumnName = "id")
    @ManyToOne
    private Translator translator;

    public void addProjectDetail(ProjectDetail pd) {
        if (projectDetails == null) {
            projectDetails = new LinkedList<>();
        }

        if (pd != null) {
            projectDetails.add(pd);
        }
    }

    public List<ProjectDetail> getProjectDetail(Translator translator) {
        List<ProjectDetail> result = new LinkedList<>();
        for (ProjectDetail detail : projectDetails) {
            if (detail.getTranslator() != null) {
                if (detail.getTranslator().getId() == translator.getId()) {
                    result.add(detail);
                }
            }
        }
        return result;
    }

    public boolean isProjectHandledBy(Translator translator) {
        for (ProjectDetail pd : projectDetails) {
            if (pd.getTranslator() != null && pd.getTranslator().getId() == translator.getId()) {
                return true;
            }
        }
        return false;
    }

    public Project() {
    }

    public Project(Project project) {
        this.projectType = project.getProjectType();
        this.translationArea = project.getTranslationArea();
        this.cat = project.getCat();
        this.client = project.getClient();
        this.contactPerson = project.getContactPerson();
        this.saleManager = project.getSaleManager();
        this.deadlineDate = project.getDeadlineDate();
        this.deadlineHour = project.getDeadlineHour();
        this.assignedManager = project.getAssignedManager();
        this.secondManager = project.getSecondManager();
        this.currency = project.getCurrency();
        this.units = project.getUnits();
        this.pricePerUnit = project.getPricePerUnit();
        this.totalPrice = project.getTotalPrice();
        this.clientPONumber = project.getClientPONumber();
        this.paymentDelay = project.getPaymentDelay();
        this.sourceLanguage = project.getSourceLanguage();
        this.destinatonLanguage = project.getDestinatonLanguage();
        this.notes = project.getNotes();
        this.status = project.getStatus();
        this.name = project.getName();
        this.industry = project.getIndustry();
    }

    public TimeZone getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public Translator getTranslator() {
        return translator;
    }

    public void setTranslator(Translator translator) {
        this.translator = translator;
    }

    public Date getClientPaymentDate() {
        if (clientPaymentDate == null) {
            clientPaymentDate = deadlineDate;
        }
        return clientPaymentDate;
    }

    public void setClientPaymentDate(Date clientPaymentDate) {
        this.clientPaymentDate = clientPaymentDate;
    }

    public Date getTranslatorPaymentDate() {
        return translatorPaymentDate;
    }

    public void setTranslatorPaymentDate(Date translatorPaymentDate) {
        this.translatorPaymentDate = translatorPaymentDate;
    }

    public BigDecimal getClientExpectedPayment() {
        if (clientExpectedPayment == null) {
            clientExpectedPayment = new BigDecimal(0);
        }
        return clientExpectedPayment;
    }

    public void setClientExpectedPayment(BigDecimal clientExpectedPayment) {
        this.clientExpectedPayment = clientExpectedPayment;
    }

    public BigDecimal getClientActualPayment() {
        if (clientActualPayment == null) {
            clientActualPayment = new BigDecimal(0);
        }
        return clientActualPayment;
    }

    public void setClientActualPayment(BigDecimal clientActualPayment) {
        this.clientActualPayment = clientActualPayment;
    }

    public BigDecimal getTranslatorActualPayment() {
        if (translatorActualPayment == null) {
            translatorActualPayment = new BigDecimal(0);
        }
        return translatorActualPayment;
    }

    public void setTranslatorActualPayment(BigDecimal translatorActualPayment) {
        this.translatorActualPayment = translatorActualPayment;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Industry getIndustry() {
        return industry;
    }

    public void setIndustry(Industry industry) {
        this.industry = industry;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public TranslationArea getTranslationArea() {
        return translationArea;
    }

    public void setTranslationArea(TranslationArea translationArea) {
        this.translationArea = translationArea;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public BigDecimal getTranslatorPayment() {
        if (translatorPayment == null){
            translatorPayment = BigDecimal.ZERO;
        }
        return translatorPayment;
    }

    public void setTranslatorPayment(BigDecimal translatorPayment) {
        this.translatorPayment = translatorPayment;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Person getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(Person contactPerson) {
        this.contactPerson = contactPerson;
    }

    public Person getSaleManager() {
        return saleManager;
    }

    public void setSaleManager(Person saleManager) {
        this.saleManager = saleManager;
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

    public Person getAssignedManager() {
        return assignedManager;
    }

    public void setAssignedManager(Person assignedManager) {
        this.assignedManager = assignedManager;
    }

    public Person getSecondManager() {
        return secondManager;
    }

    public void setSecondManager(Person secondManager) {
        this.secondManager = secondManager;
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

    public String getClientPONumber() {
        return clientPONumber;
    }

    public void setClientPONumber(String clientPONumber) {
        this.clientPONumber = clientPONumber;
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

    public Collection<ProjectDetail> getProjectDetails() {
        return projectDetails;
    }

    public void setProjectDetails(Collection<ProjectDetail> projectDetails) {
        this.projectDetails = projectDetails;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public BigDecimal getExpectedPayment() {
        return expectedPayment;
    }

    public void setExpectedPayment(BigDecimal expectedPayment) {
        this.expectedPayment = expectedPayment;
    }

    public BigDecimal getActualPayment() {
        return actualPayment;
    }

    public void setActualPayment(BigDecimal actualPayment) {
        this.actualPayment = actualPayment;
    }

    public BigDecimal getLoss() {
        if (loss == null){
            loss = BigDecimal.ZERO;
        }
        return loss;
    }

    public void setLoss(BigDecimal loss) {
        this.loss = loss;
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
        if (!(object instanceof Project)) {
            return false;
        }
        Project other = (Project) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tms.model.entity.Project[ id=" + id + " ]";
    }
}
