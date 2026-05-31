/**
 * Email Template Model
 * Mongoose model for email templates
 */

const mongoose = require('mongoose');

const templateVariableSchema = new mongoose.Schema({
  name: {
    type: String,
    required: true
  },
  description: String,
  defaultValue: mongoose.Schema.Types.Mixed,
  required: {
    type: Boolean,
    default: false
  }
}, { _id: false });

const emailTemplateSchema = new mongoose.Schema({
  // Template identification
  templateId: {
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

  // Template details
  name: {
    type: String,
    required: true
  },

  description: String,

  category: String,

  // Email content
  subject: {
    type: String,
    required: true
  },

  htmlBody: {
    type: String,
    required: true
  },

  textBody: String,

  // Template variables
  variables: [templateVariableSchema],

  // Version control
  version: {
    type: String,
    default: '1.0.0'
  },

  isActive: {
    type: Boolean,
    default: true
  },

  // Tags
  tags: [String],

  // Statistics
  usageCount: {
    type: Number,
    default: 0
  },

  lastUsedAt: Date,

  // Metadata
  metadata: mongoose.Schema.Types.Mixed
}, {
  timestamps: true
});

// Indexes
emailTemplateSchema.index({ tenantId: 1, isActive: 1 });
emailTemplateSchema.index({ tenantId: 1, category: 1 });
emailTemplateSchema.index({ name: 'text', description: 'text' });

/**
 * Increment usage count
 */
emailTemplateSchema.methods.incrementUsage = function() {
  this.usageCount += 1;
  this.lastUsedAt = new Date();
  return this.save();
};

/**
 * Get active templates by tenant
 */
emailTemplateSchema.statics.getActiveByTenant = function(tenantId) {
  return this.find({
    tenantId,
    isActive: true
  }).sort({ name: 1 });
};

/**
 * Get by category
 */
emailTemplateSchema.statics.getByCategory = function(tenantId, category) {
  return this.find({
    tenantId,
    category,
    isActive: true
  }).sort({ name: 1 });
};

/**
 * Search templates
 */
emailTemplateSchema.statics.search = function(tenantId, searchTerm) {
  return this.find({
    tenantId,
    isActive: true,
    $or: [
      { name: { $regex: searchTerm, $options: 'i' } },
      { description: { $regex: searchTerm, $options: 'i' } }
    ]
  }).sort({ name: 1 });
};

/**
 * Create or update template
 */
emailTemplateSchema.statics.upsertByTemplateId = function(templateId, data, tenantId) {
  return this.findOneAndUpdate(
    { templateId, tenantId },
    { $set: data },
    { new: true, upsert: true, runValidators: true }
  );
};

const EmailTemplate = mongoose.model('EmailTemplate', emailTemplateSchema);

module.exports = EmailTemplate;
