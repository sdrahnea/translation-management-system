package com.tms.controller;

import com.tms.model.entity.Client;
import com.tms.model.entity.ClientToContactPerson;
import com.tms.model.entity.Country;
import com.tms.model.entity.Language;
import com.tms.model.entity.Person;
import com.tms.model.entity.PersonType;
import com.tms.model.entity.Rating;
import com.tms.model.entity.ServiceProvided;
import com.tms.model.entity.TranslationArea;
import com.tms.model.entity.Translator;
import com.tms.model.entity.TranslatorFeedback;
import com.tms.model.entity.dao.ClientDao;
import com.tms.model.entity.dao.ClientToContactPersonDao;
import com.tms.model.entity.dao.CountryDao;
import com.tms.model.entity.dao.LanguageDao;
import com.tms.model.entity.dao.PersonDao;
import com.tms.model.entity.dao.PersonTypeDao;
import com.tms.model.entity.dao.RatingDao;
import com.tms.model.entity.dao.ServiceProvidedDao;
import com.tms.model.entity.dao.TranslationAreaDao;
import com.tms.model.entity.dao.TranslatorDao;
import com.tms.model.entity.dao.TranslatorFeedbackDao;
import com.tms.util.message.Message;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ViewScoped;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author sdrahnea
 */
@Component
@ViewScoped
public class DatabaseController implements Serializable{

    private UploadedFile file;
    private InputStream fileInputStream = null;
    private String fileName = null;
    private String selectedImportedItem;
    
    @Autowired
    private PersonDao personDao;
    @Autowired
    private PersonTypeDao personTypeDao;
    @Autowired
    private LanguageDao languageDao;
    @Autowired
    private CountryDao countryDao;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private ClientToContactPersonDao clientToContactPersonDao;
    @Autowired
    private ServiceProvidedDao serviceProvidedDao;
    @Autowired
    private TranslationAreaDao translationAreaDao;
    @Autowired
    private RatingDao ratingDao;
    @Autowired
    private TranslatorDao translatorDao;
    @Autowired
    private TranslatorFeedbackDao translatorFeedbackDao;
    
    @PostConstruct
    public void init() {
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        if (event.getFile() != null) {
            fileInputStream = event.getFile().getInputstream();
            fileName = event.getFile().getFileName();
        }
    }

    private Sheet getFileSheet() throws IOException {
        Workbook workbook = null;
        if (fileName.toLowerCase().endsWith("xlsx")) {
            workbook = new XSSFWorkbook(fileInputStream);
        } else if (fileName.toLowerCase().endsWith("xls")) {
            workbook = new HSSFWorkbook(fileInputStream);
        }

        //Get the nth sheet from the workbook
        Sheet sheet = workbook.getSheetAt(0);
        return sheet;
    }

    private void importClients() {
        try {
            int count = 0;
            //every sheet has rows, iterate over them
            Iterator<Row> rowIterator = getFileSheet().iterator();
            while (rowIterator.hasNext()) {
                //Get the row object
                Row row = rowIterator.next();

                //Every row has columns, get the column iterator and iterate over them
                Iterator<Cell> cellIterator = row.cellIterator();
                int index = 0;
                String name = null;
                String country = null;
                String contactPerson1 = null;
                String contactEmail1 = null;
                String contactPerson2 = null;
                String contactEmail2 = null;
                String contactPerson3 = null;
                String contactEmail3 = null;
                String invoiceEmail = null;
                String contactPhone = null;
                String skype = null;
                String website = null;

                while (cellIterator.hasNext()) {
                    //Get the Cell object
                    Cell cell = cellIterator.next();
                    String cellValue = "" + getCellValue(cell);
                    switch (index) {
                        case 0:
                            name = cellValue;
                            break;
                        case 1:
                            country = cellValue;
                            break;
                        case 2:
                            contactPerson1 = cellValue;
                            break;
                        case 3:
                            contactEmail1 = cellValue;
                            break;
                        case 4:
                            contactPerson2 = cellValue;
                            break;
                        case 5:
                            contactEmail2 = cellValue;
                            break;
                        case 6:
                            contactPerson3 = cellValue;
                            break;
                        case 7:
                            contactEmail3 = cellValue;
                            break;
                        case 8:
                            invoiceEmail = cellValue;
                            break;
                        case 9:
                            contactPhone = cellValue;
                            break;
                        case 10:
                            skype = cellValue;
                            break;
                        case 11:
                            website = cellValue;
                            break;
                        default:
                            break;
                    }
                    index++;
                } //end of cell iterator

                /**
                 * save all contact persons
                 */
                PersonType ptContactPerson = personTypeDao.CONTACT_PERSON();
                Person person1 = null;
                if (contactPerson1 != null) {
                    person1 = new Person(contactPerson1, contactEmail1);
                    personDao.merge(person1);
                }
                Person person2 = null;
                if (contactPerson2 != null) {
                    person2 = new Person(contactPerson2, contactEmail2);
                    personDao.merge(person2);
                }
                Person person3 = null;
                if (contactPerson3 != null) {
                    person3 = new Person(contactPerson3, contactEmail3);
                    personDao.merge(person3);
                }
                /**
                 * save client
                 */
                Client client = new Client();
                if (country != null) {
                    List<Country> countryList = countryDao.findByName(country);
                    Country countryObject = new Country(country);
                    if (countryList.isEmpty()) {
                        countryDao.merge(countryObject);
                        client.setCountry(countryObject);
                    } else {
                        client.setCountry(countryList.get(0));
                    }
                }
                client.setName(name);
                client.setWebsite(website);
                client.setInvoiceEmail(invoiceEmail);
                client.setDescription("contact phone: " + contactPhone + ", skype: " + skype);

                clientDao.merge(client);
                /**
                 * create join between contact person and client
                 */
                if (person1 != null) {
                    person1.setPersonType(ptContactPerson);
                    clientToContactPersonDao.merge(new ClientToContactPerson(client, person1));
                }
                if (person2 != null) {
                    person2.setPersonType(ptContactPerson);
                    clientToContactPersonDao.merge(new ClientToContactPerson(client, person2));
                }
                if (person3 != null) {
                    person3.setPersonType(ptContactPerson);
                    clientToContactPersonDao.merge(new ClientToContactPerson(client, person3));
                }
                count++;
            } //end of rows iterator

            Message.throwInfoMessage("All information ( " + count + " records) from excel file were imported successfully!");
        } catch (Exception ex) {
            Message.throwFatalMessage("Execption: " + ex);
        }
    }

