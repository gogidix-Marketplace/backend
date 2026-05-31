/**
 * Kafka Module Entry Point
 *
 * Exports Kafka producer and consumer functionality.
 */

const { connectKafkaProducer, disconnectKafkaProducer, getProducer } = require('./producer');
const { startConsumer, stopConsumer, getConsumer } = require('./consumer');

/**
 * Initialize Kafka connections
 */
async function initializeKafka() {
  try {
    // Connect producer
    await connectKafkaProducer();

    // Start consumer
    await startConsumer();

    return { producer: getProducer(), consumer: getConsumer() };
  } catch (error) {
    console.error('Failed to initialize Kafka:', error);
    throw error;
  }
}

/**
 * Shutdown Kafka connections
 */
async function shutdownKafka() {
  try {
    await stopConsumer();
    await disconnectKafkaProducer();
  } catch (error) {
    console.error('Error shutting down Kafka:', error);
  }
}

module.exports = {
  // Producer
  connectKafkaProducer,
  disconnectKafkaProducer,
  getProducer,

  // Consumer
  startConsumer,
  stopConsumer,
  getConsumer,

  // Module
  initializeKafka,
  shutdownKafka,
};
