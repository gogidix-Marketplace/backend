/**
 * Redis Client Configuration
 */

import { createClient } from 'redis';
import { logger } from './logger';
import config from '../config';

class RedisClient {
  private client: ReturnType<typeof createClient>;
  private isConnected: boolean = false;

  constructor() {
    this.client = createClient({
      socket: {
        host: config.redis.host,
        port: config.redis.port,
      },
      password: config.redis.password,
      database: config.redis.db,
    });

    this.client.on('error', (error) => {
      logger.error('Redis Client Error:', error);
      this.isConnected = false;
    });

    this.client.on('connect', () => {
      logger.info('Redis Client Connected');
      this.isConnected = true;
    });

    this.client.on('disconnect', () => {
      logger.warn('Redis Client Disconnected');
      this.isConnected = false;
    });

    this.client.on('reconnecting', () => {
      logger.info('Redis Client Reconnecting...');
    });
  }

  async connect(): Promise<void> {
    if (!this.isConnected) {
      await this.client.connect();
    }
  }

  async disconnect(): Promise<void> {
    if (this.isConnected) {
      await this.client.quit();
    }
  }

  async get(key: string): Promise<string | null> {
    try {
      return await this.client.get(key);
    } catch (error) {
      logger.error(`Redis GET error for key ${key}:`, error);
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
      logger.error(`Redis SET error for key ${key}:`, error);
      return false;
    }
  }

  async del(key: string): Promise<boolean> {
    try {
      await this.client.del(key);
      return true;
    } catch (error) {
      logger.error(`Redis DEL error for key ${key}:`, error);
      return false;
    }
  }

  async exists(key: string): Promise<boolean> {
    try {
      const result = await this.client.exists(key);
      return result === 1;
    } catch (error) {
      logger.error(`Redis EXISTS error for key ${key}:`, error);
      return false;
    }
  }

  async incr(key: string): Promise<number | null> {
    try {
      return await this.client.incr(key);
    } catch (error) {
      logger.error(`Redis INCR error for key ${key}:`, error);
      return null;
    }
  }

  async expire(key: string, seconds: number): Promise<boolean> {
    try {
      await this.client.expire(key, seconds);
      return true;
    } catch (error) {
      logger.error(`Redis EXPIRE error for key ${key}:`, error);
      return false;
    }
  }

  async hget(hash: string, field: string): Promise<string | null> {
    try {
      return await this.client.hGet(hash, field);
    } catch (error) {
      logger.error(`Redis HGET error for hash ${hash}, field ${field}:`, error);
      return null;
    }
  }

  async hset(hash: string, field: string, value: string): Promise<boolean> {
    try {
      await this.client.hSet(hash, field, value);
      return true;
    } catch (error) {
      logger.error(`Redis HSET error for hash ${hash}, field ${field}:`, error);
      return false;
    }
  }

  async hgetall(hash: string): Promise<Record<string, string> | null> {
    try {
      return await this.client.hGetAll(hash);
    } catch (error) {
      logger.error(`Redis HGETALL error for hash ${hash}:`, error);
      return null;
    }
  }

  async hdel(hash: string, field: string): Promise<boolean> {
    try {
      await this.client.hDel(hash, field);
      return true;
    } catch (error) {
      logger.error(`Redis HDEL error for hash ${hash}, field ${field}:`, error);
      return false;
    }
  }

  async lpush(key: string, ...values: string[]): Promise<number | null> {
    try {
      return await this.client.lPush(key, values);
    } catch (error) {
      logger.error(`Redis LPUSH error for key ${key}:`, error);
      return null;
    }
  }

  async lrange(key: string, start: number, stop: number): Promise<string[] | null> {
    try {
      return await this.client.lRange(key, start, stop);
    } catch (error) {
      logger.error(`Redis LRANGE error for key ${key}:`, error);
      return null;
    }
  }

  async ltrim(key: string, start: number, stop: number): Promise<boolean> {
    try {
      await this.client.lTrim(key, start, stop);
      return true;
    } catch (error) {
      logger.error(`Redis LTRIM error for key ${key}:`, error);
      return false;
    }
  }

  async publish(channel: string, message: string): Promise<number> {
    try {
      return await this.client.publish(channel, message);
    } catch (error) {
      logger.error(`Redis PUBLISH error for channel ${channel}:`, error);
      return 0;
    }
  }

  async subscribe(channel: string, callback: (message: string) => void): Promise<void> {
    try {
      const subscriber = this.client.duplicate();
      await subscriber.connect();
      await subscriber.subscribe(channel, (message) => {
        callback(message);
      });
    } catch (error) {
      logger.error(`Redis SUBSCRIBE error for channel ${channel}:`, error);
    }
  }

  async ping(): Promise<boolean> {
    try {
      const response = await this.client.ping();
      return response === 'PONG';
    } catch {
      return false;
    }
  }

  getClient() {
    return this.client;
  }
}

export const redisClient = new RedisClient();

export default redisClient;
