package com.tms.controller;

import com.tms.model.entity.Cat;
import com.tms.model.entity.Client;
import com.tms.model.entity.ClientToContactPerson;
import com.tms.model.entity.Currency;
import com.tms.model.entity.Language;
import com.tms.model.entity.Person;
import com.tms.model.entity.Project;
import com.tms.model.entity.ProjectDetail;
import com.tms.model.entity.ProjectType;
import com.tms.model.entity.Status;
import com.tms.model.entity.TimeZone;
import com.tms.model.entity.TranslationArea;
import com.tms.model.entity.Translator;
import com.tms.model.entity.User;
import com.tms.model.entity.dao.CatDao;
import com.tms.model.entity.dao.ClientDao;
import com.tms.model.entity.dao.CurrencyDao;
import com.tms.model.entity.dao.LanguageDao;
import com.tms.model.entity.dao.PersonDao;
import com.tms.model.entity.dao.ProjectTypeDao;
import com.tms.model.entity.dao.StatusDao;
import com.tms.model.entity.dao.TimeZoneDao;
import com.tms.model.entity.dao.TranslationAreaDao;
import com.tms.model.entity.service.ClientService;
import com.tms.model.entity.service.PersonService;
import com.tms.model.entity.service.ProjectService;
import com.tms.util.message.Message;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
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
public class ProjectController extends AbstractController<Project> implements Serializable, ISearcher {

    private static final long serialVersionUID = 1L;

    @Autowired
    private ProjectService projectService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private PersonService personService;
    @Autowired
    private LanguageDao languageDao;
    @Autowired
    private CurrencyDao currencyDao;
    @Autowired
    private CatDao catDao;
    @Autowired
    private ClientDao clientDao;
    @Autowired
    private TranslationAreaDao translationAreaDao;
    @Autowired
    private ProjectTypeDao projectTypeDao;
    @Autowired
    private PersonDao personDao;
    @Autowired
    private TimeZoneDao timeZoneDao;
    @Autowired
    private StatusDao statusDao;
    @Autowired
    HttpSession session;

    private List<Project> invoiceList = new LinkedList<>();
    private List<Project> archiveList = new LinkedList<>();
    private List<Project> projectList = new LinkedList<>();
    private Project project = new Project();
    private ProjectDetail projectDetail;
    private List<Client> clients = new LinkedList<>();
    private Client selectedClient = new Client();
    private List<TranslationArea> translationAreas = new LinkedList<>();
    private TranslationArea selectedTranslationArea = new TranslationArea();
    private List<Cat> cats = new LinkedList<>();
    private Cat selectedCat = new Cat();
    private List<ProjectType> projectTypes = new LinkedList<>();
    private ProjectType selectedProjectType = new ProjectType();
    private List<Person> contactPersons = new LinkedList<>();
    private Person selectedContactPerson = new Person();
    private Person selectedSaleManager = new Person();
    private TimeZone selectedTimeZone = new TimeZone();
    private List<Person> managers = new LinkedList<>();
    private Person selectedManager = new Person();
    private Person selectedSeconManager = new Person();
    private List<Currency> currencys = new LinkedList<>();
    private Currency selectedCurrency = new Currency();
    private List<Language> languages = new LinkedList<>();
    private List<TimeZone> timeZoneList = new LinkedList<>();
    private Language selectedSourceLanguage = new Language();
    private Language selectedDestinationLanguage = new Language();
    private Language[] selectedDestinationLanguages;
    private List<Status> statuses = new LinkedList<>();
    private Project selectedProject = new Project();
    private String subProjectName = "";

    private String filterProjectId;
    private Status filterProjectStatus;
    private String filterClientName;
    private String filterTranslatorName;
    private String filterTranslatorEmail;
    private Person filterSaleManager;
    private Person filterAssignedManager;
    private Person filterSecondAssignedManager;
    private Language filterSourceLanguage;
    private Language filterOutputLanguage;
    private Date filterReceiveDateStart;
    private Date filterReceiveDateEnd;
    private Date filterDeadlineStart;
    private Date filterDeadlineEnd;
    private String filterFileName;
    private String fileName;
    private String fileUUID;
    private String selectedDestinationLanguageLabel = "Select One";

    private List<Translator> informedTranslators = new LinkedList<>();

    private DefaultStreamedContent download;

    public void setDownload(DefaultStreamedContent download) {
        this.download = download;
    }

