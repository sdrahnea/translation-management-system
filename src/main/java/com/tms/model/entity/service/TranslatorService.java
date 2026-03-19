/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.service;

import com.tms.model.entity.Cat;
import com.tms.model.entity.Country;
import com.tms.model.entity.Currency;
import com.tms.model.entity.EducationDegree;
import com.tms.model.entity.Language;
import com.tms.model.entity.ServiceProvided;
import com.tms.model.entity.TranslationArea;
import com.tms.model.entity.Translator;
import com.tms.model.entity.TranslatorFeedback;
import com.tms.model.entity.TranslatorToCat;
import com.tms.model.entity.TranslatorToPaymentMethod;
import com.tms.model.entity.TranslatorToServcieProvided;
import com.tms.model.entity.TranslatorToTranslationArea;
import com.tms.repository.PaymentMethodRepository;
import com.tms.repository.TranslatorFeedbackRepository;
import com.tms.repository.TranslatorRepository;
import com.tms.repository.TranslatorToCatRepository;
import com.tms.repository.TranslatorToPaymentMethodRepository;
import com.tms.repository.TranslatorToServcieProvidedRepository;
import com.tms.repository.TranslatorToTranslationAreaRepository;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.Parameter;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Service
public class TranslatorService implements Serializable {

    @Autowired
    private TranslatorRepository translatorRepository;

    @Autowired
    private TranslatorToCatRepository translatorToCatRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private TranslatorToPaymentMethodRepository translatorToPaymentMethodRepository;

    @Autowired
    private TranslatorToTranslationAreaRepository translatorToTranslationAreaRepository;

    @Autowired
    private TranslatorToServcieProvidedRepository translatorToServcieProvidedRepository;

    @Autowired
    private TranslatorFeedbackRepository translatorFeedbackRepository;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * isEnabled is true for translators and isEnabled is false for applicants
     *
     * @param isEnabled
     * @return
     */
    @Transactional(readOnly = true)
    public List<Translator> getAll(Boolean isEnabled) {
        Map<String, Object> pars = new HashMap<>();
        pars.put("isEnabled", isEnabled);

        StringBuilder sql = new StringBuilder("FROM Translator t WHERE (1 = 1) ");
        if (isEnabled != null) {
            sql.append(" AND (isEnabled is :isEnabled)");
        }

        Query query = entityManager.createQuery(sql.toString());
        for (Parameter p : query.getParameters()) {
            query.setParameter(p, pars.get(p.getName()));
        }
        return query.getResultList();
    }

    /**
     * search by lower fields
     *
     * @param startDate
     * @param endDate
     * @param inputLanguage1
     * @param outputLanguage
     * @param translationArea
     * @param country
     * @param name
     * @param email
     * @param serviceProvided
     * @param isEnabled
     * @return
     */
    @Transactional(readOnly = true)
    public List<Translator> search(Date startDate, Date endDate,
            Language inputLanguage1, Language outputLanguage,
            TranslationArea translationArea, Country country,
            String name, String email, ServiceProvided serviceProvided, Boolean isEnabled) {
        Map<String, Object> pars = new HashMap<>();
        pars.put("startDate", startDate);
        pars.put("endDate", endDate);
        pars.put("inputLanguage1", inputLanguage1);
        pars.put("outputLanguage", outputLanguage);
        pars.put("translationArea", translationArea);
        pars.put("country", country);
        pars.put("name", name);
        pars.put("email", email);
        pars.put("serviceProvided", serviceProvided);
        pars.put("isEnabled", isEnabled);

        StringBuilder sql = new StringBuilder("FROM Translator t WHERE (1 = 1) ");
        if (startDate != null) {
            sql.append(" AND (insertDate >= :startDate)");
        }
        if (endDate != null) {
            sql.append(" AND (insertDate <= :endDate)");
        }
        if (inputLanguage1 != null) {
            sql.append(" AND (inputLanguage1 = :inputLanguage1)");
        }
        if (outputLanguage != null) {
            sql.append(" AND (outputLanguage = :outputLanguage)");
        }
        if (translationArea != null) {
            sql.append(" AND (translationArea = :translationArea)");
        }
        if (country != null) {
            sql.append(" AND (country = :country)");
        }
        if (name != null && !name.isEmpty()) {
            sql.append(" AND (name LIKE CONCAT('%', :name, '%'))");
        }
        if (email != null && !email.isEmpty()) {
            sql.append(" AND (email LIKE CONCAT('%', :email, '%'))");
        }
        if (serviceProvided != null) {
            sql.append(" AND (serviceProvided = :serviceProvided)");
        }
        if (isEnabled != null) {
            sql.append(" AND (isEnabled is :isEnabled)");
        }

        Query query = entityManager.createQuery(sql.toString());
        for (Parameter p : query.getParameters()) {
            query.setParameter(p, pars.get(p.getName()));
        }

        return query.getResultList();
    }

