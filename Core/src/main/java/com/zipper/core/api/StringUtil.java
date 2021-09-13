package com.zipper.core.api;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

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
        int temp = data;
//        if(data < 0){
//            temp = data + 256;
//        }

        int a = temp >> 4 & 15;
        int b = temp & 15;
        return subChars[a] + subChars[b];
    }

    public static String byteToHexString(byte data[]) {
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

    public static String sign1(String key, Map<String, String> params, String bodyJson) {
        List<String> list = new ArrayList<>(params.keySet());
        Collections.sort(list);
        Iterator<String> iterator = list.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(key);
        while (iterator.hasNext()){
            String paramKey = iterator.next();
            String paramValue = (String) params.get(paramKey);
            stringBuilder.append(paramKey).append("=").append(paramValue);
        }
        stringBuilder.append(bodyJson);
        stringBuilder.append(key);
        return stringBuilder.toString();
    }

    public static String signature(Map<String, String> params, String bodyJson){
        String result = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            String body = sign1("NVPh5oo715z5DIWAeQlhMDsWXXQV4hwt", params, bodyJson);
            byte[] data = messageDigest.digest(body.getBytes(StandardCharsets.UTF_8));
            result = byte2String(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String signature(String key, Map<String, String> params, String bodyJson){
        String result = "";
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            String body = sign1(key, params, bodyJson);
            byte[] data = messageDigest.digest(body.getBytes(StandardCharsets.UTF_8));
            result = byte2String(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void test() throws NoSuchAlgorithmException {
        String token = "808f24298f4c16d5fc94034065381d1864df85b05b5083bf56a249eaafd73504";
        Map<String, String> param = new HashMap<>();
        param.put("dfid" , "-");
        param.put("appid" , "1005");
        param.put("mid" , "19866310614991365975980225640804152227");
        param.put("clientver" , "10829");
        param.put("from" , "client");
        param.put("clienttime" , "1630849591");
        param.put("uuid" , "532544371bb026cc684489e57eedbe9c");
        param.put("userid" , "1882565136");
        param.put("tkick" , "1");
        param.put("token" , token);
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        String body = sign1("", param, "");
        byte[] result = messageDigest.digest(body.getBytes(StandardCharsets.UTF_8));
        System.out.println("1d0e266000d7cc5277607c40085c68dc");
        System.out.println(byte2String(result));
        System.out.println(signature(param, ""));
    }

    public static void testSignOn() throws NoSuchAlgorithmException {
        Map<String, String> param = new HashMap<>();
        param.put("srcappid","2919");
        param.put("clientver","10829");
        param.put("clienttime","1630851621967");
        param.put("mid","19866310614991365975980225640804152227");
        param.put("uuid","532544371bb026cc684489e57eedbe9c");
        param.put("dfid","-");
        param.put("userid","1882565136");
        param.put("token","h5998D15EE89547F05F68CA6D09436A9D5E4BF63E69BCB16D11A0D503D7B8177B1CA06986F6FDC21311468D8A5805219F98CDF7403E1BAB1B0B207EF593CCB5FDE8CE9804FDADFA7176575EFF199CCE70CACF82BDA9EFA1A8E7B680B978201CC960F98C892839264829FC35813D04C46C60314249BD58571F120AE1D5D45F80C57");
        param.put("appid","1005");
        param.put("from","client");
        // signature=82e3a72797ea970556b1640dc7f240f7
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        String body = sign1("NVPh5oo715z5DIWAeQlhMDsWXXQV4hwt", param, "{\"code\":\"20210906\"}");
        byte[] result = messageDigest.digest(body.getBytes(StandardCharsets.UTF_8));
        System.out.println("82e3a72797ea970556b1640dc7f240f7");
        System.out.println(byte2String(result));
        System.out.println(signature(param, "{\"code\":\"20210905\"}"));
    }

    public static void testSubmit() throws NoSuchAlgorithmException {
        Map<String, String> param = new HashMap<>();
        param.put("dfid","-");
        param.put("appid","1005");
        param.put("mid","19866310614991365975980225640804152227");
        param.put("clientver","10829");
        param.put("from","client");
        param.put("clienttime","1630850016");

        param.put("uuid","532544371bb026cc684489e57eedbe9c");
        param.put("userid","1882565136");
        param.put("token","808f24298f4c16d5fc94034065381d1864df85b05b5083bf56a249eaafd73504");

        System.out.println("baabe8b743dc989e0c73e0e36e3bbc7c");
        for (int i = 0; i< 1500; i++){
            String s = signature(param, "{\"taskid\":" + i + "}");
            if(s.equals("baabe8b743dc989e0c73e0e36e3bbc7c")){
                System.out.println("success = " + i);
            }
        }
    }



    public static void main(String[] args) throws NoSuchAlgorithmException {
//        testSubmit();
        System.out.println("1630850016");
        System.out.println("1630851621967");
        System.out.println(new Date().getTime());
    }
}
