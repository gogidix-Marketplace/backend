import { Workflow } from '../../../domain/models/workflow.entity';

export class WorkflowResponseDto {
  id: string;
  name: string;
  description: string;
  status: string;
  trigger: any;
  actions: any[];
  executionCount: number;
  lastExecutedAt?: Date;
  lastExecutionStatus?: string;
  tags: string[];
  priority: number;
  isEnabled: boolean;
  maxConcurrentExecutions: number;
  timeoutMs?: number;
  createdAt: Date;
  updatedAt: Date;
  tenantId: string;

  static fromEntity(workflow: Workflow): WorkflowResponseDto {
    return {
      id: workflow.id,
      name: workflow.name,
      description: workflow.description,
      status: workflow.status,
      trigger: workflow.trigger.toJSON(),
      actions: workflow.actions.map(a => a.toJSON()),
      executionCount: workflow.executionCount,
      lastExecutedAt: workflow.lastExecutedAt,
      lastExecutionStatus: workflow.lastExecutionStatus,
      tags: workflow.tags,
      priority: workflow.priority,
      isEnabled: workflow.isEnabled,
      maxConcurrentExecutions: workflow.maxConcurrentExecutions,
      timeoutMs: workflow.timeoutMs,
      createdAt: workflow.createdAt,
      updatedAt: workflow.updatedAt,
      tenantId: workflow.tenantId,
    };
  }

  static fromEntities(workflows: Workflow[]): WorkflowResponseDto[] {
    return workflows.map(workflow => WorkflowResponseDto.fromEntity(workflow));
  }
}

export class WorkflowExecutionResponseDto {
  executionId: string;
  workflowId: string;
  status: string;
  startedAt: Date;
  completedAt?: Date;
  results: Array<{
    actionId: string;
    actionName: string;
    status: string;
    error?: string;
  }>;
  error?: string;
  duration?: number;

  static fromResult(result: any): WorkflowExecutionResponseDto {
    return {
      executionId: result.executionId,
      workflowId: result.workflowId || '',
      status: result.status,
      startedAt: result.startedAt,
      completedAt: result.completedAt,
      results: result.results || [],
      error: result.error,
      duration: result.duration,
    };
  }
}
