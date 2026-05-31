package com.gogidix.management.executive.technology.application.command;

import com.gogidix.management.executive.technology.domain.model.Technology;
import com.gogidix.management.executive.technology.domain.repository.TechnologyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Command service for Technology operations
 * Handles all write operations (Create, Update, Delete) following CQRS pattern
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TechnologyCommandService {

    private final TechnologyRepository strategyRepository;

    /**
     * Handle create strategy command
     */
    @Transactional
    public Technology handle(CreateTechnologyCommand command) {
        log.debug("Handling CreateTechnologyCommand for tenant: {}, name: {}",
                command.getTenantId(), command.getName());

        // Create technologys
        Technology strategy = Technology.builder()
                .tenantId(command.getTenantId())
                .name(command.getName())
                .description(command.getDescription())
                .ownerId(command.getOwnerId())
                .layout(command.getLayout())
                .status(Technology.TechnologyStatus.DRAFT)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .active(true)
                .build();

        // Add widgets if provided
        if (command.getWidgets() != null && !command.getWidgets().isEmpty()) {
            for (CreateTechnologyCommand.WidgetConfig widgetConfig : command.getWidgets()) {
                Technology.Widget widget = Technology.Widget.builder()
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
    public Technology handle(UpdateTechnologyCommand command) {
        log.debug("Handling UpdateTechnologyCommand for technologys: {}, tenant: {}",
                command.getTechnologyId(), command.getTenantId());

        Technology strategy = strategyRepository
                .findByIdAndTenantIdAndDeletedAtIsNull(command.getTechnologyId(), command.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Technology not found"));

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
            strategy.setStatus(mapTechnologyStatus(command.getStatus()));
        }

        strategy.updateTimestamp();

        return strategyRepository.save(strategy);
    }

    /**
     * Handle delete strategy command (soft delete)
     */
    @Transactional
    public void handle(DeleteTechnologyCommand command) {
        log.debug("Handling DeleteTechnologyCommand for technologys: {}, tenant: {}",
                command.getTechnologyId(), command.getTenantId());

        Technology strategy = strategyRepository
                .findByIdAndTenantIdAndDeletedAtIsNull(command.getTechnologyId(), command.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Technology not found"));

        // Soft delete
        strategy.markAsDeleted();
        strategyRepository.save(strategy);
    }

    private Technology.Widget.WidgetType mapWidgetType(String type) {
        try {
            return Technology.Widget.WidgetType.valueOf(type);
        } catch (IllegalArgumentException e) {
            return Technology.Widget.WidgetType.KPI_CARD;
        }
    }

    private Technology.TechnologyStatus mapTechnologyStatus(UpdateTechnologyCommand.TechnologyStatus status) {
        return Technology.TechnologyStatus.valueOf(status.name());
    }
}
