package com.gogidix.management.executive.financial.application.query;

import com.gogidix.management.executive.financial.application.dto.FinancialDataDto;
import com.gogidix.management.executive.financial.domain.model.FinancialData;
import com.gogidix.management.executive.financial.domain.repository.FinancialDataRepository;
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
 * Query service for FinancialData read operations
 * Handles all read queries following CQRS pattern
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialDataQueryService {

    private final FinancialDataRepository strategyRepository;

    /**
     * Handle get strategy query
     */
    public FinancialDataDto handle(GetFinancialDataQuery query) {
        log.debug("Handling GetFinancialDataQuery for strategy: {}, tenant: {}",
                query.getFinancialDataId(), query.getTenantId());

        FinancialData strategy = strategyRepository
                .findByIdAndTenantIdAndDeletedAtIsNull(query.getFinancialDataId(), query.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("FinancialData not found"));

        return toDto(strategy);
    }

    /**
     * Handle list strategies query
     */
    public Page<FinancialDataDto> handle(ListFinancialDataQuery query) {
        log.debug("Handling ListFinancialDataQuery for tenant: {}", query.getTenantId());

        Pageable pageable = PageRequest.of(
                query.getPage(),
                query.getSize(),
                Sort.by(Sort.Direction.DESC, "updatedAt")
        );

        Page<FinancialData> strategies;

        if (query.getOwnerId() != null && !query.getOwnerId().isBlank()) {
            List<FinancialData> allStrategies = strategyRepository.findByTenantIdAndDeletedAtIsNull(query.getTenantId());
            List<FinancialData> filtered = allStrategies.stream()
                    .filter(s -> query.getOwnerId().equals(s.getOwnerId()))
                    .collect(Collectors.toList());

            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), filtered.size());

            List<FinancialData> pageContent = filtered.subList(start, end);
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
    public List<FinancialDataDto> getAllStrategies(String tenantId) {
        log.debug("Getting all strategies for tenant: {}", tenantId);
        return strategyRepository.findByTenantIdAndDeletedAtIsNull(tenantId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Check if strategy exists
     */
    public boolean exists(String financialDataId, String tenantId) {
        return strategyRepository.existsByIdAndTenantIdAndDeletedAtIsNull(financialDataId, tenantId);
    }

    private FinancialDataDto toDto(FinancialData strategy) {
        return FinancialDataDto.builder()
                .id(strategy.getId())
                .tenantId(strategy.getTenantId())
                .name(strategy.getName())
                .description(strategy.getDescription())
                .ownerId(strategy.getOwnerId())
                .widgets(strategy.getWidgets() == null ? null :
                        strategy.getWidgets().stream()
                                .map(widget -> FinancialDataDto.WidgetDto.builder()
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
