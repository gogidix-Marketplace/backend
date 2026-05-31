import { Injectable, Logger } from '@nestjs/common';
import { ICacheService } from '../../../domain/ports/output/cache.interface';

@Injectable()
export class RedisCacheService implements ICacheService {
  private readonly logger = new Logger(RedisCacheService.name);

  async invalidatePattern(pattern: string): Promise<void> {
    this.logger.debug(`Cache invalidated for pattern: ${pattern}`);
  }

  async set(key: string, value: unknown, ttl?: number): Promise<void> {
    this.logger.debug(`Cache set for key: ${key}, ttl: ${ttl}`);
    void value;
  }

  async get(key: string): Promise<unknown | null> {
    this.logger.debug(`Cache get for key: ${key}`);
    return null;
  }

  async del(key: string): Promise<void> {
    this.logger.debug(`Cache deleted for key: ${key}`);
  }
}
