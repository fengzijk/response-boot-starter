package com.fengzijk.response;


import java.io.Serializable;



/**
 * 统一返回结果
 *
 * @author : guozhifeng
 *@since : 2024/10/30 17:57
 */


public class ApiResponse<T> implements Serializable {

    private String statusCode;
    private String statusMessage;
    private T data;
    private Long timestamp;

    public ApiResponse(String statusCode, String statusMessage, T data, long timestamp) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.data = data;
        this.timestamp = timestamp;
    }

    /**
     * 返回成功数据
     *
     * @param data 数据
     * @return com.calf.cloud.starter.response.ApiResponse<T>
     * @author : fengzijk
     *@since : 2021/10/4 2:08
     */
    public static <T> ApiResponse<T> success(T data) {
        return success(ResponseStatusEnum.OK.getStatusCode(), ResponseStatusEnum.OK.getStatusMessage(), data);
    }

    public static <T> ApiResponse<T> success(String statusCode, String statusMessage, T data) {

        return new ApiResponse<>(statusCode, statusMessage, data, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> success(String statusCode, String statusMessage) {
        return new ApiResponse<>(statusCode, statusMessage, null, System.currentTimeMillis());
    }


    /**
     * 返回失败数据
     *
     * @param data 数据
     * @return com.calf.cloud.starter.response.ApiResponse<T>
     * @author : fengzijk
     *@since : 2021/10/4 2:11
     */
    public static <T> ApiResponse<T> fail(T data) {
        return fail(ResponseStatusEnum.BAD_REQUEST.getStatusCode(), ResponseStatusEnum.BAD_REQUEST.getStatusMessage(), data);
    }

    /**
     * 返回失败数据
     *
     * @param responseStatusEnum 数据
     * @return com.calf.cloud.starter.response.ApiResponse<T>
     * @author : fengzijk
     *@since : 2021/10/4 2:11
     */
    public static <T> ApiResponse<T> fail(ResponseStatusEnum responseStatusEnum, T data) {
        return fail(responseStatusEnum.getStatusCode(),responseStatusEnum.getStatusMessage(), data);
    }


    /**
     * 返回失败数据
     *
     * @param statusCode        状态码
     * @param statusMessage     状态提示
     * @return com.calf.cloud.starter.response.ApiResponse<T>
     * @author : fengzijk
     *@since : 2021/10/4 2:11
     */
    public static <T> ApiResponse<T> fail(String statusCode, String statusMessage) {
        return fail(statusCode,statusMessage, null);
    }





    /**
     * 返回失败数据
     *状态码
     * @param statusMessage     状态提示
     * @return com.calf.cloud.starter.response.ApiResponse<T>
     * @author : fengzijk
     *@since : 2021/10/4 2:11
     */
    public static <T> ApiResponse<T> fail( T data, String statusMessage) {
        return fail(ResponseStatusEnum.BAD_REQUEST.statusCode,statusMessage, data);
    }



    /**
     * 返回失败数据
     *
     * @return com.calf.cloud.starter.response.ApiResponse<T>
     * @author : fengzijk
     *@since : 2021/10/4 2:11
     */
    public static <T> ApiResponse<T> fail(String statusCode, String statusMessage, T data) {
        return new ApiResponse<>(statusCode, statusMessage, data, System.currentTimeMillis());
    }





    public enum ResponseStatusEnum {

        /**
         * 请求成功
         */
        OK("20000", "请求成功"),

        /**
         * 请求失败
         */
        BAD_REQUEST("20400", "请求失败"),


        /**
         * 请求失败
         */
        NO_HANDLER("40400", "资源不存在"),



        /**
         * 参数错误
         */
        PARAM_ERROR("21000", "参数错误");
        ;


        public String getStatusCode() {
            return statusCode;
        }

        private final String statusCode;
        /**
         * -- GETTER --
         * 获取状态提示消息
         */

        private final String statusMessage;

        ResponseStatusEnum(String statusCode, String statusMessage) {
            this.statusCode = statusCode;
            this.statusMessage = statusMessage;
        }

        public String getStatusMessage() {
            return statusMessage;
        }
    }

}
