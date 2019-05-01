package com.tms.controller;

import com.tms.util.message.Message;
import com.tms.model.entity.Client;
import com.tms.model.entity.ClientToContactPerson;
import com.tms.model.entity.Country;
import com.tms.model.entity.Person;
import com.tms.model.entity.PersonType;
import com.tms.model.entity.Project;
import com.tms.model.entity.Segmentation;
import com.tms.model.entity.dao.ClientDao;
import com.tms.model.entity.dao.CountryDao;
import com.tms.model.entity.dao.PersonDao;
import com.tms.model.entity.dao.PersonTypeDao;
import com.tms.model.entity.dao.ProjectDao;
import com.tms.model.entity.dao.SegmentationDao;
import com.tms.model.entity.service.ClientService;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 *
 * @author sdrahnea
 */
@Component
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS, value="session")
public class ClientController extends AbstractController<Client> implements Serializable, ISearcher {

    @Autowired
    private ClientService clientService;
    @Autowired
    private CountryDao countryDao;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private PersonTypeDao personTypeDao;
    @Autowired
    private SegmentationDao segmentationDao;
    @Autowired
    private ProjectDao projectDao;
    
    
    private List<Project> pastProjects = new LinkedList<>();
    private Client client = new Client();
    private List<Client> clientList = new LinkedList<>();
    private List<Country> countries = new LinkedList<>();
    private Country selectedCountry = new Country();
    private List<Person> saleManagers = new LinkedList<>();
    private Person selectedSaleManager = new Person();
    private Segmentation selectedSegmentation = new Segmentation();
    private List<Segmentation> segmentations = new LinkedList<>();
    private Person contactPerson = new Person();
    private PersonType ptContactPerson;
    private List<Person> contactPersons = new LinkedList<>();

    private String selectedClientName;
    private String selectedClientAddress;
    private String selectedContactPersonName;
    private String selectedContactEmail;
    private String selectedContactPhone;

    @Override
    public void clearFilters() {
        this.selectedClientName = "";
        this.selectedCountry = new Country();
        this.selectedClientAddress = "";
        this.selectedContactPersonName = "";
        this.selectedContactEmail = "";
        this.selectedContactPhone = "";
        callAllClient();
    }

    @Override
    public void filter() {
        clientList = clientService.search(selectedClientName, selectedClientAddress, selectedCountry, selectedContactPersonName, selectedContactEmail, selectedContactPhone);
    }

    private void callAllClient() {
        this.clientList = clientDao.findAll();
    }

    public void initClient(){
        this.client = new Client();
        initSelected(client);
    }
    
    @PostConstruct
    public void init() {
        try {
            callAllClient();
            this.countries = countryDao.findAll();
            this.segmentations = segmentationDao.findAll();
            this.saleManagers = personDao.findAllManagers();
            this.ptContactPerson = personTypeDao.CONTACT_PERSON();
        } catch (Exception ex) {

        }
    }

    private void refreshContactPersons() {
        this.contactPersons = personDao.findAll();
    }

    public void addClientContactPerson() {
        this.contactPersons.add(contactPerson);
        this.contactPerson = new Person();
        Message.throwInfoMessage("Operation status", "New contact person was saved successfully!");

    }

    public void onContactPersonEdit(CellEditEvent event) {
        Person oldValue = (Person) event.getOldValue();
        Person newValue = (Person) event.getOldValue();
        
        client = clientService.updateContactPerson(client, oldValue, newValue);
        contactPersons = clientService.getContactPerson(client);
        
        Message.throwInfoMessage("Operation status", "Contact person was updated successfully!");
    }

    public void onContactPersonCancel(RowEditEvent event) {
        Message.throwWarnMessage("Operation status", "Operation was canclled successfully!");
    }

    public void clearClientContactPerson(ActionEvent actionEvent) {
        this.contactPerson = new Person();
        this.contactPersons.clear();
        Message.throwWarnMessage("Operation status", "operation was cancelled!");
    }

