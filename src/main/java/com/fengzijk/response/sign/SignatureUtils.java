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




import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * <pre>签名工具类</pre>
 *
 * @author guozhifeng
 * @since 2022/8/28
 */
public class SignatureUtils {

    /**
     * 5分钟有效期
     */
    private final static long MAX_EXPIRE = 5 * 60;


    /**
     * <pre>验证Map参数的签名</pre>
     *
     * @param paramsMap 参数Map
     */
    public static void validateByMap(Map<String, String> paramsMap) {
        if (SignUtils.isBlank(paramsMap.get(SignConstant.SIGN_APP_ID_KEY))) {
            throw new RuntimeException("签名验证失败:platformInfoNo不能为空");
        }
        if (SignUtils.isBlank(paramsMap.get(SignConstant.SIGN_NONCE_KEY))) {
            throw new RuntimeException("签名验证失败:NONCE不能为空");
        }
        if (SignUtils.isBlank(paramsMap.get(SignConstant.SIGN_TIMESTAMP_KEY))) {
            throw new RuntimeException("签名验证失败:TIMESTAMP不能为空");
        }
        if (SignUtils.isBlank(paramsMap.get(SignConstant.SIGN_SIGN_TYPE_KEY))) {
            throw new RuntimeException("签名验证失败:SIGN_TYPE不能为空");
        }
        if (SignUtils.isBlank(paramsMap.get(SignConstant.SIGN_SIGN_KEY))) {
            throw new RuntimeException("签名验证失败:SIGN不能为空");
        }
        if (SignType.contains(paramsMap.get(SignConstant.SIGN_SIGN_TYPE_KEY))) {
            throw new IllegalArgumentException(String.format("签名验证失败:SIGN_TYPE必须为:%s,%s", SignType.MD5, SignType.SHA256));
        }

        String timestamp = paramsMap.get(SignConstant.SIGN_TIMESTAMP_KEY);
        long clientTimestamp = Long.parseLong(timestamp);
        //判断时间戳 timestamp=201808091113
        if ((Long.parseLong(SignUtils.getTodayDateTime()) - clientTimestamp) > MAX_EXPIRE) {
            throw new IllegalArgumentException("签名验证失败:TIMESTAMP已过期");
        }
    }


