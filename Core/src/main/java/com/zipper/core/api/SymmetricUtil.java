package com.zipper.core.api;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class SymmetricUtil {

    private static final String TYPE_DES = "DES";
    private static final String TYPE_AES = "AES";
    public static final String TYPE_AES_ECB_PSK5 = "AES/ECB/PKCS5Padding";

    public static byte[] getSecretKey(String encryptType) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(encryptType);
        keyGenerator.init(new SecureRandom());
        SecretKey generateKey = keyGenerator.generateKey();
        return generateKey.getEncoded();
    }

    public static SecretKeySpec getSecretKey(byte[] key, String type) {
        return new SecretKeySpec(key, type);
    }

    public static byte[] encrypt(byte[] key, String plain, String type, String formation) {
        SecretKey secretKey = getSecretKey(key,type);
        try {
            Cipher cipher = Cipher.getInstance(formation);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(plain.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static byte[] encryptCbc(byte[] key, byte[] iv, byte[] plain, String type, String formation) {
        SecretKey secretKey = getSecretKey(key,type);
        try {
            Cipher cipher = Cipher.getInstance(formation);
            // 使用CBC模式，需要一个向量iv，可增加加密算法的强度
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            return cipher.doFinal(plain);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static byte[] encrypt(byte[] key, byte[] plain, String type, String formation) {
        SecretKey secretKey = getSecretKey(key,type);
        try {
            Cipher cipher = Cipher.getInstance(formation);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(plain);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static byte[] encrypt(byte[] key, String plain, String type) {
        SecretKey secretKey = new SecretKeySpec(key, type);
        try {
            Cipher cipher = Cipher.getInstance(type);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return cipher.doFinal(plain.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static byte[] decrypt(byte[] key, String text, String type) {
        SecretKey secretKey = new SecretKeySpec(key, type);
        try {
            Cipher cipher = Cipher.getInstance(type);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static byte[] decrypt(byte[] key, byte[] text, String type) {
        SecretKey secretKey = new SecretKeySpec(key, type);
        try {
            Cipher cipher = Cipher.getInstance(type);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static byte[] decrypt(byte[] key, byte[] text, String type, String format) {
        SecretKey secretKey = new SecretKeySpec(key, type);
        try {
            Cipher cipher = Cipher.getInstance(format);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return cipher.doFinal(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
//        byte[] bs1 = getSecretKey(TYPE_DES);
//        System.out.println(Arrays.toString(bs1));
//        String s = StringUtil.byteToHexString(bs1);
//        System.out.println(s);
//        byte[] bs2 = StringUtil.hexString2byte(s);
//        System.out.println(Arrays.toString(bs2));


    }
}
