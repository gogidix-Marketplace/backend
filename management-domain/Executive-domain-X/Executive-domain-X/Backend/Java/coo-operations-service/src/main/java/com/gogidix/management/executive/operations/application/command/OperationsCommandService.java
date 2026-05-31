package com.gogidix.management.executive.operations.application.command;

import com.gogidix.management.executive.operations.domain.model.Operations;
import com.gogidix.management.executive.operations.domain.repository.OperationsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Command service for Operations operations
 * Handles all write operations (Create, Update, Delete) following CQRS pattern
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class OperationsCommandService {

    private final OperationsRepository strategyRepository;

    /**
     * Handle create strategy command
     */
    @Transactional
    public Operations handle(CreateOperationsCommand command) {
        log.debug("Handling CreateOperationsCommand for tenant: {}, name: {}",
                command.getTenantId(), command.getName());

        // Create operationss
        Operations strategy = Operations.builder()
                .tenantId(command.getTenantId())
                .name(command.getName())
                .description(command.getDescription())
                .ownerId(command.getOwnerId())
                .layout(command.getLayout())
                .status(Operations.OperationsStatus.DRAFT)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .active(true)
                .build();

        // Add widgets if provided
        if (command.getWidgets() != null && !command.getWidgets().isEmpty()) {
            for (CreateOperationsCommand.WidgetConfig widgetConfig : command.getWidgets()) {
                Operations.Widget widget = Operations.Widget.builder()
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
    public Operations handle(UpdateOperationsCommand command) {
        log.debug("Handling UpdateOperationsCommand for operationss: {}, tenant: {}",
                command.getOperationsId(), command.getTenantId());

        Operations strategy = strategyRepository
                .findByIdAndTenantIdAndDeletedAtIsNull(command.getOperationsId(), command.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Operations not found"));

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
            strategy.setStatus(mapOperationsStatus(command.getStatus()));
        }

        strategy.updateTimestamp();

        return strategyRepository.save(strategy);
    }

    /**
     * Handle delete strategy command (soft delete)
     */
    @Transactional
    public void handle(DeleteOperationsCommand command) {
        log.debug("Handling DeleteOperationsCommand for operationss: {}, tenant: {}",
                command.getOperationsId(), command.getTenantId());

        Operations strategy = strategyRepository
                .findByIdAndTenantIdAndDeletedAtIsNull(command.getOperationsId(), command.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Operations not found"));

        // Soft delete
        strategy.markAsDeleted();
        strategyRepository.save(strategy);
    }

    private Operations.Widget.WidgetType mapWidgetType(String type) {
        try {
            return Operations.Widget.WidgetType.valueOf(type);
        } catch (IllegalArgumentException e) {
            return Operations.Widget.WidgetType.KPI_CARD;
        }
    }

    private Operations.OperationsStatus mapOperationsStatus(UpdateOperationsCommand.OperationsStatus status) {
        return Operations.OperationsStatus.valueOf(status.name());
    }
}
