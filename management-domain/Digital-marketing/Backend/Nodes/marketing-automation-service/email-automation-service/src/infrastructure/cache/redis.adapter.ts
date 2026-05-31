import { Injectable } from '@nestjs/common';
import * as Redis from 'ioredis';

@Injectable()
export class RedisAdapter {
  private client: Redis.Redis;

  constructor() {
    this.client = new Redis({ host: process.env.REDIS_HOST ?? 'localhost', port: parseInt(process.env.REDIS_PORT ?? '6379'), db: 0 });
  }

  async get(key: string): Promise<string | null> { return this.client.get(key); }
  async set(key: string, value: string, ttlSeconds?: number): Promise<void> { if (ttlSeconds) { await this.client.set(key, value, 'EX', ttlSeconds); } else { await this.client.set(key, value); } }
  async del(key: string): Promise<void> { await this.client.del(key); }
  async exists(key: string): Promise<boolean> { return (await this.client.exists(key)) === 1; }
  getClient(): Redis.Redis { return this.client; }
}
