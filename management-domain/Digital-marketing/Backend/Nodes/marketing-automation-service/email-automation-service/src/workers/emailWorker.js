/**
 * Email Worker
 * Background processor for email queue jobs
 */

const { getQueue } = require('../services/emailQueue');
const { sendEmail: sendViaSendGrid } = require('../services/sendGridService');
const { sendEmail: sendViaSES, sendEmailWithAttachments } = require('../services/sesService');
const { renderTemplate } = require('../services/templateService');
const EmailJob = require('../models/EmailJob');
const BounceRecord = require('../models/BounceRecord');
const ComplaintRecord = require('../models/ComplaintRecord');
const logger = require('../utils/logger');
const { v4: uuidv4 } = require('uuid');

// Worker configuration
const CONCURRENCY = parseInt(process.env.EMAIL_QUEUE_CONCURRENCY || '5');
const EMAIL_PROVIDER = process.env.EMAIL_PROVIDER || 'sendgrid';
const ENABLE_PROVIDER_FALLBACK = process.env.ENABLE_PROVIDER_FALLBACK === 'true';

let workerProcessor = null;

/**
 * Process email job
 */
const processEmailJob = async (job) => {
  const { data } = job;
  const jobId = data.jobId || job.id;

  logger.info('Processing email job', { jobId, data: { to: data.to, subject: data.subject } });

  try {
    // Check for bounces and complaints
    const recipients = Array.isArray(data.to) ? data.to : [data.to];
    const emailAddresses = recipients.map(r => typeof r === 'string' ? r : r.email);

    for (const email of emailAddresses) {
      // Check bounce record
      const bounced = await BounceRecord.isBounced(email, data.tenantId);
      if (bounced) {
        logger.warn('Email bounced, skipping', { email, bounceType: bounced.bounceType });
        await markJobAsBounced(jobId, email, bounced.bounceType, 'Previously bounced');
        throw new Error(`Email ${email} is on bounce list (${bounced.bounceType})`);
      }

      // Check complaint record
      const complained = await ComplaintRecord.hasComplained(email, data.tenantId);
      if (complained) {
        logger.warn('Email has complained, skipping', { email });
        await markJobAsBounced(jobId, email, 'complaint', 'Previously complained');
        throw new Error(`Email ${email} has previously complained`);
      }
    }

    // Render template if templateId is provided
    let subject = data.subject;
    let html = data.html;
    let text = data.text;

    if (data.templateId) {
      logger.info('Rendering template', { templateId: data.templateId });
      const rendered = await renderTemplate(data.templateId, data.templateData || {}, data.tenantId);
      subject = rendered.subject;
      html = rendered.html;
      text = rendered.text;
    }

    // Prepare email data
    const emailData = {
      to: data.to,
      cc: data.cc,
      bcc: data.bcc,
      subject,
      html,
      text,
      from: data.from,
      replyTo: data.replyTo,
      attachments: data.attachments,
      tags: data.tags,
      metadata: {
        ...data.metadata,
        jobId,
        tenantId: data.tenantId,
        campaignId: data.campaignId
      }
    };

    // Create or update job record
    let emailJob = await EmailJob.findOne({ jobId });
    if (!emailJob) {
      emailJob = new EmailJob({
        jobId,
        tenantId: data.tenantId,
        campaignId: data.campaignId,
        from: emailData.from || { email: process.env.SENDGRID_FROM_EMAIL || 'noreply@gogidix.com' },
        to: recipients.map(r => typeof r === 'string' ? { email: r } : r),
        cc: emailData.cc,
        bcc: emailData.bcc,
        replyTo: emailData.replyTo,
        subject,
        html,
        text,
        templateId: data.templateId,
        templateData: data.templateData,
        attachments: (emailData.attachments || []).map(a => ({
          filename: a.filename,
          contentType: a.contentType,
          size: a.content?.length || 0,
          contentId: a.contentId
        })),
        tags: emailData.tags,
        metadata: emailData.metadata,
        priority: data.priority || 'normal'
      });
    }

    emailJob.status = 'processing';
    await emailJob.save();

    // Send email via primary provider
    let result;
    const hasAttachments = emailData.attachments && emailData.attachments.length > 0;

    if (EMAIL_PROVIDER === 'sendgrid') {
      result = await sendViaSendGrid(emailData);
    } else if (EMAIL_PROVIDER === 'ses') {
      if (hasAttachments) {
        result = await sendEmailWithAttachments(emailData);
      } else {
        result = await sendViaSES(emailData);
      }
    } else {
      throw new Error(`Invalid email provider: ${EMAIL_PROVIDER}`);
    }

    // Handle fallback if enabled and primary failed
    if (!result.success && ENABLE_PROVIDER_FALLBACK) {
      logger.info('Primary provider failed, trying fallback');

      const fallbackProvider = EMAIL_PROVIDER === 'sendgrid' ? 'ses' : 'sendgrid';
      emailJob.provider = fallbackProvider;

      if (fallbackProvider === 'sendgrid') {
        result = await sendViaSendGrid(emailData);
      } else {
        if (hasAttachments) {
          result = await sendEmailWithAttachments(emailData);
        } else {
          result = await sendViaSES(emailData);
        }
      }
    }

    // Update job as sent
    await emailJob.markAsSent(result.messageId);

    logger.info('Email sent successfully', {
      jobId,
      messageId: result.messageId,
      provider: result.provider
    });

    return {
      success: true,
      jobId,
      messageId: result.messageId,
      provider: result.provider
    };

  } catch (error) {
    logger.error('Error processing email job', {
      jobId,
      error: error.message,
      stack: error.stack
    });

    // Update job as failed
    const emailJob = await EmailJob.findOne({ jobId });
    if (emailJob) {
      await emailJob.recordAttempt(error);

      if (emailJob.deliveryAttempts >= parseInt(process.env.EMAIL_QUEUE_MAX_ATTEMPTS || '3')) {
        emailJob.status = 'failed';
        await emailJob.save();
      }
    }

    throw error;
  }
};

/**
 * Mark job as bounced
 */
const markJobAsBounced = async (jobId, email, bounceType, reason) => {
  try {
    const emailJob = await EmailJob.findOne({ jobId });
    if (emailJob) {
      await emailJob.markAsBounced(bounceType, reason);
    }
  } catch (error) {
    logger.error('Error marking job as bounced:', error);
  }
};

/**
 * Start email worker
 */
const startEmailWorker = async () => {
  try {
    const queue = getQueue();

    if (!queue) {
      throw new Error('Email queue not initialized');
    }

    // Process jobs
    workerProcessor = queue.process('send-email', CONCURRENCY, async (job) => {
      return processEmailJob(job);
    });

    logger.info(`Email worker started with concurrency: ${CONCURRENCY}`);

    return workerProcessor;

  } catch (error) {
    logger.error('Error starting email worker:', error);
    throw error;
  }
};

/**
 * Stop email worker
 */
const stopEmailWorker = async () => {
  if (workerProcessor) {
    await workerProcessor.close();
    workerProcessor = null;
    logger.info('Email worker stopped');
  }
};

module.exports = {
  startEmailWorker,
  stopEmailWorker,
  processEmailJob
};
