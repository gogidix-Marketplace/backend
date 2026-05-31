const express = require('express');
const router = express.Router();
const EventStore = require('../models/EventStore');
const EventProcessor = require('../services/EventProcessor');
const { param, query, validationResult } = require('express-validator');

/**
 * @route   GET /api/events/stats
 * @desc    Get event processing statistics
 */
router.get('/stats', async (req, res) => {
  try {
    const tenantId = req.query.tenantId || null;
    const stats = await EventStore.getStats(tenantId);
    const handlerStats = EventProcessor.getHandlerStats();

    res.json({
      success: true,
      data: {
        eventStore: stats,
        handlers: handlerStats
      }
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   GET /api/events/aggregate/:aggregateId
 * @desc    Get all events for an aggregate
 */
router.get('/aggregate/:aggregateId', [
  param('aggregateId').isString().trim().notEmpty(),
  query('tenantId').optional().isString()
], async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ success: false, errors: errors.array() });
  }

  try {
    const { aggregateId } = req.params;
    const tenantId = req.query.tenantId || null;

    const events = await EventStore.getEventsByAggregate(aggregateId, tenantId);

    res.json({
      success: true,
      data: {
        aggregateId,
        count: events.length,
        events
      }
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   GET /api/events/type/:eventType
 * @desc    Get events by type
 */
router.get('/type/:eventType', [
  param('eventType').isString().trim().notEmpty(),
  query('tenantId').optional().isString(),
  query('limit').optional().isInt({ min: 1, max: 1000 })
], async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ success: false, errors: errors.array() });
  }

  try {
    const { eventType } = req.params;
    const tenantId = req.query.tenantId || null;
    const limit = parseInt(req.query.limit) || 100;

    const events = await EventStore.getEventsByType(eventType, tenantId, limit);

    res.json({
      success: true,
      data: {
        eventType,
        count: events.length,
        events
      }
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   GET /api/events/tenant/:tenantId
 * @desc    Get events for a tenant with filters
 */
router.get('/tenant/:tenantId', [
  param('tenantId').isString().trim().notEmpty(),
  query('eventType').optional().isString(),
  query('startDate').optional().isISO8601(),
  query('endDate').optional().isISO8601(),
  query('status').optional().isIn(['PENDING', 'PROCESSING', 'COMPLETED', 'FAILED']),
  query('limit').optional().isInt({ min: 1, max: 1000 }),
  query('skip').optional().isInt({ min: 0 })
], async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ success: false, errors: errors.array() });
  }

  try {
    const { tenantId } = req.params;
    const options = {
      eventType: req.query.eventType,
      startDate: req.query.startDate,
      endDate: req.query.endDate,
      status: req.query.status,
      limit: parseInt(req.query.limit) || 100,
      skip: parseInt(req.query.skip) || 0
    };

    const events = await EventStore.getEventsByTenant(tenantId, options);

    res.json({
      success: true,
      data: {
        tenantId,
        count: events.length,
        events
      }
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   GET /api/events/replay/:aggregateId
 * @desc    Replay events to rebuild aggregate state
 */
router.get('/replay/:aggregateId', [
  param('aggregateId').isString().trim().notEmpty(),
  query('tenantId').optional().isString()
], async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ success: false, errors: errors.array() });
  }

  try {
    const { aggregateId } = req.params;
    const tenantId = req.query.tenantId || null;

    const result = await EventStore.replayAggregate(aggregateId, tenantId);

    res.json({
      success: true,
      data: result
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   GET /api/events/snapshot/:aggregateId
 * @desc    Get latest snapshot for an aggregate
 */
router.get('/snapshot/:aggregateId', [
  param('aggregateId').isString().trim().notEmpty(),
  query('tenantId').optional().isString()
], async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ success: false, errors: errors.array() });
  }

  try {
    const { aggregateId } = req.params;
    const tenantId = req.query.tenantId || null;

    const snapshot = await EventStore.getLatestSnapshot(aggregateId, tenantId);

    res.json({
      success: true,
      data: snapshot
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   GET /api/events/pending
 * @desc    Get pending events for retry
 */
router.get('/pending', [
  query('tenantId').optional().isString(),
  query('maxRetryCount').optional().isInt({ min: 1, max: 10 })
], async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ success: false, errors: errors.array() });
  }

  try {
    const tenantId = req.query.tenantId || null;
    const maxRetryCount = parseInt(req.query.maxRetryCount) || 3;

    const events = await EventStore.getPendingEvents(tenantId, maxRetryCount);

    res.json({
      success: true,
      data: {
        count: events.length,
        events
      }
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   POST /api/events/create
 * @desc    Manually create an event (for testing)
 */
router.post('/create', async (req, res) => {
  try {
    const event = await EventStore.saveEvent(req.body);

    res.status(201).json({
      success: true,
      data: event
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

module.exports = router;
