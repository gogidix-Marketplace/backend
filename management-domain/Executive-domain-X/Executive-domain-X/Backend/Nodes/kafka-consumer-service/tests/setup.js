// Jest setup file
const { MongoMemoryServer } = require('mongodb-memory-server');

let mongoServer;

beforeAll(async () => {
  // Start in-memory MongoDB for testing
  mongoServer = await MongoMemoryServer.create();
  const uri = mongoServer.getUri();

  process.env.MONGODB_URI = uri;
  process.env.DB_NAME = 'test_kafka_consumer';
  process.env.NODE_ENV = 'test';
  process.env.REDIS_HOST = 'localhost';
  process.env.REDIS_PORT = '6379';
  process.env.KAFKA_BROKERS = 'localhost:9092';
  process.env.LOG_LEVEL = 'error'; // Reduce noise in tests
});

afterAll(async () => {
  if (mongoServer) {
    await mongoServer.stop();
  }
});

// Mock Redis for testing
jest.mock('ioredis', () => {
  const mockRedis = {
    connect: jest.fn().mockResolvedValue(undefined),
    disconnect: jest.fn().mockResolvedValue(undefined),
    on: jest.fn().mockReturnValueThis,
    set: jest.fn().mockResolvedValue('OK'),
    get: jest.fn().mockResolvedValue(null),
    del: jest.fn().mockResolvedValue(1),
    setex: jest.fn().mockResolvedValue('OK'),
    hset: jest.fn().mockResolvedValue(1),
    hget: jest.fn().mockResolvedValue(null),
    hgetall: jest.fn().mockResolvedValue({}),
    lpush: jest.fn().mockResolvedValue(1),
    lrem: jest.fn().mockResolvedValue(1),
    lrange: jest.fn().mockResolvedValue([]),
    llen: jest.fn().mockResolvedValue(0),
    incr: jest.fn().mockResolvedValue(1),
    publish: jest.fn().mockResolvedValue(1),
    subscribe: jest.fn().mockResolvedValue(undefined),
    unsubscribe: jest.fn().mockResolvedValue(undefined),
    ping: jest.fn().mockResolvedValue('PONG'),
    dbsize: jest.fn().mockResolvedValue(0),
    info: jest.fn().mockResolvedValue('# Stats\n'),
    keys: jest.fn().mockResolvedValue([])
  };

  return {
    Redis: jest.fn(() => mockRedis),
    __mockRedis: mockRedis
  };
});

// Mock Kafka for testing
jest.mock('kafkajs', () => {
  const mockConsumer = {
    connect: jest.fn().mockResolvedValue(undefined),
    disconnect: jest.fn().mockResolvedValue(undefined),
    subscribe: jest.fn().mockResolvedValue(undefined),
    run: jest.fn().mockResolvedValue(undefined),
    stop: jest.fn().mockResolvedValue(undefined),
    pause: jest.fn(),
    resume: jest.fn(),
    seek: jest.fn().mockResolvedValue(undefined),
    commitOffsets: jest.fn().mockResolvedValue(undefined),
    on: jest.fn().mockReturnValueThis
  };

  const mockProducer = {
    connect: jest.fn().mockResolvedValue(undefined),
    disconnect: jest.fn().mockResolvedValue(undefined),
    send: jest.fn().mockResolvedValue(undefined)
  };

  const mockAdmin = {
    connect: jest.fn().mockResolvedValue(undefined),
    disconnect: jest.fn().mockResolvedValue(undefined),
    createTopics: jest.fn().mockResolvedValue(undefined),
    fetchTopicOffsets: jest.fn().mockResolvedValue([]),
    fetchOffsets: jest.fn().mockResolvedValue([]),
    listTopics: jest.fn().mockResolvedValue(['test-topic']),
    listConsumerGroups: jest.fn().mockResolvedValue({ groups: [] }),
    resetOffsets: jest.fn().mockResolvedValue(undefined)
  };

  return {
    Kafka: jest.fn(() => ({
      consumer: jest.fn(() => mockConsumer),
      producer: jest.fn(() => mockProducer),
      admin: jest.fn(() => mockAdmin)
    })),
    __mockConsumer: mockConsumer,
    __mockProducer: mockProducer,
    __mockAdmin: mockAdmin
  };
});

// Set test timeout
jest.setTimeout(30000);
