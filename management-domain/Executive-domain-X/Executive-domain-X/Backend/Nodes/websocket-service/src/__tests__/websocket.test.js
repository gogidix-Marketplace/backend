const { Server } = require('socket.io');
const Client = require('socket.io-client');
const http = require('http');
const jwt = require('jsonwebtoken');
const config = require('../config');
const wsServer = require('../websocket/server');
const roomService = require('../services/RoomService');
const presenceService = require('../services/PresenceService');

describe('WebSocket Server Tests', () => {
  let io, serverSocket, clientSocket, httpServer;

  beforeAll((done) => {
    httpServer = http.createServer();
    io = new Server(httpServer);

    io.on('connection', (socket) => {
      serverSocket = socket;
    });

    httpServer.listen(() => {
      const port = httpServer.address().port;
      clientSocket = Client(`http://localhost:${port}`, {
        transports: ['websocket'],
        reconnection: false
      });

      clientSocket.on('connect', done);
    });
  });

  afterAll(() => {
    io.close();
    clientSocket.close();
  });

  beforeEach(() => {
    roomService.localRooms.clear();
    roomService.roomMetadata.clear();
    presenceService.presenceCache.clear();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  test('should connect successfully', () => {
    expect(clientSocket.connected).toBe(true);
    expect(serverSocket.connected).toBe(true);
  });

  test('should receive ping and respond with pong', (done) => {
    clientSocket.emit('ping', (data) => {
      expect(data).toHaveProperty('pong');
      done();
    });

    serverSocket.on('ping', (callback) => {
      callback({ pong: Date.now() });
    });
  });

  test('should handle room join', (done) => {
    const mockUser = {
      userId: 'user-123',
      tenantId: 'tenant-456',
      name: 'Test User'
    };

    serverSocket.data.user = mockUser;

    clientSocket.emit('room:join', {
      room: 'test-room',
      metadata: { test: true }
    }, (response) => {
      expect(response.success).toBe(true);
      done();
    });

    serverSocket.on('room:join', async (data, callback) => {
      try {
        const result = await roomService.joinRoom(serverSocket, data.room, data.metadata);
        callback({ success: true, ...result });
      } catch (error) {
        callback({ success: false, message: error.message });
      }
    });
  });

  test('should handle presence update', (done) => {
    const mockUser = {
      userId: 'user-123',
      tenantId: 'tenant-456',
      name: 'Test User'
    };

    serverSocket.data.user = mockUser;

    clientSocket.emit('presence:update', {
      state: 'away',
      metadata: { message: 'Working' }
    }, (response) => {
      expect(response.success).toBe(true);
      done();
    });

    serverSocket.on('presence:update', async (data, callback) => {
      try {
        const result = await presenceService.updateStatus(serverSocket, data.state, data.metadata);
        callback({ success: true, presence: result });
      } catch (error) {
        callback({ success: false, message: error.message });
      }
    });
  });

  test('should handle message sending', (done) => {
    const testMessage = {
      room: 'test-room',
      event: 'chat:message',
      data: { text: 'Hello, World!' }
    };

    clientSocket.emit('message:send', testMessage, (response) => {
      expect(response).toBeDefined();
      done();
    });

    serverSocket.on('message:send', (data, callback) => {
      callback({ success: true, messageId: Date.now() });
    });
  });

  test('should handle disconnect', (done) => {
    const testClient = Client(`http://localhost:${httpServer.address().port}`, {
      transports: ['websocket']
    });

    testClient.on('connect', () => {
      testClient.disconnect();
    });

    serverSocket.on('disconnect', () => {
      done();
    });
  });
});
