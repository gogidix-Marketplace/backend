/**
 * KPI Model Unit Tests
 * Comprehensive test coverage for KPI domain model
 */

const KPI = require('../models/KPI');
const { ObjectId } = require('mongodb');

describe('KPI Model', () => {
  describe('Constructor', () => {
    test('should create KPI with all required fields', () => {
      const data = {
        tenantId: 'tenant-001',
        name: 'Revenue Growth',
        category: 'FINANCIAL',
        executiveLevel: 'CEO',
        value: 150000,
        unit: '$',
        period: '2024-01',
        target: 200000,
      };

      const kpi = new KPI(data);

      expect(kpi.tenantId).toBe('tenant-001');
      expect(kpi.name).toBe('Revenue Growth');
      expect(kpi.category).toBe('FINANCIAL');
      expect(kpi.executiveLevel).toBe('CEO');
      expect(kpi.value).toBe(150000);
      expect(kpi.unit).toBe('$');
      expect(kpi.period).toBe('2024-01');
      expect(kpi.target).toBe(200000);
      expect(kpi.status).toBe('ON_TRACK');
      expect(kpi.visible).toBe(true);
      expect(kpi.isCalculated).toBe(true);
    });

    test('should use default values for optional fields', () => {
      const data = {
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'OPERATIONAL',
        value: 100,
        unit: 'count',
        period: 'Q1-2024',
      };

      const kpi = new KPI(data);

      expect(kpi.executiveLevel).toBe('ALL');
      expect(kpi.status).toBe('ON_TRACK');
      expect(kpi.dataSources).toEqual([]);
      expect(kpi.metadata).toEqual({});
      expect(kpi.visible).toBe(true);
      expect(kpi.isCalculated).toBe(true);
    });

    test('should generate ObjectId if not provided', () => {
      const data = {
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: '2024-01',
      };

      const kpi = new KPI(data);

      expect(kpi._id).toBeInstanceOf(ObjectId);
    });

    test('should use provided ObjectId', () => {
      const existingId = new ObjectId();
      const data = {
        _id: existingId,
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: '2024-01',
      };

      const kpi = new KPI(data);

      expect(kpi._id).toEqual(existingId);
    });

    test('should set createdAt and updatedAt timestamps', () => {
      const beforeCreate = new Date();
      const data = {
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: '2024-01',
      };

      const kpi = new KPI(data);
      const afterCreate = new Date();

      expect(kpi.createdAt).toBeInstanceOf(Date);
      expect(kpi.updatedAt).toBeInstanceOf(Date);
      expect(kpi.createdAt.getTime()).toBeGreaterThanOrEqual(beforeCreate.getTime());
      expect(kpi.createdAt.getTime()).toBeLessThanOrEqual(afterCreate.getTime());
    });
  });

  describe('toDocument', () => {
    test('should convert KPI to MongoDB document', () => {
      const data = {
        tenantId: 'tenant-001',
        name: 'Revenue Growth',
        category: 'FINANCIAL',
        executiveLevel: 'CEO',
        value: 150000,
        unit: '$',
        period: '2024-01',
        target: 200000,
        previousValue: 120000,
        dataSources: ['ERP', 'CRM'],
        metadata: { region: 'US' },
      };

      const kpi = new KPI(data);
      const doc = kpi.toDocument();

      expect(doc.tenantId).toBe('tenant-001');
      expect(doc.name).toBe('Revenue Growth');
      expect(doc.category).toBe('FINANCIAL');
      expect(doc.value).toBe(150000);
      expect(doc.target).toBe(200000);
      expect(doc.previousValue).toBe(120000);
      expect(doc.dataSources).toEqual(['ERP', 'CRM']);
      expect(doc.metadata).toEqual({ region: 'US' });
      expect(doc._id).toEqual(kpi._id);
    });
  });

  describe('fromDocument', () => {
    test('should create KPI from MongoDB document', () => {
      const doc = {
        _id: new ObjectId(),
        tenantId: 'tenant-001',
        name: 'Revenue Growth',
        category: 'FINANCIAL',
        executiveLevel: 'CFO',
        value: 150000,
        unit: '$',
        period: '2024-01',
        target: 200000,
        status: 'ON_TRACK',
        trend: 'UP',
        dataSources: ['ERP'],
        metadata: { source: 'system' },
        visible: true,
        isCalculated: true,
        lastCalculatedAt: new Date(),
        createdAt: new Date(),
        updatedAt: new Date(),
      };

      const kpi = KPI.fromDocument(doc);

      expect(kpi).toBeInstanceOf(KPI);
      expect(kpi._id).toEqual(doc._id);
      expect(kpi.tenantId).toBe('tenant-001');
      expect(kpi.name).toBe('Revenue Growth');
      expect(kpi.category).toBe('FINANCIAL');
      expect(kpi.executiveLevel).toBe('CFO');
      expect(kpi.value).toBe(150000);
    });

    test('should return null for null document', () => {
      const kpi = KPI.fromDocument(null);
      expect(kpi).toBeNull();
    });

    test('should handle missing optional fields in document', () => {
      const doc = {
        _id: new ObjectId(),
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: '2024-01',
      };

      const kpi = KPI.fromDocument(doc);

      expect(kpi.dataSources).toEqual([]);
      expect(kpi.metadata).toEqual({});
      expect(kpi.visible).toBe(true);
      expect(kpi.isCalculated).toBe(true);
    });
  });

  describe('calculatePercentChange', () => {
    test('should calculate percent change correctly with positive change', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Revenue',
        category: 'FINANCIAL',
        value: 150000,
        previousValue: 120000,
        unit: '$',
        period: '2024-01',
      });

      const result = kpi.calculatePercentChange();

      // (150000 - 120000) / 120000 * 100 = 25%
      expect(result).toBe(25);
      expect(kpi.percentChange).toBe(25);
    });

    test('should calculate percent change correctly with negative change', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Revenue',
        category: 'FINANCIAL',
        value: 100000,
        previousValue: 120000,
        unit: '$',
        period: '2024-01',
      });

      const result = kpi.calculatePercentChange();

      // (100000 - 120000) / 120000 * 100 = -16.67%
      expect(result).toBeCloseTo(-16.67, 2);
    });

    test('should return null when previousValue is null', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Revenue',
        category: 'FINANCIAL',
        value: 150000,
        unit: '$',
        period: '2024-01',
      });

      const result = kpi.calculatePercentChange();

      expect(result).toBeNull();
    });

    test('should return null when previousValue is zero', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Revenue',
        category: 'FINANCIAL',
        value: 150000,
        previousValue: 0,
        unit: '$',
        period: '2024-01',
      });

      const result = kpi.calculatePercentChange();

      expect(result).toBeNull();
    });
  });

  describe('updateStatus', () => {
    test('should set status to AHEAD when value >= 110% of target', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Revenue',
        category: 'FINANCIAL',
        value: 220000,
        target: 200000,
        unit: '$',
        period: '2024-01',
      });

      const status = kpi.updateStatus();

      expect(status).toBe('AHEAD');
      expect(kpi.status).toBe('AHEAD');
      expect(kpi.trend).toBe('UP');
    });

    test('should set status to ON_TRACK when value >= 90% of target', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Revenue',
        category: 'FINANCIAL',
        value: 190000,
        target: 200000,
        unit: '$',
        period: '2024-01',
      });

      const status = kpi.updateStatus();

      expect(status).toBe('ON_TRACK');
      expect(kpi.status).toBe('ON_TRACK');
      expect(kpi.trend).toBe('STABLE');
    });

    test('should set status to AT_RISK when value >= 80% of target', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Revenue',
        category: 'FINANCIAL',
        value: 170000,
        target: 200000,
        unit: '$',
        period: '2024-01',
      });

      const status = kpi.updateStatus();

      expect(status).toBe('AT_RISK');
      expect(kpi.status).toBe('AT_RISK');
      expect(kpi.trend).toBe('DOWN');
    });

    test('should set status to BEHIND when value < 80% of target', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Revenue',
        category: 'FINANCIAL',
        value: 150000,
        target: 200000,
        unit: '$',
        period: '2024-01',
      });

      const status = kpi.updateStatus();

      expect(status).toBe('BEHIND');
      expect(kpi.status).toBe('BEHIND');
      expect(kpi.trend).toBe('DOWN');
    });

    test('should not update status when target is null', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Revenue',
        category: 'FINANCIAL',
        value: 150000,
        unit: '$',
        period: '2024-01',
        status: 'ON_TRACK',
      });

      const status = kpi.updateStatus();

      expect(status).toBe('ON_TRACK');
      expect(kpi.trend).toBeUndefined();
    });

    test('should not update status when target is zero', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Revenue',
        category: 'FINANCIAL',
        value: 150000,
        target: 0,
        unit: '$',
        period: '2024-01',
        status: 'ON_TRACK',
      });

      const status = kpi.updateStatus();

      expect(status).toBe('ON_TRACK');
    });
  });

  describe('isOnTrack', () => {
    test('should return true for ON_TRACK status', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: '2024-01',
        status: 'ON_TRACK',
      });

      expect(kpi.isOnTrack()).toBe(true);
    });

    test('should return true for AHEAD status', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: '2024-01',
        status: 'AHEAD',
      });

      expect(kpi.isOnTrack()).toBe(true);
    });

    test('should return false for AT_RISK status', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: '2024-01',
        status: 'AT_RISK',
      });

      expect(kpi.isOnTrack()).toBe(false);
    });

    test('should return false for BEHIND status', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: '2024-01',
        status: 'BEHIND',
      });

      expect(kpi.isOnTrack()).toBe(false);
    });
  });

  describe('needsAttention', () => {
    test('should return true for AT_RISK status', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: '2024-01',
        status: 'AT_RISK',
      });

      expect(kpi.needsAttention()).toBe(true);
    });

    test('should return true for BEHIND status', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: '2024-01',
        status: 'BEHIND',
      });

      expect(kpi.needsAttention()).toBe(true);
    });

    test('should return false for ON_TRACK status', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: '2024-01',
        status: 'ON_TRACK',
      });

      expect(kpi.needsAttention()).toBe(false);
    });

    test('should return false for AHEAD status', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: '2024-01',
        status: 'AHEAD',
      });

      expect(kpi.needsAttention()).toBe(false);
    });
  });

  describe('addMetadata', () => {
    test('should add metadata key-value pair', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: '2024-01',
      });

      const result = kpi.addMetadata('region', 'US');

      expect(kpi.metadata.region).toBe('US');
      expect(result).toBe(kpi); // Method chaining
    });

    test('should update existing metadata key', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: '2024-01',
        metadata: { region: 'US' },
      });

      kpi.addMetadata('region', 'EU');

      expect(kpi.metadata.region).toBe('EU');
    });

    test('should support method chaining', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: '2024-01',
      });

      kpi.addMetadata('region', 'US')
         .addMetadata('source', 'ERP')
         .addMetadata('verified', true);

      expect(kpi.metadata.region).toBe('US');
      expect(kpi.metadata.source).toBe('ERP');
      expect(kpi.metadata.verified).toBe(true);
    });
  });

  describe('getCollectionName', () => {
    test('should return correct collection name', () => {
      const collectionName = KPI.getCollectionName();
      expect(collectionName).toBe('kpi_metrics');
    });
  });

  describe('getIndexes', () => {
    test('should return correct index specifications', () => {
      const indexes = KPI.getIndexes();

      expect(indexes).toHaveLength(4);
      expect(indexes[0]).toEqual({ key: { tenantId: 1, category: 1, period: -1 } });
      expect(indexes[1]).toEqual({ key: { tenantId: 1, executiveLevel: 1, category: 1, period: -1 } });
      expect(indexes[2]).toEqual({ key: { tenantId: 1, period: -1 } });
      expect(indexes[3]).toEqual({ key: { tenantId: 1, name: 1 } });
    });
  });

  describe('Edge Cases', () => {
    test('should handle negative values', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Net Loss',
        category: 'FINANCIAL',
        value: -50000,
        previousValue: -30000,
        unit: '$',
        period: '2024-01',
      });

      const percentChange = kpi.calculatePercentChange();
      // (-50000 - (-30000)) / -30000 * 100 = 66.67%
      expect(percentChange).toBeCloseTo(66.67, 2);
    });

    test('should handle zero value', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Zero KPI',
        category: 'FINANCIAL',
        value: 0,
        previousValue: 100,
        unit: '$',
        period: '2024-01',
      });

      const percentChange = kpi.calculatePercentChange();
      // (0 - 100) / 100 * 100 = -100%
      expect(percentChange).toBe(-100);
    });

    test('should handle very large values', () => {
      const largeValue = 999999999999;
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Large KPI',
        category: 'FINANCIAL',
        value: largeValue,
        unit: '$',
        period: '2024-01',
      });

      expect(kpi.value).toBe(largeValue);
    });

    test('should handle decimal values', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Percentage KPI',
        category: 'FINANCIAL',
        value: 12.5,
        previousValue: 10.0,
        unit: '%',
        period: '2024-01',
      });

      const percentChange = kpi.calculatePercentChange();
      // (12.5 - 10) / 10 * 100 = 25%
      expect(percentChange).toBe(25);
    });

    test('should handle special characters in name', () => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'KPI (Test) - FY2024',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: '2024-01',
      });

      expect(kpi.name).toBe('KPI (Test) - FY2024');
    });
  });

  describe('All Executive Levels', () => {
    const executiveLevels = ['CEO', 'CFO', 'CTO', 'COO', 'ALL'];

    test.each(executiveLevels)('should handle executive level: %s', (level) => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'FINANCIAL',
        executiveLevel: level,
        value: 100,
        unit: '$',
        period: '2024-01',
      });

      expect(kpi.executiveLevel).toBe(level);
    });
  });

  describe('All Categories', () => {
    const categories = ['FINANCIAL', 'OPERATIONAL', 'CUSTOMER', 'EMPLOYEE'];

    test.each(categories)('should handle category: %s', (category) => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: category,
        value: 100,
        unit: '$',
        period: '2024-01',
      });

      expect(kpi.category).toBe(category);
    });
  });

  describe('All Status Values', () => {
    const statuses = ['ON_TRACK', 'AT_RISK', 'BEHIND', 'AHEAD'];

    test.each(statuses)('should handle status: %s', (status) => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: '2024-01',
        status: status,
      });

      expect(kpi.status).toBe(status);
    });
  });

  describe('All Trend Values', () => {
    const trends = ['UP', 'DOWN', 'STABLE'];

    test.each(trends)('should handle trend: %s', (trend) => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: '2024-01',
        trend: trend,
      });

      expect(kpi.trend).toBe(trend);
    });
  });

  describe('All Period Formats', () => {
    const periods = ['2024-01', 'Q1-2024', 'YTD-2024', 'H1-2024'];

    test.each(periods)('should handle period: %s', (period) => {
      const kpi = new KPI({
        tenantId: 'tenant-001',
        name: 'Test KPI',
        category: 'FINANCIAL',
        value: 100,
        unit: '$',
        period: period,
      });

      expect(kpi.period).toBe(period);
    });
  });
});
