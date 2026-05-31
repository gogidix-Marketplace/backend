const EventController = require('../controllers/EventController');
const EventLog = require('../models/EventLog');

jest.mock('../models/EventLog');

describe('EventController Tests', () => {
  let eventController;

  beforeEach(() => {
    eventController = new EventController();
    jest.clearAllMocks();
  });

  describe('Event Registration', () => {
    test('should register event handler successfully', () => {
      const mockHandler = jest.fn();
      eventController.registerEventHandler('TEST_EVENT', mockHandler);

      expect(eventController.eventHandlers.has('TEST_EVENT')).toBe(true);
      expect(mockHandler).not.toHaveBeenCalled();
    });

    test('should throw error when handler is not a function', () => {
      expect(() => {
        eventController.registerEventHandler('TEST_EVENT', 'not a function');
      }).toThrow('Handler must be a function');
    });
  });

  describe('Event Processing', () => {
    test('should process event successfully', async () => {
      const mockHandler = jest.fn().mockResolvedValue({ success: true });
      eventController.registerEventHandler('TEST_EVENT', mockHandler);

      const mockEvent = {
        key: 'TEST_EVENT',
        value: {
          eventId: 'test-event-id',
          eventType: 'TEST_EVENT',
          aggregateId: 'test-aggregate-id',
          payload: { data: 'test-data' }
        }
      };

      const result = await eventController.handleEvent(mockEvent);

      expect(result).toEqual({ success: true });
      expect(mockHandler).toHaveBeenCalledWith(mockEvent.value);
    });

    test('should handle missing event handler', async () => {
      const mockEvent = {
        key: 'UNREGISTERED_EVENT',
        value: { data: 'test-data' }
      };

      const result = await eventController.handleEvent(mockEvent);

      expect(result).toEqual({ success: false, message: 'No handler registered' });
    });

    test('should handle processing errors', async () => {
      const mockHandler = jest.fn().mockRejectedValue(new Error('Processing failed'));
      eventController.registerEventHandler('TEST_EVENT', mockHandler);

      const mockEvent = {
        key: 'TEST_EVENT',
        value: {
          eventId: 'test-event-id',
          eventType: 'TEST_EVENT',
          aggregateId: 'test-aggregate-id',
          payload: { data: 'test-data' }
        }
      };

      const result = await eventController.handleEvent(mockEvent);

      expect(result.success).toBe(false);
      expect(result.message).toBe('Processing failed');
    });
  });

  describe('Event Stats', () => {
    test('should return event statistics', async () => {
      const mockAggregateResult = [
        { _id: 'COMPLETED', count: 10 },
        { _id: 'FAILED', count: 2 }
      ];
      const mockFindResult = [
        { eventId: '1', timestamp: new Date(), status: 'COMPLETED' },
        { eventId: '2', timestamp: new Date(), status: 'FAILED' }
      ];

      EventLog.aggregate.mockResolvedValue(mockAggregateResult);
      EventLog.countDocuments.mockResolvedValue(12);
      EventLog.find.mockReturnValue({
        sort: jest.fn().mockReturnThis(),
        limit: jest.fn().mockReturnThis(),
        exec: jest.fn().mockResolvedValue(mockFindResult)
      });

      const result = await eventController.getEventStats();

      expect(result.success).toBe(true);
      expect(result.totalEvents).toBe(12);
      expect(result.statusCounts).toEqual({ COMPLETED: 10, FAILED: 2 });
      expect(result.recentEvents).toHaveLength(2);
    });
  });
});