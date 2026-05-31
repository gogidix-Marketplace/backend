import { Injectable, Logger } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { IScalingEventRepository } from '@domain/repositories/scaling-event-repository.interface';
import { ScalingEvent, ScalingMetrics } from '@domain/models/scaling-event.entity';
import { ScalingEventType } from '@domain/enums/scaling-event-type.enum';
import { ScalingStatus } from '@domain/enums/scaling-status.enum';

@Injectable()
export class ScalingEventRepositoryImpl implements IScalingEventRepository {
  private readonly logger = new Logger(ScalingEventRepositoryImpl.name);

  constructor(@InjectModel('ScalingEvent') private readonly model: Model<any>) {}

  async save(event: ScalingEvent): Promise<ScalingEvent> {
    const doc = new this.model(event);
    const saved = await doc.save();
    return this.toEntity(saved);
  }

  async findByPolicyId(policyId: string, limit = 50): Promise<ScalingEvent[]> {
    const docs = await this.model.find({ policyId }).sort({ startedAt: -1 }).limit(limit).lean();
    return docs.map(d => this.toEntity(d));
  }

  async findById(id: string): Promise<ScalingEvent | null> {
    const doc = await this.model.findById(id).lean();
    return doc ? this.toEntity(doc) : null;
  }

  private toEntity(doc: any): ScalingEvent {
    return new ScalingEvent(
      doc.policyId, doc.policyName, doc.eventType as ScalingEventType,
      doc.previousCapacity, doc.newCapacity, doc.triggeredBy,
      doc.metrics as ScalingMetrics, doc.status as ScalingStatus,
      doc.error, doc.startedAt, doc.completedAt, doc.metadata,
      { id: doc._id?.toString(), createdAt: doc.createdAt, updatedAt: doc.updatedAt },
    );
  }
}
