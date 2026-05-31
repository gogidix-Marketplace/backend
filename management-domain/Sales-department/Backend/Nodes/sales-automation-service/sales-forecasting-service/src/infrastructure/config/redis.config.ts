import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { CacheModule } from '@nestjs/cache-manager';
import { redisStore } from 'cache-manager-redis-store';

export const getRedisCacheConfig = () => ({
  imports: [
    CacheModule.registerAsync({
      imports: [ConfigModule],
      inject: [ConfigService],
      useFactory: async (configService: ConfigService) => ({
        store: await redisStore({
          socket: {
            host: configService.get<string>('REDIS_HOST', 'localhost'),
            port: configService.get<number>('REDIS_PORT', 6379),
          },
          password: configService.get<string>('REDIS_PASSWORD'),
          ttl: configService.get<number>('REDIS_CACHE_TTL', 3600),
        }) as any,
        isGlobal: true,
      }),
    }),
  ],
});

export const getMemoryCacheConfig = () => ({
  imports: [
    CacheModule.register({
      isGlobal: true,
      ttl: 3600,
      max: 100,
    }),
  ],
});
