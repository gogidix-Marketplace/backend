/**
 * KPI Command Service Tests
 * Comprehensive test coverage for all KPI command operations
 */

const { MongoClient, ObjectId } = require('mongodb');
const KPI = require('../models/KPI');
const KPICommandService = require('../services/KPICommandService');

// Mock dependencies
jest.mock('../config/mongodb');
jest.mock('../config/redis');
jest.mock('../config/logger');

describe('KPICommandService', () => {
  let mockCollection;
  let mockDb;
  let mockPublishEvent;
  let mockInvalidatePattern;
  let tenantId;

  beforeEach(() => {
    tenantId = 'tenant-123';

    // Mock MongoDB collection
    mockCollection = {
      insertOne: jest.fn(),
      findOne: jest.fn(),
      updateOne: jest.fn(),
      deleteOne: jest.fn(),
    };

    mockDb = {
      collection: jest.fn(() => mockCollection),
    };

    // Mock Redis functions
    mockPublishEvent = jest.fn().mockResolvedValue(true);
    mockInvalidatePattern = jest.fn().mockResolvedValue(true);

    // Reset mocks
    jest.clearAllMocks();

    // Setup default mock returns
    const { getCollection } = require('../config/mongodb');
    getCollection.mockReturnValue(mockCollection);

    const { publishEvent, invalidatePattern } = require('../config/redis');
    publishEvent.mockImplementation(mockPublishEvent);
    invalidatePattern.mockImplementation(mockInvalidatePattern);
  });

  describe('createKPI', () => {
    const validKPIData = {
      name: 'Total Revenue',
      category: 'FINANCIAL',
      executiveLevel: 'CEO',
      value: 1000000,
      unit: '$',
      period: '2024-01',
      target: 1200000,
    };

    test('should create a new KPI', async () => {
      mockCollection.findOne.mockResolvedValue(null);
      mockCollection.insertOne.mockResolvedValue({
        insertedId: new ObjectId(),
      });

      const kpi = await KPICommandService.createKPI(tenantId, validKPIData);

      expect(kpi).toBeDefined();
      expect(kpi.name).toBe(validKPIData.name);
      expect(kpi.tenantId).toBe(tenantId);
      expect(mockCollection.insertOne).toHaveBeenCalled();
      expect(mockPublishEvent).toHaveBeenCalledWith('kpi:created', expect.objectContaining({
        name: 'Total Revenue',
      }));
    });

    test('should reject duplicate KPI', async () => {
      mockCollection.findOne.mockResolvedValue({ _id: new ObjectId() });

      await expect(KPICommandService.createKPI(tenantId, validKPIData))
        .rejects.toThrow('already exists');
    });

    test('should calculate percent change when previousValue provided', async () => {
      const dataWithPrevious = {
        ...validKPIData,
        previousValue: 1000000,
        value: 1100000,
      };

      mockCollection.findOne.mockResolvedValue(null);
      mockCollection.insertOne.mockResolvedValue({
        insertedId: new ObjectId(),
      });

      const kpi = await KPICommandService.createKPI(tenantId, dataWithPrevious);

      expect(kpi.percentChange).toBe(10);
    });

    test('should update status based on target - AHEAD', async () => {
      const dataAhead = {
        ...validKPIData,
        value: 1500000,
        target: 1000000,
      };

      mockCollection.findOne.mockResolvedValue(null);
      mockCollection.insertOne.mockResolvedValue({
        insertedId: new ObjectId(),
      });

      const kpi = await KPICommandService.createKPI(tenantId, dataAhead);

      expect(kpi.status).toBe('AHEAD');
      expect(kpi.trend).toBe('UP');
    });

    test('should update status based on target - ON_TRACK', async () => {
      mockCollection.findOne.mockResolvedValue(null);
      mockCollection.insertOne.mockResolvedValue({
        insertedId: new ObjectId(),
      });

      const kpi = await KPICommandService.createKPI(tenantId, validKPIData);

      expect(kpi.status).toBe('ON_TRACK');
    });

    test('should update status based on target - AT_RISK', async () => {
      const dataAtRisk = {
        ...validKPIData,
        value: 900000,
        target: 1000000,
      };

      mockCollection.findOne.mockResolvedValue(null);
      mockCollection.insertOne.mockResolvedValue({
        insertedId: new ObjectId(),
      });

      const kpi = await KPICommandService.createKPI(tenantId, dataAtRisk);

      expect(kpi.status).toBe('AT_RISK');
      expect(kpi.trend).toBe('DOWN');
    });

    test('should update status based on target - BEHIND', async () => {
      const dataBehind = {
        ...validKPIData,
        value: 700000,
        target: 1000000,
      };

      mockCollection.findOne.mockResolvedValue(null);
      mockCollection.insertOne.mockResolvedValue({
        insertedId: new ObjectId(),
      });

      const kpi = await KPICommandService.createKPI(tenantId, dataBehind);

      expect(kpi.status).toBe('BEHIND');
      expect(kpi.trend).toBe('DOWN');
    });

    test('should create KPI with all executive levels', async () => {
      const executiveLevels = ['CEO', 'CFO', 'CTO', 'COO', 'ALL'];

      for (const level of executiveLevels) {
        const data = { ...validKPIData, executiveLevel: level };
        mockCollection.findOne.mockResolvedValue(null);
        mockCollection.insertOne.mockResolvedValue({
          insertedId: new ObjectId(),
        });

        const kpi = await KPICommandService.createKPI(tenantId, data);

        expect(kpi.executiveLevel).toBe(level);
      }
    });

    test('should create KPI with all categories', async () => {
      const categories = ['FINANCIAL', 'OPERATIONAL', 'CUSTOMER', 'EMPLOYEE'];

      for (const category of categories) {
        const data = { ...validKPIData, category };
        mockCollection.findOne.mockResolvedValue(null);
        mockCollection.insertOne.mockResolvedValue({
          insertedId: new ObjectId(),
        });

        const kpi = await KPICommandService.createKPI(tenantId, data);

        expect(kpi.category).toBe(category);
      }
    });
  });

  describe('updateKPI', () => {
    const existingKPI = (kpiId) => ({
      _id: new ObjectId(kpiId),
      tenantId,
      name: 'Total Revenue',
      category: 'FINANCIAL',
      value: 1000000,
      unit: '$',
      period: '2024-01',
      target: 1200000,
      previousValue: 900000,
    });

    test('should update an existing KPI', async () => {
      const kpiId = new ObjectId().toString();
      const data = { value: 2000000 };

      mockCollection.findOne.mockResolvedValue(existingKPI(kpiId));
      mockCollection.updateOne.mockResolvedValue({ modifiedCount: 1 });

      const kpi = await KPICommandService.updateKPI(tenantId, kpiId, data);

      expect(kpi.value).toBe(2000000);
      expect(mockCollection.updateOne).toHaveBeenCalled();
      expect(mockInvalidatePattern).toHaveBeenCalledWith(`kpi:${tenantId}:*`);
    });

    test('should recalculate percent change when value is updated', async () => {
      const kpiId = new ObjectId().toString();

      mockCollection.findOne.mockResolvedValue(existingKPI(kpiId));
      mockCollection.updateOne.mockResolvedValue({ modifiedCount: 1 });

      const kpi = await KPICommandService.updateKPI(tenantId, kpiId, {
        value: 2000000,
      });

      // (2000000 - 900000) / 900000 * 100 = 122.22%
      expect(kpi.percentChange).toBeCloseTo(122.22, 2);
    });

    test('should update status when target is changed', async () => {
      const kpiId = new ObjectId().toString();

      mockCollection.findOne.mockResolvedValue(existingKPI(kpiId));
      mockCollection.updateOne.mockResolvedValue({ modifiedCount: 1 });

      const kpi = await KPICommandService.updateKPI(tenantId, kpiId, {
        target: 900000,
      });

      expect(kpi.status).toBe('AHEAD');
    });

    test('should update updatedAt timestamp', async () => {
      const kpiId = new ObjectId().toString();
      const oldUpdatedAt = new Date('2024-01-01');

      const existingDoc = {
        ...existingKPI(kpiId),
        updatedAt: oldUpdatedAt,
      };

      mockCollection.findOne.mockResolvedValue(existingDoc);
      mockCollection.updateOne.mockResolvedValue({ modifiedCount: 1 });

      const kpi = await KPICommandService.updateKPI(tenantId, kpiId, {
        value: 1500000,
      });

      expect(kpi.updatedAt.getTime()).toBeGreaterThan(oldUpdatedAt.getTime());
    });

    test('should throw error when KPI not found', async () => {
      const kpiId = new ObjectId().toString();
      mockCollection.findOne.mockResolvedValue(null);

      await expect(KPICommandService.updateKPI(tenantId, kpiId, { value: 2000000 }))
        .rejects.toThrow('not found');
    });

    test('should update multiple fields at once', async () => {
      const kpiId = new ObjectId().toString();

      mockCollection.findOne.mockResolvedValue(existingKPI(kpiId));
      mockCollection.updateOne.mockResolvedValue({ modifiedCount: 1 });

      const kpi = await KPICommandService.updateKPI(tenantId, kpiId, {
        value: 1500000,
        target: 1800000,
        unit: 'USD',
        status: 'AT_RISK',
      });

      expect(kpi.value).toBe(1500000);
      expect(kpi.target).toBe(1800000);
      expect(kpi.unit).toBe('USD');
    });
  });

  describe('deleteKPI', () => {
    test('should delete an existing KPI', async () => {
      const kpiId = new ObjectId().toString();

      mockCollection.deleteOne.mockResolvedValue({ deletedCount: 1 });

      const result = await KPICommandService.deleteKPI(tenantId, kpiId);

      expect(result.deleted).toBe(true);
      expect(mockCollection.deleteOne).toHaveBeenCalledWith({
        _id: new ObjectId(kpiId),
        tenantId,
      });
      expect(mockPublishEvent).toHaveBeenCalledWith('kpi:deleted', {
        kpiId,
        tenantId,
      });
    });

    test('should throw error when KPI not found', async () => {
      const kpiId = new ObjectId().toString();

      mockCollection.deleteOne.mockResolvedValue({ deletedCount: 0 });

      await expect(KPICommandService.deleteKPI(tenantId, kpiId))
        .rejects.toThrow('not found');
    });

    test('should invalidate cache after deletion', async () => {
      const kpiId = new ObjectId().toString();

      mockCollection.deleteOne.mockResolvedValue({ deletedCount: 1 });

      await KPICommandService.deleteKPI(tenantId, kpiId);

      expect(mockInvalidatePattern).toHaveBeenCalledWith(`kpi:${tenantId}:*`);
    });
  });

  describe('batchCreateKPIs', () => {
    test('should create multiple KPIs', async () => {
      const kpis = [
        { name: 'Revenue', category: 'FINANCIAL', value: 100, unit: '$', period: '2024-01' },
        { name: 'Users', category: 'CUSTOMER', value: 500, unit: 'count', period: '2024-01' },
        { name: 'NPS', category: 'CUSTOMER', value: 75, unit: 'score', period: '2024-01' },
      ];

      mockCollection.findOne.mockResolvedValue(null);
      mockCollection.insertOne
        .mockResolvedValueOnce({ insertedId: new ObjectId() })
        .mockResolvedValueOnce({ insertedId: new ObjectId() })
        .mockResolvedValueOnce({ insertedId: new ObjectId() });

      const result = await KPICommandService.batchCreateKPIs(tenantId, kpis);

      expect(result.created).toBe(3);
      expect(result.failed).toBe(0);
      expect(result.results).toHaveLength(3);
      expect(result.errors).toHaveLength(0);
    });

    test('should handle partial failures', async () => {
      const kpis = [
        { name: 'Revenue', category: 'FINANCIAL', value: 100, unit: '$', period: '2024-01' },
        { name: 'Revenue', category: 'FINANCIAL', value: 200, unit: '$', period: '2024-01' }, // Duplicate
        { name: 'Users', category: 'CUSTOMER', value: 500, unit: 'count', period: '2024-01' },
      ];

      mockCollection.findOne
        .mockResolvedValueOnce(null)
        .mockResolvedValueOnce({ _id: new ObjectId() }) // Duplicate
        .mockResolvedValueOnce(null);
      mockCollection.insertOne
        .mockResolvedValueOnce({ insertedId: new ObjectId() })
        .mockResolvedValueOnce({ insertedId: new ObjectId() });

      const result = await KPICommandService.batchCreateKPIs(tenantId, kpis);

      expect(result.created).toBe(2);
      expect(result.failed).toBe(1);
      expect(result.errors).toHaveLength(1);
      expect(result.errors[0].name).toBe('Revenue');
    });

    test('should handle all failures', async () => {
      const kpis = [
        { name: 'Revenue', category: 'FINANCIAL', value: 100, unit: '$', period: '2024-01' },
        { name: 'Users', category: 'CUSTOMER', value: 500, unit: 'count', period: '2024-01' },
      ];

      mockCollection.findOne.mockResolvedValue({ _id: new ObjectId() });

      const result = await KPICommandService.batchCreateKPIs(tenantId, kpis);

      expect(result.created).toBe(0);
      expect(result.failed).toBe(2);
      expect(result.results).toHaveLength(0);
      expect(result.errors).toHaveLength(2);
    });

    test('should handle empty batch', async () => {
      const result = await KPICommandService.batchCreateKPIs(tenantId, []);

      expect(result.created).toBe(0);
      expect(result.failed).toBe(0);
      expect(result.results).toHaveLength(0);
      expect(result.errors).toHaveLength(0);
    });
  });

  describe('recalculateKPI', () => {
    const existingKPI = (kpiId) => ({
      _id: new ObjectId(kpiId),
      tenantId,
      name: 'Total Revenue',
      category: 'FINANCIAL',
      value: 1000000,
      target: 1200000,
      previousValue: 900000,
      unit: '$',
      period: '2024-01',
    });

    test('should recalculate KPI with new value', async () => {
      const kpiId = new ObjectId().toString();
      const newValue = 1500000;
      const metadata = { source: 'external' };

      mockCollection.findOne.mockResolvedValue(existingKPI(kpiId));
      mockCollection.updateOne.mockResolvedValue({ modifiedCount: 1 });

      const kpi = await KPICommandService.recalculateKPI(tenantId, kpiId, newValue, metadata);

      expect(kpi.value).toBe(newValue);
      expect(kpi.previousValue).toBe(1000000);
      expect(kpi.status).toBe('AHEAD');
      expect(kpi.metadata.source).toBe('external');
    });

    test('should store current value as previous before updating', async () => {
      const kpiId = new ObjectId().toString();
      const newValue = 1500000;

      mockCollection.findOne.mockResolvedValue(existingKPI(kpiId));
      mockCollection.updateOne.mockResolvedValue({ modifiedCount: 1 });

      const kpi = await KPICommandService.recalculateKPI(tenantId, kpiId, newValue);

      expect(kpi.previousValue).toBe(1000000);
      expect(kpi.value).toBe(1500000);
    });

    test('should calculate new percent change', async () => {
      const kpiId = new ObjectId().toString();
      const newValue = 1800000;

      mockCollection.findOne.mockResolvedValue(existingKPI(kpiId));
      mockCollection.updateOne.mockResolvedValue({ modifiedCount: 1 });

      const kpi = await KPICommandService.recalculateKPI(tenantId, kpiId, newValue);

      // (1800000 - 1000000) / 1000000 * 100 = 80%
      expect(kpi.percentChange).toBe(80);
    });

    test('should update lastCalculatedAt timestamp', async () => {
      const kpiId = new ObjectId().toString();
      const beforeCalc = new Date();

      mockCollection.findOne.mockResolvedValue(existingKPI(kpiId));
      mockCollection.updateOne.mockResolvedValue({ modifiedCount: 1 });

      const kpi = await KPICommandService.recalculateKPI(tenantId, kpiId, 1500000);

      expect(kpi.lastCalculatedAt.getTime()).toBeGreaterThanOrEqual(beforeCalc.getTime());
    });

    test('should add multiple metadata fields', async () => {
      const kpiId = new ObjectId().toString();
      const metadata = {
        source: 'external',
        verified: true,
        region: 'US',
        priority: 'high',
      };

      mockCollection.findOne.mockResolvedValue(existingKPI(kpiId));
      mockCollection.updateOne.mockResolvedValue({ modifiedCount: 1 });

      const kpi = await KPICommandService.recalculateKPI(tenantId, kpiId, 1500000, metadata);

      expect(kpi.metadata.source).toBe('external');
      expect(kpi.metadata.verified).toBe(true);
      expect(kpi.metadata.region).toBe('US');
      expect(kpi.metadata.priority).toBe('high');
    });

    test('should throw error when KPI not found', async () => {
      const kpiId = new ObjectId().toString();

      mockCollection.findOne.mockResolvedValue(null);

      await expect(KPICommandService.recalculateKPI(tenantId, kpiId, 1500000))
        .rejects.toThrow('not found');
    });

    test('should handle empty metadata', async () => {
      const kpiId = new ObjectId().toString();

      mockCollection.findOne.mockResolvedValue(existingKPI(kpiId));
      mockCollection.updateOne.mockResolvedValue({ modifiedCount: 1 });

      const kpi = await KPICommandService.recalculateKPI(tenantId, kpiId, 1500000);

      expect(kpi.metadata).toEqual({});
    });
  });

  describe('Edge Cases and Error Handling', () => {
    test('should handle negative values in createKPI', async () => {
      const data = {
        name: 'Net Loss',
        category: 'FINANCIAL',
        value: -50000,
        previousValue: -30000,
        unit: '$',
        period: '2024-01',
      };

      mockCollection.findOne.mockResolvedValue(null);
      mockCollection.insertOne.mockResolvedValue({
        insertedId: new ObjectId(),
      });

      const kpi = await KPICommandService.createKPI(tenantId, data);

      expect(kpi.value).toBe(-50000);
      expect(kpi.percentChange).toBeCloseTo(66.67, 2);
    });

    test('should handle zero values', async () => {
      const data = {
        name: 'Zero KPI',
        category: 'FINANCIAL',
        value: 0,
        unit: '$',
        period: '2024-01',
      };

      mockCollection.findOne.mockResolvedValue(null);
      mockCollection.insertOne.mockResolvedValue({
        insertedId: new ObjectId(),
      });

      const kpi = await KPICommandService.createKPI(tenantId, data);

      expect(kpi.value).toBe(0);
    });

    test('should handle very large values', async () => {
      const data = {
        name: 'Large Revenue',
        category: 'FINANCIAL',
        value: 999999999999,
        unit: '$',
        period: '2024-01',
      };

      mockCollection.findOne.mockResolvedValue(null);
      mockCollection.insertOne.mockResolvedValue({
        insertedId: new ObjectId(),
      });

      const kpi = await KPICommandService.createKPI(tenantId, data);

      expect(kpi.value).toBe(999999999999);
    });

    test('should handle database connection errors', async () => {
      const data = {
        name: 'Test KPI',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: '2024-01',
      };

      mockCollection.findOne.mockRejectedValue(new Error('Connection lost'));

      await expect(KPICommandService.createKPI(tenantId, data))
        .rejects.toThrow('Connection lost');
    });
  });
});
