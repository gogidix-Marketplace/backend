package com.gogidix.aiservices.aifrauddetectionservice.application.query;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CQRS Query Gateway for dispatching queries to their registered handlers.
 * Acts as a dispatcher that routes queries to appropriate handlers with caching support.
 */
@Component
@Slf4j
public class QueryGateway {

    private final Map<Class<?>, QueryHandler<?, ?>> handlers = new ConcurrentHashMap<>();

    /**
     * Registers a query handler for a specific query type.
     *
     * @param queryClass the query class this handler processes
     * @param handler the handler implementation
     * @param <Q> the query type
     * @param <R> the result type
     */
    public <Q extends Query<R>, R> void registerHandler(Class<Q> queryClass, QueryHandler<Q, R> handler) {
        handlers.put(queryClass, handler);
        log.debug("Registered handler for query type: {}", queryClass.getSimpleName());
    }

    /**
     * Dispatches a query to its registered handler with caching support.
     *
     * @param query the query to dispatch
     * @param <Q> the query type
     * @param <R> the result type
     * @return the result of query execution
     * @throws Exception if no handler is registered or execution fails
     */
    @SuppressWarnings("unchecked")
    public <Q extends Query<R>, R> R dispatch(Q query) throws Exception {
        QueryHandler<Q, R> handler = (QueryHandler<Q, R>) handlers.get(query.getClass());

        if (handler == null) {
            throw new IllegalArgumentException("No handler registered for query: " + query.getClass().getSimpleName());
        }

        log.debug("Dispatching query: {}", query.getClass().getSimpleName());
        return handler.handle(query);
    }

    /**
     * Dispatches a query to its registered handler without caching.
     * Use this for queries that should bypass the cache.
     *
     * @param query the query to dispatch
     * @param <Q> the query type
     * @param <R> the result type
     * @return the result of query execution
     * @throws Exception if no handler is registered or execution fails
     */
    @SuppressWarnings("unchecked")
    public <Q extends Query<R>, R> R dispatchNoCache(Q query) throws Exception {
        QueryHandler<Q, R> handler = (QueryHandler<Q, R>) handlers.get(query.getClass());

        if (handler == null) {
            throw new IllegalArgumentException("No handler registered for query: " + query.getClass().getSimpleName());
        }

        log.debug("Dispatching query (no cache): {}", query.getClass().getSimpleName());
        return handler.handle(query);
    }

    /**
     * Checks if a handler is registered for the given query type.
     *
     * @param queryClass the query class to check
     * @return true if a handler is registered, false otherwise
     */
    public boolean hasHandlerFor(Class<?> queryClass) {
        return handlers.containsKey(queryClass);
    }
}
