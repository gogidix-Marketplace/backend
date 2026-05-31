package com.gogidix.aiservices.aifrauddetectionservice.application.query;

/**
 * Functional interface for handling CQRS queries.
 * Implementations define the business logic for processing specific query types.
 *
 * @param <Q> The type of query this handler processes
 * @param <R> The type of result returned from query execution
 */
@FunctionalInterface
public interface QueryHandler<Q extends Query<R>, R> {

    /**
     * Handles the given query and returns a result.
     *
     * @param query the query to handle
     * @return the result of query execution
     * @throws Exception if query processing fails
     */
    R handle(Q query) throws Exception;
}
