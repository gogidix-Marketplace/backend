const request = require('supertest');
const app = require('../../app');

describe('API Integration Tests', () => {
  let server;

  beforeAll(async () => {
    server = app.getServer();
  });

  afterAll(async () => {
    if (server) {
      server.close();
    }
  });

  describe('Health endpoints', () => {
    test('GET / should return service info', async () => {
      const response = await request(app.getApp()).get('/');

      expect(response.status).toBe(200);
      expect(response.body.name).toBe('Gogidix WebSocket Service');
      expect(response.body.status).toBe('running');
    });

    test('GET /health should return health status', async () => {
      const response = await request(app.getApp()).get('/health');

      expect(response.status).toBeLessThan(500);
      expect(response.body).toHaveProperty('status');
      expect(response.body).toHaveProperty('timestamp');
      expect(response.body).toHaveProperty('checks');
    });

    test('GET /health/live should return alive status', async () => {
      const response = await request(app.getApp()).get('/health/live');

      expect(response.status).toBe(200);
      expect(response.body.status).toBe('alive');
    });
  });

  describe('Not found handler', () => {
    test('GET /non-existent should return 404', async () => {
      const response = await request(app.getApp()).get('/non-existent');

      expect(response.status).toBe(404);
      expect(response.body.success).toBe(false);
    });
  });

  describe('CORS', () => {
    test('should handle OPTIONS request', async () => {
      const response = await request(app.getApp())
        .options('/health')
        .set('Origin', 'http://localhost:3000');

      expect([200, 204]).toContain(response.status);
    });
  });
});
