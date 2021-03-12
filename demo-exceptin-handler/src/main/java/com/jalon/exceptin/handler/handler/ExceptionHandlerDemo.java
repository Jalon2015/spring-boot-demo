package com.jalon.exceptin.handler.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionHandlerDemo {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public String handler(Exception e){
        System.out.println("进来了");
        System.out.println(e.getMessage());
        return "haha";
    }
}
