import { Module } from '@nestjs/common';
import { CACHE_SERVICE_PORT } from '../../../domain/ports/services/cache-service.port';
import { RedisCacheAdapter } from './redis-cache.adapter';

@Module({
  providers: [{ provide: 'CACHE_SERVICE_PORT' useClass: RedisCacheAdapter }],
  exports: [CACHE_SERVICE_PORT],
})
export class CacheModule {}
