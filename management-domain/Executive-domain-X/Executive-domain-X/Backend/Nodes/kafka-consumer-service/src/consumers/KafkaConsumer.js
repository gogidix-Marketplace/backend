const logger = require('../config/logger');
const kafka = require('../config/kafka');
const metrics = require('../config/metrics');
const EventStore = require('../models/EventStore');
const DeadLetterQueue = require('../models/DeadLetterQueue');
const EventProcessor = require('../services/EventProcessor');
const { sleep } = require('../utils/helpers');

/**
 * Kafka Consumer with Multi-tenant Event Isolation
 * Implements CQRS/event-driven pattern with retry mechanisms
 */
class KafkaConsumer {
  constructor() {
    this.consumer = null;
    this.producer = null;
    this.isRunning = false;
    this.subscriptions = new Map();
    this.tenantTopics = new Map(); // Tenant-specific topic routing

    // Topic configuration
    this.topics = {
      main: process.env.KAFKA_MAIN_TOPIC || 'executive-domain-events',
      dlq: process.env.KAFKA_DLQ_TOPIC || 'executive-domain-dlq',
      tenantPrefix: process.env.KAFKA_TENANT_PREFIX || 'tenant-'
    };

    // Consumer configuration
    this.config = {
      maxPollIntervalMs: parseInt(process.env.KAFKA_MAX_POLL_INTERVAL) || 300000,
      sessionTimeoutMs: parseInt(process.env.KAFKA_SESSION_TIMEOUT) || 30000,
      heartbeatIntervalMs: parseInt(process.env.KAFKA_HEARTBEAT_INTERVAL) || 3000,
      maxBytesPerPartition: parseInt(process.env.KAFKA_MAX_BYTES) || 1048576, // 1MB
      minBytes: parseInt(process.env.KAFKA_MIN_BYTES) || 1,
      maxWaitTimeMs: parseInt(process.env.KAFKA_MAX_WAIT_TIME) || 5000
    };
  }

  /**
   * Initialize and start the consumer
   */
  async start() {
    try {
      if (this.isRunning) {
        logger.warn('Kafka consumer is already running');
        return;
      }

      logger.info('Starting Kafka consumer...');

      // Get consumer and producer from Kafka client
      const { consumer, producer } = await kafka.connect();
      this.consumer = consumer;
      this.producer = producer;

      // Subscribe to topics
      await this.subscribeToTopics();

      // Update connection metrics
      metrics.updateKafkaConnectionStatus(true);

      // Start consuming messages
      await this.run();

      this.isRunning = true;
      logger.info('Kafka consumer started successfully');
    } catch (error) {
      logger.error('Error starting Kafka consumer:', error);
      metrics.recordError('consumer_start', 'kafka');
      throw error;
    }
  }

  /**
   * Subscribe to Kafka topics with multi-tenant support
   */
  async subscribeToTopics() {
    try {
      // Subscribe to main topic
      await this.consumer.subscribe({
        topic: this.topics.main,
        fromBeginning: process.env.KAFKA_FROM_BEGINNING === 'true'
      });

      logger.info(`Subscribed to main topic: ${this.topics.main}`);

      // Subscribe to tenant-specific topics if multi-tenancy is enabled
      if (process.env.ENABLE_MULTI_TENANT === 'true') {
        const tenants = (process.env.ACTIVE_TENANTS || 'default').split(',');
        for (const tenant of tenants) {
          const tenantTopic = `${this.topics.tenantPrefix}${tenant.trim()}`;
          await this.consumer.subscribe({ topic: tenantTopic, fromBeginning: false });
          this.tenantTopics.set(tenant.trim(), tenantTopic);
        }
        logger.info(`Subscribed to ${this.tenantTopics.size} tenant-specific topics`);
      }

      // Subscribe to DLQ for retry processing
      await this.consumer.subscribe({
        topic: this.topics.dlq,
        fromBeginning: false
      });

      logger.info(`Subscribed to DLQ topic: ${this.topics.dlq}`);
    } catch (error) {
      logger.error('Error subscribing to topics:', error);
      throw error;
    }
  }

