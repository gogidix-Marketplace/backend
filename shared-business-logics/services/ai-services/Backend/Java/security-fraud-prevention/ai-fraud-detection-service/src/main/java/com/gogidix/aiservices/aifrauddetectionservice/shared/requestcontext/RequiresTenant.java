package com.gogidix.aiservices.aifrauddetectionservice.shared.requestcontext;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods that require a valid tenant context.
 * <p>
 * When used on a method, the TenantContextAspect will validate that
 * a tenant context is available before the method executes.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequiresTenant {

    /**
     * Optional error message if tenant context is not available.
     */
    String message() default "Tenant context is required for this operation";
}
