package com.smoothstack.transactionbatch.aspects;

import java.util.Arrays;

import com.smoothstack.transactionbatch.report.ReportsContainer;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
    @Pointcut("within(com.smoothstack.transactionbatch.generator.*)")
    public void generatorsPointcut() {}

    @Pointcut("within(com.smoothstack.transactionbatch.report.*)")
    public void reportsPointcut() {}

    @Pointcut("within(com.smoothstack.transactionbatch.writer..*)" +
        " || within(com.smoothstack.transactionbatch.tasklet..*)")
    public void writingPointcut() {}

    @AfterThrowing(pointcut = "writingPointcut()", throwing = "e")
    public void logAfterThrowingWriter(JoinPoint joinPoint, Throwable e) {
        log.error(
            "Writing exception in {}.{}() with cause = {}", 
            joinPoint.getSignature().getDeclaringTypeName(),
            joinPoint.getSignature().getName(),
            e.getCause() != null ? e.getCause() : "NULL"
        );
    }

    @Before("generatorsPointcut()")
    public void generatorCalled(JoinPoint joinPoint) {
        if (log.isDebugEnabled() && !joinPoint.getSignature().getName().equals("getInstance")) {
            Object[] args = joinPoint.getArgs();
            String str = String.join(
                ",",
                Arrays.copyOf(args, args.length, String[].class)
            );

            log.debug(
                "Generator {} called {}() with {}", 
                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), str);
        }
    }

    @Before("reportsPointcut()")
    public void reportCalled(JoinPoint joinPoint) {
        if (log.isDebugEnabled() && !joinPoint.getSignature().getDeclaringType().equals(ReportsContainer.class)) {
            Object[] args = joinPoint.getArgs();
            String str = String.join(
                ",",
                Arrays.copyOf(args, args.length, String[].class)
            );

            log.debug(
                "Reporter {} called {}() with {}", 
                joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), str);
        }
    }
}
