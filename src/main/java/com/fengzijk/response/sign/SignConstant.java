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


/**
 * 签名常量
 *
 * @author guozhifeng
 * @since 2022/8/28
 */
public class SignConstant {

    /**
     * 客户端ID KEY
     */
    public static final String SIGN_APP_ID_KEY = "appId";

    /**
     * 客户端秘钥 KEY
     */
    public static final String SIGN_SECRET_KEY = "appSecret";

    /**
     * 随机字符串 KEY
     */
    public static final String SIGN_NONCE_KEY = "nonce";
    /**
     * 时间戳 KEY
     */
    public static final String SIGN_TIMESTAMP_KEY = "timestamp";
    /**
     * 签名类型 KEY
     */
    public static final String SIGN_SIGN_TYPE_KEY = "signType";
    /**
     * 签名结果 KEY
     */
    public static final String SIGN_SIGN_KEY = "sign";
}
