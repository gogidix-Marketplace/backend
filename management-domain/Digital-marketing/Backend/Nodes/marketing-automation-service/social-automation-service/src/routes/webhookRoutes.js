/**
 * Webhook Routes
 * Handle webhooks from social media platforms
 */

const express = require('express');
const router = express.Router();
const { asyncHandler } = require('../middleware/errorHandler');
const ScheduledPost = require('../models/ScheduledPost');
const PostAnalytics = require('../models/PostAnalytics');
const logger = require('../utils/logger');

/**
 * Verify webhook signature
 */
const verifySignature = (payload, signature, secret) => {
  const crypto = require('crypto');
  const hmac = crypto.createHmac('sha256', secret);
  hmac.update(payload);
  const expectedSignature = hmac.digest('hex');
  return signature === expectedSignature;
};

/**
 * POST /webhook/twitter
 * Handle Twitter webhooks
 */
router.post('/twitter', asyncHandler(async (req, res) => {
  try {
    const events = req.body;

    // Process Twitter events
    if (events.tweet_create_events) {
      for (const event of events.tweet_create_events) {
        await handleTwitterTweetEvent(event);
      }
    }

    // Handle favorite events
    if (events.favorite_events) {
      for (const event of events.favorite_events) {
        await handleTwitterFavoriteEvent(event);
      }
    }

    res.status(200).json({ received: true });

  } catch (error) {
    logger.error('Error processing Twitter webhook:', error);
    res.status(200).json({ received: true }); // Always return 200 to avoid retries
  }
}));

/**
 * Handle Twitter tweet event
 */
const handleTwitterTweetEvent = async (event) => {
  try {
    // Find post by platform post ID
    const post = await ScheduledPost.findOne({
      platformPostId: event.id_str
    });

    if (post) {
      await post.updateMetrics({
        tweets: 1
      });
    }

    // Record analytics
    await PostAnalytics.recordDaily({
      postId: post?.postId || event.id_str,
      tenantId: post?.tenantId,
      platform: 'twitter',
      date: new Date(),
      metrics: {
        tweets: 1,
        impressions: 0
      }
    });

  } catch (error) {
    logger.error('Error handling Twitter tweet event:', error);
  }
};

/**
 * Handle Twitter favorite event
 */
const handleTwitterFavoriteEvent = async (event) => {
  try {
    const post = await ScheduledPost.findOne({
      platformPostId: event.favorited_status.id_str
    });

    if (post) {
      const currentLikes = post.metrics?.likes || 0;
      await post.updateMetrics({ likes: currentLikes + 1 });
    }

  } catch (error) {
    logger.error('Error handling Twitter favorite event:', error);
  }
};

/**
 * POST /webhook/facebook
 * Handle Facebook webhooks
 */
router.post('/facebook', asyncHandler(async (req, res) => {
  try {
    const { entry } = req.body;

    if (entry) {
      for (const item of entry) {
        if (item.changes) {
          for (const change of item.changes) {
            await handleFacebookChange(change);
          }
        }
      }
    }

    res.status(200).json({ received: true });

  } catch (error) {
    logger.error('Error processing Facebook webhook:', error);
    res.status(200).json({ received: true });
  }
}));

/**
 * Handle Facebook change event
 */
const handleFacebookChange = async (change) => {
  try {
    if (change.field === 'feed') {
      const value = change.value;

      if (value.item === 'like') {
        const post = await ScheduledPost.findOne({
          platformPostId: value.post_id
        });

        if (post) {
          const currentLikes = post.metrics?.likes || 0;
          await post.updateMetrics({ likes: currentLikes + 1 });
        }
      }

      if (value.item === 'comment') {
        const post = await ScheduledPost.findOne({
          platformPostId: value.post_id
        });

        if (post) {
          const currentComments = post.metrics?.comments || 0;
          await post.updateMetrics({ comments: currentComments + 1 });
        }
      }

      if (value.item === 'share') {
        const post = await ScheduledPost.findOne({
          platformPostId: value.post_id
        });

        if (post) {
          const currentShares = post.metrics?.shares || 0;
          await post.updateMetrics({ shares: currentShares + 1 });
        }
      }
    }

  } catch (error) {
    logger.error('Error handling Facebook change:', error);
  }
};

