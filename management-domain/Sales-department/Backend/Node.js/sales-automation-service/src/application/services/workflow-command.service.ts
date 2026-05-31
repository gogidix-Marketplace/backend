import { Injectable, Logger } from '@nestjs/common';
import { v4 as uuidv4 } from 'uuid';
import { Workflow, Action, Trigger } from '../../domain/models';
import { WorkflowRepository, EventPublisher, EmailService, NotificationService, WebhookService } from '../../domain/ports/output';
import { ExecutionContext } from '@shared/context';
import {
  CreateWorkflowCommand,
  UpdateWorkflowCommand,
  DeleteWorkflowCommand,
  ActivateWorkflowCommand,
  DeactivateWorkflowCommand,
  ExecuteWorkflowCommand,
} from '../../domain/ports/input';
import {
  WorkflowExecutedEvent,
  WorkflowFailedEvent,
  AutomationTriggeredEvent,
} from '../../domain/events';
import { TriggerType, ActionType, WorkflowExecutionStatus } from '../../domain/enums';
import {
  WorkflowNotFoundException,
  InvalidAutomationRuleException,
  ActionExecutionException,
} from '@shared/exceptions';

@Injectable()
export class WorkflowCommandService {
  private readonly logger = new Logger(WorkflowCommandService.name);
  private activeExecutions = new Map<string, Set<string>>();

  constructor(
    private readonly workflowRepository: WorkflowRepository,
    private readonly eventPublisher: EventPublisher,
    private readonly emailService?: EmailService,
    private readonly notificationService?: NotificationService,
    private readonly webhookService?: WebhookService,
  ) {}

  async create(
    command: CreateWorkflowCommand,
    context: ExecutionContext,
  ): Promise<Workflow> {
    this.logger.log(`Creating workflow: ${command.name}`);

    const trigger = Trigger.create(
      command.trigger.name,
      command.trigger.configuration,
      context.tenantId,
    );

    const actions = command.actions.map(actionDto =>
      Action.create(
        actionDto.name,
        actionDto.type as ActionType,
        actionDto.parameters,
        actionDto.order,
        context.tenantId,
      ),
    );

    const workflow = Workflow.create(
      command.name,
      trigger,
      actions,
      context.tenantId,
      command.description,
    );

    if (command.tags) {
      workflow.setTags(command.tags);
    }

    if (command.priority !== undefined) {
      workflow.setPriority(command.priority);
    }

    if (command.maxConcurrentExecutions) {
      workflow.setMaxConcurrentExecutions(command.maxConcurrentExecutions);
    }

    if (command.timeoutMs) {
      workflow.setTimeoutMs(command.timeoutMs);
    }

    await this.workflowRepository.save(workflow);

    this.logger.log(`Workflow created: ${workflow.id}`);
    return workflow;
  }

  async update(
    command: UpdateWorkflowCommand,
    context: ExecutionContext,
  ): Promise<Workflow> {
    this.logger.log(`Updating workflow: ${command.workflowId}`);

    const workflow = await this.workflowRepository.findById(command.workflowId);
    if (!workflow) {
      throw new WorkflowNotFoundException(command.workflowId);
    }

    if (command.name) {
      workflow.updateName(command.name);
    }

    if (command.description !== undefined) {
      workflow.updateDescription(command.description);
    }

    if (command.tags) {
      workflow.setTags(command.tags);
    }

    if (command.priority !== undefined) {
      workflow.setPriority(command.priority);
    }

    if (command.maxConcurrentExecutions) {
      workflow.setMaxConcurrentExecutions(command.maxConcurrentExecutions);
    }

    if (command.timeoutMs) {
      workflow.setTimeoutMs(command.timeoutMs);
    }

    if (command.actions) {
      for (const actionDto of command.actions) {
        const action = Action.create(
          actionDto.name,
          actionDto.type as ActionType,
          actionDto.parameters,
          actionDto.order,
          context.tenantId,
        );
        workflow.updateAction(action.id, action);
      }
    }

    await this.workflowRepository.save(workflow);

    this.logger.log(`Workflow updated: ${workflow.id}`);
    return workflow;
  }

