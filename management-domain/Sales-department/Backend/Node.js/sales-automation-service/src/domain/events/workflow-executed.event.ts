import { v4 as uuidv4 } from 'uuid';
import { DomainEvent } from './base-event.interface';
import { WorkflowExecutionStatus } from '../enums/workflow-execution-status.enum';

export interface WorkflowExecutedData {
  workflowId: string;
  workflowName: string;
  executionId: string;
  status: WorkflowExecutionStatus;
  startedAt: Date;
  completedAt?: Date;
  triggeredBy: {
    type: string;
    data: Record<string, any>;
  };
  results: Array<{
    actionId: string;
    actionName: string;
    status: WorkflowExecutionStatus;
    error?: string;
  }>;
  error?: string;
  duration?: number;
}

export class WorkflowExecutedEvent implements DomainEvent {
  readonly eventType = 'WorkflowExecuted';
  readonly eventId: string;
  readonly occurredAt: Date;
  readonly aggregateId: string;
  readonly aggregateType = 'Workflow';
  readonly correlationId?: string;
  readonly tenantId: string;
  readonly version: number;
  readonly data: WorkflowExecutedData;

  constructor(data: WorkflowExecutedData, tenantId: string, correlationId?: string) {
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
    status: WorkflowExecutionStatus,
    triggeredBy: { type: string; data: Record<string, any> },
    results: Array<{
      actionId: string;
      actionName: string;
      status: WorkflowExecutionStatus;
      error?: string;
    }>,
    tenantId: string,
    startedAt: Date,
    completedAt?: Date,
    error?: string,
    correlationId?: string,
  ): WorkflowExecutedEvent {
    const duration = completedAt
      ? completedAt.getTime() - startedAt.getTime()
      : undefined;

    return new WorkflowExecutedEvent(
      {
        workflowId,
        workflowName,
        executionId,
        status,
        startedAt,
        completedAt,
        triggeredBy,
        results,
        error,
        duration,
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
        startedAt: this.data.startedAt.toISOString(),
        completedAt: this.data.completedAt?.toISOString(),
      },
    };
  }

  static fromJSON(json: Record<string, any>): WorkflowExecutedEvent {
    const data = {
      ...json.data,
      startedAt: new Date(json.data.startedAt),
      completedAt: json.data.completedAt ? new Date(json.data.completedAt) : undefined,
    };
    const event = new WorkflowExecutedEvent(data, json.tenantId, json.correlationId);
    (event as any).eventId = json.eventId;
    (event as any).occurredAt = new Date(json.occurredAt);
    (event as any).version = json.version;
    return event;
  }
}
