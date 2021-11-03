package com.zipper.sign.core.util;

public class StringUtil {

    private static final String[] subChars = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String byte2String(byte data){
        int temp = data;
        if(data < 0){
            temp = data + 256;
        }
        int a = temp / 16;
        int b = temp % 16;
        return subChars[a] + subChars[b];
    }

    public static String byte2String1(byte data){
        int a = (int) data >> 4 & 15;
        int b = (int) data & 15;
        return subChars[a] + subChars[b];
    }

    public static String byteToHexString(byte[] data) {
        StringBuilder stringBuffer = new StringBuilder();
        for (byte datum : data) {
            stringBuffer.append(String.format("%02X", datum & 0xFF));
        }
        return stringBuffer.toString();
    }

    public static byte[] hexString2byte(String text) {
        byte[] bytes = new byte[text.length() / 2];
        for (int i = 0; i < text.length() / 2; i ++) {
            bytes[i] = (byte)Integer.parseInt(text.substring(i* 2 , i*2 + 2),16);
        }
        return bytes;
    }

    public static String byte2String(byte[] datas){
        StringBuilder builder = new StringBuilder(datas.length * 2);
        for( byte data : datas){
            builder.append(byte2String(data));
        }
        return builder.toString();
    }

}
