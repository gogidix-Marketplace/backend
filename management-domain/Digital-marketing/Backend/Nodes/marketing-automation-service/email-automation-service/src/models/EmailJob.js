/**
 * Email Job Model
 * Mongoose model for email jobs tracking
 */

const mongoose = require('mongoose');

const emailJobSchema = new mongoose.Schema({
  // Job identification
  jobId: {
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

  // Campaign association
  campaignId: {
    type: String,
    index: true
  },

  // Email details
  from: {
    email: {
      type: String,
      required: true
    },
    name: String
  },

  to: [{
    email: {
      type: String,
      required: true
    },
    name: String
  }],

  cc: [{
    email: String,
    name: String
  }],

  bcc: [{
    email: String,
    name: String
  }],

  replyTo: String,

  subject: {
    type: String,
    required: true
  },

  // Content
  html: String,
  text: String,

  // Template
  templateId: String,
  templateData: mongoose.Schema.Types.Mixed,

  // Attachments
  attachments: [{
    filename: String,
    contentType: String,
    size: Number,
    contentId: String
  }],

  // Provider info
  provider: {
    type: String,
    enum: ['sendgrid', 'ses', 'mailgun', 'smtp'],
    default: 'sendgrid'
  },

  providerMessageId: String,

  // Job status
  status: {
    type: String,
    enum: ['pending', 'queued', 'processing', 'sent', 'delivered', 'opened', 'clicked', 'bounced', 'deferred', 'failed'],
    default: 'pending',
    index: true
  },

  // Priority
  priority: {
    type: String,
    enum: ['high', 'normal', 'low'],
    default: 'normal'
  },

  // Scheduling
  scheduledFor: {
    type: Date,
    index: true
  },

  sentAt: Date,

  // Delivery tracking
  deliveryAttempts: {
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

  // Bounce/complaint info
  bounce: {
    type: String,
    enum: ['hard', 'soft', 'none'],
    default: 'none'
  },

  bounceReason: String,

  complaint: {
    reported: {
      type: Boolean,
      default: false
    },
    reason: String,
    reportedAt: Date
  },

  // Engagement tracking
  opened: {
    type: Boolean,
    default: false
  },

  openedAt: Date,
  openCount: {
    type: Number,
    default: 0
  },

  clicked: {
    type: Boolean,
    default: false
  },

  clickedAt: Date,
  clickCount: {
    type: Number,
    default: 0
  },

  // Tags and metadata
  tags: [String],

  metadata: mongoose.Schema.Types.Mixed,

  // Webhook status
  webhookProcessed: {
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

// Indexes for efficient querying
emailJobSchema.index({ tenantId: 1, status: 1 });
emailJobSchema.index({ tenantId: 1, campaignId: 1 });
emailJobSchema.index({ createdAt: -1 });
emailJobSchema.index({ scheduledFor: 1, status: 1 });
emailJobSchema.index({ 'to.email': 1 });

/**
 * Mark email as sent
 */
emailJobSchema.methods.markAsSent = function(providerMessageId) {
  this.status = 'sent';
  this.sentAt = new Date();
  if (providerMessageId) {
    this.providerMessageId = providerMessageId;
  }
  return this.save();
};

/**
 * Mark email as delivered
 */
emailJobSchema.methods.markAsDelivered = function() {
  this.status = 'delivered';
  return this.save();
};

/**
 * Mark email as opened
 */
emailJobSchema.methods.markAsOpened = function() {
  this.opened = true;
  this.openedAt = new Date();
  this.openCount += 1;
  if (this.status === 'sent') {
    this.status = 'opened';
  }
  return this.save();
};

/**
 * Mark email as clicked
 */
emailJobSchema.methods.markAsClicked = function() {
  this.clicked = true;
  this.clickedAt = new Date();
  this.clickCount += 1;
  if (this.status === 'sent' || this.status === 'opened') {
    this.status = 'clicked';
  }
  return this.save();
};

/**
 * Mark email as bounced
 */
emailJobSchema.methods.markAsBounced = function(bounceType, reason) {
  this.status = 'bounced';
  this.bounce = bounceType || 'hard';
  this.bounceReason = reason;
  return this.save();
};

/**
 * Record delivery attempt
 */
emailJobSchema.methods.recordAttempt = function(error = null) {
  this.deliveryAttempts += 1;
  this.lastAttemptAt = new Date();

  if (error) {
    this.error = {
      message: error.message,
      code: error.code,
      details: error.details
    };

    // Calculate next retry time (exponential backoff)
    const baseDelay = parseInt(process.env.EMAIL_QUEUE_BACKOFF_DELAY || '2000');
    const maxDelay = 86400000; // 24 hours
    const delay = Math.min(baseDelay * Math.pow(2, this.deliveryAttempts), maxDelay);
    this.nextRetryAt = new Date(Date.now() + delay);
  }

  return this.save();
};

/**
 * Get jobs by tenant
 */
emailJobSchema.statics.getByTenant = function(tenantId, options = {}) {
  const query = { tenantId };

  if (options.status) {
    query.status = options.status;
  }

  if (options.campaignId) {
    query.campaignId = options.campaignId;
  }

  if (options.startDate || options.endDate) {
    query.createdAt = {};
    if (options.startDate) query.createdAt.$gte = new Date(options.startDate);
    if (options.endDate) query.createdAt.$lte = new Date(options.endDate);
  }

  const sort = options.sort || { createdAt: -1 };
  const limit = parseInt(options.limit) || 20;
  const skip = parseInt(options.skip) || 0;

  return this.find(query)
    .sort(sort)
    .limit(limit)
    .skip(skip)
    .exec();
};

/**
 * Get job statistics
 */
emailJobSchema.statics.getStatistics = function(tenantId, campaignId = null) {
  const match = { tenantId };
  if (campaignId) {
    match.campaignId = campaignId;
  }

  return this.aggregate([
    { $match: match },
    {
      $group: {
        _id: '$status',
        count: { $sum: 1 }
      }
    }
  ]);
};

const EmailJob = mongoose.model('EmailJob', emailJobSchema);

module.exports = EmailJob;
