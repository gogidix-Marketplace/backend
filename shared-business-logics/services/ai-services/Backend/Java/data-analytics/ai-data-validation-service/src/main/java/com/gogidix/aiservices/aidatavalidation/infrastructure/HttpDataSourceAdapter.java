package com.gogidix.aiservices.aidatavalidation.infrastructure;

import com.gogidix.aiservices.aidatavalidation.application.port.out.DataSourceAdapter;
import com.gogidix.aiservices.aidatavalidation.application.port.out.DataSourceNotFoundException;
import com.gogidix.aiservices.aidatavalidation.application.port.out.DataSourceAdapterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * HTTP-based implementation of DataSourceAdapter.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class HttpDataSourceAdapter implements DataSourceAdapter {

    private final RestTemplate restTemplate;

    @Override
    public List<Map<String, Object>> fetchData(String dataSource) {
        try {
            log.debug("Fetching data from source: {}", dataSource);

            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(dataSource, Map.class);

            if (response == null) {
                return List.of();
            }

            // Check if response has a data wrapper
            if (response.containsKey("data")) {
                return (List<Map<String, Object>>) response.get("data");
            }

            // If response is a list
            if (response.containsKey("results")) {
                return (List<Map<String, Object>>) response.get("results");
            }

            // Return as single item list
            return List.of(response);

        } catch (Exception e) {
            log.error("Error fetching data from source: {}", dataSource, e);
            throw new DataSourceNotFoundException("Data source not found: " + dataSource);
        }
    }

    @Override
    public List<Map<String, Object>> fetchData(String dataSource, String jsonPath) {
        // Simple JSONPath implementation for common cases
        List<Map<String, Object>> data = fetchData(dataSource);

        if (jsonPath == null || jsonPath.equals("$") || jsonPath.isEmpty()) {
            return data;
        }

        // Handle nested paths like $.response.results
        String[] parts = jsonPath.replace("$.", "").split("\\.");

        List<Map<String, Object>> result = data;
        for (String part : parts) {
            List<Map<String, Object>> tempResult = new ArrayList<>();
            for (Map<String, Object> item : result) {
                if (item.containsKey(part)) {
                    Object value = item.get(part);
                    if (value instanceof List) {
                        tempResult.addAll((List<Map<String, Object>>) value);
                    }
                }
            }
            result = tempResult;
        }

        return result;
    }

    @Override
    public boolean isAccessible(String dataSource) {
        try {
            restTemplate.headForHeaders(dataSource);
            return true;
        } catch (Exception e) {
            log.warn("Data source not accessible: {}", dataSource);
            return false;
        }
    }
}
