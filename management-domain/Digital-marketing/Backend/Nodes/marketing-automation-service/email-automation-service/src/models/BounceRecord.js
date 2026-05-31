/**
 * Bounce Record Model
 * Mongoose model for tracking bounced email addresses
 */

const mongoose = require('mongoose');

const bounceRecordSchema = new mongoose.Schema({
  // Email address
  email: {
    type: String,
    required: true,
    lowercase: true,
    trim: true,
    index: true
  },

  // Tenant identification
  tenantId: {
    type: String,
    required: true,
    index: true
  },

  // Bounce type
  bounceType: {
    type: String,
    enum: ['hard', 'soft'],
    required: true
  },

  // Bounce reason
  reason: String,

  // Bounce code
  code: String,

  // Provider info
  provider: {
    type: String,
    enum: ['sendgrid', 'ses', 'mailgun', 'smtp']
  },

  providerMessageId: String,

  // Bounce count
  bounceCount: {
    type: Number,
    default: 1
  },

  // Status
  isActive: {
    type: Boolean,
    default: true
  },

  // Suppression
  suppressedUntil: Date,

  // Metadata
  metadata: mongoose.Schema.Types.Mixed
}, {
  timestamps: true
});

// Compound index for email + tenant
bounceRecordSchema.index({ email: 1, tenantId: 1 }, { unique: true });
bounceRecordSchema.index({ tenantId: 1, isActive: 1 });
bounceRecordSchema.index({ createdAt: 1 });

/**
 * Check if email is bounced
 */
bounceRecordSchema.statics.isBounced = function(email, tenantId) {
  return this.findOne({
    email: email.toLowerCase(),
    tenantId,
    isActive: true,
    $or: [
      { bounceType: 'hard' },
      { suppressedUntil: { $gt: new Date() } }
    ]
  });
};

/**
 * Record bounce
 */
bounceRecordSchema.statics.recordBounce = async function(data) {
  const { email, tenantId, bounceType, reason, provider } = data;

  const existing = await this.findOne({
    email: email.toLowerCase(),
    tenantId
  });

  if (existing) {
    // Update existing record
    existing.bounceCount += 1;
    existing.reason = reason || existing.reason;
    existing.bounceType = bounceType === 'hard' ? 'hard' : existing.bounceType;
    existing.provider = provider || existing.provider;
    existing.lastBouncedAt = new Date();

    // For hard bounces, suppress permanently
    if (bounceType === 'hard') {
      existing.isActive = true;
      existing.suppressedUntil = null;
    } else if (bounceType === 'soft') {
      // For soft bounces, suppress for 7 days
      existing.suppressedUntil = new Date(Date.now() + 7 * 24 * 60 * 60 * 1000);
    }

    return existing.save();
  }

  // Create new record
  const record = new this({
    email: email.toLowerCase(),
    tenantId,
    bounceType,
    reason,
    provider,
    suppressedUntil: bounceType === 'soft' ? new Date(Date.now() + 7 * 24 * 60 * 60 * 1000) : null
  });

  return record.save();
};

/**
 * Get bounce statistics
 */
bounceRecordSchema.statics.getStatistics = function(tenantId) {
  return this.aggregate([
    { $match: { tenantId, isActive: true } },
    {
      $group: {
        _id: '$bounceType',
        count: { $sum: 1 },
        totalBounces: { $sum: '$bounceCount' }
      }
    }
  ]);
};

/**
 * Clean old soft bounces
 */
bounceRecordSchema.statics.cleanOldSoftBounces = function() {
  const threshold = new Date(Date.now() - 30 * 24 * 60 * 60 * 1000); // 30 days

  return this.updateMany(
    {
      bounceType: 'soft',
      suppressedUntil: { $lt: new Date() },
      createdAt: { $lt: threshold }
    },
    { isActive: false }
  );
};

const BounceRecord = mongoose.model('BounceRecord', bounceRecordSchema);

module.exports = BounceRecord;
