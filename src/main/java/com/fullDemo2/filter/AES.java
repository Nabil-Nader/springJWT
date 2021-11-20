package com.fullDemo2.filter;



import com.fullDemo2.constant.SecurityConstant;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AES {


   public static String encryptMyAES(String value) {
        try {
            IvParameterSpec iv = new IvParameterSpec(SecurityConstant.INIT_VECTOR.getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new
                    SecretKeySpec(SecurityConstant.PRIVATE_KEY_FOR_AES
                    .getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

            byte[] encrypted = cipher.doFinal(value.getBytes());
            byte[] original = Base64.getEncoder().encode(encrypted);

            System.out.println("------Inside AES Class Encrypt------------");

            System.out.println(new String(original));
            System.out.println("----------------------Done with encrypt");

//            System.out.println("---------Call Decrypt -------------------");
//            System.out.println(decrypt(new String(original)));
//
//            System.out.println("-----------------Done with Decrypt ");



            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String decrypt(String encrypted) {
        try {
            IvParameterSpec iv = new IvParameterSpec(SecurityConstant.INIT_VECTOR
                    .getBytes(StandardCharsets.UTF_8));
            SecretKeySpec skeySpec = new SecretKeySpec(SecurityConstant.PRIVATE_KEY_FOR_AES.
                    getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
            byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
            return new String(original);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}