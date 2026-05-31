/**
 * Connection Manager Tests
 */

const {
  ConnectionManager,
  ConnectionState,
  connectionManager,
} = require('./connectionManager');

describe('Connection Manager', () => {
  let manager;
  let mockWs;
  let mockReq;

  beforeEach(() => {
    manager = new ConnectionManager();
    mockWs = {
      readyState: 1, // OPEN
      clientId: null,
    };
    mockReq = {
      socket: {
        remoteAddress: '127.0.0.1',
      },
      headers: {
        'user-agent': 'test-agent',
      },
    };
  });

  describe('addConnection', () => {
    it('should add a new connection', () => {
      const connection = manager.addConnection(mockWs, mockReq);

      expect(connection).toBeDefined();
      expect(connection.id).toBeDefined();
      expect(connection.ip).toBe('127.0.0.1');
      expect(connection.state).toBe(ConnectionState.CONNECTING);
      expect(connection.ws).toBe(mockWs);
    });

    it('should assign client ID to WebSocket', () => {
      const connection = manager.addConnection(mockWs, mockReq);

      expect(mockWs.clientId).toBe(connection.id);
    });
  });

  describe('getConnection', () => {
    it('should return connection by ID', () => {
      const connection = manager.addConnection(mockWs, mockReq);

      const found = manager.getConnection(connection.id);

      expect(found).toBe(connection);
    });

    it('should return undefined for unknown ID', () => {
      const found = manager.getConnection('unknown-id');

      expect(found).toBeUndefined();
    });
  });

  describe('removeConnection', () => {
    it('should remove a connection', () => {
      const connection = manager.addConnection(mockWs, mockReq);

      const result = manager.removeConnection(connection.id);

      expect(result).toBe(true);
      expect(manager.getConnection(connection.id)).toBeUndefined();
    });

    it('should return false for unknown connection', () => {
      const result = manager.removeConnection('unknown-id');

      expect(result).toBe(false);
    });

    it('should remove from tenant index', () => {
      const connection = manager.addConnection(mockWs, mockReq);
      connection.tenantId = 'tenant1';
      manager.connectionsByTenant.set('tenant1', new Set([connection.id]));

      manager.removeConnection(connection.id);

      expect(manager.connectionsByTenant.has('tenant1')).toBe(false);
    });
  });

  describe('authenticateConnection', () => {
    it('should authenticate a connection', () => {
      const connection = manager.addConnection(mockWs, mockReq);
      const userInfo = {
        tenantId: 'tenant1',
        userId: 'user1',
        executiveLevel: 'C-LEVEL',
        roles: ['admin'],
      };

      const result = manager.authenticateConnection(connection.id, userInfo);

      expect(result).toBe(true);
      expect(connection.tenantId).toBe('tenant1');
      expect(connection.userId).toBe('user1');
      expect(connection.executiveLevel).toBe('C-LEVEL');
      expect(connection.state).toBe(ConnectionState.AUTHENTICATED);
    });

    it('should return false for unknown connection', () => {
      const result = manager.authenticateConnection('unknown-id', {});

      expect(result).toBe(false);
    });

    it('should add to tenant index', () => {
      const connection = manager.addConnection(mockWs, mockReq);
      const userInfo = { tenantId: 'tenant1', userId: 'user1' };

      manager.authenticateConnection(connection.id, userInfo);

      expect(manager.connectionsByTenant.has('tenant1')).toBe(true);
      expect(manager.connectionsByTenant.get('tenant1').has(connection.id)).toBe(true);
    });
  });

  describe('joinRoom / leaveRoom', () => {
    it('should add connection to room', () => {
      const connection = manager.addConnection(mockWs, mockReq);
      const roomId = 'tenant:tenant1:dashboard:main';

      const result = manager.joinRoom(connection.id, roomId);

      expect(result).toBe(true);
      expect(connection.rooms.has(roomId)).toBe(true);
      expect(manager.rooms.has(roomId)).toBe(true);
      expect(manager.rooms.get(roomId).has(connection.id)).toBe(true);
    });

    it('should remove connection from room', () => {
      const connection = manager.addConnection(mockWs, mockReq);
      const roomId = 'tenant:tenant1:dashboard:main';

      manager.joinRoom(connection.id, roomId);
      const result = manager.leaveRoom(connection.id, roomId);

      expect(result).toBe(true);
      expect(connection.rooms.has(roomId)).toBe(false);
    });

    it('should delete empty rooms', () => {
      const connection = manager.addConnection(mockWs, mockReq);
      const roomId = 'tenant:tenant1:dashboard:main';

      manager.joinRoom(connection.id, roomId);
      manager.leaveRoom(connection.id, roomId);

      expect(manager.rooms.has(roomId)).toBe(false);
    });
  });

  describe('getRoomConnections', () => {
    it('should return connections in a room', () => {
      const conn1 = manager.addConnection(mockWs, mockReq);
      const conn2 = manager.addConnection({ ...mockWs }, mockReq);
      const roomId = 'tenant:tenant1:dashboard:main';

      manager.joinRoom(conn1.id, roomId);
      manager.joinRoom(conn2.id, roomId);

      const connections = manager.getRoomConnections(roomId);

      expect(connections).toHaveLength(2);
      expect(connections).toContain(conn1);
      expect(connections).toContain(conn2);
    });

    it('should return empty array for non-existent room', () => {
      const connections = manager.getRoomConnections('non-existent');

      expect(connections).toEqual([]);
    });

    it('should filter out closed connections', () => {
      const conn1 = manager.addConnection(mockWs, mockReq);
      const conn2 = manager.addConnection({ ...mockWs, readyState: 0 }, mockReq);
      const roomId = 'tenant:tenant1:dashboard:main';

      manager.joinRoom(conn1.id, roomId);
      manager.joinRoom(conn2.id, roomId);

      const connections = manager.getRoomConnections(roomId);

      expect(connections).toHaveLength(1);
      expect(connections[0]).toBe(conn1);
    });
  });

  describe('getTenantConnections', () => {
    it('should return connections for a tenant', () => {
      const conn1 = manager.addConnection(mockWs, mockReq);
      conn1.tenantId = 'tenant1';
      manager.connectionsByTenant.set('tenant1', new Set([conn1.id]));

      const connections = manager.getTenantConnections('tenant1');

      expect(connections).toHaveLength(1);
      expect(connections[0]).toBe(conn1);
    });

    it('should return empty array for non-existent tenant', () => {
      const connections = manager.getTenantConnections('non-existent');

      expect(connections).toEqual([]);
    });
  });

  describe('getStats', () => {
    it('should return connection statistics', () => {
      const conn1 = manager.addConnection(mockWs, mockReq);
      conn1.tenantId = 'tenant1';
      manager.connectionsByTenant.set('tenant1', new Set([conn1.id]));
      const roomId = 'tenant:tenant1:dashboard:main';
      manager.joinRoom(conn1.id, roomId);

      const stats = manager.getStats();

      expect(stats.totalConnections).toBe(1);
      expect(stats.totalTenants).toBe(1);
      expect(stats.totalRooms).toBe(1);
      expect(stats.connectionsPerTenant).toHaveLength(1);
      expect(stats.connectionsPerTenant[0]).toMatchObject({
        tenantId: 'tenant1',
        count: 1,
      });
    });
  });

  describe('cleanup', () => {
    it('should remove dead connections', () => {
      const conn1 = manager.addConnection(mockWs, mockReq);
      const conn2 = manager.addConnection({ ...mockWs, readyState: 0 }, mockReq);

      const removed = manager.cleanup();

      expect(removed).toBeGreaterThan(0);
      expect(manager.has(conn1.id)).toBe(false);
    });
  });

  describe('disconnectTenant', () => {
    it('should disconnect all tenant connections', () => {
      const closeMock = jest.fn();
      const conn1 = manager.addConnection({ ...mockWs, close: closeMock }, mockReq);
      conn1.tenantId = 'tenant1';
      manager.connectionsByTenant.set('tenant1', new Set([conn1.id]));

      const disconnected = manager.disconnectTenant('tenant1', 1000, 'Test disconnect');

      expect(disconnected).toBe(1);
      expect(closeMock).toHaveBeenCalledWith(1000, 'Test disconnect');
    });
  });
});
