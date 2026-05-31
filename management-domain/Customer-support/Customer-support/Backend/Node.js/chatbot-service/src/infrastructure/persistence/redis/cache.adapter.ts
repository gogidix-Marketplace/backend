import { Injectable, Logger } from '@nestjs/common';
import { ICachePort } from '@domain/ports/output';
import { createClient, RedisClientType } from 'redis';
import { ConfigService } from '@nestjs/config';

@Injectable()
export class CacheAdapter implements ICachePort {
  private readonly logger = new Logger(CacheAdapter.name);
  private client: RedisClientType;
  private isConnected = false;

  constructor(private configService: ConfigService) {
    this.client = createClient({
      socket: {
        host: this.configService.get<string>('REDIS_HOST', 'localhost'),
        port: parseInt(this.configService.get<string>('REDIS_PORT', '6379')),
      },
      password: this.configService.get<string>('REDIS_PASSWORD') || undefined,
      database: parseInt(this.configService.get<string>('REDIS_DB', '0')),
    });

    this.client.on('error', (error) => {
      this.logger.error('Redis Client Error: ' + error);
      this.isConnected = false;
    });

    this.client.on('connect', () => {
      this.logger.log('Redis Client Connected');
      this.isConnected = true;
    });
  }

  async connect(): Promise<void> {
    if (!this.isConnected) {
      await this.client.connect();
    }
  }

  async get(key: string): Promise<string | null> {
    try {
      return await this.client.get(key);
    } catch (error) {
      this.logger.error(`Redis GET error for key ${key}: ${error}`);
      return null;
    }
  }

  async set(key: string, value: string, expirySeconds?: number): Promise<boolean> {
    try {
      if (expirySeconds) {
        await this.client.setEx(key, expirySeconds, value);
      } else {
        await this.client.set(key, value);
      }
      return true;
    } catch (error) {
      this.logger.error(`Redis SET error for key ${key}: ${error}`);
      return false;
    }
  }

  async del(key: string): Promise<boolean> {
    try {
      await this.client.del(key);
      return true;
    } catch (error) {
      this.logger.error(`Redis DEL error for key ${key}: ${error}`);
      return false;
    }
  }

  async exists(key: string): Promise<boolean> {
    try {
      const result = await this.client.exists(key);
      return result === 1;
    } catch (error) {
      return false;
    }
  }

  async incr(key: string): Promise<number | null> {
    try {
      return await this.client.incr(key);
    } catch (error) {
      return null;
    }
  }

  async expire(key: string, seconds: number): Promise<boolean> {
    try {
      await this.client.expire(key, seconds);
      return true;
    } catch (error) {
      return false;
    }
  }
}
