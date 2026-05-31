package com.gogidix.management.executive.financial.application.command;

import com.gogidix.management.executive.financial.domain.model.FinancialData;
import com.gogidix.management.executive.financial.domain.repository.FinancialDataRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Command service for FinancialData operations
 * Handles all write operations (Create, Update, Delete) following CQRS pattern
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class FinancialDataCommandService {

    private final FinancialDataRepository strategyRepository;

    /**
     * Handle create strategy command
     */
    @Transactional
    public FinancialData handle(CreateFinancialDataCommand command) {
        log.debug("Handling CreateFinancialDataCommand for tenant: {}, name: {}",
                command.getTenantId(), command.getName());

        // Create financials
        FinancialData strategy = FinancialData.builder()
                .tenantId(command.getTenantId())
                .name(command.getName())
                .description(command.getDescription())
                .ownerId(command.getOwnerId())
                .layout(command.getLayout())
                .status(FinancialData.FinancialDataStatus.DRAFT)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .active(true)
                .build();

        // Add widgets if provided
        if (command.getWidgets() != null && !command.getWidgets().isEmpty()) {
            for (CreateFinancialDataCommand.WidgetConfig widgetConfig : command.getWidgets()) {
                FinancialData.Widget widget = FinancialData.Widget.builder()
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
    public FinancialData handle(UpdateFinancialDataCommand command) {
        log.debug("Handling UpdateFinancialDataCommand for financials: {}, tenant: {}",
                command.getFinancialDataId(), command.getTenantId());

        FinancialData strategy = strategyRepository
                .findByIdAndTenantIdAndDeletedAtIsNull(command.getFinancialDataId(), command.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("FinancialData not found"));

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
            strategy.setStatus(mapFinancialDataStatus(command.getStatus()));
        }

        strategy.updateTimestamp();

        return strategyRepository.save(strategy);
    }

    /**
     * Handle delete strategy command (soft delete)
     */
    @Transactional
    public void handle(DeleteFinancialDataCommand command) {
        log.debug("Handling DeleteFinancialDataCommand for financials: {}, tenant: {}",
                command.getFinancialDataId(), command.getTenantId());

        FinancialData strategy = strategyRepository
                .findByIdAndTenantIdAndDeletedAtIsNull(command.getFinancialDataId(), command.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("FinancialData not found"));

        // Soft delete
        strategy.markAsDeleted();
        strategyRepository.save(strategy);
    }

    private FinancialData.Widget.WidgetType mapWidgetType(String type) {
        try {
            return FinancialData.Widget.WidgetType.valueOf(type);
        } catch (IllegalArgumentException e) {
            return FinancialData.Widget.WidgetType.KPI_CARD;
        }
    }

    private FinancialData.FinancialDataStatus mapFinancialDataStatus(UpdateFinancialDataCommand.FinancialDataStatus status) {
        return FinancialData.FinancialDataStatus.valueOf(status.name());
    }
}
