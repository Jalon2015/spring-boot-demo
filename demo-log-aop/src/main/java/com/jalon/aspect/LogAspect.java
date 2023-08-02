package com.jalon.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {
    @Pointcut("execution(* com.jalon.controller.*.*(..))")
    public void pointcut(){}

    // 需返回处理方法的返回值，否则前端收到的数据为空
    @Around("pointcut()")
    public Object advice(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("开始");
        Object res = joinPoint.proceed();
        System.out.println("结束");
        return res;
    }

}