/**
 * GET /webhook/facebook
 * Facebook webhook verification
 */
router.get('/facebook', (req, res) => {
  const mode = req.query['hub.mode'];
  const token = req.query['hub.verify_token'];
  const challenge = req.query['hub.challenge'];

  const verifyToken = process.env.FACEBOOK_WEBHOOK_VERIFY_TOKEN || 'gogidix_verify_token';

  if (mode === 'subscribe' && token === verifyToken) {
    res.status(200).send(challenge);
  } else {
    res.sendStatus(403);
  }
});

/**
 * POST /webhook/instagram
 * Handle Instagram webhooks
 */
router.post('/instagram', asyncHandler(async (req, res) => {
  try {
    const { entry } = req.body;

    if (entry) {
      for (const item of entry) {
        if (item.changes) {
          for (const change of item.changes) {
            await handleInstagramChange(change);
          }
        }
      }
    }

    res.status(200).json({ received: true });

  } catch (error) {
    logger.error('Error processing Instagram webhook:', error);
    res.status(200).json({ received: true });
  }
}));

/**
 * Handle Instagram change event
 */
const handleInstagramChange = async (change) => {
  try {
    if (change.field === 'comments') {
      const value = change.value;
      const post = await ScheduledPost.findOne({
        platformPostId: value.media_id
      });

      if (post) {
        const currentComments = post.metrics?.comments || 0;
        await post.updateMetrics({ comments: currentComments + 1 });
      }
    }

    if (change.field === 'mentions' || change.field === 'story_insights') {
      // Handle mentions and story insights
      logger.debug('Instagram webhook event:', { field: change.field, value: change.value });
    }

  } catch (error) {
    logger.error('Error handling Instagram change:', error);
  }
};

/**
 * POST /webhook/linkedin
 * Handle LinkedIn webhooks
 */
router.post('/linkedin', asyncHandler(async (req, res) => {
  try {
    const events = req.body;

    logger.info('LinkedIn webhook received', { events });

    // Process LinkedIn events
    for (const event of events || [])) {
      await handleLinkedInEvent(event);
    }

    res.status(200).json({ received: true });

  } catch (error) {
    logger.error('Error processing LinkedIn webhook:', error);
    res.status(200).json({ received: true });
  }
});

/**
 * Handle LinkedIn event
 */
const handleLinkedInEvent = async (event) => {
  try {
    // Process LinkedIn-specific events
    logger.debug('LinkedIn event processed', { event });

  } catch (error) {
    logger.error('Error handling LinkedIn event:', error);
  }
};

/**
 * POST /webhook/metrics
 * Generic metrics webhook for manual updates
 */
router.post('/metrics', asyncHandler(async (req, res) => {
  try {
    const { postId, platform, metrics } = req.body;

    if (!postId || !platform || !metrics) {
      return res.status(400).json({
        error: 'BadRequest',
        message: 'Missing required fields: postId, platform, metrics'
      });
    }

    const post = await ScheduledPost.findOne({ postId });

    if (post) {
      await post.updateMetrics(metrics);
    }

    // Record daily analytics
    await PostAnalytics.recordDaily({
      postId,
      tenantId: post?.tenantId,
      platform,
      date: new Date(),
      metrics
    });

    res.json({
      success: true,
      message: 'Metrics recorded'
    });

  } catch (error) {
    logger.error('Error recording metrics:', error);
    res.status(500).json({
      error: 'InternalServerError',
      message: 'Failed to record metrics'
    });
  }
}));

module.exports = router;
