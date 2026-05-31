import { Injectable, NotFoundException, BadRequestException, ConflictException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, DataSource, Between, In } from 'typeorm';
import { v4 as uuidv4 } from 'uuid';
import {
  PickOrder,
  PickItem,
  PickBatch,
  ZoneOptimization,
  PickOrderStatus,
  PickItemStatus,
  PickPriority,
  PickType,
} from '../domain/entities/picking.entity';
import {
  PickOrderDto,
  CreatePickOrderCommand,
  AssignPickerCommand,
  UpdateItemPickCommand,
  PickQueryDto,
  PickItemDto,
  BatchPickDto,
  CreateBatchPickCommand,
  ZoneOptimizationDto,
} from './dto/picking.dto';

@Injectable()
export class PickingService {
  constructor(
    @InjectRepository(PickOrder)
    private readonly pickOrderRepository: Repository<PickOrder>,
    @InjectRepository(PickItem)
    private readonly pickItemRepository: Repository<PickItem>,
    @InjectRepository(PickBatch)
    private readonly pickBatchRepository: Repository<PickBatch>,
    @InjectRepository(ZoneOptimization)
    private readonly zoneOptimizationRepository: Repository<ZoneOptimization>,
    private readonly dataSource: DataSource,
  ) {}

  async createPickOrder(command: CreatePickOrderCommand): Promise<PickOrderDto> {
    const pickOrderNumber = await this.generatePickOrderNumber(command.tenantId);

    const items: PickItem[] = command.items.map((item, index) => ({
      id: uuidv4(),
      pickOrderId: '',
      sku: item.sku,
      productName: item.productName,
      quantity: item.quantity,
      zone: item.zone,
      aisle: item.aisle,
      shelf: item.shelf,
      bin: item.bin ?? null,
      status: PickItemStatus.PENDING,
      pickedQuantity: null,
      pickedAt: null,
      notes: null,
      sortOrder: this.calculateSortOrder(item.zone, item.aisle, item.shelf, index),
      createdAt: new Date(),
      updatedAt: new Date(),
    }));

    const pickOrder = this.pickOrderRepository.create({
      id: uuidv4(),
      tenantId: command.tenantId,
      orderId: command.orderId,
      pickOrderNumber,
      status: PickOrderStatus.PENDING,
      pickType: command.pickType || PickType.SINGLE,
      priority: command.priority || PickPriority.MEDIUM,
      items,
      warehouseId: command.warehouseId,
      assignedZone: command.assignedZone,
      batchId: command.batchId,
      waveNumber: command.waveNumber,
      estimatedTimeMinutes: this.calculateEstimatedTime(items),
      totalItems: items.length,
      notes: command.notes,
      createdAt: new Date(),
      updatedAt: new Date(),
    });

    const saved = await this.pickOrderRepository.save(pickOrder);
    return this.toDto(saved);
  }

  async getPickOrder(tenantId: string, pickOrderId: string): Promise<PickOrderDto> {
    const order = await this.pickOrderRepository.findOne({
      where: { id: pickOrderId, tenantId },
      relations: ['items'],
    });

    if (!order) {
      throw new NotFoundException(`Pick order ${pickOrderId} not found`);
    }

    return this.toDto(order);
  }

  async getPickOrderByOrderId(tenantId: string, orderId: string): Promise<PickOrderDto> {
    const order = await this.pickOrderRepository.findOne({
      where: { orderId, tenantId },
      relations: ['items'],
    });

    if (!order) {
      throw new NotFoundException(`Pick order for order ${orderId} not found`);
    }

    return this.toDto(order);
  }

  async queryPickOrders(query: PickQueryDto): Promise<{ data: PickOrderDto[]; total: number }> {
    const { page = 1, limit = 20, sortBy = 'createdAt', sortOrder = 'DESC' } = query;

    const queryBuilder = this.pickOrderRepository
      .createQueryBuilder('pickOrder')
      .leftJoinAndSelect('pickOrder.items', 'items');

    if (query.tenantId) {
      queryBuilder.andWhere('pickOrder.tenantId = :tenantId', { tenantId: query.tenantId });
    }

    if (query.status) {
      queryBuilder.andWhere('pickOrder.status = :status', { status: query.status });
    }

    if (query.pickType) {
      queryBuilder.andWhere('pickOrder.pickType = :pickType', { pickType: query.pickType });
    }

    if (query.priority) {
      queryBuilder.andWhere('pickOrder.priority = :priority', { priority: query.priority });
    }

    if (query.pickerId) {
      queryBuilder.andWhere('pickOrder.pickerId = :pickerId', { pickerId: query.pickerId });
    }

    if (query.warehouseId) {
      queryBuilder.andWhere('pickOrder.warehouseId = :warehouseId', { warehouseId: query.warehouseId });
    }

    if (query.zone) {
      queryBuilder.andWhere('pickOrder.assignedZone = :zone', { zone: query.zone });
    }

    if (query.batchId) {
      queryBuilder.andWhere('pickOrder.batchId = :batchId', { batchId: query.batchId });
    }

    queryBuilder
      .orderBy(`pickOrder.${sortBy}`, sortOrder)
      .skip((page - 1) * limit)
      .take(limit);

    const [orders, total] = await queryBuilder.getManyAndCount();

    return {
      data: orders.map((order) => this.toDto(order)),
      total,
    };
  }

