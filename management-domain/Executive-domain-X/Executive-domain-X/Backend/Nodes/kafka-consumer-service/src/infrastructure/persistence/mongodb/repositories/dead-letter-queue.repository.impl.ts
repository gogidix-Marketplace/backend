import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { DeadLetterEntry } from '../../../../domain/models/dead-letter-entry.entity';
import { IDeadLetterQueueRepository } from '../../../../domain/repositories/dead-letter-queue.interface';
import { DeadLetterDocument } from '../mongoose/dead-letter.schema';

@Injectable()
export class DeadLetterQueueRepositoryImpl implements IDeadLetterQueueRepository {
  constructor(@InjectModel('DeadLetter') private readonly dlqModel: Model<DeadLetterDocument>) {}

  async save(entry: DeadLetterEntry): Promise<DeadLetterEntry> { const doc = new this.dlqModel(entry.toJSON()); await doc.save(); return this.toEntity(doc); }
  async getRetryableEvents(tenantId?: string, limit = 10): Promise<DeadLetterEntry[]> {
    const q: any = { status: 'PENDING', retryCount: { $lt: 3 } }; if (tenantId) q.tenantId = tenantId;
    const docs = await this.dlqModel.find(q).sort({ createdAt: 1 }).limit(limit); return docs.map(d => this.toEntity(d));
  }
  async markAsResolved(dlqEventId: string): Promise<void> { await this.dlqModel.updateOne({ dlqEventId }, { $set: { status: 'RESOLVED' } }); }
  async markAsFailed(dlqEventId: string, reason: string): Promise<void> { await this.dlqModel.updateOne({ dlqEventId }, { $set: { status: 'EXHAUSTED', error: reason } }); }
  async markForRetry(dlqEventId: string): Promise<void> { await this.dlqModel.updateOne({ dlqEventId }, { $set: { status: 'PENDING' }, $inc: { retryCount: 1 } }); }

  async getStats(tenantId?: string): Promise<{ total: number; statusCounts: Record<string, number> }> {
    const m = tenantId ? { $match: { tenantId } } : { $match: {} };
    const stats = await this.dlqModel.aggregate([m, { $group: { _id: '$status', count: { $sum: 1 } } }]);
    return { total: await this.dlqModel.countDocuments(tenantId ? { tenantId } : {}), statusCounts: stats.reduce((a: any, s: any) => { a[s._id] = s.count; return a; }, {}) };
  }

  private toEntity(doc: DeadLetterDocument): DeadLetterEntry {
    return new DeadLetterEntry({ id: doc._id.toString(), dlqEventId: doc.dlqEventId, originalEventId: doc.originalEventId, eventType: doc.eventType, tenantId: doc.tenantId, payload: doc.payload || {}, error: doc.error, retryCount: doc.retryCount, maxRetryAttempts: doc.maxRetryAttempts, status: doc.status, metadata: doc.metadata || {}, createdAt: (doc as any).createdAt || new Date(), updatedAt: (doc as any).updatedAt || new Date() });
  }
}
