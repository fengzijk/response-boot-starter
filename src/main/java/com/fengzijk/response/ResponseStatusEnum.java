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

package com.fengzijk.response;


/**
 * <pre>返回值</pre>
 *
 * @author guozhifeng
 * @since 2022/8/28
 */

public enum ResponseStatusEnum {

    /**
     * 请求成功
     */
    OK(20000, "请求成功"),

    /**
     * 请求失败
     */
    BAD_REQUEST(24000, "请求失败"),

    /**
     * 请求失败
     */
    NO_HANDLER(24040, "资源不存在"),


    /**
     * 服务器内部错误
     */
    INTERNAL_ERROR(25000, "服务器内部错误"),

    /**
     * 参数错误
     */
    PARAM_ERROR(21000, "参数错误");

    private Integer code;
    private String msg;

    ResponseStatusEnum(Integer code, String name) {
        this.code = code;
        this.msg = name;
    }

    public static String getMsgByCode(Integer code) {
        if (code != null) {
            for (ResponseStatusEnum e : values()) {
                if (e.getCode().equals(code)) {
                    return e.getMsg();
                }
            }
        }
        return null;
    }

    public static ResponseStatusEnum getByCode(Integer code) {
        if (code != null) {
            for (ResponseStatusEnum e : values()) {
                if (e.getCode().equals(code)) {
                    return e;
                }
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
