/**
 * Analytics MongoDB Model
 */

import mongoose, { Schema } from 'mongoose';
import { ISessionAnalytics, IDailyAnalytics } from '../types';

const SessionAnalyticsSchema = new Schema<ISessionAnalytics>(
  {
    sessionId: {
      type: String,
      required: true,
      unique: true,
      index: true,
    },
    userId: String,
    duration: {
      type: Number,
      required: true,
    },
    messageCount: {
      type: Number,
      required: true,
      min: 0,
    },
    turnCount: {
      type: Number,
      required: true,
      min: 0,
    },
    resolutionStatus: {
      type: String,
      enum: ['resolved', 'escalated', 'abandoned'],
      required: true,
    },
    intents: {
      type: [String],
      default: [],
    },
    sentiment: {
      average: {
        type: Number,
        min: 0,
        max: 100,
        required: true,
      },
      trend: {
        type: String,
        enum: ['improving', 'declining', 'stable'],
        required: true,
      },
    },
    handoffRequired: {
      type: Boolean,
      default: false,
    },
    agentId: String,
    customerSatisfaction: {
      type: Number,
      min: 1,
      max: 5,
    },
  },
  {
    timestamps: true,
    collection: 'session_analytics',
  }
);

const DailyAnalyticsSchema = new Schema<IDailyAnalytics>(
  {
    date: {
      type: Date,
      required: true,
      unique: true,
      index: true,
    },
    totalSessions: {
      type: Number,
      default: 0,
    },
    activeSessions: {
      type: Number,
      default: 0,
    },
    resolvedByBot: {
      type: Number,
      default: 0,
    },
    escalatedToAgent: {
      type: Number,
      default: 0,
    },
    averageResolutionTime: {
      type: Number,
      default: 0,
    },
    averageSessionDuration: {
      type: Number,
      default: 0,
    },
    topIntents: {
      type: [{ intent: String, count: Number }],
      default: [],
    },
    sentimentDistribution: {
      positive: {
        type: Number,
        default: 0,
      },
      neutral: {
        type: Number,
        default: 0,
      },
      negative: {
        type: Number,
        default: 0,
      },
    },
    languageDistribution: {
      type: Map,
      of: Number,
      default: new Map<string, number>(),
    },
  },
  {
    timestamps: true,
    collection: 'daily_analytics',
  }
);

// Indexes
DailyAnalyticsSchema.index({ date: -1 });

// Static methods
DailyAnalyticsSchema.statics.findByDateRange = function (startDate: Date, endDate: Date) {
  return this.find({
    date: { $gte: startDate, $lte: endDate },
  }).sort({ date: 1 });
};

DailyAnalyticsSchema.statics.getTodayAnalytics = function () {
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  return this.findOne({ date: today });
};

// Static methods for SessionAnalytics
SessionAnalyticsSchema.statics.findByDateRange = function (startDate: Date, endDate: Date) {
  return this.find({
    createdAt: { $gte: startDate, $lte: endDate },
  });
};

SessionAnalyticsSchema.statics.findByResolutionStatus = function (status: 'resolved' | 'escalated' | 'abandoned') {
  return this.find({ resolutionStatus: status });
};

SessionAnalyticsSchema.statics.getAverageSatisfaction = async function () {
  const result = await this.aggregate([
    {
      $match: {
        customerSatisfaction: { $exists: true },
      },
    },
    {
      $group: {
        _id: null,
        averageSatisfaction: { $avg: '$customerSatisfaction' },
        totalRatings: { $sum: 1 },
      },
    },
  ]);

  return result[0] || { averageSatisfaction: 0, totalRatings: 0 };
};

SessionAnalyticsSchema.statics.getResolutionStats = async function () {
  const result = await this.aggregate([
    {
      $group: {
        _id: '$resolutionStatus',
        count: { $sum: 1 },
        avgDuration: { $avg: '$duration' },
      },
    },
  ]);

  return result;
};

export const SessionAnalyticsModel = mongoose.model<ISessionAnalytics>(
  'SessionAnalytics',
  SessionAnalyticsSchema
);

export const DailyAnalyticsModel = mongoose.model<IDailyAnalytics>(
  'DailyAnalytics',
  DailyAnalyticsSchema
);