    public DefaultStreamedContent getDownload() throws Exception {
        return download;
    }

    public void prepDownload() throws Exception {
//        File file = new File(project.getFileName());
//        InputStream input = new FileInputStream(file);
//        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
//        setDownload(new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName()));
    }

    public void initProject() {
        this.project = new Project();
        initSelected(project);
    }

    @PostConstruct
    public void init() {
        try {
            callNotArchivedProjects();
            callArchivedProjects();
            callInvoiceProjects();
            this.languages = languageDao.findAll();
            this.currencys = currencyDao.findAll();
            this.managers = personDao.findAllManagers();
            this.projectTypes = projectTypeDao.findAll();
            this.cats = catDao.findAll();
            this.clients = clientDao.findAll();
            this.translationAreas = translationAreaDao.findAll();
            this.statuses = statusDao.findProjectStatus();
            this.timeZoneList = timeZoneDao.findAll();
            this.project = new Project();
        } catch (Exception ex) {
            System.out.println("something bad happened: " + ex);
        }

    }

    public void onClientChangeEvent() {
        contactPersons.clear();
        for (ClientToContactPerson ctcp : selectedClient.getClientToContactPersons()) {
            contactPersons.add(ctcp.getPerson());
        }
    }

    @Override
    public void filter() {
        projectList = projectService.search(filterAssignedManager, filterClientName, filterDeadlineStart, filterDeadlineEnd,
                filterOutputLanguage, filterProjectId != null && !filterProjectId.isEmpty() ? Integer.valueOf(filterProjectId) : null,
                filterProjectStatus, filterReceiveDateStart, filterReceiveDateEnd, filterSaleManager, filterSecondAssignedManager,
                filterSourceLanguage, filterTranslatorName, filterTranslatorEmail, filterFileName);

    }

    public void archiveFilter() {
        archiveList = projectService.search(filterAssignedManager, filterClientName, filterDeadlineStart, filterDeadlineEnd,
                filterOutputLanguage, filterProjectId != null && !filterProjectId.isEmpty() ? Integer.valueOf(filterProjectId) : null,
                filterProjectStatus, filterReceiveDateStart, filterReceiveDateEnd, filterSaleManager, filterSecondAssignedManager,
                filterSourceLanguage, filterTranslatorName, filterTranslatorEmail, filterFileName);
    }

    public void invoiceFilter() {
        invoiceList = projectService.search(filterAssignedManager, filterClientName, filterDeadlineStart, filterDeadlineEnd,
                filterOutputLanguage, filterProjectId != null && !filterProjectId.isEmpty() ? Integer.valueOf(filterProjectId) : null,
                filterProjectStatus, filterReceiveDateStart, filterReceiveDateEnd, filterSaleManager, filterSecondAssignedManager,
                filterSourceLanguage, filterTranslatorName, filterTranslatorEmail, filterFileName);
    }

    @Override
    public void clearFilters() {
        initClearFileters();
        callNotArchivedProjects();
    }

    public void clearArchiveFilters() {
        initClearFileters();
        callArchivedProjects();
    }

    public void clearInvoiceFilters() {
        initClearFileters();
        callInvoiceProjects();
    }

