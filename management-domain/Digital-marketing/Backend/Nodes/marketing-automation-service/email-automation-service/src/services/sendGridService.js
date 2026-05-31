/**
 * SendGrid Service
 * Integration with SendGrid email API
 */

const sgMail = require('@sendgrid/mail');
const logger = require('../utils/logger');
const { calculateBackoff } = require('../utils/helpers');

// Configure SendGrid
const configureSendGrid = () => {
  const apiKey = process.env.SENDGRID_API_KEY;
  if (apiKey) {
    sgMail.setApiKey(apiKey);
  }
};

// Rate limiting
let sendGridRateLimit = parseInt(process.env.SENDGRID_RATE_LIMIT || '100');
let requestCount = 0;
let resetTime = Date.now() + 60000; // Reset every minute

/**
 * Check rate limit
 */
const checkRateLimit = () => {
  const now = Date.now();
  if (now > resetTime) {
    requestCount = 0;
    resetTime = now + 60000;
  }

  if (requestCount >= sendGridRateLimit) {
    throw new Error('SendGrid rate limit exceeded');
  }

  requestCount++;
};

/**
 * Send single email via SendGrid
 */
const sendEmail = async (emailData) => {
  try {
    configureSendGrid();

    // Check rate limit
    checkRateLimit();

    const {
      to,
      cc,
      bcc,
      subject,
      html,
      text,
      from,
      replyTo,
      attachments,
      headers,
      tags,
      metadata
    } = emailData;

    // Format recipients
    const toRecipients = Array.isArray(to) ? to : [to];
    const formattedTo = toRecipients.map(addr => {
      if (typeof addr === 'string') {
        return { email: addr };
      }
      return { email: addr.email, name: addr.name };
    });

    // Build message
    const msg = {
      to: formattedTo,
      from: from || {
        email: process.env.SENDGRID_FROM_EMAIL || 'noreply@gogidix.com',
        name: process.env.SENDGRID_FROM_NAME || 'Gogidix'
      },
      subject
    };

    if (html) msg.html = html;
    if (text) msg.text = text;
    if (replyTo) msg.replyTo = replyTo;
    if (cc) msg.cc = Array.isArray(cc) ? cc : [cc];
    if (bcc) msg.bcc = Array.isArray(bcc) ? bcc : [bcc];

    // Add attachments
    if (attachments && attachments.length > 0) {
      msg.attachments = attachments.map(att => ({
        filename: att.filename,
        content: att.content,
        type: att.contentType,
        disposition: att.disposition || 'attachment',
        contentId: att.contentId
      }));
    }

    // Add custom headers
    if (headers) {
      msg.headers = headers;
    }

    // Add SendGrid custom args (metadata)
    if (metadata) {
      msg.customArgs = metadata;
    }

    // Add tracking categories
    if (tags && tags.length > 0) {
      msg.categories = tags;
    }

    // Enable tracking
    msg.trackingSettings = {
      clickTracking: { enable: true },
      openTracking: { enable: true },
      subscriptionTracking: { enable: false }
    };

    logger.info('Sending email via SendGrid', {
      to: formattedTo.map(t => t.email),
      subject
    });

    const response = await sgMail.send(msg, { timeout: 30000 });

    logger.info('SendGrid email sent successfully', {
      to: formattedTo.map(t => t.email),
      messageId: response[0]?.headers?.['x-message-id']
    });

    return {
      success: true,
      provider: 'sendgrid',
      messageId: response[0]?.headers?.['x-message-id'],
      response: response[0]
    };

  } catch (error) {
    logger.error('SendGrid error:', error);

    // Handle rate limit errors
    if (error.response?.body?.errors?.[0]?.message?.includes('rate limit')) {
      throw new Error('SendGrid rate limit exceeded');
    }

    throw error;
  }
};

/**
 * Send batch emails via SendGrid
 */
const sendBatchEmails = async (emails) => {
  try {
    configureSendGrid();

    const results = [];

    // SendGrid batch API - split into batches of 1000
    const batchSize = 1000;
    const batches = [];

    for (let i = 0; i < emails.length; i += batchSize) {
      batches.push(emails.slice(i, i + batchSize));
    }

    for (const batch of batches) {
      const personalized = batch.map(email => {
        const to = Array.isArray(email.to) ? email.to : [email.to];
        return {
          to: to.map(addr => typeof addr === 'string' ? { email: addr } : addr),
          subject: email.subject,
          html: email.html,
          text: email.text,
          customArgs: email.metadata || {},
          substitutions: email.templateData || {}
        };
      });

      const msg = {
        personalizations: personalized,
        from: {
          email: process.env.SENDGRID_FROM_EMAIL || 'noreply@gogidix.com',
          name: process.env.SENDGRID_FROM_NAME || 'Gogidix'
        },
        batchId: generateBatchId()
      };

      const response = await sgMail.send(msg, { timeout: 60000 });
      results.push({
        batchId: msg.batchId,
        count: batch.length,
        messageId: response[0]?.headers?.['x-message-id']
      });
    }

    return {
      success: true,
      provider: 'sendgrid',
      batches: results,
      totalSent: emails.length
    };

  } catch (error) {
    logger.error('SendGrid batch error:', error);
    throw error;
  }
};

/**
 * Validate email via SendGrid
 */
const validateEmail = async (email) => {
  try {
    configureSendGrid();

    const response = await sgMail.request({
      method: 'POST',
      url: '/v3/validations/email',
      body: { email }
    });

    return response.body;

  } catch (error) {
    logger.error('SendGrid validation error:', error);
    return { valid: false, reason: error.message };
  }
};

/**
 * Get SendGrid statistics
 */
const getStats = async (options = {}) => {
  try {
    // Note: SendGrid stats require the stats API, not mail API
    // This is a placeholder for implementation
    return {
      provider: 'sendgrid',
      stats: []
    };

  } catch (error) {
    logger.error('Error fetching SendGrid stats:', error);
    throw error;
  }
};

/**
 * Generate batch ID
 */
const generateBatchId = () => {
  return `batch_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
};

/**
 * Parse SendGrid webhook
 */
const parseWebhook = (payload, signature, timestamp) => {
  try {
    // Verify webhook signature
    const publicKey = process.env.SENDGRID_WEBHOOK_PUBLIC_KEY;
    if (publicKey) {
      // Implement signature verification
      // This requires the sendgrid-webhooks library or crypto verification
    }

    const events = payload.map(event => ({
      event: event.event,
      email: event.email,
      messageId: event['sg-message-id'],
      timestamp: event.timestamp,
      reason: event.reason,
      status: event.status,
      response: event.response,
      url: event.url,
      userAgent: event.useragent,
      category: event.category,
      customArgs: event['custom_args'] || {},
      sendGridData: event
    }));

    return { events };

  } catch (error) {
    logger.error('Error parsing SendGrid webhook:', error);
    throw error;
  }
};

module.exports = {
  sendEmail,
  sendBatchEmails,
  validateEmail,
  getStats,
  parseWebhook,
  checkRateLimit,
  configureSendGrid
};
