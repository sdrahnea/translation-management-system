/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.util.crypt;

import java.security.MessageDigest;

/**
 * CryptWithMD5 class is using to crypt chars using MD5 algorithm.
 * @author sdrahnea
 */
public class CryptWithMD5 {

    private static MessageDigest md;

    /**
     * crypt entered word
     * @param pass
     * @return  MD5 crypt word
     */
    public static String cryptWithMD5(final String word) {
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] passBytes = word.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digested.length; i++) {
                sb.append(Integer.toHexString(0xff & digested[i]));
            }
            return sb.toString();
        } catch (Exception ex) {
            System.out.println("" + ex);
        }
        return null;
    }
    
}
