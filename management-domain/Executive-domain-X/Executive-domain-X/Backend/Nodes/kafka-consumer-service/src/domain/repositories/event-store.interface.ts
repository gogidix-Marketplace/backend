import { DomainEvent } from '../models/event.entity';

export interface IEventStoreRepository {
  save(event: DomainEvent): Promise<DomainEvent>;
  markAsProcessed(eventId: string): Promise<boolean>;
  markAsFailed(eventId: string, error: string): Promise<boolean>;
  markAsProcessing(eventId: string): Promise<boolean>;
  getEventsByAggregate(aggregateId: string, tenantId?: string): Promise<DomainEvent[]>;
  getEventsByType(eventType: string, tenantId?: string, limit?: number): Promise<DomainEvent[]>;
  getPendingEvents(tenantId?: string, maxRetryCount?: number): Promise<DomainEvent[]>;
  getStats(tenantId?: string): Promise<{ total: number; statusCounts: Record<string, number>; typeCounts: Record<string, number> }>;
}
