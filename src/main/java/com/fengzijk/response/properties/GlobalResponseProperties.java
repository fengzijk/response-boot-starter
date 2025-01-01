package com.fengzijk.response.properties;

import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(GlobalResponseProperties.PREFIX)
public class GlobalResponseProperties {

    public static final String PREFIX = "api-response";
    private final Boolean enabled = Boolean.TRUE;
    private Boolean onlyParamFirstError = Boolean.TRUE;
    private List<String> ignoreHeaderList;
    private List<String> adviceFilterPackageList = new ArrayList<>();
    private List<String> adviceFilterClassList = new ArrayList<>();
    private RequestLogProperties requestLog;



    public RequestLogProperties getRequestLog() {
        return requestLog;
    }

    public void setRequestLog(RequestLogProperties requestLog) {
        this.requestLog = requestLog;
    }

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



    public static class RequestLogProperties {
        private Boolean enabled = Boolean.FALSE;
        private List<String> requestLogUrlPatternList = new ArrayList<>();
        private List<String> ignoreUrlList = new ArrayList<>();
        private List<String> sensitiveHeadersList = new ArrayList<>();
        private List<String> visibleContentTypeList = new ArrayList<>();
        private Integer maxBodySize = 4096;


        public Integer getMaxBodySize() {
            return maxBodySize;
        }

        public void setMaxBodySize(Integer maxBodySize) {
            this.maxBodySize = maxBodySize;
        }



        public List<String> getRequestLogUrlPatternList() {
            return requestLogUrlPatternList;
        }

        public void setRequestLogUrlPatternList(List<String> requestLogUrlPatternList) {
            this.requestLogUrlPatternList = requestLogUrlPatternList;
        }

        public Boolean getEnabled() {
            return enabled;
        }

        public void setEnabled(Boolean enabled) {
            this.enabled = enabled;
        }

        public List<String> getIgnoreUrlList() {
            return ignoreUrlList;
        }

        public void setIgnoreUrlList(List<String> ignoreUrlList) {
            this.ignoreUrlList = ignoreUrlList;
        }

        public List<String> getSensitiveHeadersList() {
            return sensitiveHeadersList;
        }

        public void setSensitiveHeadersList(List<String> sensitiveHeadersList) {
            this.sensitiveHeadersList = sensitiveHeadersList;
        }

        public List<String> getVisibleContentTypeList() {
            return visibleContentTypeList;
        }

        public void setVisibleContentTypeList(List<String> visibleContentTypeList) {
            this.visibleContentTypeList = visibleContentTypeList;
        }



    }

}
