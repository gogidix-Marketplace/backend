package com.gogidix.management.executive.alert.application.command;

import com.gogidix.management.executive.alert.domain.model.Alert;
import com.gogidix.management.executive.alert.domain.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Command service for Alert operations
 * Handles all write operations (Create, Update, Delete) following CQRS pattern
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AlertCommandService {

    private final AlertRepository strategyRepository;

    /**
     * Handle create strategy command
     */
    @Transactional
    public Alert handle(CreateAlertCommand command) {
        log.debug("Handling CreateAlertCommand for tenant: {}, name: {}",
                command.getTenantId(), command.getName());

        // Create alerts
        Alert strategy = Alert.builder()
                .tenantId(command.getTenantId())
                .name(command.getName())
                .description(command.getDescription())
                .ownerId(command.getOwnerId())
                .layout(command.getLayout())
                .status(Alert.AlertStatus.DRAFT)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .active(true)
                .build();

        // Add widgets if provided
        if (command.getWidgets() != null && !command.getWidgets().isEmpty()) {
            for (CreateAlertCommand.WidgetConfig widgetConfig : command.getWidgets()) {
                Alert.Widget widget = Alert.Widget.builder()
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
    public Alert handle(UpdateAlertCommand command) {
        log.debug("Handling UpdateAlertCommand for alerts: {}, tenant: {}",
                command.getAlertId(), command.getTenantId());

        Alert strategy = strategyRepository
                .findByIdAndTenantIdAndDeletedAtIsNull(command.getAlertId(), command.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Alert not found"));

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
            strategy.setStatus(mapAlertStatus(command.getStatus()));
        }

        strategy.updateTimestamp();

        return strategyRepository.save(strategy);
    }

    /**
     * Handle delete strategy command (soft delete)
     */
    @Transactional
    public void handle(DeleteAlertCommand command) {
        log.debug("Handling DeleteAlertCommand for alerts: {}, tenant: {}",
                command.getAlertId(), command.getTenantId());

        Alert strategy = strategyRepository
                .findByIdAndTenantIdAndDeletedAtIsNull(command.getAlertId(), command.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Alert not found"));

        // Soft delete
        strategy.markAsDeleted();
        strategyRepository.save(strategy);
    }

    private Alert.Widget.WidgetType mapWidgetType(String type) {
        try {
            return Alert.Widget.WidgetType.valueOf(type);
        } catch (IllegalArgumentException e) {
            return Alert.Widget.WidgetType.KPI_CARD;
        }
    }

    private Alert.AlertStatus mapAlertStatus(UpdateAlertCommand.AlertStatus status) {
        return Alert.AlertStatus.valueOf(status.name());
    }
}
