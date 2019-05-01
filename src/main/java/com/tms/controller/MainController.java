package com.tms.controller;

import com.tms.model.entity.User;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Controller;

/**
 *
 * @author sdrahnea
 */
@Controller
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS, value="session")
public class MainController implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private Integer activeTabIndex = 0;
    
    private User loggedUser;
    
    @Autowired
    HttpSession session;
    
    @PostConstruct
    public void init() {
        try {
        //loggedUser = (User) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user");
        //HttpSession session = SessionUtil.getSession();
        loggedUser = (User) session.getAttribute("user");
        //loggedUser = sessionUtil.getSessionUser();
        //loggedUser = SessionUtil.getUser();
        } catch(Exception ex) {
            System.out.println("init: " + ex);
        }
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }
    
    //    rest shits
    public Integer getActiveTabIndex() {
        return activeTabIndex;
    }

    public void setActiveTabIndex(Integer activeTabIndex) {
        this.activeTabIndex = activeTabIndex;
//        if (null != activeTabIndex) switch (activeTabIndex) {
//            case 0:
//                showProject();
//                break;
//            case 1:
//                showClient();
//                break;
//            case 2:
//                showTranslator();
//                break;
//            default:
//                break;
//        }
    }
    
}
