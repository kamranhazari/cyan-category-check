package com.cyanapp.api.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class DomainCheckLogAspect {
    static final Logger LOGGER = LoggerFactory.getLogger(DomainCheckLogAspect.class);

    @Around("execution(* com.cyanapp.api.controller..*(..))")
    public Object logInfos(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = fetchClassName(joinPoint);
        LOGGER.debug(String.format("class : %s , service: %s is running", className, methodName));

        try {
            Object[] methodArgs = joinPoint.getArgs();

            LOGGER.debug("Method arguments: " + Arrays.deepToString(methodArgs));
        } catch (Exception e) {
        }

        Object result = joinPoint.proceed();

        LOGGER.debug(String.format("class : %s , service: %s running was finished", className, methodName));
        LOGGER.debug("Method result: " + result);
        return result;
    }

    private String fetchClassName(JoinPoint joinPoint) {
        String[] split = joinPoint.getSignature().getDeclaringTypeName().split("\\.");
        return split[split.length - 1];
    }
}
