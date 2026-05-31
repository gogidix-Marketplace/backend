import { Injectable, Logger } from '@nestjs/common';
import { Workflow } from '../../domain/models/workflow.entity';
import { WorkflowRepository } from '../../domain/ports/output';
import { WorkflowNotFoundException } from '@shared/exceptions';

@Injectable()
export class WorkflowQueryService {
  private readonly logger = new Logger(WorkflowQueryService.name);

  constructor(
    private readonly workflowRepository: WorkflowRepository,
  ) {}

  async findById(workflowId: string): Promise<Workflow> {
    this.logger.debug(`Finding workflow by ID: ${workflowId}`);

    const workflow = await this.workflowRepository.findById(workflowId);
    if (!workflow) {
      throw new WorkflowNotFoundException(workflowId);
    }

    return workflow;
  }

  async findByTenantId(tenantId: string, filters?: any): Promise<Workflow[]> {
    this.logger.debug(`Finding workflows for tenant: ${tenantId}`);

    return this.workflowRepository.findByTenantId(tenantId, filters);
  }

  async findActiveByTenantId(tenantId: string): Promise<Workflow[]> {
    this.logger.debug(`Finding active workflows for tenant: ${tenantId}`);

    return this.workflowRepository.findActiveByTenantId(tenantId);
  }

  async findByTriggerType(tenantId: string, triggerType: string): Promise<Workflow[]> {
    this.logger.debug(`Finding workflows by trigger type: ${triggerType} for tenant: ${tenantId}`);

    return this.workflowRepository.findByTriggerType(tenantId, triggerType);
  }

  async findByTag(tenantId: string, tag: string): Promise<Workflow[]> {
    this.logger.debug(`Finding workflows by tag: ${tag} for tenant: ${tenantId}`);

    return this.workflowRepository.findByTag(tenantId, tag);
  }

  async search(filters: any): Promise<Workflow[]> {
    this.logger.debug(`Searching workflows with filters: ${JSON.stringify(filters)}`);

    return this.workflowRepository.findAll(filters, filters.tenantId);
  }

  async getExecutionHistory(workflowId: string, limit: number = 10): Promise<Workflow.WorkflowExecutionResult[]> {
    this.logger.debug(`Getting execution history for workflow: ${workflowId}`);

    const workflow = await this.findById(workflowId);
    return workflow.getRecentExecutions(limit);
  }

  async getExecutionStats(workflowId: string): Promise<{
    total: number;
    successful: number;
    failed: number;
    averageDuration?: number;
  }> {
    this.logger.debug(`Getting execution stats for workflow: ${workflowId}`);

    const workflow = await this.findById(workflowId);
    return workflow.getExecutionStats();
  }
}
