const authMiddleware = require('../../middleware/auth');
const jwt = require('jsonwebtoken');
const config = require('../../config');

describe('AuthMiddleware', () => {
  let mockSocket;

  beforeEach(() => {
    mockSocket = {
      id: 'test-socket-id',
      handshake: {
        auth: {},
        headers: {},
        query: {}
      },
      data: {}
    };
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  describe('extractToken', () => {
    test('should extract token from handshake auth', () => {
      const token = 'test-token';
      mockSocket.handshake.auth.token = token;

      const extracted = authMiddleware.extractToken(mockSocket);
      expect(extracted).toBe(token);
    });

    test('should extract token from authorization header', () => {
      const token = 'test-token';
      mockSocket.handshake.headers.authorization = `Bearer ${token}`;

      const extracted = authMiddleware.extractToken(mockSocket);
      expect(extracted).toBe(token);
    });

    test('should extract token from query', () => {
      const token = 'test-token';
      mockSocket.handshake.query.token = token;

      const extracted = authMiddleware.extractToken(mockSocket);
      expect(extracted).toBe(token);
    });

    test('should return null if no token found', () => {
      const extracted = authMiddleware.extractToken(mockSocket);
      expect(extracted).toBeNull();
    });
  });

  describe('verifyToken', () => {
    const validPayload = {
      userId: 'user-123',
      tenantId: 'tenant-456',
      executiveId: 'exec-789',
      roles: ['executive'],
      permissions: ['dashboard:view'],
      email: 'test@example.com',
      name: 'Test User'
    };

    test('should verify valid token', () => {
      const token = jwt.sign(validPayload, config.jwt.secret, {
        issuer: config.jwt.issuer,
        audience: config.jwt.audience
      });

      const decoded = authMiddleware.verifyToken(token);
      expect(decoded).toBeDefined();
      expect(decoded.userId).toBe(validPayload.userId);
      expect(decoded.tenantId).toBe(validPayload.tenantId);
    });

    test('should reject expired token', () => {
      const token = jwt.sign(validPayload, config.jwt.secret, {
        issuer: config.jwt.issuer,
        audience: config.jwt.audience,
        expiresIn: '0s'
      });

      expect(() => authMiddleware.verifyToken(token)).toThrow('Token expired');
    });

    test('should reject invalid token', () => {
      const token = 'invalid.token.here';

      expect(() => authMiddleware.verifyToken(token)).toThrow('Invalid token');
    });
  });

  describe('hasPermission', () => {
    beforeEach(() => {
      mockSocket.data.user = {
        userId: 'user-123',
        tenantId: 'tenant-456',
        roles: ['executive'],
        permissions: ['dashboard:view', 'kpi:view']
      };
    });

    test('should return true for existing permission', () => {
      const hasPermission = authMiddleware.hasPermission(mockSocket, 'dashboard:view');
      expect(hasPermission).toBe(true);
    });

    test('should return false for non-existing permission', () => {
      const hasPermission = authMiddleware.hasPermission(mockSocket, 'dashboard:edit');
      expect(hasPermission).toBe(false);
    });

    test('should return true for admin role', () => {
      mockSocket.data.user.roles = ['admin'];
      const hasPermission = authMiddleware.hasPermission(mockSocket, 'any:permission');
      expect(hasPermission).toBe(true);
    });

    test('should return false if no user data', () => {
      mockSocket.data.user = null;
      const hasPermission = authMiddleware.hasPermission(mockSocket, 'dashboard:view');
      expect(hasPermission).toBe(false);
    });
  });

  describe('hasRole', () => {
    beforeEach(() => {
      mockSocket.data.user = {
        userId: 'user-123',
        tenantId: 'tenant-456',
        roles: ['executive', 'viewer']
      };
    });

    test('should return true for existing role', () => {
      const hasRole = authMiddleware.hasRole(mockSocket, 'executive');
      expect(hasRole).toBe(true);
    });

    test('should return false for non-existing role', () => {
      const hasRole = authMiddleware.hasRole(mockSocket, 'admin');
      expect(hasRole).toBe(false);
    });
  });

  describe('connection tracking', () => {
    test('should add connection', () => {
      authMiddleware.addConnection('user-123', 'tenant-456', 'socket-789');
      expect(authMiddleware.connectionTracker.has('tenant-456:user-123')).toBe(true);
    });

    test('should remove connection', () => {
      authMiddleware.addConnection('user-123', 'tenant-456', 'socket-789');
      authMiddleware.removeConnection('user-123', 'tenant-456');
      expect(authMiddleware.connectionTracker.has('tenant-456:user-123')).toBe(false);
    });

    test('should count tenant connections', () => {
      authMiddleware.addConnection('user-1', 'tenant-456', 'socket-1');
      authMiddleware.addConnection('user-2', 'tenant-456', 'socket-2');
      authMiddleware.addConnection('user-3', 'tenant-789', 'socket-3');

      const count = authMiddleware.countTenantConnections('tenant-456');
      expect(count).toBe(2);
    });
  });
});
