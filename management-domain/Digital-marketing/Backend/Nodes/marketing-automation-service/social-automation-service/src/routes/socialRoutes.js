/**
 * Social Routes
 * API routes for social media operations
 */

const express = require('express');
const router = express.Router();
const multer = require('multer');
const path = require('path');
const { v4: uuidv4 } = require('uuid');
const { asyncHandler } = require('../middleware/errorHandler');
const ScheduledPost = require('../models/ScheduledPost');
const SocialAccount = require('../models/SocialAccount');
const { schedulePost, cancelPost } = require('../services/scheduler');
const { getQueueStats } = require('../services/postQueue');
const Joi = require('joi');

// Multer configuration for file uploads
const storage = multer.diskStorage({
  destination: (req, file, cb) => {
    cb(null, 'uploads/');
  },
  filename: (req, file, cb) => {
    const uniqueName = `${uuidv4()}${path.extname(file.originalname)}`;
    cb(null, uniqueName);
  }
});

const upload = multer({
  storage,
  limits: {
    fileSize: parseInt(process.env.MAX_FILE_SIZE) || 10485760
  },
  fileFilter: (req, file, cb) => {
    const allowedTypes = (process.env.ALLOWED_FILE_TYPES || 'image/jpeg,image/png,image/gif,video/mp4').split(',');
    if (allowedTypes.includes(file.mimetype)) {
      cb(null, true);
    } else {
      cb(new Error('Invalid file type'));
    }
  }
});

// Validation schemas
const createPostSchema = Joi.object({
  accountId: Joi.string().required(),
  content: Joi.object({
    text: Joi.string().max(10000).required(),
    link: Joi.string().uri(),
    media: Joi.array().items(Joi.object({
      type: Joi.string().valid('image', 'video', 'gif').required(),
      url: Joi.string().uri(),
      altText: Joi.string()
    }))
  }).required(),
  scheduledFor: Joi.date().min('now').required(),
  timezone: Joi.string().default('UTC'),
  priority: Joi.string().valid('high', 'normal', 'low').default('normal'),
  campaignId: Joi.string(),
  tags: Joi.array().items(Joi.string().max(50)).max(10),
  webhookUrl: Joi.string().uri(),
  tenantId: Joi.string().required()
});

/**
 * POST /api/v1/posts
 * Create scheduled post
 */
router.post('/', upload.array('media', 10), asyncHandler(async (req, res) => {
  // Handle both JSON and multipart form data
  let postData;
  if (req.body.data) {
    postData = JSON.parse(req.body.data);
  } else {
    postData = req.body;
  }

  const { error, value } = createPostSchema.validate(postData);
  if (error) {
    return res.status(400).json({
      error: 'ValidationError',
      message: 'Invalid request data',
      errors: error.details.map(d => d.message)
    });
  }

  // Get account
  const account = await SocialAccount.getByAccountId(value.accountId);
  if (!account) {
    return res.status(404).json({
      error: 'NotFound',
      message: 'Social account not found'
    });
  }

  // Handle uploaded files
  const mediaFiles = [];
  if (req.files && req.files.length > 0) {
    req.files.forEach(file => {
      mediaFiles.push({
        type: file.mimetype.startsWith('video') ? 'video' : 'image',
        path: file.path,
        filename: file.filename,
        mimeType: file.mimetype,
        size: file.size,
        url: `/uploads/${file.filename}`
      });
    });
  }

  // Combine with media from request body
  if (value.content.media && value.content.media.length > 0) {
    value.content.media.forEach(item => {
      mediaFiles.push(item);
    });
  }

  const postId = uuidv4();

  const post = new ScheduledPost({
    postId,
    tenantId: value.tenantId,
    accountId: value.accountId,
    accountType: account.platform,
    content: {
      text: value.content.text,
      link: value.content.link,
      media: mediaFiles
    },
    scheduledFor: value.scheduledFor,
    timezone: value.timezone,
    priority: value.priority,
    campaignId: value.campaignId,
    tags: value.tags,
    webhookUrl: value.webhookUrl,
    status: 'draft'
  });

  await post.save();

  // Schedule the post
  await schedulePost(post);

  res.status(201).json({
    success: true,
    message: 'Post scheduled successfully',
    data: post
  });
}));

/**
 * GET /api/v1/posts
 * List scheduled posts
 */
router.get('/', asyncHandler(async (req, res) => {
  const tenantId = req.headers['x-tenant-id'] || req.query.tenantId;

  if (!tenantId) {
    return res.status(400).json({
      error: 'BadRequest',
      message: 'Tenant ID is required'
    });
  }

  const options = {
    status: req.query.status,
    accountType: req.query.accountType,
    campaignId: req.query.campaignId,
    startDate: req.query.startDate,
    endDate: req.query.endDate,
    limit: parseInt(req.query.limit) || 20,
    skip: (parseInt(req.query.page) - 1) * (parseInt(req.query.limit) || 20),
    sort: { [req.query.sortBy || 'scheduledFor']: req.query.sortOrder === 'asc' ? 1 : -1 }
  };

  const posts = await ScheduledPost.getByTenant(tenantId, options);
  const total = await ScheduledPost.countDocuments({ tenantId, ...options });

  res.json({
    success: true,
    data: {
      posts,
      pagination: {
        page: parseInt(req.query.page) || 1,
        limit: options.limit,
        total
      }
    }
  });
}));

