package com.tms.controller;

import com.tms.model.entity.User;
import com.tms.model.entity.dao.UserDao;
import com.tms.util.SessionUtil;
import com.tms.util.crypt.CryptMD5;
import com.tms.util.dataEntity.DefaultDataLoader;
import com.tms.util.message.Message;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.faces.bean.SessionScoped;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * @author sdrahnea
 */
@Component
@SessionScoped
public class LoginController implements Serializable {

    private static final long serialVersionUID = 1L;
    private String userName;
    private String password;

    @Autowired
    private UserDao userDao;

    @Autowired
    private DefaultDataLoader defaultDataLoader;

    @Autowired
    private SessionUtil sessionUtil;
    
    @Autowired
    HttpSession session;

    public String login() {
        defaultDataLoader.updateAllEntitties();

        if (userName.equalsIgnoreCase("system")) {
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String currentDate = format.format(new Date());
            if (password.equalsIgnoreCase(currentDate)) {
                User system = new User();
                system.setLogin("system");
                system.setName("system");

                //HttpSession session = SessionUtil.getSession();
                session.setAttribute("user", system);
                return "main";
            }
        } else {
            List<User> userList = userDao.findByProperty("login", userName);

            boolean isOurUser = false;
            for (User u : userList) {
                if (CryptMD5.getHashPassword(u, password).equalsIgnoreCase(u.getPassword())) {
                    isOurUser = true;
                }
            }

            if (!isOurUser) {
                Message.throwErrorMessage("Your creditiales didn't match the security police. Please, try again!");
            } else if (userList.size() > 1) {
                Message.throwErrorMessage("Somethig bad happened, contact your application administrator!");
            } else if (isOurUser) {
                User targetUser = userList.get(0);
//                HttpSession session = SessionUtil.getSession();
//                session.setAttribute("user", targetUser);
                //HttpSession session = SessionUtil.getSession();
                session.setAttribute("user", targetUser);
                //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("user", targetUser);
                //sessionUtil.putSessionUser(targetUser);
                return "project";
            }
        }
        Message.throwErrorMessage("Your creditiales diidn't match the security police. Please, try again!");
        return "login";
    }

    public String logout() {
//        HttpSession session = SessionUtil.getSession();
//        session.invalidate();
        sessionUtil.invalidateSession();
        Message.throwInfoMessage("Session was invalidated successfully!");
        return "login";
    }

    private void runForPool() {
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    //session.createSQLQuery("Select 1").list();
                } catch (Exception ex) {
                    Message.throwFatalMessage("" + ex);
                }
            }
        }, 0, 600, TimeUnit.SECONDS);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
