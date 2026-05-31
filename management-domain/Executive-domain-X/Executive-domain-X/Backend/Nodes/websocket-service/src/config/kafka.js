const { Kafka } = require('kafkajs');
const config = require('./index');
const logger = require('./logger');

class KafkaClient {
  constructor() {
    this.kafka = null;
    this.producer = null;
    this.consumer = null;
    this.admin = null;
    this.isConnected = false;
    this.isProducerConnected = false;
    this.isConsumerConnected = false;
    this.subscriptions = new Map();
  }

  async connect() {
    if (this.isConnected) {
      return { producer: this.producer, consumer: this.consumer, admin: this.admin };
    }

    try {
      this.kafka = new Kafka({
        clientId: config.kafka.clientId,
        brokers: config.kafka.brokers,
        connectionTimeout: 10000,
        requestTimeout: 30000,
        retry: {
          initialRetryTime: 100,
          retries: 8
        }
      });

      this.producer = this.kafka.producer({
        acks: config.kafka.producer.acks,
        maxRetries: config.kafka.producer.maxRetries
      });

      this.consumer = this.kafka.consumer({
        groupId: config.kafka.consumerGroupId,
        sessionTimeout: config.kafka.consumer.sessionTimeout,
        heartbeatInterval: config.kafka.consumer.heartbeatInterval
      });

      this.admin = this.kafka.admin();

      await Promise.all([
        this.producer.connect(),
        this.admin.connect()
      ]);

      this.isProducerConnected = true;
      this.isConnected = true;

      this.setupEventListeners();

      logger.info('Successfully connected to Kafka');
      return { producer: this.producer, consumer: this.consumer, admin: this.admin };
    } catch (error) {
      logger.error('Kafka connection failed:', error);
      throw error;
    }
  }

  setupEventListeners() {
    if (this.consumer) {
      this.consumer.on('consumer.group_join', () => {
        logger.info('Kafka consumer joined group');
      });

      this.consumer.on('consumer.fetch', () => {
        logger.debug('Kafka consumer fetching messages');
      });
    }
  }

  async connectConsumer() {
    if (!this.consumer) {
      throw new Error('Kafka consumer not initialized');
    }

    if (!this.isConsumerConnected) {
      await this.consumer.connect();
      this.isConsumerConnected = true;
      logger.info('Kafka consumer connected');
    }

    return this.consumer;
  }

  async disconnect() {
    try {
      const disconnectPromises = [];

      if (this.isConsumerConnected && this.consumer) {
        await this.consumer.stop();
        await this.consumer.disconnect();
        this.isConsumerConnected = false;
      }

      if (this.isProducerConnected && this.producer) {
        disconnectPromises.push(this.producer.disconnect());
        this.isProducerConnected = false;
      }

      if (this.admin) {
        disconnectPromises.push(this.admin.disconnect());
      }

      await Promise.all(disconnectPromises);
      this.isConnected = false;

      logger.info('Disconnected from Kafka');
    } catch (error) {
      logger.error('Kafka disconnection error:', error);
    }
  }

  async publish(topic, message, key = null) {
    if (!this.producer || !this.isProducerConnected) {
      throw new Error('Kafka producer not connected');
    }

    try {
      const payload = {
        topic,
        messages: [{
          key: key ? String(key) : undefined,
          value: JSON.stringify(message),
          timestamp: Date.now().toString()
        }]
      };

      const result = await this.producer.send(payload);
      logger.debug(`Message published to topic ${topic}`);
      return result;
    } catch (error) {
      logger.error(`Failed to publish message to topic ${topic}:`, error);
      throw error;
    }
  }

  async publishBatch(topic, messages) {
    if (!this.producer || !this.isProducerConnected) {
      throw new Error('Kafka producer not connected');
    }

    try {
      const payload = {
        topic,
        messages: messages.map(msg => ({
          key: msg.key ? String(msg.key) : undefined,
          value: JSON.stringify(msg.value),
          timestamp: Date.now().toString(),
          headers: msg.headers || {}
        }))
      };

      const result = await this.producer.send(payload);
      logger.debug(`Batch of ${messages.length} messages published to topic ${topic}`);
      return result;
    } catch (error) {
      logger.error(`Failed to publish batch to topic ${topic}:`, error);
      throw error;
    }
  }

