const EventProcessor = require('../../src/services/EventProcessor');
const redis = require('../../src/config/redis');

// Mock Redis methods
jest.mock('../../src/config/redis');

describe('EventProcessor', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('registerHandler', () => {
    it('should register a handler for an event type', () => {
      const mockHandler = jest.fn().mockResolvedValue({ success: true });

      EventProcessor.registerHandler('TEST_EVENT', mockHandler);

      const stats = EventProcessor.getHandlerStats();
      expect(stats.TEST_EVENT).toBeDefined();
    });

    it('should throw error if handler is not a function', () => {
      expect(() => {
        EventProcessor.registerHandler('TEST_EVENT', 'not a function');
      }).toThrow('must be a function');
    });
  });

  describe('handleEvent', () => {
    it('should process event with registered handler', async () => {
      const mockHandler = jest.fn().mockResolvedValue({
        success: true,
        message: 'Test successful'
      });

      EventProcessor.registerHandler('TEST_EVENT', mockHandler);

      const event = {
        eventId: 'test-123',
        eventType: 'TEST_EVENT',
        aggregateId: 'agg-123',
        aggregateType: 'TEST',
        tenantId: 'tenant-1',
        payload: { data: 'test' }
      };

      const result = await EventProcessor.handleEvent(event);

      expect(result).toBeDefined();
      expect(mockHandler).toHaveBeenCalledWith(event);
    });

    it('should return error for unregistered event type', async () => {
      const event = {
        eventId: 'test-123',
        eventType: 'UNREGISTERED_EVENT',
        aggregateId: 'agg-123',
        aggregateType: 'TEST',
        tenantId: 'tenant-1',
        payload: { data: 'test' }
      };

      const result = await EventProcessor.handleEvent(event);

      expect(result.success).toBe(false);
      expect(result.message).toContain('No handler registered');
    });
  });

  describe('executive event handlers', () => {
    const mockEvent = {
      eventId: 'exec-123',
      eventType: 'EXECUTIVE_CREATED',
      aggregateId: 'exec-abc',
      aggregateType: 'EXECUTIVE',
      tenantId: 'tenant-1',
      payload: {
        name: 'John Doe',
        position: 'CEO',
        email: 'john@example.com'
      }
    };

    it('should handle EXECUTIVE_CREATED event', async () => {
      redis.set.mockResolvedValue('OK');
      redis.del.mockResolvedValue(1);

      const result = await EventProcessor.handleEvent(mockEvent);

      expect(result.success).toBe(true);
      expect(redis.set).toHaveBeenCalledWith(
        `executive:tenant-1:exec-abc`,
        mockEvent.payload,
        3600
      );
    });

    it('should handle EXECUTIVE_UPDATED event', async () => {
      redis.set.mockResolvedValue('OK');
      redis.del.mockResolvedValue(1);
      redis.publish.mockResolvedValue(1);

      const updatedEvent = {
        ...mockEvent,
        eventType: 'EXECUTIVE_UPDATED',
        payload: { name: 'John Updated' }
      };

      const result = await EventProcessor.handleEvent(updatedEvent);

      expect(result.success).toBe(true);
      expect(redis.publish).toHaveBeenCalled();
    });
  });

  describe('KPI event handlers', () => {
    const mockKpiEvent = {
      eventId: 'kpi-123',
      eventType: 'KPI_UPDATED',
      aggregateId: 'kpi-abc',
      aggregateType: 'KPI',
      tenantId: 'tenant-1',
      payload: {
        name: 'Revenue',
        value: 1000000,
        threshold: 500000
      }
    };

    it('should handle KPI_UPDATED event', async () => {
      redis.set.mockResolvedValue('OK');
      redis.del.mockResolvedValue(1);
      redis.lpush.mockResolvedValue(1);
      redis.publish.mockResolvedValue(1);

      const result = await EventProcessor.handleEvent(mockKpiEvent);

      expect(result.success).toBe(true);
    });

    it('should detect threshold breach', async () => {
      redis.set.mockResolvedValue('OK');
      redis.del.mockResolvedValue(1);
      redis.lpush.mockResolvedValue(1);
      redis.publish.mockResolvedValue(1);

      const breachEvent = {
        ...mockKpiEvent,
        payload: {
          ...mockKpiEvent.payload,
          value: 2000000, // Above threshold
          threshold: 500000
        }
      };

      const result = await EventProcessor.handleEvent(breachEvent);

      expect(result.success).toBe(true);
      expect(redis.lpush).toHaveBeenCalledWith(
        'alerts:tenant-1:kpi',
        expect.objectContaining({
          kpiId: 'kpi-abc'
        })
      );
    });
  });

  describe('circuit breaker', () => {
    it('should get circuit breaker states', () => {
      const states = EventProcessor.getCircuitBreakerStates();

      expect(states).toBeDefined();
      expect(typeof states).toBe('object');
    });

    it('should reset circuit breaker for event type', () => {
      const result = EventProcessor.resetCircuitBreaker('EXECUTIVE_CREATED');

      expect(typeof result).toBe('boolean');
    });
  });

  describe('handler stats', () => {
    it('should track handler statistics', async () => {
      const mockHandler = jest.fn().mockResolvedValue({ success: true });
      EventProcessor.registerHandler('STATS_TEST', mockHandler);

      const event = {
        eventId: 'test-stats',
        eventType: 'STATS_TEST',
        aggregateId: 'agg-1',
        aggregateType: 'TEST',
        tenantId: 'tenant-1',
        payload: {}
      };

      await EventProcessor.handleEvent(event);

      const stats = EventProcessor.getHandlerStats();
      expect(stats.STATS_TEST).toBeDefined();
      expect(stats.STATS_TEST.processed).toBeGreaterThan(0);
    });
  });
});
