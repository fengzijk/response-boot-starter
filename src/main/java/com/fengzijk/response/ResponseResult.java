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





import java.io.Serializable;

/**
 * <pre>统一返回结果</pre>
 *
 * @author fengzijk
 * @date : 2021/10/4 1:01
 */


public class ResponseResult<T> implements Serializable {


    private int code;
    private String msg;
    private T data;
    private Long timestamp = System.currentTimeMillis();
    private boolean success;

    /**
     * 返回成功数据
     *
     * @param data 数据
     * @return com.calf.cloud.starter.response.ResponseResult<T>
     * @author : fengzijk
     * @date : 2021/10/4 2:08
     */
    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(ResponseStatusEnum.OK.getCode());
        result.setMsg(ResponseStatusEnum.OK.getMsg());
        result.setData(data);
        result.setSuccess(result.code==200);
        return result;
    }

    /**
     * 返回失败数据
     *
     * @param data 数据
     * @return com.calf.cloud.starter.response.ResponseResult<T>
     * @author : fengzijk
     * @date : 2021/10/4 2:11
     */
    public static <T> ResponseResult<T> fail(T data, ResponseStatusEnum responseStatusEnum) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(responseStatusEnum.getCode());
        result.setMsg(responseStatusEnum.getMsg());
        result.setData(data);
        result.setSuccess(false);
        return result;
    }

    /**
     * 返回失败数据
     *
     * @param responseStatusEnum 数据
     * @return com.calf.cloud.starter.response.ResponseResult<T>
     * @author : fengzijk
     * @date : 2021/10/4 2:11
     */
    public static <T> ResponseResult<T> fail(ResponseStatusEnum responseStatusEnum) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(responseStatusEnum.getCode());
        result.setSuccess(false);
        result.setMsg(responseStatusEnum.getMsg());
        return result;
    }

    /**
     * 返回失败数据
     *
     * @return com.calf.cloud.starter.response.ResponseResult<T>
     * @author : fengzijk
     * @date : 2021/10/4 2:11
     */
    public static <T> ResponseResult<T> fail(int code, String msg) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(code);
        result.setSuccess(false);
        result.setMsg(msg);
        return result;
    }

    /**
     * 返回失败数据
     *
     * @param data 数据
     * @return com.calf.cloud.starter.response.ResponseResult<T>
     * @author : fengzijk
     * @date : 2021/10/4 2:11
     */
    public static <T> ResponseResult<T> fail(T data) {
        ResponseResult<T> result = new ResponseResult<>();
        result.setCode(ResponseStatusEnum.BAD_REQUEST.getCode());
        result.setMsg(ResponseStatusEnum.BAD_REQUEST.getMsg());
        result.setSuccess(false);
        result.setData(data);
        return result;
    }

    /**
     * 根据Boolean值动态返回true或false
     *
     * @param result 返回结果
     * @return com.calf.cloud.starter.response.ResponseResult<T>
     * @author : fengzijk
     * @date : 2021/10/4 2:13
     */
    public static <T> ResponseResult<T> result(T result) {
        if (result instanceof Boolean && (Boolean) result) {
            return success(result);
        }
        return fail(result);
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "ResponseResult{" + "code=" + code + ", msg='" + msg + '\'' + ", data=" + data + ", timestamp=" + timestamp + ", success=" + success + '}';
    }
}
