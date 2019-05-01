/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.util.dataEntity;

import com.tms.model.entity.Cat;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sdrahnea
 */
public class CatDataEntity {
 
    public static List<Cat> getCatList(){
        List<Cat> result = new LinkedList<>();
        
        result.add(new Cat("ACROSS"));
        result.add(new Cat("InDesign"));
        result.add(new Cat("MemoQ"));
        result.add(new Cat("Omega"));
        result.add(new Cat("SDLX"));
        result.add(new Cat("TRADOS"));
        result.add(new Cat("Transit"));
        result.add(new Cat("WORDFAST"));
        
        return result;
    }
    
}
