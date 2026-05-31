/**
 * WebSocket Server Tests
 */

const WebSocket = require('ws');
const { WebSocketServer } = require('./server');
const { signToken } = require('../auth/jwt');

describe('WebSocketServer', () => {
  let wss;
  let httpServer;
  let wsPort;

  beforeEach((done) => {
    // Create a test HTTP server
    const http = require('http');
    httpServer = http.createServer();
    httpServer.listen(0, () => {
      wsPort = httpServer.address().port;
      wss = new WebSocketServer({ server: httpServer });
      wss.initialize().then(done);
    });
  });

  afterEach((done) => {
    if (wss) {
      wss.shutdown().then(() => {
        httpServer.close(done);
      });
    } else {
      httpServer.close(done);
    }
  });

  describe('Connection', () => {
    test('should accept new WebSocket connections', (done) => {
      const ws = new WebSocket(`ws://localhost:${wsPort}/ws`);

      ws.on('open', () => {
        ws.close();
        done();
      });

      ws.on('error', (err) => {
        done(err);
      });
    });

    test('should send welcome message on connection', (done) => {
      const ws = new WebSocket(`ws://localhost:${wsPort}/ws`);

      ws.on('message', (data) => {
        const message = JSON.parse(data);
        expect(message.type).toBe('welcome');
        expect(message.clientId).toBeDefined();
        expect(message.requiresAuth).toBe(true);
        ws.close();
        done();
      });
    });

    test('should authenticate with valid JWT', (done) => {
      const token = signToken({
        tenantId: 'tenant-1',
        userId: 'user-1',
        executiveLevel: 'CEO',
      });

      const ws = new WebSocket(`ws://localhost:${wsPort}/ws`);
      let clientId = null;

      ws.on('message', (data) => {
        const message = JSON.parse(data);

        if (message.type === 'welcome') {
          clientId = message.clientId;
          // Send auth message
          ws.send(JSON.stringify({ type: 'auth', token }));
        }

        if (message.type === 'authenticated') {
          expect(message.tenantId).toBe('tenant-1');
          expect(message.userId).toBe('user-1');
          ws.close();
          done();
        }
      });
    });

    test('should reject invalid JWT', (done) => {
      const ws = new WebSocket(`ws://localhost:${wsPort}/ws`);
      let clientId = null;

      ws.on('message', (data) => {
        const message = JSON.parse(data);

        if (message.type === 'welcome') {
          clientId = message.clientId;
          // Send invalid auth
          ws.send(JSON.stringify({ type: 'auth', token: 'invalid' }));
        }

        if (message.type === 'error') {
          ws.close();
          done();
        }
      });

      ws.on('close', () => {
        // Connection should close after auth failure
        done();
      });
    });
  });

  describe('Subscriptions', () => {
    test('should allow authenticated clients to subscribe to rooms', (done) => {
      const token = signToken({
        tenantId: 'tenant-1',
        userId: 'user-1',
        executiveLevel: 'CEO',
      });

      const ws = new WebSocket(`ws://localhost:${wsPort}/ws`);

      ws.on('message', (data) => {
        const message = JSON.parse(data);

        if (message.type === 'authenticated') {
          // Subscribe to rooms
          ws.send(JSON.stringify({
            type: 'subscribe',
            rooms: [`tenant:tenant-1`, `tenant:tenant-1:FINANCIAL`],
          }));
        }

        if (message.type === 'subscribed') {
          expect(message.rooms).toHaveLength(2);
          ws.close();
          done();
        }
      });

      ws.on('open', () => {
        ws.send(JSON.stringify({ type: 'auth', token }));
      });
    });

    test('should prevent cross-tenant room access', (done) => {
      const token = signToken({
        tenantId: 'tenant-1',
        userId: 'user-1',
        executiveLevel: 'CEO',
      });

      const ws = new WebSocket(`ws://localhost:${wsPort}/ws`);

      ws.on('message', (data) => {
        const message = JSON.parse(data);

        if (message.type === 'authenticated') {
          // Try to subscribe to different tenant
          ws.send(JSON.stringify({
            type: 'subscribe',
            rooms: [`tenant:tenant-2`, `tenant:tenant-1`],
          }));
        }

        if (message.type === 'subscribed') {
          // Should only be subscribed to own tenant
          expect(message.rooms).toContain('tenant:tenant-1');
          expect(message.rooms).not.toContain('tenant:tenant-2');
          ws.close();
          done();
        }
      });

      ws.on('open', () => {
        ws.send(JSON.stringify({ type: 'auth', token }));
      });
    });
  });

  describe('Broadcasting', () => {
    test('should broadcast message to room subscribers', (done) => {
      const token = signToken({
        tenantId: 'tenant-1',
        userId: 'user-1',
        executiveLevel: 'CEO',
      });

      const ws1 = new WebSocket(`ws://localhost:${wsPort}/ws`);
      const ws2 = new WebSocket(`ws://localhost:${wsPort}/ws`);
      let ws1Authenticated = false;
      let ws2Authenticated = false;

      const checkBothAuthenticated = () => {
        if (ws1Authenticated && ws2Authenticated) {
          // Subscribe ws1 to room
          ws1.send(JSON.stringify({
            type: 'subscribe',
            rooms: ['tenant:tenant-1:test-room'],
          }));
        }
      };

      ws1.on('message', (data) => {
        const message = JSON.parse(data);

        if (message.type === 'authenticated') {
          ws1Authenticated = true;
          checkBothAuthenticated();
        }

        if (message.type === 'subscribed') {
          // Broadcast to room
          const sent = wss.broadcastToRoom('tenant:tenant-1:test-room', {
            type: 'test',
            data: 'hello',
          });
          expect(sent).toBe(1);
        }

        if (message.type === 'test' && message.data === 'hello') {
          ws1.close();
          ws2.close();
          done();
        }
      });

      ws2.on('message', (data) => {
        const message = JSON.parse(data);
        if (message.type === 'authenticated') {
          ws2Authenticated = true;
          checkBothAuthenticated();
        }
      });

      ws1.on('open', () => ws1.send(JSON.stringify({ type: 'auth', token })));
      ws2.on('open', () => ws2.send(JSON.stringify({ type: 'auth', token })));
    });
  });

  describe('Rate Limiting', () => {
    test('should rate limit client messages', (done) => {
      const token = signToken({
        tenantId: 'tenant-1',
        userId: 'user-1',
        executiveLevel: 'CEO',
      });

      const ws = new WebSocket(`ws://localhost:${wsPort}/ws`);
      let messageCount = 0;
      let rateLimited = false;

      ws.on('message', (data) => {
        const message = JSON.parse(data);

        if (message.type === 'authenticated') {
          // Send many messages quickly
          for (let i = 0; i < 150; i++) {
            ws.send(JSON.stringify({ type: 'ping' }));
          }
        }

        if (message.type === 'error' && message.code === 4290) {
          rateLimited = true;
          ws.close();
          expect(rateLimited).toBe(true);
          done();
        }

        messageCount++;
      });

      ws.on('open', () => {
        ws.send(JSON.stringify({ type: 'auth', token }));
      });
    }, 10000);
  });

  describe('Stats', () => {
    test('should return server statistics', () => {
      const stats = wss.getStats();

      expect(stats).toHaveProperty('totalConnections');
      expect(stats).toHaveProperty('totalTenants');
      expect(stats).toHaveProperty('totalRooms');
      expect(stats).toHaveProperty('connectionsPerTenant');
    });
  });

  describe('Disconnection', () => {
    test('should clean up client on disconnect', (done) => {
      const token = signToken({
        tenantId: 'tenant-1',
        userId: 'user-1',
        executiveLevel: 'CEO',
      });

      const ws = new WebSocket(`ws://localhost:${wsPort}/ws`);

      ws.on('message', (data) => {
        const message = JSON.parse(data);

        if (message.type === 'authenticated') {
          const statsBefore = wss.getStats();
          expect(statsBefore.totalConnections).toBeGreaterThan(0);

          ws.close();

          setTimeout(() => {
            const statsAfter = wss.getStats();
            expect(statsAfter.totalConnections).toBe(0);
            done();
          }, 100);
        }
      });

      ws.on('open', () => {
        ws.send(JSON.stringify({ type: 'auth', token }));
      });
    });
  });
});
