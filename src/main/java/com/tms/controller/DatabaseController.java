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
import com.tms.repository.ClientRepository;
import com.tms.repository.ClientToContactPersonRepository;
import com.tms.repository.CountryRepository;
import com.tms.repository.LanguageRepository;
import com.tms.repository.PersonRepository;
import com.tms.repository.PersonTypeRepository;
import com.tms.repository.RatingRepository;
import com.tms.repository.ServiceProvidedRepository;
import com.tms.repository.TranslationAreaRepository;
import com.tms.repository.TranslatorFeedbackRepository;
import com.tms.repository.TranslatorRepository;
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
import org.apache.poi.ss.usermodel.CellType;
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
public class DatabaseController implements Serializable {

    private static final long serialVersionUID = 1L;

    private UploadedFile file;
    private String fileName;
    private String selectedImportedItem;

    @Autowired
    private PersonRepository personRepository;
    @Autowired
    private PersonTypeRepository personTypeRepository;
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientToContactPersonRepository clientToContactPersonRepository;
    @Autowired
    private ServiceProvidedRepository serviceProvidedRepository;
    @Autowired
    private TranslationAreaRepository translationAreaRepository;
    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private TranslatorRepository translatorRepository;
    @Autowired
    private TranslatorFeedbackRepository translatorFeedbackRepository;

    @PostConstruct
    public void init() {
    }

    public void handleFileUpload(FileUploadEvent event) {
        if (event != null && event.getFile() != null) {
            this.file = event.getFile();
            this.fileName = this.file.getFileName();
        }
    }

    private Workbook openWorkbook() throws IOException {
        if (file == null || isBlank(fileName)) {
            throw new IOException("No file uploaded.");
        }

        InputStream inputStream = file.getInputstream();
        if (fileName.toLowerCase().endsWith(".xlsx")) {
            return new XSSFWorkbook(inputStream);
        }
        if (fileName.toLowerCase().endsWith(".xls")) {
            return new HSSFWorkbook(inputStream);
        }
        throw new IOException("Unsupported file type: " + fileName);
    }

