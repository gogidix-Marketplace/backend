import { Injectable, NotFoundException, BadRequestException, ConflictException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { v4 as uuidv4 } from 'uuid';
import {
  InventoryItemDto,
  CreateInventoryItemCommand,
  UpdateStockCommand,
  AdjustStockCommand,
  AdjustmentType,
  TransferStockCommand,
  StockMovementDto,
  InventoryQueryDto,
  MovementType,
} from './dto/inventory.dto';
import { InventoryItem, InventoryItemDocument, StockMovement, StockMovementDocument } from '../domain/entities/inventory.entity';

@Injectable()
export class InventoryService {
  constructor(
    @InjectModel(InventoryItem.name)
    private inventoryModel: Model<InventoryItemDocument>,
    @InjectModel(StockMovement.name)
    private stockMovementModel: Model<StockMovementDocument>,
  ) {}

  async createInventoryItem(command: CreateInventoryItemCommand): Promise<InventoryItemDto> {
    const existing = await this.inventoryModel.findOne({
      tenantId: command.tenantId,
      warehouseId: command.warehouseId,
      sku: command.sku,
    });

    if (existing) {
      throw new ConflictException(`Inventory item with SKU ${command.sku} already exists in this warehouse`);
    }

    const item = new this.inventoryModel({
      ...command,
      reservedQuantity: 0,
    });

    const saved = await item.save();

    if (saved.quantity > 0) {
      await this.recordMovement({
        tenantId: saved.tenantId,
        itemId: saved._id.toString(),
        sku: saved.sku,
        warehouseId: saved.warehouseId,
        movementType: MovementType.RECEIVED,
        quantity: saved.quantity,
        previousQuantity: 0,
        newQuantity: saved.quantity,
        reason: 'Initial stock',
      });
    }

    return this.toDto(saved);
  }

  async getInventoryItem(tenantId: string, itemId: string): Promise<InventoryItemDto> {
    const item = await this.inventoryModel.findOne({ _id: itemId, tenantId });
    if (!item) {
      throw new NotFoundException(`Inventory item ${itemId} not found`);
    }
    return this.toDto(item);
  }

  async getInventoryItemBySku(tenantId: string, sku: string, warehouseId: string): Promise<InventoryItemDto> {
    const item = await this.inventoryModel.findOne({ tenantId, sku, warehouseId });
    if (!item) {
      throw new NotFoundException(`Inventory item with SKU ${sku} not found in warehouse ${warehouseId}`);
    }
    return this.toDto(item);
  }

  async queryInventoryItems(query: InventoryQueryDto): Promise<{ data: InventoryItemDto[]; total: number }> {
    const { tenantId, warehouseId, sku, name, minQuantity, maxQuantity, page = 1, limit = 20 } = query;

    const filter: any = { tenantId };
    if (warehouseId) filter.warehouseId = warehouseId;
    if (sku) filter.sku = { $regex: sku, $options: 'i' };
    if (name) filter.name = { $regex: name, $options: 'i' };
    if (minQuantity !== undefined || maxQuantity !== undefined) {
      filter.quantity = {};
      if (minQuantity !== undefined) filter.quantity.$gte = minQuantity;
      if (maxQuantity !== undefined) filter.quantity.$lte = maxQuantity;
    }

    const skip = (page - 1) * limit;
    const [items, total] = await Promise.all([
      this.inventoryModel.find(filter).skip(skip).limit(limit).sort({ createdAt: -1 }),
      this.inventoryModel.countDocuments(filter),
    ]);

    return {
      data: items.map((item) => this.toDto(item)),
      total,
    };
  }

  async updateStock(command: UpdateStockCommand): Promise<InventoryItemDto> {
    const item = await this.inventoryModel.findOne({ _id: command.itemId, tenantId: command.tenantId });
    if (!item) {
      throw new NotFoundException(`Inventory item ${command.itemId} not found`);
    }

    const previousQuantity = item.quantity;
    item.quantity = command.quantity;
    await item.save();

    await this.recordMovement({
      tenantId: item.tenantId,
      itemId: item._id.toString(),
      sku: item.sku,
      warehouseId: item.warehouseId,
      movementType: MovementType.ADJUSTED,
      quantity: Math.abs(command.quantity - previousQuantity),
      previousQuantity,
      newQuantity: command.quantity,
      reason: command.reason || 'Stock update',
    });

    return this.toDto(item);
  }

  async adjustStock(command: AdjustStockCommand): Promise<InventoryItemDto> {
    const item = await this.inventoryModel.findOne({ _id: command.itemId, tenantId: command.tenantId });
    if (!item) {
      throw new NotFoundException(`Inventory item ${command.itemId} not found`);
    }

    const previousQuantity = item.quantity;

    if (command.adjustmentType === AdjustmentType.INCREASE) {
      item.quantity += command.quantity;
    } else {
      if (item.quantity - item.reservedQuantity < command.quantity) {
        throw new BadRequestException('Insufficient available stock for this adjustment');
      }
      item.quantity -= command.quantity;
    }

    await item.save();

    await this.recordMovement({
      tenantId: item.tenantId,
      itemId: item._id.toString(),
      sku: item.sku,
      warehouseId: item.warehouseId,
      movementType: MovementType.ADJUSTED,
      quantity: command.quantity,
      previousQuantity,
      newQuantity: item.quantity,
      reason: command.reason,
    });

    return this.toDto(item);
  }

  async transferStock(command: TransferStockCommand): Promise<StockMovementDto> {
    const sourceItem = await this.inventoryModel.findOne({
      tenantId: command.tenantId,
      sku: command.sku,
      warehouseId: command.sourceWarehouseId,
    });

    if (!sourceItem) {
      throw new NotFoundException(`Source inventory item with SKU ${command.sku} not found`);
    }

    if (sourceItem.quantity - sourceItem.reservedQuantity < command.quantity) {
      throw new BadRequestException('Insufficient available stock for transfer');
    }

    let targetItem = await this.inventoryModel.findOne({
      tenantId: command.tenantId,
      sku: command.sku,
      warehouseId: command.targetWarehouseId,
    });

    const sourcePreviousQuantity = sourceItem.quantity;
    sourceItem.quantity -= command.quantity;
    await sourceItem.save();

    let targetPreviousQuantity = 0;
    if (!targetItem) {
      targetItem = new this.inventoryModel({
        tenantId: command.tenantId,
        warehouseId: command.targetWarehouseId,
        sku: command.sku,
        name: sourceItem.name,
        quantity: command.quantity,
        reservedQuantity: 0,
        reorderPoint: sourceItem.reorderPoint,
        reorderQuantity: sourceItem.reorderQuantity,
        unitCost: sourceItem.unitCost,
      });
      await targetItem.save();
    } else {
      targetPreviousQuantity = targetItem.quantity;
      targetItem.quantity += command.quantity;
      await targetItem.save();
    }

    const transferReference = command.reference || `TRF-${uuidv4().substring(0, 8)}`;

    await this.recordMovement({
      tenantId: sourceItem.tenantId,
      itemId: sourceItem._id.toString(),
      sku: sourceItem.sku,
      warehouseId: sourceItem.warehouseId,
      movementType: MovementType.TRANSFERRED_OUT,
      quantity: command.quantity,
      previousQuantity: sourcePreviousQuantity,
      newQuantity: sourceItem.quantity,
      reference: transferReference,
      reason: `Transfer to warehouse ${command.targetWarehouseId}`,
    });

    const targetMovement = await this.recordMovement({
      tenantId: targetItem.tenantId,
      itemId: targetItem._id.toString(),
      sku: targetItem.sku,
      warehouseId: targetItem.warehouseId,
      movementType: MovementType.TRANSFERRED_IN,
      quantity: command.quantity,
      previousQuantity: targetPreviousQuantity,
      newQuantity: targetItem.quantity,
      reference: transferReference,
      reason: `Transfer from warehouse ${command.sourceWarehouseId}`,
    });

    return targetMovement;
  }

  async getLowStockItems(tenantId: string, warehouseId: string, threshold?: number): Promise<InventoryItemDto[]> {
    const items = await this.inventoryModel.find({
      tenantId,
      warehouseId,
      $expr: {
        $lte: ['$quantity', { $ifNull: [threshold, '$reorderPoint'] }],
      },
    });

    return items.map((item) => this.toDto(item));
  }

  async getStockMovements(
    tenantId: string,
    warehouseId: string,
    itemId?: string,
    startDate?: Date,
    endDate?: Date,
  ): Promise<StockMovementDto[]> {
    const filter: any = { tenantId, warehouseId };

    if (itemId) filter.itemId = itemId;
    if (startDate || endDate) {
      filter.createdAt = {};
      if (startDate) filter.createdAt.$gte = startDate;
      if (endDate) filter.createdAt.$lte = endDate;
    }

    const movements = await this.stockMovementModel.find(filter).sort({ createdAt: -1 }).limit(100);
    return movements.map((m) => this.movementToDto(m));
  }

  async getReservedStock(tenantId: string, warehouseId: string): Promise<InventoryItemDto[]> {
    const items = await this.inventoryModel.find({
      tenantId,
      warehouseId,
      reservedQuantity: { $gt: 0 },
    });

    return items.map((item) => this.toDto(item));
  }

  async reserveStock(tenantId: string, itemId: string, quantity: number, reference: string): Promise<InventoryItemDto> {
    const item = await this.inventoryModel.findOne({ _id: itemId, tenantId });
    if (!item) {
      throw new NotFoundException(`Inventory item ${itemId} not found`);
    }

    const available = item.quantity - item.reservedQuantity;
    if (available < quantity) {
      throw new BadRequestException(`Only ${available} units available for reservation`);
    }

    const previousQuantity = item.reservedQuantity;
    item.reservedQuantity += quantity;
    await item.save();

    await this.recordMovement({
      tenantId,
      itemId: item._id.toString(),
      sku: item.sku,
      warehouseId: item.warehouseId,
      movementType: MovementType.RESERVED,
      quantity,
      previousQuantity: previousQuantity,
      newQuantity: item.reservedQuantity,
      reference,
      reason: 'Stock reservation',
    });

    return this.toDto(item);
  }

  async releaseReservedStock(tenantId: string, itemId: string, quantity: number, reference: string): Promise<InventoryItemDto> {
    const item = await this.inventoryModel.findOne({ _id: itemId, tenantId });
    if (!item) {
      throw new NotFoundException(`Inventory item ${itemId} not found`);
    }

    if (item.reservedQuantity < quantity) {
      throw new BadRequestException(`Cannot release ${quantity} units, only ${item.reservedQuantity} reserved`);
    }

    const previousQuantity = item.reservedQuantity;
    item.reservedQuantity -= quantity;
    await item.save();

    await this.recordMovement({
      tenantId,
      itemId: item._id.toString(),
      sku: item.sku,
      warehouseId: item.warehouseId,
      movementType: MovementType.RELEASED,
      quantity,
      previousQuantity: previousQuantity,
      newQuantity: item.reservedQuantity,
      reference,
      reason: 'Reservation released',
    });

    return this.toDto(item);
  }

  private async recordMovement(data: {
    tenantId: string;
    itemId: string;
    sku: string;
    warehouseId: string;
    movementType: MovementType;
    quantity: number;
    previousQuantity: number;
    newQuantity: number;
    reference?: string;
    reason?: string;
  }): Promise<StockMovementDto> {
    const movement = new this.stockMovementModel(data);
    const saved = await movement.save();
    return this.movementToDto(saved);
  }

  private toDto(item: InventoryItemDocument): InventoryItemDto {
    return {
      id: item._id.toString(),
      tenantId: item.tenantId,
      warehouseId: item.warehouseId,
      sku: item.sku,
      name: item.name,
      quantity: item.quantity,
      reservedQuantity: item.reservedQuantity,
      availableQuantity: item.quantity - item.reservedQuantity,
      reorderPoint: item.reorderPoint,
      reorderQuantity: item.reorderQuantity,
      unitCost: item.unitCost,
      createdAt: item.createdAt,
      updatedAt: item.updatedAt,
    };
  }

  private movementToDto(movement: StockMovementDocument): StockMovementDto {
    return {
      id: movement._id.toString(),
      tenantId: movement.tenantId,
      itemId: movement.itemId,
      sku: movement.sku,
      warehouseId: movement.warehouseId,
      movementType: movement.movementType as MovementType,
      quantity: movement.quantity,
      previousQuantity: movement.previousQuantity,
      newQuantity: movement.newQuantity,
      reference: movement.reference,
      reason: movement.reason,
      createdAt: movement.createdAt,
    };
  }
}
