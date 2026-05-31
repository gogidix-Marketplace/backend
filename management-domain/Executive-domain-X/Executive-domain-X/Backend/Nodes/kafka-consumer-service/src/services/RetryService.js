const logger = require('../config/logger');
const metrics = require('../config/metrics');
const DeadLetterQueue = require('../models/DeadLetterQueue');
const EventProcessor = require('./EventProcessor');
const { sleep, RateLimiter } = require('../utils/helpers');

/**
 * Retry Service - Processes failed events from DLQ
 * Implements exponential backoff and rate limiting
 */
class RetryService {
  constructor() {
    this.isRunning = false;
    this.interval = null;
    this.retryInterval = parseInt(process.env.DLQ_RETRY_INTERVAL) || 60000; // 1 minute
    this.batchSize = parseInt(process.env.DLQ_RETRY_BATCH_SIZE) || 10;
    this.rateLimiter = new RateLimiter({
      rate: parseInt(process.env.DLQ_RETRY_RATE) || 10, // 10 events per second
      interval: 1000
    });
    this.processingStats = {
      totalProcessed: 0,
      successfulRetries: 0,
      failedRetries: 0,
      lastRunTime: null
    };
  }

  /**
   * Start the retry service
   */
  async start() {
    if (this.isRunning) {
      logger.warn('Retry service is already running');
      return;
    }

    logger.info('Starting DLQ retry service...');
    this.isRunning = true;

    // Run initial retry
    await this.runRetryCycle();

    // Set up interval for continuous retry
    this.interval = setInterval(async () => {
      try {
        await this.runRetryCycle();
      } catch (error) {
        logger.error('Error in retry cycle:', error);
        metrics.recordError('retry_cycle', 'retry_service');
      }
    }, this.retryInterval);

    logger.info(`DLQ retry service started (interval: ${this.retryInterval}ms)`);
  }

  /**
   * Stop the retry service
   */
  async stop() {
    if (!this.isRunning) {
      return;
    }

    logger.info('Stopping DLQ retry service...');
    this.isRunning = false;

    if (this.interval) {
      clearInterval(this.interval);
      this.interval = null;
    }

    logger.info('DLQ retry service stopped');
  }

  /**
   * Run a single retry cycle
   */
  async runRetryCycle() {
    const startTime = Date.now();
    this.processingStats.lastRunTime = new Date();

    try {
      // Get events ready for retry
      const events = await DeadLetterQueue.getRetryableEvents(null, this.batchSize);

      if (events.length === 0) {
        logger.debug('No events to retry in this cycle');
        return;
      }

      logger.info(`Processing ${events.length} events from DLQ`);

      // Process events with rate limiting
      const results = {
        successful: 0,
        failed: 0,
        exhausted: 0
      };

      for (const event of events) {
        try {
          // Respect rate limiting
          await this.rateLimiter.consume(1);

          const result = await this.retryEvent(event);

          if (result.success) {
            results.successful++;
            await DeadLetterQueue.markAsResolved(event.dlqEventId);
          } else if (result.exhausted) {
            results.exhausted++;
            await DeadLetterQueue.markAsFailed(event.dlqEventId, result.reason);
          } else {
            results.failed++;
            await DeadLetterQueue.markForRetry(event.dlqEventId);
          }
        } catch (error) {
          logger.error(`Error retrying event ${event.dlqEventId}:`, error);
          results.failed++;
          await DeadLetterQueue.markForRetry(event.dlqEventId);
        }
      }

      // Update stats
      this.processingStats.totalProcessed += events.length;
      this.processingStats.successfulRetries += results.successful;
      this.processingStats.failedRetries += results.failed;

      const duration = Date.now() - startTime;
      logger.info(`Retry cycle completed:`, {
        total: events.length,
        successful: results.successful,
        failed: results.failed,
        exhausted: results.exhausted,
        duration: `${duration}ms`
      });

      // Record metrics
      metrics.updateActiveProcessors(0);

    } catch (error) {
      logger.error('Error in retry cycle:', error);
      metrics.recordError('retry_cycle_error', 'retry_service');
      throw error;
    }
  }

  /**
   * Retry a single event
   */
  async retryEvent(dlqEvent) {
    try {
      // Reconstruct the original event
      const event = {
        eventId: dlqEvent.originalEventId,
        eventType: dlqEvent.eventType,
        tenantId: dlqEvent.tenantId,
        aggregateId: dlqEvent.payload?.aggregateId || dlqEvent.payload?.id,
        aggregateType: dlqEvent.payload?.aggregateType || this.inferAggregateType(dlqEvent.eventType),
        payload: dlqEvent.payload,
        correlationId: dlqEvent.metadata?.correlationId,
        causationId: dlqEvent.dlqEventId, // Track causation through DLQ
        metadata: {
          ...dlqEvent.metadata,
          dlqRetry: true,
          originalError: dlqEvent.error
        }
      };

      // Process through EventProcessor
      const result = await EventProcessor.handleEvent(event);

      if (result.success) {
        logger.info(`Successfully retried event: ${dlqEvent.dlqEventId}`);
        metrics.recordDlqRetry(dlqEvent.eventType, dlqEvent.tenantId);
        return { success: true };
      } else {
        return { success: false, exhausted: false };
      }
    } catch (error) {
      // Check if retry attempts are exhausted
      if (dlqEvent.retryCount >= dlqEvent.maxRetryAttempts - 1) {
        logger.error(`Event ${dlqEvent.dlqEventId} exhausted retry attempts`);
        return {
          success: false,
          exhausted: true,
          reason: `Exhausted ${dlqEvent.maxRetryAttempts} retry attempts. Last error: ${error.message}`
        };
      }

      return { success: false, exhausted: false, error };
    }
  }

  /**
   * Infer aggregate type from event type
   */
  inferAggregateType(eventType) {
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
      'DEPARTMENT_UPDATED': 'DEPARTMENT',
      'DEPARTMENT_DELETED': 'DEPARTMENT'
    };
    return typeMap[eventType] || 'UNKNOWN';
  }

  /**
   * Manually trigger a retry cycle
   */
  async triggerRetry() {
    if (this.isRunning) {
      return await this.runRetryCycle();
    } else {
      logger.warn('Retry service is not running');
      return { triggered: false, message: 'Retry service not running' };
    }
  }

  /**
   * Get retry service statistics
   */
  getStats() {
    return {
      isRunning: this.isRunning,
      retryInterval: this.retryInterval,
      batchSize: this.batchSize,
      ...this.processingStats
    };
  }

  /**
   * Reset statistics
   */
  resetStats() {
    this.processingStats = {
      totalProcessed: 0,
      successfulRetries: 0,
      failedRetries: 0,
      lastRunTime: null
    };
  }

  /**
   * Get health status
   */
  async getHealth() {
    const dlqStats = await DeadLetterQueue.getStats();

    return {
      service: 'retry-service',
      status: this.isRunning ? 'UP' : 'DOWN',
      stats: this.getStats(),
      dlq: {
        total: dlqStats.total,
        pending: dlqStats.statusCounts.PENDING || 0,
        exhausted: dlqStats.statusCounts.EXHAUSTED || 0
      }
    };
  }
}

module.exports = new RetryService();
