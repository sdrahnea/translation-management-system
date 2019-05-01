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
public class CryptMD5 {

    public static String getHashPassword(final User user, final String noHasshPassword) {
        final byte[] userSalt = user.getSalt();
        if (userSalt != null || userSalt.length > 0) {
            return getSecurePassword(noHasshPassword, userSalt);
        }
        return null;
    }

    public static User createHashPassword(User user, final String noHasshPassword) {
        try {
            final byte[] salt = getSalt();
            user.setPassword(getSecurePassword(noHasshPassword, salt));
            user.setSalt(salt);
            return user;
        } catch (Exception ex) {
            System.out.println("" + ex);
            return null;
        }
    }

    private static String getSecurePassword(String passwordToHash, byte[] salt) {
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
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    //Add salt
    private static byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException, java.security.NoSuchProviderException {
        //Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        for (byte b : salt) {
            System.out.print("," + b);
        }
        return salt;
    }

}
