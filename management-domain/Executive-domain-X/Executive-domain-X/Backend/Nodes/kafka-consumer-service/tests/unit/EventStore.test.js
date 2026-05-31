const EventStore = require('../../src/models/EventStore');

describe('EventStore Model', () => {
  beforeAll(async () => {
    // MongoDB is started by setup.js
    await require('../../src/config/mongodb').connect();
  });

  afterAll(async () => {
    await require('../../src/config/mongodb').disconnect();
  });

  beforeEach(async () => {
    // Clear event store before each test
    const collection = await EventStore.getCollection();
    await collection.deleteMany({});
  });

  describe('saveEvent', () => {
    it('should save a new event to the event store', async () => {
      const eventData = {
        eventType: 'EXECUTIVE_CREATED',
        aggregateId: 'exec-123',
        aggregateType: 'EXECUTIVE',
        tenantId: 'tenant-1',
        payload: {
          name: 'John Doe',
          position: 'CEO'
        }
      };

      const saved = await EventStore.saveEvent(eventData);

      expect(saved).toBeDefined();
      expect(saved.eventId).toBeDefined();
      expect(saved.eventType).toBe('EXECUTIVE_CREATED');
      expect(saved.aggregateId).toBe('exec-123');
      expect(saved.status).toBe('PENDING');
    });

    it('should increment version for events on same aggregate', async () => {
      const eventData = {
        eventType: 'EXECUTIVE_CREATED',
        aggregateId: 'exec-123',
        aggregateType: 'EXECUTIVE',
        tenantId: 'tenant-1',
        payload: { name: 'John Doe' }
      };

      const first = await EventStore.saveEvent(eventData);
      expect(first.version).toBe(1);

      const second = await EventStore.saveEvent({
        ...eventData,
        eventType: 'EXECUTIVE_UPDATED'
      });
      expect(second.version).toBe(2);
    });

    it('should handle duplicate event IDs', async () => {
      const eventData = {
        eventId: 'duplicate-event',
        eventType: 'EXECUTIVE_CREATED',
        aggregateId: 'exec-123',
        aggregateType: 'EXECUTIVE',
        tenantId: 'tenant-1',
        payload: { name: 'John Doe' }
      };

      const first = await EventStore.saveEvent(eventData);
      const second = await EventStore.saveEvent(eventData);

      expect(first.eventId).toBe(second.eventId);
      expect(first.version).toBe(second.version);
    });
  });

  describe('getEventsByAggregate', () => {
    it('should retrieve all events for an aggregate in order', async () => {
      await EventStore.saveEvent({
        eventType: 'EXECUTIVE_CREATED',
        aggregateId: 'exec-123',
        aggregateType: 'EXECUTIVE',
        tenantId: 'tenant-1',
        payload: { name: 'John' }
      });

      await EventStore.saveEvent({
        eventType: 'EXECUTIVE_UPDATED',
        aggregateId: 'exec-123',
        aggregateType: 'EXECUTIVE',
        tenantId: 'tenant-1',
        payload: { name: 'John Updated' }
      });

      const events = await EventStore.getEventsByAggregate('exec-123', 'tenant-1');

      expect(events).toHaveLength(2);
      expect(events[0].eventType).toBe('EXECUTIVE_CREATED');
      expect(events[1].eventType).toBe('EXECUTIVE_UPDATED');
    });
  });

  describe('getEventsByType', () => {
    it('should retrieve events by type', async () => {
      await EventStore.saveEvent({
        eventType: 'EXECUTIVE_CREATED',
        aggregateId: 'exec-1',
        aggregateType: 'EXECUTIVE',
        tenantId: 'tenant-1',
        payload: { name: 'John' }
      });

      await EventStore.saveEvent({
        eventType: 'EXECUTIVE_CREATED',
        aggregateId: 'exec-2',
        aggregateType: 'EXECUTIVE',
        tenantId: 'tenant-1',
        payload: { name: 'Jane' }
      });

      await EventStore.saveEvent({
        eventType: 'KPI_CREATED',
        aggregateId: 'kpi-1',
        aggregateType: 'KPI',
        tenantId: 'tenant-1',
        payload: { name: 'Revenue' }
      });

      const events = await EventStore.getEventsByType('EXECUTIVE_CREATED', 'tenant-1');

      expect(events).toHaveLength(2);
      expect(events.every(e => e.eventType === 'EXECUTIVE_CREATED')).toBe(true);
    });
  });

  describe('markAsProcessed', () => {
    it('should mark an event as processed', async () => {
      const event = await EventStore.saveEvent({
        eventType: 'EXECUTIVE_CREATED',
        aggregateId: 'exec-123',
        aggregateType: 'EXECUTIVE',
        tenantId: 'tenant-1',
        payload: { name: 'John' }
      });

      const marked = await EventStore.markAsProcessed(event.eventId);

      expect(marked).toBe(true);

      const collection = await EventStore.getCollection();
      const updated = await collection.findOne({ eventId: event.eventId });
      expect(updated.status).toBe('COMPLETED');
      expect(updated.processedAt).toBeDefined();
    });
  });

  describe('markAsFailed', () => {
    it('should mark an event as failed and increment retry count', async () => {
      const event = await EventStore.saveEvent({
        eventType: 'EXECUTIVE_CREATED',
        aggregateId: 'exec-123',
        aggregateType: 'EXECUTIVE',
        tenantId: 'tenant-1',
        payload: { name: 'John' }
      });

      await EventStore.markAsFailed(event.eventId, 'Test error');

      const collection = await EventStore.getCollection();
      const updated = await collection.findOne({ eventId: event.eventId });

      expect(updated.status).toBe('FAILED');
      expect(updated.error).toBe('Test error');
      expect(updated.retryCount).toBe(1);
    });
  });

  describe('getStats', () => {
    it('should return event statistics', async () => {
      await EventStore.saveEvent({
        eventType: 'EXECUTIVE_CREATED',
        aggregateId: 'exec-1',
        aggregateType: 'EXECUTIVE',
        tenantId: 'tenant-1',
        payload: { name: 'John' }
      });

      await EventStore.saveEvent({
        eventType: 'KPI_CREATED',
        aggregateId: 'kpi-1',
        aggregateType: 'KPI',
        tenantId: 'tenant-1',
        payload: { name: 'Revenue' }
      });

      await EventStore.markAsProcessed;

      const stats = await EventStore.getStats('tenant-1');

      expect(stats.total).toBeGreaterThanOrEqual(2);
      expect(stats.statusCounts).toBeDefined();
      expect(stats.typeCounts).toBeDefined();
    });
  });
});