/**
 * GET /api/v1/posts/:postId
 * Get post by ID
 */
router.get('/:postId', asyncHandler(async (req, res) => {
  const { postId } = req.params;

  const post = await ScheduledPost.findOne({ postId });

  if (!post) {
    return res.status(404).json({
      error: 'NotFound',
      message: 'Post not found'
    });
  }

  res.json({
    success: true,
    data: post
  });
}));

/**
 * PUT /api/v1/posts/:postId
 * Update scheduled post
 */
router.put('/:postId', asyncHandler(async (req, res) => {
  const { postId } = req.params;

  const post = await ScheduledPost.findOne({ postId });

  if (!post) {
    return res.status(404).json({
      error: 'NotFound',
      message: 'Post not found'
    });
  }

  if (!['draft', 'scheduled'].includes(post.status)) {
    return res.status(400).json({
      error: 'BadRequest',
      message: 'Cannot update post with status: ' + post.status
    });
  }

  const allowedUpdates = ['content', 'scheduledFor', 'priority', 'tags', 'webhookUrl'];
  const updates = {};

  allowedUpdates.forEach(key => {
    if (req.body[key] !== undefined) {
      updates[key] = req.body[key];
    }
  });

  Object.assign(post, updates);
  await post.save();

  // Reschedule if time changed
  if (updates.scheduledFor) {
    await schedulePost(post);
  }

  res.json({
    success: true,
    message: 'Post updated',
    data: post
  });
}));

/**
 * DELETE /api/v1/posts/:postId
 * Cancel/delete post
 */
router.delete('/:postId', asyncHandler(async (req, res) => {
  const { postId } = req.params;

  const post = await cancelPost(postId);

  res.json({
    success: true,
    message: 'Post cancelled',
    data: post
  });
}));

/**
 * POST /api/v1/posts/:postId/retry
 * Retry failed post
 */
router.post('/:postId/retry', asyncHandler(async (req, res) => {
  const { postId } = req.params;

  const post = await ScheduledPost.findOne({ postId });

  if (!post) {
    return res.status(404).json({
      error: 'NotFound',
      message: 'Post not found'
    });
  }

  if (post.status !== 'failed') {
    return res.status(400).json({
      error: 'BadRequest',
      message: 'Can only retry failed posts'
    });
  }

  // Reset status
  post.status = 'scheduled';
  post.publishAttempts = 0;
  post.error = undefined;

  await post.save();

  // Reschedule
  await schedulePost(post);

  res.json({
    success: true,
    message: 'Post queued for retry',
    data: post
  });
}));

/**
 * GET /api/v1/stats
 * Get statistics
 */
router.get('/stats/summary', asyncHandler(async (req, res) => {
  const tenantId = req.headers['x-tenant-id'] || req.query.tenantId;

  const [statusStats, platformStats, queueStats] = await Promise.all([
    ScheduledPost.getStatistics(tenantId),
    ScheduledPost.getPlatformBreakdown(tenantId),
    getQueueStats()
  ]);

  res.json({
    success: true,
    data: {
      byStatus: statusStats,
      byPlatform: platformStats,
      queue: queueStats
    }
  });
}));

// ==================== Account Routes ====================

/**
 * GET /api/v1/accounts
 * List social accounts
 */
router.get('/accounts/list', asyncHandler(async (req, res) => {
  const tenantId = req.headers['x-tenant-id'] || req.query.tenantId;

  const accounts = await SocialAccount.getActiveByTenant(tenantId);

  res.json({
    success: true,
    data: {
      accounts: accounts.map(a => ({
        accountId: a.accountId,
        platform: a.platform,
        profile: a.profile,
        isActive: a.isActive,
        isVerified: a.isVerified
      }))
    }
  });
}));

/**
 * POST /api/v1/accounts/connect
 * Connect social account (placeholder for OAuth flow)
 */
router.post('/accounts/connect', asyncHandler(async (req, res) => {
  const { platform } = req.body;

  if (!['twitter', 'linkedin', 'facebook', 'instagram'].includes(platform)) {
    return res.status(400).json({
      error: 'BadRequest',
      message: 'Invalid platform'
    });
  }

  // This would typically redirect to the OAuth flow
  // For now, return a placeholder
  res.json({
    success: true,
    message: 'OAuth flow initiated',
    data: {
      platform,
      oauthUrl: `/api/v1/auth/${platform}`
    }
  });
}));

module.exports = router;
