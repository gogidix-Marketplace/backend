import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { ILeadScoreRepository } from '../../../domain/ports/repositories/lead-score.repository';
import { LeadScore } from '../../../domain/models/lead-score';
import { LeadScoreDocument } from '../schemas/lead-score.schema';

@Injectable()
export class LeadScoreRepositoryImpl implements ILeadScoreRepository {
  constructor(@InjectModel(LeadScoreDocument.name) private readonly model: Model<LeadScoreDocument>) {}

  async save(score: LeadScore): Promise<LeadScore> {
    const props = score.toPlainObject();
    const doc = new this.model(props);
    const saved = await doc.save();
    return new LeadScore({ ...saved.toObject(), id: saved._id.toString() });
  }

  async findById(id: string): Promise<LeadScore | null> {
    const doc = await this.model.findById(id).exec();
    if (!doc) return null;
    return new LeadScore({ ...doc.toObject(), id: doc._id.toString() });
  }

  async findByLeadId(leadId: string, tenantId: string): Promise<LeadScore | null> {
    const doc = await this.model.findOne({ leadId, tenantId }).exec();
    if (!doc) return null;
    return new LeadScore({ ...doc.toObject(), id: doc._id.toString() });
  }

  async findByTenantId(tenantId: string, options?: { minScore?: number; maxScore?: number; page?: number; limit?: number }): Promise<LeadScore[]> {
    const filter: any = { tenantId };
    if (options?.minScore !== undefined) filter.score = { ...filter.score, $gte: options.minScore };
    if (options?.maxScore !== undefined) filter.score = { ...filter.score, $lte: options.maxScore };
    const page = options?.page ?? 1;
    const limit = options?.limit ?? 50;
    const docs = await this.model.find(filter).skip((page - 1) * limit).limit(limit).sort({ score: -1 }).exec();
    return docs.map(doc => new LeadScore({ ...doc.toObject(), id: doc._id.toString() }));
  }

  async findHotLeads(tenantId: string, threshold?: number): Promise<LeadScore[]> {
    const docs = await this.model.find({ tenantId, score: { $gte: threshold ?? 80 } }).sort({ score: -1 }).exec();
    return docs.map(doc => new LeadScore({ ...doc.toObject(), id: doc._id.toString() }));
  }

  async update(score: LeadScore): Promise<LeadScore> {
    const props = score.toPlainObject();
    if (!props.id) return this.save(score);
    const doc = await this.model.findByIdAndUpdate(props.id, props, { new: true, upsert: true }).exec();
    return new LeadScore({ ...doc.toObject(), id: doc._id.toString() });
  }

  async delete(id: string): Promise<boolean> {
    const result = await this.model.findByIdAndDelete(id).exec();
    return !!result;
  }

  async countByTenant(tenantId: string): Promise<number> {
    return this.model.countDocuments({ tenantId }).exec();
  }
}
