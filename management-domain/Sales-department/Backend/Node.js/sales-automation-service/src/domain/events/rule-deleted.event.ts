import { v4 as uuidv4 } from 'uuid';
import { DomainEvent } from './base-event.interface';

export interface RuleDeletedData {
  ruleId: string;
  ruleName: string;
  deletedBy: string;
  deletionReason?: string;
  wasActive: boolean;
}

export class RuleDeletedEvent implements DomainEvent {
  readonly eventType = 'RuleDeleted';
  readonly eventId: string;
  readonly occurredAt: Date;
  readonly aggregateId: string;
  readonly aggregateType = 'AutomationRule';
  readonly correlationId?: string;
  readonly tenantId: string;
  readonly version: number;
  readonly data: RuleDeletedData;

  constructor(data: RuleDeletedData, tenantId: string, correlationId?: string) {
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
    deletedBy: string,
    tenantId: string,
    wasActive: boolean,
    deletionReason?: string,
    correlationId?: string,
  ): RuleDeletedEvent {
    return new RuleDeletedEvent(
      {
        ruleId,
        ruleName,
        deletedBy,
        deletionReason,
        wasActive,
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

  static fromJSON(json: Record<string, any>): RuleDeletedEvent {
    const event = new RuleDeletedEvent(json.data, json.tenantId, json.correlationId);
    (event as any).eventId = json.eventId;
    (event as any).occurredAt = new Date(json.occurredAt);
    (event as any).version = json.version;
    return event;
  }
}
