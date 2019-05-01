package com.tms.controller;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

/**
 *
 * @author sdrahnea
 */
public class AbstractController<T> implements Serializable {

    private String crudFormCaption;

    /**
     * 
     * @param arg arg0 - to page, arg1 - prefix caption
     * @return 
     */
    public String workflowController(String... arg) {
        if (arg.length == 1) {
            return arg[0];
        }
        if (arg.length == 2) {
            setCrudFormCaption(arg[1] + " " + getClassName());
            return arg[0];
        }
        return null;
    }

    private String getClassName() {
        String[] className = ("" + ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]).split("\\.");
        return className[className.length - 1];
    }

    public String getCrudFormCaption() {
        return crudFormCaption;
    }

    public void setCrudFormCaption(String crudFormCaption) {
        this.crudFormCaption = crudFormCaption;
    }

}
