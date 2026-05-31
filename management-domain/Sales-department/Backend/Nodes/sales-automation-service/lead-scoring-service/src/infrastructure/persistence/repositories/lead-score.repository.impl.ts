import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { LeadScoreSchema, LeadScoreDocument } from '../mongo/schemas/lead-score.schema';
import { LeadScore } from '../../../domain/entities/lead-score.entity';
import {
  LeadScoreRepositoryPort,
  PaginationOptions,
  ScoreStatistics,
} from '../../../domain/ports/in/lead-score.repository.port';

@Injectable()
export class LeadScoreRepositoryImpl implements LeadScoreRepositoryPort {
  constructor(
    @InjectModel(LeadScoreSchema.name)
    private readonly leadScoreModel: Model<LeadScoreDocument>,
  ) {}

  async save(leadScore: LeadScore): Promise<LeadScore> {
    const data = leadScore.toPrimitives();
    const doc = await this.leadScoreModel.findOneAndUpdate(
      { _id: data.id },
      data as any,
      { upsert: true, new: true }
    );
    return LeadScore.fromPrimitives(doc.toObject());
  }

  async findById(id: string): Promise<LeadScore | null> {
    const doc = await this.leadScoreModel.findOne({ _id: id }).lean();
    return doc ? LeadScore.fromPrimitives(doc) : null;
  }

  async findByLeadId(leadId: string): Promise<LeadScore | null> {
    const doc = await this.leadScoreModel.findOne({ leadId }).sort({ updatedAt: -1 }).lean();
    return doc ? LeadScore.fromPrimitives(doc) : null;
  }

  async findByTenantId(tenantId: string, options?: PaginationOptions): Promise<LeadScore[]> {
    const query = this.leadScoreModel.find({ tenantId });

    if (options?.sortBy) {
      const sortOrder = options.sortOrder === 'asc' ? 1 : -1;
      query.sort({ [options.sortBy]: sortOrder });
    } else {
      query.sort({ updatedAt: -1 });
    }

    if (options?.page && options?.limit) {
      query.skip((options.page - 1) * options.limit).limit(options.limit);
    } else if (options?.limit) {
      query.limit(options.limit);
    }

    const docs = await query.lean();
    return docs.map(doc => LeadScore.fromPrimitives(doc));
  }

  async findByLeadIdAndTenantId(leadId: string, tenantId: string): Promise<LeadScore | null> {
    const doc = await this.leadScoreModel.findOne({ leadId, tenantId }).lean();
    return doc ? LeadScore.fromPrimitives(doc) : null;
  }

  async findByScoreModelId(scoreModelId: string, options?: PaginationOptions): Promise<LeadScore[]> {
    const query = this.leadScoreModel.find({ scoreModelId });

    if (options?.page && options?.limit) {
      query.skip((options.page - 1) * options.limit).limit(options.limit);
    }

    const docs = await query.lean();
    return docs.map(doc => LeadScore.fromPrimitives(doc));
  }

  async findQualifiedLeads(tenantId: string, minScore: number, options?: PaginationOptions): Promise<LeadScore[]> {
    const query = this.leadScoreModel.find({
      tenantId,
      totalScore: { $gte: minScore }
    }).sort({ totalScore: -1 });

    if (options?.page && options?.limit) {
      query.skip((options.page - 1) * options.limit).limit(options.limit);
    }

    const docs = await query.lean();
    return docs.map(doc => LeadScore.fromPrimitives(doc));
  }

  async findLeadsNeedingRescoring(tenantId: string, thresholdDays: number): Promise<LeadScore[]> {
    const thresholdDate = new Date();
    thresholdDate.setDate(thresholdDate.getDate() - thresholdDays);

    const docs = await this.leadScoreModel.find({
      tenantId,
      lastScoredAt: { $lte: thresholdDate }
    }).lean();

    return docs.map(doc => LeadScore.fromPrimitives(doc));
  }

  async findByGrade(tenantId: string, grade: string, options?: PaginationOptions): Promise<LeadScore[]> {
    const query = this.leadScoreModel.find({ tenantId, grade });

    if (options?.page && options?.limit) {
      query.skip((options.page - 1) * options.limit).limit(options.limit);
    }

    const docs = await query.lean();
    return docs.map(doc => LeadScore.fromPrimitives(doc));
  }

