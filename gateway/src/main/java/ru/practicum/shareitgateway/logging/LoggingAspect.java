package ru.practicum.shareitgateway.logging;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

    @Pointcut("@within(ru.practicum.shareit.logging.Logging) && execution(public * *(..))")
    private void allMethodsInClass() {
    }

    @Before(value = "allMethodsInClass()")
    public void logBefore(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        log.info("==> {}() - {}", methodName, Arrays.toString(args));
    }

    @AfterReturning(value = "allMethodsInClass()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        log.info("<== {}() - {}", methodName, result);
    }
}
