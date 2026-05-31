import { Injectable } from '@nestjs/common';
import { ICacheService } from '../../../domain/ports/services/cache-service.port';
import * as Redis from 'ioredis';

@Injectable()
export class RedisCacheAdapter implements ICacheService {
  private client: Redis.Redis;
  constructor() { this.client = new Redis({ host: process.env.REDIS_HOST ?? 'localhost', port: parseInt(process.env.REDIS_PORT ?? '6379'), db: 2 }); }
  async get<T>(key: string): Promise<T | null> { const val = await this.client.get(key); return val ? JSON.parse(val) : null; }
  async set<T>(key: string, value: T, ttlSeconds?: number): Promise<void> { const val = JSON.stringify(value); if (ttlSeconds) { await this.client.set(key, val, 'EX', ttlSeconds); } else { await this.client.set(key, val); } }
  async del(key: string): Promise<void> { await this.client.del(key); }
}
