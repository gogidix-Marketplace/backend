/**
 * Tests for centralized-reporting service
 * Tests cover all endpoints: health, sales report, inventory report, performance report
 */

const request = require('supertest');
const express = require('express');
const fetch = require('node-fetch');

// Mock node-fetch
jest.mock('node-fetch');
const { Response } = jest.requireActual('node-fetch');

// Import the app after mocking
let app;

describe('Centralized Reporting Service', () => {
  beforeEach(() => {
    // Clear all mocks before each test
    jest.clearAllMocks();

    // Import fresh app instance for each test
    jest.isolateModules(() => {
      app = require('../src/index');
    });
  });

  afterEach(() => {
    // Close the server if it's open
    if (app && app._server) {
      app._server.close();
    }
  });

  describe('GET /health', () => {
    it('should return health status UP', async () => {
      const response = await request(app).get('/health');

      expect(response.status).toBe(200);
      expect(response.body).toEqual({
        status: 'UP',
        service: 'centralized-reporting'
      });
    });

    it('should return JSON content type', async () => {
      const response = await request(app).get('/health');

      expect(response.headers['content-type']).toContain('application/json');
    });
  });

  describe('GET /reports/sales', () => {
    const mockSalesData = {
      daily: {
        '2024-01-01': 1500.50,
        '2024-01-02': 2300.75,
        '2024-01-03': 1800.25
      }
    };

    beforeEach(() => {
      fetch.mockResolvedValue(
        new Response(JSON.stringify(mockSalesData), {
          status: 200,
          headers: { 'content-type': 'application/json' }
        })
      );
    });

    it('should generate CSV sales report', async () => {
      const response = await request(app).get('/reports/sales');

      expect(response.status).toBe(200);
      expect(response.headers['content-type']).toBe('text/csv; charset=utf-8');
      expect(response.headers['content-disposition']).toContain('sales_report.csv');
    });

    it('should filter sales by date range', async () => {
      const response = await request(app).get('/reports/sales?from=2024-01-02&to=2024-01-03');

      expect(response.status).toBe(200);
      const csvData = response.text;

      // Should only include dates in range
      expect(csvData).toContain('2024-01-02');
      expect(csvData).toContain('2024-01-03');
      expect(csvData).not.toContain('2024-01-01');
    });

    it('should include correct CSV headers', async () => {
      const response = await request(app).get('/reports/sales');

      const csvData = response.text;
      const lines = csvData.split('\n');

      expect(lines[0]).toBe('date,amount');
    });

    it('should handle empty sales data', async () => {
      fetch.mockResolvedValue(
        new Response(JSON.stringify({ daily: {} }), {
          status: 200,
          headers: { 'content-type': 'application/json' }
        })
      );

      const response = await request(app).get('/reports/sales');

      expect(response.status).toBe(200);
      const csvData = response.text;
      expect(csvData).toContain('date,amount');
    });

    it('should handle upstream service errors', async () => {
      fetch.mockRejectedValue(new Error('Service unavailable'));

      const response = await request(app).get('/reports/sales');

      expect(response.status).toBe(500);
    });
  });

  describe('GET /reports/inventory', () => {
    const mockInventoryData = {
      byProduct: {
        'PROD-001': 150,
        'PROD-002': 75,
        'PROD-003': 200
      }
    };

    beforeEach(() => {
      fetch.mockResolvedValue(
        new Response(JSON.stringify(mockInventoryData), {
          status: 200,
          headers: { 'content-type': 'application/json' }
        })
      );
    });

    it('should generate CSV inventory report', async () => {
      const response = await request(app).get('/reports/inventory');

      expect(response.status).toBe(200);
      expect(response.headers['content-type']).toBe('text/csv; charset=utf-8');
      expect(response.headers['content-disposition']).toContain('inventory_report.csv');
    });

    it('should include correct CSV headers for inventory', async () => {
      const response = await request(app).get('/reports/inventory');

      const csvData = response.text;
      const lines = csvData.split('\n');

      expect(lines[0]).toBe('productId,quantity');
    });

    it('should include all products in report', async () => {
      const response = await request(app).get('/reports/inventory');

      const csvData = response.text;

      expect(csvData).toContain('PROD-001');
      expect(csvData).toContain('PROD-002');
      expect(csvData).toContain('PROD-003');
      expect(csvData).toContain('150');
      expect(csvData).toContain('75');
      expect(csvData).toContain('200');
    });

    it('should handle empty inventory data', async () => {
      fetch.mockResolvedValue(
        new Response(JSON.stringify({ byProduct: {} }), {
          status: 200,
          headers: { 'content-type': 'application/json' }
        })
      );

      const response = await request(app).get('/reports/inventory');

      expect(response.status).toBe(200);
      const csvData = response.text;
      expect(csvData).toContain('productId,quantity');
    });
  });

  describe('GET /reports/performance', () => {
    const mockPerformanceData = {
      byService: {
        'api-gateway': {
          avgLatency: 45.2,
          avgErrorRate: 0.01,
          avgThroughput: 1250
        },
        'analytics-service': {
          avgLatency: 120.5,
          avgErrorRate: 0.05,
          avgThroughput: 450
        }
      }
    };

    beforeEach(() => {
      fetch.mockResolvedValue(
        new Response(JSON.stringify(mockPerformanceData), {
          status: 200,
          headers: { 'content-type': 'application/json' }
        })
      );
    });

    it('should generate CSV performance report', async () => {
      const response = await request(app).get('/reports/performance');

      expect(response.status).toBe(200);
      expect(response.headers['content-type']).toBe('text/csv; charset=utf-8');
      expect(response.headers['content-disposition']).toContain('performance_report.csv');
    });

    it('should include correct CSV headers for performance', async () => {
      const response = await request(app).get('/reports/performance');

      const csvData = response.text;
      const lines = csvData.split('\n');

      expect(lines[0]).toBe('service,avgLatency,avgErrorRate,avgThroughput');
    });

    it('should include all services and metrics', async () => {
      const response = await request(app).get('/reports/performance');

      const csvData = response.text;

      expect(csvData).toContain('api-gateway');
      expect(csvData).toContain('analytics-service');
      expect(csvData).toContain('45.2');
      expect(csvData).toContain('120.5');
    });

    it('should handle empty performance data', async () => {
      fetch.mockResolvedValue(
        new Response(JSON.stringify({ byService: {} }), {
          status: 200,
          headers: { 'content-type': 'application/json' }
        })
      );

      const response = await request(app).get('/reports/performance');

      expect(response.status).toBe(200);
      const csvData = response.text;
      expect(csvData).toContain('service,avgLatency,avgErrorRate,avgThroughput');
    });
  });

  describe('Error Handling', () => {
    it('should return 404 for non-existent routes', async () => {
      const response = await request(app).get('/reports/nonexistent');

      expect(response.status).toBe(404);
    });

    it('should handle malformed JSON from upstream', async () => {
      fetch.mockResolvedValue(
        new Response('invalid json', {
          status: 200,
          headers: { 'content-type': 'application/json' }
        })
      );

      const response = await request(app).get('/reports/sales');

      expect(response.status).toBe(500);
    });

    it('should handle upstream timeout', async () => {
      fetch.mockRejectedValue(new Error('ETIMEDOUT'));

      const response = await request(app).get('/reports/inventory');

      expect(response.status).toBe(500);
    });
  });

  describe('CSV Generation Edge Cases', () => {
    it('should handle special characters in product IDs', async () => {
      const mockData = {
        byProduct: {
          'PROD-001,With,Commas': 100,
          'PROD-002"WithQuotes"': 200
        }
      };

      fetch.mockResolvedValue(
        new Response(JSON.stringify(mockData), {
          status: 200,
          headers: { 'content-type': 'application/json' }
        })
      );

      const response = await request(app).get('/reports/inventory');

      expect(response.status).toBe(200);
      const csvData = response.text;

      // CSV parser should quote fields with special characters
      expect(csvData).toContain('"');
    });

    it('should handle large datasets efficiently', async () => {
      const largeData = { daily: {} };
      for (let i = 0; i < 10000; i++) {
        largeData.daily[`2024-01-${String(i % 28 + 1).padStart(2, '0')}`] = Math.random() * 1000;
      }

      fetch.mockResolvedValue(
        new Response(JSON.stringify(largeData), {
          status: 200,
          headers: { 'content-type': 'application/json' }
        })
      );

      const startTime = Date.now();
      const response = await request(app).get('/reports/sales');
      const duration = Date.now() - startTime;

      expect(response.status).toBe(200);
      expect(duration).toBeLessThan(5000); // Should complete in under 5 seconds
    });
  });

  describe('Content Type and Headers', () => {
    it('should set correct attachment filename', async () => {
      fetch.mockResolvedValue(
        new Response(JSON.stringify({ daily: {} }), {
          status: 200,
          headers: { 'content-type': 'application/json' }
        })
      );

      const response = await request(app).get('/reports/sales');

      expect(response.headers['content-disposition']).toMatch(/attachment/);
      expect(response.headers['content-disposition']).toMatch(/sales_report\.csv/);
    });
  });
});
