package com.jalon.controller;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogController {

    Logger logger = LoggerFactory.getLogger(LogController.class);
    @GetMapping("/test")
    public String test(){
        logger.info("test");
//        LoggerContext iLoggerFactory = (LoggerContext) LoggerFactory.getILoggerFactory();
//        StatusPrinter.print(iLoggerFactory);
        return "test";
    }
}
