package com.tms.controller.entity;

import com.tms.model.entity.Person;
import com.tms.model.entity.PersonType;
import com.tms.model.entity.User;
import com.tms.model.entity.service.UserService;
import java.util.Date;
import javax.annotation.PostConstruct;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

/**
 *
 * @author sdrahnea
 */
@Component
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS, value="request")
public class ManagerController extends Controller<Person> {

    private Person person = new Person();
    private Session session = null; //HibernateUtil.getCurrentSession();
    private User user = new User();
    
    @Autowired
    private UserService userService;

    @PostConstruct
    @Override
    public void init() {
        person = new Person();
        user = new User();
        this.getEntityList().clear();
        this.getEntityList().addAll(session.createQuery("FROM Person entity WHERE entity.personType.name = 'MANAGER'").list());
    }

    public void save() {
        PersonType type = (PersonType) session.createQuery("FROM PersonType pt WHERE pt.name = 'MANAGER'").list().get(0);
        Transaction transaction = session.beginTransaction();
        person.setPersonType(type);
        person.setInsertDate(new Date());
        session.save(person);
        transaction.commit();

        userService.createManager(user, person);

        session.refresh(person);

        init();
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
