import { Injectable, Logger } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { IScalingPolicyRepository } from '@domain/repositories/scaling-policy-repository.interface';
import { ScalingPolicy, ScalingRule } from '@domain/models/scaling-policy.entity';
import { CloudProvider } from '@domain/enums/cloud-provider.enum';

@Injectable()
export class ScalingPolicyRepositoryImpl implements IScalingPolicyRepository {
  private readonly logger = new Logger(ScalingPolicyRepositoryImpl.name);

  constructor(@InjectModel('ScalingPolicy') private readonly model: Model<any>) {}

  async save(policy: ScalingPolicy): Promise<ScalingPolicy> {
    const doc = new this.model(policy);
    const saved = await doc.save();
    return this.toEntity(saved);
  }

  async findById(id: string): Promise<ScalingPolicy | null> {
    const doc = await this.model.findById(id).lean();
    return doc ? this.toEntity(doc) : null;
  }

  async findEnabled(): Promise<ScalingPolicy[]> {
    const docs = await this.model.find({ enabled: true }).lean();
    return docs.map(d => this.toEntity(d));
  }

  async findByFilters(filters: any, limit: number, skip: number): Promise<{ data: ScalingPolicy[]; total: number }> {
    const filter: any = {};
    if (filters.enabled !== undefined) filter.enabled = filters.enabled === 'true';
    if (filters.cloudProvider) filter.cloudProvider = filters.cloudProvider;
    const [data, total] = await Promise.all([
      this.model.find(filter).sort({ createdAt: -1 }).skip(skip).limit(limit).lean(),
      this.model.countDocuments(filter),
    ]);
    return { data: data.map(d => this.toEntity(d)), total };
  }

  async findByIdAndUpdate(id: string, update: any): Promise<ScalingPolicy | null> {
    const doc = await this.model.findByIdAndUpdate(id, update, { new: true, runValidators: true }).lean();
    return doc ? this.toEntity(doc) : null;
  }

  async findByIdAndDelete(id: string): Promise<ScalingPolicy | null> {
    const doc = await this.model.findByIdAndDelete(id).lean();
    return doc ? this.toEntity(doc) : null;
  }

  private toEntity(doc: any): ScalingPolicy {
    return new ScalingPolicy(
      doc.name, doc.description, doc.resourceId,
      doc.cloudProvider as CloudProvider, doc.enabled,
      doc.scaleOutRules || [], doc.scaleInRules || [],
      doc.cooldownPeriod, doc.minInstances, doc.maxInstances,
      doc.currentInstances, doc.targetInstances, doc.lastEvaluatedAt,
      { id: doc._id?.toString(), createdAt: doc.createdAt, updatedAt: doc.updatedAt },
    );
  }
}
