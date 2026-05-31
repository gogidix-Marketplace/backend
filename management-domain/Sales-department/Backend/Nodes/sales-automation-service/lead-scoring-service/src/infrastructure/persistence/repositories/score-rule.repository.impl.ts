import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { ScoreRuleSchema, ScoreRuleDocument } from '../mongo/schemas/score-rule.schema';
import { ScoreRule } from '../../../domain/entities/score-rule.entity';
import {
  ScoreRuleRepositoryPort,
  PaginationOptions,
} from '../../../domain/ports/in/score-rule.repository.port';

@Injectable()
export class ScoreRuleRepositoryImpl implements ScoreRuleRepositoryPort {
  constructor(
    @InjectModel(ScoreRuleSchema.name)
    private readonly scoreRuleModel: Model<ScoreRuleDocument>,
  ) {}

  async save(scoreRule: ScoreRule): Promise<ScoreRule> {
    const data = scoreRule.toPrimitives();
    const doc = await this.scoreRuleModel.findOneAndUpdate(
      { _id: data.id },
      data as any,
      { upsert: true, new: true }
    );
    return ScoreRule.fromPrimitives(doc.toObject());
  }

  async findById(id: string): Promise<ScoreRule | null> {
    const doc = await this.scoreRuleModel.findOne({ _id: id }).lean();
    return doc ? ScoreRule.fromPrimitives(doc) : null;
  }

  async findByTenantId(tenantId: string, options?: PaginationOptions): Promise<ScoreRule[]> {
    const query = this.scoreRuleModel.find({ tenantId });

    if (options?.sortBy) {
      const sortOrder = options.sortOrder === 'asc' ? 1 : -1;
      query.sort({ [options.sortBy]: sortOrder });
    } else {
      query.sort({ priority: -1, createdAt: -1 });
    }

    if (options?.page && options?.limit) {
      query.skip((options.page - 1) * options.limit).limit(options.limit);
    } else if (options?.limit) {
      query.limit(options.limit);
    }

    const docs = await query.lean();
    return docs.map(doc => ScoreRule.fromPrimitives(doc));
  }

  async findByScoreModelId(scoreModelId: string): Promise<ScoreRule[]> {
    const docs = await this.scoreRuleModel
      .find({ scoreModelId })
      .sort({ priority: -1, createdAt: -1 })
      .lean();
    return docs.map(doc => ScoreRule.fromPrimitives(doc));
  }

  async findByCategory(tenantId: string, category: string): Promise<ScoreRule[]> {
    const docs = await this.scoreRuleModel
      .find({ tenantId, category })
      .sort({ priority: -1 })
      .lean();
    return docs.map(doc => ScoreRule.fromPrimitives(doc));
  }

  async findActiveByScoreModelId(scoreModelId: string): Promise<ScoreRule[]> {
    const docs = await this.scoreRuleModel
      .find({ scoreModelId, isActive: true })
      .sort({ priority: -1 })
      .lean();
    return docs.map(doc => ScoreRule.fromPrimitives(doc));
  }

  async delete(id: string): Promise<void> {
    await this.scoreRuleModel.deleteOne({ _id: id });
  }

  async deleteByScoreModelId(scoreModelId: string): Promise<void> {
    await this.scoreRuleModel.deleteMany({ scoreModelId });
  }

  async exists(id: string, tenantId: string): Promise<boolean> {
    const count = await this.scoreRuleModel.countDocuments({ _id: id, tenantId });
    return count > 0;
  }

  async count(tenantId: string): Promise<number> {
    return this.scoreRuleModel.countDocuments({ tenantId });
  }

  async countByScoreModelId(scoreModelId: string): Promise<number> {
    return this.scoreRuleModel.countDocuments({ scoreModelId });
  }
}

