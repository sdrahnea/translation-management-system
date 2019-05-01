package com.tms.controller;

import com.tms.model.entity.Client;
import com.tms.model.entity.Person;
import com.tms.model.entity.PersonType;
import com.tms.model.entity.Role;
import com.tms.model.entity.Translator;
import com.tms.model.entity.User;
import com.tms.model.entity.dao.ClientDao;
import com.tms.model.entity.dao.PersonDao;
import com.tms.model.entity.dao.PersonTypeDao;
import com.tms.model.entity.dao.RoleDao;
import com.tms.model.entity.dao.TranslatorDao;
import com.tms.model.entity.dao.UserDao;
import com.tms.util.crypt.CryptMD5;
import com.tms.util.message.Message;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
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
public class UserController implements Serializable {

    private List<User> userList = new LinkedList<>();
    private User user = new User();
    private List<Role> roles = new LinkedList<>();
    private Role selectedRole;
    private List<Translator> translators = new LinkedList<>();
    private Translator selectedTranslator;
    private List<Client> clients = new LinkedList<>();
    private Client selectedClient;
    private Person person = new Person();
    private boolean displayTranslator;
    private boolean displayClient;

    private String oldPassword;
    private String newPassword;

    @Autowired
    private RoleDao roleDao;
    
    @Autowired
    private TranslatorDao translatorDao;
    
    @Autowired
    private ClientDao clientDao;
    
    @Autowired
    private PersonTypeDao personTypeDao;
    
    @Autowired
    private PersonDao personDao;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private HttpSession httpSession;
    
    
    @PostConstruct
    public void init() {
        try {
            this.roles = roleDao.findAll();
            this.translators = translatorDao.findAll();
            this.clients = clientDao.findAll();
            callUserList();
        } catch (Exception ex) {

        }
    }

    private void callUserList() {
        this.userList = userDao.findAll();
    }

    public void onRoleSelect() {
        if (selectedRole.getName().toLowerCase().equalsIgnoreCase("translator")) {
            displayTranslator = true;
            displayClient = false;
        } else if (selectedRole.getName().toLowerCase().equalsIgnoreCase("client")) {
            displayTranslator = false;
            displayClient = true;
        } else {
            displayTranslator = false;
            displayClient = false;
        }
    }

    public void changePassword() {
        User sessionUser = (User) httpSession.getAttribute("user");
        if (oldPassword.equalsIgnoreCase(newPassword)) {
            Message.throwFatalMessage("New and old passwords should be different!");
        } else if (CryptMD5.getHashPassword(sessionUser, oldPassword).equalsIgnoreCase(sessionUser.getPassword())) {
            User user = (User) httpSession.getAttribute("user");
            user = CryptMD5.createHashPassword(user, newPassword);
            userDao.merge(user);
        } else {
            Message.throwFatalMessage("Old password is not correct!");
        }

    }

    public void saveUser() {
        if (selectedRole.getName().contains("MANAGER")) {
            PersonType type = personTypeDao.MANAGER();
            person.setPersonType(type);
        }
        personDao.merge(person);

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            user = CryptMD5.createHashPassword(user, "123");
        } else {
            user = CryptMD5.createHashPassword(user, user.getPassword());
        }
        user.setRole(selectedRole);
        user.setInsertDate(new Date());
        user.setName(person.getName());
        userDao.merge(user);

        if (selectedRole.getName().contains("TRANSLATOR")) {
            Translator translator = new Translator();
            translator.setUser(user);
            translator.setName(person.getName());
            translator.setEmail(person.getEmail());
            translator.setContactPhone(person.getPhone());
            translatorDao.merge(translator);
        } else if (selectedRole.getName().contains("CLIENT")) {
            Client client = new Client();
            client.setUser(user);
            client.setName(person.getName());
            client.setInvoiceEmail(person.getEmail());
            clientDao.merge(client);
        }

        selectedClient = null;
        selectedRole = null;
        selectedTranslator = null;
        user = new User();
        person = new Person();
        callUserList();
        Message.throwInfoMessage("New user was created successfuly!");
    }

    public void onRowEdit(RowEditEvent event) {
        // Transaction transaction = session.beginTransaction();
        // session.save((User) event.getObject());
        // transaction.commit();

        FacesMessage msg = new FacesMessage("Edited", "" + ((User) event.getObject()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onRowCancel(RowEditEvent event) {
        Message.throwWarnMessage("Edit option id cancelled");
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public Role getSelectedRole() {
        return selectedRole;
    }

    public void setSelectedRole(Role selectedRole) {
        this.selectedRole = selectedRole;
    }

    public List<Translator> getTranslators() {
        return translators;
    }

    public void setTranslators(List<Translator> translators) {
        this.translators = translators;
    }

    public Translator getSelectedTranslator() {
        return selectedTranslator;
    }

    public void setSelectedTranslator(Translator selectedTranslator) {
        this.selectedTranslator = selectedTranslator;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }

    public Client getSelectedClient() {
        return selectedClient;
    }

    public void setSelectedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
    }

    public boolean isDisplayTranslator() {
        return displayTranslator;
    }

    public void setDisplayTranslator(boolean displayTranslator) {
        this.displayTranslator = displayTranslator;
    }

    public boolean isDisplayClient() {
        return displayClient;
    }

    public void setDisplayClient(boolean displayClient) {
        this.displayClient = displayClient;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

}
