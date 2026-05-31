import { Injectable, Logger } from '@nestjs/common';
import Redis from 'ioredis';
import { ContextRepository } from '@domain/ports/output';
import { ConversationContext } from '@domain/models';

const CONTEXT_PREFIX = 'chatbot:context:';
const CONTEXT_EXPIRY = 60 * 30;

@Injectable()
export class RedisContextRepository implements ContextRepository {
  private readonly logger = new Logger(RedisContextRepository.name);
  private readonly redis: Redis;

  constructor() {
    this.redis = new Redis({
      host: process.env.REDIS_HOST || 'localhost',
      port: parseInt(process.env.REDIS_PORT || '6379'),
      password: process.env.REDIS_PASSWORD || undefined,
      db: parseInt(process.env.REDIS_DB || '0'),
    });
  }

  async save(context: ConversationContext): Promise<ConversationContext> {
    const key = `${CONTEXT_PREFIX}${context.sessionId}`;
    await this.redis.set(key, JSON.stringify(context.toPlainObject()));
    await this.redis.expire(key, CONTEXT_EXPIRY);
    return context;
  }

  async findBySessionId(sessionId: string): Promise<ConversationContext | null> {
    const key = `${CONTEXT_PREFIX}${sessionId}`;
    const data = await this.redis.get(key);
    if (!data) return null;
    const props = JSON.parse(data);
    props.startedAt = new Date(props.startedAt);
    props.lastActivityAt = new Date(props.lastActivityAt);
    props.messages = props.messages.map((m: any) => ({ ...m, timestamp: new Date(m.timestamp) }));
    return ConversationContext.create(props);
  }

  async delete(sessionId: string): Promise<boolean> {
    const key = `${CONTEXT_PREFIX}${sessionId}`;
    await this.redis.del(key);
    return true;
  }
}
