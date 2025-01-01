/*
 *   All rights Reserved, Designed By ZTE-ITS
 *   Copyright:    Copyright(C) 2019-2025
 *   Company       FENGZIJK LTD.
 *   @Author:    fengzijk
 *   @Email: guozhifengvip@gmail.com
 *   @Version    V1.0
 *   @Date:   2022年06月19日 13时33分
 *   Modification       History:
 *   ------------------------------------------------------------------------------------
 *   Date                  Author        Version        Description
 *   -----------------------------------------------------------------------------------
 *  2022-06-19 13:33:40    fengzijk         1.0         Why & What is modified: <修改原因描述>
 *
 *
 */

package com.fengzijk.response.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * 全局返回值与异常处理
 *
 * @author : fengzijk
 * @since : 2021/10/4 0:47
 */
@ConfigurationProperties(GlobalResponseProperties.PREFIX)
public class GlobalResponseProperties {


    /**
     * 定义过滤拦截头部
     */
    public static final String PREFIX = "global-response";
    private final Boolean enabled = Boolean.TRUE;



    /**
     *  参数校验 是否只返回第一个错误
     */
    private   Boolean onlyParamFirstError=Boolean.TRUE;

    /**
     * feign 接口过滤
     */
    private List<String> ignoreHeader;
    /**
     * 统一返回过滤包
     */
    private List<String> adviceFilterPackage = new ArrayList<>();
    /**
     * 统一返回过滤类
     */
    private List<String> adviceFilterClass = new ArrayList<>();

    public Boolean getEnabled() {
        return enabled;
    }

    public List<String> getAdviceFilterPackage() {
        return adviceFilterPackage;
    }

    public void setAdviceFilterPackage(List<String> adviceFilterPackage) {
        this.adviceFilterPackage = adviceFilterPackage;
    }

    public List<String> getAdviceFilterClass() {
        return adviceFilterClass;
    }

    public void setAdviceFilterClass(List<String> adviceFilterClass) {
        this.adviceFilterClass = adviceFilterClass;
    }



    public Boolean getOnlyParamFirstError() {
        return onlyParamFirstError;
    }

    public void setOnlyParamFirstError(Boolean onlyParamFirstError) {
        this.onlyParamFirstError = onlyParamFirstError;
    }


    public List<String> getIgnoreHeader() {
        return ignoreHeader;
    }

    public void setIgnoreHeader(List<String> ignoreHeader) {
        this.ignoreHeader = ignoreHeader;
    }
}

