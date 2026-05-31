/**
 * Kafka Producer Configuration
 *
 * Handles producing messages to Kafka topics.
 */

const { Kafka } = require('kafkajs');
const logger = require('../config/logger');

let kafkaProducer = null;

/**
 * Kafka configuration
 */
function getKafkaConfig() {
  const brokers = process.env.KAFKA_BROKERS
    ? process.env.KAFKA_BROKERS.split(',')
    : ['localhost:9092'];

  const clientId = process.env.KAFKA_CLIENT_ID || 'executive-realtime-producer';

  return {
    clientId,
    brokers,
    connectionTimeout: 10000,
    requestTimeout: 30000,
    retry: {
      initialRetryTime: 100,
      retries: 8,
    },
  };
}

/**
 * Create and connect Kafka producer
 */
async function connectKafkaProducer() {
  if (kafkaProducer && kafkaProducer.isConnected()) {
    return kafkaProducer;
  }

  try {
    const kafka = new Kafka(getKafkaConfig());
    kafkaProducer = kafka.producer();

    await kafkaProducer.connect();

    kafkaProducer.on('producer.disconnect', () => {
      logger.warn('Kafka producer disconnected');
    });

    logger.info('Kafka producer connected successfully');

    return kafkaProducer;

  } catch (error) {
    logger.error('Failed to connect Kafka producer:', error);
    throw error;
  }
}

/**
 * Get producer instance
 */
function getProducer() {
  return kafkaProducer;
}

/**
 * Disconnect producer
 */
async function disconnectKafkaProducer() {
  if (kafkaProducer) {
    try {
      await kafkaProducer.disconnect();
      logger.info('Kafka producer disconnected');
    } catch (error) {
      logger.error('Error disconnecting Kafka producer:', error);
    }
    kafkaProducer = null;
  }
}

/**
 * Check if producer is connected
 */
function isProducerConnected() {
  return kafkaProducer && kafkaProducer.isConnected();
}

module.exports = {
  kafkaProducer,
  connectKafkaProducer,
  disconnectKafkaProducer,
  getProducer,
  isProducerConnected,
};
