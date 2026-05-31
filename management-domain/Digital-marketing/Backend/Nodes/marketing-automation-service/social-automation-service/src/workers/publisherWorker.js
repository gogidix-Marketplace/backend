/**
 * Publisher Worker
 * Background processor for social media post publishing
 */

const { getQueue } = require('../services/postQueue');
const { publishPost: publishToTwitter } = require('../services/twitterService');
const { publishPost: publishToLinkedIn } = require('../services/linkedinService');
const { publishPost: publishToFacebook } = require('../services/facebookService');
const { publishPost: publishToInstagram } = require('../services/instagramService');
const ScheduledPost = require('../models/ScheduledPost');
const logger = require('../utils/logger');

const CONCURRENCY = parseInt(process.env.POST_QUEUE_CONCURRENCY || '3');
let workerProcessor = null;

/**
 * Get platform service
 */
const getPlatformService = (platform) => {
  const services = {
    twitter: publishToTwitter,
    linkedin: publishToLinkedIn,
    facebook: publishToFacebook,
    instagram: publishToInstagram
  };

  const service = services[platform];
  if (!service) {
    throw new Error(`Unsupported platform: ${platform}`);
  }

  return service;
};

/**
 * Process post job
 */
const processPostJob = async (job) => {
  const { data } = job;
  const { postId, accountId, accountType, content, scheduledFor } = data;

  logger.info('Processing social post job', { postId, accountType });

  try {
    // Get post from database
    const post = await ScheduledPost.findOne({ postId });

    if (!post) {
      throw new Error('Post not found in database');
    }

    // Check if post was cancelled
    if (post.status === 'cancelled') {
      logger.info(`Post ${postId} was cancelled, skipping`);
      return { success: false, reason: 'cancelled' };
    }

    // Update status to publishing
    post.status = 'publishing';
    await post.save();

    // Get the appropriate platform service
    const publishService = getPlatformService(accountType);

    // Publish the post
    const result = await publishService(postId, content, post.content.media, accountId);

    // Update post as published
    await post.markAsPublished(result.platformPostId, result.platformPostUrl);

    // Send webhook if configured
    if (post.webhookUrl) {
      await sendWebhook(post.webhookUrl, {
        event: 'post_published',
        postId,
        platformPostId: result.platformPostId,
        platformPostUrl: result.platformPostUrl
      });
    }

    logger.info('Social post published successfully', {
      postId,
      platformPostId: result.platformPostId
    });

    return {
      success: true,
      postId,
      platformPostId: result.platformPostId,
      platformPostUrl: result.platformPostUrl
    };

  } catch (error) {
    logger.error('Error processing social post job', {
      postId,
      error: error.message,
      stack: error.stack
    });

    // Update job as failed
    const post = await ScheduledPost.findOne({ postId });
    if (post) {
      await post.markAsFailed(error);
    }

    throw error;
  }
};

/**
 * Send webhook notification
 */
const sendWebhook = async (url, data) => {
  try {
    const axios = require('axios');

    await axios.post(url, data, {
      timeout: 5000,
      headers: {
        'Content-Type': 'application/json',
        'User-Agent': 'SocialAutomationService/1.0'
      }
    });

    logger.info('Webhook sent successfully', { url });

  } catch (error) {
    logger.error('Error sending webhook:', error);
    // Don't throw, webhook failures shouldn't fail the job
  }
};

/**
 * Start publisher worker
 */
const startPublisherWorker = async () => {
  try {
    const queue = getQueue();

    if (!queue) {
      throw new Error('Post queue not initialized');
    }

    // Process jobs
    workerProcessor = queue.process('publish-post', CONCURRENCY, async (job) => {
      return processPostJob(job);
    });

    logger.info(`Publisher worker started with concurrency: ${CONCURRENCY}`);

    return workerProcessor;

  } catch (error) {
    logger.error('Error starting publisher worker:', error);
    throw error;
  }
};

/**
 * Stop publisher worker
 */
const stopPublisherWorker = async () => {
  if (workerProcessor) {
    await workerProcessor.close();
    workerProcessor = null;
    logger.info('Publisher worker stopped');
  }
};

module.exports = {
  startPublisherWorker,
  stopPublisherWorker,
  processPostJob
};
