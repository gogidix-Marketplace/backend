/**
 * Segment Model
 * Mongoose model for lead segments
 */

const mongoose = require('mongoose');

const segmentSchema = new mongoose.Schema({
  // Segment identification
  segmentId: {
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

  // Segment details
  name: {
    type: String,
    required: true
  },

  description: String,

  color: {
    type: String,
    default: '#3498db'
  },

  // Segment criteria (rules that define this segment)
  criteria: {
    minScore: {
      type: Number,
      default: 0
    },
    maxScore: {
      type: Number,
      default: 100
    },
    qualities: [String],
    requiredFlags: [String],
    excludedFlags: [String],
    customRules: [{
      ruleId: String,
      mustMatch: Boolean
    }]
  },

  // Auto-assignment settings
  autoAssign: {
    type: Boolean,
    default: true
  },

  // Segment priority
  priority: {
    type: Number,
    default: 0
  },

  // Status
  isActive: {
    type: Boolean,
    default: true
  },

  // Lead count (cached)
  leadCount: {
    type: Number,
    default: 0
  },

  // Tags
  tags: [String],

  // Metadata
  metadata: mongoose.Schema.Types.Mixed,

  // Statistics
  lastCalculatedAt: Date
}, {
  timestamps: true
});

// Indexes
segmentSchema.index({ tenantId: 1, isActive: 1 });
segmentSchema.index({ tenantId: 1, priority: -1 });

/**
 * Check if lead matches segment criteria
 */
segmentSchema.methods.matches = function(leadScore) {
  if (!this.isActive) {
    return false;
  }

  // Check score range
  if (leadScore.score < this.criteria.minScore || leadScore.score > this.criteria.maxScore) {
    return false;
  }

  // Check quality
  if (this.criteria.qualities && this.criteria.qualities.length > 0) {
    if (!this.criteria.qualities.includes(leadScore.quality)) {
      return false;
    }
  }

  // Check required flags
  if (this.criteria.requiredFlags && this.criteria.requiredFlags.length > 0) {
    const leadFlags = leadScore.flags.map(f => f.type);
    const hasAllFlags = this.criteria.requiredFlags.every(f => leadFlags.includes(f));
    if (!hasAllFlags) {
      return false;
    }
  }

  // Check excluded flags
  if (this.criteria.excludedFlags && this.criteria.excludedFlags.length > 0) {
    const leadFlags = leadScore.flags.map(f => f.type);
    const hasExcludedFlags = this.criteria.excludedFlags.some(f => leadFlags.includes(f));
    if (hasExcludedFlags) {
      return false;
    }
  }

  return true;
};

/**
 * Get active segments by tenant
 */
segmentSchema.statics.getActiveByTenant = function(tenantId) {
  return this.find({
    tenantId,
    isActive: true
  }).sort({ priority: -1 });
};

/**
 * Get segment by ID
 */
segmentSchema.statics.getBySegmentId = function(segmentId, tenantId) {
  return this.findOne({ segmentId, tenantId });
};

/**
 * Update lead count
 */
segmentSchema.methods.updateLeadCount = async function() {
  const LeadScore = mongoose.model('LeadScore');

  const query = { tenantId: this.tenantId };

  if (this.criteria.minScore !== undefined) query.score = { ...query.score, $gte: this.criteria.minScore };
  if (this.criteria.maxScore !== undefined) query.score = { ...query.score, $lte: this.criteria.maxScore };
  if (this.criteria.qualities && this.criteria.qualities.length > 0) query.quality = { $in: this.criteria.qualities };

  this.leadCount = await LeadScore.countDocuments(query);
  this.lastCalculatedAt = new Date();

  return this.save();
};

/**
 * Assign leads to segment
 */
segmentSchema.methods.assignLeads = async function() {
  const LeadScore = mongoose.model('LeadScore');

  const query = { tenantId: this.tenantId };

  if (this.criteria.minScore !== undefined) query.score = { ...query.score, $gte: this.criteria.minScore };
  if (this.criteria.maxScore !== undefined) query.score = { ...query.score, $lte: this.criteria.maxScore };
  if (this.criteria.qualities && this.criteria.qualities.length > 0) query.quality = { $in: this.criteria.qualities };

  const leads = await LeadScore.find(query);

  for (const lead of leads) {
    if (this.matches(lead)) {
      lead.segmentId = this.segmentId;
      lead.segmentAssignedAt = new Date();
      await lead.save();
    }
  }

  this.leadCount = leads.length;
  this.lastCalculatedAt = new Date();

  return this.save();
};

/**
 * Get segment statistics
 */
segmentSchema.statics.getStatistics = function(tenantId) {
  return this.aggregate([
    { $match: { tenantId, isActive: true } },
    {
      $group: {
        _id: null,
        totalSegments: { $sum: 1 },
        totalLeads: { $sum: '$leadCount' },
        avgLeadsPerSegment: { $avg: '$leadCount' }
      }
    }
  ]);
};

const Segment = mongoose.model('Segment', segmentSchema);

module.exports = Segment;
