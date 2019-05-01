/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.util;

import com.tms.model.entity.ProjectDetail;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 *
 * @author sdrahnea
 */
public class CompareUtil {

    /**
     * 
     * @param details
     * @param value
     * @return 
     */
    public static boolean comparasionProjectDetail(Collection<ProjectDetail> details, String value) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        if (details == null || details.isEmpty()) {
            return true;
        }

        for (ProjectDetail detail : details) {
            if (detail.getFileName().equalsIgnoreCase(value)) {
                return true;
            }
        }
        return false;
    }

    public static boolean comparasion(Date column, Date value, int type) {
        if ((column == null) && (value == null)) {
            return true;
        } else if ((column == null) && (value != null)) {
            return false;
        } else if ((column != null) && (value != null)) {
            return (type < 0) ? column.before(value) : column.after(value);
        }
        return true;
    }

    public static boolean comparasion(String column, String value) {
        if (value != null && value.equalsIgnoreCase("Select One")) {
            return true;
        }

        if (column == null) {
            if (value == null || value.isEmpty()) {
                return true;
            } else {
                return false;
            }
        } else if (column.isEmpty() && value.isEmpty()) {
            return true;
        }
        return column.toLowerCase().contains(coalesce(value, column).toLowerCase());
    }

    public static boolean comparasion(List<String> columns, List<String> values) {
        boolean result = false;
        for (String col : columns) {
            boolean check = false;
            for (String val : values) {
                if (!check) {
                    check = comparasion(col, val);
                }
            }
        }
        return result;
    }

    public static String coalesce(String parameter1, String parameter2) {
        return (parameter1 == null || parameter1.isEmpty()) ? parameter2 : parameter1;
    }

}
