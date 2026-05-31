package com.gogidix.aiservices.aifrauddetectionservice.shared.requestcontext;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * Aspect for logging tenant context information during method execution.
 * <p>
 * This aspect automatically logs the tenant context before and after
 * method execution for methods in the service, repository, and controller packages.
 */
@Slf4j
@Aspect
@Component
public class TenantContextAspect {

    /**
     * Pointcut for all methods in the service layer.
     */
    @Pointcut("execution(* com.gogidix.aiservices.aifrauddetectionservice.domain..*.*(..)) || " +
              "execution(* com.gogidix.aiservices.aifrauddetectionservice.application..*.*(..)) || " +
              "execution(* com.gogidix.aiservices.aifrauddetectionservice.infrastructure..*.*(..))")
    public void applicationLayerPointcut() {
        // Pointcut definition
    }

    /**
     * Pointcut for all controller methods.
     */
    @Pointcut("execution(* com.gogidix.aiservices.aifrauddetectionservice.interfaces..*.*(..))")
    public void controllerLayerPointcut() {
        // Pointcut definition
    }

    /**
     * Around advice for application layer methods - logs tenant context.
     *
     * @param joinPoint the join point
     * @return the result of the method execution
     * @throws Throwable if the method execution fails
     */
    @Around("applicationLayerPointcut()")
    public Object logTenantContextAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        if (TenantContextHolder.hasContext()) {
            String tenantId = TenantContextHolder.getTenantId();
            String correlationId = TenantContextHolder.getCorrelationId();
            String userId = TenantContextHolder.getUserId();

            log.trace("[Tenant: {}] [User: {}] [Correlation: {}] Entering {}.{}",
                    tenantId, userId, correlationId, className, methodName);

            long startTime = System.currentTimeMillis();
            try {
                Object result = joinPoint.proceed();
                long duration = System.currentTimeMillis() - startTime;

                log.trace("[Tenant: {}] [User: {}] [Correlation: {}] Exiting {}.{} ({}ms)",
                        tenantId, userId, correlationId, className, methodName, duration);

                return result;
            } catch (Exception e) {
                long duration = System.currentTimeMillis() - startTime;
                log.error("[Tenant: {}] [User: {}] [Correlation: {}] Error in {}.{} ({}ms): {}",
                        tenantId, userId, correlationId, className, methodName, duration, e.getMessage());
                throw e;
            }
        } else {
            log.trace("No tenant context available for {}.{}", className, methodName);
            return joinPoint.proceed();
        }
    }

    /**
     * Around advice for controller methods - logs request details.
     *
     * @param joinPoint the join point
     * @return the result of the method execution
     * @throws Throwable if the method execution fails
     */
    @Around("controllerLayerPointcut()")
    public Object logControllerRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        if (TenantContextHolder.hasContext()) {
            String tenantId = TenantContextHolder.getTenantId();
            String correlationId = TenantContextHolder.getCorrelationId();
            String userId = TenantContextHolder.getUserId();

            log.debug("[Controller] [Tenant: {}] [User: {}] [Correlation: {}] {}.{}",
                    tenantId, userId, correlationId, className, methodName);

            long startTime = System.currentTimeMillis();
            try {
                Object result = joinPoint.proceed();
                long duration = System.currentTimeMillis() - startTime;

                log.debug("[Controller] [Tenant: {}] [User: {}] [Correlation: {}] {}.{} completed in {}ms",
                        tenantId, userId, correlationId, className, methodName, duration);

                return result;
            } catch (Exception e) {
                long duration = System.currentTimeMillis() - startTime;
                log.warn("[Controller] [Tenant: {}] [User: {}] [Correlation: {}] {}.{} failed after {}ms: {}",
                        tenantId, userId, correlationId, className, methodName, duration, e.getMessage());
                throw e;
            }
        } else {
            log.warn("[Controller] No tenant context for {}.{}", className, methodName);
            return joinPoint.proceed();
        }
    }

    /**
     * Pointcut for methods that require tenant context validation.
     */
    @Pointcut("@annotation(com.gogidix.aiservices.aifrauddetectionservice.shared.requestcontext.RequiresTenant)")
    public void requiresTenantPointcut() {
        // Pointcut definition
    }

    /**
     * Around advice for methods requiring tenant context.
     * Throws an exception if no tenant context is available.
     *
     * @param joinPoint the join point
     * @return the result of the method execution
     * @throws Throwable if the method execution fails or no context is available
     */
    @Around("requiresTenantPointcut()")
    public Object validateTenantContext(ProceedingJoinPoint joinPoint) throws Throwable {
        if (!TenantContextHolder.hasContext()) {
            throw new IllegalStateException("Tenant context is required but not available");
        }

        TenantContext context = TenantContextHolder.getContext();
        if (!context.isValid()) {
            throw new IllegalStateException("Invalid tenant context: tenant ID is missing");
        }

        return joinPoint.proceed();
    }
}
