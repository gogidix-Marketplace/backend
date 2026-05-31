/**
 * Post Analytics Model
 * Mongoose model for tracking post performance analytics
 */

const mongoose = require('mongoose');

const postAnalyticsSchema = new mongoose.Schema({
  // Post reference
  postId: {
    type: String,
    required: true,
    index: true
  },

  tenantId: {
    type: String,
    required: true,
    index: true
  },

  // Platform
  platform: {
    type: String,
    enum: ['twitter', 'linkedin', 'facebook', 'instagram'],
    required: true
  },

  // Daily metrics
  date: {
    type: Date,
    required: true,
    index: true
  },

  // Engagement metrics
  metrics: {
    likes: { type: Number, default: 0 },
    comments: { type: Number, default: 0 },
    shares: { type: Number, default: 0 },
    clicks: { type: Number, default: 0 },
    impressions: { type: Number, default: 0 },
    reach: { type: Number, default: 0 },
    saves: { type: Number, default: 0 }
  },

  // Demographics (if available)
  demographics: {
    ageRanges: [{
      range: String,
      count: Number
    }],
    genders: [{
      gender: String,
      count: Number
    }],
    locations: [{
      country: String,
      city: String,
      count: Number
    }]
  },

  // Engagement breakdown over time
  timeline: [{
    hour: Number,
    likes: Number,
    comments: Number,
    shares: Number,
    clicks: Number
  }],

  // Top performing content insights
  insights: {
    bestPostingTime: String,
    topHashtags: [String],
    topMentions: [String],
    sentiment: {
      positive: Number,
      neutral: Number,
      negative: Number
    }
  }
}, {
  timestamps: true
});

// Compound index for efficient queries
postAnalyticsSchema.index({ postId: 1, date: -1 });
postAnalyticsSchema.index({ tenantId: 1, date: -1 });
postAnalyticsSchema.index({ tenantId: 1, platform: 1, date: -1 });

/**
 * Get analytics for post
 */
postAnalyticsSchema.statics.getForPost = function(postId) {
  return this.find({ postId }).sort({ date: -1 });
};

/**
 * Get analytics for date range
 */
postAnalyticsSchema.statics.getForDateRange = function(tenantId, startDate, endDate, platform = null) {
  const query = {
    tenantId,
    date: {
      $gte: new Date(startDate),
      $lte: new Date(endDate)
    }
  };

  if (platform) {
    query.platform = platform;
  }

  return this.find(query).sort({ date: -1 });
};

/**
 * Aggregate metrics for posts
 */
postAnalyticsSchema.statics.aggregateMetrics = function(tenantId, postIds) {
  return this.aggregate([
    {
      $match: {
        postId: { $in: postIds }
      }
    },
    {
      $group: {
        _id: '$postId',
        totalLikes: { $sum: '$metrics.likes' },
        totalComments: { $sum: '$metrics.comments' },
        totalShares: { $sum: '$metrics.shares' },
        totalClicks: { $sum: '$metrics.clicks' },
        totalImpressions: { $sum: '$metrics.impressions' },
        totalReach: { $sum: '$metrics.reach' }
      }
    }
  ]);
};

/**
 * Get top performing posts
 */
postAnalyticsSchema.statics.getTopPerforming = function(tenantId, limit = 10) {
  return this.aggregate([
    {
      $match: { tenantId }
    },
    {
      $group: {
        _id: '$postId',
        platform: { $first: '$platform' },
        totalEngagement: {
          $sum: {
            $add: ['$metrics.likes', '$metrics.comments', '$metrics.shares', '$metrics.clicks']
          }
        },
        totalImpressions: { $sum: '$metrics.impressions' }
      }
    },
    {
      $sort: { totalEngagement: -1 }
    },
    {
      $limit: limit
    }
  ]);
};

/**
 * Record daily metrics
 */
postAnalyticsSchema.statics.recordDaily = function(data) {
  return this.findOneAndUpdate(
    {
      postId: data.postId,
      date: new Date(data.date)
    },
    { $set: data },
    { upsert: true, new: true }
  );
};

const PostAnalytics = mongoose.model('PostAnalytics', postAnalyticsSchema);

module.exports = PostAnalytics;
