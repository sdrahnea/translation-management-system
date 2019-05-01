package com.tms.controller;

import com.tms.model.entity.Cat;
import com.tms.model.entity.Country;
import com.tms.model.entity.Currency;
import com.tms.model.entity.EducationDegree;
import com.tms.model.entity.Language;
import com.tms.model.entity.PaymentMethod;
import com.tms.model.entity.Project;
import com.tms.model.entity.ServiceProvided;
import com.tms.model.entity.TranslationArea;
import com.tms.model.entity.Translator;
import com.tms.model.entity.TranslatorFeedback;
import com.tms.model.entity.TranslatorToCat;
import com.tms.model.entity.TranslatorToPaymentMethod;
import com.tms.model.entity.TranslatorToServcieProvided;
import com.tms.model.entity.TranslatorToTranslationArea;
import com.tms.model.entity.dao.CatDao;
import com.tms.model.entity.dao.CountryDao;
import com.tms.model.entity.dao.CurrencyDao;
import com.tms.model.entity.dao.EducationDegreeDao;
import com.tms.model.entity.dao.LanguageDao;
import com.tms.model.entity.dao.PaymentMethodDao;
import com.tms.model.entity.dao.ProjectDao;
import com.tms.model.entity.dao.ServiceProvidedDao;
import com.tms.model.entity.dao.TranslationAreaDao;
import com.tms.model.entity.dao.TranslatorDao;
import com.tms.model.entity.service.ProjectService;
import com.tms.model.entity.service.TranslatorService;
import com.tms.util.message.Message;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 *
 * @author sdrahnea
 */
@Component
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS, value = "session")
public class TranslatorController extends AbstractController<Translator> implements Serializable, ISearcher {

    @Autowired
    private CountryDao countryDao;
    @Autowired
    private EducationDegreeDao educationDegreeDao;
    @Autowired
    private LanguageDao langaugeDao;
    @Autowired
    private CatDao catDao;
    @Autowired
    private CurrencyDao currencyDao;
    @Autowired
    private ServiceProvidedDao serviceProvidedDao;
    @Autowired
    private TranslationAreaDao translationAreaDao;
    @Autowired
    private PaymentMethodDao paymentMethodDao;
    @Autowired
    private TranslatorDao translatorDao;
    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private TranslatorService translatorService;
    @Autowired
    private ProjectService projectService;

    private Translator translator = new Translator();
    private List<Translator> translatorList = new LinkedList<>();
    private List<Translator> applicantList = new LinkedList<>();
    private List<Country> countries = new LinkedList<>();
    private Country selectedCountry = new Country();
    private List<EducationDegree> educationDegrees = new LinkedList<>();
    private EducationDegree selectedEducationDegree = new EducationDegree();
    private List<Language> languages = new LinkedList<>();
    private Language selectedInputLanguage1 = new Language();
    private Language selectedInputLanguage2 = new Language();
    private Language selectedInputLanguage3 = new Language();
    private Language selectedOutputLanguage = new Language();
    private List<Cat> cats = new LinkedList<>();
    private List<TranslationArea> translationAreas = new LinkedList<>();
    private List<ServiceProvided> serviceProvideds = new LinkedList<>();
    private Cat[] selectedCats;
    private ServiceProvided[] selectedServiceProvideds;
    private TranslationArea[] selectedTranslationAreas;
    private List<Currency> currencies = new LinkedList<>();
    private Currency selectedTranslatorRateCurrency;
    private Currency selectedProofReadingRateCurrency;
    private Currency selectedMinimumRateCurrency;
    private ServiceProvided selectedServiceProvided = new ServiceProvided();
    private List<TranslationArea> traslationAreas = new LinkedList<>();
    private TranslationArea selectedTranslationArea = new TranslationArea();
    private List<PaymentMethod> paymentMethodList = new LinkedList<>();
    private List<TranslatorToPaymentMethod> translatorToPaymentMethodList = new LinkedList<>();
    private String paymentDescription;
    private PaymentMethod selectedPaymentMethod = new PaymentMethod();
    private TranslatorFeedback translatorFeedback = new TranslatorFeedback();
    private List<Project> pastProjects = new LinkedList<>();

    private String selectedTranslatorName;
    private String selectedTranslatorEmail;

    private DefaultStreamedContent download;

