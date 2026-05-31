/**
 * Scheduler Service
 * Cron-based scheduler for scheduled posts
 */

const cron = require('node-cron');
const ScheduledPost = require('../models/ScheduledPost');
const { addPostJob } = require('./postQueue');
const logger = require('../utils/logger');

let scheduledTasks = new Map();
let isRunning = false;

/**
 * Start the scheduler
 */
const startScheduler = async () => {
  if (isRunning) {
    logger.info('Scheduler already running');
    return;
  }

  logger.info('Starting post scheduler');

  // Schedule task to run every minute
  const task = cron.schedule('* * * * *', async () => {
    await checkScheduledPosts();
  }, {
    scheduled: true,
    timezone: process.env.TIMEZONE || 'UTC'
  });

  scheduledTasks.set('main', task);
  isRunning = true;

  // Check for any pending posts on startup
  await checkScheduledPosts();

  logger.info('Post scheduler started');
};

/**
 * Stop the scheduler
 */
const stopScheduler = async () => {
  logger.info('Stopping post scheduler');

  scheduledTasks.forEach((task, name) => {
    task.stop();
    logger.debug(`Stopped scheduled task: ${name}`);
  });

  scheduledTasks.clear();
  isRunning = false;

  logger.info('Post scheduler stopped');
};

/**
 * Check for posts ready to publish
 */
const checkScheduledPosts = async () => {
  try {
    const now = new Date();

    // Find posts that should be published
    const posts = await ScheduledPost.getReadyToPublish();

    if (posts.length === 0) {
      return;
    }

    logger.info(`Found ${posts.length} posts ready to publish`);

    for (const post of posts) {
      try {
        // Update status to publishing
        post.status = 'publishing';
        await post.save();

        // Add to queue
        await addPostJob({
          postId: post.postId,
          accountId: post.accountId,
          accountType: post.accountType,
          content: post.content,
          scheduledFor: post.scheduledFor,
          tenantId: post.tenantId
        });

        logger.info(`Added post to queue: ${post.postId}`);

      } catch (error) {
        logger.error(`Error scheduling post ${post.postId}:`, error);

        // Mark as failed
        await post.markAsFailed(error);
      }
    }

  } catch (error) {
    logger.error('Error checking scheduled posts:', error);
  }
};

/**
 * Schedule a specific post
 */
const schedulePost = async (post) => {
  try {
    const scheduledTime = new Date(post.scheduledFor);
    const now = new Date();

    if (scheduledTime <= now) {
      // Post should be published now
      post.status = 'publishing';
      await post.save();

      await addPostJob({
        postId: post.postId,
        accountId: post.accountId,
        accountType: post.accountType,
        content: post.content,
        scheduledFor: post.scheduledFor,
        tenantId: post.tenantId
      });

      logger.info(`Post ${post.postId} queued for immediate publishing`);
    } else {
      // Post is scheduled for future
      post.status = 'scheduled';
      await post.save();

      logger.info(`Post ${post.postId} scheduled for ${scheduledTime}`);
    }

    return post;

  } catch (error) {
    logger.error('Error scheduling post:', error);
    throw error;
  }
};

/**
 * Cancel scheduled post
 */
const cancelPost = async (postId) => {
  try {
    const post = await ScheduledPost.findOne({ postId });

    if (!post) {
      throw new Error('Post not found');
    }

    if (!['draft', 'scheduled'].includes(post.status)) {
      throw new Error(`Cannot cancel post with status: ${post.status}`);
    }

    // Remove from queue if exists
    const { removeJob } = require('./postQueue');
    try {
      await removeJob(postId);
    } catch (error) {
      // Job might not be in queue, ignore
    }

    post.status = 'cancelled';
    await post.save();

    logger.info(`Post ${postId} cancelled`);

    return post;

  } catch (error) {
    logger.error('Error cancelling post:', error);
    throw error;
  }
};

/**
 * Get scheduler status
 */
const getSchedulerStatus = () => {
  return {
    isRunning,
    scheduledTasksCount: scheduledTasks.size,
    tasks: Array.from(scheduledTasks.keys())
  };
};

module.exports = {
  startScheduler,
  stopScheduler,
  checkScheduledPosts,
  schedulePost,
  cancelPost,
  getSchedulerStatus
};
