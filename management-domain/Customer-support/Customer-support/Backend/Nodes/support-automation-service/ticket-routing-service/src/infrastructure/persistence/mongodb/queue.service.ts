import { Injectable, Logger } from '@nestjs/common';
import Redis from 'ioredis';
import { Ticket } from '@domain/models';
import { TicketPriority } from '@domain/enums';
import { QueueManagementInputPort } from '@domain/ports/input';

const priorityToNumber = (p: TicketPriority): number => ({ [TicketPriority.CRITICAL]: 4, [TicketPriority.HIGH]: 3, [TicketPriority.MEDIUM]: 2, [TicketPriority.LOW]: 1 }[p] || 0);

@Injectable()
export class QueueService implements QueueManagementInputPort {
  private readonly redis: Redis;
  private readonly logger = new Logger(QueueService.name);
  private readonly PREFIX = 'ticket:queue:set:';

  constructor() {
    this.redis = new Redis({ host: process.env.REDIS_HOST || 'localhost', port: parseInt(process.env.REDIS_PORT || '6379') });
  }

  async enqueue(ticket: Ticket, department?: string): Promise<number> {
    const key = `${this.PREFIX}${ticket.id}`;
    const entry = JSON.stringify({ ticketId: ticket.id, ticket: ticket.toPlainObject(), queuedAt: new Date(), priority: priorityToNumber(ticket.priority), attempts: 0 });
    await this.redis.zadd(key, priorityToNumber(ticket.priority), entry);
    const position = await this.redis.zrevrank(key, entry);
    return (position || 0) + 1;
  }

  async dequeue(department?: string): Promise<any> {
    const key = `${this.PREFIX}${department}`;
    const results = await this.redis.zpopmax(key);
    if (!results || results.length === 0) return null;
    return JSON.parse(results[0]);
  }

  async getQueueLength(department?: string): Promise<number> {
    const key = `${this.PREFIX}${department}`;
    return this.redis.zcard(key);
  }

  async getQueueStats(department?: string): Promise<Record<string, number>> {
    const key = `${this.PREFIX}${department}`;
    return { currentQueueLength: await this.redis.zcard(key) };
  }

  async clearQueue(department?: string): Promise<number> {
    const key = `${this.PREFIX}${department}`;
    const count = await this.redis.zcard(key);
    await this.redis.del(key);
    return count;
  }

  async getTicketPosition(ticketId: string, department?: string): Promise<number | null> {
    const key = `${this.PREFIX}${ticketId}`;
    const entries = await this.redis.zrevrange(key, 0, -1);
    for (let i = 0; i < entries.length; i++) {
      const parsed = JSON.parse(entries[i]);
      if (parsed.ticketId === ticketId) return i + 1;
    }
    return null;
  }
}
