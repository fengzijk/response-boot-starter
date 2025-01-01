

package com.fengzijk.response.sign;

import java.io.Serializable;



public class BaseSignDTO implements Serializable {

    
    public String nonce;
    
    private String appId;
    
    private String signType;
    
    private String timestamp;
    
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
