package com.gogidix.management.executive.workflow.application.query;

import com.gogidix.management.executive.workflow.application.dto.WorkflowDto;
import com.gogidix.management.executive.workflow.domain.model.Workflow;
import com.gogidix.management.executive.workflow.domain.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Query service for Workflow read operations
 * Handles all read queries following CQRS pattern
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WorkflowQueryService {

    private final WorkflowRepository strategyRepository;

    /**
     * Handle get strategy query
     */
    public WorkflowDto handle(GetWorkflowQuery query) {
        log.debug("Handling GetWorkflowQuery for strategy: {}, tenant: {}",
                query.getWorkflowId(), query.getTenantId());

        Workflow strategy = strategyRepository
                .findByIdAndTenantIdAndDeletedAtIsNull(query.getWorkflowId(), query.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Workflow not found"));

        return toDto(strategy);
    }

    /**
     * Handle list strategies query
     */
    public Page<WorkflowDto> handle(ListWorkflowQuery query) {
        log.debug("Handling ListWorkflowQuery for tenant: {}", query.getTenantId());

        Pageable pageable = PageRequest.of(
                query.getPage(),
                query.getSize(),
                Sort.by(Sort.Direction.DESC, "updatedAt")
        );

        Page<Workflow> strategies;

        if (query.getOwnerId() != null && !query.getOwnerId().isBlank()) {
            List<Workflow> allStrategies = strategyRepository.findByTenantIdAndDeletedAtIsNull(query.getTenantId());
            List<Workflow> filtered = allStrategies.stream()
                    .filter(s -> query.getOwnerId().equals(s.getOwnerId()))
                    .collect(Collectors.toList());

            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), filtered.size());

            List<Workflow> pageContent = filtered.subList(start, end);
            strategies = new org.springframework.data.domain.PageImpl<>(
                    pageContent,
                    pageable,
                    filtered.size()
            );
        } else {
            strategies = strategyRepository.findByTenantIdAndDeletedAtIsNull(query.getTenantId(), pageable);
        }

        return strategies.map(this::toDto);
    }

    /**
     * Get all strategies for a tenant
     */
    public List<WorkflowDto> getAllStrategies(String tenantId) {
        log.debug("Getting all strategies for tenant: {}", tenantId);
        return strategyRepository.findByTenantIdAndDeletedAtIsNull(tenantId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Check if strategy exists
     */
    public boolean exists(String workflowId, String tenantId) {
        return strategyRepository.existsByIdAndTenantIdAndDeletedAtIsNull(workflowId, tenantId);
    }

    private WorkflowDto toDto(Workflow strategy) {
        return WorkflowDto.builder()
                .id(strategy.getId())
                .tenantId(strategy.getTenantId())
                .name(strategy.getName())
                .description(strategy.getDescription())
                .ownerId(strategy.getOwnerId())
                .widgets(strategy.getWidgets() == null ? null :
                        strategy.getWidgets().stream()
                                .map(widget -> WorkflowDto.WidgetDto.builder()
                                        .id(widget.getId())
                                        .name(widget.getName())
                                        .type(widget.getType())
                                        .position(widget.getPosition())
                                        .row(widget.getRow())
                                        .column(widget.getColumn())
                                        .width(widget.getWidth())
                                        .height(widget.getHeight())
                                        .config(widget.getConfig())
                                        .dataSource(widget.getDataSource())
                                        .build())
                                .collect(Collectors.toList())
                )
                .status(strategy.getStatus())
                .layout(strategy.getLayout())
                .createdAt(strategy.getCreatedAt())
                .updatedAt(strategy.getUpdatedAt())
                .build();
    }
}
