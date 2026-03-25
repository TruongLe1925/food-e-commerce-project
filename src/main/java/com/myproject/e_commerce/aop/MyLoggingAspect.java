package com.myproject.e_commerce.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class MyLoggingAspect {
    @Around("execution(* *..service..*.*(..))")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();
        long executionTime = end - start;
        log.info("Execution time: {} ms", executionTime);
        return result;
    }
    @Before("execution(* * ..service..*.*(..))")
    public void logBefore(JoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();
        log.info("Before method: {}", methodName);
    }
    @After("execution(* *..service.OrderService.OrderService.addToOrder(..))")
    public void logAfter(JoinPoint joinPoint) throws Throwable {
        log.info("đã thực hiện thêm vào order thành công qua: " + joinPoint.getSignature().getName());
    }
}
