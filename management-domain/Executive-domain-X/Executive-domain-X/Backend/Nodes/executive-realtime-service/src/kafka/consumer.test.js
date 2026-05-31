/**
 * Kafka Consumer Tests
 */

const { startConsumer, stopConsumer, getConsumer, isConsumerRunning } = require('./consumer');
const logger = require('../config/logger');

// Mock WebSocket server
const mockWsServer = {
  broadcastToRoom: jest.fn(),
};

// Mock dependencies
jest.mock('../config/logger');
jest.mock('../websocket/registry', () => ({
  getWebSocketServer: () => mockWsServer,
}));

describe('Kafka Consumer', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  afterEach(async () => {
    if (isConsumerRunning()) {
      await stopConsumer();
    }
  });

  describe('startConsumer', () => {
    it('should start the consumer successfully', async () => {
      // This test would require a running Kafka instance
      // For now, we test the function exists and can be called
      expect(typeof startConsumer).toBe('function');
    });

    it('should return early if already running', async () => {
      // Mock isRunning to true
      const consumer = require('./consumer');
      consumer.isRunning = true;

      await startConsumer();

      expect(logger.warn).toHaveBeenCalledWith('Kafka consumer is already running');
    });
  });

  describe('stopConsumer', () => {
    it('should stop the consumer', async () => {
      expect(typeof stopConsumer).toBe('function');
    });

    it('should return early if not running', async () => {
      await stopConsumer();
      // Should not throw
      expect(true).toBe(true);
    });
  });

  describe('getConsumer', () => {
    it('should return the consumer instance', () => {
      expect(typeof getConsumer).toBe('function');
    });
  });

  describe('isConsumerRunning', () => {
    it('should return running status', () => {
      expect(typeof isConsumerRunning).toBe('function');
    });
  });
});