  async getAvailableOrders(tenantId: string): Promise<PickOrderDto[]> {
    const orders = await this.pickOrderRepository.find({
      where: { tenantId, status: PickOrderStatus.PENDING },
      relations: ['items'],
      order: { priority: 'DESC', createdAt: 'ASC' },
    });

    return orders.map((order) => this.toDto(order));
  }

  async getPickerOrders(tenantId: string, pickerId: string): Promise<PickOrderDto[]> {
    const orders = await this.pickOrderRepository.find({
      where: { tenantId, pickerId, status: In([PickOrderStatus.ASSIGNED, PickOrderStatus.IN_PROGRESS]) },
      relations: ['items'],
      order: { priority: 'DESC', createdAt: 'ASC' },
    });

    return orders.map((order) => this.toDto(order));
  }

  async assignPicker(command: AssignPickerCommand): Promise<PickOrderDto> {
    const order = await this.pickOrderRepository.findOne({
      where: { id: command.pickOrderId, tenantId: command.tenantId },
    });

    if (!order) {
      throw new NotFoundException(`Pick order ${command.pickOrderId} not found`);
    }

    if (order.status !== PickOrderStatus.PENDING) {
      throw new BadRequestException(`Cannot assign picker to order with status ${order.status}`);
    }

    order.pickerId = command.pickerId;
    order.pickerName = command.pickerName ?? null;
    order.status = PickOrderStatus.ASSIGNED;
    order.assignedAt = new Date();
    order.updatedAt = new Date();

    const saved = await this.pickOrderRepository.save(order);
    const withItems = await this.pickOrderRepository.findOne({
      where: { id: saved.id },
      relations: ['items'],
    });

    return this.toDto(withItems!);
  }

  async startPick(tenantId: string, pickOrderId: string): Promise<PickOrderDto> {
    const order = await this.pickOrderRepository.findOne({
      where: { id: pickOrderId, tenantId },
    });

    if (!order) {
      throw new NotFoundException(`Pick order ${pickOrderId} not found`);
    }

    if (order.status === PickOrderStatus.PENDING) {
      throw new BadRequestException('Pick order must be assigned before starting');
    }

    if (order.status !== PickOrderStatus.ASSIGNED) {
      throw new BadRequestException(`Cannot start pick order with status ${order.status}`);
    }

    order.status = PickOrderStatus.IN_PROGRESS;
    order.startedAt = new Date();
    order.updatedAt = new Date();

    const saved = await this.pickOrderRepository.save(order);
    const withItems = await this.pickOrderRepository.findOne({
      where: { id: saved.id },
      relations: ['items'],
    });

    return this.toDto(withItems!);
  }

  async updateItemPick(command: UpdateItemPickCommand): Promise<PickOrderDto> {
    const order = await this.pickOrderRepository.findOne({
      where: { id: command.pickOrderId, tenantId: command.tenantId },
      relations: ['items'],
    });

    if (!order) {
      throw new NotFoundException(`Pick order ${command.pickOrderId} not found`);
    }

    if (order.status !== PickOrderStatus.IN_PROGRESS) {
      throw new BadRequestException('Pick order must be in progress to update items');
    }

    const item = order.items.find((i) => i.id === command.itemId);

    if (!item) {
      throw new NotFoundException(`Pick item ${command.itemId} not found`);
    }

    item.status = command.status;
    item.pickedQuantity = command.pickedQuantity ?? (command.status === PickItemStatus.PICKED ? item.quantity : 0);
    item.pickedAt = command.status === PickItemStatus.PICKED ? new Date() : null;
    item.notes = command.notes ?? item.notes;
    item.updatedAt = new Date();

    await this.pickItemRepository.save(item);

    const updatedOrder = await this.pickOrderRepository.findOne({
      where: { id: command.pickOrderId },
      relations: ['items'],
    });

    if (updatedOrder) {
      updatedOrder.pickedItems = updatedOrder.items.filter(
        (i) => i.status === PickItemStatus.PICKED || i.status === PickItemStatus.SHORT,
      ).length;
      await this.pickOrderRepository.save(updatedOrder);
    }

    return this.toDto(updatedOrder!);
  }

