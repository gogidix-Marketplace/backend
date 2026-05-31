/**
 * Social Account Model
 * Mongoose model for connected social media accounts
 */

const mongoose = require('mongoose');

const socialAccountSchema = new mongoose.Schema({
  // Account identification
  accountId: {
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

  // Platform
  platform: {
    type: String,
    enum: ['twitter', 'linkedin', 'facebook', 'instagram'],
    required: true
  },

  // Account details
  profile: {
    username: String,
    displayName: String,
    avatar: String,
    bio: String,
    followers: Number,
    following: Number,
    verified: Boolean
  },

  // OAuth tokens
  credentials: {
    accessToken: {
      type: String,
      required: true
    },
    refreshToken: String,
    tokenSecret: String,
    expiresIn: Date,
    scopes: [String]
  },

  // Additional platform-specific config
  config: mongoose.Schema.Types.Mixed,

  // Account status
  isActive: {
    type: Boolean,
    default: true
  },

  isVerified: {
    type: Boolean,
    default: false
  },

  lastUsedAt: Date,

  // Rate limiting info
  rateLimit: {
    limit: Number,
    remaining: Number,
    resetAt: Date
  },

  // Tags
  tags: [String],

  // Metadata
  metadata: mongoose.Schema.Types.Mixed
}, {
  timestamps: true
});

// Indexes
socialAccountSchema.index({ tenantId: 1, platform: 1 });
socialAccountSchema.index({ tenantId: 1, isActive: 1 });

/**
 * Check if token is expired
 */
socialAccountSchema.methods.isTokenExpired = function() {
  if (!this.credentials.expiresIn) {
    return false;
  }
  return new Date() > new Date(this.credentials.expiresIn);
};

/**
 * Update rate limit info
 */
socialAccountSchema.methods.updateRateLimit = function(limit, remaining, resetAt) {
  this.rateLimit = {
    limit,
    remaining,
    resetAt: resetAt ? new Date(resetAt) : null
  };
  return this.save();
};

/**
 * Update last used
 */
socialAccountSchema.methods.updateLastUsed = function() {
  this.lastUsedAt = new Date();
  return this.save();
};

/**
 * Get active accounts by tenant
 */
socialAccountSchema.statics.getActiveByTenant = function(tenantId) {
  return this.find({
    tenantId,
    isActive: true,
    isVerified: true
  });
};

/**
 * Get by platform
 */
socialAccountSchema.statics.getByPlatform = function(tenantId, platform) {
  return this.find({
    tenantId,
    platform,
    isActive: true
  });
};

/**
 * Get account by ID
 */
socialAccountSchema.statics.getByAccountId = function(accountId) {
  return this.findOne({ accountId });
};

/**
 * Mark as inactive
 */
socialAccountSchema.methods.markAsInactive = function() {
  this.isActive = false;
  return this.save();
};

/**
 * Refresh token placeholder
 */
socialAccountSchema.methods.refreshAccessToken = async function() {
  // This would be implemented per-platform
  // For now, just update the last used time
  this.updateLastUsed();
};

const SocialAccount = mongoose.model('SocialAccount', socialAccountSchema);

module.exports = SocialAccount;
