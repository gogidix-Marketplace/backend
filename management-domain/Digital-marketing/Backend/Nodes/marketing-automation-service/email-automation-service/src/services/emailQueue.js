/**
 * Email Queue Service
 * Bull queue management for email processing
 */

const Queue = require('bull');
const { getRedisClient } = require('../config/redis');
const logger = require('../utils/logger');

let emailQueue = null;

// Queue configuration
const QUEUE_NAME = 'email-queue';
const CONCURRENCY = parseInt(process.env.EMAIL_QUEUE_CONCURRENCY || '5');
const MAX_ATTEMPTS = parseInt(process.env.EMAIL_QUEUE_MAX_ATTEMPTS || '3');
const BACKOFF_TYPE = process.env.EMAIL_QUEUE_BACKOFF_TYPE || 'exponential';
const BACKOFF_DELAY = parseInt(process.env.EMAIL_QUEUE_BACKOFF_DELAY || '2000');

/**
 * Initialize email queue
 */
const initEmailQueue = async () => {
  if (emailQueue) {
    logger.info('Email queue already initialized');
    return emailQueue;
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
        type: BACKOFF_TYPE,
        delay: BACKOFF_DELAY
      },
      removeOnComplete: {
        age: 24 * 3600, // Keep completed jobs for 24 hours
        count: 1000
      },
      removeOnFail: {
        age: 7 * 24 * 3600, // Keep failed jobs for 7 days
        count: 5000
      },
      timeout: 30000 // 30 seconds timeout
    }
  };

  emailQueue = new Queue(QUEUE_NAME, queueOptions);

  // Queue event handlers
  emailQueue.on('error', (error) => {
    logger.error('Queue error:', error);
  });

  emailQueue.on('waiting', (jobId) => {
    logger.debug(`Job ${jobId} is waiting`);
  });

  emailQueue.on('active', (job, jobPromise) => {
    logger.info(`Job ${job.id} is now processing`, {
      jobId: job.id,
      data: job.data
    });
  });

  emailQueue.on('completed', (job, result) => {
    logger.info(`Job ${job.id} completed successfully`, {
      jobId: job.id,
      provider: result?.provider
    });
  });

  emailQueue.on('failed', (job, error) => {
    logger.error(`Job ${job?.id} failed`, {
      jobId: job?.id,
      error: error.message,
      attemptsMade: job?.attemptsMade
    });
  });

  emailQueue.on('stalled', (job) => {
    logger.warn(`Job ${job} stalled`);
  });

  emailQueue.on('removed', (job) => {
    logger.info(`Job ${job} removed`);
  });

  logger.info(`Email queue '${QUEUE_NAME}' initialized`);
  return emailQueue;
};

/**
 * Add email job to queue
 */
const addEmailJob = async (jobData, options = {}) => {
  if (!emailQueue) {
    throw new Error('Email queue not initialized');
  }

  const {
    to,
    subject,
    html,
    text,
    templateId,
    templateData,
    from,
    replyTo,
    attachments,
    tags,
    metadata,
    priority = 'normal',
    scheduledFor,
    tenantId,
    campaignId
  } = jobData;

  // Validate required fields
  if (!to || !subject) {
    throw new Error('Missing required fields: to, subject');
  }

  // Determine job priority
  const bullPriority = priority === 'high' ? 1 : priority === 'low' ? 10 : 5;

  // Job options
  const jobOptions = {
    priority: bullPriority,
    delay: scheduledFor ? new Date(scheduledFor).getTime() - Date.now() : 0,
    attempts: options.attempts || MAX_ATTEMPTS,
    jobId: options.jobId || undefined
  };

  const job = await emailQueue.add(
    'send-email',
    {
      to,
      subject,
      html,
      text,
      templateId,
      templateData,
      from,
      replyTo,
      attachments,
      tags,
      metadata,
      tenantId,
      campaignId
    },
    jobOptions
  );

  logger.info('Email job added to queue', {
    jobId: job.id,
    tenantId,
    priority,
    scheduledFor
  });

  return job;
};