  async completePick(tenantId: string, pickOrderId: string): Promise<PickOrderDto> {
    const order = await this.pickOrderRepository.findOne({
      where: { id: pickOrderId, tenantId },
      relations: ['items'],
    });

    if (!order) {
      throw new NotFoundException(`Pick order ${pickOrderId} not found`);
    }

    if (order.status !== PickOrderStatus.IN_PROGRESS) {
      throw new BadRequestException('Pick order must be in progress to complete');
    }

    const allItemsProcessed = order.items.every(
      (item) => item.status !== PickItemStatus.PENDING,
    );

    if (!allItemsProcessed) {
      throw new BadRequestException('All items must be processed before completing');
    }

    order.status = PickOrderStatus.COMPLETED;
    order.completedAt = new Date();
    order.actualTimeMinutes = this.calculateActualTime(order.startedAt!, order.completedAt);
    order.pickedItems = order.items.filter(
      (i) => i.status === PickItemStatus.PICKED || i.status === PickItemStatus.SHORT,
    ).length;
    order.updatedAt = new Date();

    const saved = await this.pickOrderRepository.save(order);
    return this.toDto(saved);
  }

  async cancelPick(tenantId: string, pickOrderId: string, reason?: string): Promise<PickOrderDto> {
    const order = await this.pickOrderRepository.findOne({
      where: { id: pickOrderId, tenantId },
      relations: ['items'],
    });

    if (!order) {
      throw new NotFoundException(`Pick order ${pickOrderId} not found`);
    }

    if (order.status === PickOrderStatus.COMPLETED) {
      throw new BadRequestException('Cannot cancel a completed pick order');
    }

    order.status = PickOrderStatus.CANCELLED;
    order.cancellationReason = reason ?? null;
    order.updatedAt = new Date();

    const saved = await this.pickOrderRepository.save(order);
    return this.toDto(saved);
  }

  async createBatchPick(command: CreateBatchPickCommand): Promise<BatchPickDto> {
    const orders = await this.pickOrderRepository.find({
      where: { id: In(command.pickOrderIds), tenantId: command.tenantId },
      relations: ['items'],
    });

    if (orders.length === 0) {
      throw new NotFoundException('No pick orders found');
    }

    const batchNumber = await this.generateBatchNumber(command.tenantId);
    const batchId = uuidv4();

    for (const order of orders) {
      if (order.status !== PickOrderStatus.PENDING) {
        throw new BadRequestException(`Order ${order.id} is not in pending status`);
      }
      order.batchId = batchId;
      order.pickType = PickType.BATCH;
      if (command.pickerId) {
        order.pickerId = command.pickerId;
        order.status = PickOrderStatus.ASSIGNED;
        order.assignedAt = new Date();
      }
      order.updatedAt = new Date();
    }

    await this.pickOrderRepository.save(orders);

    const batch = this.pickBatchRepository.create({
      id: batchId,
      tenantId: command.tenantId,
      batchNumber,
      pickOrderIds: command.pickOrderIds,
      totalItems: orders.reduce((sum, o) => sum + o.totalItems, 0),
      pickerId: command.pickerId,
      status: command.pickerId ? PickOrderStatus.ASSIGNED : PickOrderStatus.PENDING,
      createdAt: new Date(),
      updatedAt: new Date(),
    });

    const savedBatch = await this.pickBatchRepository.save(batch);

    return {
      id: savedBatch.id,
      tenantId: savedBatch.tenantId,
      batchNumber: savedBatch.batchNumber,
      pickOrders: orders.map((o) => this.toDto(o)),
      totalItems: savedBatch.totalItems,
      pickerId: savedBatch.pickerId ?? undefined,
      status: savedBatch.status,
      createdAt: savedBatch.createdAt.toISOString(),
    };
  }

  async getZoneOptimizations(tenantId: string): Promise<ZoneOptimizationDto[]> {
    const pendingItems = await this.pickItemRepository
      .createQueryBuilder('item')
      .innerJoin('item.pickOrder', 'pickOrder')
      .where('pickOrder.tenantId = :tenantId', { tenantId })
      .andWhere('pickOrder.status IN (:...statuses)', { statuses: [PickOrderStatus.PENDING, PickOrderStatus.ASSIGNED] })
      .andWhere('item.status = :itemStatus', { itemStatus: PickItemStatus.PENDING })
      .getMany();

    const zoneMap = new Map<string, { count: number; estimatedTime: number }>();

    for (const item of pendingItems) {
      const zoneData = zoneMap.get(item.zone) || { count: 0, estimatedTime: 0 };
      zoneData.count += 1;
      zoneData.estimatedTime += 2;
      zoneMap.set(item.zone, zoneData);
    }

    const optimizations: ZoneOptimizationDto[] = [];

    for (const [zone, data] of zoneMap.entries()) {
      const priorityScore = data.count * 1.5 + data.estimatedTime * 0.5;
      optimizations.push({
        zoneId: `zone-${zone.toLowerCase().replace(/\s+/g, '-')}`,
        zoneName: zone,
        pendingPicks: data.count,
        estimatedTimeMinutes: Math.ceil(data.estimatedTime),
        priorityScore,
      });
    }

    return optimizations.sort((a, b) => (b.priorityScore ?? 0) - (a.priorityScore ?? 0));
  }

