import { Test, TestingModule } from '@nestjs/testing';
import { getModelToken } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { InventoryService } from './inventory.service';
import { InventoryItem, StockMovement } from '../domain/entities/inventory.entity';
import { AdjustmentType, MovementType } from './dto/inventory.dto';
import { NotFoundException, BadRequestException, ConflictException } from '@nestjs/common';

describe('InventoryService', () => {
  let service: InventoryService;
  let inventoryModel: Model<InventoryItem>;
  let movementModel: Model<StockMovement>;

  const mockInventoryDoc = (overrides = {}) => ({
    _id: { toString: () => 'item-123' },
    tenantId: 'tenant-1',
    warehouseId: 'warehouse-1',
    sku: 'SKU-001',
    name: 'Test Product',
    quantity: 100,
    reservedQuantity: 10,
    reorderPoint: 20,
    reorderQuantity: 50,
    unitCost: 25.99,
    createdAt: new Date('2024-01-01'),
    updatedAt: new Date('2024-01-02'),
    save: jest.fn().mockResolvedValue(this),
    ...overrides,
  });

  const mockMovementDoc = (overrides = {}) => ({
    _id: { toString: () => 'movement-123' },
    tenantId: 'tenant-1',
    itemId: 'item-123',
    sku: 'SKU-001',
    warehouseId: 'warehouse-1',
    movementType: 'adjusted',
    quantity: 10,
    previousQuantity: 90,
    newQuantity: 100,
    reference: 'REF-001',
    reason: 'Test',
    createdAt: new Date('2024-01-01'),
    ...overrides,
  });

  const MockMovementModel = jest.fn().mockImplementation((data) => {
    const instance = {
      ...data,
      _id: { toString: () => 'movement-123' },
      save: jest.fn().mockResolvedValue({
        ...data,
        _id: { toString: () => 'movement-123' },
        createdAt: new Date('2024-01-01'),
      }),
    };
    MockMovementModel.lastInstance = instance;
    return instance;
  });

  beforeEach(async () => {
    jest.clearAllMocks();

    const module: TestingModule = await Test.createTestingModule({
      providers: [
        InventoryService,
        {
          provide: getModelToken(InventoryItem.name),
          useValue: {
            find: jest.fn(),
            findOne: jest.fn(),
            countDocuments: jest.fn(),
          },
        },
        {
          provide: getModelToken(StockMovement.name),
          useValue: MockMovementModel,
        },
      ],
    }).compile();

    service = module.get<InventoryService>(InventoryService);
    inventoryModel = module.get<Model<InventoryItem>>(getModelToken(InventoryItem.name));
    movementModel = module.get<Model<StockMovement>>(getModelToken(StockMovement.name));
  });

  describe('getInventoryItem', () => {
    it('should return item when found', async () => {
      const mockItem = mockInventoryDoc();
      jest.spyOn(inventoryModel, 'findOne').mockResolvedValue(mockItem as any);

      const result = await service.getInventoryItem('tenant-1', 'item-123');

      expect(result.id).toBe('item-123');
      expect(result.sku).toBe('SKU-001');
      expect(result.availableQuantity).toBe(90);
    });

    it('should throw NotFoundException when item not found', async () => {
      jest.spyOn(inventoryModel, 'findOne').mockResolvedValue(null);

      await expect(service.getInventoryItem('tenant-1', 'nonexistent')).rejects.toThrow(NotFoundException);
    });
  });

  describe('getInventoryItemBySku', () => {
    it('should return item by SKU', async () => {
      const mockItem = mockInventoryDoc();
      jest.spyOn(inventoryModel, 'findOne').mockResolvedValue(mockItem as any);

      const result = await service.getInventoryItemBySku('tenant-1', 'SKU-001', 'warehouse-1');

      expect(result.sku).toBe('SKU-001');
    });

    it('should throw NotFoundException for non-existent SKU', async () => {
      jest.spyOn(inventoryModel, 'findOne').mockResolvedValue(null);

      await expect(service.getInventoryItemBySku('tenant-1', 'INVALID', 'warehouse-1')).rejects.toThrow(NotFoundException);
    });
  });

  describe('queryInventoryItems', () => {
    it('should return paginated results', async () => {
      const mockItems = [mockInventoryDoc(), mockInventoryDoc({ _id: { toString: () => 'item-456' } })];
      const chainMock: any = {
        skip: jest.fn().mockReturnThis(),
        limit: jest.fn().mockReturnThis(),
        sort: jest.fn().mockReturnThis(),
      };
      chainMock.then = (resolve: any) => resolve(mockItems);
      jest.spyOn(inventoryModel, 'find').mockReturnValue(chainMock);
      jest.spyOn(inventoryModel, 'countDocuments').mockResolvedValue(2);

      const result = await service.queryInventoryItems({
        tenantId: 'tenant-1',
        page: 1,
        limit: 20,
      });

      expect(result.data).toHaveLength(2);
      expect(result.total).toBe(2);
    });
  });

  describe('adjustStock', () => {
    it('should increase stock', async () => {
      const mockItem = mockInventoryDoc({ quantity: 100, reservedQuantity: 10 });
      mockItem.save = jest.fn().mockResolvedValue(mockItem);
      jest.spyOn(inventoryModel, 'findOne').mockResolvedValue(mockItem as any);

      const command = {
        tenantId: 'tenant-1',
        itemId: 'item-123',
        adjustmentType: AdjustmentType.INCREASE,
        quantity: 50,
        reason: 'Stock replenishment',
      };

      const result = await service.adjustStock(command);

      expect(mockItem.quantity).toBe(150);
    });

    it('should decrease stock', async () => {
      const mockItem = mockInventoryDoc({ quantity: 100, reservedQuantity: 10 });
      mockItem.save = jest.fn().mockResolvedValue(mockItem);
      jest.spyOn(inventoryModel, 'findOne').mockResolvedValue(mockItem as any);

      const command = {
        tenantId: 'tenant-1',
        itemId: 'item-123',
        adjustmentType: AdjustmentType.DECREASE,
        quantity: 30,
        reason: 'Damaged stock',
      };

      await service.adjustStock(command);

      expect(mockItem.quantity).toBe(70);
    });

    it('should throw BadRequestException when insufficient stock', async () => {
      const mockItem = mockInventoryDoc({ quantity: 50, reservedQuantity: 45 });
      jest.spyOn(inventoryModel, 'findOne').mockResolvedValue(mockItem as any);

      const command = {
        tenantId: 'tenant-1',
        itemId: 'item-123',
        adjustmentType: AdjustmentType.DECREASE,
        quantity: 10,
        reason: 'Test',
      };

      await expect(service.adjustStock(command)).rejects.toThrow(BadRequestException);
    });
  });

  describe('getLowStockItems', () => {
    it('should return items below reorder point', async () => {
      const lowStockItems = [
        mockInventoryDoc({ quantity: 15, reorderPoint: 20 }),
        mockInventoryDoc({ _id: { toString: () => 'item-456' }, quantity: 5, reorderPoint: 10 }),
      ];
      jest.spyOn(inventoryModel, 'find').mockResolvedValue(lowStockItems as any);

      const result = await service.getLowStockItems('tenant-1', 'warehouse-1');

      expect(result.length).toBe(2);
    });
  });

  describe('getReservedStock', () => {
    it('should return items with reservations', async () => {
      const reservedItems = [mockInventoryDoc({ reservedQuantity: 25 })];
      jest.spyOn(inventoryModel, 'find').mockResolvedValue(reservedItems as any);

      const result = await service.getReservedStock('tenant-1', 'warehouse-1');

      expect(result.length).toBe(1);
      expect(result[0].reservedQuantity).toBe(25);
    });
  });

  describe('reserveStock', () => {
    it('should reserve stock successfully', async () => {
      const mockItem = mockInventoryDoc({ quantity: 100, reservedQuantity: 10 });
      mockItem.save = jest.fn().mockResolvedValue(mockItem);
      jest.spyOn(inventoryModel, 'findOne').mockResolvedValue(mockItem as any);

      await service.reserveStock('tenant-1', 'item-123', 20, 'ORDER-001');

      expect(mockItem.reservedQuantity).toBe(30);
    });

    it('should throw BadRequestException when insufficient available', async () => {
      const mockItem = mockInventoryDoc({ quantity: 50, reservedQuantity: 45 });
      jest.spyOn(inventoryModel, 'findOne').mockResolvedValue(mockItem as any);

      await expect(service.reserveStock('tenant-1', 'item-123', 10, 'ORDER-001')).rejects.toThrow(BadRequestException);
    });
  });

  describe('releaseReservedStock', () => {
    it('should release reserved stock', async () => {
      const mockItem = mockInventoryDoc({ reservedQuantity: 30 });
      mockItem.save = jest.fn().mockResolvedValue(mockItem);
      jest.spyOn(inventoryModel, 'findOne').mockResolvedValue(mockItem as any);

      await service.releaseReservedStock('tenant-1', 'item-123', 10, 'ORDER-001');

      expect(mockItem.reservedQuantity).toBe(20);
    });

    it('should throw BadRequestException when trying to release more than reserved', async () => {
      const mockItem = mockInventoryDoc({ reservedQuantity: 5 });
      jest.spyOn(inventoryModel, 'findOne').mockResolvedValue(mockItem as any);

      await expect(service.releaseReservedStock('tenant-1', 'item-123', 10, 'ORDER-001')).rejects.toThrow(BadRequestException);
    });
  });
});
