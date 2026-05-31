import { Injectable, Inject } from '@nestjs/common';
import { Model } from 'mongoose';
import { InjectModel } from '@nestjs/mongoose';
import { IChatSessionRepository } from '@domain/ports/output';
import { ChatSessionProps } from '@domain/models';
import { ChatSessionSchema } from '../mongoose/chat-session.schema';

@Injectable()
export class ChatSessionRepository implements IChatSessionRepository {
  constructor(
    @InjectModel('ChatSession') private readonly model: any,
  ) {}

  async findOne(sessionId: string): Promise<ChatSessionProps | null> {
    const doc = await this.model.findOne({ sessionId }).lean();
    return doc ? this.toProps(doc) : null;
  }

  async findActiveByCustomerId(customerId: string): Promise<ChatSessionProps[]> {
    const docs = await this.model.findActiveByCustomerId(customerId).lean();
    return docs.map(d => this.toProps(d));
  }

  async findExpiredSessions(timeoutMs: number): Promise<ChatSessionProps[]> {
    const docs = await this.model.findExpiredSessions(timeoutMs).lean();
    return docs.map(d => this.toProps(d));
  }

  async findByCustomerId(customerId: string, status?: string, limit: number = 20): Promise<ChatSessionProps[]> {
    const query: any = { customerId };
    if (status) query.status = status;
    const docs = await this.model.find(query).sort({ lastActivityAt: -1 }).limit(limit).lean();
    return docs.map(d => this.toProps(d));
  }

  async countDocuments(filter: any): Promise<number> {
    return this.model.countDocuments(filter);
  }

  async getSessionStats(startDate: Date, endDate: Date): Promise<any> {
    return this.model.getSessionStats(startDate, endDate);
  }

  async save(session: ChatSessionProps): Promise<ChatSessionProps> {
    const existing = await this.model.findOne({ sessionId: session.sessionId });
    if (existing) {
      await this.model.updateOne({ sessionId: session.sessionId }, { $set: session });
      const updated = await this.model.findOne({ sessionId: session.sessionId }).lean();
      return this.toProps(updated);
    }
    const created = await this.model.create(session);
    return this.toProps(created.toObject());
  }

  async updateOne(sessionId: string, update: Partial<ChatSessionProps>): Promise<void> {
    await this.model.updateOne({ sessionId }, update);
  }

  async find(filter: any): Promise<ChatSessionProps[]> {
    const docs = await this.model.find(filter).lean();
    return docs.map(d => this.toProps(d));
  }

  private toProps(doc: any): ChatSessionProps {
    return {
      sessionId: doc.sessionId,
      userId: doc.userId,
      customerId: doc.customerId,
      status: doc.status,
      language: doc.language,
      startedAt: doc.startedAt,
      endedAt: doc.endedAt,
      lastActivityAt: doc.lastActivityAt,
      context: doc.context || {},
      messages: doc.messages || [],
      assignedAgentId: doc.assignedAgentId,
      handoffRequest: doc.handoffRequest,
      sentiment: doc.sentiment,
      tags: doc.tags || [],
      metadata: doc.metadata || {},
    };
  }
}
