const express = require('express');
const eventController = require('../controllers/EventController');
const eventProcessor = require('../services/EventProcessor');
const router = express.Router();

// Health check
router.get('/health', (req, res) => {
  res.json({
    status: 'ok',
    timestamp: new Date().toISOString(),
    service: 'kafka-consumer-service'
  });
});

// Event stats endpoint
router.get('/events/stats', async (req, res) => {
  try {
    const stats = await eventProcessor.getProcessingStats();
    res.json(stats);
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

// Register event handler (for testing)
router.post('/events/register', (req, res) => {
  const { eventType, handler } = req.body;

  if (!eventType || !handler) {
    return res.status(400).json({
      success: false,
      message: 'eventType and handler are required'
    });
  }

  try {
    eventController.registerEventHandler(eventType, eval(handler));
    res.json({ success: true, message: 'Event handler registered' });
  } catch (error) {
    res.status(400).json({ success: false, message: error.message });
  }
});

module.exports = router;