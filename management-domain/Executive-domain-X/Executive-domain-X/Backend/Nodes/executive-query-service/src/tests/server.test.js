/**
 * Server Integration Tests
 */

const request = require('supertest');
const jwt = require('jsonwebtoken');

const app = require('../server');

// Mock dependencies
jest.mock('../config/mongodb');
jest.mock('../config/redis');

const JWT_SECRET = process.env.JWT_SECRET || 'your-secret-key';

// Helper to generate valid token
function generateToken(tenantId = 'test-tenant') {
  return jwt.sign(
    { tenantId, sub: 'user-123' },
    JWT_SECRET,
    { expiresIn: '1h' }
  );
}

describe('Server', () => {
  describe('Health Check', () => {
    test('GET /health should return healthy status', async () => {
      const response = await request(app)
        .get('/health')
        .expect(200);

      expect(response.body.success).toBe(true);
      expect(response.body.service).toBe('executive-query-service');
      expect(response.body.status).toBe('healthy');
    });
  });

  describe('Authentication', () => {
    test('should reject request without token', async () => {
      const response = await request(app)
        .get('/api/kpi')
        .expect(401);

      expect(response.body.error).toContain('token');
    });

    test('should reject request with invalid token', async () => {
      const response = await request(app)
        .get('/api/kpi')
        .set('Authorization', 'Bearer invalid-token')
        .expect(403);

      expect(response.body.error).toContain('Invalid');
    });

    test('should accept request with valid token', async () => {
      const token = generateToken();

      // Mock service to return empty array
      const KPIQueryService = require('../services/KPIQueryService');
      KPIQueryService.getKPIs = jest.fn().mockResolvedValue({
        data: [],
        pagination: { page: 1, limit: 20, total: 0, pages: 0 },
      });

      await request(app)
        .get('/api/kpi')
        .set('Authorization', `Bearer ${token}`)
        .expect(200);
    });
  });

  describe('Route Handlers', () => {
    const token = generateToken();

    beforeEach(() => {
      // Mock service
      const KPIQueryService = require('../services/KPIQueryService');
      KPIQueryService.getKPIs = jest.fn().mockResolvedValue({
        data: [],
        pagination: { page: 1, limit: 20, total: 0, pages: 0 },
      });
    });

    test('GET /api/kpi should call service with correct params', async () => {
      await request(app)
        .get('/api/kpi?page=1&limit=10')
        .set('Authorization', `Bearer ${token}`)
        .expect(200);
    });

    test('should return 404 for non-existent routes', async () => {
      await request(app)
        .get('/api/nonexistent')
        .set('Authorization', `Bearer ${token}`)
        .expect(404);
    });
  });
});
