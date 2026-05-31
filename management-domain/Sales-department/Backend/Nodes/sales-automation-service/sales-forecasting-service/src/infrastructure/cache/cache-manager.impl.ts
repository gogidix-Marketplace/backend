import { Injectable, Inject } from '@nestjs/common';
import { CACHE_MANAGER } from '@nestjs/cache-manager';
import { Cache } from 'cache-manager';
import { CacheManagerPort } from '../../domain/ports/out/cache-manager.port';

@Injectable()
export class CacheManagerImpl implements CacheManagerPort {
  constructor(@Inject(CACHE_MANAGER) private readonly cacheManager: Cache) {}

  async get<T>(key: string): Promise<T | null> {
    try {
      const value = await this.cacheManager.get<T>(key);
      return value ?? null;
    } catch (error) {
      console.error(`Cache get error for key ${key}:`, error);
      return null;
    }
  }

  async set<T>(key: string, value: T, ttl?: number): Promise<void> {
    try {
      await this.cacheManager.set(key, value, ttl);
    } catch (error) {
      console.error(`Cache set error for key ${key}:`, error);
    }
  }

  async delete(key: string): Promise<void> {
    try {
      await this.cacheManager.del(key);
    } catch (error) {
      console.error(`Cache delete error for key ${key}:`, error);
    }
  }

  async invalidatePattern(pattern: string): Promise<void> {
    try {
      const cacheStore = this.cacheManager.store;
      // Note: This is a simplified implementation
      // In production, you might want to use Redis keys command or similar
      if ('keys' in cacheStore && typeof cacheStore.keys === 'function') {
        const keys = await cacheStore.keys(pattern);
        await Promise.all(keys.map((key: string) => this.cacheManager.del(key)));
      }
    } catch (error) {
      console.error(`Cache invalidate pattern error for ${pattern}:`, error);
    }
  }

  async clear(): Promise<void> {
    try {
      await this.cacheManager.reset();
    } catch (error) {
      console.error('Cache clear error:', error);
    }
  }
}
