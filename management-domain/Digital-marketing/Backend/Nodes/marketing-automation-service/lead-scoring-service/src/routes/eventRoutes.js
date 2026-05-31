/**
 * Event Routes
 * API routes for lead scoring events
 */

const express = require('express');
const router = express.Router();
const { asyncHandler } = require('../middleware/errorHandler');
const { processLeadEvent, publishEvent } = require('../events/eventHandlers');
const Joi = require('joi');

// Event validation schema
const eventSchema = Joi.object({
  eventType: Joi.string().required(),
  leadId: Joi.string().required(),
  tenantId: Joi.string().required(),
  data: Joi.object().default({}),
  timestamp: Joi.date().default(Date.now)
});

/**
 * POST /api/v1/events
 * Submit lead event for scoring
 */
router.post('/', asyncHandler(async (req, res) => {
  const { error, value } = eventSchema.validate(req.body);
  if (error) {
    return res.status(400).json({
      error: 'ValidationError',
      message: 'Invalid event data',
      errors: error.details.map(d => d.message)
    });
  }

  // Process event
  await processLeadEvent(value);

  res.status(202).json({
    success: true,
    message: 'Event processed'
  });
}));

/**
 * POST /api/v1/events/batch
 * Submit multiple lead events
 */
router.post('/batch', asyncHandler(async (req, res) => {
  const { events } = req.body;

  if (!events || !Array.isArray(events)) {
    return res.status(400).json({
      error: 'ValidationError',
      message: 'Events array is required'
    });
  }

  // Validate each event
  const validationResults = events.map(event => {
    const { error, value } = eventSchema.validate(event);
    return { valid: !error, value, error: error?.details };
  });

  const invalidEvents = validationResults.filter(r => !r.valid);

  if (invalidEvents.length > 0) {
    return res.status(400).json({
      error: 'ValidationError',
      message: 'Some events are invalid',
      errors: invalidEvents.map(r => r.error)
    });
  }

  // Process all valid events
  for (const result of validationResults) {
    await processLeadEvent(result.value);
  }

  res.status(202).json({
    success: true,
    message: `${events.length} events processed`
  });
}));

/**
 * GET /api/v1/events/types
 * Get supported event types
 */
router.get('/types', (req, res) => {
  const eventTypes = [
    { type: 'lead_created', description: 'New lead created' },
    { type: 'lead_updated', description: 'Lead information updated' },
    { type: 'email_opened', description: 'Lead opened an email' },
    { type: 'email_clicked', description: 'Lead clicked an email link' },
    { type: 'form_submitted', description: 'Lead submitted a form' },
    { type: 'page_visited', description: 'Lead visited a page' },
    { type: 'content_downloaded', description: 'Lead downloaded content' },
    { type: 'meeting_scheduled', description: 'Lead scheduled a meeting' },
    { type: 'webinar_attended', description: 'Lead attended a webinar' },
    { type: 'demo_requested', description: 'Lead requested a demo' },
    { type: 'social_engagement', description: 'Lead engaged on social media' }
  ];

  res.json({
    success: true,
    data: eventTypes
  });
});

module.exports = router;
