const request = require('supertest');
const app = require('../../src/server');
const mongodb = require('../../src/config/mongodb');

describe('Server Integration Tests', () => {
  beforeAll(async () => {
    // Connect to test database
    await mongodb.connect();
  });

  afterAll(async () => {
    await mongodb.disconnect();
  });

  describe('Health Endpoints', () => {
    it('GET /health should return health status', async () => {
      const response = await request(app)
        .get('/health')
        .expect(200);

      expect(response.body).toHaveProperty('status', 'ok');
      expect(response.body).toHaveProperty('service', 'kafka-consumer-service');
      expect(response.body).toHaveProperty('uptime');
    });

    it('GET /ready should check dependencies', async () => {
      const response = await request(app)
        .get('/ready')
        .expect(expect.anything());

      expect(response.body).toHaveProperty('status');
      expect(response.body).toHaveProperty('checks');
      expect(response.body.checks).toHaveProperty('kafka');
      expect(response.body.checks).toHaveProperty('mongodb');
      expect(response.body.checks).toHaveProperty('redis');
    });

    it('GET /live should return alive status', async () => {
      const response = await request(app)
        .get('/live')
        .expect(200);

      expect(response.body).toHaveProperty('status', 'alive');
      expect(response.body).toHaveProperty('uptime');
    });
  });

  describe('Metrics Endpoints', () => {
    it('GET /metrics should return Prometheus metrics', async () => {
      const response = await request(app)
        .get('/metrics')
        .expect(200);

      expect(response.headers['content-type']).toContain('text/plain');
      expect(response.text).toContain('# HELP');
    });

    it('GET /api/stats/health should return health checks', async () => {
      const response = await request(app)
        .get('/api/stats/health')
        .expect(expect.anything());

      expect(response.body).toHaveProperty('status');
      expect(response.body).toHaveProperty('checks');
    });

    it('GET /api/stats/overview should return service overview', async () => {
      const response = await request(app)
        .get('/api/stats/overview')
        .expect(expect.anything());

      expect(response.body).toHaveProperty('success');
      expect(response.body.data).toHaveProperty('connections');
    });
  });

  describe('API Routes', () => {
    it('GET /api/health should return API health', async () => {
      const response = await request(app)
        .get('/api/health')
        .expect(200);

      expect(response.body).toHaveProperty('status', 'ok');
    });

    it('GET /api/events/stats should return event statistics', async () => {
      const response = await request(app)
        .get('/api/events/stats')
        .expect(expect.anything());

      expect(response.body).toHaveProperty('success');
    });

    it('GET /api/dlq/stats should return DLQ statistics', async () => {
      const response = await request(app)
        .get('/api/dlq/stats')
        .expect(expect.anything());

      expect(response.body).toHaveProperty('success');
    });

    it('GET /api/consumer/status should return consumer status', async () => {
      const response = await request(app)
        .get('/api/consumer/status')
        .expect(expect.anything());

      expect(response.body).toHaveProperty('success');
      expect(response.body.data).toHaveProperty('kafka');
      expect(response.body.data).toHaveProperty('circuitBreakers');
    });
  });

  describe('Error Handling', () => {
    it('GET /nonexistent should return 404', async () => {
      const response = await request(app)
        .get('/nonexistent')
        .expect(404);

      expect(response.body).toHaveProperty('success', false);
      expect(response.body).toHaveProperty('message');
    });

    it('GET /api/nonexistent should return 404', async () => {
      const response = await request(app)
        .get('/api/nonexistent')
        .expect(404);

      expect(response.body).toHaveProperty('success', false);
    });
  });

  describe('Request ID Middleware', () => {
    it('should include X-Request-ID header', async () => {
      const response = await request(app)
        .get('/health')
        .expect(200);

      expect(response.headers['x-request-id']).toBeDefined();
    });

    it('should use provided X-Request-ID', async () => {
      const customId = 'custom-request-id-123';

      const response = await request(app)
        .get('/health')
        .set('X-Request-ID', customId)
        .expect(200);

      expect(response.headers['x-request-id']).toBe(customId);
    });
  });
});
