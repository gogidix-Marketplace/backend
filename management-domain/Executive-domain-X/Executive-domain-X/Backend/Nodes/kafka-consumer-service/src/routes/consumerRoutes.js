const express = require('express');
const router = express.Router();
const KafkaConsumer = require('../consumers/KafkaConsumer');
const kafka = require('../config/kafka');
const EventProcessor = require('../services/EventProcessor');
const { param, query, validationResult } = require('express-validator');

/**
 * @route   GET /api/consumer/status
 * @desc    Get consumer status
 */
router.get('/status', async (req, res) => {
  try {
    const kafkaConnected = kafka.isHealthy();
    const consumerRunning = KafkaConsumer.isRunning;
    const circuitBreakers = EventProcessor.getCircuitBreakerStates();

    // Get consumer lag if running
    let lag = [];
    if (consumerRunning) {
      try {
        lag = await KafkaConsumer.getConsumerLag();
      } catch (error) {
        // Lag check failed, but consumer may still be running
      }
    }

    res.json({
      success: true,
      data: {
        kafka: {
          connected: kafkaConnected,
          consumerRunning
        },
        lag,
        circuitBreakers
      }
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   POST /api/consumer/start
 * @desc    Start the Kafka consumer
 */
router.post('/start', async (req, res) => {
  try {
    if (KafkaConsumer.isRunning) {
      return res.json({
        success: true,
        message: 'Consumer is already running'
      });
    }

    await KafkaConsumer.start();

    res.json({
      success: true,
      message: 'Kafka consumer started'
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   POST /api/consumer/stop
 * @desc    Stop the Kafka consumer
 */
router.post('/stop', async (req, res) => {
  try {
    await KafkaConsumer.stop();

    res.json({
      success: true,
      message: 'Kafka consumer stopped'
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   POST /api/consumer/pause
 * @desc    Pause consumption for a specific topic partition
 */
router.post('/pause', async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ success: false, errors: errors.array() });
  }

  try {
    const { topic, partitions } = req.body;

    if (!topic || !partitions || !Array.isArray(partitions)) {
      return res.status(400).json({
        success: false,
        message: 'topic and partitions array are required'
      });
    }

    await KafkaConsumer.pause(topic, partitions);

    res.json({
      success: true,
      message: `Paused consumption for ${topic}, partitions: ${partitions.join(', ')}`
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   POST /api/consumer/resume
 * @desc    Resume consumption for a specific topic partition
 */
router.post('/resume', async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ success: false, errors: errors.array() });
  }

  try {
    const { topic, partitions } = req.body;

    if (!topic || !partitions || !Array.isArray(partitions)) {
      return res.status(400).json({
        success: false,
        message: 'topic and partitions array are required'
      });
    }

    await KafkaConsumer.resume(topic, partitions);

    res.json({
      success: true,
      message: `Resumed consumption for ${topic}, partitions: ${partitions.join(', ')}`
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   POST /api/consumer/seek
 * @desc    Seek to a specific offset
 */
router.post('/seek', async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ success: false, errors: errors.array() });
  }

  try {
    const { topic, partition, offset } = req.body;

    if (!topic || partition === undefined || offset === undefined) {
      return res.status(400).json({
        success: false,
        message: 'topic, partition, and offset are required'
      });
    }

    await KafkaConsumer.seek(topic, partition, offset);

    res.json({
      success: true,
      message: `Seeked to offset ${offset} for ${topic}/${partition}`
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   GET /api/consumer/lag
 * @desc    Get consumer lag
 */
router.get('/lag', async (req, res) => {
  try {
    const lag = await KafkaConsumer.getConsumerLag();

    res.json({
      success: true,
      data: lag
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   POST /api/consumer/circuit-breaker/:eventType/reset
 * @desc    Reset circuit breaker for a specific event type
 */
router.post('/circuit-breaker/:eventType/reset', [
  param('eventType').isString().trim().notEmpty()
], async (req, res) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return res.status(400).json({ success: false, errors: errors.array() });
  }

  try {
    const { eventType } = req.params;
    const reset = EventProcessor.resetCircuitBreaker(eventType);

    if (!reset) {
      return res.status(404).json({
        success: false,
        message: `No circuit breaker found for event type: ${eventType}`
      });
    }

    res.json({
      success: true,
      message: `Circuit breaker reset for ${eventType}`
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   GET /api/consumer/circuit-breakers
 * @desc    Get all circuit breaker states
 */
router.get('/circuit-breakers', async (req, res) => {
  try {
    const states = EventProcessor.getCircuitBreakerStates();

    res.json({
      success: true,
      data: states
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   GET /api/consumer/topics
 * @desc    List Kafka topics
 */
router.get('/topics', async (req, res) => {
  try {
    const admin = kafka.getAdmin();
    const topics = await admin.listTopics();

    res.json({
      success: true,
      data: { topics }
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   GET /api/consumer/consumer-groups
 * @desc    List consumer groups
 */
router.get('/consumer-groups', async (req, res) => {
  try {
    const groups = await kafka.listConsumerGroups();

    res.json({
      success: true,
      data: groups
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

module.exports = router;
