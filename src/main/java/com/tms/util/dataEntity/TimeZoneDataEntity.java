/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.util.dataEntity;

import com.tms.model.entity.TimeZone;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sdrahnea
 */
public class TimeZoneDataEntity {
 
    public static List<TimeZone> list(){
        List<TimeZone> result = new LinkedList<>();
        
        result.add(new TimeZone("UTC-12:00"));
        result.add(new TimeZone("UTC-11:00 (New Zealand)"));
        result.add(new TimeZone("UTC-10:00 (Hawaii)"));
        result.add(new TimeZone("UTC-09:00 (Alaska)"));
        result.add(new TimeZone("UTC-08:00 (PST) San Francisco"));
        result.add(new TimeZone("UTC-07:00 (MST) Phoenix"));
        result.add(new TimeZone("UTC-06:00 (CST) Chicago"));
        result.add(new TimeZone("UTC-05:00 (EST) New York"));
        result.add(new TimeZone("UTC-04:00 (AST)"));
        result.add(new TimeZone("UTC-03:00 (Argentina)"));
        result.add(new TimeZone("UTC-02:00 (Brazil)"));
        result.add(new TimeZone("UTC-01:00"));
        result.add(new TimeZone("UTC-00:00 (London)"));
        result.add(new TimeZone("UTC+01:00 (CET)"));
        result.add(new TimeZone("UTC+02:00 (Moldova)"));
        result.add(new TimeZone("UTC+03:00 (Moscow)"));    
        result.add(new TimeZone("UTC+04:00 (Dubai)"));
        result.add(new TimeZone("UTC+05:00"));
        result.add(new TimeZone("UTC+06:00")); 
        result.add(new TimeZone("UTC+07:00"));
        result.add(new TimeZone("UTC+08:00 (Beijing)"));
        result.add(new TimeZone("UTC+09:00 (Tokyo)")); 
        result.add(new TimeZone("UTC+10:00 (Sydney)"));
        result.add(new TimeZone("UTC+11:00")); 
        result.add(new TimeZone("UTC+12:00"));
        result.add(new TimeZone("UTC+13:00")); 
        result.add(new TimeZone("UTC+14:00"));
        
        return result;
    }
    

    
}