  async findByVariant(tenantId: string, variantId: string, options?: PaginationOptions): Promise<LeadScore[]> {
    const query = this.leadScoreModel.find({ tenantId, variantId });

    if (options?.page && options?.limit) {
      query.skip((options.page - 1) * options.limit).limit(options.limit);
    }

    const docs = await query.lean();
    return docs.map(doc => LeadScore.fromPrimitives(doc));
  }

  async delete(id: string): Promise<void> {
    await this.leadScoreModel.deleteOne({ _id: id });
  }

  async deleteByLeadId(leadId: string): Promise<void> {
    await this.leadScoreModel.deleteMany({ leadId });
  }

  async exists(leadId: string, tenantId: string): Promise<boolean> {
    const count = await this.leadScoreModel.countDocuments({ leadId, tenantId });
    return count > 0;
  }

  async count(tenantId: string): Promise<number> {
    return this.leadScoreModel.countDocuments({ tenantId });
  }

  async countByGrade(tenantId: string, grade: string): Promise<number> {
    return this.leadScoreModel.countDocuments({ tenantId, grade });
  }

  async getScoreStatistics(tenantId: string): Promise<ScoreStatistics> {
    const totalLeads = await this.count(tenantId);

    const aggregationResult = await this.leadScoreModel.aggregate([
      { $match: { tenantId } },
      {
        $group: {
          _id: null,
          averageScore: { $avg: '$totalScore' },
          grades: {
            $push: '$grade'
          },
          scores: {
            $push: '$totalScore'
          }
        }
      }
    ]);

    const result = aggregationResult[0];

    // Grade distribution
    const gradeDistribution: Record<string, number> = {
      A: await this.countByGrade(tenantId, 'A'),
      B: await this.countByGrade(tenantId, 'B'),
      C: await this.countByGrade(tenantId, 'C'),
      D: await this.countByGrade(tenantId, 'D'),
    };

    // Score distribution
    const scoreDistribution: Record<string, number> = {
      '0-20': 0,
      '21-40': 0,
      '41-60': 0,
      '61-80': 0,
      '81-100': 0,
    };

    const allScores = await this.leadScoreModel.find({ tenantId }).select('totalScore').lean();
    allScores.forEach(doc => {
      const score = doc.totalScore;
      if (score <= 20) scoreDistribution['0-20']++;
      else if (score <= 40) scoreDistribution['21-40']++;
      else if (score <= 60) scoreDistribution['41-60']++;
      else if (score <= 80) scoreDistribution['61-80']++;
      else scoreDistribution['81-100']++;
    });

    // Top performers
    const topPerformers = await this.leadScoreModel
      .find({ tenantId })
      .sort({ totalScore: -1 })
      .limit(10)
      .select('leadId totalScore')
      .lean();

    return {
      totalLeads,
      averageScore: result?.averageScore ?? 0,
      scoreDistribution,
      gradeDistribution,
      topPerformers: topPerformers.map(p => ({
        leadId: p.leadId,
        score: p.totalScore
      }))
    };
  }

  async updateMany(leadScores: LeadScore[]): Promise<void> {
    const bulkOps = leadScores.map(score => {
      const data = score.toPrimitives();
      return {
        updateOne: {
          filter: { _id: data.id },
          update: { $set: data as any },
          upsert: true
        }
      };
    });

    if (bulkOps.length > 0) {
      await this.leadScoreModel.bulkWrite(bulkOps);
    }
  }

  async getScoreTrends(tenantId: string, leadId?: string, days?: number, granularity?: string): Promise<any[]> {
    const match: any = { tenantId };
    if (leadId) match.leadId = leadId;
    const since = new Date();
    since.setDate(since.getDate() - (days ?? 30));
    match.updatedAt = { $gte: since };
    return this.leadScoreModel.aggregate([
      { $match: match },
      { $group: { _id: '$updatedAt', averageScore: { $avg: '$totalScore' }, count: { $sum: 1 } } },
      { $sort: { _id: 1 } },
    ]);
  }

  async getScoreHistory(leadId: string, tenantId: string, limit?: number): Promise<any[]> {
    return this.leadScoreModel
      .find({ leadId, tenantId })
      .sort({ updatedAt: -1 })
      .limit(limit ?? 50)
      .lean();
  }
}

