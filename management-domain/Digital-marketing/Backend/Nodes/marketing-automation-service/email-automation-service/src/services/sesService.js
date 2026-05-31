/**
 * AWS SES Service
 * Integration with Amazon Simple Email Service
 */

const AWS = require('aws-sdk');
const logger = require('../utils/logger');

// Configure SES
let sesClient = null;

/**
 * Initialize SES client
 */
const initSES = () => {
  if (sesClient) {
    return sesClient;
  }

  AWS.config.update({
    region: process.env.AWS_REGION || 'us-east-1',
    accessKeyId: process.env.AWS_ACCESS_KEY_ID,
    secretAccessKey: process.env.AWS_SECRET_ACCESS_KEY
  });

  sesClient = new AWS.SES({
    apiVersion: '2010-12-01',
    maxRetries: 3,
    httpOptions: {
      timeout: 30000,
      connectTimeout: 5000
    }
  });

  return sesClient;
};

/**
 * Get SES rate limits
 */
const getSendQuota = async () => {
  try {
    const ses = initSES();
    const data = await ses.getSendQuota().promise();

    return {
      max24HourSend: data.Max24HourSend,
      maxSendRate: data.MaxSendRate,
      sentLast24Hours: data.SentLast24Hours
    };
  } catch (error) {
    logger.error('Error getting SES quota:', error);
    return null;
  }
};

/**
 * Check if sending is allowed based on quota
 */
const checkQuota = async () => {
  const quota = await getSendQuota();

  if (!quota) {
    return true; // Allow if quota check fails
  }

  // Check if we're close to the daily limit
  const available = quota.max24HourSend - quota.sentLast24Hours;

  if (available <= 0) {
    throw new Error('SES daily quota exceeded');
  }

  return true;
};

/**
 * Send single email via SES
 */
const sendEmail = async (emailData) => {
  try {
    const ses = initSES();

    // Check quota
    await checkQuota();

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
    const toAddresses = Array.isArray(to)
      ? to.map(addr => typeof addr === 'string' ? addr : addr.email)
      : [typeof to === 'string' ? to : to.email];

    // Build email params
    const params = {
      Source: from?.email || process.env.SES_FROM_EMAIL || 'noreply@gogidix.com',
      Destination: {
        ToAddresses: toAddresses
      },
      Message: {
        Subject: {
          Data: subject,
          Charset: 'UTF-8'
        }
      }
    };

    // Add name to source if provided
    if (from?.name) {
      params.Source = `"${from.name}" <${from.email}>`;
    } else if (process.env.SES_FROM_NAME) {
      params.Source = `"${process.env.SES_FROM_NAME}" <${params.Source}>`;
    }

    // Add HTML or text body
    if (html) {
      params.Message.Body = {
        Html: {
          Data: html,
          Charset: 'UTF-8'
        }
      };
    } else if (text) {
      params.Message.Body = {
        Text: {
          Data: text,
          Charset: 'UTF-8'
        }
      };
    }

    // Add both if available
    if (html && text) {
      params.Message.Body = {
        Html: {
          Data: html,
          Charset: 'UTF-8'
        },
        Text: {
          Data: text,
          Charset: 'UTF-8'
        }
      };
    }

    // Add CC
    if (cc && cc.length > 0) {
      const ccAddresses = Array.isArray(cc)
        ? cc.map(addr => typeof addr === 'string' ? addr : addr.email)
        : [cc];
      params.Destination.CcAddresses = ccAddresses;
    }

    // Add BCC
    if (bcc && bcc.length > 0) {
      const bccAddresses = Array.isArray(bcc)
        ? bcc.map(addr => typeof addr === 'string' ? addr : addr.email)
        : [bcc];
      params.Destination.BccAddresses = bccAddresses;
    }

    // Add reply-to
    if (replyTo) {
      params.ReplyToAddresses = Array.isArray(replyTo) ? replyTo : [replyTo];
    }

    // Add headers
    if (headers && Object.keys(headers).length > 0) {
      const headerKeys = Object.keys(headers);
      params.Headers = headerKeys.map(key => ({
        Name: key,
        Value: headers[key]
      }));
    }

    // Add tags
    if (tags && tags.length > 0) {
      params.Tags = tags.slice(0, 10).map(tag => ({
        Name: tag,
        Value: tag
      }));
    }

    // Add custom headers for metadata
    if (metadata && Object.keys(metadata).length > 0) {
      if (!params.Headers) params.Headers = [];
      Object.keys(metadata).forEach(key => {
        if (key.length <= 50 && String(metadata[key]).length <= 500) {
          params.Headers.push({
            Name: `X-Metadata-${key}`,
            Value: String(metadata[key])
          });
        }
      });
    }

    // Enable tracking
    params.ConfigurationSetName = process.env.SES_CONFIGURATION_SET_NAME || 'EmailTracking';

    logger.info('Sending email via SES', {
      to: toAddresses,
      subject
    });

    const response = await ses.sendEmail(params).promise();

    logger.info('SES email sent successfully', {
      to: toAddresses,
      messageId: response.MessageId
    });

    return {
      success: true,
      provider: 'ses',
      messageId: response.MessageId,
      response
    };

  } catch (error) {
    logger.error('SES error:', error);

    // Handle specific SES errors
    if (error.code === 'MessageRejected') {
      throw new Error(`Message rejected: ${error.message}`);
    }

    if (error.code === 'Throttling') {
      throw new Error('SES rate limit exceeded');
    }

    if (error.code === 'DailyLimitExceeded') {
      throw new Error('SES daily quota exceeded');
    }

    throw error;
  }
};

/**
 * Send email with attachments via SES (using RawEmail)
 */
