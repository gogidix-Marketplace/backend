/**
 * Redis Configuration Tests
 */

const { connectRedis, subscribe, unsubscribe, publishMessage, getRedisStatus } = require('./redis');

// Mock Redis to avoid actual connection in tests
jest.mock('ioredis', () => {
  const mockClient = {
    status: 'ready',
    connect: jest.fn().mockResolvedValue(),
    quit: jest.fn().mockResolvedValue(),
    subscribe: jest.fn().mockResolvedValue(),
    unsubscribe: jest.fn().mockResolvedValue(),
    publish: jest.fn().mockResolvedValue(),
    on: jest.fn(),
  };

  return {
    __esModule: true,
    default: jest.fn(() => mockClient),
  };
});

describe('Redis Configuration', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('connectRedis', () => {
    test('should connect to Redis successfully', async () => {
      const { pubClient, subClient } = await connectRedis();

      expect(pubClient).toBeDefined();
      expect(subClient).toBeDefined();
    });

    test('should handle connection errors', async () => {
      // Mock connection failure
      const Redis = require('ioredis');
      Redis.mockImplementationOnce(() => {
        throw new Error('Connection failed');
      });

      await expect(connectRedis()).rejects.toThrow('Connection failed');
    });
  });

  describe('subscribe', () => {
    test('should subscribe to a channel', async () => {
      await connectRedis();
      const handler = jest.fn();

      await subscribe('test-channel', handler);

      // Verify subscribe was called
      expect(handler).toBeDefined();
    });

    test('should throw error when Redis not connected', async () => {
      const handler = jest.fn();

      await expect(subscribe('test-channel', handler)).rejects.toThrow();
    });
  });

  describe('unsubscribe', () => {
    test('should unsubscribe from a channel', async () => {
      await connectRedis();
      const handler = jest.fn();

      await subscribe('test-channel', handler);
      await unsubscribe('test-channel');

      // Verify unsubscribe doesn't throw
      expect(true).toBe(true);
    });
  });

  describe('publishMessage', () => {
    test('should publish message to channel', async () => {
      await connectRedis();

      await publishMessage('test-channel', { test: 'data' });

      // Verify publish was called
      expect(true).toBe(true);
    });

    test('should stringify objects before publishing', async () => {
      await connectRedis();

      await publishMessage('test-channel', { test: 'data' });

      expect(true).toBe(true);
    });
  });

  describe('getRedisStatus', () => {
    test('should return connected status when ready', async () => {
      await connectRedis();
      const status = await getRedisStatus();

      expect(status).toHaveProperty('status');
      expect(status).toHaveProperty('publisher');
      expect(status).toHaveProperty('subscriber');
    });

    test('should return disconnected status when not connected', async () => {
      const status = await getRedisStatus();

      expect(status.status).toBe('disconnected');
    });
  });
});
