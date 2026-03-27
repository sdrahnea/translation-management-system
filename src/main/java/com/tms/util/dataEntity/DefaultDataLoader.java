package com.tms.util.dataEntity;

import com.tms.model.entity.Cat;
import com.tms.model.entity.Client;
import com.tms.model.entity.Country;
import com.tms.model.entity.Currency;
import com.tms.model.entity.EducationDegree;
import com.tms.model.entity.Language;
import com.tms.model.entity.PaymentMethod;
import com.tms.model.entity.PersonType;
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
import com.tms.repository.CatRepository;
import com.tms.repository.ClientRepository;
import com.tms.repository.CountryRepository;
import com.tms.repository.CurrencyRepository;
import com.tms.repository.EducationDegreeRepository;
import com.tms.repository.LanguageRepository;
import com.tms.repository.PaymentMethodRepository;
import com.tms.repository.PersonTypeRepository;
import com.tms.repository.ProjectTypeRepository;
import com.tms.repository.RoleRepository;
import com.tms.repository.SegmentationRepository;
import com.tms.repository.ServiceProvidedRepository;
import com.tms.repository.StatusRepository;
import com.tms.repository.StatusTypeRepository;
import com.tms.repository.TimeZoneRepository;
import com.tms.repository.TranslationAreaRepository;
import com.tms.repository.TranslatorRepository;
import com.tms.repository.UserRepository;
import com.tms.util.crypt.CryptMD5;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Loads default reference data into the database on first run.
 * All persistence is done via Spring Data repositories — no EntityManager.
 */
@Service
public class DefaultDataLoader {

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private SegmentationRepository segmentationRepository;
    @Autowired
    private PersonTypeRepository personTypeRepository;
    @Autowired
    private EducationDegreeRepository educationDegreeRepository;
    @Autowired
    private CatRepository catRepository;
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private ServiceProvidedRepository serviceProvidedRepository;
    @Autowired
    private TranslationAreaRepository translationAreaRepository;
    @Autowired
    private LanguageRepository languageRepository;
    @Autowired
    private ProjectTypeRepository projectTypeRepository;
    @Autowired
    private StatusTypeRepository statusTypeRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PaymentMethodRepository paymentMethodRepository;
    @Autowired
    private TimeZoneRepository timeZoneRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private TranslatorRepository translatorRepository;

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

    @Transactional
    public void createDefaultUsers() {
        if (userRepository.count() > 0) {
            return;
        }

        Role clientRole     = roleRepository.find("CLIENT");
        Role translatorRole = roleRepository.find("TRANSLATOR");
        Role adminRole      = roleRepository.find("ADMIN");
        Role managerRole    = roleRepository.find("MANAGER");

        User clientUser = new User();
        clientUser.setName("Client only");
        clientUser.setLogin("client");
        clientUser.setRole(clientRole);
        clientUser = CryptMD5.createHashPassword(clientUser, "123");
        clientUser = userRepository.save(clientUser);

        User translatorUser = new User();
        translatorUser.setName("Translator only");
        translatorUser.setLogin("translator");
        translatorUser.setRole(translatorRole);
        translatorUser = CryptMD5.createHashPassword(translatorUser, "123");
        translatorUser = userRepository.save(translatorUser);

        User adminUser = new User();
        adminUser.setName("Admin only");
        adminUser.setLogin("admin");
        adminUser.setRole(adminRole);
        adminUser = CryptMD5.createHashPassword(adminUser, "123");
        userRepository.save(adminUser);

        User managerUser = new User();
        managerUser.setName("Manager only");
        managerUser.setLogin("manager");
        managerUser.setRole(managerRole);
        managerUser = CryptMD5.createHashPassword(managerUser, "123");
        userRepository.save(managerUser);

        Client client = new Client("client");
        client.setUser(clientUser);
        clientRepository.save(client);

        Translator translator = new Translator();
        translator.setName("translator");
        translator.setIsEnabled(true);
        translator.setUser(translatorUser);
        translatorRepository.save(translator);
    }

