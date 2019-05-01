/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.util.dataEntity;

import com.tms.model.entity.Status;
import com.tms.model.entity.StatusType;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sdrahnea
 */
public class StatusDataEntity {

    public static List<Status> list(List<StatusType> statusTypes) {
        List<Status> result = new LinkedList<>();
        /**
         * DO NOT INSERT HERE DATA
         */
        for (StatusType statusType : statusTypes) {
            if (statusType.getName().equalsIgnoreCase("PROJECT_STATUS")) {
                result.add(new Status("NEW", statusType, 1));
                result.add(new Status("INFORMED", statusType, 2));
                result.add(new Status("READY_TO_WORK", statusType, 3));
                result.add(new Status("ASSIGNED", statusType, 4));
                result.add(new Status("DELIVERED", statusType, 5));
                result.add(new Status("ARCHIVED", statusType, 10));
                result.add(new Status("RESTORED", statusType, 1));
                result.add(new Status("INVOICED", statusType, 6));
                result.add(new Status("PAID", statusType, 7));
                result.add(new Status("CLIENT_PAID", statusType, 8));
                result.add(new Status("TRANSLATOR_PAID", statusType, 9));
            }
        }

        return result;
    }

}
