const promClient = require('prom-client');
const logger = require('./logger');

// Create a Registry to register the metrics
const register = new promClient.Registry();

// Add default metrics (CPU, memory, etc.)
promClient.collectDefaultMetrics({
  register,
  prefix: 'kafka_consumer_'
});

// Custom metrics for Kafka Consumer Service

// Event processing metrics
const eventsProcessedTotal = new promClient.Counter({
  name: 'kafka_consumer_events_processed_total',
  help: 'Total number of events processed',
  labelNames: ['event_type', 'tenant_id', 'status'],
  registers: [register]
});

const eventProcessingDuration = new promClient.Histogram({
  name: 'kafka_consumer_event_processing_duration_seconds',
  help: 'Duration of event processing in seconds',
  labelNames: ['event_type', 'tenant_id'],
  buckets: [0.001, 0.005, 0.01, 0.025, 0.05, 0.1, 0.25, 0.5, 1, 2.5, 5, 10],
  registers: [register]
});

const eventsPendingGauge = new promClient.Gauge({
  name: 'kafka_consumer_events_pending',
  help: 'Number of events currently pending processing',
  labelNames: ['tenant_id'],
  registers: [register]
});

// Dead letter queue metrics
const dlqEventsTotal = new promClient.Counter({
  name: 'kafka_consumer_dlq_events_total',
  help: 'Total number of events sent to dead letter queue',
  labelNames: ['event_type', 'tenant_id', 'reason'],
  registers: [register]
});

const dlqRetryAttemptsTotal = new promClient.Counter({
  name: 'kafka_consumer_dlq_retry_attempts_total',
  help: 'Total number of retry attempts from DLQ',
  labelNames: ['event_type', 'tenant_id'],
  registers: [register]
});

// Kafka consumer lag metrics
const consumerLagGauge = new promClient.Gauge({
  name: 'kafka_consumer_lag',
  help: 'Consumer group lag per partition',
  labelNames: ['topic', 'partition', 'group_id'],
  registers: [register]
});

// Connection health metrics
const kafkaConnectionGauge = new promClient.Gauge({
  name: 'kafka_consumer_connected',
  help: 'Kafka connection status (1=connected, 0=disconnected)',
  registers: [register]
});

const mongodbConnectionGauge = new promClient.Gauge({
  name: 'mongodb_connected',
  help: 'MongoDB connection status (1=connected, 0=disconnected)',
  registers: [register]
});

const redisConnectionGauge = new promClient.Gauge({
  name: 'redis_connected',
  help: 'Redis connection status (1=connected, 0=disconnected)',
  registers: [register]
});

// Throughput metrics
const eventsPerSecond = new promClient.Gauge({
  name: 'kafka_consumer_events_per_second',
  help: 'Events processed per second',
  labelNames: ['tenant_id'],
  registers: [register]
});

// Error metrics
const errorsTotal = new promClient.Counter({
  name: 'kafka_consumer_errors_total',
  help: 'Total number of errors',
  labelNames: ['error_type', 'component'],
  registers: [register]
});

// Active processing gauge
const activeProcessorsGauge = new promClient.Gauge({
  name: 'kafka_consumer_active_processors',
  help: 'Number of active event processors',
  registers: [register]
});

// Metrics helper functions
const metrics = {
  register,

  // Event processing metrics
  recordEventProcessed(eventType, tenantId, status) {
    eventsProcessedTotal.inc({ event_type: eventType, tenant_id: tenantId, status });
  },

  recordEventProcessingDuration(eventType, tenantId, durationSeconds) {
    eventProcessingDuration.observe({ event_type: eventType, tenant_id: tenantId }, durationSeconds);
  },

  updatePendingEvents(tenantId, count) {
    eventsPendingGauge.set({ tenant_id: tenantId }, count);
  },

  // Dead letter queue metrics
  recordDlqEvent(eventType, tenantId, reason) {
    dlqEventsTotal.inc({ event_type: eventType, tenant_id: tenantId, reason });
  },

  recordDlqRetry(eventType, tenantId) {
    dlqRetryAttemptsTotal.inc({ event_type: eventType, tenant_id: tenantId });
  },

  // Consumer lag metrics
  updateConsumerLag(topic, partition, groupId, lag) {
    consumerLagGauge.set({ topic, partition: String(partition), group_id: groupId }, lag);
  },

  // Connection status metrics
  updateKafkaConnectionStatus(isConnected) {
    kafkaConnectionGauge.set(isConnected ? 1 : 0);
  },

  updateMongoDBConnectionStatus(isConnected) {
    mongodbConnectionGauge.set(isConnected ? 1 : 0);
  },

  updateRedisConnectionStatus(isConnected) {
    redisConnectionGauge.set(isConnected ? 1 : 0);
  },

  // Throughput metrics
  updateEventsPerSecond(tenantId, count) {
    eventsPerSecond.set({ tenant_id: tenantId }, count);
  },

  // Error metrics
  recordError(errorType, component) {
    errorsTotal.inc({ error_type: errorType, component });
  },

  // Active processors metrics
  updateActiveProcessors(count) {
    activeProcessorsGauge.set(count);
  },

  // Reset metrics
  resetAll() {
    register.resetMetrics();
  },

  // Get metrics as string
  async getMetrics() {
    return await register.metrics();
  },

  // Get metrics as JSON
  async getMetricsAsJson() {
    const metrics = await register.getMetricsAsJSON();
    return metrics;
  }
};

// Export metrics instance
module.exports = metrics;
