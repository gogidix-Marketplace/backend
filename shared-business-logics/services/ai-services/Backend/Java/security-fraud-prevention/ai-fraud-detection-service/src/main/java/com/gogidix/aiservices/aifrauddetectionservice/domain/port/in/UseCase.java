package com.gogidix.aiservices.aifrauddetectionservice.domain.port.in;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker interface for use cases in the hexagonal architecture.
 * Input ports represent the application's use cases and define the intent
 * of the application independent of external concerns.
 *
 * This annotation can be used to mark interfaces as use cases.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface UseCase {

    /**
     * Optional description of the use case's purpose.
     */
    String value() default "";

    /**
     * Category of the use case (e.g., "command", "query", "monitoring").
     */
    String category() default "general";
}