    /**
     * change isEnabled field value to true. This allow object to be in
     * translator list
     *
     * @param translator
     */
    @Transactional
    public void changeToTranslator(Translator translator) {
        translator.setIsEnabled(true);
        translatorRepository.save(translator);
    }

    /**
     * create a translator based on input translator object and payment methods
     * list
     *
     * @param translator
     * @param translatorToPaymentMethodList
     */
    @Transactional
    public void create(Translator translator, List<TranslatorToPaymentMethod> translatorToPaymentMethodList) {
        translator.setIsEnabled(true);
        translatorRepository.save(translator);

        for (TranslatorToPaymentMethod ttpm : translatorToPaymentMethodList) {
            paymentMethodRepository.save(ttpm.getPaymentMethod());
            ttpm.setTranslator(translator);
            translatorToPaymentMethodRepository.save(ttpm);
        }
    }

    /**
     * create a translator based on lower entered fields
     *
     * @param translator
     * @param country
     * @param educationDegree
     * @param inputLanguage1
     * @param inputLanguage2
     * @param inputLanguage3
     * @param outputLanguage
     * @param minimumRateCurrency
     * @param proofReadingRateCurrency
     * @param translatorRateCurrency
     * @param translationArea
     * @param serviceProvided
     * @param catList
     * @param translatorToPaymentMethodList
     * @param serviceProvidedList
     * @param translationAreaList
     * @param translatorFeedback
     */
    @Transactional
    public void create(Translator translator, final Country country, final EducationDegree educationDegree,
            final Language inputLanguage1, final Language inputLanguage2, final Language inputLanguage3, final Language outputLanguage,
            final Currency minimumRateCurrency, final Currency proofReadingRateCurrency, final Currency translatorRateCurrency,
            final TranslationArea translationArea, final ServiceProvided serviceProvided,
            Cat[] catList,
            List<TranslatorToPaymentMethod> translatorToPaymentMethodList,
            final ServiceProvided[] serviceProvidedList,
            final TranslationArea[] translationAreaList,
            final TranslatorFeedback translatorFeedback
    ) {
        translator.setCountry(country);
        translator.setEducationDegree(educationDegree);
        translator.setInputLanguage1(inputLanguage1);
        translator.setInputLanguage2(inputLanguage2);
        translator.setInputLanguage3(inputLanguage3);
        translator.setOutputLanguage(outputLanguage);
        translator.setMinimumRateCurrency(minimumRateCurrency);
        translator.setProofReadingRateCurrency(proofReadingRateCurrency);
        translator.setTranslatorRateCurrency(translatorRateCurrency);
        translator.setTranslationArea(translationArea);
        translator.setServiceProvided(serviceProvided);
        translator.setIsEnabled(true);
        translator = translatorRepository.save(translator);

//        TranslatorFeedback lastTranslatorFeedback = translator.getLastTranslatorFeedback();
//        if (lastTranslatorFeedback.getRatingAsNumber() != translatorFeedback.getRatingAsNumber()
//                || !lastTranslatorFeedback.getComment().equalsIgnoreCase(translatorFeedback.getComment())) {
            TranslatorFeedback tf = new TranslatorFeedback();
            tf.setTranslator(translator);
            tf.setRatingAsNumber(translatorFeedback.getRatingAsNumber());
            tf.setComment(translatorFeedback.getComment());
            translatorFeedbackRepository.save(tf);

//            translator.getTranslatorFeedbacks().add(translatorFeedback);
//            translator = translatorDao.merge(translator);
//        } 

        translatorToCatRepository.removeBy(translator);
        for (Cat scat : catList) {
            TranslatorToCat join = translatorToCatRepository.find(translator, scat);
            translatorToCatRepository.save(join);
        }

        for (TranslatorToPaymentMethod ttpm : translatorToPaymentMethodList) {
            ttpm.setTranslator(translator);
            paymentMethodRepository.save(ttpm.getPaymentMethod());
            translatorToPaymentMethodRepository.save(ttpm);
        }

        translatorToServcieProvidedRepository.removeBy(translator);
        for (ServiceProvided ssp : serviceProvidedList) {
            TranslatorToServcieProvided join = translatorToServcieProvidedRepository.find(translator, ssp);
            translatorToServcieProvidedRepository.save(join);
        }

        translatorToTranslationAreaRepository.removeBy(translator);
        for (TranslationArea sta : translationAreaList) {
            TranslatorToTranslationArea join = translatorToTranslationAreaRepository.find(translator, sta);
            translatorToTranslationAreaRepository.save(join);
        }
    }

}
