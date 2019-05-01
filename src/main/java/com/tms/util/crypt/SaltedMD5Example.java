/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tms.util.crypt;

import com.tms.model.entity.User;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.mail.NoSuchProviderException;

/**
 *
 * @author sdrahnea
 */
public class SaltedMD5Example 
{
   
    public static boolean autentificate(final User user){
        final byte [] salt = {89,-20,-48,87,-81,-98,-74,-93,72,118,-103,12,-5,-82,99,-44};
        System.out.println(getSecurePassword("password", salt));
        return false;
    }
    
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, java.security.NoSuchProviderException 
    {
        String passwordToHash = "password";
        byte[] salt = getSalt();
         
        System.out.println("" + new String(salt));
        
        String securePassword = getSecurePassword(passwordToHash, salt);
        System.out.println(securePassword); 
         
        String regeneratedPassowrdToVerify = getSecurePassword(passwordToHash, salt);
        System.out.println(regeneratedPassowrdToVerify); 
        
        autentificate(null);
        
    }
     
    private static String getSecurePassword(String passwordToHash, byte[] salt)
    {
        String generatedPassword = null;
        try {
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(salt);
            //Get the hash's bytes 
            byte[] bytes = md.digest(passwordToHash.getBytes());
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }
     
    //Add salt
    private static byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException, java.security.NoSuchProviderException
    {
        //Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        for(byte b : salt) {
            System.out.print("," +  b);
        }
        return salt;
    }
}
