package com.tms.controller;

import com.tms.util.BackUpper;
import com.tms.util.message.Message;
import java.io.Serializable;
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
public class AdminController implements Serializable {

    @Autowired
    private BackUpper backUpper;
    
    public void restoreLastBackUp() {
        try {
            backUpper.restoreLastBackUp();
            Message.throwInfoMessage("Command was executed successfully!");
        } catch (Exception ex) {
            Message.throwFatalMessage("Exception", "" + ex);
        }
    }
}