  /**
   * Run the consumer loop
   */
  async run() {
    await this.consumer.run({
      eachMessage: async ({ topic, partition, message }) => {
        const startTime = Date.now();

        try {
          // Extract tenant from topic for multi-tenant isolation
          const tenantId = this.extractTenantFromTopic(topic);

          // Parse message
          const event = this.parseMessage(message, topic, partition, tenantId);

          logger.events('Received event', {
            eventId: event.eventId,
            eventType: event.eventType,
            tenantId,
            topic,
            partition
          });

          // Process event
          await this.processEvent(event);

          // Record successful processing metrics
          const duration = (Date.now() - startTime) / 1000;
          metrics.recordEventProcessed(event.eventType, tenantId, 'success');
          metrics.recordEventProcessingDuration(event.eventType, tenantId, duration);

        } catch (error) {
          logger.error(`Error processing message from topic ${topic}:`, error);
          await this.handleProcessingError(topic, partition, message, error);
        }
      },
      eachBatch: async ({ batch, resolveOffset, heartbeat, isRunning, isStale }) => {
        // Batch processing for better throughput
        if (!isRunning() || isStale()) {
          logger.warn('Consumer is not running or is stale');
          return;
        }

        const tenantId = this.extractTenantFromTopic(batch.topic);

        for (const message of batch.messages) {
          if (!isRunning() || isStale()) {
            break;
          }

          try {
            const event = this.parseMessage(message, batch.topic, batch.partition, tenantId);
            await this.processEvent(event);

            metrics.recordEventProcessed(
              event.eventType,
              tenantId,
              'success'
            );

            resolveOffset(message.offset);
          } catch (error) {
            logger.error('Error in batch processing:', error);
            // Continue processing other messages in batch
          }

          // Send heartbeat to avoid session timeout
          await heartbeat();
        }
      },
      autoCommit: true,
      autoCommitInterval: parseInt(process.env.KAFKA_AUTO_COMMIT_INTERVAL) || 5000,
      autoCommitThreshold: parseInt(process.env.KAFKA_AUTO_COMMIT_THRESHOLD) || 100
    });
  }

  /**
   * Extract tenant ID from topic name
   */
  extractTenantFromTopic(topic) {
    if (topic.startsWith(this.topics.tenantPrefix)) {
      const tenant = topic.substring(this.topics.tenantPrefix.length);
      return tenant || 'default';
    }
    return 'default';
  }

  /**
   * Parse Kafka message into event object
   */
  parseMessage(message, topic, partition, tenantId) {
    try {
      const value = message.value ? message.value.toString() : '{}';
      const key = message.key ? message.key.toString() : null;

      let payload;
      try {
        payload = JSON.parse(value);
      } catch (parseError) {
        // Handle non-JSON messages
        payload = { raw: value };
      }

      return {
        eventId: payload.eventId || payload.id || `${topic}-${partition}-${message.offset}`,
        eventType: key || payload.eventType || 'UNKNOWN',
        eventVersion: payload.eventVersion || '1.0',
        aggregateId: payload.aggregateId || payload.id,
        aggregateType: payload.aggregateType || this.inferAggregateType(key),
        tenantId: payload.tenantId || tenantId,
        correlationId: payload.correlationId || null,
        causationId: payload.causationId || null,
        payload: payload.data || payload.payload || payload,
        metadata: {
          topic,
          partition,
          offset: message.offset,
          timestamp: message.timestamp,
          headers: this.parseHeaders(message.headers),
          key
        }
      };
    } catch (error) {
      logger.error('Error parsing message:', error);
      throw new Error(`Failed to parse Kafka message: ${error.message}`);
    }
  }

  /**
   * Parse message headers
   */
  parseHeaders(headers) {
    const parsed = {};
    if (headers && Array.isArray(headers)) {
      for (const header of headers) {
        parsed[header.key] = header.value ? header.value.toString() : null;
      }
    }
    return parsed;
  }

  /**
   * Infer aggregate type from event type
   */
  inferAggregateType(eventType) {
    if (!eventType) return 'UNKNOWN';

    const typeMap = {
      'EXECUTIVE_CREATED': 'EXECUTIVE',
      'EXECUTIVE_UPDATED': 'EXECUTIVE',
      'EXECUTIVE_DELETED': 'EXECUTIVE',
      'KPI_CREATED': 'KPI',
      'KPI_UPDATED': 'KPI',
      'KPI_DELETED': 'KPI',
      'STRATEGY_CREATED': 'STRATEGY',
      'STRATEGY_UPDATED': 'STRATEGY',
      'STRATEGY_DELETED': 'STRATEGY',
      'APPROVAL_CREATED': 'APPROVAL',
      'APPROVAL_UPDATED': 'APPROVAL',
      'APPROVAL_APPROVED': 'APPROVAL',
      'APPROVAL_REJECTED': 'APPROVAL',
      'DECISION_MADE': 'DECISION',
      'DEPARTMENT_CREATED': 'DEPARTMENT',
      'DEPARTMENT_UPDATED': 'DEPARTMENT'
    };

    return typeMap[eventType] || 'UNKNOWN';
  }

  /**
   * Process event through EventProcessor
   */
  async processEvent(event) {
    try {
      // Save to event store
      await EventStore.saveEvent(event);

      // Mark as processing
      await EventStore.markAsProcessing(event.eventId);

      // Process event through handlers
      await EventProcessor.handleEvent(event);

      // Mark as completed
      await EventStore.markAsProcessed(event.eventId);

    } catch (error) {
      // Mark as failed in event store
      await EventStore.markAsFailed(event.eventId, error);

      // Add to DLQ for retry
      await DeadLetterQueue.addEvent(event, error);

      throw error;
    }
  }

