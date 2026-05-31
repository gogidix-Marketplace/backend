import { v4 as uuidv4 } from 'uuid';
import { DomainEvent } from './base-event.interface';
import { AutomationRuleStatus } from '../enums/automation-rule-status.enum';

export interface RuleCreatedData {
  ruleId: string;
  ruleName: string;
  description: string;
  status: AutomationRuleStatus;
  category?: string;
  triggerType: string;
  actionCount: number;
  createdBy: string;
}

export class RuleCreatedEvent implements DomainEvent {
  readonly eventType = 'RuleCreated';
  readonly eventId: string;
  readonly occurredAt: Date;
  readonly aggregateId: string;
  readonly aggregateType = 'AutomationRule';
  readonly correlationId?: string;
  readonly tenantId: string;
  readonly version: number;
  readonly data: RuleCreatedData;

  constructor(data: RuleCreatedData, tenantId: string, correlationId?: string) {
    this.eventId = uuidv4();
    this.occurredAt = new Date();
    this.aggregateId = data.ruleId;
    this.tenantId = tenantId;
    this.correlationId = correlationId;
    this.version = 1;
    this.data = data;
  }

  static fromRule(
    ruleId: string,
    ruleName: string,
    description: string,
    status: AutomationRuleStatus,
    triggerType: string,
    actionCount: number,
    tenantId: string,
    createdBy: string,
    category?: string,
    correlationId?: string,
  ): RuleCreatedEvent {
    return new RuleCreatedEvent(
      {
        ruleId,
        ruleName,
        description,
        status,
        category,
        triggerType,
        actionCount,
        createdBy,
      },
      tenantId,
      correlationId,
    );
  }

  toJSON(): Record<string, any> {
    return {
      eventType: this.eventType,
      eventId: this.eventId,
      occurredAt: this.occurredAt.toISOString(),
      aggregateId: this.aggregateId,
      aggregateType: this.aggregateType,
      correlationId: this.correlationId,
      tenantId: this.tenantId,
      version: this.version,
      data: this.data,
    };
  }

  static fromJSON(json: Record<string, any>): RuleCreatedEvent {
    const event = new RuleCreatedEvent(json.data, json.tenantId, json.correlationId);
    (event as any).eventId = json.eventId;
    (event as any).occurredAt = new Date(json.occurredAt);
    (event as any).version = json.version;
    return event;
  }
}
