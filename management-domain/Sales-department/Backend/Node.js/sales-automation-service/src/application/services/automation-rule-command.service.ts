import { Injectable, Logger } from '@nestjs/common';
import { AutomationRule } from '../../domain/models/automation-rule.entity';
import { AutomationRuleRepository, EventPublisher } from '../../domain/ports/output';
import { ExecutionContext } from '@shared/context';
import {
  RuleCreatedEvent,
  RuleUpdatedEvent,
  RuleDeletedEvent,
} from '../../domain/events';
import {
  CreateAutomationRuleCommand,
  UpdateAutomationRuleCommand,
  DeleteAutomationRuleCommand,
  ActivateAutomationRuleCommand,
  PauseAutomationRuleCommand,
  ArchiveAutomationRuleCommand,
} from '../../domain/ports/input';
import {
  AutomationRuleNotFoundException,
  InvalidAutomationRuleException,
  AutomationRuleAlreadyActiveException,
  AutomationRuleAlreadyInactiveException,
} from '@shared/exceptions';

@Injectable()
export class AutomationRuleCommandService {
  private readonly logger = new Logger(AutomationRuleCommandService.name);

  constructor(
    private readonly ruleRepository: AutomationRuleRepository,
    private readonly eventPublisher: EventPublisher,
  ) {}

  async create(
    command: CreateAutomationRuleCommand,
    context: ExecutionContext,
  ): Promise<AutomationRule> {
    this.logger.log(`Creating automation rule: ${command.name}`);

    const rule = AutomationRule.create(
      command.name,
      command.trigger,
      command.actions,
      context.tenantId,
      command.description,
    );

    if (command.tags) {
      rule.setTags(command.tags);
    }

    if (command.priority !== undefined) {
      rule.setPriority(command.priority);
    }

    if (command.category) {
      (rule as any)._category = command.category;
    }

    if (command.templateId) {
      rule.setTemplateId(command.templateId);
    }

    if (command.schedule) {
      rule.setSchedule(command.schedule);
    }

    if (command.leadScoring) {
      rule.setLeadScoringRule(command.leadScoring);
    }

    if (command.dealStageRule) {
      rule.setDealStageRule(command.dealStageRule);
    }

    await this.ruleRepository.save(rule);

    const event = RuleCreatedEvent.fromRule(
      rule.id,
      rule.name,
      rule.description,
      rule.status,
      command.trigger.type,
      command.actions.length,
      context.tenantId,
      context.userId,
      command.category,
      context.correlationId,
    );

    await this.eventPublisher.publish(event);

    this.logger.log(`Automation rule created: ${rule.id}`);
    return rule;
  }

  async update(
    command: UpdateAutomationRuleCommand,
    context: ExecutionContext,
  ): Promise<AutomationRule> {
    this.logger.log(`Updating automation rule: ${command.ruleId}`);

    const rule = await this.ruleRepository.findById(command.ruleId);
    if (!rule) {
      throw new AutomationRuleNotFoundException(command.ruleId);
    }

    const previousValues = {
      name: rule.name,
      description: rule.description,
      priority: rule.priority,
      tags: rule.tags,
    };

    const updatedFields: string[] = [];

    if (command.name) {
      rule.updateName(command.name);
      updatedFields.push('name');
    }

    if (command.description !== undefined) {
      rule.updateDescription(command.description);
      updatedFields.push('description');
    }

    if (command.tags) {
      rule.setTags(command.tags);
      updatedFields.push('tags');
    }

    if (command.priority !== undefined) {
      rule.setPriority(command.priority);
      updatedFields.push('priority');
    }

    if (command.trigger) {
      rule.updateTrigger(command.trigger);
      updatedFields.push('trigger');
    }

    if (command.actions) {
      for (const action of command.actions) {
        rule.updateAction(action.order, action);
      }
      updatedFields.push('actions');
    }

    if (command.leadScoring) {
      rule.setLeadScoringRule(command.leadScoring);
      updatedFields.push('leadScoring');
    }

    if (command.dealStageRule) {
      rule.setDealStageRule(command.dealStageRule);
      updatedFields.push('dealStageRule');
    }

    if (command.schedule) {
      rule.setSchedule(command.schedule);
      updatedFields.push('schedule');
    }

    await this.ruleRepository.save(rule);

    const event = RuleUpdatedEvent.fromRule(
      rule.id,
      rule.name,
      updatedFields,
      context.userId,
      context.tenantId,
      previousValues,
      { name: rule.name, description: rule.description, priority: rule.priority, tags: rule.tags },
      context.correlationId,
    );

    await this.eventPublisher.publish(event);

    this.logger.log(`Automation rule updated: ${rule.id}`);
    return rule;
  }

