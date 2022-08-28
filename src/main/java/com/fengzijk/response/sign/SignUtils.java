/*
 *   All rights Reserved, Designed By ZTE-ITS
 *   Copyright:    Copyright(C) 2019-2025
 *   Company       FENGZIJK LTD.
 *   @Author:    fengzijk
 *   @Email: guozhifengvip@gmail.com
 *   @Version    V1.0
 *   date:   2022年08月28日 03时33分
 *   Modification       History:
 *   ------------------------------------------------------------------------------------
 *   Date                  Author        Version        Description
 *   -----------------------------------------------------------------------------------
 *  2022-08-28 03:33:35    fengzijk         1.0         Why & What is modified: <修改原因描述>
 *
 *
 */

package com.fengzijk.response.sign;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * <pre>签名工具类</pre>
 *
 * @author guozhifeng
 * @since 2022/8/28
 */
public class SignUtils {


    /**
     * <pre>获取当前时间格式化字符串</pre>

     * @return java.lang.String
    */
    public static String getTodayDateTime() {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        LocalDateTime time = LocalDateTime.now();
        return df.format(time);
    }



    /**
     * <pre>判断字符串是否为空</pre>
     *
     *
     * @param cs 字符串
     * @return boolean

    */
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

    /**
     * <pre>判断字符串是否不为空</pre>
     *
     *
     * @param cs 字符串
     * @return boolean
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return !isBlank(cs);
    }



    /**
     * <pre>MD5加密(32位大写)</pre>
     *
     * @param src 字符串
     * @return java.lang.String
    */
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


    /**
     * <pre>利用java原生的类实现SHA256加密</pre>
     * @author fengzijk
     * @param str 加密后的报文
     * @return java.lang.String
    */
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


    /**
     * <pre>将byte转为16进制</pre>
     *
     * @param bytes 数组
     * @return java.lang.String
    */
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
