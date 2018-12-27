package com.shineyue.bpm.util;

import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * 机密工具
 *
 * @author liubo
 * @version 2018-12-12 16：49
 **/

public class DecryptUtil {
    private static final String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJ9oso3tqD+7W5HV75i8+x23eZnQtWgbIQp1gwfr1b2XzlRIUZDyvUYyJ6SAL/WJ7G9bM7eZKUn7ycI3nL3I4DtXiFSwzSEkUSbNbALGcJHyWPF+DD/xgsasJ2FhUf/oSAfX5mWlLDRmod1ZFxxRZVbmkzAjNZTQNbqd95wnBMM9AgMBAAECgYAsmX5bd0K7kg/snY6LjVQ03ECJaHtYxT5ZTsd6T+FdSjvQGOu5QjDyktXEkJjaedxMy5eMpq2GpYutayHPd4REpKlSRv9X3rqEeLv2Pdn0vv5aXRvS1NX3BG+ShkPpSpq1+7FaKS/U6lck+PDJjnmiU2GXpE6f0yk+m+oJ5Z/dgQJBANuk+XiVGgLcY1TNf1VKtjmvgX/cva29g/tPyw0Sqm6A1Vujv4cPja09R2WetLgWItaD8tQsIh7F04CyHEXNYZcCQQC5y1twRBtDJ21sGoG9xDPVb3MvBUoviwPGMfYUfhyIdzeIquNmy8/olRHtziZhax2K3fDUFljBZO6jLqIP/bRLAkAJ6QasjyWuTV1Z/S+qnIVeUvHn4ADa7vsegropq9bux3dY/ClvIZ+F2ob3KdMDn1QRE8EIG7mqY8ioPOETsTGfAkEApjxrrgFiGy3V29nDqBU7A4damgGzpOJPGDBvq1pDEV8J00CDguBtP/7RMeM3uAZtQmjeKAVumGtEkcERxBkODQJBAIyp7bOW3opp02w04wujnZzKsjS9Q1CB0485YRH3kge5ovEwbOP9DSehtxjV+vWt/GV5nk1dHaZOwo8e2+GKTwg=";

    public static String DESCrypt(String password) {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        try {
            byte[] keyByte = base64Decoder.decodeBuffer(privateKey);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyByte);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] result = cipher.doFinal(base64Decoder.decodeBuffer(password));
            return new String(result);
        } catch (Exception e) {
            e.printStackTrace();
            return "解密失败";
        }
    }
}
