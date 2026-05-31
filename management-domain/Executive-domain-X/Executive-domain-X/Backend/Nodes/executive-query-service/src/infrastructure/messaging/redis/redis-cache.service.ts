import { Injectable, Logger } from '@nestjs/common';
import { ICacheService } from '../../../domain/ports/output/cache.interface';

@Injectable()
export class RedisCacheService implements ICacheService {
  private readonly logger = new Logger(RedisCacheService.name);
  async get(key: string): Promise<unknown | null> { this.logger.debug(`Cache get: ${key}`); return null; }
  async set(key: string, value: unknown, ttl?: number): Promise<void> { this.logger.debug(`Cache set: ${key}, ttl: ${ttl}`); void value; }
  async del(key: string): Promise<void> { this.logger.debug(`Cache del: ${key}`); }
  async invalidatePattern(pattern: string): Promise<void> { this.logger.debug(`Cache invalidated: ${pattern}`); }
}
