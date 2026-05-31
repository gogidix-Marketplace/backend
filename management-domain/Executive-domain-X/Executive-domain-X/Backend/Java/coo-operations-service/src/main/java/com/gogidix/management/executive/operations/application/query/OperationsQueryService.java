package com.gogidix.management.executive.operations.application.query;

import com.gogidix.management.executive.operations.application.dto.OperationsDto;
import com.gogidix.management.executive.operations.domain.model.Operations;
import com.gogidix.management.executive.operations.domain.repository.OperationsRepository;
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
 * Query service for Operations read operations
 * Handles all read queries following CQRS pattern
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OperationsQueryService {

    private final OperationsRepository strategyRepository;

    /**
     * Handle get strategy query
     */
    public OperationsDto handle(GetOperationsQuery query) {
        log.debug("Handling GetOperationsQuery for strategy: {}, tenant: {}",
                query.getOperationsId(), query.getTenantId());

        Operations strategy = strategyRepository
                .findByIdAndTenantIdAndDeletedAtIsNull(query.getOperationsId(), query.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Operations not found"));

        return toDto(strategy);
    }

    /**
     * Handle list strategies query
     */
    public Page<OperationsDto> handle(ListOperationsQuery query) {
        log.debug("Handling ListOperationsQuery for tenant: {}", query.getTenantId());

        Pageable pageable = PageRequest.of(
                query.getPage(),
                query.getSize(),
                Sort.by(Sort.Direction.DESC, "updatedAt")
        );

        Page<Operations> strategies;

        if (query.getOwnerId() != null && !query.getOwnerId().isBlank()) {
            List<Operations> allStrategies = strategyRepository.findByTenantIdAndDeletedAtIsNull(query.getTenantId());
            List<Operations> filtered = allStrategies.stream()
                    .filter(s -> query.getOwnerId().equals(s.getOwnerId()))
                    .collect(Collectors.toList());

            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), filtered.size());

            List<Operations> pageContent = filtered.subList(start, end);
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
    public List<OperationsDto> getAllStrategies(String tenantId) {
        log.debug("Getting all strategies for tenant: {}", tenantId);
        return strategyRepository.findByTenantIdAndDeletedAtIsNull(tenantId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Check if strategy exists
     */
    public boolean exists(String operationsId, String tenantId) {
        return strategyRepository.existsByIdAndTenantIdAndDeletedAtIsNull(operationsId, tenantId);
    }

    private OperationsDto toDto(Operations strategy) {
        return OperationsDto.builder()
                .id(strategy.getId())
                .tenantId(strategy.getTenantId())
                .name(strategy.getName())
                .description(strategy.getDescription())
                .ownerId(strategy.getOwnerId())
                .widgets(strategy.getWidgets() == null ? null :
                        strategy.getWidgets().stream()
                                .map(widget -> OperationsDto.WidgetDto.builder()
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