  /**
   * Handle processing errors with retry logic
   */
  async handleProcessingError(topic, partition, message, error) {
    try {
      const event = this.parseMessage(message, topic, partition, 'default');

      // Determine if error is retryable
      const isRetryable = this.isRetryableError(error);

      if (isRetryable) {
        // Add to DLQ for retry
        await DeadLetterQueue.addEvent(event, error, {
          reason: 'RETRYABLE_ERROR'
        });

        metrics.recordEventProcessed(event.eventType, event.tenantId, 'retryable_error');
      } else {
        // Add to DLQ as permanent failure
        await DeadLetterQueue.addEvent(event, error, {
          reason: 'PERMANENT_ERROR',
          maxRetryAttempts: 0
        });

        metrics.recordEventProcessed(event.eventType, event.tenantId, 'permanent_error');
      }

      metrics.recordError(error.name, 'kafka_consumer');
    } catch (dlqError) {
      logger.error('Error handling processing error:', dlqError);
      metrics.recordError('dlq_error', 'kafka_consumer');
    }
  }

  /**
   * Determine if error is retryable
   */
  isRetryableError(error) {
    const retryableErrors = [
      'ECONNREFUSED',
      'ETIMEDOUT',
      'ECONNRESET',
      'ENOTFOUND',
      'EAI_AGAIN'
    ];

    const nonRetryableErrors = [
      'ValidationError',
      'SchemaError',
      'AuthenticationError'
    ];

    if (nonRetryableErrors.some(e => error.name.includes(e))) {
      return false;
    }

    return retryableErrors.includes(error.code) ||
           retryableErrors.some(e => error.message.includes(e));
  }

  /**
   * Stop the consumer gracefully
   */
  async stop() {
    try {
      if (!this.isRunning) {
        logger.warn('Kafka consumer is not running');
        return;
      }

      logger.info('Stopping Kafka consumer...');

      // Stop consuming
      if (this.consumer) {
        await this.consumer.stop();
      }

      // Update metrics
      metrics.updateKafkaConnectionStatus(false);

      this.isRunning = false;
      logger.info('Kafka consumer stopped');
    } catch (error) {
      logger.error('Error stopping Kafka consumer:', error);
      throw error;
    }
  }

  /**
   * Get consumer lag for monitoring
   */
  async getConsumerLag() {
    try {
      const admin = kafka.getAdmin();
      const groupId = kafka.consumerGroupId;

      const offsets = await admin.fetchOffsets({
        groupId,
        topics: [this.topics.main, ...Array.from(this.tenantTopics.values())]
      });

      const lagInfo = [];

      for (const topicOffset of offsets) {
        for (const partitionOffset of topicOffset.partitions) {
          lagInfo.push({
            topic: topicOffset.topic,
            partition: partitionOffset.partition,
            groupId,
            offset: partitionOffset.offset,
            lag: partitionOffset.lag
          });

          // Update metrics
          if (partitionOffset.lag !== undefined) {
            metrics.updateConsumerLag(
              topicOffset.topic,
              partitionOffset.partition,
              groupId,
              partitionOffset.lag
            );
          }
        }
      }

      return lagInfo;
    } catch (error) {
      logger.error('Error getting consumer lag:', error);
      return [];
    }
  }

  /**
   * Pause consumption for a specific topic partition
   */
  async pause(topic, partitions) {
    try {
      if (this.consumer) {
        this.consumer.pause([{ topic, partitions }]);
        logger.info(`Paused consumption for ${topic}, partitions: ${partitions.join(', ')}`);
      }
    } catch (error) {
      logger.error('Error pausing consumer:', error);
      throw error;
    }
  }

  /**
   * Resume consumption for a specific topic partition
   */
  async resume(topic, partitions) {
    try {
      if (this.consumer) {
        this.consumer.resume([{ topic, partitions }]);
        logger.info(`Resumed consumption for ${topic}, partitions: ${partitions.join(', ')}`);
      }
    } catch (error) {
      logger.error('Error resuming consumer:', error);
      throw error;
    }
  }

  /**
   * Seek to specific offset
   */
  async seek(topic, partition, offset) {
    try {
      if (this.consumer) {
        await this.consumer.seek({ topic, partition, offset });
        logger.info(`Seeked to offset ${offset} for ${topic}/${partition}`);
      }
    } catch (error) {
      logger.error('Error seeking offset:', error);
      throw error;
    }
  }

  /**
   * Commit specific offsets
   */
  async commitOffsets(topicPartitions) {
    try {
      if (this.consumer) {
        await this.consumer.commitOffsets(topicPartitions);
        logger.info('Committed offsets:', topicPartitions);
      }
    } catch (error) {
      logger.error('Error committing offsets:', error);
      throw error;
    }
  }
}

module.exports = new KafkaConsumer();