  async delete(
    command: DeleteWorkflowCommand,
    context: ExecutionContext,
  ): Promise<void> {
    this.logger.log(`Deleting workflow: ${command.workflowId}`);

    const workflow = await this.workflowRepository.findById(command.workflowId);
    if (!workflow) {
      throw new WorkflowNotFoundException(command.workflowId);
    }

    await this.workflowRepository.delete(command.workflowId);

    this.logger.log(`Workflow deleted: ${command.workflowId}`);
  }

  async activate(
    command: ActivateWorkflowCommand,
    context: ExecutionContext,
  ): Promise<Workflow> {
    this.logger.log(`Activating workflow: ${command.workflowId}`);

    const workflow = await this.workflowRepository.findById(command.workflowId);
    if (!workflow) {
      throw new WorkflowNotFoundException(command.workflowId);
    }

    workflow.activate();
    await this.workflowRepository.save(workflow);

    this.logger.log(`Workflow activated: ${command.workflowId}`);
    return workflow;
  }

  async deactivate(
    command: DeactivateWorkflowCommand,
    context: ExecutionContext,
  ): Promise<Workflow> {
    this.logger.log(`Deactivating workflow: ${command.workflowId}`);

    const workflow = await this.workflowRepository.findById(command.workflowId);
    if (!workflow) {
      throw new WorkflowNotFoundException(command.workflowId);
    }

    workflow.deactivate();
    await this.workflowRepository.save(workflow);

    this.logger.log(`Workflow deactivated: ${command.workflowId}`);
    return workflow;
  }

  async execute(
    command: ExecuteWorkflowCommand,
    context: ExecutionContext,
  ): Promise<Workflow.WorkflowExecutionResult> {
    this.logger.log(`Executing workflow: ${command.workflowId}`);

    const workflow = await this.workflowRepository.findById(command.workflowId);
    if (!workflow) {
      throw new WorkflowNotFoundException(command.workflowId);
    }

    if (!workflow.canExecute()) {
      throw new InvalidAutomationRuleException('Workflow is not enabled for execution');
    }

    // Check concurrent executions
    if (!this.canExecuteConcurrently(workflow)) {
      throw new InvalidAutomationRuleException('Maximum concurrent executions reached');
    }

    const executionId = uuidv4();
    const startedAt = new Date();

    // Track active execution
    this.trackExecution(workflow.id, executionId);

    // Publish automation triggered event
    const triggeredEvent = AutomationTriggeredEvent.fromRule(
      workflow.id,
      workflow.name,
      workflow.trigger.configuration.type as TriggerType,
      command.triggerData,
      context.tenantId,
      command.entityType,
      command.entityId,
      command.userId,
      context.correlationId,
    );
    await this.eventPublisher.publish(triggeredEvent);

    const results: Workflow.ActionExecutionResult[] = [];
    let finalStatus = WorkflowExecutionStatus.COMPLETED;
    let error: string | undefined;

    try {
      // Execute actions in order
      for (const action of workflow.actions) {
        const actionResult = await this.executeAction(action, command, context);
        results.push(actionResult);

        if (actionResult.status === WorkflowExecutionStatus.FAILED && !action.continueOnError) {
          finalStatus = WorkflowExecutionStatus.FAILED;
          error = `Action "${action.name}" failed: ${actionResult.error}`;
          break;
        }

        if (action.delayMs) {
          await this.delay(action.delayMs);
        }
      }
    } catch (err) {
      finalStatus = WorkflowExecutionStatus.FAILED;
      error = err instanceof Error ? err.message : 'Unknown error';
      this.logger.error(`Workflow execution failed: ${error}`);

      // Publish workflow failed event
      const failedEvent = WorkflowFailedEvent.fromWorkflow(
        workflow.id,
        workflow.name,
        executionId,
        error,
        'ExecutionError',
        { type: 'trigger', data: command.triggerData },
        context.tenantId,
        undefined,
        undefined,
        err instanceof Error ? err.stack : undefined,
        context.correlationId,
      );
      await this.eventPublisher.publish(failedEvent);
    } finally {
      this.untrackExecution(workflow.id, executionId);
    }

    const completedAt = new Date();
    const executionResult: Workflow.WorkflowExecutionResult = {
      executionId,
      status: finalStatus,
      startedAt,
      completedAt,
      results,
      error,
    };

    workflow.recordExecution(executionResult);
    await this.workflowRepository.save(workflow);

    // Publish workflow executed event
    const executedEvent = WorkflowExecutedEvent.fromWorkflow(
      workflow.id,
      workflow.name,
      executionId,
      finalStatus,
      { type: 'trigger', data: command.triggerData },
      results,
      context.tenantId,
      startedAt,
      completedAt,
      error,
      context.correlationId,
    );
    await this.eventPublisher.publish(executedEvent);

    this.logger.log(`Workflow execution completed: ${executionId} with status: ${finalStatus}`);
    return executionResult;
  }

