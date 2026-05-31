import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { AutomationRule } from '../../../../domain/models/automation-rule.entity';
import { AutomationRuleRepository } from '../../../../domain/ports/output/automation-rule.repository.interface';
import { AutomationRuleDocument, AutomationRuleSchema } from './automation-rule.schema';

@Injectable()
export class MongoAutomationRuleRepository implements AutomationRuleRepository {
  constructor(
    @InjectModel(AutomationRuleSchema.name)
    private readonly automationRuleModel: Model<AutomationRuleDocument>,
  ) {}

  async save(rule: AutomationRule): Promise<AutomationRule> {
    const existing = await this.automationRuleModel.findById(rule.id);

    if (existing) {
      await this.automationRuleModel.updateOne({ _id: rule.id }, rule.toJSON());
      return rule;
    }

    const created = new this.automationRuleModel(rule.toJSON());
    await created.save();
    return rule;
  }

  async findById(id: string): Promise<AutomationRule | null> {
    const doc = await this.automationRuleModel.findById(id).exec();

    if (!doc) {
      return null;
    }

    return AutomationRule.fromJSON(doc.toJSON());
  }

  async findByTenantId(tenantId: string, filters?: any): Promise<AutomationRule[]> {
    const query: any = { tenantId };

    if (filters?.status) {
      query.status = filters.status;
    }

    if (filters?.category) {
      query.category = filters.category;
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

    const docs = await this.automationRuleModel.find(query).exec();
    return docs.map(doc => AutomationRule.fromJSON(doc.toJSON()));
  }

  async findActiveByTenantId(tenantId: string): Promise<AutomationRule[]> {
    const docs = await this.automationRuleModel
      .find({ tenantId, status: 'ACTIVE', isEnabled: true })
      .exec();

    return docs.map(doc => AutomationRule.fromJSON(doc.toJSON()));
  }

  async findByCategory(tenantId: string, category: string): Promise<AutomationRule[]> {
    const docs = await this.automationRuleModel
      .find({ tenantId, category })
      .exec();

    return docs.map(doc => AutomationRule.fromJSON(doc.toJSON()));
  }

  async findByTag(tenantId: string, tag: string): Promise<AutomationRule[]> {
    const docs = await this.automationRuleModel
      .find({ tenantId, tags: tag })
      .exec();

    return docs.map(doc => AutomationRule.fromJSON(doc.toJSON()));
  }

  async findAll(filters?: any, tenantId?: string): Promise<AutomationRule[]> {
    const query: any = {};

    if (tenantId) {
      query.tenantId = tenantId;
    }

    if (filters) {
      if (filters.status) query.status = filters.status;
      if (filters.category) query.category = filters.category;
      if (filters.isEnabled !== undefined) query.isEnabled = filters.isEnabled;
    }

    const docs = await this.automationRuleModel.find(query).exec();
    return docs.map(doc => AutomationRule.fromJSON(doc.toJSON()));
  }

  async delete(id: string): Promise<void> {
    await this.automationRuleModel.deleteOne({ _id: id }).exec();
  }

  async exists(id: string): Promise<boolean> {
    const count = await this.automationRuleModel.countDocuments({ _id: id }).exec();
    return count > 0;
  }
}
