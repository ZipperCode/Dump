package com.zipper.dump.utils;

import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class Name {

    public static String parseSignature(byte[] bArr, char c2) {
        String str = "";
        try {
            String substring = new String(Base64.encode(((X509Certificate) CertificateFactory.getInstance("X.509")
                    .generateCertificate(new ByteArrayInputStream(bArr))).getPublicKey().getEncoded(), 10)).substring(9);
            String substring2 = substring.substring(0, substring.length() - 5);
            str = substring2.substring(substring2.length() - 36);
            Log.d("AAAAA","str = " + str);
            return str.substring(0, str.length() - (c2 % '\n'));
        } catch (CertificateException e) {
            e.printStackTrace();
            return str;
        }
    }

}
