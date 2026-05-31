import { v4 as uuidv4 } from 'uuid';
import { DomainEvent } from './base-event.interface';
import { TriggerType } from '../enums/trigger-type.enum';

export interface AutomationTriggeredData {
  ruleId: string;
  ruleName: string;
  triggerType: TriggerType;
  entityType?: string;
  entityId?: string;
  triggerData: Record<string, any>;
  userId?: string;
}

export class AutomationTriggeredEvent implements DomainEvent {
  readonly eventType = 'AutomationTriggered';
  readonly eventId: string;
  readonly occurredAt: Date;
  readonly aggregateId: string;
  readonly aggregateType = 'AutomationRule';
  readonly correlationId?: string;
  readonly tenantId: string;
  readonly version: number;
  readonly data: AutomationTriggeredData;

  constructor(data: AutomationTriggeredData, tenantId: string, correlationId?: string) {
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
    triggerType: TriggerType,
    triggerData: Record<string, any>,
    tenantId: string,
    entityType?: string,
    entityId?: string,
    userId?: string,
    correlationId?: string,
  ): AutomationTriggeredEvent {
    return new AutomationTriggeredEvent(
      {
        ruleId,
        ruleName,
        triggerType,
        entityType,
        entityId,
        triggerData,
        userId,
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

  static fromJSON(json: Record<string, any>): AutomationTriggeredEvent {
    const event = new AutomationTriggeredEvent(
      json.data,
      json.tenantId,
      json.correlationId,
    );
    (event as any).eventId = json.eventId;
    (event as any).occurredAt = new Date(json.occurredAt);
    (event as any).version = json.version;
    return event;
  }
}
