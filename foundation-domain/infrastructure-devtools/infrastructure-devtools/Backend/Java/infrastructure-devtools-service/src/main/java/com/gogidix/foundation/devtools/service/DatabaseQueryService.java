package com.gogidix.foundation.devtools.service;

import com.gogidix.foundation.devtools.domain.entity.DatabaseQuery;
import com.gogidix.foundation.devtools.domain.entity.DatabaseQueryExecution;
import com.gogidix.foundation.devtools.domain.repository.DatabaseQueryRepository;
import com.gogidix.foundation.devtools.domain.repository.DatabaseQueryExecutionRepository;
import com.gogidix.foundation.devtools.dto.DatabaseQueryDto;
import com.gogidix.foundation.devtools.dto.DatabaseQueryExecutionDto;
import com.gogidix.foundation.devtools.dto.QueryResult;
import com.gogidix.foundation.devtools.exception.DatabaseQueryException;
import com.gogidix.foundation.devtools.mapper.DatabaseQueryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
 * Service for database query functionality.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DatabaseQueryService {

    private final DatabaseQueryRepository queryRepository;
    private final DatabaseQueryExecutionRepository executionRepository;
    private final DatabaseQueryMapper mapper;
    private final Map<String, JdbcTemplate> jdbcTemplateMap = new HashMap<>();

    @Value("${devtools.database.query-timeout:60000}")
    private int queryTimeout;

    @Value("${devtools.database.max-rows:1000}")
    private int maxRows;

    private final DataSource dataSource;

    /**
     * Create a new saved database query.
     */
    @Transactional
    public DatabaseQueryDto createQuery(DatabaseQueryDto dto) {
        log.info("Creating database query: {}", dto.getName());

        DatabaseQuery entity = mapper.toEntity(dto);
        entity.setUuid(UUID.randomUUID());
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());

        entity = queryRepository.save(entity);
        return mapper.toDto(entity);
    }

    /**
     * Update an existing database query.
     */
    @Transactional
    @CacheEvict(value = "databaseQueries", key = "#uuid")
    public DatabaseQueryDto updateQuery(UUID uuid, DatabaseQueryDto dto) {
        log.info("Updating database query: {}", uuid);

        DatabaseQuery entity = queryRepository.findByUuid(uuid)
                .orElseThrow(() -> new DatabaseQueryException("Query not found: " + uuid));

        mapper.updateEntityFromDto(dto, entity);
        entity.setUpdatedAt(LocalDateTime.now());

        entity = queryRepository.save(entity);
        return mapper.toDto(entity);
    }

    /**
     * Get a database query by UUID.
     */
    @Cacheable(value = "databaseQueries", key = "#uuid")
    @Transactional(readOnly = true)
    public DatabaseQueryDto getQuery(UUID uuid) {
        DatabaseQuery entity = queryRepository.findByUuid(uuid)
                .orElseThrow(() -> new DatabaseQueryException("Query not found: " + uuid));
        return mapper.toDto(entity);
    }

    /**
     * Get all queries for a project.
     */
    @Transactional(readOnly = true)
    public Page<DatabaseQueryDto> getQueriesByProject(String projectId, Pageable pageable) {
        return queryRepository.findByProjectId(projectId, pageable)
                .map(mapper::toDto);
    }

    /**
     * Delete a database query.
     */
    @Transactional
    @CacheEvict(value = "databaseQueries", key = "#uuid")
    public void deleteQuery(UUID uuid) {
        log.info("Deleting database query: {}", uuid);
        queryRepository.findByUuid(uuid).orElseThrow(() -> new DatabaseQueryException("Query not found: " + uuid)); queryRepository.deleteById(queryRepository.findByUuid(uuid).get().getId());
    }

    /**
     * Execute a saved database query.
     */
    @Async
    @Transactional
    public CompletableFuture<QueryResult> executeQuery(Long queryId, Map<String, Object> parameters, String executedBy) {
        DatabaseQuery query = queryRepository.findById(queryId)
                .orElseThrow(() -> new DatabaseQueryException("Query not found: " + queryId));

        return executeDatabaseQuery(query, parameters, executedBy);
    }

    /**
     * Execute an ad-hoc database query.
     */
    @Async
    @Transactional
    public CompletableFuture<QueryResult> executeAdHocQuery(String sql, String databaseName,
                                                             Map<String, Object> parameters, String executedBy) {
        DatabaseQuery adHocQuery = DatabaseQuery.builder()
                .uuid(UUID.randomUUID())
                .name("Ad-hoc Query")
                .query(sql)
                .databaseName(databaseName)
                .maxRows(maxRows)
                .timeoutSeconds(queryTimeout / 1000)
                .build();

        return executeDatabaseQuery(adHocQuery, parameters, executedBy);
    }

    /**
     * Internal method to execute a database query.
     */
    private CompletableFuture<QueryResult> executeDatabaseQuery(DatabaseQuery query,
                                                                  Map<String, Object> parameters,
                                                                  String executedBy) {
        return CompletableFuture.supplyAsync(() -> {
            long startTime = System.currentTimeMillis();
            QueryResult.QueryResultBuilder resultBuilder = QueryResult.builder()
                    .queryUuid(query.getUuid())
                    .queryName(query.getName())
                    .databaseName(query.getDatabaseName());

            DatabaseQueryExecution.DatabaseQueryExecutionBuilder executionBuilder =
                    DatabaseQueryExecution.builder()
                            .uuid(UUID.randomUUID())
                            .queryId(query.getId())
                            .executedBy(executedBy)
                            .executedAt(LocalDateTime.now());

            try {
                log.info("Executing database query: {} on database: {}", query.getName(), query.getDatabaseName());

                JdbcTemplate jdbcTemplate = getJdbcTemplate(query.getDatabaseName());

                // Set query timeout
                int timeout = query.getTimeoutSeconds() != null ? query.getTimeoutSeconds() : queryTimeout / 1000;
                jdbcTemplate.setQueryTimeout(timeout);

                // Determine if it's a query or update
                String sql = query.getQuery().trim().toUpperCase();
                boolean isSelect = sql.startsWith("SELECT") || sql.startsWith("WITH") || sql.startsWith("SHOW");

                List<Map<String, Object>> rows;
                Integer rowsAffected = null;
                Integer rowsReturned = null;

                if (isSelect) {
                    rows = jdbcTemplate.queryForList(query.getQuery());

                    // Limit rows
                    int maxResultRows = query.getMaxRows() != null ? query.getMaxRows() : maxRows;
                    if (rows.size() > maxResultRows) {
                        rows = rows.subList(0, maxResultRows);
                        resultBuilder.truncated(true);
                    }

                    rowsReturned = rows.size();
                    resultBuilder.rows(rows);
                } else {
                    rowsAffected = jdbcTemplate.update(query.getQuery());
                    rows = new ArrayList<>();
                    resultBuilder.rowsAffected(rowsAffected);
                }

                long executionTime = System.currentTimeMillis() - startTime;

                resultBuilder.status("SUCCESS")
                        .executionTime(executionTime)
                        .rowsReturned(rowsReturned);

                executionBuilder.status("SUCCESS")
                        .rowsAffected(rowsAffected)
                        .rowsReturned(rowsReturned)
                        .resultData(serializeResults(rows))
                        .executionTime(executionTime);

            } catch (Exception e) {
                log.error("Database query execution failed", e);
                long executionTime = System.currentTimeMillis() - startTime;

                resultBuilder.status("ERROR")
                        .errorMessage(e.getMessage())
                        .executionTime(executionTime);

                executionBuilder.status("ERROR")
                        .errorMessage(e.getMessage())
                        .executionTime(executionTime);
            }

            QueryResult result = resultBuilder.build();
            DatabaseQueryExecution execution = executionBuilder.build();

            // Save execution
            if (query.getId() != null) {
                executionRepository.save(execution);
            }

            return result;
        });
    }

    /**
     * Get query execution history.
     */
    @Transactional(readOnly = true)
    public List<DatabaseQueryExecutionDto> getExecutionHistory(Long queryId, Pageable pageable) {
        return executionRepository.findByQueryId(queryId, pageable)
                .stream()
                .map(mapper::toExecutionDto)
                .toList();
    }

    /**
     * Get query statistics for a project.
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getQueryStatistics(String projectId) {
        List<DatabaseQuery> queries = queryRepository.findByProjectId(projectId);

        long totalQueries = queries.size();
        long enabledQueries = queries.stream().filter(DatabaseQuery::getEnabled).count();

        Map<String, Long> typeCounts = new HashMap<>();
        for (DatabaseQuery q : queries) {
            if (q.getQueryType() != null) {
                typeCounts.put(q.getQueryType(), typeCounts.getOrDefault(q.getQueryType(), 0L) + 1);
            }
        }

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalQueries", totalQueries);
        stats.put("enabledQueries", enabledQueries);
        stats.put("queryTypes", typeCounts);

        return stats;
    }

    /**
     * Validate a SQL query without executing it.
     */
    public Map<String, Object> validateQuery(String sql, String databaseName) {
        Map<String, Object> result = new HashMap<>();

        try {
            // Basic validation
            if (sql == null || sql.trim().isEmpty()) {
                result.put("valid", false);
                result.put("error", "Query cannot be empty");
                return result;
            }

            // Check for dangerous keywords
            String upperSql = sql.toUpperCase();
            List<String> dangerousKeywords = List.of("DROP", "DELETE", "TRUNCATE", "ALTER", "CREATE");
            List<String> foundDangerous = dangerousKeywords.stream()
                    .filter(upperSql::contains)
                    .toList();

            if (!foundDangerous.isEmpty()) {
                result.put("valid", false);
                result.put("warning", "Query contains potentially dangerous keywords: " + foundDangerous);
                return result;
            }

            // Try to parse the query
            String trimmedUpper = sql.trim().toUpperCase();
            String queryType = trimmedUpper.split("\\s+")[0];
            result.put("valid", true);
            result.put("queryType", queryType);

        } catch (Exception e) {
            result.put("valid", false);
            result.put("error", e.getMessage());
        }

        return result;
    }

    private JdbcTemplate getJdbcTemplate(String databaseName) {
        return jdbcTemplateMap.computeIfAbsent(databaseName, k -> new JdbcTemplate(dataSource));
    }

    private String serializeResults(List<Map<String, Object>> rows) {
        try {
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < Math.min(rows.size(), 100); i++) {
                if (i > 0) sb.append(",");
                sb.append("{");
                Map<String, Object> row = rows.get(i);
                int j = 0;
                for (Map.Entry<String, Object> entry : row.entrySet()) {
                    if (j > 0) sb.append(",");
                    sb.append("\"").append(entry.getKey()).append("\":");
                    Object value = entry.getValue();
                    if (value == null) {
                        sb.append("null");
                    } else if (value instanceof Number) {
                        sb.append(value);
                    } else {
                        sb.append("\"").append(value.toString().replace("\"", "\\\"")).append("\"");
                    }
                    j++;
                }
                sb.append("}");
            }
            sb.append("]");
            return sb.toString();
        } catch (Exception e) {
            return "[]";
        }
    }
}
