const presenceService = require('../../services/PresenceService');

describe('PresenceService', () => {
  beforeEach(() => {
    presenceService.presenceCache.clear();
    presenceService.typingIndicators.clear();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  describe('getPresenceKey', () => {
    test('should generate presence key', () => {
      const key = presenceService.getPresenceKey('tenant-123', 'user-456');
      expect(key).toBe('presence:tenant-123:user-456');
    });
  });

  describe('getTenantOnlineKey', () => {
    test('should generate tenant online key', () => {
      const key = presenceService.getTenantOnlineKey('tenant-123');
      expect(key).toBe('online:tenant-123');
    });
  });

  describe('presenceCache', () => {
    test('should store presence data', () => {
      const socket = {
        id: 'socket-123',
        data: {
          user: {
            userId: 'user-456',
            tenantId: 'tenant-789',
            name: 'Test User'
          }
        }
      };

      presenceService.presenceCache.set('socket-123', {
        userId: 'user-456',
        status: 'online',
        lastSeen: new Date().toISOString()
      });

      const cached = presenceService.presenceCache.get('socket-123');
      expect(cached).toBeDefined();
      expect(cached.userId).toBe('user-456');
      expect(cached.status).toBe('online');
    });

    test('should remove presence data', () => {
      presenceService.presenceCache.set('socket-123', { userId: 'user-456' });
      presenceService.presenceCache.delete('socket-123');

      const cached = presenceService.presenceCache.get('socket-123');
      expect(cached).toBeUndefined();
    });
  });

  describe('typingIndicators', () => {
    test('should store typing indicator', () => {
      presenceService.typingIndicators.set('room-123:socket-456', {
        userId: 'user-789',
        isTyping: true,
        timestamp: Date.now()
      });

      const typing = presenceService.typingIndicators.get('room-123:socket-456');
      expect(typing).toBeDefined();
      expect(typing.isTyping).toBe(true);
    });

    test('should clear typing indicator for socket', () => {
      presenceService.typingIndicators.set('room-123:socket-456', { userId: 'user-789' });
      presenceService.typingIndicators.set('room-456:socket-456', { userId: 'user-789' });

      presenceService.clearTypingIndicator('socket-456');

      expect(presenceService.typingIndicators.has('room-123:socket-456')).toBe(false);
      expect(presenceService.typingIndicators.has('room-456:socket-456')).toBe(false);
    });
  });

  describe('validate status', () => {
    test('should accept valid status values', async () => {
      const mockSocket = {
        id: 'socket-123',
        data: { user: { userId: 'user-456', tenantId: 'tenant-789' } }
      };

      const validStatuses = ['online', 'offline', 'away', 'busy'];

      for (const status of validStatuses) {
        const result = await presenceService.updateStatus(mockSocket, status);
        expect(result).toBeDefined();
      }
    });
  });
});
