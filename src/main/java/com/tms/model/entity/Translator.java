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
import org.hibernate.annotations.Type;
/**
 *
 * @author sdrahnea
 */
@Entity
@Table(name = "translator")
@XmlRootElement
public class Translator implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Size(max = 255)
    @Column(name = "name")
    private String name;
    @Size(max = 255)
    @Column(name = "email")
    private String email;
    @Size(max = 255)
    @Column(name = "contact_phone")
    private String contactPhone;
    @Size(max = 255)
    @Column(name = "alternative_contact")
    private String alternativeContact;
    @JoinColumn(name = "country_id", referencedColumnName = "id")
    @ManyToOne
    private Country country;
    @JoinColumn(name = "education_degree_id", referencedColumnName = "id")
    @ManyToOne
    private EducationDegree educationDegree;
    @Column(name = "years_of_experience")
    private BigDecimal yearsOfExperience;
    @JoinColumn(name = "input_language1_id", referencedColumnName = "id")
    @ManyToOne
    private Language inputLanguage1;
    @JoinColumn(name = "input_language2_id", referencedColumnName = "id")
    @ManyToOne
    private Language inputLanguage2;
    @JoinColumn(name = "input_language3_id", referencedColumnName = "id")
    @ManyToOne
    private Language inputLanguage3;
    @JoinColumn(name = "output_language_id", referencedColumnName = "id")
    @ManyToOne
    private Language outputLanguage;
    @JoinColumn(name = "cat_id", referencedColumnName = "id")
    @ManyToOne
    private Cat cat;
    @Column(name = "translator_rate")
    private BigDecimal translatorRate;
    @Column(name = "proof_reading_rate")
    private BigDecimal proofReadingRate;
    @Column(name = "minimum_rate")
    private BigDecimal minimumRate;
    @JoinColumn(name = "translator_rate_currency", referencedColumnName = "id")
    @ManyToOne
    private Currency translatorRateCurrency;
    @JoinColumn(name = "proof_reading_rate_currency", referencedColumnName = "id")
    @ManyToOne
    private Currency proofReadingRateCurrency;
    @JoinColumn(name = "minimum_rate_currency", referencedColumnName = "id")
    @ManyToOne
    private Currency minimumRateCurrency;
    @JoinColumn(name = "payment_method_id", referencedColumnName = "id")
    @ManyToOne
    private PaymentMethod paymentMethod;
    @OneToMany(mappedBy = "translator", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<TranslatorToPaymentMethod> translatorToPaymentMethods;  
    @JoinColumn(name = "service_provided_id", referencedColumnName = "id")
    @ManyToOne
    private ServiceProvided serviceProvided;
    @Size(max = 255)
    @Column(name = "daily_words_output")
    private String dailyWordsOutput;
    @Size(max = 255)
    @Column(name = "reference")
    private String references;
    @Size(max = 255)
    @Column(name = "link_to_proz")
    private String linkToProz;
    @JoinColumn(name = "translation_area_id", referencedColumnName = "id")
    @ManyToOne
    private TranslationArea translationArea;
    @Column(name = "is_agreement_sign")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isAgreementSign;
    @Size(max = 255)
    @Column(name = "cv_file")
    private String cvFile;
    @Column(name = "is_enabled")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private Boolean isEnabled;
    @Column(name = "insert_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date insertDate;
    @OneToMany(mappedBy = "translator", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<TranslatorFeedback> translatorFeedbacks;
    @OneToMany(mappedBy = "translator", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<TranslatorToCat> translatorToCats;
    @OneToMany(mappedBy = "translator", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<TranslatorToServcieProvided> translatorToServcieProvideds;
    @OneToMany(mappedBy = "translator", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Collection<TranslatorToTranslationArea> translatorToTranslationAreas;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private User user;
    @Size(max = 4000)
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_uuid")
    private String fileUUID;

    public Translator() {
    }

    public Translator(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
    
    public Translator(String name) {
        this.name = name;
    }

    public Rating getLastRating() {
        if (getLastTranslatorFeedback() == null) {
            return new Rating();
        }
        return getLastTranslatorFeedback().getRating();
    }
    
    public Integer getLastRatingAsNumber() {
        return getLastTranslatorFeedback().getRatingAsNumber();
    }

    public String getLastComment() {
        return getLastTranslatorFeedback().getComment();
    }
    
    public Integer getMinimumRatingAsNumber() {
        if (translatorFeedbacks != null) {
            if (translatorFeedbacks.isEmpty()) {
                return 0;
            }

            if (translatorFeedbacks.size() == 1) {
                for (TranslatorFeedback feedback : translatorFeedbacks) {
                    return feedback.getRatingAsNumber();
                }
            }

            Integer minimumRating = 10;
            if (translatorFeedbacks.size() > 1) {
                for (TranslatorFeedback feedback : translatorFeedbacks) {
                    if (minimumRating  > feedback.getRatingAsNumber()
                            && feedback.getRatingAsNumber() != 0) {
                        minimumRating = feedback.getRatingAsNumber();
                    }
                }
            }
            return minimumRating;
        } else {
            return 0;
        }
    }

    public TranslatorFeedback getLastTranslatorFeedback() {
        TranslatorFeedback UNKNOW_FEEDBACK = new TranslatorFeedback();
        UNKNOW_FEEDBACK.setComment("No comment");
        UNKNOW_FEEDBACK.setRating(new Rating("No rating"));

        if (translatorFeedbacks != null) {
            if (translatorFeedbacks.isEmpty()) {
                return UNKNOW_FEEDBACK;
            }

            if (translatorFeedbacks.size() == 1) {
                for (TranslatorFeedback feedback : translatorFeedbacks) {
                    return feedback;
                }
            }

            TranslatorFeedback lastFeedback = null;
            if (translatorFeedbacks.size() > 1) {
                for (TranslatorFeedback feedback : translatorFeedbacks) {
                    if (lastFeedback == null) {
                        lastFeedback = feedback;
                    } else if (lastFeedback.getId() < feedback.getId()) {
                        lastFeedback = feedback;
                    }
                }
            }
            return lastFeedback;
        } else {
            return UNKNOW_FEEDBACK;
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAlternativeContact() {
        return alternativeContact;
    }

    public void setAlternativeContact(String alternativeContact) {
        this.alternativeContact = alternativeContact;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Collection<TranslatorToCat> getTranslatorToCats() {
        return translatorToCats;
    }

    public void setTranslatorToCats(Collection<TranslatorToCat> translatorToCats) {
        this.translatorToCats = translatorToCats;
    }

    public EducationDegree getEducationDegree() {
        return educationDegree;
    }

    public void setEducationDegree(EducationDegree educationDegree) {
        this.educationDegree = educationDegree;
    }

    public BigDecimal getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(BigDecimal yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public Language getInputLanguage1() {
        return inputLanguage1;
    }

    public void setInputLanguage1(Language inputLanguage1) {
        this.inputLanguage1 = inputLanguage1;
    }

    public Language getInputLanguage2() {
        return inputLanguage2;
    }

    public void setInputLanguage2(Language inputLanguage2) {
        this.inputLanguage2 = inputLanguage2;
    }

    public Language getInputLanguage3() {
        return inputLanguage3;
    }

    public void setInputLanguage3(Language inputLanguage3) {
        this.inputLanguage3 = inputLanguage3;
    }

    public Language getOutputLanguage() {
        return outputLanguage;
    }

    public void setOutputLanguage(Language outputLanguage) {
        this.outputLanguage = outputLanguage;
    }

    public Cat getCat() {
        return cat;
    }

    public void setCat(Cat cat) {
        this.cat = cat;
    }

    public BigDecimal getTranslatorRate() {
        return translatorRate;
    }

    public void setTranslatorRate(BigDecimal translatorRate) {
        this.translatorRate = translatorRate;
    }

    public BigDecimal getProofReadingRate() {
        return proofReadingRate;
    }

    public void setProofReadingRate(BigDecimal proofReadingRate) {
        this.proofReadingRate = proofReadingRate;
    }

    public BigDecimal getMinimumRate() {
        return minimumRate;
    }

    public void setMinimumRate(BigDecimal minimumRate) {
        this.minimumRate = minimumRate;
    }

    public Currency getTranslatorRateCurrency() {
        return translatorRateCurrency;
    }

    public void setTranslatorRateCurrency(Currency translatorRateCurrency) {
        this.translatorRateCurrency = translatorRateCurrency;
    }

    public Currency getProofReadingRateCurrency() {
        return proofReadingRateCurrency;
    }

    public void setProofReadingRateCurrency(Currency proofReadingRateCurrency) {
        this.proofReadingRateCurrency = proofReadingRateCurrency;
    }

    public Currency getMinimumRateCurrency() {
        return minimumRateCurrency;
    }

    public void setMinimumRateCurrency(Currency minimumRateCurrency) {
        this.minimumRateCurrency = minimumRateCurrency;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Collection<TranslatorToPaymentMethod> getTranslatorToPaymentMethods() {
        return translatorToPaymentMethods;
    }

    public void setTranslatorToPaymentMethods(Collection<TranslatorToPaymentMethod> translatorToPaymentMethods) {
        this.translatorToPaymentMethods = translatorToPaymentMethods;
    }

    public ServiceProvided getServiceProvided() {
        return serviceProvided;
    }

    public void setServiceProvided(ServiceProvided serviceProvided) {
        this.serviceProvided = serviceProvided;
    }

    public String getDailyWordsOutput() {
        return dailyWordsOutput;
    }

    public void setDailyWordsOutput(String dailyWordsOutput) {
        this.dailyWordsOutput = dailyWordsOutput;
    }

    public String getReferences() {
        return references;
    }

    public void setReferences(String references) {
        this.references = references;
    }

    public String getLinkToProz() {
        return linkToProz;
    }

    public void setLinkToProz(String linkToProz) {
        this.linkToProz = linkToProz;
    }

    public TranslationArea getTranslationArea() {
        return translationArea;
    }

    public void setTranslationArea(TranslationArea translationArea) {
        this.translationArea = translationArea;
    }

    public Boolean getIsAgreementSign() {
        return isAgreementSign;
    }

    public void setIsAgreementSign(Boolean isAgreementSign) {
        this.isAgreementSign = isAgreementSign;
    }

    public String getCvFile() {
        return cvFile;
    }

    public void setCvFile(String cvFile) {
        this.cvFile = cvFile;
    }

    public Boolean getIsEnabled() {
        return isEnabled;
    }

    public void setIsEnabled(Boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public Date getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public Collection<TranslatorFeedback> getTranslatorFeedbacks() {
        return translatorFeedbacks;
    }

    public void setTranslatorFeedbacks(Collection<TranslatorFeedback> translatorFeedbacks) {
        this.translatorFeedbacks = translatorFeedbacks;
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

    public Collection<TranslatorToServcieProvided> getTranslatorToServcieProvideds() {
        return translatorToServcieProvideds;
    }

    public void setTranslatorToServcieProvideds(Collection<TranslatorToServcieProvided> translatorToServcieProvideds) {
        this.translatorToServcieProvideds = translatorToServcieProvideds;
    }

    public Collection<TranslatorToTranslationArea> getTranslatorToTranslationAreas() {
        return translatorToTranslationAreas;
    }

    public void setTranslatorToTranslationAreas(Collection<TranslatorToTranslationArea> translatorToTranslationAreas) {
        this.translatorToTranslationAreas = translatorToTranslationAreas;
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
        if (!(object instanceof Translator)) {
            return false;
        }
        Translator other = (Translator) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.tms.model.entity.Translator[ id=" + id + " ]";
    }

}