    @Transactional
    public void updatePaymentMethod() {
        List<PaymentMethod> entity = PaymentMethodDataEntity.list();
        if (paymentMethodRepository.count() == 0) {
            paymentMethodRepository.save(entity);
        }
    }

    @Transactional
    public void updateRole() {
        List<Role> entity = RoleDataEntity.list();
        if (roleRepository.count() == 0) {
            roleRepository.save(entity);
        }
    }

    @Transactional
    public void updateStatusType() {
        List<StatusType> entity = StatusTypeDataEntity.list();
        if (statusTypeRepository.count() == 0) {
            for (StatusType st : entity) {
                st = statusTypeRepository.save(st);
                if (st.getName().equalsIgnoreCase("PROJECT_STATUS")) {
                    java.util.Arrays.asList(
                        new Status("Select One", st),
                        new Status("NEW", st),
                        new Status("INFORMED", st),
                        new Status("ASSIGNED", st),
                        new Status("DELIVERED", st),
                        new Status("ARCHIVED", st),
                        new Status("RESTORED", st),
                        new Status("INVOICED", st),
                        new Status("PAID", st),
                        new Status("CLIENT_PAID", st),
                        new Status("TRANSLATOR_PAID", st),
                        new Status("READY_TO_WORK", st)
                    ).forEach(statusRepository::save);
                }
            }
        }
    }

    @Transactional
    public void updateProjectType() {
        List<ProjectType> entity = ProjectTypeDataEntity.list();
        if (projectTypeRepository.count() != entity.size()) {
            projectTypeRepository.save(entity);
        }
    }

    @Transactional
    public void updateLanguages() {
        List<Language> entity = LanguageDataEntity.list();
        if (languageRepository.count() == 0) {
            languageRepository.save(entity);
        }
    }

    @Transactional
    public void updateTranslationArea() {
        List<TranslationArea> entity = TranslationAreaDataEntity.list();
        if (translationAreaRepository.count() != entity.size()) {
            translationAreaRepository.save(entity);
        }
    }

    @Transactional
    public void updateTimeZone() {
        List<TimeZone> entity = TimeZoneDataEntity.list();
        if (timeZoneRepository.count() == 0) {
            timeZoneRepository.save(entity);
        }
    }

    @Transactional
    public void updateServiceProvided() {
        List<ServiceProvided> entity = ServiceProvidedDataEntity.getServiceProvided();
        if (serviceProvidedRepository.count() == 0) {
            serviceProvidedRepository.save(entity);
        }
    }

    @Transactional
    public void updateCurrency() {
        List<Currency> entity = CurrencyDataEntity.getCurrencyList();
        if (currencyRepository.count() == 0) {
            currencyRepository.save(entity);
        }
    }

    @Transactional
    public void updateCat() {
        List<Cat> entity = CatDataEntity.getCatList();
        if (catRepository.count() == 0) {
            catRepository.save(entity);
        }
    }

    @Transactional
    public void updateEducationDegree() {
        List<EducationDegree> entity = EducationDegreeDataEntity.getEducationDegreeList();
        if (educationDegreeRepository.count() == 0) {
            educationDegreeRepository.save(entity);
        }
    }

    @Transactional
    public void updateCountries() {
        List<Country> entityCountries = CountryDataEntity.getCountryData();
        if (countryRepository.count() == 0) {
            countryRepository.save(entityCountries);
        }
    }

    @Transactional
    public void updateSegmentation() {
        List<Segmentation> entitySeg = SegmentationDataEntity.getSegmentation();
        if (segmentationRepository.count() == 0) {
            segmentationRepository.save(entitySeg);
        }
    }

    @Transactional
    public void updatePersonType() {
        List<PersonType> entityPersonType = PersonTypeDataEntity.getPersonType();
        if (personTypeRepository.count() == 0) {
            personTypeRepository.save(entityPersonType);
        }
    }
}
