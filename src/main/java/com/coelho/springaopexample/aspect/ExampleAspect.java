package com.coelho.springaopexample.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ExampleAspect {

    // executado antes e depois do método com a annotation LogExecutionTime
    @Around("@annotation(LogExecutionTime)")
    public Object audit(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        log.info("{} executed in {} ms", joinPoint.getSignature(), executionTime);
        return proceed;
    }

    @Pointcut("within(@org.springframework.stereotype.Repository *)")
    public void repositoryClassMethods() {
    }

    @Around("repositoryClassMethods()")
    public Object measureMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();

        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;

        log.info("{} executed in {} ms", joinPoint.getSignature(), executionTime);
        return proceed;
    }

    @Pointcut("@annotation(Audit)")
    public void auditCreationMethods() { }

    @AfterReturning(value = "auditCreationMethods()", returning = "entity")
    public void auditMethodCall(JoinPoint joinPoint, Object entity) throws Throwable {
        log.info("SEND OBJECT TO AUDIT");

        log.info("ENTITY {}", entity);

        log.info("JP SIGNATURE {}", joinPoint.getSignature().getName());
    }

    @AfterThrowing("@annotation(Audit)")
    public void auditException(JoinPoint joinPoint) {
        log.info("JP SIGNATURE {}", joinPoint.getSignature());

        log.info("JP ARGS {}", joinPoint.getArgs());
    }

}
