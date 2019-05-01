/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.util.dataEntity;

import com.tms.model.entity.Currency;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sdrahnea
 */
public class CurrencyDataEntity {

    public static List<Currency> getCurrencyList() {
        List<Currency> result = new LinkedList<>();
        
        result.add(new Currency("USD"));
        result.add(new Currency("EUR"));
        result.add(new Currency("GBP"));
        result.add(new Currency("CAD"));
        
        return result;
    }

}
