/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.util.dataEntity;

import com.tms.model.entity.Cat;
import com.tms.model.entity.Client;
import com.tms.model.entity.Country;
import com.tms.model.entity.Currency;
import com.tms.model.entity.EducationDegree;
import com.tms.model.entity.Language;
import com.tms.model.entity.PaymentMethod;
import com.tms.model.entity.PersonType;
import com.tms.model.entity.Project;
import com.tms.model.entity.ProjectType;
import com.tms.model.entity.Role;
import com.tms.model.entity.Segmentation;
import com.tms.model.entity.ServiceProvided;
import com.tms.model.entity.Status;
import com.tms.model.entity.StatusType;
import com.tms.model.entity.TimeZone;
import com.tms.model.entity.TranslationArea;
import com.tms.model.entity.Translator;
import com.tms.model.entity.User;
import com.tms.model.entity.service.ClientService;
import com.tms.model.entity.service.ProjectService;
import com.tms.model.entity.service.TranslatorService;
import com.tms.util.crypt.CryptMD5;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author sdrahnea
 */
@Component
public class DefaultDataLoader {

    @PersistenceContext(unitName = "persistenceUnit")
    private EntityManager em;

    public void setEm(EntityManager em) {
        this.em = em;
    }

    @Transactional
    public void updateAllEntitties() {
        updateCountries();
        updateSegmentation();
        updatePersonType();
        updateEducationDegree();
        updateCat();
        updateCurrency();
        updateServiceProvided();
        updateTranslationArea();
        updateLanguages();
        updateProjectType();
        updateStatusType();
        updateRole();
        updatePaymentMethod();
        updateTimeZone();

        createDefaultUsers();
    }

    public void createDummyProjects() {

        System.out.println("CREATE DUMMY PROJECTS");

        ProjectService projectService = new ProjectService();
        TranslatorService translatorService = new TranslatorService();
        ClientService clientService = new ClientService();

        for (int i = 0; i < 1000; i++) {
            Translator translator = new Translator();
            translator.setName("" + UUID.randomUUID().toString());
            translator.setContactPhone("" + UUID.randomUUID().toString());
            translator.setCvFile("" + UUID.randomUUID().toString());
            translator.setFileUUID("" + UUID.randomUUID().toString());
            translator.setReferences("" + UUID.randomUUID().toString());
            //translatorService.create(translator);

        }

        for (int i = 0; i < 1000; i++) {
            Client client = new Client();
            client.setName("" + UUID.randomUUID().toString());
            client.setInvoiceEmail("" + UUID.randomUUID().toString());
            client.setAddress("" + UUID.randomUUID().toString());
            client.setVat("" + UUID.randomUUID().toString());
            client.setWebsite("" + UUID.randomUUID().toString());
            //clientService.create(client);

        }

        for (int i = 0; i < 100000; i++) {
            Project project = new Project();
            project.setName("dummy_" + i);
            project.setInsertDate(new Date());
            project.setNotes("" + UUID.randomUUID().toString());

            //projectService.createOrUpdate(project, false, null, null, null, null, null, null, null, null, null, null, null, null, null);
        }

        System.out.println("END   --------   CREATE DUMMY PROJECTS");

    }