    /**
     * <pre>验证Map类型参数的签名</pre>
     *
     * @param paramsMap    Map参数
     * @param clientSecret 秘钥
     * @return boolean
     */
    public static boolean validateSignByMap(Map<String, String> paramsMap, String clientSecret) {
        try {
            validateByMap(paramsMap);
            String sign = paramsMap.get(SignConstant.SIGN_SIGN_KEY);
            //重新生成签名
            String signNew = getSignV1(paramsMap, clientSecret);
            //判断当前签名是否正确
            if (signNew.equals(sign)) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    /**
     * <pre>获取V1版本的签名</pre>
     *
     * @param paramMap     Map 参数
     * @param clientSecret 秘钥
     * @return java.lang.String
     */
    public static String getSignV1(Map<String, String> paramMap, String clientSecret) {
        //加密
        String signType = paramMap.get(SignConstant.SIGN_SIGN_TYPE_KEY);
        SignType type = null;
        if (SignUtils.isNotBlank(signType)) {
            type = SignType.valueOf(signType);
        }
        if (type == null) {
            type = SignType.MD5;
        }
        StringBuilder sb = getSignCheckContentV1(paramMap);
        sb.append(SignConstant.SIGN_SECRET_KEY + "=").append(clientSecret);
        String signStr = "";

        switch (type) {
            case MD5:
                signStr = SignUtils.md5Hex(sb.toString()).toUpperCase();
                break;
            case SHA256:
                signStr = SignUtils.sha256Hex(sb.toString()).toUpperCase();
                break;
            default:
                break;
        }
        return signStr;
    }

    public static StringBuilder getSignCheckContentV1(Map<String, String> paramMap) {
        if (paramMap == null) {
            return new StringBuilder();
        }
        //排序
        Set<String> keySet = paramMap.keySet();
        //noinspection ToArrayCallWithZeroLengthArrayArgument
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        StringBuilder sb = new StringBuilder();
        for (String k : keyArray) {
            if (k.equals(SignConstant.SIGN_SIGN_KEY) || k.equals(SignConstant.SIGN_SECRET_KEY)) {
                continue;
            }
            if (String.valueOf(paramMap.get(k)).trim().length() > 0 && !k.toLowerCase().contains("time")) {
                // 参数值为空，则不参与签名
                sb.append(k).append("=").append(String.valueOf(paramMap.get(k)).trim()).append("&");
            }
        }
        return sb;
    }

    /**
     * <pre>V2获取签名内容</pre>
     *
     * @param bean 对象
     * @return java.lang.StringBuilder
     */
    public static StringBuilder getSignCheckContentV2(Object bean) {
        if (bean == null) {
            return new StringBuilder();
        }

        StringBuilder sb = new StringBuilder();
        //获取所有的字段包括public,private,protected,private

        List<Field> fields = BeanHelper.getFieldList(bean);
        Map<String, Field> fieldMap = new HashMap<>(20);
        Set<String> keySet = new HashSet<>();
        for (Field field : fields) {
            fieldMap.put(field.getName(), field);
            keySet.add(field.getName());
        }
        //noinspection ToArrayCallWithZeroLengthArrayArgument
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        Arrays.sort(keyArray);
        if (fields.size() > 0) {

            for (String s : keyArray) {
                Field f = fieldMap.get(s);
                //获取字段名
                String k = f.getName();
                if (SignUtils.isNotBlank(k.trim())) {
                    String v = BeanHelper.getValue(bean, k);
                    if (k.equals(SignConstant.SIGN_SIGN_KEY) || k.equals(SignConstant.SIGN_SECRET_KEY)) {
                        continue;
                    }
                    if (SignUtils.isNotBlank(v) && v.trim().length() > 0 && !k.toLowerCase().contains("time")) {
                        // 参数值为空，则不参与签名
                        sb.append(k).append("=").append(v.trim()).append("&");
                    }
                }
            }
        }
        return sb;
    }


    /**
     * <pre>获取V2版本签名</pre>
     *
     * @param bean         对象
     * @param clientSecret 验证接口的clientSecret
     * @return java.lang.String
     */
    public static String getSignV2(Object bean, String clientSecret) {

        //加密
        String signType = BeanHelper.getSignType(bean);
        SignType type = null;
        if (SignUtils.isNotBlank(signType)) {
            type = SignType.valueOf(signType);
        }
        if (type == null) {
            type = SignType.MD5;
        }
        StringBuilder sb = getSignCheckContentV2(bean);
        System.out.println("-=========" + sb);
        //暂时不需要个人认证
        sb.append(SignConstant.SIGN_SECRET_KEY + "=").append(clientSecret);
        String signStr = "";

        switch (type) {
            case MD5:
                signStr = SignUtils.md5Hex(sb.toString());
                break;
            case SHA256:
                signStr = SignUtils.sha256Hex(sb.toString());
                break;
            default:
                break;
        }
        return signStr;
    }

    public static void validateParamsV2(Object bean) {
        if (SignUtils.isBlank(BeanHelper.getValue(bean, SignConstant.SIGN_APP_ID_KEY))) {
            throw new RuntimeException("签名验证失败:platformInfoNo不能为空");
        }
        if (SignUtils.isBlank(BeanHelper.getValue(bean, SignConstant.SIGN_NONCE_KEY))) {
            throw new RuntimeException("签名验证失败:NONCE不能为空");
        }
        if (SignUtils.isBlank(BeanHelper.getValue(bean, SignConstant.SIGN_TIMESTAMP_KEY))) {
            throw new RuntimeException("签名验证失败:TIMESTAMP不能为空");
        }
        if (SignUtils.isBlank(BeanHelper.getValue(bean, SignConstant.SIGN_SIGN_TYPE_KEY))) {
            throw new RuntimeException("签名验证失败:SIGN_TYPE不能为空");
        }
        if (SignUtils.isBlank(BeanHelper.getValue(bean, SignConstant.SIGN_SIGN_KEY))) {
            throw new RuntimeException("签名验证失败:SIGN不能为空");
        }
        if (SignType.contains(BeanHelper.getValue(bean, SignConstant.SIGN_SIGN_TYPE_KEY))) {
            throw new IllegalArgumentException(String.format("签名验证失败:SIGN_TYPE必须为:%s,%s", SignType.MD5, SignType.SHA256));
        }


        String timestamp = BeanHelper.getValue(bean, SignConstant.SIGN_TIMESTAMP_KEY);
        long clientTimestamp = Long.parseLong(timestamp);
        //判断时间戳 timestamp=201808091113
        if ((Long.parseLong(SignUtils.getTodayDateTime()) - clientTimestamp) > MAX_EXPIRE) {
            throw new IllegalArgumentException("签名验证失败:TIMESTAMP已过期");
        }
    }

    /**
     * <pre>校验V2版本的签名</pre>
     *
     * @param object       对象
     * @param clientSecret 秘钥
     * @return boolean
     */
    public static boolean validateSignV2(Object object, String clientSecret) {
        try {
            validateParamsV2(object);
            String sign = BeanHelper.getValue(object, SignConstant.SIGN_SIGN_KEY);
            //重新生成签名
            String signNew = getSignV2(object, clientSecret);
            //判断当前签名是否正确
            if (signNew.equals(sign)) {
                return true;
            }
        } catch (Exception e) {
            System.err.printf("validateSign error:{%s}%n", e.getMessage());
            return false;
        }
        return false;
    }


    public enum SignType {
        /**
         * md5
         */
        MD5, SHA256;

        public static boolean contains(String type) {
            for (SignType typeEnum : SignType.values()) {
                if (typeEnum.name().equalsIgnoreCase(type)) {
                    return false;
                }
            }
            return true;
        }
    }

}
