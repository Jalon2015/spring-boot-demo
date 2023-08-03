package com.jalon.exceptin.handler;

import com.jalon.exceptin.exception.JsonException;
import com.jalon.exceptin.response.ApiResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JsonException.class)
    @ResponseBody
    public ApiResponse handler(JsonException e){
        System.out.println(e.getMessage());

        return new ApiResponse(e.getCode(),e.getErrorMsg());
    }
}
