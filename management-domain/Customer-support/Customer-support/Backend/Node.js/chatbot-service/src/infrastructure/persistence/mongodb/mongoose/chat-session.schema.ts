import { Schema } from 'mongoose';

export const MessageSchema = new Schema(
  {
    id: { type: String, required: true },
    sessionId: { type: String, required: true, index: true },
    content: { type: String, required: true, trim: true, maxlength: 5000 },
    sender: { type: String, enum: ['user', 'bot', 'agent'], required: true },
    timestamp: { type: Date, default: Date.now },
    language: { type: String, default: 'en' },
    metadata: { type: Map, of: Schema.Types.Mixed, default: {} },
    confidence: { type: Number, min: 0, max: 1 },
  },
  { _id: false },
);

export const SessionContextSchema = new Schema(
  {
    userId: String,
    customerId: String,
    locale: { type: String, default: 'en' },
    timezone: String,
    metadata: { type: Map, of: Schema.Types.Mixed, default: {} },
    accumulatedContext: { type: Map, of: Schema.Types.Mixed, default: {} },
    turnCount: { type: Number, default: 0 },
    lastActivity: { type: Date, default: Date.now },
  },
  { _id: false },
);

export const HandoffRequestSchema = new Schema(
  {
    sessionId: String,
    reason: { type: String, required: true },
    priority: { type: String, enum: ['low', 'medium', 'high', 'urgent'], default: 'medium' },
    requiredSkills: [String],
    summary: String,
    conversationHistory: [MessageSchema],
    customerInfo: { type: Map, of: Schema.Types.Mixed },
  },
  { _id: false },
);

export const ChatSessionSchema = new Schema(
  {
    sessionId: { type: String, required: true, unique: true, index: true },
    userId: { type: String, index: true },
    customerId: { type: String, index: true },
    status: {
      type: String,
      enum: ['active', 'waiting_for_agent', 'with_agent', 'closed', 'timeout'],
      default: 'active',
      required: true,
      index: true,
    },
    language: { type: String, default: 'en', required: true },
    startedAt: { type: Date, default: Date.now, required: true },
    endedAt: { type: Date },
    lastActivityAt: { type: Date, default: Date.now, required: true, index: true },
    context: { type: SessionContextSchema, default: {} },
    messages: { type: [MessageSchema], default: [] },
    assignedAgentId: { type: String, index: true },
    handoffRequest: HandoffRequestSchema,
    sentiment: {
      score: { type: Number, min: 0, max: 100 },
      label: { type: String, enum: ['positive', 'neutral', 'negative'] },
    },
    tags: { type: [String], default: [], index: true },
    metadata: { type: Map, of: Schema.Types.Mixed, default: {} },
  },
  { timestamps: true, collection: 'chat_sessions' },
);

ChatSessionSchema.index({ createdAt: 1 });
ChatSessionSchema.index({ lastActivityAt: 1, status: 1 });
ChatSessionSchema.index({ customerId: 1, status: 1 });
ChatSessionSchema.index({ 'sentiment.label': 1 });
ChatSessionSchema.index({ tags: 1 });

ChatSessionSchema.statics.findActiveByCustomerId = function (customerId: string) {
  return this.find({
    customerId,
    status: { $in: ['active', 'waiting_for_agent', 'with_agent'] },
  }).sort({ lastActivityAt: -1 });
};

ChatSessionSchema.statics.findExpiredSessions = function (timeoutMs: number) {
  const cutoffTime = new Date(Date.now() - timeoutMs);
  return this.find({ status: { $in: ['active'] }, lastActivityAt: { $lt: cutoffTime } });
};

ChatSessionSchema.statics.getSessionStats = async function (startDate: Date, endDate: Date) {
  const stats = await this.aggregate([
    { $match: { startedAt: { $gte: startDate, $lte: endDate } } },
    {
      $group: {
        _id: null,
        totalSessions: { $sum: 1 },
        activeSessions: { $sum: { $cond: [{ $in: ['$status', ['active', 'waiting_for_agent', 'with_agent']] }, 1, 0] } },
        closedSessions: { $sum: { $cond: [{ $eq: ['$status', 'closed'] }, 1, 0] } },
        escalatedSessions: { $sum: { $cond: [{ $ifNull: ['$assignedAgentId', false] }, 1, 0] } },
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
  return stats[0] || { totalSessions: 0, activeSessions: 0, closedSessions: 0, escalatedSessions: 0, avgMessagesPerSession: 0, avgTurnsPerSession: 0 };
};

ChatSessionSchema.pre('save', function (next) {
  this.lastActivityAt = new Date();
  next();
});
