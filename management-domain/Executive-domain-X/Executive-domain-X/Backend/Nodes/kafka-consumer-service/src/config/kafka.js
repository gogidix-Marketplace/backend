const { Kafka } = require('kafkajs');
const logger = require('./logger');

class KafkaClient {
  constructor() {
    this.kafka = new Kafka({
      clientId: 'kafka-consumer-service',
      brokers: process.env.KAFKA_BROKERS ? process.env.KAFKA_BROKERS.split(',') : ['localhost:9092'],
      retry: {
        initialRetryTime: 300,
        retries: 10
      },
      ssl: process.env.KAFKA_SSL === 'true' ? {} : false,
      sasl: process.env.KAFKA_SASL_MECHANISM ? {
        mechanism: process.env.KAFKA_SASL_MECHANISM,
        username: process.env.KAFKA_USERNAME,
        password: process.env.KAFKA_PASSWORD
      } : undefined
    });

    this.consumer = this.kafka.consumer({ groupId: 'executive-events-group' });
    this.isConnected = false;
  }

  async connect() {
    try {
      await this.consumer.connect();
      this.isConnected = true;
      logger.info('Connected to Kafka consumer');
      return this.consumer;
    } catch (error) {
      logger.error('Kafka connection error:', error);
      throw error;
    }
  }

  async disconnect() {
    if (this.isConnected) {
      await this.consumer.disconnect();
      this.isConnected = false;
      logger.info('Disconnected from Kafka');
    }
  }

  getConsumer() {
    return this.consumer;
  }

  isHealthy() {
    return this.isConnected;
  }
}

module.exports = new KafkaClient();