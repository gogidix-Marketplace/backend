const broadcastService = require('../../services/BroadcastService');
const config = require('../../config');

describe('BroadcastService', () => {
  let mockIo;

  beforeEach(() => {
    mockIo = {
      to: jest.fn().mockReturnThis(),
      except: jest.fn().mockReturnThis(),
      emit: jest.fn().mockReturnThis(),
      in: jest.fn().mockReturnThis(),
      emit: jest.fn().mockReturnThis(),
      sockets: {
        fetchSockets: jest.fn().mockResolvedValue([])
      }
    };

    broadcastService.io = mockIo;
    broadcastService.messageQueue = [];
    broadcastService.stats = {
      messagesBroadcast: 0,
      messagesToKafka: 0,
      messagesToRedis: 0,
      errors: 0
    };
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  describe('generateMessageId', () => {
    test('should generate unique message IDs', () => {
      const id1 = broadcastService.generateMessageId();
      const id2 = broadcastService.generateMessageId();

      expect(id1).toBeDefined();
      expect(id2).toBeDefined();
      expect(id1).not.toBe(id2);
    });

    test('should include timestamp in message ID', () => {
      const id = broadcastService.generateMessageId();
      expect(id).toMatch(/^msg_\d+_/);
    });
  });

  describe('broadcastToRoom', () => {
    test('should broadcast to room', async () => {
      const result = await broadcastService.broadcastToRoom(
        'test-room',
        'test-event',
        { message: 'test' }
      );

      expect(result.success).toBe(true);
      expect(mockIo.to).toHaveBeenCalledWith('test-room');
      expect(mockIo.emit).toHaveBeenCalled();
    });

    test('should exclude sockets if specified', async () => {
      await broadcastService.broadcastToRoom(
        'test-room',
        'test-event',
        { message: 'test' },
        { exclude: ['socket-123'] }
      );

      expect(mockIo.to).toHaveBeenCalledWith('test-room');
      expect(mockIo.except).toHaveBeenCalledWith(['socket-123']);
    });
  });

  describe('broadcastToUser', () => {
    test('should broadcast to user room', async () => {
      const result = await broadcastService.broadcastToUser(
        'tenant-123',
        'user-456',
        'test-event',
        { message: 'test' }
      );

      expect(result.success).toBe(true);
      expect(mockIo.to).toHaveBeenCalled();
    });
  });

  describe('broadcastToTenant', () => {
    test('should broadcast to tenant', async () => {
      const result = await broadcastService.broadcastToTenant(
        'tenant-123',
        'test-event',
        { message: 'test' }
      );

      expect(result.success).toBe(true);
      expect(mockIo.emit).toHaveBeenCalled();
    });
  });

  describe('broadcastGlobal', () => {
    test('should broadcast globally', async () => {
      const result = await broadcastService.broadcastGlobal(
        'test-event',
        { message: 'test' }
      );

      expect(result.success).toBe(true);
      expect(mockIo.emit).toHaveBeenCalled();
    });
  });

  describe('getStats', () => {
    test('should return current stats', () => {
      broadcastService.stats.messagesBroadcast = 100;
      broadcastService.stats.messagesToKafka = 50;
      broadcastService.stats.messagesToRedis = 75;
      broadcastService.stats.errors = 5;

      const stats = broadcastService.getStats();

      expect(stats.messagesBroadcast).toBe(100);
      expect(stats.messagesToKafka).toBe(50);
      expect(stats.messagesToRedis).toBe(75);
      expect(stats.errors).toBe(5);
      expect(stats.queueSize).toBe(0);
    });
  });

  describe('resetStats', () => {
    test('should reset stats to zero', () => {
      broadcastService.stats.messagesBroadcast = 100;
      broadcastService.resetStats();

      expect(broadcastService.stats.messagesBroadcast).toBe(0);
      expect(broadcastService.stats.messagesToKafka).toBe(0);
      expect(broadcastService.stats.messagesToRedis).toBe(0);
      expect(broadcastService.stats.errors).toBe(0);
    });
  });
});
