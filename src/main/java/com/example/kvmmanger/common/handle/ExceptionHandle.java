package com.example.kvmmanger.common.handle;

import com.alibaba.fastjson.JSON;
import com.example.kvmmanger.common.Result;
import com.example.kvmmanger.common.exception.BusinessException;
import com.example.kvmmanger.common.util.RetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


@ControllerAdvice
public class ExceptionHandle {

    private final static Logger logger = LoggerFactory.getLogger(ExceptionHandle.class);

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Result<Object> bindExceptionErrorHandler(MethodArgumentNotValidException exception) {
        logger.error("bindExceptionErrorHandler info:{}", exception.getMessage());
        List<String> errorMsgList = new ArrayList<>();
        BindingResult bindingResult = exception.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMsgList.add(fieldError.getDefaultMessage());
        }
        return RetResponse.error(JSON.toJSONString(errorMsgList));
    }

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result<Object> handle(Exception e) {
        if (e instanceof BusinessException) {
            BusinessException girlException = (BusinessException) e;
            return RetResponse.make(girlException.getCode(), girlException.getMessage());
        } else {
            logger.error("【系统异常】{}", e);
            return RetResponse.error("系统异常");
        }
    }

}
