import { v4 as uuidv4 } from 'uuid';
import { DomainEvent } from './base-event.interface';

export interface RuleUpdatedData {
  ruleId: string;
  ruleName: string;
  updatedFields: string[];
  updatedBy: string;
  previousValues?: Record<string, any>;
  newValues?: Record<string, any>;
}

export class RuleUpdatedEvent implements DomainEvent {
  readonly eventType = 'RuleUpdated';
  readonly eventId: string;
  readonly occurredAt: Date;
  readonly aggregateId: string;
  readonly aggregateType = 'AutomationRule';
  readonly correlationId?: string;
  readonly tenantId: string;
  readonly version: number;
  readonly data: RuleUpdatedData;

  constructor(data: RuleUpdatedData, tenantId: string, correlationId?: string) {
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
    updatedFields: string[],
    updatedBy: string,
    tenantId: string,
    previousValues?: Record<string, any>,
    newValues?: Record<string, any>,
    correlationId?: string,
  ): RuleUpdatedEvent {
    return new RuleUpdatedEvent(
      {
        ruleId,
        ruleName,
        updatedFields,
        updatedBy,
        previousValues,
        newValues,
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

  static fromJSON(json: Record<string, any>): RuleUpdatedEvent {
    const event = new RuleUpdatedEvent(json.data, json.tenantId, json.correlationId);
    (event as any).eventId = json.eventId;
    (event as any).occurredAt = new Date(json.occurredAt);
    (event as any).version = json.version;
    return event;
  }
}
