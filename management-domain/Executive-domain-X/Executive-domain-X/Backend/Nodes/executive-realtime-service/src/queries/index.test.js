/**
 * Query Handler Tests
 */

const {
  executeQuery,
  registerQuery,
  invalidateCache,
  QueryTypes,
  CacheTTL,
} = require('./index');

// Mock Redis client
jest.mock('../config/redis', () => ({
  getRedisClient: () => ({
    get: jest.fn(),
    setex: jest.fn(),
    keys: jest.fn(),
    del: jest.fn(),
  }),
}));

describe('Query Handler', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('executeQuery', () => {
    it('should execute registered query handler', async () => {
      const mockHandler = jest.fn().mockResolvedValue({ result: 'data' });
      registerQuery('test_query', mockHandler);

      const result = await executeQuery('test_query', { id: 123 });

      expect(result).toEqual({ result: 'data' });
      expect(mockHandler).toHaveBeenCalledWith({ id: 123 });
    });

    it('should throw error for unregistered query', async () => {
      await expect(executeQuery('unknown_query', {})).rejects.toThrow(
        'No handler registered for query type: unknown_query'
      );
    });

    it('should use cache when available', async () => {
      const { getRedisClient } = require('../config/redis');
      const redis = getRedisClient();
      redis.get.mockResolvedValue(JSON.stringify({ cached: true }));

      const mockHandler = jest.fn().mockResolvedValue({ result: 'data' });
      registerQuery('cached_query', mockHandler);

      const result = await executeQuery('cached_query', { id: 123 });

      expect(result).toEqual({ cached: true });
      expect(mockHandler).not.toHaveBeenCalled();
    });

    it('should cache result when enabled', async () => {
      const { getRedisClient } = require('../config/redis');
      const redis = getRedisClient();
      redis.get.mockResolvedValue(null);
      redis.setex.mockResolvedValue('OK');

      const mockHandler = jest.fn().mockResolvedValue({ result: 'data' });
      registerQuery('cacheable_query', mockHandler);

      const result = await executeQuery('cacheable_query', { id: 123 });

      expect(result).toEqual({ result: 'data' });
      expect(redis.setex).toHaveBeenCalled();
    });

    it('should handle handler errors', async () => {
      const mockHandler = jest.fn().mockRejectedValue(new Error('Query failed'));
      registerQuery('failing_query', mockHandler);

      await expect(executeQuery('failing_query', {})).rejects.toThrow('Query failed');
    });
  });

  describe('invalidateCache', () => {
    it('should invalidate cache entries', async () => {
      const { getRedisClient } = require('../config/redis');
      const redis = getRedisClient();
      redis.keys.mockResolvedValue(['query:test_query:123', 'query:test_query:456']);
      redis.del.mockResolvedValue(2);

      await invalidateCache('test_query', { id: 123 });

      expect(redis.keys).toHaveBeenCalled();
      expect(redis.del).toHaveBeenCalled();
    });

    it('should handle Redis errors gracefully', async () => {
      const { getRedisClient } = require('../config/redis');
      const redis = getRedisClient();
      redis.keys.mockRejectedValue(new Error('Redis error'));

      await invalidateCache('test_query');

      // Should not throw
      expect(true).toBe(true);
    });
  });

  describe('QueryTypes', () => {
    it('should have all expected query types', () => {
      expect(QueryTypes).toHaveProperty('GET_CURRENT_KPI');
      expect(QueryTypes).toHaveProperty('GET_KPI_HISTORY');
      expect(QueryTypes).toHaveProperty('GET_KPI_TRENDS');
      expect(QueryTypes).toHaveProperty('LIST_KPIS');
      expect(QueryTypes).toHaveProperty('GET_DASHBOARD_CONFIG');
      expect(QueryTypes).toHaveProperty('GET_DASHBOARD_DATA');
      expect(QueryTypes).toHaveProperty('GET_ACTIVE_CONNECTIONS');
      expect(QueryTypes).toHaveProperty('GET_ACTIVE_ALERTS');
    });
  });

  describe('CacheTTL', () => {
    it('should have defined TTL values', () => {
      expect(CacheTTL).toHaveProperty('SHORT', 30);
      expect(CacheTTL).toHaveProperty('MEDIUM', 300);
      expect(CacheTTL).toHaveProperty('LONG', 3600);
    });
  });
});
