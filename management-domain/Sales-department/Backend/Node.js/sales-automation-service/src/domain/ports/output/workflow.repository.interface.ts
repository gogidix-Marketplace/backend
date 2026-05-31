import { Workflow } from '../../models/workflow.entity';

export interface WorkflowRepository {
  save(workflow: Workflow): Promise<Workflow>;
  findById(id: string): Promise<Workflow | null>;
  findByTenantId(tenantId: string, filters?: any): Promise<Workflow[]>;
  findActiveByTenantId(tenantId: string): Promise<Workflow[]>;
  findByTag(tenantId: string, tag: string): Promise<Workflow[]>;
  findByTriggerType(tenantId: string, triggerType: string): Promise<Workflow[]>;
  findAll(filters?: any, tenantId?: string): Promise<Workflow[]>;
  delete(id: string): Promise<void>;
  exists(id: string): Promise<boolean>;
}
