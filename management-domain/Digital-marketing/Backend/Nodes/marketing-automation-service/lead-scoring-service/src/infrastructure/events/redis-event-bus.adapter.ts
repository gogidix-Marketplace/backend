import { Injectable } from '@nestjs/common';
import { IEventBus, DomainEvent } from '../../../domain/ports/services/event-bus.port';
import * as Redis from 'ioredis';

@Injectable()
export class RedisEventBusAdapter implements IEventBus {
  private publisher: Redis.Redis;
  private subscriber: Redis.Redis;
  constructor() {
    const opts = { host: process.env.REDIS_HOST ?? 'localhost', port: parseInt(process.env.REDIS_PORT ?? '6379'), db: 2 };
    this.publisher = new Redis(opts);
    this.subscriber = new Redis(opts);
  }
  async publish(channel: string, event: DomainEvent): Promise<void> { await this.publisher.publish(channel, JSON.stringify(event)); }
  async subscribe(channel: string, handler: (event: DomainEvent) => Promise<void>): Promise<void> {
    await this.subscriber.subscribe(channel);
    this.subscriber.on('message', async (ch, message) => {
      if (ch === channel) { await handler(JSON.parse(message)); }
    });
  }
}
