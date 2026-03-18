/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.webservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tms.model.entity.Translator;
import com.tms.model.entity.TranslatorToPaymentMethod;
import com.tms.model.entity.dao.CatDao;
import com.tms.model.entity.dao.CurrencyDao;
import com.tms.model.entity.dao.EducationDegreeDao;
import com.tms.model.entity.dao.LanguageDao;
import com.tms.model.entity.dao.PaymentMethodDao;
import com.tms.model.entity.dao.ServiceProvidedDao;
import com.tms.model.entity.dao.TranslationAreaDao;
import com.tms.model.entity.dao.TranslatorDao;
import com.tms.model.entity.service.TranslatorService;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tms.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author sdrahnea
 */
@WebServlet(urlPatterns = "/aws", loadOnStartup = 1)
public class ApplicantService extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private CountryRepository countryRepository;

    @Autowired
    private EducationDegreeDao educationDegreeDao;

    @Autowired
    private LanguageDao languageDao;

    @Autowired
    private CatDao catDao;

    @Autowired
    private CurrencyDao curencyDao;

    @Autowired
    private ServiceProvidedDao serviceProvidedDao;

    @Autowired
    private TranslationAreaDao translationAreaDao;

    @Autowired
    private PaymentMethodDao paymentMethodDao;

    @Autowired
    private TranslatorService translatorService;

    @Autowired
    private TranslatorDao translatorDao;

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isException = false;
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        String jsonAppliant = request.getParameter("applicant");
        try {
            JsonNode jsonObject = OBJECT_MAPPER.readTree(jsonAppliant);

            Translator translator = new Translator();
            translator.setName(jsonObject.path("name").asText(null));
            translator.setEmail(jsonObject.path("email").asText(null));
            translator.setContactPhone(jsonObject.path("contactPhone").asText(null));
            translator.setAlternativeContact(jsonObject.path("alternativeContact").asText(null));
            //translator.setCountry(countryDao.saveIfNotExist((String) jsonObject.get("country")));
            //translator.setEducationDegree(educationDegreeDao.saveIfNotExist((String) jsonObject.get("educationDegree")));
            translator.setYearsOfExperience(BigDecimal.valueOf(jsonObject.path("yearsOfExperience").asLong()));
            //translator.setInputLanguage1(languageDao.saveIfNotExist((String) jsonObject.get("inputLanguage1")));
            //translator.setInputLanguage2(languageDao.saveIfNotExist((String) jsonObject.get("inputLanguage2")));
            //translator.setInputLanguage3(languageDao.saveIfNotExist((String) jsonObject.get("inputLanguage3")));
            //translator.setOutputLanguage(languageDao.saveIfNotExist((String) jsonObject.get("outputLanguage")));
            //translator.setCat(catDao.saveIfNotExist((String) jsonObject.get("cat")));
            translator.setTranslatorRate(BigDecimal.valueOf(jsonObject.path("translatorRate").asLong()));
            translator.setProofReadingRate(BigDecimal.valueOf(jsonObject.path("proofReadingRate").asLong()));
            translator.setMinimumRate(BigDecimal.valueOf(jsonObject.path("minimumRate").asLong()));
            //translator.setTranslatorRateCurrency(curencyDao.saveIfNotExist((String) jsonObject.get("translatorRateCurrency")));
            //translator.setProofReadingRateCurrency(curencyDao.saveIfNotExist((String) jsonObject.get("proofReadingRateCurrency")));
            //translator.setMinimumRateCurrency(curencyDao.saveIfNotExist((String) jsonObject.get("minimumRateCurrency")));

            List<TranslatorToPaymentMethod> translatorToPaymentMethodList = new LinkedList<>();
            JsonNode paymentMethodList = jsonObject.path("paymentMethodList");
            for (JsonNode rec : paymentMethodList) {
                //PaymentMethod paymentMethod = paymentMethodDao.saveIfNotExist(rec.path("description").asText());
                //TranslatorToPaymentMethod t = new TranslatorToPaymentMethod(paymentMethod, rec.path("description").asText());
                //translatorToPaymentMethodList.add(t);
            }
            //translator.setServiceProvided(serviceProvidedDao.saveIfNotExist((String) jsonObject.get("serviceProvided")));
            translator.setDailyWordsOutput(jsonObject.path("dailyWordsOutput").asText(null));
            translator.setReferences(jsonObject.path("references").asText(null));
            translator.setLinkToProz(jsonObject.path("linkToProz").asText(null));
            //translator.setTranslationArea(translationAreaDao.saveIfNotExist((String) jsonObject.get("translationArea")));
            translator.setIsAgreementSign(jsonObject.path("isAgreementSign").asBoolean());
            translator.setIsEnabled(false);

            translatorService.create(translator, translatorToPaymentMethodList);

        } catch (Exception ex) {
            System.out.println("" + ex);
            out.println("{\"operationStatus\" : \"the following shit happened: " + ex + "\"}");
        }
        if (!isException) {
            out.println("{\"operationStatus\" : \"SUCCESS\"}");
        }
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("Hello World!");
    }
}
