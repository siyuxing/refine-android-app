package com.refine.utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import android.util.Base64;
import com.mysql.jdbc.StringUtils;

public class PasswordUtils {
    private static final SecretKey SECRET_KEY = new SecretKeySpec("hashkey313720280".getBytes(),"AES");

    public static String encrypt(String strClearText) {
        if (StringUtils.isNullOrEmpty(strClearText)) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY);
            byte[] encrypted = cipher.doFinal(strClearText.getBytes());
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static String decrypt(String strEncrypted) {
        if (StringUtils.isNullOrEmpty(strEncrypted)) {
            return null;
        }

        try {
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY);
            byte[] decrypted = cipher.doFinal(Base64.decode(strEncrypted, Base64.DEFAULT));
            return new String(decrypted);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
