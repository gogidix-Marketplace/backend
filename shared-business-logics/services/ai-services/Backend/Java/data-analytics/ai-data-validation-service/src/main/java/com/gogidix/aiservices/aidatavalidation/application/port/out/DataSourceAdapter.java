package com.gogidix.aiservices.aidatavalidation.application.port.out;

import java.util.List;
import java.util.Map;

/**
 * Adapter port for fetching data from external sources.
 */
public interface DataSourceAdapter {

    /**
     * Fetches data from the specified source.
     */
    List<Map<String, Object>> fetchData(String dataSource);

    /**
     * Fetches data from a specific path in the source.
     */
    List<Map<String, Object>> fetchData(String dataSource, String jsonPath);

    /**
     * Checks if the data source is accessible.
     */
    boolean isAccessible(String dataSource);
}