const sendEmailWithAttachments = async (emailData) => {
  try {
    const ses = initSES();
    const { buildRawEmail } = require('./emailBuilder');

    const rawMessage = buildRawEmail(emailData);

    const params = {
      RawMessage: {
        Data: rawMessage
      }
    };

    const response = await ses.sendRawEmail(params).promise();

    return {
      success: true,
      provider: 'ses',
      messageId: response.MessageId,
      response
    };

  } catch (error) {
    logger.error('SES raw email error:', error);
    throw error;
  }
};

/**
 * Send batch emails via SES
 */
const sendBatchEmails = async (emails) => {
  try {
    const results = [];

    // SES has a limit of sending to 50 recipients per call
    // We'll send in batches
    const batchSize = 50;

    for (let i = 0; i < emails.length; i += batchSize) {
      const batch = emails.slice(i, i + batchSize);

      const promises = batch.map(email => {
        if (email.attachments && email.attachments.length > 0) {
          return sendEmailWithAttachments(email);
        }
        return sendEmail(email);
      });

      const batchResults = await Promise.allSettled(promises);

      batchResults.forEach((result, index) => {
        results.push({
          index: i + index,
          success: result.status === 'fulfilled',
          data: result.status === 'fulfilled' ? result.value : null,
          error: result.status === 'rejected' ? result.reason.message : null
        });
      });

      // Rate limiting - SES max send rate
      const quota = await getSendQuota();
      if (quota && quota.maxSendRate) {
        const delay = Math.ceil(1000 / quota.maxSendRate) * batch.length;
        await new Promise(resolve => setTimeout(resolve, Math.min(delay, 1000)));
      }
    }

    return {
      success: true,
      provider: 'ses',
      results,
      totalSent: results.filter(r => r.success).length,
      totalFailed: results.filter(r => !r.success).length
    };

  } catch (error) {
    logger.error('SES batch error:', error);
    throw error;
  }
};

/**
 * Verify email identity
 */
const verifyEmailIdentity = async (email) => {
  try {
    const ses = initSES();

    const params = {
      EmailAddress: email
    };

    await ses.verifyEmailIdentity(params).promise();

    return {
      success: true,
      message: 'Verification email sent'
    };

  } catch (error) {
    logger.error('SES verify email error:', error);
    throw error;
  }
};

/**
 * Get verification status
 */
const getVerificationStatus = async (email) => {
  try {
    const ses = initSES();

    // Check if email is verified
    const identities = await ses.listVerifiedEmailAddresses().promise();

    const isVerified = identities.VerifiedEmailAddresses.some(
      verified => verified.toLowerCase() === email.toLowerCase()
    );

    return {
      email,
      isVerified
    };

  } catch (error) {
    logger.error('Error checking verification status:', error);
    throw error;
  }
};

/**
 * Parse SES notification (SNS)
 */
const parseNotification = (message) => {
  try {
    const notification = JSON.parse(message);

    const eventType = notification.notificationType;

    let event = {
      eventType,
      messageId: notification.mail?.messageId,
      timestamp: notification.mail?.timestamp,
      recipients: notification.mail?.destination || []
    };

    switch (eventType) {
      case 'Delivery':
        event.delivery = {
          processingTimeMillis: notification.delivery?.processingTimeMillis,
          recipients: notification.delivery?.recipients,
          reportingMTA: notification.delivery?.reportingMTA
        };
        break;

      case 'Bounce':
        event.bounce = {
          bounceType: notification.bounce?.bounceType,
          bounceSubType: notification.bounce?.bounceSubType,
          bouncedRecipients: notification.bounce?.bouncedRecipients,
          timestamp: notification.bounce?.timestamp,
          feedbackId: notification.bounce?.feedbackId
        };
        break;

      case 'Complaint':
        event.complaint = {
          complainedRecipients: notification.complaint?.complainedRecipients,
          timestamp: notification.complaint?.timestamp,
          feedbackId: notification.complaint?.feedbackId,
          complaintFeedbackType: notification.complaint?.complaintFeedbackType
        };
        break;

      case 'Reject':
        event.reject = {
          reason: notification.reject?.reason
        };
        break;

      case 'Open':
        event.open = {
          ipAddress: notification.open?.ipAddress,
          userAgent: notification.open?.userAgent,
          timestamp: notification.open?.timestamp
        };
        break;

      case 'Click':
        event.click = {
          link: notification.click?.link,
          linkTags: notification.click?.linkTags,
          ipAddress: notification.click?.ipAddress,
          userAgent: notification.click?.userAgent,
          timestamp: notification.click?.timestamp
        };
        break;
    }

    return event;

  } catch (error) {
    logger.error('Error parsing SES notification:', error);
    throw error;
  }
};

/**
 * Get SES statistics
 */
const getStatistics = async () => {
  try {
    const ses = initSES();

    const [quota, stats] = await Promise.all([
      ses.getSendQuota().promise(),
      ses.getSendStatistics().promise()
    ]);

    return {
      quota: {
        max24HourSend: quota.Max24HourSend,
        maxSendRate: quota.MaxSendRate,
        sentLast24Hours: quota.SentLast24Hours
      },
      stats: stats.SendDataPoints.map(point => ({
        timestamp: point.Timestamp,
        deliveryAttempts: point.DeliveryAttempts,
        bounces: point.Bounces,
        complaints: point.Complaints,
        rejections: point.Rejects
      }))
    };

  } catch (error) {
    logger.error('Error getting SES statistics:', error);
    throw error;
  }
};

module.exports = {
  initSES,
  sendEmail,
  sendEmailWithAttachments,
  sendBatchEmails,
  verifyEmailIdentity,
  getVerificationStatus,
  parseNotification,
  getStatistics,
  getSendQuota,
  checkQuota
};
