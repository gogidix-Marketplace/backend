const express = require('express');
const router = express.Router();
const DeadLetterQueue = require('../models/DeadLetterQueue');
const RetryService = require('../services/RetryService');
const { param, query, validationResult } = require('express-validator');

/**
 * @route   GET /api/dlq/stats
 * @desc    Get DLQ statistics
 */
router.get('/stats', async (req, res) => {
  try {
    const tenantId = req.query.tenantId || null;
    const stats = await DeadLetterQueue.getStats(tenantId);

    res.json({
      success: true,
      data: stats
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   GET /api/dlq/events
 * @desc    Get DLQ events with filtering
 */
router.get('/events', [
  query('tenantId').optional().isString(),
  query('eventType').optional().isString(),
  query('status').optional().isIn(['PENDING', 'EXHAUSTED', 'RESOLVED', 'FAILED_PERMANENTLY']),
  query('severity').optional().isIn(['LOW', 'MEDIUM', 'HIGH', 'FATAL']),
  query('startDate').optional().isISO8601(),
  query('endDate').optional().isISO8601(),
  query('limit').optional().isInt({ min: 1, max: 1000 }),
  query('skip').optional().isInt({ min: 0 })
], async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ success: false, errors: errors.array() });
  }

  try {
    const filters = {
      tenantId: req.query.tenantId,
      eventType: req.query.eventType,
      status: req.query.status,
      severity: req.query.severity,
      startDate: req.query.startDate,
      endDate: req.query.endDate,
      limit: parseInt(req.query.limit) || 100,
      skip: parseInt(req.query.skip) || 0
    };

    const events = await DeadLetterQueue.getEvents(filters);

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
 * @route   GET /api/dlq/events/:dlqEventId
 * @desc    Get a specific DLQ event
 */
router.get('/events/:dlqEventId', [
  param('dlqEventId').isString().trim().notEmpty()
], async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ success: false, errors: errors.array() });
  }

  try {
    const { dlqEventId } = req.params;
    const event = await DeadLetterQueue.getEvent(dlqEventId);

    if (!event) {
      return res.status(404).json({
        success: false,
        message: 'DLQ event not found'
      });
    }

    res.json({
      success: true,
      data: event
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   POST /api/dlq/events/:dlqEventId/retry
 * @desc    Retry a specific DLQ event immediately
 */
router.post('/events/:dlqEventId/retry', [
  param('dlqEventId').isString().trim().notEmpty()
], async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ success: false, errors: errors.array() });
  }

  try {
    const { dlqEventId } = req.params;
    const event = await DeadLetterQueue.retryNow(dlqEventId);

    res.json({
      success: true,
      message: 'Event queued for retry',
      data: event
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   POST /api/dlq/events/:dlqEventId/resolve
 * @desc    Mark a DLQ event as resolved
 */
router.post('/events/:dlqEventId/resolve', [
  param('dlqEventId').isString().trim().notEmpty()
], async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ success: false, errors: errors.array() });
  }

  try {
    const { dlqEventId } = req.params;
    await DeadLetterQueue.markAsResolved(dlqEventId);

    res.json({
      success: true,
      message: 'Event marked as resolved'
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   POST /api/dlq/events/:dlqEventId/fail
 * @desc    Mark a DLQ event as permanently failed
 */
router.post('/events/:dlqEventId/fail', [
  param('dlqEventId').isString().trim().notEmpty()
], async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ success: false, errors: errors.array() });
  }

  try {
    const { dlqEventId } = req.params;
    const notes = req.body.notes;
    await DeadLetterQueue.markAsFailed(dlqEventId, notes);

    res.json({
      success: true,
      message: 'Event marked as permanently failed'
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   POST /api/dlq/retry/pending
 * @desc    Trigger retry of all pending events
 */
router.post('/retry/pending', async (req, res) => {
  try {
    const result = await RetryService.triggerRetry();

    res.json({
      success: true,
      message: 'Retry cycle triggered',
      data: result
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   GET /api/dlq/retry/stats
 * @desc    Get retry service statistics
 */
router.get('/retry/stats', async (req, res) => {
  try {
    const stats = RetryService.getStats();

    res.json({
      success: true,
      data: stats
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   POST /api/dlq/retry/start
 * @desc    Start the retry service
 */
router.post('/retry/start', async (req, res) => {
  try {
    await RetryService.start();

    res.json({
      success: true,
      message: 'Retry service started'
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   POST /api/dlq/retry/stop
 * @desc    Stop the retry service
 */
router.post('/retry/stop', async (req, res) => {
  try {
    await RetryService.stop();

    res.json({
      success: true,
      message: 'Retry service stopped'
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   DELETE /api/dlq/purge
 * @desc    Purge old resolved events
 */
router.delete('/purge', [
  query('olderThanDays').optional().isInt({ min: 1, max: 365 })
], async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ success: false, errors: errors.array() });
  }

  try {
    const olderThanDays = parseInt(req.query.olderThanDays) || 30;
    const deletedCount = await DeadLetterQueue.purgeResolved(olderThanDays);

    res.json({
      success: true,
      message: `Purged ${deletedCount} resolved events`,
      data: { deletedCount }
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

module.exports = router;
