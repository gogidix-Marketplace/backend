/**
 * Scoring Rule Model
 * Mongoose model for lead scoring rules
 */

const mongoose = require('mongoose');

const conditionSchema = new mongoose.Schema({
  field: {
    type: String,
    required: true
  },
  operator: {
    type: String,
    enum: ['equals', 'not_equals', 'contains', 'not_contains', 'greater_than', 'less_than', 'in', 'not_in', 'exists', 'not_exists'],
    required: true
  },
  value: mongoose.Schema.Types.Mixed,
  valueType: {
    type: String,
    enum: ['string', 'number', 'boolean', 'array', 'date'],
    default: 'string'
  }
}, { _id: false });

const scoringRuleSchema = new mongoose.Schema({
  // Rule identification
  ruleId: {
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

  // Rule details
  name: {
    type: String,
    required: true
  },

  description: String,

  category: {
    type: String,
    enum: ['demographic', 'behavioral', 'engagement', 'firmographic', 'custom'],
    default: 'custom'
  },

  // Rule conditions (all must match for AND logic)
  conditions: {
    type: [conditionSchema],
    required: true,
    validate: {
      validator: function(v) {
        return v && v.length > 0;
      },
      message: 'At least one condition is required'
    }
  },

  // Logic operator (AND/OR)
  logic: {
    type: String,
    enum: ['AND', 'OR'],
    default: 'AND'
  },

  // Score to add when rule matches
  score: {
    type: Number,
    required: true,
    default: 0
  },

  // Maximum score this rule can contribute
  maxScore: {
    type: Number,
    default: 100
  },

  // Priority for rule evaluation order
  priority: {
    type: Number,
    default: 0
  },

  // Status
  isActive: {
    type: Boolean,
    default: true
  },

  // Time-based constraints
  validFrom: Date,

  validUntil: Date,

  // Applies to specific segments
  segmentIds: [String],

  // Tags
  tags: [String],

  // Metadata
  metadata: mongoose.Schema.Types.Mixed,

  // Statistics
  usageCount: {
    type: Number,
    default: 0
  },

  lastEvaluatedAt: Date
}, {
  timestamps: true
});

// Indexes
scoringRuleSchema.index({ tenantId: 1, isActive: 1 });
scoringRuleSchema.index({ tenantId: 1, category: 1 });
scoringRuleSchema.index({ priority: -1 });

/**
 * Check if rule is valid (within date range)
 */
scoringRuleSchema.methods.isValid = function() {
  if (!this.isActive) {
    return false;
  }

  const now = new Date();

  if (this.validFrom && now < this.validFrom) {
    return false;
  }

  if (this.validUntil && now > this.validUntil) {
    return false;
  }

  return true;
};

/**
 * Evaluate rule against lead data
 */
scoringRuleSchema.methods.evaluate = function(leadData) {
  if (!this.isValid()) {
    return { matched: false, score: 0 };
  }

  const results = this.conditions.map(condition => this.evaluateCondition(condition, leadData));

  let matched = this.logic === 'AND'
    ? results.every(r => r.matched)
    : results.some(r => r.matched);

  if (matched) {
    this.usageCount += 1;
    this.lastEvaluatedAt = new Date();
  }

  return {
    matched,
    score: matched ? this.score : 0,
    conditionResults: results
  };
};

/**
 * Evaluate single condition
 */
scoringRuleSchema.methods.evaluateCondition = function(condition, leadData) => {
  const fieldValue = this.getNestedValue(leadData, condition.field);

  let matched = false;

  switch (condition.operator) {
    case 'equals':
      matched = fieldValue === condition.value;
      break;

    case 'not_equals':
      matched = fieldValue !== condition.value;
      break;

    case 'contains':
      matched = typeof fieldValue === 'string' && fieldValue.includes(condition.value);
      break;

    case 'not_contains':
      matched = typeof fieldValue === 'string' && !fieldValue.includes(condition.value);
      break;

    case 'greater_than':
      matched = typeof fieldValue === 'number' && fieldValue > condition.value;
      break;

    case 'less_than':
      matched = typeof fieldValue === 'number' && fieldValue < condition.value;
      break;

    case 'in':
      matched = Array.isArray(condition.value) && condition.value.includes(fieldValue);
      break;

    case 'not_in':
      matched = Array.isArray(condition.value) && !condition.value.includes(fieldValue);
      break;

    case 'exists':
      matched = fieldValue !== undefined && fieldValue !== null;
      break;

    case 'not_exists':
      matched = fieldValue === undefined || fieldValue === null;
      break;
  }

  return {
    field: condition.field,
    operator: condition.operator,
    expected: condition.value,
    actual: fieldValue,
    matched
  };
};

/**
 * Get nested value from object
 */
scoringRuleSchema.methods.getNestedValue = function(obj, path) => {
  return path.split('.').reduce((current, key) => current?.[key], obj);
};

/**
 * Get active rules by tenant
 */
scoringRuleSchema.statics.getActiveByTenant = function(tenantId, category = null) {
  const query = {
    tenantId,
    isActive: true
  };

  if (category) {
    query.category = category;
  }

  return this.find(query).sort({ priority: -1 });
};

/**
 * Get rule by ID
 */
scoringRuleSchema.statics.getByRuleId = function(ruleId, tenantId) {
  return this.findOne({ ruleId, tenantId });
};

/**
 * Bulk update rules
 */
scoringRuleSchema.statics.bulkUpdate = function(ruleIds, updates, tenantId) {
  return this.updateMany(
    { ruleId: { $in: ruleIds }, tenantId },
    { $set: updates }
  );
};

const ScoringRule = mongoose.model('ScoringRule', scoringRuleSchema);

module.exports = ScoringRule;
