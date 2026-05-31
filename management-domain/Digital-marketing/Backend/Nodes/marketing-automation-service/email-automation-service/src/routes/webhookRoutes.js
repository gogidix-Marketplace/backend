/**
 * Webhook Routes
 * Handle webhooks from email providers
 */

const express = require('express');
const router = express.Router();
const crypto = require('crypto');
const { asyncHandler } = require('../middleware/errorHandler');
const { webhookLimiter } = require('../middleware/rateLimiter');
const { parseWebhook: parseSendGridWebhook } = require('../services/sendGridService');
const { parseNotification: parseSESNotification } = require('../services/sesService');
const EmailJob = require('../models/EmailJob');
const BounceRecord = require('../models/BounceRecord');
const ComplaintRecord = require('../models/ComplaintRecord');
const logger = require('../utils/logger');

/**
 * Verify SendGrid webhook signature
 */
const verifySendGridSignature = (payload, signature, timestamp) => {
  const publicKey = process.env.SENDGRID_WEBHOOK_PUBLIC_KEY;
  if (!publicKey) {
    return true; // Skip verification if no key configured
  }

  const encodedPayload = `${timestamp}${payload}`;
  const expectedSignature = crypto
    .createHmac('sha256', publicKey)
    .update(encodedPayload)
    .digest('base64');

  return signature === expectedSignature;
};

/**
 * Verify SES/SNS signature
 */
const verifySNSArn = (topicArn, message) => {
  // In production, verify the SNS signature
  // For now, just check the topic ARN matches our configuration
  const expectedTopic = process.env.SES_SNS_TOPIC_ARN;
  return !expectedTopic || topicArn === expectedTopic;
};

/**
 * POST /webhook/sendgrid
 * Handle SendGrid webhooks
 */
router.post('/sendgrid', webhookLimiter, asyncHandler(async (req, res) => {
  const signature = req.headers['x-twilio-email-event-uid'];
  const timestamp = req.headers['x-twilio-email-event-timestamp'];
  const payload = req.body;

  // Verify signature
  if (signature && timestamp) {
    const payloadString = JSON.stringify(payload);
    if (!verifySendGridSignature(payloadString, signature, timestamp)) {
      logger.warn('Invalid SendGrid webhook signature');
      return res.status(401).json({ error: 'Invalid signature' });
    }
  }

  // Process events
  const events = Array.isArray(payload) ? payload : [payload];

  for (const event of events) {
    await processSendGridEvent(event);
  }

  res.status(200).json({ received: true });
}));

/**
 * Process SendGrid event
 */
const processSendGridEvent = async (event) => {
  try {
    const { email, event: eventType, 'sg-message-id': messageId, reason, url, category } = event;

    // Find email job by message ID or email
    const query = {};
    if (messageId) {
      query.providerMessageId = messageId.replace(/<|>/g, '').split('.')[0];
    }
    if (email && !query.providerMessageId) {
      query['to.email'] = email;
    }

    // Find recent job
    const emailJob = await EmailJob.findOne(query).sort({ createdAt: -1 });

    if (!emailJob) {
      logger.debug('Email job not found for webhook event', { email, eventType });
      return;
    }

    // Update job based on event type
    switch (eventType) {
      case 'delivered':
        await emailJob.markAsDelivered();
        logger.info('Email delivered', { jobId: emailJob.jobId, email });
        break;

      case 'open':
        await emailJob.markAsOpened();
        logger.info('Email opened', { jobId: emailJob.jobId, email });
        break;

      case 'click':
        await emailJob.markAsClicked();
        logger.info('Email clicked', { jobId: emailJob.jobId, email, url });
        break;

      case 'bounce':
        const bounceType = reason && reason.includes('permanent') ? 'hard' : 'soft';
        await emailJob.markAsBounced(bounceType, reason);
        await BounceRecord.recordBounce({
          email,
          tenantId: emailJob.tenantId,
          bounceType,
          reason,
          provider: 'sendgrid',
          providerMessageId: messageId
        });
        logger.warn('Email bounced', { jobId: emailJob.jobId, email, bounceType, reason });
        break;

      case 'spamreport':
      case 'complaint':
        emailJob.complaint = {
          reported: true,
          reason: eventType,
          reportedAt: new Date()
        };
        await emailJob.save();

        await ComplaintRecord.recordComplaint({
          email,
          tenantId: emailJob.tenantId,
          reason: eventType,
          provider: 'sendgrid'
        });
        logger.warn('Email complaint', { jobId: emailJob.jobId, email });
        break;

      case 'dropped':
        emailJob.status = 'failed';
        emailJob.error = {
          message: reason || 'Email dropped by provider',
          code: 'DROPPED'
        };
        await emailJob.save();
        logger.warn('Email dropped', { jobId: emailJob.jobId, email, reason });
        break;

      case 'deferred':
        emailJob.status = 'deferred';
        await emailJob.save();
        logger.info('Email deferred', { jobId: emailJob.jobId, email });
        break;
    }

    // Mark webhook as processed
    emailJob.webhookProcessed = true;
    emailJob.webhookAttempts += 1;
    await emailJob.save();

  } catch (error) {
    logger.error('Error processing SendGrid event:', error);
  }
};

