/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 *
 * @author sdrahnea
 */
public class FileUtil {

    public static final void main(String... arg) {
        try {
            FileInputStream fstream = new FileInputStream("g:/l.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

            String strLine;

//Read File Line By Line
            while ((strLine = br.readLine()) != null) {
                // Print the content on the console
//                System.out.println("countries.add(new Country(\"" + strLine.trim() + "\"));");
//                System.out.println("result.add(new TranslationArea(\"" + strLine.trim() + "\"));");
                if(!"".equalsIgnoreCase(strLine.trim())){
                    System.out.println("result.add(new Language(\"" + strLine.trim() + "\"));");
                }
            }

//Close the input stream
            br.close();
        } catch (Exception ex) {
            System.out.println("" + ex);
        }
    }
    
}