    private void importTranslators() {
        try {
            int count = 0;
            //every sheet has rows, iterate over them
            Iterator<Row> rowIterator = getFileSheet().iterator();
            while (rowIterator.hasNext()) {
                //Get the row object
                Row row = rowIterator.next();

                //Every row has columns, get the column iterator and iterate over them
                Iterator<Cell> cellIterator = row.cellIterator();
                int index = 0;
                String name = null;
                String country = null;
                String email = null;
                String contactPhone = null;
                String inputLanguage1 = null;
                String inputLanguage2 = null;
                String inputLanguage3 = null;
                String outputLanguage = null;
                String translationRate = null;
                String proofReadingRate = null;
                String serviceProvoded = null;
                String translationArea = null;
                String rating = null;
                String minimumRate = null;
                String lastComment = null;
                String linkToProz = null;

                while (cellIterator.hasNext()) {
                    //Get the Cell object
                    Cell cell = cellIterator.next();
                    String cellValue = "" + getCellValue(cell);
                    switch (index) {
                        case 0:
                            name = cellValue;
                            break;
                        case 1:
                            country = cellValue;
                            break;
                        case 2:
                            email = cellValue;
                            break;
                        case 3:
                            contactPhone = cellValue;
                            break;
                        case 4:
                            inputLanguage1 = cellValue;
                            break;
                        case 5:
                            inputLanguage2 = cellValue;
                            break;
                        case 6:
                            inputLanguage3 = cellValue;
                            break;
                        case 7:
                            outputLanguage = cellValue;
                            break;
                        case 8:
                            translationRate = cellValue;
                            break;
                        case 9:
                            proofReadingRate = cellValue;
                            break;
                        case 10:
                            serviceProvoded = cellValue;
                            break;
                        case 11:
                            translationArea = cellValue;
                            break;
                        case 12:
                            rating = cellValue;
                            break;
                        case 13:
                            minimumRate = cellValue;
                            break;
                        case 14:
                            lastComment = cellValue;
                            break;
                        case 15:
                            linkToProz = cellValue;
                            break;
                        default:
                            break;
                    }
                    index++;
                } //end of cell iterator

                Translator translator = new Translator();
                translator.setName(name);
                translator.setEmail(email);
                translator.setContactPhone(contactPhone);
                if (country != null) {
                    List<Country> countryList = countryDao.findByName(country);
                    Country countryObject = new Country(country);
                    if (countryList.isEmpty()) {
                        countryDao.merge(countryObject);
                        translator.setCountry(countryObject);
                    } else {
                        translator.setCountry(countryList.get(0));
                    }
                }
                if (inputLanguage1 != null) {
                    List<Language> languageList = languageDao.findByName(inputLanguage1);
                    Language languageObject = new Language(inputLanguage1);
                    if (languageList.isEmpty()) {
                        languageDao.merge(languageObject);
                        translator.setInputLanguage1(languageObject);
                    } else {
                        translator.setInputLanguage1(languageList.get(0));
                    }
                }
                if (inputLanguage2 != null) {
                    List<Language> languageList = languageDao.findByName(inputLanguage2);
                    Language languageObject = new Language(inputLanguage2);
                    if (languageList.isEmpty()) {
                        languageDao.merge(languageObject);
                        translator.setInputLanguage2(languageObject);
                    } else {
                        translator.setInputLanguage2(languageList.get(0));
                    }
                }
                if (inputLanguage3 != null) {
                    List<Language> languageList = languageDao.findByName(inputLanguage3);
                    Language languageObject = new Language(inputLanguage3);
                    if (languageList.isEmpty()) {
                        languageDao.merge(languageObject);
                        translator.setInputLanguage3(languageObject);
                    } else {
                        translator.setInputLanguage3(languageList.get(0));
                    }
                }
                if (outputLanguage != null) {
                    List<Language> languageList = languageDao.findByName(outputLanguage);
                    Language languageObject = new Language(outputLanguage);
                    if (languageList.isEmpty()) {
                        languageDao.merge(languageObject);
                        translator.setOutputLanguage(languageObject);
                    } else {
                        translator.setOutputLanguage(languageList.get(0));
                    }
                }
                /**
                 * translator rate
                 */
                try {
                    translator.setTranslatorRate(translationRate != null || translationRate.isEmpty()
                            ? new BigDecimal(translationRate)
                            : new BigDecimal(0));
                } catch (Exception ex) {

                }

                /**
                 * translator proofrading
                 */
                try {
                    translator.setProofReadingRate(proofReadingRate != null || proofReadingRate.isEmpty()
                            ? new BigDecimal(proofReadingRate)
                            : new BigDecimal(0));
                } catch (Exception exProofReading) {

                }

                if (serviceProvoded != null) {
                    List<ServiceProvided> serviceProvidedList = serviceProvidedDao.findByName(serviceProvoded);
                    ServiceProvided serviceProvidedObject = new ServiceProvided(serviceProvoded);
                    if (serviceProvidedList.isEmpty()) {
                        serviceProvidedDao.merge(serviceProvidedObject);
                        translator.setServiceProvided(serviceProvidedObject);
                    } else {
                        translator.setServiceProvided(serviceProvidedList.get(0));
                    }
                }

                if (translationArea != null) {
                    List<TranslationArea> translationAreaList = translationAreaDao.findByName(translationArea);
                    TranslationArea translationAreaObject = new TranslationArea(translationArea);
                    if (translationAreaList.isEmpty()) {
                        translationAreaDao.merge(translationAreaObject);
                        translator.setTranslationArea(translationAreaObject);
                    } else {
                        translator.setTranslationArea(translationAreaList.get(0));
                    }
                }

                try {
                    translator.setMinimumRate(minimumRate != null || minimumRate.isEmpty()
                            ? new BigDecimal(minimumRate)
                            : new BigDecimal(0));
                } catch (Exception exMinimumRate) {

                }

                translator.setLinkToProz(linkToProz);

                translatorDao.merge(translator);

                if (rating != null) {
                    TranslatorFeedback feedack = new TranslatorFeedback();
                    
                    List<Rating> ratingList = ratingDao.findByName(rating);
                    Rating ratingObject = new Rating(rating);
                    if (ratingList.isEmpty()) {
                        ratingDao.merge(ratingObject);
                        feedack.setRating(ratingObject);
                    } else {
                        feedack.setRating(ratingList.get(0));
                    }
                    feedack.setComment(lastComment);
                    feedack.setTranslator(translator);
                    
                    translatorFeedbackDao.merge(feedack);
                }
                count++;
            } //end of rows iterator
            Message.throwInfoMessage("All information ( " + count + " records) from excel file were imported successfully!");
        } catch (Exception ex) {
            Message.throwFatalMessage("Execption: " + ex);
        }
    }

    private Object getCellValue(Cell cell) {
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                return cell.getBooleanCellValue();
            case Cell.CELL_TYPE_NUMERIC:
                return cell.getNumericCellValue();
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
        }
        return null;
    }

    public void importData() {
        try {
            if (selectedImportedItem.equalsIgnoreCase("Client")) {
                importClients();
            } else if (selectedImportedItem.equalsIgnoreCase("Translator")) {
                importTranslators();
            }
        } catch (Exception ex) {
            Message.throwFatalMessage("Execption: " + ex);
        }
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public String getSelectedImportedItem() {
        return selectedImportedItem;
    }

    public void setSelectedImportedItem(String selectedImportedItem) {
        this.selectedImportedItem = selectedImportedItem;
    }

}
