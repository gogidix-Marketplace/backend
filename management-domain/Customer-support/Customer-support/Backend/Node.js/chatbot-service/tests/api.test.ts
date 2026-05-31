/**
 * API Integration Tests
 */

import request from 'supertest';
import { ChatbotService } from '../src/index';
import { MongoMemoryServer } from 'mongodb-memory-server';
import mongoose from 'mongoose';

describe('API Integration Tests', () => {
  let mongoServer: MongoMemoryServer;
  let app: any;
  let server: any;

  beforeAll(async () => {
    mongoServer = await MongoMemoryServer.create();
    const mongoUri = mongoServer.getUri();

    // Set environment variables for testing
    process.env.MONGODB_URI = mongoUri;
    process.env.NODE_ENV = 'test';
    process.env.JWT_SECRET = 'test-secret-key';
    process.env.OPENAI_API_KEY = 'test-key';

    const service = new ChatbotService();
    app = service.app;
  });

  afterAll(async () => {
    await mongoose.disconnect();
    await mongoServer.stop();
  });

  describe('Health Endpoints', () => {
    it('GET /health should return health status', async () => {
      const response = await request(app).get('/api/v1/health');

      expect(response.status).toBe(200);
      expect(response.body).toHaveProperty('status');
    });

    it('GET /status should return system status', async () => {
      const response = await request(app).get('/api/v1/health/status');

      expect(response.status).toBe(200);
      expect(response.body).toHaveProperty('system');
    });
  });

  describe('Chat Endpoints', () => {
    it('POST /sessions should create new session', async () => {
      const response = await request(app)
        .post('/api/v1/chat/sessions')
        .send({
          customerId: 'test-customer',
          language: 'en',
        });

      expect(response.status).toBe(201);
      expect(response.body.success).toBe(true);
      expect(response.body.data).toHaveProperty('sessionId');
    });

    it('POST /send should send message', async () => {
      // First create a session
      const sessionResponse = await request(app)
        .post('/api/v1/chat/sessions')
        .send({ customerId: 'test-customer' });

      const { sessionId } = sessionResponse.body.data;

      const response = await request(app)
        .post('/api/v1/chat/send')
        .send({
          sessionId,
          message: 'Hello, I need help',
          language: 'en',
        });

      expect(response.status).toBe(200);
      expect(response.body.success).toBe(true);
      expect(response.body.data).toHaveProperty('type');
    });

    it('GET /sessions/:sessionId should return session details', async () => {
      const sessionResponse = await request(app)
        .post('/api/v1/chat/sessions')
        .send({ customerId: 'test-customer' });

      const { sessionId } = sessionResponse.body.data;

      const response = await request(app)
        .get(`/api/v1/chat/sessions/${sessionId}`);

      expect(response.status).toBe(200);
      expect(response.body.data).toHaveProperty('sessionId', sessionId);
    });
  });

  describe('Validation', () => {
    it('should reject empty message', async () => {
      const response = await request(app)
        .post('/api/v1/chat/send')
        .send({
          message: '',
        });

      expect(response.status).toBe(400);
      expect(response.body.success).toBe(false);
    });

    it('should reject invalid language', async () => {
      const response = await request(app)
        .post('/api/v1/chat/sessions')
        .send({
          customerId: 'test',
          language: 'invalid',
        });

      expect(response.status).toBe(400);
      expect(response.body.success).toBe(false);
    });
  });
});
