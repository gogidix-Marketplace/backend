import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { IScoringRuleRepository } from '../../../domain/ports/repositories/scoring-rule.repository';
import { ScoringRule } from '../../../domain/models/scoring-rule';
import { ScoringRuleDocument } from '../schemas/scoring-rule.schema';

@Injectable()
export class ScoringRuleRepositoryImpl implements IScoringRuleRepository {
  constructor(@InjectModel(ScoringRuleDocument.name) private readonly model: Model<ScoringRuleDocument>) {}

  async save(rule: ScoringRule): Promise<ScoringRule> {
    const props = rule.toPlainObject();
    const doc = new this.model(props);
    const saved = await doc.save();
    return new ScoringRule({ ...saved.toObject(), id: saved._id.toString() });
  }

  async findById(id: string): Promise<ScoringRule | null> {
    const doc = await this.model.findById(id).exec();
    if (!doc) return null;
    return new ScoringRule({ ...doc.toObject(), id: doc._id.toString() });
  }

  async findByTenantId(tenantId: string, options?: { category?: string; isActive?: boolean }): Promise<ScoringRule[]> {
    const filter: any = { tenantId };
    if (options?.category) filter.category = options.category;
    if (options?.isActive !== undefined) filter.isActive = options.isActive;
    const docs = await this.model.find(filter).sort({ priority: -1 }).exec();
    return docs.map(doc => new ScoringRule({ ...doc.toObject(), id: doc._id.toString() }));
  }

  async update(rule: ScoringRule): Promise<ScoringRule> {
    const props = rule.toPlainObject();
    const doc = await this.model.findByIdAndUpdate(props.id, props, { new: true }).exec();
    if (!doc) return rule;
    return new ScoringRule({ ...doc.toObject(), id: doc._id.toString() });
  }

  async delete(id: string): Promise<boolean> {
    const result = await this.model.findByIdAndDelete(id).exec();
    return !!result;
  }
}
