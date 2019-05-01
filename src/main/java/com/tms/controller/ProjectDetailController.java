package com.tms.controller;

import com.tms.model.entity.Language;
import com.tms.model.entity.NotSentEmail;
import com.tms.model.entity.ProjectDetail;
import com.tms.model.entity.ProjectDetailTranslatorStatus;
import com.tms.model.entity.ProjectType;
import com.tms.model.entity.Translator;
import com.tms.model.entity.dao.LanguageDao;
import com.tms.model.entity.dao.NotSentEmailDao;
import com.tms.model.entity.dao.ProjectDetailDao;
import com.tms.model.entity.dao.ProjectTypeDao;
import com.tms.model.entity.dao.TranslatorDao;
import com.tms.model.entity.service.ProjectDetailService;
import com.tms.util.EmailSender;
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
import javax.transaction.Transactional;
import org.primefaces.event.FileUploadEvent;
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
public class ProjectDetailController extends AbstractController<ProjectDetail> implements Serializable {

    private ProjectDetail projectDetail;
    private List<ProjectType> projectTypes = new LinkedList<>();
    private ProjectType selectedProjectType = new ProjectType();
    private List<Language> languages = new LinkedList<>();
    private Language selectedSourceLanguage = new Language();
    private Language selectedDestinationLanguage = new Language();
    private DefaultStreamedContent download;
    private List<Translator> selectInformedTranslators = new LinkedList<>();
    private List<Translator> selectAssignedTranslators = new LinkedList<>();
    private List<Translator> assignedTranslators = new LinkedList<>();
    private List<ProjectDetailTranslatorStatus> selectReadyToWorkTranslators = new LinkedList<>();
    private List<Translator> allTranslators = new LinkedList<>();

    @Autowired
    private ProjectDetailDao projectDetailDao;
    @Autowired
    private LanguageDao languageDao;
    @Autowired
    private ProjectTypeDao projectTypeDao;
    @Autowired
    private ProjectDetailService projectDetailService;
    @Autowired
    private TranslatorDao translatorDao;
    @Autowired
    private NotSentEmailDao notSentEmailDao;

    private List<ProjectDetailTranslatorStatus> pdtsList = new LinkedList<>();

    @PostConstruct
    public void init() {
        this.languages = languageDao.findAll();
        this.projectTypes = projectTypeDao.findAll();
        this.allTranslators = translatorDao.findAll();
        //this.pdtsList = projectDetailService.getAll(projectDetail);
        this.pdtsList = projectDetailService.getAllInformedTranslators(projectDetail);
        this.assignedTranslators = projectDetailService.getAllAssignedTranslators(projectDetail);

        if (pdtsList != null){
            allTranslators = removeTranslators(allTranslators, pdtsList);
        }
        
        if (assignedTranslators != null) {
            allTranslators.removeAll(assignedTranslators);
        }
    }

    private List<Translator> removeTranslators(List<Translator> output, List<ProjectDetailTranslatorStatus> removed) {
        if (removed == null){
            return output;
        }
        for (ProjectDetailTranslatorStatus temp : removed) {
            output.remove(temp.getTranslator());
        }
        return output;
    }

    public void setDownload(DefaultStreamedContent download) {
        this.download = download;
    }

    public DefaultStreamedContent getDownload() throws Exception {
        return download;
    }

    public void prepDownload() throws Exception {
        File file = new File(projectDetail.getFileUUID());
        InputStream input = new FileInputStream(file);
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        setDownload(new DefaultStreamedContent(input, externalContext.getMimeType(projectDetail.getFileName()), projectDetail.getFileName()));
    }

    public void saveDetail() {
        projectDetail.setDestinatonLanguage(selectedDestinationLanguage);
        projectDetail.setSourceLanguage(selectedSourceLanguage);
        projectDetail.setProjectType(selectedProjectType);
        projectDetail = projectDetailDao.merge(projectDetail);
        
        selectedDestinationLanguage = null;
        selectedSourceLanguage = null;
        selectedProjectType = null;
    }

    public String cancel(ActionEvent actionEvent) {
        return null;
    }

    private NotSentEmail sentEmailToTranslator(Translator translator) {
        try {
            String toEmail = translator.getEmail();
            if (toEmail != null && !toEmail.isEmpty()) {
                String emailSenderResult = EmailSender.sentEmail(toEmail, toEmail, toEmail);
                if (!emailSenderResult.equalsIgnoreCase("OK")) {
                    return new NotSentEmail(toEmail, "Inform trsnalator "
                            + translator.getName(), emailSenderResult.substring(0, emailSenderResult.length() > 250 ? 250 : emailSenderResult.length()));
                }
                return null;
            }
            return new NotSentEmail("", "Inform trsnalator " + translator.getName(), "Email field is not setuped or is empty for this translator");
        } catch (Exception ex) {
            return new NotSentEmail("", "Inform trsnalator " + translator.getName(), "During method invockation was throw an error" + ex);
        }
    }