    private void initClearFileters() {
        filterAssignedManager = null;
        filterClientName = null;
        filterDeadlineEnd = null;
        filterDeadlineStart = null;
        filterFileName = null;
        filterOutputLanguage = null;
        filterProjectId = null;
        filterProjectStatus = null;
        filterReceiveDateEnd = null;
        filterReceiveDateStart = null;
        filterSaleManager = null;
        filterSecondAssignedManager = null;
        filterSourceLanguage = null;
        filterTranslatorEmail = null;
        filterTranslatorName = null;
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
        UploadedFile ufile = event.getFile();
        if (ufile != null) {
            this.fileUUID = UUID.randomUUID().toString();
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
            this.fileName = ufile.getFileName();
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

    public void saveProject(ActionEvent actionEvent) {
        System.out.println("saveProject: " + selectedDestinationLanguages + " -> " + selectedDestinationLanguages.length);
        boolean isEditOption = getCrudFormCaption().toLowerCase().contains("edit");
        project = projectService.createOrUpdate(project, isEditOption, selectedProjectType, selectedTranslationArea,
                selectedCat, selectedClient, selectedContactPerson, selectedSaleManager, selectedManager,
                selectedSeconManager, selectedCurrency, selectedSourceLanguage, selectedDestinationLanguage,
                selectedTimeZone,
                fileName, fileUUID, selectedDestinationLanguages);
        callNotArchivedProjects();

//        if (!isEditOption) {
//            project = new Project();
//            initSelected(project);
//        }
        initSelected(project);

        Message.throwInfoMessage(isEditOption ? "Project was edited successfully!" : "New project was saved successfully!");
        //return "";
    }

    public void moveToArchive() {
        projectService.moveToArchive(project);

        callNotArchivedProjects();
        callArchivedProjects();

        Message.throwInfoMessage("Project was moved to archive successfully!");
    }

    /**
     * restore project from archive status
     */
    public void restoreProject() {
        projectService.restoreProject(project);

        callNotArchivedProjects();
        callArchivedProjects();

        Message.throwInfoMessage("Project was restored successfully successfully!");
    }

    public void moveToClientPaidStatus() {
        projectService.moveToClientPaidStatus(project);

        callNotArchivedProjects();

        Message.throwInfoMessage("Project was moved successfully to new status!");
    }

    public void moveToTranslatorPaidStatus() {
        projectService.moveToTranslatorPaidStatus(project);

        callNotArchivedProjects();

        Message.throwInfoMessage("Project was moved successfully to new status!");
    }

    public void moveToProjectInvoicedStatus() {
        projectService.moveToProjectInvoicedStatus(project);

        callNotArchivedProjects();
        callInvoiceProjects();

        Message.throwInfoMessage("Project was moved successfully to new status!");
    }

    public void moveToProjectPaidStatus() {
        projectService.moveToProjectPaidStatus(project);

        callNotArchivedProjects();
        callInvoiceProjects();

        Message.throwInfoMessage("Project was moved successfully to new status!");
    }

    public void onDestinationLanguageSelected() {
        if (selectedDestinationLanguages != null && selectedDestinationLanguages.length > 0) {
            selectedDestinationLanguageLabel = "";
            for (Language language : selectedDestinationLanguages) {
                selectedDestinationLanguageLabel += (language.getName() + ",");
            }
            selectedDestinationLanguageLabel = selectedDestinationLanguageLabel.substring(0, selectedDestinationLanguageLabel.length() - 1);
        }
    }

    public String cancel(ActionEvent actionEvent) {
        try {
            project = new Project();
            initSelected(project);
            Message.throwWarnMessage("Operation was cancelled!");
        } catch (Exception ex) {
            Message.throwFatalMessage("Exception", "" + ex);
        }
        return null;
    }

    public void onRowEditProject(RowEditEvent event) {

    }

    public void onRowCancelProject(RowEditEvent event) {
    }

    public void cancelProject(ActionEvent actionEvent) {
        this.project = new Project();
        Message.throwWarnMessage("Operation status", "New project was calcelled successfully!");
    }

    private void callArchivedProjects() {
        this.archiveList = projectService.getArchivedProjects();
    }

    private void callInvoiceProjects() {
        this.invoiceList = projectService.getInvoicedProjects();
    }

    private void callNotArchivedProjects() {
        try {
            User user = (User) session.getAttribute("user");
            this.projectList = projectService.getProjects(user);
        } catch (Exception ex) {
            System.out.println("callNotArchivedProjects: " + ex);
        }
    }

    public ProjectDetail createDetail() {
        return projectService.createNewEmptyDetail(project);
    }

    public void markAsInformed() {
        projectService.moveToProjectInformedStatus(project);
    }

    public void markAsAssigned() {
        projectService.moveToProjectAssignedStatus(project);
    }

    public void markAsDelivered() {
        projectService.moveToProjectDelivedStatus(project);
    }

    private void initSelected(Project project) {
        if (project != null) {
            setSelectedCat(project.getCat());
            setSelectedClient(project.getClient());
            //onClientChangeEvent();
            setSelectedContactPerson(project.getContactPerson());
            setSelectedCurrency(project.getCurrency());
            setSelectedDestinationLanguage(project.getDestinatonLanguage());
            setSelectedManager(project.getAssignedManager());
            setSelectedProjectType(project.getProjectType());
            setSelectedSaleManager(project.getSaleManager());
            setSelectedSeconManager(project.getSecondManager());
            setSelectedSourceLanguage(project.getSourceLanguage());
            setSelectedTranslationArea(project.getTranslationArea());
            setSelectedTimeZone(project.getTimeZone());
        }
    }

    public void check(SelectEvent event) {
        System.out.println("in check");
    }

    public List<Project> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(List<Project> invoiceList) {
        this.invoiceList = invoiceList;
    }

    public Project getSelectedProject() {

        return selectedProject;
    }

    public void setSelectedProject(Project selectedProject) {
        this.selectedProject = selectedProject;
    }

    public List<Translator> getInformedTranslators() {
        return informedTranslators;
    }

    public void setInformedTranslators(List<Translator> informedTranslators) {
        this.informedTranslators = informedTranslators;
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<Project> projectList) {
        this.projectList = projectList;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        initSelected(project);
        this.project = project;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public List<Project> getArchiveList() {
        return archiveList;
    }

    public void setArchiveList(List<Project> archiveList) {
        this.archiveList = archiveList;
    }

    public Client getSelectedClient() {
        return selectedClient;
    }

    public void setSelectedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
    }

    public List<TranslationArea> getTranslationAreas() {
        return translationAreas;
    }

    public void setTranslationAreas(List<TranslationArea> translationAreas) {
        this.translationAreas = translationAreas;
    }

    public TranslationArea getSelectedTranslationArea() {
        return selectedTranslationArea;
    }

    public void setSelectedTranslationArea(TranslationArea selectedTranslationArea) {
        this.selectedTranslationArea = selectedTranslationArea;
    }

    public List<Cat> getCats() {
        return cats;
    }

    public void setCats(List<Cat> cats) {
        this.cats = cats;
    }

    public Cat getSelectedCat() {
        return selectedCat;
    }

    public void setSelectedCat(Cat selectedCat) {
        this.selectedCat = selectedCat;
    }

    public List<ProjectType> getProjectTypes() {
        return projectTypes;
    }

    public void setProjectTypes(List<ProjectType> projectTypes) {
        this.projectTypes = projectTypes;
    }

    public ProjectType getSelectedProjectType() {
        return selectedProjectType;
    }

    public void setSelectedProjectType(ProjectType selectedProjectType) {
        this.selectedProjectType = selectedProjectType;
    }

    public List<Person> getContactPersons() {
        return contactPersons;
    }

    public void setContactPersons(List<Person> contactPersons) {
        this.contactPersons = contactPersons;
    }

    public Person getSelectedContactPerson() {
        return selectedContactPerson;
    }

    public void setSelectedContactPerson(Person selectedContactPerson) {
        this.selectedContactPerson = selectedContactPerson;
    }

    public Person getSelectedSaleManager() {
        return selectedSaleManager;
    }

    public void setSelectedSaleManager(Person selectedSaleManager) {
        this.selectedSaleManager = selectedSaleManager;
    }

    public List<Person> getManagers() {
        return managers;
    }

    public void setManagers(List<Person> managers) {
        this.managers = managers;
    }

    public Person getSelectedManager() {
        return selectedManager;
    }

    public void setSelectedManager(Person selectedManager) {
        this.selectedManager = selectedManager;
    }

    public Person getSelectedSeconManager() {
        return selectedSeconManager;
    }

    public void setSelectedSeconManager(Person selectedSeconManager) {
        this.selectedSeconManager = selectedSeconManager;
    }

    public List<Currency> getCurrencys() {
        return currencys;
    }

    public void setCurrencys(List<Currency> currencys) {
        this.currencys = currencys;
    }

    public Currency getSelectedCurrency() {
        return selectedCurrency;
    }

    public void setSelectedCurrency(Currency selectedCurrency) {
        this.selectedCurrency = selectedCurrency;
    }

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public Language getSelectedSourceLanguage() {
        return selectedSourceLanguage;
    }

    public void setSelectedSourceLanguage(Language selectedSourceLanguage) {
        this.selectedSourceLanguage = selectedSourceLanguage;
    }

    public Language getSelectedDestinationLanguage() {
        return selectedDestinationLanguage;
    }

    public void setSelectedDestinationLanguage(Language selectedDestinationLanguage) {
        this.selectedDestinationLanguage = selectedDestinationLanguage;
    }

    public String getSubProjectName() {
        return subProjectName;
    }

    public void setSubProjectName(String subProjectName) {
        this.subProjectName = subProjectName;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

    public String getFilterProjectId() {
        return filterProjectId;
    }

    public void setFilterProjectId(String filterProjectId) {
        this.filterProjectId = filterProjectId;
    }

    public Status getFilterProjectStatus() {
        return filterProjectStatus;
    }

    public void setFilterProjectStatus(Status filterProjectStatus) {
        this.filterProjectStatus = filterProjectStatus;
    }

    public String getFilterClientName() {
        return filterClientName;
    }

    public void setFilterClientName(String filterClientName) {
        this.filterClientName = filterClientName;
    }

    public String getFilterTranslatorName() {
        return filterTranslatorName;
    }

    public void setFilterTranslatorName(String filterTranslatorName) {
        this.filterTranslatorName = filterTranslatorName;
    }

    public String getFilterTranslatorEmail() {
        return filterTranslatorEmail;
    }

    public void setFilterTranslatorEmail(String filterTranslatorEmail) {
        this.filterTranslatorEmail = filterTranslatorEmail;
    }

    public Person getFilterSaleManager() {
        return filterSaleManager;
    }

    public void setFilterSaleManager(Person filterSaleManager) {
        this.filterSaleManager = filterSaleManager;
    }

    public Person getFilterAssignedManager() {
        return filterAssignedManager;
    }

    public void setFilterAssignedManager(Person filterAssignedManager) {
        this.filterAssignedManager = filterAssignedManager;
    }

    public Person getFilterSecondAssignedManager() {
        return filterSecondAssignedManager;
    }

    public void setFilterSecondAssignedManager(Person filterSecondAssignedManager) {
        this.filterSecondAssignedManager = filterSecondAssignedManager;
    }

    public Language getFilterSourceLanguage() {
        return filterSourceLanguage;
    }

    public void setFilterSourceLanguage(Language filterSourceLanguage) {
        this.filterSourceLanguage = filterSourceLanguage;
    }

    public Language getFilterOutputLanguage() {
        return filterOutputLanguage;
    }

    public void setFilterOutputLanguage(Language filterOutputLanguage) {
        this.filterOutputLanguage = filterOutputLanguage;
    }

    public Date getFilterReceiveDateStart() {
        return filterReceiveDateStart;
    }

    public void setFilterReceiveDateStart(Date filterReceiveDateStart) {
        this.filterReceiveDateStart = filterReceiveDateStart;
    }

    public Date getFilterReceiveDateEnd() {
        return filterReceiveDateEnd;
    }

    public void setFilterReceiveDateEnd(Date filterReceiveDateEnd) {
        this.filterReceiveDateEnd = filterReceiveDateEnd;
    }

    public Date getFilterDeadlineStart() {
        return filterDeadlineStart;
    }

    public void setFilterDeadlineStart(Date filterDeadlineStart) {
        this.filterDeadlineStart = filterDeadlineStart;
    }

    public Date getFilterDeadlineEnd() {
        return filterDeadlineEnd;
    }

    public void setFilterDeadlineEnd(Date filterDeadlineEnd) {
        this.filterDeadlineEnd = filterDeadlineEnd;
    }

    public String getFilterFileName() {
        return filterFileName;
    }

    public void setFilterFileName(String filterFileName) {
        this.filterFileName = filterFileName;
    }

    public ProjectDetail getProjectDetail() {
        return projectDetail;
    }

    public void setProjectDetail(ProjectDetail projectDetail) {
        this.projectDetail = projectDetail;
    }

    public ClientService getClientService() {
        return clientService;
    }

    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
    }

    public TimeZone getSelectedTimeZone() {
        return selectedTimeZone;
    }

    public void setSelectedTimeZone(TimeZone selectedTimeZone) {
        this.selectedTimeZone = selectedTimeZone;
    }

    public List<TimeZone> getTimeZoneList() {
        return timeZoneList;
    }

    public void setTimeZoneList(List<TimeZone> timeZoneList) {
        this.timeZoneList = timeZoneList;
    }

    public ProjectService getProjectService() {
        return projectService;
    }

    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }

    public PersonService getPersonService() {
        return personService;
    }

    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    public Language[] getSelectedDestinationLanguages() {
        return selectedDestinationLanguages;
    }

    public void setSelectedDestinationLanguages(Language[] selectedDestinationLanguages) {
        this.selectedDestinationLanguages = selectedDestinationLanguages;
    }

    public String getSelectedDestinationLanguageLabel() {
        return selectedDestinationLanguageLabel;
    }

    public void setSelectedDestinationLanguageLabel(String selectedDestinationLanguageLabel) {
        this.selectedDestinationLanguageLabel = selectedDestinationLanguageLabel;
    }

}
