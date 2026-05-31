/**
 * Utility Helper Functions
 */

/**
 * Sleep/delay function for async operations
 * @param {number} ms - Milliseconds to sleep
 * @returns {Promise<void>}
 */
const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));

/**
 * Retry function with exponential backoff
 * @param {Function} fn - Function to retry
 * @param {Object} options - Retry options
 * @returns {Promise<any>}
 */
const retry = async (fn, options = {}) => {
  const {
    maxAttempts = 3,
    initialDelay = 1000,
    backoffMultiplier = 2,
    maxDelay = 30000,
    jitter = true
  } = options;

  let lastError;
  let delay = initialDelay;

  for (let attempt = 1; attempt <= maxAttempts; attempt++) {
    try {
      return await fn();
    } catch (error) {
      lastError = error;

      if (attempt === maxAttempts) {
        throw error;
      }

      // Calculate next delay with backoff
      delay = Math.min(delay * backoffMultiplier, maxDelay);

      // Add jitter if enabled
      if (jitter) {
        delay = delay * (0.5 + Math.random());
      }

      // Wait before retrying
      await sleep(delay);
    }
  }

  throw lastError;
};

/**
 * Circuit breaker pattern implementation
 */
class CircuitBreaker {
  constructor(options = {}) {
    this.threshold = options.threshold || 5; // Failures before opening
    this.timeout = options.timeout || 60000; // Time to stay open
    this.resetTimeout = options.resetTimeout || 30000; // Half-open timeout
    this.failures = 0;
    this.state = 'CLOSED'; // CLOSED, OPEN, HALF_OPEN
    this.nextAttempt = 0;
    this.logger = options.logger || console;
  }

  async execute(fn) {
    if (this.state === 'OPEN') {
      if (Date.now() > this.nextAttempt) {
        this.state = 'HALF_OPEN';
        this.logger.info('Circuit breaker entering HALF_OPEN state');
      } else {
        throw new Error('Circuit breaker is OPEN');
      }
    }

    try {
      const result = await fn();
      this.onSuccess();
      return result;
    } catch (error) {
      this.onFailure();
      throw error;
    }
  }

  onSuccess() {
    this.failures = 0;
    if (this.state === 'HALF_OPEN') {
      this.state = 'CLOSED';
      this.logger.info('Circuit breaker reset to CLOSED state');
    }
  }

  onFailure() {
    this.failures++;
    this.logger.warn(`Circuit breaker failure count: ${this.failures}/${this.threshold}`);

    if (this.failures >= this.threshold) {
      this.state = 'OPEN';
      this.nextAttempt = Date.now() + this.timeout;
      this.logger.error('Circuit breaker opened');
    }
  }

  getState() {
    return {
      state: this.state,
      failures: this.failures,
      nextAttempt: new Date(this.nextAttempt)
    };
  }

  reset() {
    this.failures = 0;
    this.state = 'CLOSED';
    this.logger.info('Circuit breaker manually reset');
  }
}

/**
 * Rate limiter using token bucket algorithm
 */
class RateLimiter {
  constructor(options = {}) {
    this.rate = options.rate || 100; // Tokens per interval
    this.interval = options.interval || 1000; // Interval in ms
    this.tokens = this.rate;
    this.lastRefill = Date.now();
    this.logger = options.logger || console;
  }

  async consume(tokens = 1) {
    this.refill();

    if (this.tokens >= tokens) {
      this.tokens -= tokens;
      return true;
    }

    const waitTime = this.getWaitTime(tokens);
    this.logger.debug(`Rate limit reached, waiting ${waitTime}ms`);
    await sleep(waitTime);
    return this.consume(tokens);
  }

  refill() {
    const now = Date.now();
    const elapsed = now - this.lastRefill;

    if (elapsed >= this.interval) {
      const tokensToAdd = Math.floor(elapsed / this.interval) * this.rate;
      this.tokens = Math.min(this.rate, this.tokens + tokensToAdd);
      this.lastRefill = now;
    }
  }

  getWaitTime(tokens) {
    const tokensNeeded = tokens - this.tokens;
    const intervalsNeeded = Math.ceil(tokensNeeded / this.rate);
    return intervalsNeeded * this.interval;
  }

  getAvailableTokens() {
    this.refill();
    return this.tokens;
  }
}

/**
 * Batch processor for accumulating and processing items in batches
 */
class BatchProcessor {
  constructor(options = {}) {
    this.batchSize = options.batchSize || 100;
    this.batchTimeout = options.batchTimeout || 5000;
    this.batch = [];
    this.timeout = null;
    this.handler = options.handler;
    this.logger = options.logger || console;
  }

