package com.gogidix.management.executive.technology.application.query;

import com.gogidix.management.executive.technology.application.dto.TechnologyDto;
import com.gogidix.management.executive.technology.domain.model.Technology;
import com.gogidix.management.executive.technology.domain.repository.TechnologyRepository;
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
 * Query service for Technology read operations
 * Handles all read queries following CQRS pattern
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TechnologyQueryService {

    private final TechnologyRepository strategyRepository;

    /**
     * Handle get strategy query
     */
    public TechnologyDto handle(GetTechnologyQuery query) {
        log.debug("Handling GetTechnologyQuery for strategy: {}, tenant: {}",
                query.getTechnologyId(), query.getTenantId());

        Technology strategy = strategyRepository
                .findByIdAndTenantIdAndDeletedAtIsNull(query.getTechnologyId(), query.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Technology not found"));

        return toDto(strategy);
    }

    /**
     * Handle list strategies query
     */
    public Page<TechnologyDto> handle(ListTechnologyQuery query) {
        log.debug("Handling ListTechnologyQuery for tenant: {}", query.getTenantId());

        Pageable pageable = PageRequest.of(
                query.getPage(),
                query.getSize(),
                Sort.by(Sort.Direction.DESC, "updatedAt")
        );

        Page<Technology> strategies;

        if (query.getOwnerId() != null && !query.getOwnerId().isBlank()) {
            List<Technology> allStrategies = strategyRepository.findByTenantIdAndDeletedAtIsNull(query.getTenantId());
            List<Technology> filtered = allStrategies.stream()
                    .filter(s -> query.getOwnerId().equals(s.getOwnerId()))
                    .collect(Collectors.toList());

            int start = (int) pageable.getOffset();
            int end = Math.min(start + pageable.getPageSize(), filtered.size());

            List<Technology> pageContent = filtered.subList(start, end);
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
    public List<TechnologyDto> getAllStrategies(String tenantId) {
        log.debug("Getting all strategies for tenant: {}", tenantId);
        return strategyRepository.findByTenantIdAndDeletedAtIsNull(tenantId)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Check if strategy exists
     */
    public boolean exists(String technologyId, String tenantId) {
        return strategyRepository.existsByIdAndTenantIdAndDeletedAtIsNull(technologyId, tenantId);
    }

    private TechnologyDto toDto(Technology strategy) {
        return TechnologyDto.builder()
                .id(strategy.getId())
                .tenantId(strategy.getTenantId())
                .name(strategy.getName())
                .description(strategy.getDescription())
                .ownerId(strategy.getOwnerId())
                .widgets(strategy.getWidgets() == null ? null :
                        strategy.getWidgets().stream()
                                .map(widget -> TechnologyDto.WidgetDto.builder()
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
