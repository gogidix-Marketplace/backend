/**
 * Post Queue Service
 * Bull queue management for social media post publishing
 */

const Queue = require('bull');
const { getRedisClient } = require('../config/redis');
const logger = require('../utils/logger');

let postQueue = null;

const QUEUE_NAME = 'social-post-queue';
const CONCURRENCY = parseInt(process.env.POST_QUEUE_CONCURRENCY || '3');
const MAX_ATTEMPTS = parseInt(process.env.POST_QUEUE_MAX_ATTEMPTS || '3');

/**
 * Initialize post queue
 */
const initPostQueue = async () => {
  if (postQueue) {
    logger.info('Post queue already initialized');
    return postQueue;
  }

  const redis = getRedisClient();

  const queueOptions = {
    redis: {
      host: redis.options.host,
      port: redis.options.port,
      password: redis.options.password,
      db: redis.options.db
    },
    defaultJobOptions: {
      attempts: MAX_ATTEMPTS,
      backoff: {
        type: 'exponential',
        delay: 5000
      },
      removeOnComplete: {
        age: 7 * 24 * 3600,
        count: 1000
      },
      removeOnFail: {
        age: 30 * 24 * 3600,
        count: 5000
      },
      timeout: 60000
    }
  };

  postQueue = new Queue(QUEUE_NAME, queueOptions);

  postQueue.on('error', (error) => {
    logger.error('Post queue error:', error);
  });

  postQueue.on('active', (job) => {
    logger.info(`Post job ${job.id} is now processing`, {
      postId: job.data.postId,
      platform: job.data.platform
    });
  });

  postQueue.on('completed', (job, result) => {
    logger.info(`Post job ${job.id} completed`, {
      postId: job.data.postId,
      platformPostId: result?.platformPostId
    });
  });

  postQueue.on('failed', (job, error) => {
    logger.error(`Post job ${job?.id} failed`, {
      postId: job?.data?.postId,
      error: error.message
    });
  });

  logger.info(`Post queue '${QUEUE_NAME}' initialized`);
  return postQueue;
};

/**
 * Add post job to queue
 */
const addPostJob = async (postData, options = {}) => {
  if (!postQueue) {
    throw new Error('Post queue not initialized');
  }

  const { postId, scheduledFor, priority = 'normal' } = postData;

  const bullPriority = priority === 'high' ? 1 : priority === 'low' ? 10 : 5;

  const jobOptions = {
    priority: bullPriority,
    delay: scheduledFor ? new Date(scheduledFor).getTime() - Date.now() : 0,
    attempts: options.attempts || MAX_ATTEMPTS,
    jobId: `post:${postId}`
  };

  const job = await postQueue.add(
    'publish-post',
    postData,
    jobOptions
  );

  logger.info('Post job added to queue', {
    postId,
    scheduledFor,
    priority
  });

  return job;
};

/**
 * Get job by ID
 */
const getJob = async (postId) => {
  if (!postQueue) {
    throw new Error('Post queue not initialized');
  }

  const job = await postQueue.getJob(`post:${postId}`);

  if (!job) {
    return null;
  }

  return {
    id: job.id,
    data: job.data,
    progress: job.progress(),
    attemptsMade: job.attemptsMade,
    failedReason: job.failedReason,
    state: await job.getState()
  };
};

/**
 * Get queue statistics
 */
const getQueueStats = async () => {
  if (!postQueue) {
    throw new Error('Post queue not initialized');
  }

  const [waiting, active, completed, failed, delayed] = await Promise.all([
    postQueue.getWaiting(),
    postQueue.getActive(),
    postQueue.getCompleted(),
    postQueue.getFailed(),
    postQueue.getDelayed()
  ]);

  return {
    waiting: waiting.length,
    active: active.length,
    completed: completed.length,
    failed: failed.length,
    delayed: delayed.length,
    total: waiting.length + active.length + completed.length + failed.length + delayed.length
  };
};

/**
 * Retry failed job
 */
const retryJob = async (postId) => {
  if (!postQueue) {
    throw new Error('Post queue not initialized');
  }

  const job = await postQueue.getJob(`post:${postId}`);

  if (!job) {
    throw new Error('Job not found');
  }

  await job.retry();
  logger.info(`Post job ${postId} queued for retry`);

  return { success: true, postId };
};

/**
 * Remove job
 */
const removeJob = async (postId) => {
  if (!postQueue) {
    throw new Error('Post queue not initialized');
  }

  const job = await postQueue.getJob(`post:${postId}`);

  if (!job) {
    throw new Error('Job not found');
  }

  await job.remove();
  logger.info(`Post job ${postId} removed`);

  return { success: true, postId };
};

/**
 * Close queue
 */
const closePostQueue = async () => {
  if (postQueue) {
    await postQueue.close();
    postQueue = null;
    logger.info('Post queue closed');
  }
};

/**
 * Get queue instance
 */
const getQueue = () => postQueue;

module.exports = {
  initPostQueue,
  addPostJob,
  getJob,
  getQueueStats,
  retryJob,
  removeJob,
  closePostQueue,
  getQueue
};
