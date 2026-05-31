/**
 * ChatSession MongoDB Model
 */

import mongoose, { Schema, Types } from 'mongoose';
import {
  IChatSession,
  IMessage,
  ISessionContext,
  SessionStatus,
} from '../types';

const MessageSchema = new Schema<IMessage>(
  {
    id: {
      type: String,
      required: true,
    },
    sessionId: {
      type: String,
      required: true,
      index: true,
    },
    content: {
      type: String,
      required: true,
      trim: true,
      maxlength: 5000,
    },
    sender: {
      type: String,
      enum: ['user', 'bot', 'agent'],
      required: true,
    },
    timestamp: {
      type: Date,
      default: Date.now,
    },
    language: {
      type: String,
      default: 'en',
    },
    metadata: {
      type: Map,
      of: Schema.Types.Mixed,
      default: {},
    },
    confidence: {
      type: Number,
      min: 0,
      max: 1,
    },
  },
  { _id: false }
);

const SessionContextSchema = new Schema<ISessionContext>(
  {
    userId: String,
    customerId: String,
    locale: {
      type: String,
      default: 'en',
    },
    timezone: String,
    metadata: {
      type: Map,
      of: Schema.Types.Mixed,
      default: new Map(),
    },
    accumulatedContext: {
      type: Map,
      of: Schema.Types.Mixed,
      default: new Map(),
    },
    turnCount: {
      type: Number,
      default: 0,
    },
    lastActivity: {
      type: Date,
      default: Date.now,
    },
  },
  { _id: false }
);

const HandoffRequestSchema = new Schema(
  {
    sessionId: String,
    reason: {
      type: String,
      required: true,
    },
    priority: {
      type: String,
      enum: ['low', 'medium', 'high', 'urgent'],
      default: 'medium',
    },
    requiredSkills: [String],
    summary: String,
    conversationHistory: [MessageSchema],
    customerInfo: {
      type: Map,
      of: Schema.Types.Mixed,
    },
  },
  { _id: false }
);

const ChatSessionSchema = new Schema<IChatSession>(
  {
    sessionId: {
      type: String,
      required: true,
      unique: true,
      index: true,
    },
    userId: {
      type: String,
      index: true,
    },
    customerId: {
      type: String,
      index: true,
    },
    status: {
      type: String,
      enum: Object.values(SessionStatus),
      default: SessionStatus.ACTIVE,
      required: true,
      index: true,
    },
    language: {
      type: String,
      default: 'en',
      required: true,
    },
    startedAt: {
      type: Date,
      default: Date.now,
      required: true,
    },
    endedAt: {
      type: Date,
    },
    lastActivityAt: {
      type: Date,
      default: Date.now,
      required: true,
      index: true,
    },
    context: {
      type: SessionContextSchema,
      default: {},
    },
    messages: {
      type: [MessageSchema],
      default: [],
    },
    assignedAgentId: {
      type: String,
      index: true,
    },
    handoffRequest: HandoffRequestSchema,
    sentiment: {
      score: {
        type: Number,
        min: 0,
        max: 100,
      },
      label: {
        type: String,
        enum: ['positive', 'neutral', 'negative'],
      },
    },
    tags: {
      type: [String],
      default: [],
      index: true,
    },
    metadata: {
      type: Map,
      of: Schema.Types.Mixed,
      default: new Map(),
    },
  },
  {
    timestamps: true,
    collection: 'chat_sessions',
  }
);

// Indexes for efficient queries
ChatSessionSchema.index({ createdAt: 1 });
ChatSessionSchema.index({ lastActivityAt: 1, status: 1 });
ChatSessionSchema.index({ customerId: 1, status: 1 });
ChatSessionSchema.index({ 'sentiment.label': 1 });
ChatSessionSchema.index({ tags: 1 });

