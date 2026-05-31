import { Workflow } from '../../models/workflow.entity';
import { ExecutionContext } from '@shared/context';

export interface CreateWorkflowCommand {
  name: string;
  description?: string;
  trigger: {
    name: string;
    configuration: {
      type: string;
      conditions?: Array<{
        field: string;
        operator: string;
        value: any;
      }>;
      entityTypes?: string[];
      cronExpression?: string;
      filters?: Record<string, any>;
    };
  };
  actions: Array<{
    name: string;
    type: string;
    parameters: Record<string, any>;
    order: number;
    continueOnError?: boolean;
    delayMs?: number;
  }>;
  tags?: string[];
  priority?: number;
  maxConcurrentExecutions?: number;
  timeoutMs?: number;
}

export interface UpdateWorkflowCommand {
  workflowId: string;
  name?: string;
  description?: string;
  actions?: CreateWorkflowCommand['actions'];
  tags?: string[];
  priority?: number;
  maxConcurrentExecutions?: number;
  timeoutMs?: number;
}

export interface DeleteWorkflowCommand {
  workflowId: string;
}

export interface ActivateWorkflowCommand {
  workflowId: string;
}

export interface DeactivateWorkflowCommand {
  workflowId: string;
}

export interface ExecuteWorkflowCommand {
  workflowId: string;
  triggerData: Record<string, any>;
  entityId?: string;
  entityType?: string;
  userId?: string;
  correlationId?: string;
}

export interface WorkflowCommand {
  create(command: CreateWorkflowCommand, context: ExecutionContext): Promise<Workflow>;
  update(command: UpdateWorkflowCommand, context: ExecutionContext): Promise<Workflow>;
  delete(command: DeleteWorkflowCommand, context: ExecutionContext): Promise<void>;
  activate(command: ActivateWorkflowCommand, context: ExecutionContext): Promise<Workflow>;
  deactivate(command: DeactivateWorkflowCommand, context: ExecutionContext): Promise<Workflow>;
  execute(command: ExecuteWorkflowCommand, context: ExecutionContext): Promise<Workflow.WorkflowExecutionResult>;
}
