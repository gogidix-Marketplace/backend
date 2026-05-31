package com.gogidix.aiservices.aifrauddetectionservice.application.query;

/**
 * Base marker interface for all CQRS queries.
 * Queries represent intent to retrieve data without modifying system state.
 *
 * @param <R> The type of result this query returns
 */
public interface Query<R> {
}
