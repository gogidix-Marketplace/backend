const request = require('supertest');
const app = require('../server');

describe('Server Tests', () => {
  describe('Health Check', () => {
    test('GET /api/health should return status ok', async () => {
      const response = await request(app)
        .get('/api/health')
        .expect(200);

      expect(response.body.status).toBe('ok');
      expect(response.body.service).toBe('kafka-consumer-service');
      expect(response.body.timestamp).toBeDefined();
    });
  });

  describe('Event Stats Endpoint', () => {
    test('GET /api/events/stats should return event statistics', async () => {
      const response = await request(app)
        .get('/api/events/stats')
        .expect(200);

      expect(response.body.success).toBeDefined();
    });
  });

  describe('Event Handler Registration', () => {
    test('POST /api/events/register should register event handler', async () => {
      const response = await request(app)
        .post('/api/events/register')
        .send({
          eventType: 'TEST_EVENT',
          handler: '() => ({ success: true })'
        })
        .expect(200);

      expect(response.body.success).toBe(true);
    });

    test('POST /api/events/register should return error for missing handler', async () => {
      const response = await request(app)
        .post('/api/events/register')
        .send({
          eventType: 'TEST_EVENT'
        })
        .expect(400);

      expect(response.body.success).toBe(false);
    });
  });
});