const DeadLetterQueue = require('../../src/models/DeadLetterQueue');

describe('DeadLetterQueue Model', () => {
  beforeAll(async () => {
    await require('../../src/config/mongodb').connect();
  });

  afterAll(async () => {
    await require('../../src/config/mongodb').disconnect();
  });

  beforeEach(async () => {
    const collection = await DeadLetterQueue.getCollection();
    await collection.deleteMany({});
  });

  describe('addEvent', () => {
    it('should add a failed event to DLQ', async () => {
      const originalEvent = {
        eventId: 'event-123',
        eventType: 'EXECUTIVE_CREATED',
        aggregateId: 'exec-abc',
        tenantId: 'tenant-1',
        payload: { name: 'John Doe' }
      };

      const error = new Error('Processing failed');

      const dlqEvent = await DeadLetterQueue.addEvent(originalEvent, error);

      expect(dlqEvent).toBeDefined();
      expect(dlqEvent.dlqEventId).toBeDefined();
      expect(dlqEvent.originalEventId).toBe('event-123');
      expect(dlqEvent.eventType).toBe('EXECUTIVE_CREATED');
      expect(dlqEvent.status).toBe('PENDING');
      expect(dlqEvent.retryCount).toBe(0);
    });

    it('should set severity based on error type', async () => {
      const event = {
        eventId: 'event-456',
        eventType: 'TEST_EVENT',
        aggregateId: 'test-abc',
        tenantId: 'tenant-1',
        payload: {}
      };

      // Validation error should be FATAL
      const validationError = new Error('Schema validation failed');
      validationError.code = 'VALIDATION_ERROR';

      const dlqEvent1 = await DeadLetterQueue.addEvent(
        event,
        validationError,
        { reason: 'SCHEMA_VALIDATION' }
      );

      expect(dlqEvent1.severity).toBe('FATAL');

      // Connection error should be HIGH
      const connError = new Error('Connection refused');
      connError.code = 'ECONNREFUSED';

      const dlqEvent2 = await DeadLetterQueue.addEvent(
        { ...event, eventId: 'event-789' },
        connError,
        { reason: 'DEPENDENCY_UNAVAILABLE' }
      );

      expect(dlqEvent2.severity).toBe('HIGH');
    });
  });

  describe('getRetryableEvents', () => {
    it('should retrieve events ready for retry', async () => {
      const event = {
        eventId: 'event-123',
        eventType: 'EXECUTIVE_CREATED',
        aggregateId: 'exec-abc',
        tenantId: 'tenant-1',
        payload: {}
      };

      const error = new Error('Temporary failure');
      await DeadLetterQueue.addEvent(event, error, { maxRetryAttempts: 5 });

      // Get events that can be retried
      const retryable = await DeadLetterQueue.getRetryableEvents('tenant-1');

      expect(retryable).toBeDefined();
      expect(Array.isArray(retryable)).toBe(true);
    });

    it('should not return FATAL severity events', async () => {
      const event = {
        eventId: 'event-456',
        eventType: 'TEST_EVENT',
        aggregateId: 'test-abc',
        tenantId: 'tenant-1',
        payload: {}
      };

      const error = new Error('Fatal error');
      await DeadLetterQueue.addEvent(event, error, {
        reason: 'SCHEMA_VALIDATION',
        maxRetryAttempts: 5
      });

      const retryable = await DeadLetterQueue.getRetryableEvents('tenant-1');

      expect(retryable.filter(e => e.severity === 'FATAL')).toHaveLength(0);
    });
  });

  describe('markForRetry', () => {
    it('should increment retry count and schedule next retry', async () => {
      const event = {
        eventId: 'event-123',
        eventType: 'TEST_EVENT',
        aggregateId: 'test-abc',
        tenantId: 'tenant-1',
        payload: {}
      };

      const dlqEvent = await DeadLetterQueue.addEvent(event, new Error('Test'));

      const marked = await DeadLetterQueue.markForRetry(dlqEvent.dlqEventId);

      expect(marked).toBe(true);

      const collection = await DeadLetterQueue.getCollection();
      const updated = await collection.findOne({ dlqEventId: dlqEvent.dlqEventId });

      expect(updated.retryCount).toBe(1);
      expect(updated.nextRetryAt).toBeDefined();
      expect(updated.nextRetryAt).toBeInstanceOf(Date);
    });

    it('should mark as EXHAUSTED when max retries reached', async () => {
      const event = {
        eventId: 'event-789',
        eventType: 'TEST_EVENT',
        aggregateId: 'test-abc',
        tenantId: 'tenant-1',
        payload: {}
      };

      const dlqEvent = await DeadLetterQueue.addEvent(event, new Error('Test'), {
        maxRetryAttempts: 1
      });

      await DeadLetterQueue.markForRetry(dlqEvent.dlqEventId);

      const collection = await DeadLetterQueue.getCollection();
      const updated = await collection.findOne({ dlqEventId: dlqEvent.dlqEventId });

      expect(updated.status).toBe('EXHAUSTED');
    });
  });

  describe('markAsResolved', () => {
    it('should mark DLQ event as resolved', async () => {
      const dlqEvent = await DeadLetterQueue.addEvent(
        { eventId: 'event-123', eventType: 'TEST', aggregateId: 'test-1', tenantId: 'tenant-1', payload: {} },
        new Error('Test')
      );

      const marked = await DeadLetterQueue.markAsResolved(dlqEvent.dlqEventId);

      expect(marked).toBe(true);

      const collection = await DeadLetterQueue.getCollection();
      const updated = await collection.findOne({ dlqEventId: dlqEvent.dlqEventId });

      expect(updated.status).toBe('RESOLVED');
      expect(updated.resolvedAt).toBeDefined();
    });
  });

  describe('markAsFailed', () => {
    it('should mark DLQ event as permanently failed', async () => {
      const dlqEvent = await DeadLetterQueue.addEvent(
        { eventId: 'event-123', eventType: 'TEST', aggregateId: 'test-1', tenantId: 'tenant-1', payload: {} },
        new Error('Test')
      );

      await DeadLetterQueue.markAsFailed(dlqEvent.dlqEventId, 'Manual failure');

      const collection = await DeadLetterQueue.getCollection();
      const updated = await collection.findOne({ dlqEventId: dlqEvent.dlqEventId });

      expect(updated.status).toBe('FAILED_PERMANENTLY');
      expect(updated.notes).toBe('Manual failure');
    });
  });

  describe('getStats', () => {
    it('should return DLQ statistics', async () => {
      const event = {
        eventId: 'event-123',
        eventType: 'TEST_EVENT',
        aggregateId: 'test-abc',
        tenantId: 'tenant-1',
        payload: {}
      };

      await DeadLetterQueue.addEvent(event, new Error('Test'));

      const stats = await DeadLetterQueue.getStats('tenant-1');

      expect(stats).toBeDefined();
      expect(stats.total).toBeGreaterThan(0);
      expect(stats.statusCounts).toBeDefined();
      expect(stats.typeCounts).toBeDefined();
      expect(stats.severityCounts).toBeDefined();
    });
  });

  describe('getEvents', () => {
    it('should filter DLQ events by criteria', async () => {
      await DeadLetterQueue.addEvent(
        { eventId: 'e1', eventType: 'TYPE_A', aggregateId: 'a1', tenantId: 'tenant-1', payload: {} },
        new Error('Error 1')
      );

      await DeadLetterQueue.addEvent(
        { eventId: 'e2', eventType: 'TYPE_B', aggregateId: 'a2', tenantId: 'tenant-1', payload: {} },
        new Error('Error 2')
      );

      const events = await DeadLetterQueue.getEvents({
        tenantId: 'tenant-1',
        eventType: 'TYPE_A'
      });

      expect(events).toBeDefined();
      expect(events.every(e => e.eventType === 'TYPE_A')).toBe(true);
    });
  });

  describe('purgeResolved', () => {
    it('should purge old resolved events', async () => {
      const dlqEvent = await DeadLetterQueue.addEvent(
        { eventId: 'event-123', eventType: 'TEST', aggregateId: 'test-1', tenantId: 'tenant-1', payload: {} },
        new Error('Test')
      );

      await DeadLetterQueue.markAsResolved(dlqEvent.dlqEventId);

      // Manually set resolvedAt to past date
      const collection = await DeadLetterQueue.getCollection();
      const pastDate = new Date();
      pastDate.setDate(pastDate.getDate() - 31);
      await collection.updateOne(
        { dlqEventId: dlqEvent.dlqEventId },
        { $set: { resolvedAt: pastDate } }
      );

      const purged = await DeadLetterQueue.purgeResolved(30);

      expect(purged).toBe(1);

      const remaining = await collection.findOne({ dlqEventId: dlqEvent.dlqEventId });
      expect(remaining).toBeNull();
    });
  });
});
