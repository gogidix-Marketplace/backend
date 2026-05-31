import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Workflow } from '../../../../domain/models/workflow.entity';
import { WorkflowRepository } from '../../../../domain/ports/output/workflow.repository.interface';
import { WorkflowDocument, WorkflowSchema } from './workflow.schema';

@Injectable()
export class MongoWorkflowRepository implements WorkflowRepository {
  constructor(
    @InjectModel(WorkflowSchema.name)
    private readonly workflowModel: Model<WorkflowDocument>,
  ) {}

  async save(workflow: Workflow): Promise<Workflow> {
    const existing = await this.workflowModel.findById(workflow.id);

    if (existing) {
      await this.workflowModel.updateOne({ _id: workflow.id }, workflow.toJSON());
      return workflow;
    }

    const created = new this.workflowModel(workflow.toJSON());
    await created.save();
    return workflow;
  }

  async findById(id: string): Promise<Workflow | null> {
    const doc = await this.workflowModel.findById(id).exec();

    if (!doc) {
      return null;
    }

    return Workflow.fromJSON(doc.toJSON());
  }

  async findByTenantId(tenantId: string, filters?: any): Promise<Workflow[]> {
    const query: any = { tenantId };

    if (filters?.status) {
      query.status = filters.status;
    }

    if (filters?.isEnabled !== undefined) {
      query.isEnabled = filters.isEnabled;
    }

    if (filters?.tags && filters.tags.length > 0) {
      query.tags = { $in: filters.tags };
    }

    if (filters?.search) {
      query.$or = [
        { name: { $regex: filters.search, $options: 'i' } },
        { description: { $regex: filters.search, $options: 'i' } },
      ];
    }

    const docs = await this.workflowModel.find(query).exec();
    return docs.map(doc => Workflow.fromJSON(doc.toJSON()));
  }

  async findActiveByTenantId(tenantId: string): Promise<Workflow[]> {
    const docs = await this.workflowModel
      .find({ tenantId, status: 'ACTIVE', isEnabled: true })
      .exec();

    return docs.map(doc => Workflow.fromJSON(doc.toJSON()));
  }

  async findByTriggerType(tenantId: string, triggerType: string): Promise<Workflow[]> {
    const docs = await this.workflowModel
      .find({ tenantId, 'trigger.configuration.type': triggerType })
      .exec();

    return docs.map(doc => Workflow.fromJSON(doc.toJSON()));
  }

  async findByTag(tenantId: string, tag: string): Promise<Workflow[]> {
    const docs = await this.workflowModel
      .find({ tenantId, tags: tag })
      .exec();

    return docs.map(doc => Workflow.fromJSON(doc.toJSON()));
  }

  async findAll(filters?: any, tenantId?: string): Promise<Workflow[]> {
    const query: any = {};

    if (tenantId) {
      query.tenantId = tenantId;
    }

    if (filters) {
      if (filters.status) query.status = filters.status;
      if (filters.isEnabled !== undefined) query.isEnabled = filters.isEnabled;
    }

    const docs = await this.workflowModel.find(query).exec();
    return docs.map(doc => Workflow.fromJSON(doc.toJSON()));
  }

  async delete(id: string): Promise<void> {
    await this.workflowModel.deleteOne({ _id: id }).exec();
  }

  async exists(id: string): Promise<boolean> {
    const count = await this.workflowModel.countDocuments({ _id: id }).exec();
    return count > 0;
  }
}