  async delete(
    command: DeleteAutomationRuleCommand,
    context: ExecutionContext,
  ): Promise<void> {
    this.logger.log(`Deleting automation rule: ${command.ruleId}`);

    const rule = await this.ruleRepository.findById(command.ruleId);
    if (!rule) {
      throw new AutomationRuleNotFoundException(command.ruleId);
    }

    const event = RuleDeletedEvent.fromRule(
      rule.id,
      rule.name,
      context.userId,
      context.tenantId,
      rule.status === 'ACTIVE',
      command.reason,
      context.correlationId,
    );

    await this.ruleRepository.delete(command.ruleId);
    await this.eventPublisher.publish(event);

    this.logger.log(`Automation rule deleted: ${command.ruleId}`);
  }

  async activate(
    command: ActivateAutomationRuleCommand,
    context: ExecutionContext,
  ): Promise<AutomationRule> {
    this.logger.log(`Activating automation rule: ${command.ruleId}`);

    const rule = await this.ruleRepository.findById(command.ruleId);
    if (!rule) {
      throw new AutomationRuleNotFoundException(command.ruleId);
    }

    try {
      rule.activate();
    } catch (error) {
      if (error.message.includes('already active')) {
        throw new AutomationRuleAlreadyActiveException(command.ruleId);
      }
      throw new InvalidAutomationRuleException(error.message);
    }

    await this.ruleRepository.save(rule);

    const event = RuleUpdatedEvent.fromRule(
      rule.id,
      rule.name,
      ['status'],
      context.userId,
      context.tenantId,
      undefined,
      { status: rule.status },
      context.correlationId,
    );

    await this.eventPublisher.publish(event);

    this.logger.log(`Automation rule activated: ${command.ruleId}`);
    return rule;
  }

  async pause(
    command: PauseAutomationRuleCommand,
    context: ExecutionContext,
  ): Promise<AutomationRule> {
    this.logger.log(`Pausing automation rule: ${command.ruleId}`);

    const rule = await this.ruleRepository.findById(command.ruleId);
    if (!rule) {
      throw new AutomationRuleNotFoundException(command.ruleId);
    }

    try {
      rule.pause();
    } catch (error) {
      if (error.message.includes('not active')) {
        throw new AutomationRuleAlreadyInactiveException(command.ruleId);
      }
      throw new InvalidAutomationRuleException(error.message);
    }

    await this.ruleRepository.save(rule);

    const event = RuleUpdatedEvent.fromRule(
      rule.id,
      rule.name,
      ['status'],
      context.userId,
      context.tenantId,
      undefined,
      { status: rule.status },
      context.correlationId,
    );

    await this.eventPublisher.publish(event);

    this.logger.log(`Automation rule paused: ${command.ruleId}`);
    return rule;
  }

  async archive(
    command: ArchiveAutomationRuleCommand,
    context: ExecutionContext,
  ): Promise<AutomationRule> {
    this.logger.log(`Archiving automation rule: ${command.ruleId}`);

    const rule = await this.ruleRepository.findById(command.ruleId);
    if (!rule) {
      throw new AutomationRuleNotFoundException(command.ruleId);
    }

    try {
      rule.archive();
    } catch (error) {
      throw new InvalidAutomationRuleException(error.message);
    }

    await this.ruleRepository.save(rule);

    const event = RuleUpdatedEvent.fromRule(
      rule.id,
      rule.name,
      ['status'],
      context.userId,
      context.tenantId,
      undefined,
      { status: rule.status },
      context.correlationId,
    );

    await this.eventPublisher.publish(event);

    this.logger.log(`Automation rule archived: ${command.ruleId}`);
    return rule;
  }
}
