

package com.fengzijk.response.sign;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class SignUtils {


    
    public static String getTodayDateTime() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime time = LocalDateTime.now();
        return df.format(time);
    }



    
    public static boolean isBlank(CharSequence cs) {
        int strLen = cs == null ? 0 : cs.length();
        if (strLen != 0) {
            for (int i = 0; i < strLen; ++i) {
                if (!Character.isWhitespace(cs.charAt(i))) {
                    return false;
                }
            }

        }
        return true;
    }

    
    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }



    
    public static String md5Hex(String src) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] b = src.getBytes();
            md.reset();
            md.update(b);
            byte[] hash = md.digest();
            StringBuilder hs = new StringBuilder();
            String stamp;
            for (byte value : hash) {
                stamp = Integer.toHexString(value & 0xFF);
                if (stamp.length() == 1) {
                    hs.append("0").append(stamp);
                } else {
                    hs.append(stamp);
                }
            }
            return hs.toString().toUpperCase();
        } catch (Exception e) {
            return "";
        }
    }


    
    public static String sha256Hex(String str) {

        MessageDigest messageDigest;

        String encodeStr = "";

        try {

            messageDigest = MessageDigest.getInstance("SHA-256");

            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));

            encodeStr = byte2Hex(messageDigest.digest());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return encodeStr.toUpperCase();

    }


    
    private static String byte2Hex(byte[] bytes) {

        StringBuilder stringBuffer = new StringBuilder();

        String temp;
        for (byte aByte : bytes) {
            temp = Integer.toHexString(aByte & 0xFF);
            if (temp.length() == 1) {
                //1得到一位的进行补0操作
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }

}
