import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { DomainEvent, EventProps } from '../../../../domain/models/event.entity';
import { IEventStoreRepository } from '../../../../domain/repositories/event-store.interface';
import { EventDocument } from '../mongoose/event.schema';

@Injectable()
export class EventStoreRepositoryImpl implements IEventStoreRepository {
  constructor(@InjectModel('Event') private readonly eventModel: Model<EventDocument>) {}

  async save(event: DomainEvent): Promise<DomainEvent> {
    const existing = await this.eventModel.findOne({ eventId: event.eventId });
    if (existing) return this.toEntity(existing);
    const lastEvent = await this.eventModel.findOne({ aggregateId: event.aggregateId }).sort({ version: -1 });
    const version = lastEvent ? (lastEvent.version || 0) + 1 : 1;
    const doc = new this.eventModel({ ...event.toJSON(), version });
    await doc.save();
    return this.toEntity(doc);
  }

  async markAsProcessed(eventId: string): Promise<boolean> { const r = await this.eventModel.updateOne({ eventId }, { $set: { status: 'COMPLETED', processedAt: new Date() } }); return r.modifiedCount > 0; }
  async markAsFailed(eventId: string, error: string): Promise<boolean> { const r = await this.eventModel.updateOne({ eventId }, { $set: { status: 'FAILED', error }, $inc: { retryCount: 1 } }); return r.modifiedCount > 0; }
  async markAsProcessing(eventId: string): Promise<boolean> { const r = await this.eventModel.updateOne({ eventId, status: 'PENDING' }, { $set: { status: 'PROCESSING' } }); return r.modifiedCount > 0; }

  async getEventsByAggregate(aggregateId: string, tenantId?: string): Promise<DomainEvent[]> {
    const q: any = { aggregateId }; if (tenantId) q.tenantId = tenantId;
    const docs = await this.eventModel.find(q).sort({ version: 1 }); return docs.map(d => this.toEntity(d));
  }

  async getEventsByType(eventType: string, tenantId?: string, limit = 100): Promise<DomainEvent[]> {
    const q: any = { eventType }; if (tenantId) q.tenantId = tenantId;
    const docs = await this.eventModel.find(q).sort({ timestamp: -1 }).limit(limit); return docs.map(d => this.toEntity(d));
  }

  async getPendingEvents(tenantId?: string, maxRetryCount = 3): Promise<DomainEvent[]> {
    const q: any = { status: { $in: ['FAILED', 'PENDING'] }, retryCount: { $lt: maxRetryCount } }; if (tenantId) q.tenantId = tenantId;
    const docs = await this.eventModel.find(q).sort({ timestamp: 1 }).limit(100); return docs.map(d => this.toEntity(d));
  }

  async getStats(tenantId?: string): Promise<{ total: number; statusCounts: Record<string, number>; typeCounts: Record<string, number> }> {
    const m = tenantId ? { $match: { tenantId } } : { $match: {} };
    const [stats, types, total] = await Promise.all([
      this.eventModel.aggregate([m, { $group: { _id: '$status', count: { $sum: 1 } } }]),
      this.eventModel.aggregate([m, { $group: { _id: '$eventType', count: { $sum: 1 } } }]),
      this.eventModel.countDocuments(tenantId ? { tenantId } : {}),
    ]);
    return { total, statusCounts: stats.reduce((a: any, s: any) => { a[s._id] = s.count; return a; }, {}), typeCounts: types.reduce((a: any, s: any) => { a[s._id] = s.count; return a; }, {}) };
  }

  private toEntity(doc: EventDocument): DomainEvent {
    return new DomainEvent({ id: doc._id.toString(), eventId: doc.eventId, eventType: doc.eventType, eventVersion: doc.eventVersion, aggregateId: doc.aggregateId, aggregateType: doc.aggregateType, tenantId: doc.tenantId, payload: doc.payload || {}, metadata: doc.metadata || {}, causationId: doc.causationId, correlationId: doc.correlationId, version: doc.version, status: doc.status, error: doc.error, processedAt: doc.processedAt, retryCount: doc.retryCount, timestamp: (doc as any).createdAt || new Date(), createdAt: (doc as any).createdAt || new Date() } as EventProps);
  }
}
