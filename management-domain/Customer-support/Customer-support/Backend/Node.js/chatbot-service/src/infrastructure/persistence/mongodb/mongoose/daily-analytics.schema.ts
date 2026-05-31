import { Schema } from 'mongoose';

export const DailyAnalyticsSchema = new Schema(
  {
    date: { type: Date, required: true, unique: true, index: true },
    totalSessions: { type: Number, default: 0 },
    activeSessions: { type: Number, default: 0 },
    resolvedByBot: { type: Number, default: 0 },
    escalatedToAgent: { type: Number, default: 0 },
    averageResolutionTime: { type: Number, default: 0 },
    averageSessionDuration: { type: Number, default: 0 },
    topIntents: { type: [{ intent: String, count: Number }], default: [] },
    sentimentDistribution: {
      positive: { type: Number, default: 0 },
      neutral: { type: Number, default: 0 },
      negative: { type: Number, default: 0 },
    },
    languageDistribution: { type: Map, of: Number, default: new Map() },
  },
  { timestamps: true, collection: 'daily_analytics' },
);

DailyAnalyticsSchema.index({ date: -1 });

DailyAnalyticsSchema.statics.findByDateRange = function (startDate: Date, endDate: Date) {
  return this.find({ date: { $gte: startDate, $lte: endDate } }).sort({ date: 1 });
};

DailyAnalyticsSchema.statics.getTodayAnalytics = function () {
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  return this.findOne({ date: today });
};