    private static int getRandomIndex(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    @Transactional
    private void createDefaultUsers() {
        List<User> result = em.createQuery("FROM User user").getResultList();
        if (result.isEmpty()) {

            Role clientRole = (Role) em.createQuery("FROM Role r WHERE r.name = 'CLIENT'").getResultList().get(0);
            Role translatorRole = (Role) em.createQuery("FROM Role r WHERE r.name = 'TRANSLATOR'").getResultList().get(0);
            Role adminRole = (Role) em.createQuery("FROM Role r WHERE r.name = 'ADMIN'").getResultList().get(0);
            Role managerRole = (Role) em.createQuery("FROM Role r WHERE r.name = 'MANAGER'").getResultList().get(0);

            Translator translator = new Translator();
            translator.setName("translator");
            translator.setIsEnabled(true);

            Client client = new Client("client");

            User clientUser = new User();
            clientUser.setName("Client only");
            //clientUser.setPassword(CryptWithMD5.cryptWithMD5("123"));
            clientUser.setLogin("client");
            clientUser.setRole(clientRole);
            clientUser = CryptMD5.createHashPassword(clientUser, "123");

            User translatorUser = new User();
            translatorUser.setName("Translator only");
            //translatorUser.setPassword(CryptWithMD5.cryptWithMD5("123"));
            translatorUser.setLogin("translator");
            translatorUser.setRole(translatorRole);
            translatorUser = CryptMD5.createHashPassword(translatorUser, "123");

            User adminUser = new User();
            adminUser.setName("Admin only");
            //adminUser.setPassword(CryptWithMD5.cryptWithMD5("123"));
            adminUser.setLogin("admin");
            adminUser.setRole(adminRole);
            adminUser = CryptMD5.createHashPassword(adminUser, "123");

            User managerUser = new User();
            managerUser.setName("Manager only");
            //managerUser.setPassword(CryptWithMD5.cryptWithMD5("123"));
            managerUser.setLogin("manager");
            managerUser.setRole(managerRole);
            managerUser = CryptMD5.createHashPassword(managerUser, "123");

            em.persist(translatorUser);
            em.persist(clientUser);
            em.persist(adminUser);
            em.persist(managerUser);

            client.setUser(clientUser);
            translator.setUser(translatorUser);
            em.persist(client);
            em.persist(translator);
        }
    }

    @Transactional
    public void updatePaymentMethod() {
        List<PaymentMethod> entity = PaymentMethodDataEntity.list();
        List<PaymentMethod> database = em.createQuery("FROM PaymentMethod pm").getResultList();
        if (database.isEmpty()) {
            for (PaymentMethod pm : entity) {
                em.persist(pm);
            }
        }
    }

    @Transactional
    public void updateRole() {
        List<Role> entity = RoleDataEntity.list();
        List<Role> database = em.createQuery("FROM Role r").getResultList();
        if (database.isEmpty()) {
            for (Role role : entity) {
                em.persist(role);
            }
        }
    }

    @Transactional
    public void updateStatusType() {
        List<StatusType> entity = StatusTypeDataEntity.list();
        List<StatusType> database = em.createQuery("FROM StatusType st").getResultList();
        if (database.isEmpty()) {
            for (StatusType st : entity) {
                em.persist(st);
                if (st.getName().equalsIgnoreCase("PROJECT_STATUS")) {
                    em.persist(new Status("Select One", st));
                    em.persist(new Status("NEW", st));
                    em.persist(new Status("INFORMED", st));
                    em.persist(new Status("ASSIGNED", st));
                    em.persist(new Status("DELIVERED", st));
                    em.persist(new Status("ARCHIVED", st));
                    em.persist(new Status("RESTORED", st));
                    em.persist(new Status("INVOICED", st));
                    em.persist(new Status("PAID", st));
                    em.persist(new Status("CLIENT_PAID", st));
                    em.persist(new Status("TRANSLATOR_PAID", st));
                    em.persist(new Status("READY_TO_WORK", st));
                }

            }
        }
    }

    @Transactional
    public void updateProjectType() {
        List<ProjectType> entity = ProjectTypeDataEntity.list();
        List<ProjectType> database = em.createQuery("FROM ProjectType pt").getResultList();
        if (entity.size() != database.size()) {
            for (ProjectType pt : entity) {
                em.persist(pt);
            }
        }
    }

    @Transactional
    public void updateLanguages() {
        List<Language> entity = LanguageDataEntity.list();
        List<Language> database = em.createQuery("FROM Language l").getResultList();
        if (database.isEmpty()) {
            for (Language language : entity) {
                em.persist(language);
            }
        }
    }

    @Transactional
    public void updateTranslationArea() {
        List<TranslationArea> entity = TranslationAreaDataEntity.list();
        List<TranslationArea> database = em.createQuery("FROM TranslationArea ta").getResultList();
        if (entity.size() != database.size()) {
            for (TranslationArea ta : entity) {
                em.persist(ta);
            }
        }
    }

    @Transactional
    public void updateTimeZone() {
        List<TimeZone> entity = TimeZoneDataEntity.list();
        List<TimeZone> database = em.createQuery("FROM TimeZone tz").getResultList();
        if (database.isEmpty()) {
            for (TimeZone tz : entity) {
                em.persist(tz);
            }
        }
    }

    @Transactional
    public void updateServiceProvided() {
        List<ServiceProvided> entity = ServiceProvidedDataEntity.getServiceProvided();
        List<ServiceProvided> database = em.createQuery("FROM ServiceProvided sp").getResultList();
        if (database.isEmpty()) {
            for (ServiceProvided sp : entity) {
                em.persist(sp);
            }
        }
    }

    @Transactional
    public void updateCurrency() {
        List<Currency> entity = CurrencyDataEntity.getCurrencyList();
        List<Currency> database = em.createQuery("FROM Currency").getResultList();
        if (database.isEmpty()) {
            for (Currency currency : entity) {
                em.persist(currency);
            }
        }
    }

    @Transactional
    public void updateCat() {
        List<Cat> entity = CatDataEntity.getCatList();
        List<Cat> database = em.createQuery("FROM Cat c").getResultList();
        if (database.isEmpty()) {
            for (Cat cat : entity) {
                em.persist(cat);
            }
        }
    }

    @Transactional
    public void updateEducationDegree() {
        List<EducationDegree> entity = EducationDegreeDataEntity.getEducationDegreeList();
        List<EducationDegree> database = em.createQuery("FROM EducationDegree ed").getResultList();
        if (database.isEmpty()) {
            for (EducationDegree ed : entity) {
                em.persist(ed);
            }
        }
    }

    @Transactional
    public void updateCountries() {
        List<Country> entityCountries = CountryDataEntity.getCountryData();
        List<Country> database = em.createQuery("FROM Country c").getResultList();
        if (database.isEmpty()) {
            for (Country country : entityCountries) {
                em.persist(country);
            }
        }
    }

    @Transactional
    public void updateSegmentation() {
        List<Segmentation> entitySeg = SegmentationDataEntity.getSegmentation();
        List<Segmentation> database = em.createQuery("FROM Segmentation s").getResultList();
        if (database.isEmpty()) {
            for (Segmentation seg : entitySeg) {
                em.persist(seg);
            }
        }
    }

    @Transactional
    public void updatePersonType() {
        List<PersonType> entityPersonType = PersonTypeDataEntity.getPersonType();
        List<PersonType> database = em.createQuery("FROM PersonType pt").getResultList();
        if (database.isEmpty()) {
            for (PersonType pt : entityPersonType) {
                em.persist(pt);
            }
        }
    }
}
