package dev.williamnogueira.ecommerce.infrastructure.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void controllerClassMethods() {}

    @Around("controllerClassMethods()")
    public Object logControllerCalls(ProceedingJoinPoint joinPoint) throws Throwable {
        var className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        var methodName = joinPoint.getSignature().getName();
        var args = joinPoint.getArgs();

        log.trace("Entering {}.{} with arguments: {}", className, methodName, Arrays.toString(args));

        var start = System.currentTimeMillis();
        var result = joinPoint.proceed();
        var duration = System.currentTimeMillis() - start;

        log.trace("Exiting {}.{} - Execution time: {} ms", className, methodName, duration);
        return result;
    }
}
