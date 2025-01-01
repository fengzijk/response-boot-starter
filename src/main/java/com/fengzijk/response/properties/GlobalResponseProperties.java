

package com.fengzijk.response.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;


@ConfigurationProperties(GlobalResponseProperties.PREFIX)
public class GlobalResponseProperties {


    
    public static final String PREFIX = "global-response";
    private final Boolean enabled = Boolean.TRUE;



    
    private   Boolean onlyParamFirstError=Boolean.TRUE;

    
    private List<String> ignoreHeaderList;
    
    private List<String> adviceFilterPackageList = new ArrayList<>();
    
    private List<String> adviceFilterClassList = new ArrayList<>();



    public String getRequestLogUrlPattern() {
        return requestLogUrlPattern;
    }

    public void setRequestLogUrlPattern(String requestLogUrlPattern) {
        this.requestLogUrlPattern = requestLogUrlPattern;
    }

    
    private String  requestLogUrlPattern;

    public Boolean getEnabled() {
        return enabled;
    }

    public List<String> getAdviceFilterPackageList() {
        return adviceFilterPackageList;
    }

    public void setAdviceFilterPackageList(List<String> adviceFilterPackageList) {
        this.adviceFilterPackageList = adviceFilterPackageList;
    }

    public List<String> getAdviceFilterClassList() {
        return adviceFilterClassList;
    }

    public void setAdviceFilterClassList(List<String> adviceFilterClassList) {
        this.adviceFilterClassList = adviceFilterClassList;
    }



    public Boolean getOnlyParamFirstError() {
        return onlyParamFirstError;
    }

    public void setOnlyParamFirstError(Boolean onlyParamFirstError) {
        this.onlyParamFirstError = onlyParamFirstError;
    }


    public List<String> getIgnoreHeaderList() {
        return ignoreHeaderList;
    }

    public void setIgnoreHeaderList(List<String> ignoreHeaderList) {
        this.ignoreHeaderList = ignoreHeaderList;
    }
}

