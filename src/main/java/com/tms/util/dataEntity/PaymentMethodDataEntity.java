/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.util.dataEntity;

import com.tms.model.entity.PaymentMethod;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sdrahnea
 */
public class PaymentMethodDataEntity {
 
    public static List<PaymentMethod> list(){
        List<PaymentMethod> result = new LinkedList<>();
        
        result.add(new PaymentMethod("PayPal"));
        result.add(new PaymentMethod("Bank"));
        result.add(new PaymentMethod("CHECK"));
        
        return result;
    }
    
}