  private async executeAction(
    action: Action,
    command: ExecuteWorkflowCommand,
    context: ExecutionContext,
  ): Promise<Workflow.ActionExecutionResult> {
    const startedAt = new Date();
    action.recordExecution();

    try {
      switch (action.type) {
        case ActionType.SEND_EMAIL:
          await this.executeEmailAction(action, command, context);
          break;

        case ActionType.SEND_NOTIFICATION:
          await this.executeNotificationAction(action, command, context);
          break;

        case ActionType.SEND_WEBHOOK:
          await this.executeWebhookAction(action, command, context);
          break;

        case ActionType.UPDATE_LEAD_STATUS:
        case ActionType.UPDATE_DEAL_STAGE:
        case ActionType.UPDATE_FIELD:
          await this.executeUpdateFieldAction(action, command, context);
          break;

        case ActionType.CREATE_TASK:
        case ActionType.ASSIGN_TASK:
          await this.executeTaskAction(action, command, context);
          break;

        case ActionType.SCORE_LEAD:
          await this.executeScoreLeadAction(action, command, context);
          break;

        default:
          this.logger.warn(`Unknown action type: ${action.type}. Skipping.`);
          return {
            actionId: action.id,
            actionName: action.name,
            status: WorkflowExecutionStatus.COMPLETED,
            startedAt,
            completedAt: new Date(),
          };
      }

      return {
        actionId: action.id,
        actionName: action.name,
        status: WorkflowExecutionStatus.COMPLETED,
        startedAt,
        completedAt: new Date(),
      };
    } catch (error) {
      const errorMessage = error instanceof Error ? error.message : 'Unknown error';
      return {
        actionId: action.id,
        actionName: action.name,
        status: WorkflowExecutionStatus.FAILED,
        startedAt,
        completedAt: new Date(),
        error: errorMessage,
      };
    }
  }

  private async executeEmailAction(
    action: Action,
    command: ExecuteWorkflowCommand,
    context: ExecutionContext,
  ): Promise<void> {
    if (!this.emailService) {
      throw new ActionExecutionException(action.id, 'Email service not configured');
    }

    const params = action.parameters;
    const result = await this.emailService.sendEmail({
      to: params.to || this.resolveValue(params.to, command.triggerData),
      cc: params.cc,
      bcc: params.bcc,
      subject: params.subject || this.resolveValue(params.subject, command.triggerData),
      template: params.template,
      templateData: params.templateData ? this.resolveTemplateData(params.templateData, command.triggerData) : undefined,
      body: params.body,
    });

    if (!result.success) {
      throw new ActionExecutionException(action.id, result.error || 'Email send failed');
    }
  }