    public String saveClient(ActionEvent actionEvent) {

        clientService.create(client, selectedSaleManager, selectedSegmentation, selectedCountry, contactPersons);

        clientList = clientService.getList();
        this.client = new Client();
        initSelected(client);
        contactPersons.clear();

        Message.throwInfoMessage("Operation status", "New client was saved successfully!");
        return "";
    }

    public void cancelClient(ActionEvent actionEvent) {
        client = new Client();
        initSelected(client);
        contactPersons.clear();
        contactPerson = new Person();
        Message.throwWarnMessage("Operation status", "operation was cancelled!");
    }

    public void onRowEditProject(RowEditEvent event) {
    }

    public void onRowCancelProject(RowEditEvent event) {
    }

    private void initSelected(Client client) {
        if (client != null) {
            if (client.getClientToContactPersons() != null) {
                List<Person> cps = new LinkedList<>();
                for (ClientToContactPerson d : client.getClientToContactPersons()) {
                    cps.add(d.getPerson());
                }
                setContactPersons(cps);
            }
            setSelectedCountry(client.getCountry());
            setSelectedSaleManager(client.getSaleManager());
            setSelectedSegmentation(client.getSegmentation());
            pastProjects = projectDao.getProjects(client);
        }
        this.saleManagers = personDao.findAllManagers();
    }

    public List<Client> getClientList() {
        return clientList;
    }

    public void setClientList(List<Client> clientList) {
        this.clientList = clientList;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        initSelected(client);
        this.client = client;
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

    public List<Person> getSaleManagers() {
        return saleManagers;
    }

    public void setSaleManagers(List<Person> saleManagers) {
        this.saleManagers = saleManagers;
    }

    public Person getSelectedSaleManager() {
        return selectedSaleManager;
    }

    public void setSelectedSaleManager(Person selectedSaleManager) {
        this.selectedSaleManager = selectedSaleManager;
    }

    public Segmentation getSelectedSegmentation() {
        return selectedSegmentation;
    }

    public void setSelectedSegmentation(Segmentation selectedSegmentation) {
        this.selectedSegmentation = selectedSegmentation;
    }

    public List<Segmentation> getSegmentations() {
        return segmentations;
    }

    public void setSegmentations(List<Segmentation> segmentations) {
        this.segmentations = segmentations;
    }

    public Person getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(Person contactPerson) {
        this.contactPerson = contactPerson;
    }

    public List<Person> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<Person> contactPersons) {
        this.contactPersons = contactPersons;
    }

    public PersonType getPtContactPerson() {
        return ptContactPerson;
    }

    public void setPtContactPerson(PersonType ptContactPerson) {
        this.ptContactPerson = ptContactPerson;
    }

    public String getSelectedClientName() {
        return selectedClientName;
    }

    public void setSelectedClientName(String selectedClientName) {
        this.selectedClientName = selectedClientName;
    }

    public String getSelectedClientAddress() {
        return selectedClientAddress;
    }

    public void setSelectedClientAddress(String selectedClientAddress) {
        this.selectedClientAddress = selectedClientAddress;
    }

    public String getSelectedContactPersonName() {
        return selectedContactPersonName;
    }

    public void setSelectedContactPersonName(String selectedContactPersonName) {
        this.selectedContactPersonName = selectedContactPersonName;
    }

    public String getSelectedContactEmail() {
        return selectedContactEmail;
    }

    public void setSelectedContactEmail(String selectedContactEmail) {
        this.selectedContactEmail = selectedContactEmail;
    }

    public String getSelectedContactPhone() {
        return selectedContactPhone;
    }

    public void setSelectedContactPhone(String selectedContactPhone) {
        this.selectedContactPhone = selectedContactPhone;
    }

    public List<Project> getPastProjects() {
        return pastProjects;
    }

    public void setPastProjects(List<Project> pastProjects) {
        this.pastProjects = pastProjects;
    }
    
}
