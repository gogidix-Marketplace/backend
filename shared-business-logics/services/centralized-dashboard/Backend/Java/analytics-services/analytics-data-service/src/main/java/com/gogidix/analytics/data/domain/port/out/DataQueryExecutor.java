package com.gogidix.analytics.data.domain.port.out;

import com.gogidix.analytics.data.domain.model.DataQuery;

import java.util.Map;

/**
 * Output port: Executor for data queries.
 */
public interface DataQueryExecutor {

    Map<String, Object> executeQuery(DataQuery query, Map<String, Object> parameters);

    long executeCountQuery(DataQuery query, Map<String, Object> parameters);

    void validateQuery(String queryDefinition, DataQuery.QueryType queryType);

    Map<String, Object> getQuerySchema(String queryDefinition);
}
