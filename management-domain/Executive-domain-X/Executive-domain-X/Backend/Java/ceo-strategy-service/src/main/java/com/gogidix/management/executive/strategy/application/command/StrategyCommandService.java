package com.gogidix.management.executive.strategy.application.command;

import com.gogidix.management.executive.strategy.domain.model.Strategy;
import com.gogidix.management.executive.strategy.domain.repository.StrategyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Command service for Strategy operations
 * Handles all write operations (Create, Update, Delete) following CQRS pattern
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StrategyCommandService {

    private final StrategyRepository strategyRepository;

    /**
     * Handle create strategy command
     */
    @Transactional
    public Strategy handle(CreateStrategyCommand command) {
        log.debug("Handling CreateStrategyCommand for tenant: {}, name: {}",
                command.getTenantId(), command.getName());

        // Create dashboard
        Strategy strategy = Strategy.builder()
                .tenantId(command.getTenantId())
                .name(command.getName())
                .description(command.getDescription())
                .ownerId(command.getOwnerId())
                .layout(command.getLayout())
                .status(Strategy.StrategyStatus.DRAFT)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .active(true)
                .build();

        // Add widgets if provided
        if (command.getWidgets() != null && !command.getWidgets().isEmpty()) {
            for (CreateStrategyCommand.WidgetConfig widgetConfig : command.getWidgets()) {
                Strategy.Widget widget = Strategy.Widget.builder()
                        .id(UUID.randomUUID().toString())
                        .name(widgetConfig.getName())
                        .type(mapWidgetType(widgetConfig.getType()))
                        .position(widgetConfig.getPosition())
                        .row(widgetConfig.getRow())
                        .column(widgetConfig.getColumn())
                        .width(widgetConfig.getWidth())
                        .height(widgetConfig.getHeight())
                        .config(widgetConfig.getConfig())
                        .dataSource(widgetConfig.getDataSource())
                        .build();
                strategy.addWidget(widget);
            }
        }

        return strategyRepository.save(strategy);
    }

    /**
     * Handle update strategy command
     */
    @Transactional
    public Strategy handle(UpdateStrategyCommand command) {
        log.debug("Handling UpdateStrategyCommand for dashboard: {}, tenant: {}",
                command.getStrategyId(), command.getTenantId());

        Strategy strategy = strategyRepository
                .findByIdAndTenantIdAndDeletedAtIsNull(command.getStrategyId(), command.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Strategy not found"));

        // Update fields
        if (command.getName() != null && !command.getName().isBlank()) {
            strategy.setName(command.getName());
        }
        if (command.getDescription() != null) {
            strategy.setDescription(command.getDescription());
        }
        if (command.getLayout() != null) {
            strategy.setLayout(command.getLayout());
        }
        if (command.getStatus() != null) {
            strategy.setStatus(mapStrategyStatus(command.getStatus()));
        }

        strategy.updateTimestamp();

        return strategyRepository.save(strategy);
    }

    /**
     * Handle delete strategy command (soft delete)
     */
    @Transactional
    public void handle(DeleteStrategyCommand command) {
        log.debug("Handling DeleteStrategyCommand for dashboard: {}, tenant: {}",
                command.getStrategyId(), command.getTenantId());

        Strategy strategy = strategyRepository
                .findByIdAndTenantIdAndDeletedAtIsNull(command.getStrategyId(), command.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Strategy not found"));

        // Soft delete
        strategy.markAsDeleted();
        strategyRepository.save(strategy);
    }

    private Strategy.Widget.WidgetType mapWidgetType(String type) {
        try {
            return Strategy.Widget.WidgetType.valueOf(type);
        } catch (IllegalArgumentException e) {
            return Strategy.Widget.WidgetType.KPI_CARD;
        }
    }

    private Strategy.StrategyStatus mapStrategyStatus(UpdateStrategyCommand.StrategyStatus status) {
        return Strategy.StrategyStatus.valueOf(status.name());
    }
}
