import { v4 as uuidv4 } from 'uuid';
import { DomainEvent } from './base-event.interface';

export interface DealStageChangedData {
  dealId: string;
  dealName?: string;
  dealValue?: number;
  previousStage: string;
  newStage: string;
  changedBy: string;
  reason?: string;
  ruleId?: string;
  ruleName?: string;
  durationInPreviousStage?: number;
  automatedRoute?: boolean;
}

export class DealStageChangedEvent implements DomainEvent {
  readonly eventType = 'DealStageChanged';
  readonly eventId: string;
  readonly occurredAt: Date;
  readonly aggregateId: string;
  readonly aggregateType = 'Deal';
  readonly correlationId?: string;
  readonly tenantId: string;
  readonly version: number;
  readonly data: DealStageChangedData;

  constructor(data: DealStageChangedData, tenantId: string, correlationId?: string) {
    this.eventId = uuidv4();
    this.occurredAt = new Date();
    this.aggregateId = data.dealId;
    this.tenantId = tenantId;
    this.correlationId = correlationId;
    this.version = 1;
    this.data = data;
  }

  static fromDeal(
    dealId: string,
    previousStage: string,
    newStage: string,
    changedBy: string,
    tenantId: string,
    dealName?: string,
    dealValue?: number,
    reason?: string,
    ruleId?: string,
    ruleName?: string,
    durationInPreviousStage?: number,
    automatedRoute?: boolean,
    correlationId?: string,
  ): DealStageChangedEvent {
    return new DealStageChangedEvent(
      {
        dealId,
        dealName,
        dealValue,
        previousStage,
        newStage,
        changedBy,
        reason,
        ruleId,
        ruleName,
        durationInPreviousStage,
        automatedRoute,
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

  static fromJSON(json: Record<string, any>): DealStageChangedEvent {
    const event = new DealStageChangedEvent(json.data, json.tenantId, json.correlationId);
    (event as any).eventId = json.eventId;
    (event as any).occurredAt = new Date(json.occurredAt);
    (event as any).version = json.version;
    return event;
  }
}