/**
 * POST /webhook/ses
 * Handle AWS SES/SNS webhooks
 */
router.post('/ses', webhookLimiter, asyncHandler(async (req, res) => {
  const payload = req.body;

  // Handle SNS subscription confirmation
  if (payload.Type === 'SubscriptionConfirmation') {
    logger.info('SNS SubscriptionConfirmation received', { TopicArn: payload.TopicArn });
    // In production, you would visit the SubscribeURL to confirm
    return res.status(200).json({ message: 'Subscription confirmation received' });
  }

  if (payload.Type === 'UnsubscribeConfirmation') {
    logger.info('SNS UnsubscribeConfirmation received');
    return res.status(200).json({ message: 'Unsubscribe confirmation received' });
  }

  if (payload.Type !== 'Notification') {
    return res.status(400).json({ error: 'Invalid notification type' });
  }

  // Verify ARN
  const topicArn = payload.TopicArn;
  const message = payload.Message;

  if (!verifySNSArn(topicArn, message)) {
    logger.warn('Invalid SNS topic ARN');
    return res.status(401).json({ error: 'Invalid topic ARN' });
  }

  // Parse SES notification
  const notification = parseSESNotification(message);

  await processSESEvent(notification);

  res.status(200).json({ received: true });
}));

/**
 * Process SES event
 */
const processSESEvent = async (notification) => {
  try {
    const { eventType, messageId, recipients } = notification;

    if (!recipients || recipients.length === 0) {
      logger.debug('SES event without recipients', { eventType });
      return;
    }

    const email = recipients[0];

    // Find email job
    const emailJob = await EmailJob.findOne({
      $or: [
        { providerMessageId: messageId },
        { 'to.email': email }
      ]
    }).sort({ createdAt: -1 });

    if (!emailJob) {
      logger.debug('Email job not found for SES event', { email, eventType });
      return;
    }

    // Update job based on event type
    switch (eventType) {
      case 'Delivery':
        await emailJob.markAsDelivered();
        logger.info('SES email delivered', { jobId: emailJob.jobId, email });
        break;

      case 'Bounce':
        const bounceType = notification.bounce?.bounceType === 'Permanent' ? 'hard' : 'soft';
        const bounceReason = notification.bounce?.bounceSubType || 'Unknown';

        await emailJob.markAsBounced(bounceType, bounceReason);

        await BounceRecord.recordBounce({
          email,
          tenantId: emailJob.tenantId,
          bounceType,
          reason: bounceReason,
          provider: 'ses',
          providerMessageId: messageId
        });
        logger.warn('SES email bounced', { jobId: emailJob.jobId, email, bounceType });
        break;

      case 'Complaint':
        emailJob.complaint = {
          reported: true,
          reason: notification.complaint?.complaintFeedbackType || 'spam',
          reportedAt: new Date(notification.complaint?.timestamp)
        };
        await emailJob.save();

        await ComplaintRecord.recordComplaint({
          email,
          tenantId: emailJob.tenantId,
          reason: notification.complaint?.complaintFeedbackType,
          provider: 'ses'
        });
        logger.warn('SES email complaint', { jobId: emailJob.jobId, email });
        break;

      case 'Reject':
        emailJob.status = 'failed';
        emailJob.error = {
          message: notification.reject?.reason || 'Email rejected',
          code: 'REJECTED'
        };
        await emailJob.save();
        logger.warn('SES email rejected', { jobId: emailJob.jobId, email });
        break;

      case 'Open':
        await emailJob.markAsOpened();
        logger.info('SES email opened', { jobId: emailJob.jobId, email });
        break;

      case 'Click':
        await emailJob.markAsClicked();
        logger.info('SES email clicked', { jobId: emailJob.jobId, email, url: notification.click?.link });
        break;
    }

    emailJob.webhookProcessed = true;
    emailJob.webhookAttempts += 1;
    await emailJob.save();

  } catch (error) {
    logger.error('Error processing SES event:', error);
  }
};

/**
 * POST /webhook/generic
 * Generic webhook endpoint for other providers
 */
router.post('/generic', webhookLimiter, asyncHandler(async (req, res) => {
  const { event, data } = req.body;

  // Generic event processing
  logger.info('Generic webhook received', { event, data });

  res.status(200).json({ received: true });
}));

module.exports = router;
