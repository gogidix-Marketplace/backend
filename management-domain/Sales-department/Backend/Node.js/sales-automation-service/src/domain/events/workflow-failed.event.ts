import { v4 as uuidv4 } from 'uuid';
import { DomainEvent } from './base-event.interface';

export interface WorkflowFailedData {
  workflowId: string;
  workflowName: string;
  executionId: string;
  failedAt: Date;
  reason: string;
  errorType: string;
  stackTrace?: string;
  actionIndex?: number;
  actionName?: string;
  triggeredBy: {
    type: string;
    data: Record<string, any>;
  };
}

export class WorkflowFailedEvent implements DomainEvent {
  readonly eventType = 'WorkflowFailed';
  readonly eventId: string;
  readonly occurredAt: Date;
  readonly aggregateId: string;
  readonly aggregateType = 'Workflow';
  readonly correlationId?: string;
  readonly tenantId: string;
  readonly version: number;
  readonly data: WorkflowFailedData;

  constructor(data: WorkflowFailedData, tenantId: string, correlationId?: string) {
    this.eventId = uuidv4();
    this.occurredAt = new Date();
    this.aggregateId = data.workflowId;
    this.tenantId = tenantId;
    this.correlationId = correlationId;
    this.version = 1;
    this.data = data;
  }

  static fromWorkflow(
    workflowId: string,
    workflowName: string,
    executionId: string,
    reason: string,
    errorType: string,
    triggeredBy: { type: string; data: Record<string, any> },
    tenantId: string,
    actionIndex?: number,
    actionName?: string,
    stackTrace?: string,
    correlationId?: string,
  ): WorkflowFailedEvent {
    return new WorkflowFailedEvent(
      {
        workflowId,
        workflowName,
        executionId,
        failedAt: new Date(),
        reason,
        errorType,
        stackTrace,
        actionIndex,
        actionName,
        triggeredBy,
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
      data: {
        ...this.data,
        failedAt: this.data.failedAt.toISOString(),
      },
    };
  }

  static fromJSON(json: Record<string, any>): WorkflowFailedEvent {
    const data = {
      ...json.data,
      failedAt: new Date(json.data.failedAt),
    };
    const event = new WorkflowFailedEvent(data, json.tenantId, json.correlationId);
    (event as any).eventId = json.eventId;
    (event as any).occurredAt = new Date(json.occurredAt);
    (event as any).version = json.version;
    return event;
  }
}