    private void importClients() {
        try (Workbook workbook = openWorkbook()) {
            int count = 0;
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next(); // Skip header row.
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (isRowBlank(row, 12)) {
                    continue;
                }

                String name = getCellString(row, 0);
                String country = getCellString(row, 1);
                String contactPerson1 = getCellString(row, 2);
                String contactEmail1 = getCellString(row, 3);
                String contactPerson2 = getCellString(row, 4);
                String contactEmail2 = getCellString(row, 5);
                String contactPerson3 = getCellString(row, 6);
                String contactEmail3 = getCellString(row, 7);
                String invoiceEmail = getCellString(row, 8);
                String contactPhone = getCellString(row, 9);
                String skype = getCellString(row, 10);
                String website = getCellString(row, 11);

                PersonType ptContactPerson = personTypeRepository.CONTACT_PERSON();

                Person person1 = null;
                if (!isBlank(contactPerson1)) {
                    person1 = new Person(contactPerson1, contactEmail1);
                    person1.setPersonType(ptContactPerson);
                    person1 = personRepository.save(person1);
                }

                Person person2 = null;
                if (!isBlank(contactPerson2)) {
                    person2 = new Person(contactPerson2, contactEmail2);
                    person2.setPersonType(ptContactPerson);
                    person2 = personRepository.save(person2);
                }

                Person person3 = null;
                if (!isBlank(contactPerson3)) {
                    person3 = new Person(contactPerson3, contactEmail3);
                    person3.setPersonType(ptContactPerson);
                    person3 = personRepository.save(person3);
                }

                Client client = new Client();
                if (!isBlank(country)) {
                    List<Country> countryList = countryRepository.findByName(country);
                    if (countryList.isEmpty()) {
                        Country countryObject = countryRepository.save(new Country(country));
                        client.setCountry(countryObject);
                    } else {
                        client.setCountry(countryList.get(0));
                    }
                }

                client.setName(name);
                client.setWebsite(website);
                client.setInvoiceEmail(invoiceEmail);
                client.setDescription("contact phone: " + contactPhone + ", skype: " + skype);

                client = clientRepository.save(client);

                if (person1 != null) {
                    clientToContactPersonRepository.save(new ClientToContactPerson(client, person1));
                }
                if (person2 != null) {
                    clientToContactPersonRepository.save(new ClientToContactPerson(client, person2));
                }
                if (person3 != null) {
                    clientToContactPersonRepository.save(new ClientToContactPerson(client, person3));
                }
                count++;
            }

            Message.throwInfoMessage("All information ( " + count + " records) from excel file were imported successfully!");
        } catch (Exception ex) {
            Message.throwFatalMessage("Execption: " + ex);
        }
    }

    private void importTranslators() {
        try (Workbook workbook = openWorkbook()) {
            int count = 0;
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next(); // Skip header row.
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (isRowBlank(row, 16)) {
                    continue;
                }

                String name = getCellString(row, 0);
                String country = getCellString(row, 1);
                String email = getCellString(row, 2);
                String contactPhone = getCellString(row, 3);
                String inputLanguage1 = getCellString(row, 4);
                String inputLanguage2 = getCellString(row, 5);
                String inputLanguage3 = getCellString(row, 6);
                String outputLanguage = getCellString(row, 7);
                String translationRate = getCellString(row, 8);
                String proofReadingRate = getCellString(row, 9);
                String serviceProvided = getCellString(row, 10);
                String translationArea = getCellString(row, 11);
                String rating = getCellString(row, 12);
                String minimumRate = getCellString(row, 13);
                String lastComment = getCellString(row, 14);
                String linkToProz = getCellString(row, 15);

                Translator translator = new Translator();
                translator.setName(name);
                translator.setEmail(email);
                translator.setContactPhone(contactPhone);

                if (!isBlank(country)) {
                    List<Country> countryList = countryRepository.findByName(country);
                    if (countryList.isEmpty()) {
                        Country countryObject = countryRepository.save(new Country(country));
                        translator.setCountry(countryObject);
                    } else {
                        translator.setCountry(countryList.get(0));
                    }
                }

                if (!isBlank(inputLanguage1)) {
                    translator.setInputLanguage1(resolveLanguage(inputLanguage1));
                }
                if (!isBlank(inputLanguage2)) {
                    translator.setInputLanguage2(resolveLanguage(inputLanguage2));
                }
                if (!isBlank(inputLanguage3)) {
                    translator.setInputLanguage3(resolveLanguage(inputLanguage3));
                }
                if (!isBlank(outputLanguage)) {
                    translator.setOutputLanguage(resolveLanguage(outputLanguage));
                }

                translator.setTranslatorRate(parseDecimalOrZero(translationRate));
                translator.setProofReadingRate(parseDecimalOrZero(proofReadingRate));
                translator.setMinimumRate(parseDecimalOrZero(minimumRate));

                if (!isBlank(serviceProvided)) {
                    List<ServiceProvided> serviceProvidedList = serviceProvidedRepository.findByName(serviceProvided);
                    ServiceProvided serviceProvidedObject = new ServiceProvided(serviceProvided);
                    if (serviceProvidedList.isEmpty()) {
                        serviceProvidedObject = serviceProvidedRepository.save(serviceProvidedObject);
                        translator.setServiceProvided(serviceProvidedObject);
                    } else {
                        translator.setServiceProvided(serviceProvidedList.get(0));
                    }
                }

                if (!isBlank(translationArea)) {
                    List<TranslationArea> translationAreaList = translationAreaRepository.findByName(translationArea);
                    TranslationArea translationAreaObject = new TranslationArea(translationArea);
                    if (translationAreaList.isEmpty()) {
                        translationAreaObject = translationAreaRepository.save(translationAreaObject);
                        translator.setTranslationArea(translationAreaObject);
                    } else {
                        translator.setTranslationArea(translationAreaList.get(0));
                    }
                }

                translator.setLinkToProz(linkToProz);
                translator = translatorRepository.save(translator);

                if (!isBlank(rating)) {
                    TranslatorFeedback feedback = new TranslatorFeedback();

                    List<Rating> ratingList = ratingRepository.findByName(rating);
                    Rating ratingObject = new Rating(rating);
                    if (ratingList.isEmpty()) {
                        ratingObject = ratingRepository.save(ratingObject);
                        feedback.setRating(ratingObject);
                    } else {
                        feedback.setRating(ratingList.get(0));
                    }
                    feedback.setComment(lastComment);
                    feedback.setTranslator(translator);

                    translatorFeedbackRepository.save(feedback);
                }
                count++;
            }
            Message.throwInfoMessage("All information ( " + count + " records) from excel file were imported successfully!");
        } catch (Exception ex) {
            Message.throwFatalMessage("Execption: " + ex);
        }
    }

    private Language resolveLanguage(String name) {
        List<Language> languageList = languageRepository.findByName(name);
        Language languageObject = new Language(name);
        if (languageList.isEmpty()) {
            languageObject = languageRepository.save(languageObject);
            return languageObject;
        }
        return languageList.get(0);
    }

    private String getCellString(Row row, int index) {
        Cell cell = row.getCell(index, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
        return getCellValue(cell);
    }

    private String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        CellType cellType = cell.getCellType();
        if (cellType == CellType.FORMULA) {
            cellType = cell.getCachedFormulaResultType();
        }

        switch (cellType) {
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue()).stripTrailingZeros().toPlainString();
            case STRING:
                return trimToNull(cell.getStringCellValue());
            default:
                return null;
        }
    }

    private BigDecimal parseDecimalOrZero(String value) {
        if (isBlank(value)) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(value.trim());
        } catch (NumberFormatException ex) {
            return BigDecimal.ZERO;
        }
    }

    private boolean isRowBlank(Row row, int maxColumns) {
        for (int i = 0; i < maxColumns; i++) {
            if (!isBlank(getCellString(row, i))) {
                return false;
            }
        }
        return true;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public void importData() {
        try {
            if (isBlank(selectedImportedItem)) {
                Message.throwWarnMessage("Please select import type: Client or Translator.");
                return;
            }
            if (file == null) {
                Message.throwWarnMessage("Please upload an Excel file first.");
                return;
            }

            if (selectedImportedItem.equalsIgnoreCase("Client")) {
                importClients();
            } else if (selectedImportedItem.equalsIgnoreCase("Translator")) {
                importTranslators();
            } else {
                Message.throwWarnMessage("Unsupported import type: " + selectedImportedItem);
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