    public void setDownload(DefaultStreamedContent download) {
        this.download = download;
    }

    public DefaultStreamedContent getDownload() throws Exception {
        return download;
    }

    public void initTranslator() {
        this.translator = new Translator();
        initSelected(translator);
    }

    @PostConstruct
    public void init() {
        try {
            this.translator = new Translator();
            this.selectedCountry = new Country();
            this.selectedEducationDegree = new EducationDegree();
            this.selectedInputLanguage1 = new Language();
            this.selectedInputLanguage2 = new Language();
            this.selectedInputLanguage3 = new Language();
            this.selectedOutputLanguage = new Language();

            this.selectedTranslatorRateCurrency = new Currency();
            this.selectedProofReadingRateCurrency = new Currency();
            this.selectedMinimumRateCurrency = new Currency();

            this.translatorToPaymentMethodList.clear();

            selectedTranslationAreas = null;
            selectedServiceProvideds = null;

            this.selectedCats = null;
            this.selectedServiceProvided = new ServiceProvided();
            this.selectedPaymentMethod = new PaymentMethod();
            this.selectedTranslationArea = new TranslationArea();
            this.translatorFeedback = new TranslatorFeedback();

            this.countries = countryDao.findAll();
            this.educationDegrees = educationDegreeDao.findAll();
            this.languages = langaugeDao.findAll();
            this.cats = catDao.findAll();
            this.currencies = currencyDao.findAll();
            this.serviceProvideds = serviceProvidedDao.findAll();
            this.traslationAreas = translationAreaDao.findAll();
            this.paymentMethodList = paymentMethodDao.findAll();
            callAllTranslators();
            callAllApplicants();
        } catch (Exception ex) {

        }
    }

    @Override
    public void filter() {
        translatorList = translatorService.search(null, null, selectedInputLanguage1, selectedOutputLanguage,
                selectedTranslationArea, selectedCountry,
                selectedTranslatorName, selectedTranslatorEmail, selectedServiceProvided, true);
    }

    @Override
    public void clearFilters() {
        this.selectedInputLanguage1 = null;
        this.selectedOutputLanguage = null;
        this.selectedTranslationArea = null;
        this.selectedCountry = null;
        this.selectedTranslatorName = null;
        this.selectedTranslatorEmail = null;
        this.selectedServiceProvided = null;
        this.selectedCats = null;
        callAllTranslators();
    }

    public void filterApplicant() {
        applicantList = translatorService.search(null, null, selectedInputLanguage1, selectedOutputLanguage,
                selectedTranslationArea, selectedCountry,
                selectedTranslatorName, selectedTranslatorEmail, selectedServiceProvided, false);
    }

    public void clearApplicantFilters() {
        this.selectedInputLanguage1 = null;
        this.selectedOutputLanguage = null;
        this.selectedTranslationArea = null;
        this.selectedCountry = null;
        this.selectedTranslatorName = null;
        this.selectedTranslatorEmail = null;
        this.selectedServiceProvided = null;
        this.selectedCats = null;
        callAllApplicants();
    }

    private void callAllTranslators() {
        this.translatorList = translatorService.getAll(true);
    }

    private void callAllApplicants() {
        this.applicantList = translatorService.getAll(false);
    }

    public void cancel(ActionEvent actionEvent) {
        translator = new Translator();
        initSelected(translator);
    }

    public void saveTranslator(ActionEvent actionEvent) {
        try {
            translatorService.create(translator, selectedCountry, selectedEducationDegree,
                    selectedInputLanguage1, selectedInputLanguage2, selectedInputLanguage3, selectedOutputLanguage,
                    selectedMinimumRateCurrency, selectedProofReadingRateCurrency, selectedTranslatorRateCurrency,
                    selectedTranslationArea, selectedServiceProvided, selectedCats, translatorToPaymentMethodList,
                    selectedServiceProvideds, selectedTranslationAreas, translatorFeedback);

            callAllTranslators();
            this.translator = new Translator();
            init();
            Message.throwInfoMessage("Operation status", "New translator was saved successfully!");
        } catch (Exception ex) {
            System.out.println("saveTranslator: " + ex);
            Message.throwFatalMessage("Exception", "" + ex);
        }
    }

    public void changeToTranslator() {
        translatorService.changeToTranslator(translator);
        callAllApplicants();
        callAllTranslators();
    }

