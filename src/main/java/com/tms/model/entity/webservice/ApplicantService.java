/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.model.entity.webservice;

import com.tms.model.entity.Cat;
import com.tms.model.entity.Country;
import com.tms.model.entity.Currency;
import com.tms.model.entity.EducationDegree;
import com.tms.model.entity.Language;
import com.tms.model.entity.PaymentMethod;
import com.tms.model.entity.ServiceProvided;
import com.tms.model.entity.TranslationArea;
import com.tms.model.entity.Translator;
import com.tms.model.entity.TranslatorToPaymentMethod;
import com.tms.model.entity.dao.CatDao;
import com.tms.model.entity.dao.CountryDao;
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
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Size;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
/**
 *
 * @author sdrahnea
 */
@WebServlet(urlPatterns = "/aws", loadOnStartup = 1)
public class ApplicantService extends HttpServlet {

    private static final long serialVersionUID = 1L;
    
    @Autowired
    private CountryDao countryDao;
    
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
        JSONParser parser = new JSONParser();
        try {

            Object obj = parser.parse(jsonAppliant);
            JSONObject jsonObject = (JSONObject) obj;

            Translator translator = new Translator();
            translator.setName((String) jsonObject.get("name"));
            translator.setEmail((String) jsonObject.get("email"));
            translator.setContactPhone((String) jsonObject.get("contactPhone"));
            translator.setAlternativeContact((String) jsonObject.get("alternativeContact"));
            //translator.setCountry(countryDao.saveIfNotExist((String) jsonObject.get("country")));
            //translator.setEducationDegree(educationDegreeDao.saveIfNotExist((String) jsonObject.get("educationDegree")));
            translator.setYearsOfExperience(new BigDecimal((Long) jsonObject.get("yearsOfExperience")));
            //translator.setInputLanguage1(languageDao.saveIfNotExist((String) jsonObject.get("inputLanguage1")));
            //translator.setInputLanguage2(languageDao.saveIfNotExist((String) jsonObject.get("inputLanguage2")));
            //translator.setInputLanguage3(languageDao.saveIfNotExist((String) jsonObject.get("inputLanguage3")));
            //translator.setOutputLanguage(languageDao.saveIfNotExist((String) jsonObject.get("outputLanguage")));
            //translator.setCat(catDao.saveIfNotExist((String) jsonObject.get("cat")));
            translator.setTranslatorRate(new BigDecimal((Long) jsonObject.get("translatorRate")));
            translator.setProofReadingRate(new BigDecimal((Long) jsonObject.get("proofReadingRate")));
            translator.setMinimumRate(new BigDecimal((Long) jsonObject.get("minimumRate")));
            //translator.setTranslatorRateCurrency(curencyDao.saveIfNotExist((String) jsonObject.get("translatorRateCurrency")));
            //translator.setProofReadingRateCurrency(curencyDao.saveIfNotExist((String) jsonObject.get("proofReadingRateCurrency")));
            //translator.setMinimumRateCurrency(curencyDao.saveIfNotExist((String) jsonObject.get("minimumRateCurrency")));

            List<TranslatorToPaymentMethod> translatorToPaymentMethodList = new LinkedList<>();
            JSONArray paymentMethodList = (JSONArray) jsonObject.get("paymentMethodList");
            for (int i = 0; i < paymentMethodList.size(); ++i) {
                JSONObject rec = (JSONObject) paymentMethodList.get(i);
                //PaymentMethod paymentMethod = paymentMethodDao.saveIfNotExist((String) rec.get("description"));
                //TranslatorToPaymentMethod t = new TranslatorToPaymentMethod(paymentMethod, (String) rec.get("description"));
                //translatorToPaymentMethodList.add(t);
            }
            //translator.setServiceProvided(serviceProvidedDao.saveIfNotExist((String) jsonObject.get("serviceProvided")));
            translator.setDailyWordsOutput((String) jsonObject.get("dailyWordsOutput"));
            translator.setReferences((String) jsonObject.get("references"));
            translator.setLinkToProz((String) jsonObject.get("linkToProz"));
            //translator.setTranslationArea(translationAreaDao.saveIfNotExist((String) jsonObject.get("translationArea")));
            translator.setIsAgreementSign((Boolean) jsonObject.get("isAgreementSign"));
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
