package com.gogidix.management.executive.strategy.application.query;

import com.gogidix.management.executive.strategy.application.dto.StrategyDto;
import com.gogidix.management.executive.strategy.domain.model.Strategy;
import com.gogidix.management.executive.strategy.domain.repository.StrategyRepository;
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
 * Query service for Strategy read operations
 * Handles all read queries following CQRS pattern
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StrategyQueryService {

    private final StrategyRepository strategyRepository;

    /**
     * Handle get strategy query
     */
    public StrategyDto handle(GetStrategyQuery query) {
        log.debug("Handling GetStrategyQuery for strategy: {}, tenant: {}",
                query.getStrategyId(), query.getTenantId());

        Strategy strategy = strategyRepository
                .findByIdAndTenantIdAndDeletedAtIsNull(query.getStrategyId(), query.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Strategy not found"));

        return toDto(strategy);
    }

    /**
     * Handle list strategies query
     */
    public Page<StrategyDto> handle(ListStrategiesQuery query) {
        log.debug("Handling ListStrategiesQuery for tenant: {}", query.getTenantId());

        Pageable pageable = PageRequest.of(
                query.getPage(),
                query.getSize(),
                Sort.by(Sort.Direction.DESC, "updatedAt")
        );

        Page<Strategy> strategies;

        if (query.getOwnerId() != null && !query.getOwnerId().isBlank()) {
            List<Strategy> allStrategies = strategyRepository.findByTenantIdAndDeletedAtIsNull(query.getTenantId());
            List<Strategy> filtered = allStrategies.stream()
                    .filter(s -> query.getOwnerId().equals(s.getOwnerId()))
                    .collect(Collectors.toList());

            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), filtered.size());

            List<Strategy> pageContent = filtered.subList(start, end);
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
    public List<StrategyDto> getAllStrategies(String tenantId) {
        log.debug("Getting all strategies for tenant: {}", tenantId);
        return strategyRepository.findByTenantIdAndDeletedAtIsNull(tenantId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Check if strategy exists
     */
    public boolean exists(String strategyId, String tenantId) {
        return strategyRepository.existsByIdAndTenantIdAndDeletedAtIsNull(strategyId, tenantId);
    }

    private StrategyDto toDto(Strategy strategy) {
        return StrategyDto.builder()
                .id(strategy.getId())
                .tenantId(strategy.getTenantId())
                .name(strategy.getName())
                .description(strategy.getDescription())
                .ownerId(strategy.getOwnerId())
                .widgets(strategy.getWidgets() == null ? null :
                        strategy.getWidgets().stream()
                                .map(widget -> StrategyDto.WidgetDto.builder()
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
