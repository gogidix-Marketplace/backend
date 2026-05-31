package com.gogidix.aiservices.aifrauddetectionservice.domain.aggregate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker annotation for Domain Aggregates in DDD (Domain-Driven Design).
 * Aggregates represent a cluster of domain objects that can be treated as a single unit.
 *
 * The aggregate root is the only entry point for modifications to the aggregate.
 * All invariants and consistency rules are enforced within the aggregate boundary.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Aggregate {

    /**
     * Optional description of the aggregate's purpose.
     */
    String value() default "";

    /**
     * The type/name of the aggregate for identification purposes.
     */
    String type() default "";
}
