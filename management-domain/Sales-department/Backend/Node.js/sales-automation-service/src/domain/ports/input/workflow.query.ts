import { Workflow } from '../../models/workflow.entity';

export interface WorkflowQuery {
  findById(workflowId: string): Promise<Workflow | null>;
  findByTenantId(tenantId: string, filters?: any): Promise<Workflow[]>;
  findActiveByTenantId(tenantId: string): Promise<Workflow[]>;
  findByTriggerType(tenantId: string, triggerType: string): Promise<Workflow[]>;
  findByTag(tenantId: string, tag: string): Promise<Workflow[]>;
  search(filters: any): Promise<Workflow[]>;
  getExecutionHistory(workflowId: string, limit?: number): Promise<Workflow.WorkflowExecutionResult[]>;
  getExecutionStats(workflowId: string): Promise<{
    total: number;
    successful: number;
    failed: number;
    averageDuration?: number;
  }>;
}