  private async executeNotificationAction(
    action: Action,
    command: ExecuteWorkflowCommand,
    context: ExecutionContext,
  ): Promise<void> {
    if (!this.notificationService) {
      throw new ActionExecutionException(action.id, 'Notification service not configured');
    }

    const params = action.parameters;
    const result = await this.notificationService.sendNotification({
      recipient: params.recipient || this.resolveValue(params.recipient, command.triggerData),
      title: params.title || this.resolveValue(params.title, command.triggerData),
      message: params.message || this.resolveValue(params.message, command.triggerData),
      type: params.type,
      data: params.data,
    });

    if (!result.success) {
      throw new ActionExecutionException(action.id, result.error || 'Notification send failed');
    }
  }

  private async executeWebhookAction(
    action: Action,
    command: ExecuteWorkflowCommand,
    context: ExecutionContext,
  ): Promise<void> {
    if (!this.webhookService) {
      throw new ActionExecutionException(action.id, 'Webhook service not configured');
    }

    const params = action.parameters;
    const body = params.body
      ? this.resolveTemplateData(params.body, command.triggerData)
      : { triggerData: command.triggerData };

    const result = await this.webhookService.sendWebhook({
      url: params.url,
      method: params.method || 'POST',
      headers: params.headers,
      body,
      timeout: params.timeout || 30000,
    });

    if (!result.success) {
      throw new ActionExecutionException(action.id, result.error || 'Webhook call failed');
    }
  }

  private async executeUpdateFieldAction(
    action: Action,
    command: ExecuteWorkflowCommand,
    context: ExecutionContext,
  ): Promise<void> {
    const params = action.parameters;
    this.logger.log(
      `Updating field: ${params.entityType}.${params.field} to ${params.value} for entity ${params.entityId || command.entityId}`,
    );
    // This would typically call an external service to update the entity
    // For now, we'll just log it
  }

  private async executeTaskAction(
    action: Action,
    command: ExecuteWorkflowCommand,
    context: ExecutionContext,
  ): Promise<void> {
    const params = action.parameters;
    this.logger.log(
      `Creating task: ${params.title} assigned to ${params.assignTo}`,
    );
    // This would typically call a task service to create the task
    // For now, we'll just log it
  }

  private async executeScoreLeadAction(
    action: Action,
    command: ExecuteWorkflowCommand,
    context: ExecutionContext,
  ): Promise<void> {
    const params = action.parameters;
    this.logger.log(
      `Scoring lead ${params.leadId || command.entityId} with score ${params.score}`,
    );
    // This would typically call a lead service to update the lead score
    // For now, we'll just log it
  }

  private resolveValue(value: any, data: Record<string, any>): any {
    if (typeof value === 'string' && value.startsWith('{{') && value.endsWith('}}')) {
      const path = value.slice(2, -2).trim();
      return path.split('.').reduce((current, key) => current?.[key], data);
    }
    return value;
  }

  private resolveTemplateData(template: Record<string, any>, data: Record<string, any>): Record<string, any> {
    const resolved: Record<string, any> = {};
    for (const [key, value] of Object.entries(template)) {
      resolved[key] = this.resolveValue(value, data);
    }
    return resolved;
  }

  private canExecuteConcurrently(workflow: Workflow): boolean {
    const activeExecutions = this.activeExecutions.get(workflow.id) || new Set();
    return activeExecutions.size < workflow.maxConcurrentExecutions;
  }

  private trackExecution(workflowId: string, executionId: string): void {
    if (!this.activeExecutions.has(workflowId)) {
      this.activeExecutions.set(workflowId, new Set());
    }
    this.activeExecutions.get(workflowId)!.add(executionId);
  }

  private untrackExecution(workflowId: string, executionId: string): void {
    const executions = this.activeExecutions.get(workflowId);
    if (executions) {
      executions.delete(executionId);
      if (executions.size === 0) {
        this.activeExecutions.delete(workflowId);
      }
    }
  }

  private delay(ms: number): Promise<void> {
    return new Promise(resolve => setTimeout(resolve, ms));
  }
}
