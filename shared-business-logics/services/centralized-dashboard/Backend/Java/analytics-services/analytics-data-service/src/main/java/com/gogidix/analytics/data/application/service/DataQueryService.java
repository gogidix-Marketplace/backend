package com.gogidix.analytics.data.application.service;

import com.gogidix.analytics.data.domain.model.AnalyticsDataset;
import com.gogidix.analytics.data.domain.model.DataExport;
import com.gogidix.analytics.data.domain.model.DataQuery;
import com.gogidix.analytics.data.domain.port.in.ExecuteDataQueryCommand;
import com.gogidix.analytics.data.domain.port.out.DataQueryExecutor;
import com.gogidix.analytics.data.domain.repository.AnalyticsDatasetRepository;
import com.gogidix.analytics.data.domain.repository.DataExportRepository;
import com.gogidix.analytics.data.domain.repository.DataQueryRepository;
import com.gogidix.shared.exceptions.NotFoundException;
import com.gogidix.shared.exceptions.ValidationException;
import com.gogidix.shared.security.context.RequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataQueryService {

    private final DataQueryRepository queryRepository;
    private final DataExportRepository exportRepository;
    private final AnalyticsDatasetRepository datasetRepository;
    private final DataQueryExecutor queryExecutor;

    public Page<DataQuery> getQueries(Pageable pageable) {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        return queryRepository.findByTenantId(tenantId).stream()
            .collect(java.util.stream.Collector.of(
                () -> new java.util.ArrayList<DataQuery>(),
                (list, item) -> list.add(item),
                (list1, list2) -> { list1.addAll(list2); return list1; }
            ))
            .stream()
            .skip(pageable.getOffset())
            .limit(pageable.getPageSize())
            .collect(java.util.stream.Collectors.collectingAndThen(
                java.util.stream.Collectors.toList(),
                list -> new PageImpl<>(list, pageable, queryRepository.findByTenantId(tenantId).size())
            ));
    }

    public DataQuery getQuery(String queryId) {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        DataQuery query = queryRepository.findById(queryId)
            .orElseThrow(() -> new NotFoundException("Query not found: " + queryId));

        if (!query.getTenantId().equals(tenantId) && !query.getIsPublic()) {
            throw new ValidationException("Access denied: Query belongs to different tenant");
        }

        return query;
    }

    public List<DataQuery> searchQueries(String search) {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        return queryRepository.searchByTenantId(tenantId, search);
    }

    public List<DataQuery> getFavoriteQueries() {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        RequestContext ctx = RequestContext.get();
        String userId = ctx != null ? ctx.getSubject() : null;
        if (userId == null) {
            throw new ValidationException("User ID not found in request context");
        }

        return queryRepository.findByTenantIdAndOwnerIdAndIsFavoriteTrue(tenantId, userId);
    }

    public List<DataQuery> getPublicQueries() {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        return queryRepository.findByTenantIdAndIsPublicTrue(tenantId);
    }

    public Map<String, Object> executeQuery(ExecuteDataQueryCommand command) {
        log.info("Executing data query: queryId={}, type={}", command.getQueryId(), command.getQueryType());

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        DataQuery query = null;
        if (command.getQueryId() != null) {
            query = queryRepository.findById(command.getQueryId())
                .orElseThrow(() -> new NotFoundException("Query not found: " + command.getQueryId()));

            if (!query.getTenantId().equals(tenantId) && !query.getIsPublic()) {
                throw new ValidationException("Access denied: Query belongs to different tenant");
            }
        }

        String queryDefinition = command.getQueryDefinition() != null
            ? command.getQueryDefinition()
            : query != null ? query.getQueryDefinition() : null;

        if (queryDefinition == null) {
            throw new ValidationException("Query definition is required");
        }

        long startTime = System.currentTimeMillis();
        Map<String, Object> result = queryExecutor.executeQuery(
            DataQuery.builder()
                .queryDefinition(queryDefinition)
                .queryType(command.getQueryType())
                .build(),
            command.getParameters()
        );
        long executionTime = System.currentTimeMillis() - startTime;

        if (query != null) {
            query.recordExecution(executionTime);
            queryRepository.save(query);
        }

        log.info("Query executed successfully: executionTimeMs={}", executionTime);
        return result;
    }

    public Map<String, Object> getQuerySchema(String queryDefinition) {
        return queryExecutor.getQuerySchema(queryDefinition);
    }

    public Page<DataExport> getExports(Pageable pageable) {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        List<DataExport> exports = exportRepository.findByTenantIdOrderByCreatedAtDesc(tenantId);

        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), exports.size());
        List<DataExport> pagedExports = exports.subList(start, end);

        return new PageImpl<>(pagedExports, pageable, exports.size());
    }

    public DataExport getExport(String exportId) {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        RequestContext ctx = RequestContext.get();
        String userId = ctx != null ? ctx.getSubject() : null;

        DataExport export = exportRepository.findById(exportId)
            .orElseThrow(() -> new NotFoundException("Export not found: " + exportId));

        if (!export.getTenantId().equals(tenantId)) {
            throw new ValidationException("Access denied: Export belongs to different tenant");
        }

        if (userId != null && !userId.equals(export.getRequestedBy())) {
        }

        return export;
    }

    public List<AnalyticsDataset> getDatasets() {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        return datasetRepository.findByTenantIdAndIsActiveTrue(tenantId);
    }

    public AnalyticsDataset getDataset(String datasetId) {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        AnalyticsDataset dataset = datasetRepository.findById(datasetId)
            .orElseThrow(() -> new NotFoundException("Dataset not found: " + datasetId));

        if (!dataset.getTenantId().equals(tenantId) && !dataset.getIsPublic()) {
            throw new ValidationException("Access denied: Dataset belongs to different tenant");
        }

        return dataset;
    }

    public List<AnalyticsDataset> searchDatasets(String search) {
        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        return datasetRepository.searchByTenantId(tenantId, search);
    }
}
