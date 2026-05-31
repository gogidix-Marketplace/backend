import { DeadLetterEntry } from '../models/dead-letter-entry.entity';

export interface IDeadLetterQueueRepository {
  save(entry: DeadLetterEntry): Promise<DeadLetterEntry>;
  getRetryableEvents(tenantId?: string, limit?: number): Promise<DeadLetterEntry[]>;
  markAsResolved(dlqEventId: string): Promise<void>;
  markAsFailed(dlqEventId: string, reason: string): Promise<void>;
  markForRetry(dlqEventId: string): Promise<void>;
  getStats(tenantId?: string): Promise<{ total: number; statusCounts: Record<string, number> }>;
}
