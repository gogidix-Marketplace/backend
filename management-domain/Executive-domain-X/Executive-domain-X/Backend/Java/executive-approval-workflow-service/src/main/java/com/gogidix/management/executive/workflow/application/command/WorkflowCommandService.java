package com.gogidix.management.executive.workflow.application.command;

import com.gogidix.management.executive.workflow.domain.model.Workflow;
import com.gogidix.management.executive.workflow.domain.repository.WorkflowRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

/**
 * Command service for Workflow operations
 * Handles all write operations (Create, Update, Delete) following CQRS pattern
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class WorkflowCommandService {

    private final WorkflowRepository strategyRepository;

    /**
     * Handle create strategy command
     */
    @Transactional
    public Workflow handle(CreateWorkflowCommand command) {
        log.debug("Handling CreateWorkflowCommand for tenant: {}, name: {}",
                command.getTenantId(), command.getName());

        // Create workflows
        Workflow strategy = Workflow.builder()
                .tenantId(command.getTenantId())
                .name(command.getName())
                .description(command.getDescription())
                .ownerId(command.getOwnerId())
                .layout(command.getLayout())
                .status(Workflow.WorkflowStatus.DRAFT)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .active(true)
                .build();

        // Add widgets if provided
        if (command.getWidgets() != null && !command.getWidgets().isEmpty()) {
            for (CreateWorkflowCommand.WidgetConfig widgetConfig : command.getWidgets()) {
                Workflow.Widget widget = Workflow.Widget.builder()
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
    public Workflow handle(UpdateWorkflowCommand command) {
        log.debug("Handling UpdateWorkflowCommand for workflows: {}, tenant: {}",
                command.getWorkflowId(), command.getTenantId());

        Workflow strategy = strategyRepository
                .findByIdAndTenantIdAndDeletedAtIsNull(command.getWorkflowId(), command.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Workflow not found"));

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
            strategy.setStatus(mapWorkflowStatus(command.getStatus()));
        }

        strategy.updateTimestamp();

        return strategyRepository.save(strategy);
    }

    /**
     * Handle delete strategy command (soft delete)
     */
    @Transactional
    public void handle(DeleteWorkflowCommand command) {
        log.debug("Handling DeleteWorkflowCommand for workflows: {}, tenant: {}",
                command.getWorkflowId(), command.getTenantId());

        Workflow strategy = strategyRepository
                .findByIdAndTenantIdAndDeletedAtIsNull(command.getWorkflowId(), command.getTenantId())
                .orElseThrow(() -> new IllegalArgumentException("Workflow not found"));

        // Soft delete
        strategy.markAsDeleted();
        strategyRepository.save(strategy);
    }

    private Workflow.Widget.WidgetType mapWidgetType(String type) {
        try {
            return Workflow.Widget.WidgetType.valueOf(type);
        } catch (IllegalArgumentException e) {
            return Workflow.Widget.WidgetType.KPI_CARD;
        }
    }

    private Workflow.WorkflowStatus mapWorkflowStatus(UpdateWorkflowCommand.WorkflowStatus status) {
        return Workflow.WorkflowStatus.valueOf(status.name());
    }
}
