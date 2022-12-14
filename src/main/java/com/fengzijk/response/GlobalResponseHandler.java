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


import com.fengzijk.response.properties.GlobalResponseProperties;
import com.fengzijk.response.annotation.IgnoreGlobalResponse;
import com.fengzijk.response.exception.BizException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <pre>全局异常处理</pre>
 *
 * @author : fengzijk
 * @date : 2021/10/3 19:15
 */

@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {
    private final static Logger log = LoggerFactory.getLogger(GlobalResponseHandler.class);

    @Autowired
    private GlobalResponseProperties globalResponseProperties;


    /**
     * 拦截MethodArgumentNotValidException异常，针对body参数的表单注解（如：@NotEmpty）校验拦截
     *
     * @param e 错误信息
     * @return com.calf.cloud.starter.response.ResponseResult<?>
     * @author : fengzijk
     * @date : 2021/10/4 2:21
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseResult<?> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.warn("MethodArgumentNotValidException,message={}", e.getMessage());

        // 实体参数注解 ，只返回第一个提示
        List<ObjectError> objectErrors = e.getBindingResult().getAllErrors();
        if (!CollectionUtils.isEmpty(objectErrors) && objectErrors.size() > 0) {
            return ResponseResult.fail(objectErrors.get(0).getDefaultMessage());
        }

        return ResponseResult.fail(ResponseStatusEnum.BAD_REQUEST);
    }


    /**
     * 拦截BindException异常，针对form参数的表单注解（如：@NotEmpty）校验拦截
     *
     * @param e 错误信息
     * @return com.calf.cloud.starter.response.ResponseResult<?>
     * @author : fengzijk
     * @date : 2021/10/4 2:22
     */
    @ExceptionHandler(value = BindException.class)
    public ResponseResult<?> bindExceptionHandler(BindException e) {
        log.warn("BindException,message={}", e.getMessage());


        List<String> list = e.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage).collect(Collectors.toList());
       // 是否只返回第一个错误信息
        if (globalResponseProperties.getOnlyParamFirstError()) {
            return ResponseResult.fail(list.get(0), ResponseStatusEnum.PARAM_ERROR);
        }
        return ResponseResult.fail(list, ResponseStatusEnum.PARAM_ERROR);
    }


    /**
     * <pre>service层校验注解错误</pre>
     *
     * @param e 错误信息
     * @return com.fengzijk.response.ResponseResult<?>
     * @author : guozhifeng
     * @date : 2022/9/17 22:50
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseResult<?> constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.warn("ConstraintViolationException,message={}", e.getMessage());
        List<String> list = e.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.toList());
        // 是否只返回第一个错误信息
        if (globalResponseProperties.getOnlyParamFirstError()) {
            return ResponseResult.fail(list.get(0), ResponseStatusEnum.PARAM_ERROR);
        }
        return ResponseResult.fail(list, ResponseStatusEnum.PARAM_ERROR);
    }


    /**
     * 404异常处理 拦截NoHandlerFoundException异常，针对form参数的表单注解（如：@NotEmpty）校验拦截
     *
     * @param e 错误信息
     * @return java.lang.Object
     * @author : fengzijk
     * @date : 2021/10/4 4:04
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Object notFountHandler(NoHandlerFoundException e) {
        return ResponseResult.fail(e.getMessage(), ResponseStatusEnum.NO_HANDLER);
    }

    /**
     * 拦截ValidationException异常
     *
     * @param e 错误信息
     * @return com.calf.cloud.starter.response.ResponseResult<?>
     * @author : fengzijk
     * @date : 2021/10/4 2:22
     */
    @ExceptionHandler(value = ValidationException.class)
    public ResponseResult<?> validationExceptionHandler(ValidationException e) {
        log.warn("ValidationException,message={}", e.getMessage());
        return ResponseResult.fail(e.getMessage());
    }


    /**
     * HTTP方法异常调用
     *
     * @param e 错误信息
     * @return com.calf.cloud.starter.response.ResponseResult<?>
     * @author : fengzijk
     * @date : 2021/10/4 2:20
     */
    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class, HttpMediaTypeNotSupportedException.class})
    public ResponseResult<?> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
        log.warn("方法调用方式异常,message={}", e.getMessage());
        return ResponseResult.fail("方法调用方式异常，Get、Post请求不匹配，或Form、Body参数不匹配");
    }


    /**
     * 拦截MissingServletRequestParameterException异常
     *
     * @param e 错误信息
     * @return com.calf.cloud.starter.response.ResponseResult<?>
     * @author : fengzijk
     * @date : 2021/10/4 2:23
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseResult<?> missingServletRequestParameterExceptionHandler(MissingServletRequestParameterException e) {
        log.warn("MissingServletRequestParameterException,message={}", e.getMessage());
        return ResponseResult.fail("缺少参数");

    }


    /**
     * 拦截自定义的BusinessException异常
     *
     * @param e 错误信息
     * @return com.calf.cloud.starter.response.ResponseResult<?>
     * @author : fengzijk
     * @date : 2021/10/4 2:20
     */
    @ExceptionHandler(value = BizException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseResult<?> daoExceptionHandler(BizException e) {
        log.error("DaoException,message={}", e.getMessage());
        return ResponseResult.fail(e.getCode(), e.getMessage());

    }


    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> aClass) {
        return this.filter(methodParameter);
    }

    @Override
    public Object beforeBodyWrite(Object obj, MethodParameter methodParameter, MediaType mediaType, Class<? extends HttpMessageConverter<?>> aClass, ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse) {

        if (serverHttpRequest.getHeaders().containsKey(globalResponseProperties.getFeignHeader())) {
            return obj;
        }

        if (obj instanceof Boolean) {
            return ResponseResult.result(obj);
        }

        //当 obj 返回类型为ResultMsg(统一封装返回对象),则直接返回
        if (obj instanceof ResponseResult) {
            return obj;
        }


        return ResponseResult.success(obj);
    }


    /**
     * 自定义规则拦截器
     * 过滤: 1.检查过滤包路径
     * 2.检查类过滤列表
     * 3.检查忽略注解是否存在于类上
     * 4.检查注解是否存在于方法上
     *
     * @param methodParameter 方法参数
     * @return java.lang.Boolean
     * @author : fengzijk
     * @date : 2021/10/4 0:30
     */
    private Boolean filter(MethodParameter methodParameter) {
        Class<?> declaringClass = methodParameter.getDeclaringClass();
        //检查过滤包路径
        long count = globalResponseProperties.getAdviceFilterPackage().stream().filter(a -> declaringClass.getName().contains(a)).count();
        if (count > 0) {
            return false;
        }
        //检查<类>过滤列表
        if (globalResponseProperties.getAdviceFilterClass().contains(declaringClass.getName())) {
            return false;
        }
        //检查忽略注解是否存在于类上
        if (methodParameter.getDeclaringClass().isAnnotationPresent(IgnoreGlobalResponse.class)) {
            return false;
        }
        //检查注解是否存在于方法上
        return !Objects.nonNull(methodParameter.getMethod()) || !methodParameter.getMethod().isAnnotationPresent(IgnoreGlobalResponse.class);
    }

}
