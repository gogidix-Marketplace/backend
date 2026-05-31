/**
 * Configuration Tests
 */

const { connect: connectMongo, getCollection } = require('../config/mongodb');
const { connect: connectRedis, get, set, del, kpiKey } = require('../config/redis');

describe('MongoDB Configuration', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('should throw error when not connected', () => {
    expect(() => getCollection('test')).toThrow('Database not connected');
  });

  test('should handle connection errors gracefully', async () => {
    // Mock failed connection
    const { MongoClient } = require('mongodb');
    MongoClient.mockImplementationOnce(() => ({
      connect: jest.fn().mockRejectedValue(new Error('Connection failed')),
    }));

    await expect(connectMongo()).rejects.toThrow('Connection failed');
  });
});

describe('Redis Configuration', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('should throw error when not connected', () => {
    expect(() => get()).toThrow('Redis not connected');
  });

  test('should generate correct cache keys', () => {
    const { kpiKey } = require('../config/redis');
    expect(kpiKey('tenant-123', 'kpi-456')).toBe('kpi:tenant-123:kpi-456');
  });

  test('should handle cache operations gracefully', async () => {
    // Mock Redis to return null (cache miss)
    const mockRedis = {
      get: jest.fn().mockResolvedValue(null),
      setex: jest.fn().mockResolvedValue('OK'),
      del: jest.fn().mockResolvedValue(1),
    };

    const Redis = require('ioredis');
    Redis.mockImplementationOnce(() => mockRedis);

    await connectRedis();

    // Test get operation
    const value = await get('test-key');
    expect(value).toBeNull();

    // Test set operation
    await set('test-key', { data: 'test' });
    expect(mockRedis.setex).toHaveBeenCalled();

    // Test delete operation
    await del('test-key');
    expect(mockRedis.del).toHaveBeenCalled();
  });
});