  async optimizeZonePicking(tenantId: string, pickerId: string): Promise<PickOrderDto[]> {
    const optimizations = await this.getZoneOptimizations(tenantId);
    
    if (optimizations.length === 0) {
      return [];
    }

    const topZone = optimizations[0].zoneName;

    const orders = await this.pickOrderRepository.find({
      where: {
        tenantId,
        assignedZone: topZone,
        status: PickOrderStatus.PENDING,
      },
      relations: ['items'],
      order: { priority: 'DESC', createdAt: 'ASC' },
      take: 5,
    });

    for (const order of orders) {
      order.pickerId = pickerId;
      order.status = PickOrderStatus.ASSIGNED;
      order.assignedAt = new Date();
      order.updatedAt = new Date();
    }

    const savedOrders = await this.pickOrderRepository.save(orders);
    return savedOrders.map((o) => this.toDto(o));
  }

  private async generatePickOrderNumber(tenantId: string): Promise<string> {
    const count = await this.pickOrderRepository.count({ where: { tenantId } });
    const date = new Date();
    const dateStr = date.toISOString().slice(0, 10).replace(/-/g, '');
    return `PICK-${dateStr}-${(count + 1).toString().padStart(6, '0')}`;
  }

  private async generateBatchNumber(tenantId: string): Promise<string> {
    const count = await this.pickBatchRepository.count({ where: { tenantId } });
    const date = new Date();
    const dateStr = date.toISOString().slice(0, 10).replace(/-/g, '');
    return `BATCH-${dateStr}-${(count + 1).toString().padStart(4, '0')}`;
  }

  private calculateSortOrder(zone: string, aisle: string, shelf: string, defaultOrder: number): number {
    const zoneWeight = parseInt(zone.replace(/\D/g, '')) || defaultOrder;
    const aisleWeight = parseInt(aisle.replace(/\D/g, '')) || 0;
    const shelfWeight = parseInt(shelf.replace(/\D/g, '')) || 0;
    return zoneWeight * 10000 + aisleWeight * 100 + shelfWeight;
  }

  private calculateEstimatedTime(items: PickItem[]): number {
    return items.length * 2 + Math.ceil(items.length / 10) * 5;
  }

  private calculateActualTime(startedAt: Date, completedAt: Date): number {
    return Math.ceil((completedAt.getTime() - startedAt.getTime()) / (1000 * 60));
  }

  private toDto(entity: PickOrder): PickOrderDto {
    return {
      id: entity.id,
      tenantId: entity.tenantId,
      orderId: entity.orderId ?? undefined,
      pickOrderNumber: entity.pickOrderNumber,
      status: entity.status,
      pickType: entity.pickType,
      priority: entity.priority,
      items: entity.items?.map((item) => ({
        id: item.id,
        sku: item.sku,
        productName: item.productName,
        quantity: item.quantity,
        zone: item.zone,
        aisle: item.aisle,
        shelf: item.shelf,
        bin: item.bin ?? undefined,
        status: item.status,
        pickedQuantity: item.pickedQuantity ?? undefined,
        pickedAt: item.pickedAt?.toISOString(),
        notes: item.notes ?? undefined,
        sortOrder: item.sortOrder,
      })) ?? [],
      pickerId: entity.pickerId ?? undefined,
      pickerName: entity.pickerName ?? undefined,
      warehouseId: entity.warehouseId ?? undefined,
      assignedZone: entity.assignedZone ?? undefined,
      batchId: entity.batchId ?? undefined,
      waveNumber: entity.waveNumber ?? undefined,
      estimatedTimeMinutes: entity.estimatedTimeMinutes,
      actualTimeMinutes: entity.actualTimeMinutes ?? undefined,
      totalItems: entity.totalItems,
      pickedItems: entity.pickedItems ?? undefined,
      assignedAt: entity.assignedAt?.toISOString(),
      startedAt: entity.startedAt?.toISOString(),
      completedAt: entity.completedAt?.toISOString(),
      notes: entity.notes ?? undefined,
      createdAt: entity.createdAt.toISOString(),
      updatedAt: entity.updatedAt?.toISOString(),
    };
  }
}
