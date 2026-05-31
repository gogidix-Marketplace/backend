/**
 * Scheduled Post Model
 * Mongoose model for scheduled social media posts
 */

const mongoose = require('mongoose');

const mediaAttachmentSchema = new mongoose.Schema({
  type: {
    type: String,
    enum: ['image', 'video', 'gif', 'document'],
    required: true
  },
  url: String,
  path: String,
  filename: String,
  mimeType: String,
  size: Number,
  altText: String,
  metadata: mongoose.Schema.Types.Mixed
}, { _id: false });

const scheduledPostSchema = new mongoose.Schema({
  // Post identification
  postId: {
    type: String,
    required: true,
    unique: true,
    index: true
  },

  // Tenant identification
  tenantId: {
    type: String,
    required: true,
    index: true
  },

  // Account info
  accountId: {
    type: String,
    required: true,
    index: true
  },

  accountType: {
    type: String,
    enum: ['twitter', 'linkedin', 'facebook', 'instagram'],
    required: true
  },

  // Post content
  content: {
    text: {
      type: String,
      required: true,
      maxlength: 10000
    },
    media: [mediaAttachmentSchema],
    link: String,
    linkPreview: {
      title: String,
      description: String,
      image: String
    }
  },

  // Scheduling
  scheduledFor: {
    type: Date,
    required: true,
    index: true
  },

  timezone: {
    type: String,
    default: 'UTC'
  },

  // Post status
  status: {
    type: String,
    enum: ['draft', 'scheduled', 'publishing', 'published', 'failed', 'cancelled'],
    default: 'draft',
    index: true
  },

  // Published info
  publishedAt: Date,

  platformPostId: String,

  platformPostUrl: String,

  // Engagement tracking
  metrics: {
    likes: { type: Number, default: 0 },
    comments: { type: Number, default: 0 },
    shares: { type: Number, default: 0 },
    clicks: { type: Number, default: 0 },
    impressions: { type: Number, default: 0 },
    reach: { type: Number, default: 0 },
    lastUpdated: Date
  },

  // Priority
  priority: {
    type: String,
    enum: ['high', 'normal', 'low'],
    default: 'normal'
  },

  // Campaign association
  campaignId: String,

  // Tags
  tags: [String],

  // Metadata
  metadata: mongoose.Schema.Types.Mixed,

  // Retry info
  publishAttempts: {
    type: Number,
    default: 0
  },

  lastAttemptAt: Date,

  nextRetryAt: Date,

  // Error tracking
  error: {
    message: String,
    code: String,
    details: mongoose.Schema.Types.Mixed
  },

  // Webhook status
  webhookUrl: String,

  webhookSent: {
    type: Boolean,
    default: false
  },

  webhookAttempts: {
    type: Number,
    default: 0
  }
}, {
  timestamps: true
});

// Indexes
scheduledPostSchema.index({ tenantId: 1, status: 1 });
scheduledPostSchema.index({ tenantId: 1, scheduledFor: 1 });
scheduledPostSchema.index({ accountType: 1, status: 1 });
scheduledPostSchema.index({ campaignId: 1 });

/**
 * Mark as published
 */
scheduledPostSchema.methods.markAsPublished = function(platformPostId, platformPostUrl) {
  this.status = 'published';
  this.publishedAt = new Date();
  if (platformPostId) this.platformPostId = platformPostId;
  if (platformPostUrl) this.platformPostUrl = platformPostUrl;
  return this.save();
};

/**
 * Mark as failed
 */
scheduledPostSchema.methods.markAsFailed = function(error) {
  this.status = 'failed';
  this.error = {
    message: error?.message || 'Unknown error',
    code: error?.code,
    details: error?.details
  };
  this.publishAttempts += 1;
  this.lastAttemptAt = new Date();
  return this.save();
};

/**
 * Record publish attempt
 */
scheduledPostSchema.methods.recordAttempt = function(error = null) {
  this.publishAttempts += 1;
  this.lastAttemptAt = new Date();

  if (error) {
    this.error = {
      message: error.message,
      code: error.code,
      details: error.details
    };
  }

  return this.save();
};

/**
 * Update metrics
 */
scheduledPostSchema.methods.updateMetrics = function(metrics) {
  if (!this.metrics) {
    this.metrics = {};
  }

  Object.keys(metrics).forEach(key => {
    if (this.metrics[key] !== undefined) {
      this.metrics[key] = metrics[key];
    }
  });

  this.metrics.lastUpdated = new Date();
  return this.save();
};

/**
 * Get scheduled posts ready to publish
 */
scheduledPostSchema.statics.getReadyToPublish = function() {
  return this.find({
    status: 'scheduled',
    scheduledFor: { $lte: new Date() }
  }).sort({ scheduledFor: 1, priority: 1 });
};

/**
 * Get by tenant with filters
 */
scheduledPostSchema.statics.getByTenant = function(tenantId, options = {}) {
  const query = { tenantId };

  if (options.status) {
    query.status = options.status;
  }

  if (options.accountType) {
    query.accountType = options.accountType;
  }

  if (options.campaignId) {
    query.campaignId = options.campaignId;
  }

  if (options.startDate || options.endDate) {
    query.scheduledFor = {};
    if (options.startDate) query.scheduledFor.$gte = new Date(options.startDate);
    if (options.endDate) query.scheduledFor.$lte = new Date(options.endDate);
  }

  const sort = options.sort || { scheduledFor: -1 };
  const limit = parseInt(options.limit) || 20;
  const skip = parseInt(options.skip) || 0;

  return this.find(query)
    .sort(sort)
    .limit(limit)
    .skip(skip)
    .exec();
};

/**
 * Get statistics
 */
scheduledPostSchema.statics.getStatistics = function(tenantId) {
  return this.aggregate([
    { $match: { tenantId } },
    {
      $group: {
        _id: '$status',
        count: { $sum: 1 }
      }
    }
  ]);
};

/**
 * Get platform breakdown
 */
scheduledPostSchema.statics.getPlatformBreakdown = function(tenantId) {
  return this.aggregate([
    { $match: { tenantId } },
    {
      $group: {
        _id: '$accountType',
        count: { $sum: 1 }
      }
    }
  ]);
};

const ScheduledPost = mongoose.model('ScheduledPost', scheduledPostSchema);

module.exports = ScheduledPost;
