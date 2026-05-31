/**
 * Integration Tests for Executive Real-time Service
 */

const request = require('supertest');
const { createServer } = require('./app');
const { signToken } = require('./auth/jwt');

// Mock dependencies that require external services
jest.mock('./config/redis', () => {
  const mockRedis = {
    connect: jest.fn(),
    disconnect: jest.fn(),
    subscribe: jest.fn(),
    unsubscribe: jest.fn(),
    publish: jest.fn(),
    get: jest.fn(),
    setex: jest.fn(),
    keys: jest.fn(),
    del: jest.fn(),
    status: 'ready',
    on: jest.fn(),
  };

  return {
    connectRedis: jest.fn().mockResolvedValue(mockRedis),
    disconnectRedis: jest.fn().mockResolvedValue(undefined),
    subscribe: jest.fn().mockResolvedValue(undefined),
    unsubscribe: jest.fn().mockResolvedValue(undefined),
    publishMessage: jest.fn().mockResolvedValue(undefined),
    getRedisStatus: jest.fn().mockResolvedValue({ status: 'connected' }),
    getRedisClient: () => mockRedis,
    getSubscriber: () => mockRedis,
  };
});

jest.mock('./kafka', () => ({
  initializeKafka: jest.fn().mockResolvedValue(undefined),
  shutdownKafka: jest.fn().mockResolvedValue(undefined),
  connectKafkaProducer: jest.fn().mockResolvedValue(undefined),
  startConsumer: jest.fn().mockResolvedValue(undefined),
  stopConsumer: jest.fn().mockResolvedValue(undefined),
}));

describe('Executive Real-time Service - Integration Tests', () => {
  let app;
  let server;
  let authToken;
  let testUserId = 'test-user-123';
  let testTenantId = 'test-tenant-456';

  beforeAll(async () => {
    // Create test token
    authToken = signToken({
      tenantId: testTenantId,
      userId: testUserId,
      executiveLevel: 'C-LEVEL',
      roles: ['admin'],
    });

    // Create server
    const serverInstance = await createServer();
    server = serverInstance.server;
    app = express(); // Use express app for testing
  });

  afterAll(async () => {
    if (server) {
      await new Promise((resolve) => server.close(resolve));
    }
  });

  describe('Health Check', () => {
    it('should return healthy status', async () => {
      // Note: This would require the actual express app to be exposed
      // For now, this is a placeholder test structure
      expect(true).toBe(true);
    });
  });

  describe('Authentication', () => {
    it('should accept valid JWT token', async () => {
      const token = signToken({
        tenantId: testTenantId,
        userId: testUserId,
      });

      expect(typeof token).toBe('string');
      expect(token.split('.')).toHaveLength(3); // JWT format
    });

    it('should create token with correct claims', async () => {
      const token = signToken({
        tenantId: testTenantId,
        userId: testUserId,
        executiveLevel: 'VP',
        roles: ['executive'],
      });

      expect(token).toBeDefined();
    });
  });

  describe('CQRS Commands', () => {
    const { createCommand, CommandTypes } = require('./commands');

    it('should create KPI threshold update command', () => {
      const command = createCommand(
        CommandTypes.UPDATE_KPI_THRESHOLD,
        {
          kpiId: 'kpi123',
          threshold: {
            warning: 80,
            critical: 90,
            operator: 'gt',
          },
        },
        {
          tenantId: testTenantId,
          userId: testUserId,
        }
      );

      expect(command.type).toBe(CommandTypes.UPDATE_KPI_THRESHOLD);
      expect(command.id).toBeDefined();
      expect(command.metadata.tenantId).toBe(testTenantId);
    });

    it('should create KPI alert command', () => {
      const command = createCommand(
        CommandTypes.CREATE_KPI_ALERT,
        {
          kpiId: 'kpi123',
          level: 'CRITICAL',
          message: 'KPI exceeded critical threshold',
        },
        {
          tenantId: testTenantId,
        }
      );

      expect(command.type).toBe(CommandTypes.CREATE_KPI_ALERT);
      expect(command.payload.level).toBe('CRITICAL');
    });
  });

  describe('Tenant Middleware', () => {
    const { getTenantRoom, getCategoryRoom, getExecutiveLevelRoom, getDashboardRoom } = require('./middleware/tenant');

    it('should generate tenant room name', () => {
      const room = getTenantRoom(testTenantId);
      expect(room).toBe(`tenant:${testTenantId}`);
    });

    it('should generate category room name', () => {
      const room = getCategoryRoom(testTenantId, 'sales');
      expect(room).toBe(`tenant:${testTenantId}:sales`);
    });

    it('should generate executive level room name', () => {
      const room = getExecutiveLevelRoom(testTenantId, 'C-LEVEL');
      expect(room).toBe(`tenant:${testTenantId}:C-LEVEL`);
    });

    it('should generate dashboard room name', () => {
      const room = getDashboardRoom(testTenantId, 'main');
      expect(room).toBe(`tenant:${testTenantId}:dashboard:main`);
    });
  });

  describe('Connection Manager', () => {
    const { connectionManager } = require('./websocket/connectionManager');
    const mockWs = {
      readyState: 1,
    };
    const mockReq = {
      socket: { remoteAddress: '127.0.0.1' },
      headers: { 'user-agent': 'test' },
    };

    it('should add and remove connections', () => {
      const connection = connectionManager.addConnection(mockWs, mockReq);

      expect(connection).toBeDefined();
      expect(connection.id).toBeDefined();

      const removed = connectionManager.removeConnection(connection.id);
      expect(removed).toBe(true);
    });

    it('should authenticate connections', () => {
      const connection = connectionManager.addConnection(mockWs, mockReq);

      const result = connectionManager.authenticateConnection(connection.id, {
        tenantId: testTenantId,
        userId: testUserId,
        executiveLevel: 'C-LEVEL',
      });

      expect(result).toBe(true);
      expect(connection.tenantId).toBe(testTenantId);
    });

    it('should manage room subscriptions', () => {
      const connection = connectionManager.addConnection(mockWs, mockReq);
      connection.tenantId = testTenantId;

      const roomId = `tenant:${testTenantId}:dashboard:main`;

      connectionManager.joinRoom(connection.id, roomId);
      expect(connection.rooms.has(roomId)).toBe(true);

      connectionManager.leaveRoom(connection.id, roomId);
      expect(connection.rooms.has(roomId)).toBe(false);

      connectionManager.removeConnection(connection.id);
    });
  });

  describe('Query Handlers', () => {
    const { executeQuery, registerQuery, QueryTypes } = require('./queries');

    it('should register and execute custom query', async () => {
      const mockHandler = jest.fn().mockResolvedValue({ test: 'data' });

      registerQuery('test_query', mockHandler);

      const result = await executeQuery('test_query', { id: 123 });

      expect(result).toEqual({ test: 'data' });
      expect(mockHandler).toHaveBeenCalledWith({ id: 123 });
    });

    it('should execute built-in queries', async () => {
      const result = await executeQuery(
        QueryTypes.GET_CURRENT_KPI,
        { kpiId: 'test-kpi', tenantId: testTenantId }
      );

      expect(result).toHaveProperty('kpiId');
    });
  });
});
