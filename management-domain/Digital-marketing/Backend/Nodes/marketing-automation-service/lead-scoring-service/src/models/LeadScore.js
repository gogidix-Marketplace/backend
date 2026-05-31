/**
 * Lead Score Model
 * Mongoose model for lead score cache
 */

const mongoose = require('mongoose');

const leadScoreSchema = new mongoose.Schema({
  // Lead identification
  leadId: {
    type: String,
    required: true,
    index: true
  },

  // Tenant identification
  tenantId: {
    type: String,
    required: true,
    index: true
  },

  // Current score
  score: {
    type: Number,
    default: 0,
    min: 0
  },

  // Maximum possible score
  maxScore: {
    type: Number,
    default: 100
  },

  // Score breakdown by category
  breakdown: {
    demographic: { type: Number, default: 0 },
    behavioral: { type: Number, default: 0 },
    engagement: { type: Number, default: 0 },
    firmographic: { type: Number, default: 0 },
    custom: { type: Number, default: 0 }
  },

  // Score history
  history: [{
    score: Number,
    change: Number,
    reason: String,
    ruleId: String,
    timestamp: {
      type: Date,
      default: Date.now
    }
  }],

  // Last score calculation
  lastCalculatedAt: {
    type: Date,
    default: Date.now
  },

  // Last activity that affected the score
  lastActivityAt: Date,

  // Time-based decay info
  decayInfo: {
    originalScore: Number,
    decayedAmount: Number,
    lastDecayAt: Date
  },

  // Segment assignment
  segmentId: String,

  segmentAssignedAt: Date,

  // Quality classification
  quality: {
    type: String,
    enum: ['hot', 'warm', 'cold', 'unknown'],
    default: 'unknown'
  },

  // Flags
  flags: [{
    type: {
      type: String,
      enum: ['high_priority', 'at_risk', 'dormant', 'vip', 'new']
    },
    since: Date
  }],

  // Metadata
  metadata: mongoose.Schema.Types.Mixed
}, {
  timestamps: true
});

// Compound index for lead + tenant
leadScoreSchema.index({ leadId: 1, tenantId: 1 }, { unique: true });
leadScoreSchema.index({ tenantId: 1, score: -1 });
leadScoreSchema.index({ tenantId: 1, quality: 1 });
leadScoreSchema.index({ tenantId: 1, lastActivityAt: -1 });

/**
 * Update score
 */
leadScoreSchema.methods.updateScore = function(delta, reason, ruleId = null) {
  const oldScore = this.score;
  this.score = Math.max(0, Math.min(this.maxScore, this.score + delta));
  const change = this.score - oldScore;

  this.lastCalculatedAt = new Date();
  this.lastActivityAt = new Date();

  // Add to history
  this.history.push({
    score: this.score,
    change,
    reason,
    ruleId,
    timestamp: new Date()
  });

  // Keep history manageable (last 100 entries)
  if (this.history.length > 100) {
    this.history = this.history.slice(-100);
  }

  // Update quality based on score
  this.updateQuality();

  return this.save();
};

/**
 * Update category breakdown
 */
leadScoreSchema.methods.updateBreakdown = function(category, score) {
  if (this.breakdown.hasOwnProperty(category)) {
    this.breakdown[category] = Math.max(0, score);
  }
  return this.save();
};

/**
 * Update quality classification
 */
leadScoreSchema.methods.updateQuality = function() {
  const scorePercentage = this.score / this.maxScore;

  if (scorePercentage >= 0.8) {
    this.quality = 'hot';
  } else if (scorePercentage >= 0.5) {
    this.quality = 'warm';
  } else if (scorePercentage > 0) {
    this.quality = 'cold';
  } else {
    this.quality = 'unknown';
  }

  return this.save();
};

/**
 * Apply time-based decay
 */
leadScoreSchema.methods.applyDecay = function(decayRate, maxDecayDays) {
  if (!this.lastActivityAt) {
    return this;
  }

  const daysSinceActivity = (Date.now() - this.lastActivityAt.getTime()) / (1000 * 60 * 60 * 24);

  if (daysSinceActivity > maxDecayDays) {
    const daysOverLimit = daysSinceActivity - maxDecayDays;
    const decayAmount = Math.floor(this.score * decayRate * Math.min(daysOverLimit, 30) / 30);

    if (decayAmount > 0) {
      const oldScore = this.score;
      this.score = Math.max(0, this.score - decayAmount);

      this.decayInfo = {
        originalScore: oldScore,
        decayedAmount: decayAmount,
        lastDecayAt: new Date()
      };

      this.history.push({
        score: this.score,
        change: -decayAmount,
        reason: 'time_decay',
        timestamp: new Date()
      });

      this.updateQuality();
    }
  }

  return this.save();
};

/**
 * Add flag
 */
leadScoreSchema.methods.addFlag = function(flagType) {
  const existingFlag = this.flags.find(f => f.type === flagType);

  if (existingFlag) {
    existingFlag.since = new Date();
  } else {
    this.flags.push({ type: flagType, since: new Date() });
  }

  return this.save();
};

/**
 * Remove flag
 */
leadScoreSchema.methods.removeFlag = function(flagType) {
  this.flags = this.flags.filter(f => f.type !== flagType);
  return this.save();
};

/**
 * Get or create lead score
 */
leadScoreSchema.statics.getOrCreate = function(leadId, tenantId) {
  return this.findOneAndUpdate(
    { leadId, tenantId },
    {
      leadId,
      tenantId,
      score: parseInt(process.env.DEFAULT_LEAD_SCORE || '0'),
      maxScore: parseInt(process.env.MAX_LEAD_SCORE || '100')
    },
    { upsert: true, new: true }
  );
};

/**
 * Get scores by quality
 */
leadScoreSchema.statics.getByQuality = function(tenantId, quality) {
  return this.find({ tenantId, quality });
};

/**
 * Get top leads
 */
leadScoreSchema.statics.getTopLeads = function(tenantId, limit = 100) {
  return this.find({ tenantId })
    .sort({ score: -1 })
    .limit(limit);
};

/**
 * Get decayed leads
 */
leadScoreSchema.statics.getDecayedLeads = function(tenantId, threshold = 0.7) {
  const thresholdDate = new Date();
  thresholdDate.setDate(thresholdDate.getDate() - 30);

  return this.find({
    tenantId,
    lastActivityAt: { $lt: thresholdDate },
    score: { $gt: 0 }
  });
};

/**
 * Bulk update segments
 */
leadScoreSchema.statics.bulkUpdateSegments = function(leadScores, segmentId) {
  const leadIds = leadScores.map(ls => ls.leadId);

  return this.updateMany(
    { leadId: { $in: leadIds } },
    {
      segmentId,
      segmentAssignedAt: new Date()
    }
  );
};

/**
 * Get score statistics
 */
leadScoreSchema.statics.getStatistics = function(tenantId) {
  return this.aggregate([
    { $match: { tenantId } },
    {
      $group: {
        _id: null,
        avgScore: { $avg: '$score' },
        maxScore: { $max: '$score' },
        minScore: { $min: '$score' },
        totalLeads: { $sum: 1 },
        hotLeads: {
          $sum: { $cond: [{ $eq: ['$quality', 'hot'] }, 1, 0] }
        },
        warmLeads: {
          $sum: { $cond: [{ $eq: ['$quality', 'warm'] }, 1, 0] }
        },
        coldLeads: {
          $sum: { $cond: [{ $eq: ['$quality', 'cold'] }, 1, 0] }
        }
      }
    }
  ]);
};

const LeadScore = mongoose.model('LeadScore', leadScoreSchema);

module.exports = LeadScore;
