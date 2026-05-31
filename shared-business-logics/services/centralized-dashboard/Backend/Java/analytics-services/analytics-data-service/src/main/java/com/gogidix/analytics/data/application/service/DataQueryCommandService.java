package com.gogidix.analytics.data.application.service;

import com.gogidix.analytics.data.domain.model.DataQuery;
import com.gogidix.analytics.data.domain.port.in.CreateDataQueryCommand;
import com.gogidix.analytics.data.domain.port.out.DataQueryExecutor;
import com.gogidix.analytics.data.domain.repository.DataQueryRepository;
import com.gogidix.shared.audit.service.AuditService;
import com.gogidix.shared.exceptions.NotFoundException;
import com.gogidix.shared.exceptions.ValidationException;
import com.gogidix.shared.security.context.RequestContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class DataQueryCommandService {

    private final DataQueryRepository queryRepository;
    private final DataQueryExecutor queryExecutor;
    private final AuditService auditService;

    @Transactional
    public DataQuery createQuery(CreateDataQueryCommand command) {
        log.info("Creating data query: name={}", command.getQueryName());

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        queryExecutor.validateQuery(command.getQueryDefinition(), command.getQueryType());

        DataQuery query = DataQuery.builder()
            .queryName(command.getQueryName())
            .description(command.getDescription())
            .queryDefinition(command.getQueryDefinition())
            .queryType(command.getQueryType())
            .dataSource(command.getDataSource())
            .parametersSchema(command.getParametersSchema())
            .category(command.getCategory())
            .tags(command.getTags())
            .ownerId(command.getOwnerId())
            .tenantId(tenantId)
            .isPublic(command.getIsPublic())
            .isFavorite(false)
            .executionCount(0)
            .build();

        DataQuery savedQuery = queryRepository.save(query);

        auditService.logEvent(
            "DATA_QUERY_CREATED",
            "DataQuery",
            savedQuery.getId(),
            "Created data query: " + savedQuery.getQueryName()
        );

        log.info("Data query created: id={}, name={}", savedQuery.getId(), savedQuery.getQueryName());
        return savedQuery;
    }

    @Transactional
    public DataQuery updateQuery(String queryId, CreateDataQueryCommand command) {
        log.info("Updating data query: id={}", queryId);

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        DataQuery query = queryRepository.findById(queryId)
            .orElseThrow(() -> new NotFoundException("Query not found: " + queryId));

        if (!query.getTenantId().equals(tenantId)) {
            throw new ValidationException("Access denied: Query belongs to different tenant");
        }

        queryExecutor.validateQuery(command.getQueryDefinition(), command.getQueryType());

        query.setQueryName(command.getQueryName());
        query.setDescription(command.getDescription());
        query.setQueryDefinition(command.getQueryDefinition());
        query.setQueryType(command.getQueryType());
        query.setDataSource(command.getDataSource());
        query.setParametersSchema(command.getParametersSchema());
        query.setCategory(command.getCategory());
        query.setTags(command.getTags());
        query.setIsPublic(command.getIsPublic());

        DataQuery savedQuery = queryRepository.save(query);

        auditService.logEvent(
            "DATA_QUERY_UPDATED",
            "DataQuery",
            savedQuery.getId(),
            "Updated data query: " + savedQuery.getQueryName()
        );

        log.info("Data query updated: id={}", queryId);
        return savedQuery;
    }

    @Transactional
    public void deleteQuery(String queryId) {
        log.info("Deleting data query: id={}", queryId);

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        DataQuery query = queryRepository.findById(queryId)
            .orElseThrow(() -> new NotFoundException("Query not found: " + queryId));

        if (!query.getTenantId().equals(tenantId)) {
            throw new ValidationException("Access denied: Query belongs to different tenant");
        }

        queryRepository.delete(query);

        auditService.logEvent(
            "DATA_QUERY_DELETED",
            "DataQuery",
            queryId,
            "Deleted data query: " + query.getQueryName()
        );

        log.info("Data query deleted: id={}", queryId);
    }

    @Transactional
    public DataQuery toggleFavorite(String queryId) {
        log.info("Toggling favorite: queryId={}", queryId);

        String tenantId = RequestContext.getTenantIdFromThreadLocal();
        if (tenantId == null) {
            throw new ValidationException("Tenant ID not found in request context");
        }

        DataQuery query = queryRepository.findById(queryId)
            .orElseThrow(() -> new NotFoundException("Query not found: " + queryId));

        if (!query.getTenantId().equals(tenantId)) {
            throw new ValidationException("Access denied: Query belongs to different tenant");
        }

        query.setIsFavorite(!query.getIsFavorite());
        DataQuery savedQuery = queryRepository.save(query);

        log.info("Data query favorite toggled: id={}, isFavorite={}", queryId, savedQuery.getIsFavorite());
        return savedQuery;
    }

    @Transactional
    public void recordExecution(String queryId, long executionTimeMs) {
        DataQuery query = queryRepository.findById(queryId).orElse(null);
        if (query != null) {
            query.recordExecution(executionTimeMs);
            queryRepository.save(query);
        }
    }
}
