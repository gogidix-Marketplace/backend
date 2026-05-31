const roomService = require('../../services/RoomService');

describe('RoomService', () => {
  beforeEach(() => {
    roomService.localRooms.clear();
    roomService.roomMetadata.clear();
  });

  describe('generateRoomId', () => {
    test('should generate room ID with tenant prefix', () => {
      const roomId = roomService.generateRoomId('tenant-123', 'test-room');
      expect(roomId).toContain('tenant:tenant-123:room:');
    });

    test('should generate unique room IDs', () => {
      const roomId1 = roomService.generateRoomId('tenant-123', 'room1');
      const roomId2 = roomService.generateRoomId('tenant-123', 'room2');
      expect(roomId1).not.toBe(roomId2);
    });
  });

  describe('createRoom', () => {
    test('should create room with metadata', async () => {
      const room = await roomService.createRoom('tenant-123', 'Test Room', 'public', {
        maxUsers: 50,
        createdBy: 'user-456'
      });

      expect(room).toBeDefined();
      expect(room.tenantId).toBe('tenant-123');
      expect(room.name).toBe('Test Room');
      expect(room.type).toBe('public');
      expect(room.maxUsers).toBe(50);
      expect(room.createdBy).toBe('user-456');
    });

    test('should store room in memory', async () => {
      const room = await roomService.createRoom('tenant-123', 'Test Room', 'public');
      const stored = roomService.roomMetadata.get(room.id);

      expect(stored).toBeDefined();
      expect(stored.name).toBe('Test Room');
    });
  });

  describe('getRoom', () => {
    test('should get room from memory', async () => {
      const createdRoom = await roomService.createRoom('tenant-123', 'Test Room', 'public');
      const room = await roomService.getRoom(createdRoom.id);

      expect(room).toBeDefined();
      expect(room.id).toBe(createdRoom.id);
    });

    test('should return null for non-existent room', async () => {
      const room = await roomService.getRoom('non-existent-room');
      expect(room).toBeUndefined();
    });
  });

  describe('ensureTenantPrefix', () => {
    test('should add tenant prefix if multi-tenant is enabled', () => {
      const prefixed = roomService.ensureTenantPrefix('my-room', 'tenant-123');
      expect(prefixed).toBe('tenant:tenant-123:my-room');
    });

    test('should preserve existing tenant prefix', () => {
      const roomId = 'tenant:tenant-123:my-room';
      const prefixed = roomService.ensureTenantPrefix(roomId, 'tenant-123');
      expect(prefixed).toBe(roomId);
    });
  });

  describe('extractTenantId', () => {
    test('should extract tenant ID from room ID', () => {
      const roomId = 'tenant:tenant-123:room:abc123';
      const tenantId = roomService.extractTenantId(roomId);
      expect(tenantId).toBe('tenant-123');
    });

    test('should return default tenant ID if no tenant in room', () => {
      const roomId = 'room:abc123';
      const tenantId = roomService.extractTenantId(roomId);
      expect(tenantId).toBeDefined();
    });
  });

  describe('get system rooms', () => {
    test('should generate system room name', () => {
      const systemRoom = roomService.getSystemRoom('tenant-123', 'announcements');
      expect(systemRoom).toBe('tenant:tenant-123:system:announcements');
    });

    test('should generate user room name', () => {
      const userRoom = roomService.getUserRoom('tenant-123', 'user-456');
      expect(userRoom).toBe('tenant:tenant-123:user:user-456');
    });

    test('should generate dashboard room name', () => {
      const dashboardRoom = roomService.getDashboardRoom('tenant-123', 'dash-789');
      expect(dashboardRoom).toBe('tenant:tenant-123:dashboard:dash-789');
    });

    test('should generate KPI room name', () => {
      const kpiRoom = roomService.getKpiRoom('tenant-123', 'kpi-101');
      expect(kpiRoom).toBe('tenant:tenant-123:kpi:kpi-101');
    });

    test('should generate notification room name', () => {
      const notifRoom = roomService.getNotificationRoom('tenant-123', 'user-456');
      expect(notifRoom).toBe('tenant:tenant-123:notifications:user-456');
    });
  });

  describe('isTenantRoom', () => {
    test('should return true for room with matching tenant', () => {
      const roomId = 'tenant:tenant-123:room:abc';
      const isMatch = roomService.isTenantRoom(roomId, 'tenant-123');
      expect(isMatch).toBe(true);
    });

    test('should return false for room with different tenant', () => {
      const roomId = 'tenant:tenant-456:room:abc';
      const isMatch = roomService.isTenantRoom(roomId, 'tenant-123');
      expect(isMatch).toBe(false);
    });
  });
});