  async subscribe(topics, callback, options = {}) {
    await this.connectConsumer();

    const defaultOptions = {
      fromBeginning: false,
      ...options
    };

    await this.consumer.subscribe(
      Array.isArray(topics) ? topics.map(t => ({ topic: t, ...defaultOptions })) : [{ topic: topics, ...defaultOptions }]
    );

    this.consumer.run({
      eachMessage: async ({ topic, partition, message }) => {
        try {
          const value = JSON.parse(message.value.toString());
          const key = message.key?.toString();

          logger.debug(`Message received from topic ${topic}, partition ${partition}`);

          await callback({
            topic,
            partition,
            key,
            value,
            timestamp: message.timestamp,
            headers: message.headers
          });
        } catch (error) {
          logger.error(`Error processing message from topic ${topic}:`, error);
        }
      }
    });

    this.subscriptions.set(Array.isArray(topics) ? topics.join(',') : topics, {
      topics,
      callback,
      options: defaultOptions
    });

    logger.info(`Subscribed to topics: ${Array.isArray(topics) ? topics.join(', ') : topics}`);
  }

  async unsubscribe(topics) {
    if (!this.consumer || !this.isConsumerConnected) {
      return;
    }

    try {
      const topicArray = Array.isArray(topics) ? topics : [topics];
      await this.consumer.unsubscribe({ topics: topicArray });

      for (const topic of topicArray) {
        this.subscriptions.delete(topic);
      }

      logger.info(`Unsubscribed from topics: ${topicArray.join(', ')}`);
    } catch (error) {
      logger.error('Failed to unsubscribe from topics:', error);
    }
  }

  async createTopic(topic, options = {}) {
    if (!this.admin) {
      throw new Error('Kafka admin not connected');
    }

    try {
      await this.admin.createTopics({
        topics: [{
          topic,
          numPartitions: options.numPartitions || 3,
          replicationFactor: options.replicationFactor || 1
        }],
        validateOnly: false
      });

      logger.info(`Created topic: ${topic}`);
    } catch (error) {
      if (error.type === 'TOPIC_ALREADY_EXISTS') {
        logger.debug(`Topic already exists: ${topic}`);
      } else {
        logger.error(`Failed to create topic ${topic}:`, error);
        throw error;
      }
    }
  }

  async listTopics() {
    if (!this.admin) {
      throw new Error('Kafka admin not connected');
    }

    try {
      const topics = await this.admin.listTopics();
      return topics;
    } catch (error) {
      logger.error('Failed to list Kafka topics:', error);
      throw error;
    }
  }

  async getTopicOffsets(topic) {
    if (!this.admin) {
      throw new Error('Kafka admin not connected');
    }

    try {
      const offsets = await this.admin.fetchTopicOffsets(topic);
      return offsets;
    } catch (error) {
      logger.error(`Failed to fetch offsets for topic ${topic}:`, error);
      throw error;
    }
  }

  async commitOffsets(offsets) {
    if (!this.consumer || !this.isConsumerConnected) {
      throw new Error('Kafka consumer not connected');
    }

    try {
      await this.consumer.commitOffsets(offsets);
      logger.debug('Offsets committed successfully');
    } catch (error) {
      logger.error('Failed to commit offsets:', error);
      throw error;
    }
  }

  async seek({ topic, partition, offset }) {
    if (!this.consumer || !this.isConsumerConnected) {
      throw new Error('Kafka consumer not connected');
    }

    try {
      await this.consumer.seek({ topic, partition, offset });
      logger.debug(`Seeked to offset ${offset} for topic ${topic}, partition ${partition}`);
    } catch (error) {
      logger.error('Failed to seek offset:', error);
      throw error;
    }
  }

  getProducer() {
    return this.producer;
  }

  getConsumer() {
    return this.consumer;
  }

  getAdmin() {
    return this.admin;
  }

  isHealthy() {
    return this.isConnected && this.isProducerConnected;
  }

  getTopics() {
    return config.kafka.topics;
  }
}

module.exports = new KafkaClient();