// Instance methods
ChatSessionSchema.methods.addMessage = function (message: IMessage): void {
  this.messages.push(message);
  this.context.turnCount = (this.context.turnCount || 0) + 1;
  this.lastActivityAt = new Date();
  this.context.lastActivity = new Date();
};

ChatSessionSchema.methods.updateStatus = function (status: SessionStatus): void {
  this.status = status;
  if (status === SessionStatus.CLOSED || status === SessionStatus.TIMEOUT) {
    this.endedAt = new Date();
  }
  this.lastActivityAt = new Date();
};

ChatSessionSchema.methods.updateSentiment = function (
  score: number,
  label: 'positive' | 'neutral' | 'negative'
): void {
  this.sentiment = { score, label };
};

ChatSessionSchema.methods.addTag = function (tag: string): void {
  if (!this.tags.includes(tag)) {
    this.tags.push(tag);
  }
};

ChatSessionSchema.methods.setMetadata = function (key: string, value: unknown): void {
  if (!this.metadata) {
    this.metadata = new Map();
  }
  this.metadata.set(key, value);
};

// Static methods
ChatSessionSchema.statics.findActiveByCustomerId = function (customerId: string) {
  return this.find({
    customerId,
    status: { $in: [SessionStatus.ACTIVE, SessionStatus.WAITING_FOR_AGENT, SessionStatus.WITH_AGENT] },
  }).sort({ lastActivityAt: -1 });
};

ChatSessionSchema.statics.findExpiredSessions = function (timeoutMs: number) {
  const cutoffTime = new Date(Date.now() - timeoutMs);
  return this.find({
    status: { $in: [SessionStatus.ACTIVE] },
    lastActivityAt: { $lt: cutoffTime },
  });
};

ChatSessionSchema.statics.getSessionStats = async function (startDate: Date, endDate: Date) {
  const stats = await this.aggregate([
    {
      $match: {
        startedAt: { $gte: startDate, $lte: endDate },
      },
    },
    {
      $group: {
        _id: null,
        totalSessions: { $sum: 1 },
        activeSessions: {
          $sum: {
            $cond: [
              { $in: ['$status', [SessionStatus.ACTIVE, SessionStatus.WAITING_FOR_AGENT, SessionStatus.WITH_AGENT]] },
              1,
              0,
            ],
          },
        },
        closedSessions: {
          $sum: { $cond: [{ $eq: ['$status', SessionStatus.CLOSED] }, 1, 0] },
        },
        escalatedSessions: {
          $sum: {
            $cond: [{ $ifNull: ['$assignedAgentId', false] }, 1, 0],
          },
        },
        avgMessagesPerSession: { $avg: { $size: '$messages' } },
        avgTurnsPerSession: { $avg: '$context.turnCount' },
      },
    },
    {
      $project: {
        _id: 0,
        totalSessions: 1,
        activeSessions: 1,
        closedSessions: 1,
        escalatedSessions: 1,
        avgMessagesPerSession: { $round: ['$avgMessagesPerSession', 2] },
        avgTurnsPerSession: { $round: ['$avgTurnsPerSession', 2] },
      },
    },
  ]);

  return stats[0] || {
    totalSessions: 0,
    activeSessions: 0,
    closedSessions: 0,
    escalatedSessions: 0,
    avgMessagesPerSession: 0,
    avgTurnsPerSession: 0,
  };
};

// Virtual properties
ChatSessionSchema.virtual('duration').get(function () {
  const end = this.endedAt || new Date();
  return end.getTime() - this.startedAt.getTime();
});

ChatSessionSchema.virtual('isExpired').get(function () {
  return this.status === SessionStatus.ACTIVE &&
    Date.now() - this.lastActivityAt.getTime() > 1800000; // 30 minutes
});

// Middleware
ChatSessionSchema.pre('save', function (next) {
  this.lastActivityAt = new Date();
  next();
});

export const ChatSessionModel = mongoose.model<IChatSession>('ChatSession', ChatSessionSchema);
