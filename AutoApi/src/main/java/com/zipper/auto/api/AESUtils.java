package com.zipper.auto.api;
import android.util.Base64;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import kotlin.UByte;

public class AESUtils {
    private static final String PUBLIC_KEY = "Jiangjia2020";

    public static String encode(String stringToEncode) throws NullPointerException {
        try {
            SecretKeySpec skeySpec = getKey(md5(PUBLIC_KEY));
            byte[] clearText = stringToEncode.getBytes("UTF8");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
            cipher.init(1, skeySpec);
            return Base64.encodeToString(cipher.doFinal(clearText), 0);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return "";
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            return "";
        } catch (NoSuchAlgorithmException e3) {
            e3.printStackTrace();
            return "";
        } catch (BadPaddingException e4) {
            e4.printStackTrace();
            return "";
        } catch (NoSuchPaddingException e5) {
            e5.printStackTrace();
            return "";
        } catch (IllegalBlockSizeException e6) {
            e6.printStackTrace();
            return "";
        }
    }

    public static String decode(String str) {
        try {
            SecretKeySpec skeySpec = getKey(md5(PUBLIC_KEY));
            byte[] clearText = Base64.decode(str.getBytes(), 0);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding");
            cipher.init(2, skeySpec);
            return new String(cipher.doFinal(clearText), "utf-8");
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            return "";
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
            return "";
        } catch (NoSuchAlgorithmException e3) {
            e3.printStackTrace();
            return "";
        } catch (BadPaddingException e4) {
            e4.printStackTrace();
            return "";
        } catch (NoSuchPaddingException e5) {
            e5.printStackTrace();
            return "";
        } catch (IllegalBlockSizeException e6) {
            e6.printStackTrace();
            return "";
        }
    }

    private static SecretKeySpec getKey(String password) throws UnsupportedEncodingException {
        byte[] keyBytes = new byte[(256 / 8)];
        Arrays.fill(keyBytes, (byte) 0);
        byte[] passwordBytes = password.getBytes("UTF-8");
        System.arraycopy(passwordBytes, 0, keyBytes, 0, passwordBytes.length < keyBytes.length ? passwordBytes.length : keyBytes.length);
        return new SecretKeySpec(keyBytes, "AES");
    }

    public static String md5(String string) {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & UByte.MAX_VALUE) < 16) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & UByte.MAX_VALUE));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e2) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e2);
        }
    }
}