    public String wrappTranslatorAgreementSign(Translator translator) {
        if (translator == null || translator.getIsAgreementSign() == null) {
            return null;
        }
        return translator.getIsAgreementSign() ? "signed" : "not signed";
    }

    public void prepDownload() throws Exception {
        File file = new File(translator.getFileUUID());
        InputStream input = new FileInputStream(file);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        setDownload(new DefaultStreamedContent(input, externalContext.getMimeType(translator.getFileName()), translator.getFileName()));
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        UploadedFile ufile = event.getFile();
        if (ufile != null) {
            String fileUUID = UUID.randomUUID().toString();
            File newFile = new File(fileUUID);
            if (newFile.exists()) {
                newFile.delete();
            }
            try {
                newFile.createNewFile();
                copyFileUsingStream(ufile.getInputstream(), newFile);
            } catch (Exception e) {
                throw new IllegalStateException("Error");
            }
            this.translator.setFileName(ufile.getFileName());
            this.translator.setFileUUID(fileUUID);
            translatorDao.merge(translator);
        }
    }

    private static void copyFileUsingStream(InputStream inputStream, File dest) throws IOException {
        OutputStream os = null;
        try {
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            inputStream.close();
            os.close();
        }
    }

    private void initSelected(Translator translator) {
        if (translator != null) {
            pastProjects = projectService.getProjects(translator);
            
            if (translator.getTranslatorToCats() != null) {
                Cat[] catss = new Cat[translator.getTranslatorToCats().size()];
                int index = 0;
                for (TranslatorToCat ttc : translator.getTranslatorToCats()) {
                    catss[index] = ttc.getCat();
                    index++;
                }
                setSelectedCats(catss);
            }

            if (translator.getTranslatorToServcieProvideds() != null) {
                ServiceProvided[] sps = new ServiceProvided[translator.getTranslatorToServcieProvideds().size()];
                int index = 0;
                for (TranslatorToServcieProvided tsp : translator.getTranslatorToServcieProvideds()) {
                    sps[index] = tsp.getServiceProvided();
                    index++;
                }
                setSelectedServiceProvideds(sps);
            }

            if (translator.getTranslatorToTranslationAreas() != null) {
                TranslationArea[] tas = new TranslationArea[translator.getTranslatorToTranslationAreas().size()];
                int index = 0;
                for (TranslatorToTranslationArea ta : translator.getTranslatorToTranslationAreas()) {
                    tas[index] = ta.getTranslationArea();
                    index++;
                }
                setSelectedTranslationAreas(tas);
            }

            setSelectedCountry(translator.getCountry());
            setSelectedEducationDegree(translator.getEducationDegree());
            setSelectedInputLanguage1(translator.getInputLanguage1());
            setSelectedInputLanguage2(translator.getInputLanguage2());
            setSelectedInputLanguage3(translator.getInputLanguage3());
            setSelectedMinimumRateCurrency(translator.getMinimumRateCurrency());
            setSelectedOutputLanguage(translator.getOutputLanguage());
            setSelectedProofReadingRateCurrency(translator.getProofReadingRateCurrency());

            if (translator.getTranslatorToPaymentMethods() != null) {
                List<TranslatorToPaymentMethod> tList = new LinkedList<>();
                for (TranslatorToPaymentMethod t : translator.getTranslatorToPaymentMethods()) {
                    tList.add(t);
                }
                setTranslatorToPaymentMethodList(tList);
            }
//            setSelectedServiceProvided(translator.getServiceProvided());
//            setSelectedTranslationArea(translator.getTranslationArea());
            setSelectedTranslatorRateCurrency(translator.getTranslatorRateCurrency());
            setTranslatorFeedback(translator.getLastTranslatorFeedback());

        }
    }

    public void addPaymentMethod() {
        this.translatorToPaymentMethodList.add(new TranslatorToPaymentMethod(selectedPaymentMethod, paymentDescription));
        this.selectedPaymentMethod = new PaymentMethod();
        this.paymentDescription = "";
        Message.throwInfoMessage("Operation status", "New paymet method was saved successfully!");

    }