    public void informTranslators() {
        if (!selectInformedTranslators.isEmpty()) {
            for (Translator translator : selectInformedTranslators) {
                projectDetailService.informTranslator(projectDetail, translator);
                NotSentEmail notSentEmail = sentEmailToTranslator(translator);
                if (notSentEmail != null) {
                    notSentEmailDao.merge(notSentEmail);
                }
            }
            boolean many = selectInformedTranslators.size() > 1;
            selectInformedTranslators.clear();

            this.pdtsList = projectDetailService.getAllInformedTranslators(projectDetail);

            init();

            Message.throwInfoMessage("Operation status", "Selected translator" + (many ? "(s) were " : " was ") + "informed successfully");
        } else {
            Message.throwWarnMessage("Operation status", "Please, select at lest one translator!");
        }
    }

    public void readyToWorkAction() {
        if (!selectReadyToWorkTranslators.isEmpty()) {
            for (ProjectDetailTranslatorStatus pdts : selectReadyToWorkTranslators) {
                projectDetailService.readyToWorkTranslator(projectDetail, pdts);
            }
            boolean many = selectReadyToWorkTranslators.size() > 1;
            selectReadyToWorkTranslators.clear();

            this.pdtsList = projectDetailService.getAll(projectDetail);

            Message.throwInfoMessage("Operation status", "Selected translator" + (many ? "(s) were " : " was ") + "ready to work successfully");
        } else {
            Message.throwWarnMessage("Operation status", "Please, select at lest one translator!");
        }
    }

    @Transactional
    public void assignTranslatorAction() {
        if (!selectReadyToWorkTranslators.isEmpty()) {
            for (ProjectDetailTranslatorStatus pdts : selectReadyToWorkTranslators) {
                projectDetailService.assignTranslator(projectDetail, pdts);
            }
            boolean many = selectReadyToWorkTranslators.size() > 1;
            selectReadyToWorkTranslators.clear();

            init();

            Message.throwInfoMessage("Operation status", "Selected translator" + (many ? "(s) were " : " was ") + "assigned successfully");
        } else {
            Message.throwWarnMessage("Operation status", "Please, select at lest one translator!");
        }
    }

    public void markAsDelivered() {
        projectDetailService.markAsDelivered(projectDetail);
    }

    public void markAsClientPaid() {
        projectDetailService.markAsClientPaid(projectDetail);
    }

    public void markAsTranslatorPaid() {
        projectDetailService.markAsTranslatorPaid(projectDetail);
    }

    public void markAsPaid() {
        projectDetailService.markAsPaid(projectDetail);
    }

    public void markAsInvoiced() {
        projectDetailService.markAsInvoiced(projectDetail);
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
            this.projectDetail.setFileName(ufile.getFileName());
            this.projectDetail.setFileUUID(fileUUID);
            projectDetailDao.merge(projectDetail);

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

    private void initSelected(ProjectDetail detail) {
        if (detail != null) {
            setSelectedDestinationLanguage(detail.getDestinatonLanguage());
            setSelectedSourceLanguage(detail.getSourceLanguage());
            setSelectedProjectType(detail.getProjectType());
            this.pdtsList = projectDetailService.getAll(detail);
        }
    }

    public ProjectDetail getProjectDetail() {
        return projectDetail;
    }

    public void setProjectDetail(ProjectDetail projectDetail) {
        initSelected(projectDetail);
        this.projectDetail = projectDetail;
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

    public List<ProjectDetailTranslatorStatus> getPdtsList() {
        return pdtsList;
    }

    public void setPdtsList(List<ProjectDetailTranslatorStatus> pdtsList) {
        this.pdtsList = pdtsList;
    }

    public List<Translator> getAllTranslators() {
        return allTranslators;
    }

    public void setAllTranslators(List<Translator> allTranslators) {
        this.allTranslators = allTranslators;
    }

    public List<Translator> getSelectInformedTranslators() {
        return selectInformedTranslators;
    }

    public void setSelectInformedTranslators(List<Translator> selectInformedTranslators) {
        this.selectInformedTranslators = selectInformedTranslators;
    }

    public List<ProjectDetailTranslatorStatus> getSelectReadyToWorkTranslators() {
        return selectReadyToWorkTranslators;
    }

    public void setSelectReadyToWorkTranslators(List<ProjectDetailTranslatorStatus> selectReadyToWorkTranslators) {
        this.selectReadyToWorkTranslators = selectReadyToWorkTranslators;
    }

    public List<Translator> getAssignedTranslators() {
        return assignedTranslators;
    }

    public void setAssignedTranslators(List<Translator> assignedTranslators) {
        this.assignedTranslators = assignedTranslators;
    }

    public List<Translator> getSelectAssignedTranslators() {
        return selectAssignedTranslators;
    }

    public void setSelectAssignedTranslators(List<Translator> selectAssignedTranslators) {
        this.selectAssignedTranslators = selectAssignedTranslators;
    }

}
