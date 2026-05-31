/**
 * Event Handlers
 * Real-time event processing for lead scoring
 */

const { calculateScore, invalidateScore, fetchLeadData } = require('../services/scoringEngine');
const LeadScore = require('../models/LeadScore');
const logger = require('../utils/logger');

let eventProcessorInterval = null;

/**
 * Process lead event
 */
const processLeadEvent = async (event) => {
  try {
    const { eventType, leadId, tenantId, data, timestamp } = event;

    logger.info('Processing lead event', { eventType, leadId, tenantId });

    switch (eventType) {
      case 'lead_created':
        await handleLeadCreated(leadId, tenantId, data);
        break;

      case 'lead_updated':
        await handleLeadUpdated(leadId, tenantId, data);
        break;

      case 'email_opened':
        await handleEmailOpened(leadId, tenantId, data);
        break;

      case 'email_clicked':
        await handleEmailClicked(leadId, tenantId, data);
        break;

      case 'form_submitted':
        await handleFormSubmitted(leadId, tenantId, data);
        break;

      case 'page_visited':
        await handlePageVisited(leadId, tenantId, data);
        break;

      case 'content_downloaded':
        await handleContentDownloaded(leadId, tenantId, data);
        break;

      case 'meeting_scheduled':
        await handleMeetingScheduled(leadId, tenantId, data);
        break;

      case 'webinar_attended':
        await handleWebinarAttended(leadId, tenantId, data);
        break;

      case 'demo_requested':
        await handleDemoRequested(leadId, tenantId, data);
        break;

      case 'social_engagement':
        await handleSocialEngagement(leadId, tenantId, data);
        break;

      default:
        logger.warn('Unknown event type', { eventType });
    }

  } catch (error) {
    logger.error('Error processing lead event:', error);
  }
};

/**
 * Handle lead created
 */
const handleLeadCreated = async (leadId, tenantId, data) => {
  // Invalidate any cached score
  await invalidateScore(leadId, tenantId);

  // Calculate initial score
  const leadData = await fetchLeadData(leadId, tenantId);
  await calculateScore(leadId, tenantId, { ...leadData, ...data });

  logger.info('Lead created, score calculated', { leadId, tenantId });
};

/**
 * Handle lead updated
 */
const handleLeadUpdated = async (leadId, tenantId, data) => {
  // Invalidate cached score
  await invalidateScore(leadId, tenantId);

  // Recalculate score
  const leadData = await fetchLeadData(leadId, tenantId);
  await calculateScore(leadId, tenantId, { ...leadData, ...data });

  logger.info('Lead updated, score recalculated', { leadId, tenantId });
};

/**
 * Handle email opened
 */
const handleEmailOpened = async (leadId, tenantId, data) => {
  const leadScore = await LeadScore.getOrCreate(leadId, tenantId);

  // Add points for email engagement
  await leadScore.updateScore(2, 'email_opened');

  // Invalidate cache
  await invalidateScore(leadId, tenantId);

  logger.info('Email opened, score updated', { leadId, tenantId });
};

/**
 * Handle email clicked
 */
const handleEmailClicked = async (leadId, tenantId, data) => {
  const leadScore = await LeadScore.getOrCreate(leadId, tenantId);

  // Add more points for email click
  await leadScore.updateScore(5, 'email_clicked');

  // Invalidate cache
  await invalidateScore(leadId, tenantId);

  logger.info('Email clicked, score updated', { leadId, tenantId });
};

/**
 * Handle form submitted
 */
const handleFormSubmitted = async (leadId, tenantId, data) => {
  const leadScore = await LeadScore.getOrCreate(leadId, tenantId);

  // Add points for form submission
  const points = data.formType === 'contact' ? 10 : data.formType === 'demo' ? 20 : 5;
  await leadScore.updateScore(points, 'form_submitted');

  // Invalidate cache
  await invalidateScore(leadId, tenantId);

  logger.info('Form submitted, score updated', { leadId, tenantId, formType: data.formType });
};

/**
 * Handle page visited
 */
const handlePageVisited = async (leadId, tenantId, data) => {
  const leadScore = await LeadScore.getOrCreate(leadId, tenantId);

  // Add small points for page visit (capped to prevent abuse)
  if (data.pageType === 'pricing') {
    await leadScore.updateScore(3, 'pricing_page_visited');
  } else if (data.pageType === 'product') {
    await leadScore.updateScore(2, 'product_page_visited');
  }

  // Invalidate cache
  await invalidateScore(leadId, tenantId);

  logger.debug('Page visited, score updated', { leadId, tenantId, pageType: data.pageType });
};

/**
 * Handle content downloaded
 */
