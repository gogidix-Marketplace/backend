import { Schema } from 'mongoose';

export const SessionAnalyticsSchema = new Schema(
  {
    sessionId: { type: String, required: true, unique: true, index: true },
    userId: String,
    customerId: String,
    duration: { type: Number, required: true },
    messageCount: { type: Number, required: true, min: 0 },
    turnCount: { type: Number, required: true, min: 0 },
    resolutionStatus: { type: String, enum: ['resolved', 'escalated', 'abandoned'], required: true },
    intents: { type: [String], default: [] },
    sentiment: {
      average: { type: Number, min: 0, max: 100, required: true },
      trend: { type: String, enum: ['improving', 'declining', 'stable'], required: true },
    },
    handoffRequired: { type: Boolean, default: false },
    agentId: String,
    customerSatisfaction: { type: Number, min: 1, max: 5 },
  },
  { timestamps: true, collection: 'session_analytics' },
);

SessionAnalyticsSchema.statics.findByDateRange = function (startDate: Date, endDate: Date) {
  return this.find({ createdAt: { $gte: startDate, $lte: endDate } });
};

SessionAnalyticsSchema.statics.findByResolutionStatus = function (status: string) {
  return this.find({ resolutionStatus: status });
};

SessionAnalyticsSchema.statics.getAverageSatisfaction = async function () {
  const result = await this.aggregate([
    { $match: { customerSatisfaction: { $exists: true } } },
    { $group: { _id: null, averageSatisfaction: { $avg: '$customerSatisfaction' }, totalRatings: { $sum: 1 } } },
  ]);
  return result[0] || { averageSatisfaction: 0, totalRatings: 0 };
};

SessionAnalyticsSchema.statics.getResolutionStats = async function () {
  return this.aggregate([
    { $group: { _id: '$resolutionStatus', count: { $sum: 1 }, avgDuration: { $avg: '$duration' } } },
  ]);
};