    public void onPaymentMethodEdit(RowEditEvent event) {
        TranslatorToPaymentMethod oldValue = (TranslatorToPaymentMethod) event.getObject();
        for (int index = 0; index < translatorToPaymentMethodList.size(); index++) {
            if (translatorToPaymentMethodList.get(index).equals(oldValue)) {
                translatorToPaymentMethodList.set(index, (TranslatorToPaymentMethod) event.getObject());
            }
        }
        Message.throwInfoMessage("Operation status", "Contact person was updated successfully!");
    }

    public String showTranslationArea(final Translator translator, final List<TranslationArea> tas) {
        return (translator.getTranslatorToTranslationAreas().size() == tas.size()) ? "All" : "" + translator.getTranslatorToTranslationAreas();
    }

    public String showServiceProvided(final Translator translator, final List<ServiceProvided> sps) {
        return (translator.getTranslatorToServcieProvideds().size() == sps.size()) ? "All" : "" + translator.getTranslatorToServcieProvideds();
    }

    public void onPaymentMethodCancel(RowEditEvent event) {
        TranslatorToPaymentMethod object = (TranslatorToPaymentMethod) event.getObject();
        this.translatorToPaymentMethodList.remove(object);
        Message.throwWarnMessage("Operation status", "Record was removed successfully!");
    }

    public void clearPaymentMethod(ActionEvent actionEvent) {
        this.selectedPaymentMethod = new PaymentMethod();
        this.paymentDescription = "";
        this.translatorToPaymentMethodList.clear();
        Message.throwWarnMessage("Operation status", "Payment method data were cleared!");
    }

    public List<Translator> getTranslatorList() {
        return translatorList;
    }

    public void setTranslatorList(List<Translator> translatorList) {
        this.translatorList = translatorList;
    }

    public List<TranslationArea> getTraslationAreas() {
        return traslationAreas;
    }

    public void setTraslationAreas(List<TranslationArea> traslationAreas) {
        this.traslationAreas = traslationAreas;
    }

    public TranslationArea getSelectedTranslationArea() {
        return selectedTranslationArea;
    }

    public void setSelectedTranslationArea(TranslationArea selectedTranslationArea) {
        this.selectedTranslationArea = selectedTranslationArea;
    }

    public List<ServiceProvided> getServiceProvideds() {
        return serviceProvideds;
    }

    public void setServiceProvideds(List<ServiceProvided> serviceProvideds) {
        this.serviceProvideds = serviceProvideds;
    }

    public ServiceProvided getSelectedServiceProvided() {
        return selectedServiceProvided;
    }

    public void setSelectedServiceProvided(ServiceProvided selectedServiceProvided) {
        this.selectedServiceProvided = selectedServiceProvided;
    }

    public Translator getTranslator() {
        return translator;
    }

