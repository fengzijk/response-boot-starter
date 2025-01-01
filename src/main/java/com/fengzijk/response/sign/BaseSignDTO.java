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

import java.io.Serializable;


/**
 * 功能描述
 *
 * @author guozhifeng
 * @since 2022/8/28
 */
public class BaseSignDTO implements Serializable {

    /**
     * 随机字符串
     */
    public String nonce;
    /**
     * 客户端ID
     */
    private String appId;
    /**
     * 签名方式  signType = SHA256|MD5
     */
    private String signType;
    /**
     * 时间戳
     */
    private String timestamp;
    /**
     * 签名
     */
    private String sign;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "BaseSignDTO{" + "appId='" + appId + '\'' + ", signType='" + signType + '\'' + ", timestamp='" + timestamp + '\'' + ", nonce='" + nonce + '\'' + ", sign='" + sign + '\'' + '}';
    }
}
