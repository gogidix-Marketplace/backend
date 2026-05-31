import { v4 as uuidv4 } from 'uuid';
import { DomainEvent } from './base-event.interface';

export interface LeadScoredData {
  leadId: string;
  leadEmail?: string;
  previousScore: number;
  newScore: number;
  scoreChange: number;
  reason: string;
  ruleId?: string;
  ruleName?: string;
  scoredBy: string;
  category?: 'demographic' | 'behavioral' | 'engagement' | 'manual';
}

export class LeadScoredEvent implements DomainEvent {
  readonly eventType = 'LeadScored';
  readonly eventId: string;
  readonly occurredAt: Date;
  readonly aggregateId: string;
  readonly aggregateType = 'Lead';
  readonly correlationId?: string;
  readonly tenantId: string;
  readonly version: number;
  readonly data: LeadScoredData;

  constructor(data: LeadScoredData, tenantId: string, correlationId?: string) {
    this.eventId = uuidv4();
    this.occurredAt = new Date();
    this.aggregateId = data.leadId;
    this.tenantId = tenantId;
    this.correlationId = correlationId;
    this.version = 1;
    this.data = data;
  }

  static fromLead(
    leadId: string,
    previousScore: number,
    newScore: number,
    reason: string,
    tenantId: string,
    scoredBy: string,
    leadEmail?: string,
    ruleId?: string,
    ruleName?: string,
    category?: 'demographic' | 'behavioral' | 'engagement' | 'manual',
    correlationId?: string,
  ): LeadScoredEvent {
    return new LeadScoredEvent(
      {
        leadId,
        leadEmail,
        previousScore,
        newScore,
        scoreChange: newScore - previousScore,
        reason,
        ruleId,
        ruleName,
        scoredBy,
        category,
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

  static fromJSON(json: Record<string, any>): LeadScoredEvent {
    const event = new LeadScoredEvent(json.data, json.tenantId, json.correlationId);
    (event as any).eventId = json.eventId;
    (event as any).occurredAt = new Date(json.occurredAt);
    (event as any).version = json.version;
    return event;
  }
}