    public void setTranslator(Translator translator) {
        initSelected(translator);
        this.translator = translator;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void setCountries(List<Country> countries) {
        this.countries = countries;
    }

    public Country getSelectedCountry() {
        return selectedCountry;
    }

    public void setSelectedCountry(Country selectedCountry) {
        this.selectedCountry = selectedCountry;
    }

    public List<EducationDegree> getEducationDegrees() {
        return educationDegrees;
    }

    public void setEducationDegrees(List<EducationDegree> educationDegrees) {
        this.educationDegrees = educationDegrees;
    }

    public EducationDegree getSelectedEducationDegree() {
        return selectedEducationDegree;
    }

    public void setSelectedEducationDegree(EducationDegree selectedEducationDegree) {
        this.selectedEducationDegree = selectedEducationDegree;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public Language getSelectedInputLanguage1() {
        return selectedInputLanguage1;
    }

    public void setSelectedInputLanguage1(Language selectedInputLanguage1) {
        this.selectedInputLanguage1 = selectedInputLanguage1;
    }

    public Language getSelectedInputLanguage2() {
        return selectedInputLanguage2;
    }

    public void setSelectedInputLanguage2(Language selectedInputLanguage2) {
        this.selectedInputLanguage2 = selectedInputLanguage2;
    }

    public Language getSelectedInputLanguage3() {
        return selectedInputLanguage3;
    }

    public void setSelectedInputLanguage3(Language selectedInputLanguage3) {
        this.selectedInputLanguage3 = selectedInputLanguage3;
    }

    public Language getSelectedOutputLanguage() {
        return selectedOutputLanguage;
    }

    public void setSelectedOutputLanguage(Language selectedOutputLanguage) {
        this.selectedOutputLanguage = selectedOutputLanguage;
    }

    public List<Cat> getCats() {
        return cats;
    }

    public void setCats(List<Cat> cats) {
        this.cats = cats;
    }

    public Cat[] getSelectedCats() {
        return selectedCats;
    }

    public void setSelectedCats(Cat[] selectedCats) {
        this.selectedCats = selectedCats;
    }

    public List<Currency> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(List<Currency> currencies) {
        this.currencies = currencies;
    }

    public Currency getSelectedTranslatorRateCurrency() {
        return selectedTranslatorRateCurrency;
    }

    public void setSelectedTranslatorRateCurrency(Currency selectedTranslatorRateCurrency) {
        this.selectedTranslatorRateCurrency = selectedTranslatorRateCurrency;
    }

    public Currency getSelectedProofReadingRateCurrency() {
        return selectedProofReadingRateCurrency;
    }

    public void setSelectedProofReadingRateCurrency(Currency selectedProofReadingRateCurrency) {
        this.selectedProofReadingRateCurrency = selectedProofReadingRateCurrency;
    }

    public Currency getSelectedMinimumRateCurrency() {
        return selectedMinimumRateCurrency;
    }

    public void setSelectedMinimumRateCurrency(Currency selectedMinimumRateCurrency) {
        this.selectedMinimumRateCurrency = selectedMinimumRateCurrency;
    }

    public String getSelectedTranslatorName() {
        return selectedTranslatorName;
    }

    public void setSelectedTranslatorName(String selectedTranslatorName) {
        this.selectedTranslatorName = selectedTranslatorName;
    }

    public String getSelectedTranslatorEmail() {
        return selectedTranslatorEmail;
    }

    public void setSelectedTranslatorEmail(String selectedTranslatorEmail) {
        this.selectedTranslatorEmail = selectedTranslatorEmail;
    }

    public List<PaymentMethod> getPaymentMethodList() {
        return paymentMethodList;
    }

    public void setPaymentMethodList(List<PaymentMethod> paymentMethodList) {
        this.paymentMethodList = paymentMethodList;
    }

    public List<TranslatorToPaymentMethod> getTranslatorToPaymentMethodList() {
        return translatorToPaymentMethodList;
    }

    public void setTranslatorToPaymentMethodList(List<TranslatorToPaymentMethod> translatorToPaymentMethodList) {
        this.translatorToPaymentMethodList = translatorToPaymentMethodList;
    }

    public String getPaymentDescription() {
        return paymentDescription;
    }

    public void setPaymentDescription(String paymentDescription) {
        this.paymentDescription = paymentDescription;
    }

    public PaymentMethod getSelectedPaymentMethod() {
        return selectedPaymentMethod;
    }

    public void setSelectedPaymentMethod(PaymentMethod selectedPaymentMethod) {
        this.selectedPaymentMethod = selectedPaymentMethod;
    }

    public List<Translator> getApplicantList() {
        return applicantList;
    }

    public void setApplicantList(List<Translator> applicantList) {
        this.applicantList = applicantList;
    }

    public ServiceProvided[] getSelectedServiceProvideds() {
        return selectedServiceProvideds;
    }

    public void setSelectedServiceProvideds(ServiceProvided[] selectedServiceProvideds) {
        this.selectedServiceProvideds = selectedServiceProvideds;
    }

    public TranslationArea[] getSelectedTranslationAreas() {
        return selectedTranslationAreas;
    }

    public void setSelectedTranslationAreas(TranslationArea[] selectedTranslationAreas) {
        this.selectedTranslationAreas = selectedTranslationAreas;
    }

    public List<TranslationArea> getTranslationAreas() {
        return translationAreas;
    }

    public void setTranslationAreas(List<TranslationArea> translationAreas) {
        this.translationAreas = translationAreas;
    }

    public TranslatorService getTranslatorService() {
        return translatorService;
    }

    public void setTranslatorService(TranslatorService translatorService) {
        this.translatorService = translatorService;
    }

    public TranslatorFeedback getTranslatorFeedback() {
        return translatorFeedback;
    }

    public void setTranslatorFeedback(TranslatorFeedback translatorFeedback) {
        this.translatorFeedback = translatorFeedback;
    }

    public List<Project> getPastProjects() {
        return pastProjects;
    }

    public void setPastProjects(List<Project> pastProjects) {
        this.pastProjects = pastProjects;
    }

}
