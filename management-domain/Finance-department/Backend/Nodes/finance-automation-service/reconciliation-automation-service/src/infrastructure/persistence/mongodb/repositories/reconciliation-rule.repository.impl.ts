import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { ReconciliationRule, ReconciliationRuleProps } from '../../../../domain/models/reconciliation-rule.entity';
import { IReconciliationRuleRepository, RuleFindOptions } from '../../../../domain/repositories/reconciliation-repository.interface';
import { ReconciliationRuleDocument, ReconciliationRuleSchema } from '../mongoose/reconciliation-rule.schema';
import { MatchType } from '../../../../domain/enums/match-type.enum';

@Injectable()
export class ReconciliationRuleRepository implements IReconciliationRuleRepository {
  constructor(
    @InjectModel('ReconciliationRule')
    private readonly ruleModel: Model<ReconciliationRuleDocument>,
  ) {}

  async save(rule: ReconciliationRule): Promise<ReconciliationRule> {
    const doc = new this.ruleModel(rule.toJSON());
    const saved = await doc.save();
    return this.documentToEntity(saved);
  }

  async findById(id: string, tenantId: string): Promise<ReconciliationRule | null> {
    const doc = await this.ruleModel.findOne({ _id: id, tenantId });
    return doc ? this.documentToEntity(doc) : null;
  }

  async findByTenant(tenantId: string, options?: RuleFindOptions): Promise<ReconciliationRule[]> {
    const query: any = { tenantId };

    if (options?.enabledOnly) {
      query.enabled = true;
    }

    if (options?.matchType) {
      query.matchType = options.matchType;
    }

    let docs = await this.ruleModel.find(query).sort({ priority: -1 });

    if (options?.page && options?.limit) {
      docs = docs.slice((options.page - 1) * options.limit, options.page * options.limit);
    }

    return docs.map((doc) => this.documentToEntity(doc));
  }

  async findActive(tenantId: string): Promise<ReconciliationRule[]> {
    const docs = await this.ruleModel.find({ tenantId, enabled: true }).sort({ priority: -1 }).exec();
    return docs.map((doc) => this.documentToEntity(doc));
  }

  async findByMatchType(matchType: MatchType, tenantId: string): Promise<ReconciliationRule[]> {
    const docs = await this.ruleModel.find({ tenantId, matchType }).exec();
    return docs.map((doc) => this.documentToEntity(doc));
  }

  async delete(id: string, tenantId: string): Promise<void> {
    await this.ruleModel.deleteOne({ _id: id, tenantId });
  }

  async update(rule: ReconciliationRule): Promise<ReconciliationRule> {
    const updated = await this.ruleModel
      .findOneAndUpdate({ _id: rule.id, tenantId: rule.tenantId }, rule.toJSON(), { new: true })
      .exec();

    if (!updated) {
      throw new Error('Rule not found');
    }

    return this.documentToEntity(updated);
  }

  async enable(id: string, tenantId: string): Promise<void> {
    await this.ruleModel.updateOne({ _id: id, tenantId }, { enabled: true });
  }

  async disable(id: string, tenantId: string): Promise<void> {
    await this.ruleModel.updateOne({ _id: id, tenantId }, { enabled: false });
  }

  private documentToEntity(doc: ReconciliationRuleDocument): ReconciliationRule {
    const props: ReconciliationRuleProps = {
      id: doc._id.toString() as any,
      tenantId: doc.tenantId,
      name: doc.name,
      description: doc.description,
      matchType: doc.matchType as MatchType,
      priority: doc.priority,
      enabled: doc.enabled,
      conditions: doc.conditions as any,
      actions: doc.actions as any,
      confidenceThreshold: doc.confidenceThreshold,
      createdBy: doc.createdBy,
      createdAt: (doc as any).createdAt || new Date(),
      updatedAt: (doc as any).updatedAt || new Date(),
    };

    return new ReconciliationRule(props);
  }
}
