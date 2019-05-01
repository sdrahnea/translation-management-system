/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.util.message;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author sdrahnea
 */
public class Message {

    private final static String OPERATION_STATUS = "Operation status";

    public static void throwInfoMessage(final String detail) {
        if (detail != null && !detail.isEmpty()) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, OPERATION_STATUS, detail);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public static void throwErrorMessage(String... arg) {
        if (arg.length == 1) {
            throwErrorMessage(OPERATION_STATUS, arg[0]);
        } else if (arg.length == 2) {
            throwErrorMessage(arg[0], arg[1]);
        }
    }

    public static void throwWarnMessage(String... arg) {
        if (arg.length == 1) {
            throwWarnMessage(OPERATION_STATUS, arg[0]);
        } else if (arg.length == 2) {
            throwWarnMessage(arg[0], arg[1]);
        }
    }

    public static void throwFatalMessage(String... arg) {
        if (arg.length == 1) {
            throwFatalMessage(OPERATION_STATUS, arg[0]);
        } else if (arg.length == 2) {
            throwFatalMessage(arg[0], arg[1]);
        }
    }

    public static void throwErrorMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
    }

    public static void throwInfoMessage(String summary, String detail) {
        if (detail != null && !detail.isEmpty()) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }
    }

    public static void throwWarnMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, summary, detail));
    }

    public static void throwFatalMessage(String summary, String detail) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_FATAL, summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

}