  async add(item) {
    this.batch.push(item);

    if (this.batch.length >= this.batchSize) {
      await this.flush();
    } else {
      this.scheduleFlush();
    }
  }

  scheduleFlush() {
    if (this.timeout) {
      return;
    }

    this.timeout = setTimeout(() => {
      this.flush().catch(err => this.logger.error('Batch flush error:', err));
    }, this.batchTimeout);
  }

  async flush() {
    if (this.timeout) {
      clearTimeout(this.timeout);
      this.timeout = null;
    }

    if (this.batch.length === 0) {
      return;
    }

    const items = [...this.batch];
    this.batch = [];

    try {
      await this.handler(items);
      this.logger.debug(`Processed batch of ${items.length} items`);
    } catch (error) {
      this.logger.error('Batch processing error:', error);
      // Re-queue failed items
      this.batch.unshift(...items);
      throw error;
    }
  }

  async flushNow() {
    return this.flush();
  }

  getPendingCount() {
    return this.batch.length;
  }
}

/**
 * Partition helper for Kafka message distribution
 */
const partitioner = {
  /**
   * Get partition based on key (consistent hashing)
   */
  getPartition(key, numPartitions) {
    if (!key) return 0;
    const hash = hashString(key.toString());
    return Math.abs(hash) % numPartitions;
  },

  /**
   * Distribute items across partitions
   */
  distribute(items, numPartitions, keyFn) {
    const partitions = Array.from({ length: numPartitions }, () => []);

    for (const item of items) {
      const key = keyFn ? keyFn(item) : item.id;
      const partition = this.getPartition(key, numPartitions);
      partitions[partition].push(item);
    }

    return partitions;
  }
};

/**
 * Simple string hash function
 */
function hashString(str) {
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    const char = str.charCodeAt(i);
    hash = ((hash << 5) - hash) + char;
    hash = hash & hash; // Convert to 32bit integer
  }
  return hash;
}

/**
 * Validator for event schema
 */
const validator = {
  /**
   * Validate required event fields
   */
  validateEvent(event) {
    const errors = [];

    if (!event.eventId) errors.push('eventId is required');
    if (!event.eventType) errors.push('eventType is required');
    if (!event.aggregateId) errors.push('aggregateId is required');
    if (!event.aggregateType) errors.push('aggregateType is required');
    if (!event.tenantId) errors.push('tenantId is required');

    return {
      valid: errors.length === 0,
      errors
    };
  },

  /**
   * Validate tenant ID format
   */
  validateTenantId(tenantId) {
    if (!tenantId || typeof tenantId !== 'string') {
      return { valid: false, error: 'tenantId must be a non-empty string' };
    }
    if (!/^[a-zA-Z0-9-_]+$/.test(tenantId)) {
      return { valid: false, error: 'tenantId contains invalid characters' };
    }
    return { valid: true };
  }
};

/**
 * Metrics accumulator for batch metric updates
 */
class MetricsAccumulator {
  constructor(flushInterval = 60000) {
    this.counters = new Map();
    this.gauges = new Map();
    this.histograms = new Map();
    this.flushInterval = flushInterval;
    this.interval = null;
    this.metricsService = null;
  }

  start(metricsService) {
    this.metricsService = metricsService;
    this.interval = setInterval(() => this.flush(), this.flushInterval);
  }

  stop() {
    if (this.interval) {
      clearInterval(this.interval);
      this.interval = null;
    }
  }

  incrementCounter(name, labels = {}, value = 1) {
    const key = this.makeKey(name, labels);
    const current = this.counters.get(key) || 0;
    this.counters.set(key, current + value);
  }

  setGauge(name, labels = {}, value) {
    const key = this.makeKey(name, labels);
    this.gauges.set(key, value);
  }

  observeHistogram(name, labels = {}, value) {
    const key = this.makeKey(name, labels);
    if (!this.histograms.has(key)) {
      this.histograms.set(key, []);
    }
    this.histograms.get(key).push(value);
  }

  makeKey(name, labels) {
    const labelStr = Object.entries(labels)
      .map(([k, v]) => `${k}=${v}`)
      .join(',');
    return `${name}{${labelStr}}`;
  }

  flush() {
    if (this.metricsService) {
      // Flush counters
      for (const [key, value] of this.counters) {
        const [name, labelsStr] = key.split('{');
        // Parse labels and update metrics
        // Implementation depends on metrics service
      }

      // Flush gauges
      for (const [key, value] of this.gauges) {
        // Update gauges
      }

      // Clear accumulated values
      this.counters.clear();
      this.gauges.clear();
      this.histograms.clear();
    }
  }
}

module.exports = {
  sleep,
  retry,
  CircuitBreaker,
  RateLimiter,
  BatchProcessor,
  partitioner,
  validator,
  MetricsAccumulator
};
