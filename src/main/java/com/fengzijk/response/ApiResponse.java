package com.fengzijk.response;


import java.io.Serializable;




@SuppressWarnings("All")
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

    
    public static <T> ApiResponse<T> success(T data) {
        return success(ResponseStatusEnum.OK.getStatusCode(), ResponseStatusEnum.OK.getStatusMessage(), data);
    }

    public static <T> ApiResponse<T> success(String statusCode, String statusMessage, T data) {

        return new ApiResponse<>(statusCode, statusMessage, data, System.currentTimeMillis());
    }

    public static <T> ApiResponse<T> success(String statusCode, String statusMessage) {
        return new ApiResponse<>(statusCode, statusMessage, null, System.currentTimeMillis());
    }


    
    public static <T> ApiResponse<T> fail(T data) {
        return fail(ResponseStatusEnum.BAD_REQUEST.getStatusCode(), ResponseStatusEnum.BAD_REQUEST.getStatusMessage(), data);
    }

    
    public static <T> ApiResponse<T> fail(ResponseStatusEnum responseStatusEnum, T data) {
        return fail(responseStatusEnum.getStatusCode(), responseStatusEnum.getStatusMessage(), data);
    }


    
    public static <T> ApiResponse<T> fail(String statusCode, String statusMessage) {
        return fail(statusCode, statusMessage, null);
    }



    
    public static <T> ApiResponse<T> fail(T data, String statusMessage) {
        return fail(ResponseStatusEnum.BAD_REQUEST.statusCode, statusMessage, data);
    }



    
    public static <T> ApiResponse<T> fail(String statusCode, String statusMessage, T data) {
        return new ApiResponse<>(statusCode, statusMessage, data, System.currentTimeMillis());
    }



    public enum ResponseStatusEnum {

        
        OK("20000", "请求成功"),

        
        BAD_REQUEST("20400", "请求失败"),


        
        NO_HANDLER("40400", "资源不存在"),



        
        PARAM_ERROR("21000", "参数错误");;


        public String getStatusCode() {
            return statusCode;
        }

        private final String statusCode;
        

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
