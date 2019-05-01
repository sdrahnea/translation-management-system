/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.util.dataEntity;

import com.tms.model.entity.Segmentation;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author sdrahnea
 */
public class SegmentationDataEntity {
    
    public static List<Segmentation> getSegmentation(){
        List<Segmentation> result = new LinkedList<>();

        result.add(new Segmentation("Translation Company"));
        result.add(new Segmentation("End client"));
        result.add(new Segmentation("Other Company"));
        
        return result;
    }
    
}
