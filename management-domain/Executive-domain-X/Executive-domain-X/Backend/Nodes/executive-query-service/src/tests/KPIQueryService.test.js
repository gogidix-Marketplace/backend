/**
 * KPI Query Service Tests
 */

const { ObjectId } = require('mongodb');
const KPIQueryService = require('../services/KPIQueryService');

// Mock dependencies
jest.mock('../config/mongodb');
jest.mock('../config/redis');

const { getCollection } = require('../config/mongodb');
const { get, set, del, kpiKey, kpiListKey } = require('../config/redis');

describe('KPIQueryService', () => {
  let mockCollection;

  beforeEach(() => {
    // Mock MongoDB collection
    mockCollection = {
      findOne: jest.fn(),
      find: jest.fn(),
      aggregate: jest.fn(),
      countDocuments: jest.fn(),
    };

    getCollection.mockReturnValue(mockCollection);

    // Reset all mocks
    jest.clearAllMocks();

    // Mock cache miss by default
    get.mockResolvedValue(null);
    set.mockResolvedValue(true);
    del.mockResolvedValue(true);
  });

  describe('getKPIById', () => {
    test('should return KPI from cache if available', async () => {
      const tenantId = 'tenant-123';
      const kpiId = new ObjectId().toString();
      const cachedKPI = { _id: kpiId, name: 'Revenue', value: 1000 };

      get.mockResolvedValue(cachedKPI);

      const result = await KPIQueryService.getKPIById(tenantId, kpiId);

      expect(result).toEqual(cachedKPI);
      expect(get).toHaveBeenCalled();
      expect(mockCollection.findOne).not.toHaveBeenCalled();
    });

    test('should fetch KPI from database on cache miss', async () => {
      const tenantId = 'tenant-123';
      const kpiId = new ObjectId().toString();
      const dbKPI = { _id: new ObjectId(kpiId), name: 'Revenue', value: 1000, tenantId };

      get.mockResolvedValue(null);
      mockCollection.findOne.mockResolvedValue(dbKPI);

      const result = await KPIQueryService.getKPIById(tenantId, kpiId);

      expect(result).toEqual(dbKPI);
      expect(mockCollection.findOne).toHaveBeenCalledWith({
        _id: new ObjectId(kpiId),
        tenantId,
      });
      expect(set).toHaveBeenCalled();
    });

    test('should throw error when KPI not found', async () => {
      const tenantId = 'tenant-123';
      const kpiId = new ObjectId().toString();

      get.mockResolvedValue(null);
      mockCollection.findOne.mockResolvedValue(null);

      await expect(KPIQueryService.getKPIById(tenantId, kpiId))
        .rejects.toThrow('KPI not found');
    });
  });

  describe('getKPIs', () => {
    test('should return paginated KPIs', async () => {
      const tenantId = 'tenant-123';
      const options = { page: 1, limit: 20 };

      const mockKPIs = [
        { _id: new ObjectId(), name: 'Revenue', tenantId },
        { _id: new ObjectId(), name: 'Users', tenantId },
      ];

      const mockFind = {
        sort: jest.fn().mockReturnThis(),
        skip: jest.fn().mockReturnThis(),
        limit: jest.fn().mockResolvedValue(mockKPIs),
      };

      mockCollection.find.mockReturnValue(mockFind);
      mockCollection.countDocuments.mockResolvedValue(2);

      const result = await KPIQueryService.getKPIs(tenantId, options);

      expect(result.data).toEqual(mockKPIs);
      expect(result.pagination.total).toBe(2);
      expect(result.pagination.page).toBe(1);
    });

    test('should filter by category', async () => {
      const tenantId = 'tenant-123';
      const options = { category: 'FINANCIAL' };

      const mockFind = {
        sort: jest.fn().mockReturnThis(),
        skip: jest.fn().mockReturnThis(),
        limit: jest.fn().mockResolvedValue([]),
      };

      mockCollection.find.mockReturnValue(mockFind);
      mockCollection.countDocuments.mockResolvedValue(0);

      await KPIQueryService.getKPIs(tenantId, options);

      expect(mockCollection.find).toHaveBeenCalledWith({
        tenantId,
        category: 'FINANCIAL',
      });
    });

    test('should filter by executive level', async () => {
      const tenantId = 'tenant-123';
      const options = { executiveLevel: 'CFO' };

      const mockFind = {
        sort: jest.fn().mockReturnThis(),
        skip: jest.fn().mockReturnThis(),
        limit: jest.fn().mockResolvedValue([]),
      };

      mockCollection.find.mockReturnValue(mockFind);
      mockCollection.countDocuments.mockResolvedValue(0);

      await KPIQueryService.getKPIs(tenantId, options);

      expect(mockCollection.find).toHaveBeenCalledWith({
        tenantId,
        executiveLevel: { $in: ['CFO', 'ALL'] },
      });
    });

    test('should filter by status', async () => {
      const tenantId = 'tenant-123';
      const options = { status: 'ON_TRACK' };

      const mockFind = {
        sort: jest.fn().mockReturnThis(),
        skip: jest.fn().mockReturnThis(),
        limit: jest.fn().mockResolvedValue([]),
      };

      mockCollection.find.mockReturnValue(mockFind);
      mockCollection.countDocuments.mockResolvedValue(0);

      await KPIQueryService.getKPIs(tenantId, options);

      expect(mockCollection.find).toHaveBeenCalledWith({
        tenantId,
        status: 'ON_TRACK',
      });
    });
  });

  describe('getKPISummary', () => {
    test('should return summary by category', async () => {
      const tenantId = 'tenant-123';

      const mockAggregate = {
        toArray: jest.fn().mockResolvedValue([
          {
            _id: 'FINANCIAL',
            total: 10,
            ahead: 2,
            onTrack: 5,
            atRisk: 2,
            behind: 1,
          },
        ]),
      };

      mockCollection.aggregate.mockReturnValue(mockAggregate);

      const result = await KPIQueryService.getKPISummary(tenantId);

      expect(result.FINANCIAL).toEqual({
        total: 10,
        ahead: 2,
        onTrack: 5,
        atRisk: 2,
        behind: 1,
      });
    });

    test('should return cached summary', async () => {
      const tenantId = 'tenant-123';
      const cached = { FINANCIAL: { total: 10 } };

      get.mockResolvedValue(cached);

      const result = await KPIQueryService.getKPISummary(tenantId);

      expect(result).toEqual(cached);
      expect(mockCollection.aggregate).not.toHaveBeenCalled();
    });
  });

  describe('getDashboardKPIs', () => {
    test('should return all KPIs for CEO', async () => {
      const tenantId = 'tenant-123';
      const level = 'CEO';

      const mockKPIs = [
        { _id: new ObjectId(), name: 'Revenue', tenantId, status: 'ON_TRACK' },
      ];

      mockCollection.find.mockResolvedValue(mockKPIs);

      const result = await KPIQueryService.getDashboardKPIs(tenantId, level);

      expect(result.kpi).toEqual(mockKPIs);
      expect(result.summary).toBeDefined();
      expect(mockCollection.find).toHaveBeenCalledWith({ tenantId });
    });

    test('should filter KPIs for non-CEO executives', async () => {
      const tenantId = 'tenant-123';
      const level = 'CFO';

      const mockKPIs = [];
      mockCollection.find.mockResolvedValue(mockKPIs);

      await KPIQueryService.getDashboardKPIs(tenantId, level);

      expect(mockCollection.find).toHaveBeenCalledWith({
        $or: [
          { tenantId, executiveLevel: 'CFO' },
          { tenantId, executiveLevel: 'ALL' },
        ],
      });
    });

    test('should calculate summary correctly', async () => {
      const tenantId = 'tenant-123';
      const level = 'CEO';

      const mockKPIs = [
        { _id: new ObjectId(), tenantId, status: 'AHEAD' },
        { _id: new ObjectId(), tenantId, status: 'ON_TRACK' },
        { _id: new ObjectId(), tenantId, status: 'ON_TRACK' },
        { _id: new ObjectId(), tenantId, status: 'AT_RISK' },
        { _id: new ObjectId(), tenantId, status: 'BEHIND' },
      ];

      mockCollection.find.mockResolvedValue(mockKPIs);

      const result = await KPIQueryService.getDashboardKPIs(tenantId, level);

      expect(result.summary).toEqual({
        total: 5,
        ahead: 1,
        onTrack: 2,
        atRisk: 1,
        behind: 1,
      });
    });
  });

  describe('getKPITrends', () => {
    test('should return trends with historical data', async () => {
      const tenantId = 'tenant-123';
      const kpiId = new ObjectId().toString();

      const currentKPI = {
        _id: new ObjectId(kpiId),
        tenantId,
        name: 'Revenue',
        value: 1000,
        period: '2024-01',
      };

      const historicalKPIs = [
        { _id: new ObjectId(), name: 'Revenue', value: 900, period: '2023-12' },
        { _id: new ObjectId(), name: 'Revenue', value: 800, period: '2023-11' },
      ];

      mockCollection.findOne.mockResolvedValue(currentKPI);
      mockCollection.find.mockResolvedValue(historicalKPIs);

      const result = await KPIQueryService.getKPITrends(tenantId, kpiId, 12);

      expect(result.current).toEqual(currentKPI);
      expect(result.historical).toBeDefined();
      expect(result.trend).toBeDefined();
    });

    test('should throw error when KPI not found', async () => {
      const tenantId = 'tenant-123';
      const kpiId = new ObjectId().toString();

      mockCollection.findOne.mockResolvedValue(null);

      await expect(KPIQueryService.getKPITrends(tenantId, kpiId))
        .rejects.toThrow('KPI not found');
    });
  });

  describe('searchKPIs', () => {
    test('should search KPIs by name', async () => {
      const tenantId = 'tenant-123';
      const searchTerm = 'Revenue';

      const mockKPIs = [
        { _id: new ObjectId(), name: 'Total Revenue', tenantId },
        { _id: new ObjectId(), name: 'Revenue Growth', tenantId },
      ];

      mockCollection.find.mockReturnValue({
        limit: jest.fn().mockResolvedValue(mockKPIs),
      });

      const result = await KPIQueryService.searchKPIs(tenantId, searchTerm);

      expect(result).toEqual(mockKPIs);
      expect(mockCollection.find).toHaveBeenCalledWith({
        tenantId,
        name: { $regex: 'Revenue', $options: 'i' },
      });
    });
  });

  describe('invalidateKPI', () => {
    test('should invalidate KPI cache', async () => {
      const tenantId = 'tenant-123';
      const kpiId = new ObjectId().toString();

      await KPIQueryService.invalidateKPI(tenantId, kpiId);

      expect(del).toHaveBeenCalled();
    });
  });

  describe('invalidateAll', () => {
    test('should invalidate all tenant cache', async () => {
      const tenantId = 'tenant-123';

      await KPIQueryService.invalidateAll(tenantId);

      expect(del).toHaveBeenCalled();
    });
  });
});