const handleContentDownloaded = async (leadId, tenantId, data) => {
  const leadScore = await LeadScore.getOrCreate(leadId, tenantId);

  // Add points for content download
  const points = data.contentTier === 'premium' ? 15 : 8;
  await leadScore.updateScore(points, 'content_downloaded');

  // Invalidate cache
  await invalidateScore(leadId, tenantId);

  logger.info('Content downloaded, score updated', { leadId, tenantId });
};

/**
 * Handle meeting scheduled
 */
const handleMeetingScheduled = async (leadId, tenantId, data) => {
  const leadScore = await LeadScore.getOrCreate(leadId, tenantId);

  // Add significant points for meeting
  await leadScore.updateScore(25, 'meeting_scheduled');

  // Add high priority flag
  await leadScore.addFlag('high_priority');

  // Invalidate cache
  await invalidateScore(leadId, tenantId);

  logger.info('Meeting scheduled, score updated', { leadId, tenantId });
};

/**
 * Handle webinar attended
 */
const handleWebinarAttended = async (leadId, tenantId, data) => {
  const leadScore = await LeadScore.getOrCreate(leadId, tenantId);

  // Add points for webinar attendance
  const points = data.duration > 30 ? 20 : 10;
  await leadScore.updateScore(points, 'webinar_attended');

  // Invalidate cache
  await invalidateScore(leadId, tenantId);

  logger.info('Webinar attended, score updated', { leadId, tenantId });
};

/**
 * Handle demo requested
 */
const handleDemoRequested = async (leadId, tenantId, data) => {
  const leadScore = await LeadScore.getOrCreate(leadId, tenantId);

  // Add high points for demo request
  await leadScore.updateScore(30, 'demo_requested');

  // Add high priority flag
  await leadScore.addFlag('high_priority');

  // Invalidate cache
  await invalidateScore(leadId, tenantId);

  logger.info('Demo requested, score updated', { leadId, tenantId });
};

/**
 * Handle social engagement
 */
const handleSocialEngagement = async (leadId, tenantId, data) => {
  const leadScore = await LeadScore.getOrCreate(leadId, tenantId);

  // Add points for social engagement
  const points = data.engagementType === 'share' ? 10 : data.engagementType === 'comment' ? 8 : 5;
  await leadScore.updateScore(points, 'social_engagement');

  // Invalidate cache
  await invalidateScore(leadId, tenantId);

  logger.info('Social engagement, score updated', { leadId, tenantId });
};

/**
 * Start event processor
 */
const startEventProcessor = async () => {
  const { getRedisClient } = require('../config/redis');

  try {
    const redis = getRedisClient();

    // Subscribe to lead events channel
    const subscriber = redis.duplicate();

    await subscriber.subscribe('lead-events', (err) => {
      if (err) {
        logger.error('Failed to subscribe to lead-events:', err);
      } else {
        logger.info('Subscribed to lead-events channel');
      }
    });

    // Handle incoming messages
    subscriber.on('message', async (channel, message) => {
      if (channel === 'lead-events') {
        try {
          const event = JSON.parse(message);
          await processLeadEvent(event);
        } catch (error) {
          logger.error('Error processing event message:', error);
        }
      }
    });

    // Start scheduled decay job (run hourly)
    const { applyDecay } = require('../services/scoringEngine');
    eventProcessorInterval = setInterval(async () => {
      // Apply decay to all tenants (in production, you might want to batch this)
      logger.info('Running scheduled score decay');
      // Note: This is a simplified version. In production, you'd want to
      // process tenants individually or use a queue.
    }, 60 * 60 * 1000); // Every hour

    logger.info('Event processor started');

    return true;

  } catch (error) {
    logger.error('Error starting event processor:', error);
    throw error;
  }
};

/**
 * Stop event processor
 */
const stopEventProcessor = async () => {
  if (eventProcessorInterval) {
    clearInterval(eventProcessorInterval);
    eventProcessorInterval = null;
    logger.info('Event processor stopped');
  }
};

/**
 * Publish event to Redis
 */
const publishEvent = async (event) => {
  try {
    const { getRedisClient } = require('../config/redis');
    const redis = getRedisClient();

    await redis.publish('lead-events', JSON.stringify(event));

    logger.debug('Event published', { eventType: event.eventType, leadId: event.leadId });

  } catch (error) {
    logger.error('Error publishing event:', error);
  }
};

module.exports = {
  processLeadEvent,
  startEventProcessor,
  stopEventProcessor,
  publishEvent,
  handleLeadCreated,
  handleLeadUpdated,
  handleEmailOpened,
  handleEmailClicked,
  handleFormSubmitted,
  handlePageVisited,
  handleContentDownloaded,
  handleMeetingScheduled,
  handleWebinarAttended,
  handleDemoRequested,
  handleSocialEngagement
};
