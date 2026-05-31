/**
 * Complaint Record Model
 * Mongoose model for tracking email spam complaints
 */

const mongoose = require('mongoose');

const complaintRecordSchema = new mongoose.Schema({
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

  // Complaint details
  reason: String,

  // Provider info
  provider: {
    type: String,
    enum: ['sendgrid', 'ses', 'mailgun', 'smtp']
  },

  providerMessageId: String,

  // Complaint count
  complaintCount: {
    type: Number,
    default: 1
  },

  // Status - always suppress complaints
  suppressed: {
    type: Boolean,
    default: true
  },

  // Metadata
  metadata: mongoose.Schema.Types.Mixed
}, {
  timestamps: true
});

// Compound index for email + tenant
complaintRecordSchema.index({ email: 1, tenantId: 1 }, { unique: true });
complaintRecordSchema.index({ tenantId: 1, suppressed: 1 });

/**
 * Check if email has complained
 */
complaintRecordSchema.statics.hasComplained = function(email, tenantId) {
  return this.findOne({
    email: email.toLowerCase(),
    tenantId,
    suppressed: true
  });
};

/**
 * Record complaint
 */
complaintRecordSchema.statics.recordComplaint = async function(data) {
  const { email, tenantId, reason, provider } = data;

  const existing = await this.findOne({
    email: email.toLowerCase(),
    tenantId
  });

  if (existing) {
    existing.complaintCount += 1;
    existing.reason = reason || existing.reason;
    existing.provider = provider || existing.provider;
    return existing.save();
  }

  const record = new this({
    email: email.toLowerCase(),
    tenantId,
    reason,
    provider
  });

  return record.save();
};

/**
 * Get complaint statistics
 */
complaintRecordSchema.statics.getStatistics = function(tenantId) {
  return this.aggregate([
    { $match: { tenantId, suppressed: true } },
    {
      $group: {
        _id: null,
        totalComplaints: { $sum: 1 },
        totalReports: { $sum: '$complaintCount' }
      }
    }
  ]);
};

const ComplaintRecord = mongoose.model('ComplaintRecord', complaintRecordSchema);

module.exports = ComplaintRecord;