/**
 * Add batch email jobs
 */
const addBatchEmailJobs = async (emails, options = {}) => {
  if (!emailQueue) {
    throw new Error('Email queue not initialized');
  }

  const jobs = emails.map(email => ({
    name: 'send-email',
    data: email,
    opts: {
      priority: email.priority === 'high' ? 1 : email.priority === 'low' ? 10 : 5,
      delay: options.scheduleAt ? new Date(options.scheduleAt).getTime() - Date.now() : 0
    }
  }));

  const addedJobs = await emailQueue.addBulk(jobs);

  logger.info('Batch email jobs added to queue', {
    count: jobs.length,
    tenantId: options.tenantId
  });

  return addedJobs;
};

/**
 * Get job by ID
 */
const getJob = async (jobId) => {
  if (!emailQueue) {
    throw new Error('Email queue not initialized');
  }

  const job = await emailQueue.getJob(jobId);

  if (!job) {
    return null;
  }

  return {
    id: job.id,
    name: job.name,
    data: job.data,
    opts: job.opts,
    progress: job.progress(),
    attemptsMade: job.attemptsMade,
    failedReason: job.failedReason,
    stacktrace: job.stacktrace,
    returnvalue: job.returnvalue,
    finishedOn: job.finishedOn,
    processedOn: job.processedOn,
    state: await job.getState()
  };
};

/**
 * Get queue statistics
 */
const getQueueStats = async () => {
  if (!emailQueue) {
    throw new Error('Email queue not initialized');
  }

  const [waiting, active, completed, failed, delayed] = await Promise.all([
    emailQueue.getWaiting(),
    emailQueue.getActive(),
    emailQueue.getCompleted(),
    emailQueue.getFailed(),
    emailQueue.getDelayed()
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
const retryJob = async (jobId) => {
  if (!emailQueue) {
    throw new Error('Email queue not initialized');
  }

  const job = await emailQueue.getJob(jobId);

  if (!job) {
    throw new Error('Job not found');
  }

  await job.retry();
  logger.info(`Job ${jobId} queued for retry`);

  return { success: true, jobId };
};

/**
 * Remove job
 */
const removeJob = async (jobId) => {
  if (!emailQueue) {
    throw new Error('Email queue not initialized');
  }

  const job = await emailQueue.getJob(jobId);

  if (!job) {
    throw new Error('Job not found');
  }

  await job.remove();
  logger.info(`Job ${jobId} removed`);

  return { success: true, jobId };
};

/**
 * Get failed jobs
 */
const getFailedJobs = async (start = 0, end = 100) => {
  if (!emailQueue) {
    throw new Error('Email queue not initialized');
  }

  return emailQueue.getFailed(start, end);
};

/**
 * Clean old jobs
 */
const cleanQueue = async (grace = 5000) => {
  if (!emailQueue) {
    throw new Error('Email queue not initialized');
  }

  await emailQueue.clean(grace, 'completed');
  await emailQueue.clean(grace * 10, 'failed');

  logger.info('Queue cleaned');
};

/**
 * Close queue
 */
const closeEmailQueue = async () => {
  if (emailQueue) {
    await emailQueue.close();
    emailQueue = null;
    logger.info('Email queue closed');
  }
};

/**
 * Pause queue
 */
const pauseQueue = async () => {
  if (!emailQueue) {
    throw new Error('Email queue not initialized');
  }

  await emailQueue.pause();
  logger.info('Email queue paused');
};

/**
 * Resume queue
 */
const resumeQueue = async () => {
  if (!emailQueue) {
    throw new Error('Email queue not initialized');
  }

  await emailQueue.resume();
  logger.info('Email queue resumed');
};

module.exports = {
  initEmailQueue,
  addEmailJob,
  addBatchEmailJobs,
  getJob,
  getQueueStats,
  retryJob,
  removeJob,
  getFailedJobs,
  cleanQueue,
  closeEmailQueue,
  pauseQueue,
  resumeQueue,
  getQueue: () => emailQueue
};
