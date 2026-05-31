import { Injectable, NotFoundException, BadRequestException, Logger } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, DataSource } from 'typeorm';
import { v4 as uuidv4 } from 'uuid';
import {
  PackOrderDto,
  CreatePackOrderCommand,
  AssignPackerCommand,
  PackItemCommand,
  PackQueryDto,
  PackageDto,
  ShippingLabelDto,
  PackOrderStatus,
  PackageStatus,
  QualityCheckStatus,
  BoxType,
} from './dto/packing.dto';
import { PackOrderEntity } from '../domain/entities/packing.entity';

@Injectable()
export class PackingService {
  private readonly logger = new Logger(PackingService.name);

  constructor(
    @InjectRepository(PackOrderEntity)
    private readonly packOrderRepository: Repository<PackOrderEntity>,
    private readonly dataSource: DataSource,
  ) {}

  async createPackOrder(command: CreatePackOrderCommand): Promise<PackOrderDto> {
    const packOrderNumber = await this.generatePackOrderNumber(command.tenantId);
    
    const packOrder = this.packOrderRepository.create({
      id: uuidv4(),
      tenantId: command.tenantId,
      packOrderNumber,
      pickOrderId: command.pickOrderId,
      orderId: command.orderId,
      status: PackOrderStatus.PENDING,
      items: command.items,
      priority: command.priority || 3,
      specialInstructions: command.specialInstructions,
      packages: [],
      qualityChecks: [],
      materialsUsed: [],
    });

    const saved = await this.packOrderRepository.save(packOrder);
    this.logger.log(`Created pack order ${saved.packOrderNumber} for tenant ${command.tenantId}`);
    
    return this.toDto(saved);
  }

  async getPackOrder(tenantId: string, packOrderId: string): Promise<PackOrderDto> {
    const packOrder = await this.packOrderRepository.findOne({
      where: { id: packOrderId, tenantId },
    });

    if (!packOrder) {
      throw new NotFoundException(`Pack order ${packOrderId} not found`);
    }

    return this.toDto(packOrder);
  }

  async getPackOrderByPickOrderId(tenantId: string, pickOrderId: string): Promise<PackOrderDto> {
    const packOrder = await this.packOrderRepository.findOne({
      where: { tenantId, pickOrderId },
    });

    if (!packOrder) {
      throw new NotFoundException(`Pack order for pick order ${pickOrderId} not found`);
    }

    return this.toDto(packOrder);
  }

  async queryPackOrders(query: PackQueryDto): Promise<{ data: PackOrderDto[]; total: number }> {
    const { tenantId, status, packerId, page = 1, limit = 20, search, fromDate, toDate } = query;
    
    const queryBuilder = this.packOrderRepository.createQueryBuilder('po')
      .where('po.tenantId = :tenantId', { tenantId });

    if (status) {
      queryBuilder.andWhere('po.status = :status', { status });
    }

    if (packerId) {
      queryBuilder.andWhere('po.packerId = :packerId', { packerId });
    }

    if (search) {
      queryBuilder.andWhere('po.packOrderNumber ILIKE :search', { search: `%${search}%` });
    }

    if (fromDate) {
      queryBuilder.andWhere('po.createdAt >= :fromDate', { fromDate });
    }

    if (toDate) {
      queryBuilder.andWhere('po.createdAt <= :toDate', { toDate });
    }

    queryBuilder
      .orderBy('po.priority', 'DESC')
      .addOrderBy('po.createdAt', 'ASC')
      .skip((page - 1) * limit)
      .take(limit);

    const [data, total] = await queryBuilder.getManyAndCount();
    
    return {
      data: data.map(d => this.toDto(d)),
      total,
    };
  }

  async getAvailableOrders(tenantId: string): Promise<PackOrderDto[]> {
    const orders = await this.packOrderRepository.find({
      where: { tenantId, status: PackOrderStatus.PENDING },
      order: { priority: 'DESC', createdAt: 'ASC' },
      take: 50,
    });

    return orders.map(o => this.toDto(o));
  }

  async getPackerOrders(tenantId: string, packerId: string): Promise<PackOrderDto[]> {
    const orders = await this.packOrderRepository.find({
      where: { tenantId, packerId },
      order: { createdAt: 'DESC' },
    });

    return orders.map(o => this.toDto(o));
  }

  async assignPacker(command: AssignPackerCommand): Promise<PackOrderDto> {
    const packOrder = await this.packOrderRepository.findOne({
      where: { id: command.packOrderId, tenantId: command.tenantId },
    });

    if (!packOrder) {
      throw new NotFoundException(`Pack order ${command.packOrderId} not found`);
    }

    if (packOrder.status !== PackOrderStatus.PENDING) {
      throw new BadRequestException(`Cannot assign packer to order with status ${packOrder.status}`);
    }

    packOrder.packerId = command.packerId;
    packOrder.packerName = command.packerName ?? '';
    packOrder.status = PackOrderStatus.ASSIGNED;

    const saved = await this.packOrderRepository.save(packOrder);
    this.logger.log(`Assigned packer ${command.packerId} to pack order ${saved.packOrderNumber}`);
    
    return this.toDto(saved);
  }

  async startPack(tenantId: string, packOrderId: string): Promise<PackOrderDto> {
    const packOrder = await this.packOrderRepository.findOne({
      where: { id: packOrderId, tenantId },
    });

    if (!packOrder) {
      throw new NotFoundException(`Pack order ${packOrderId} not found`);
    }

    if (packOrder.status !== PackOrderStatus.ASSIGNED && packOrder.status !== PackOrderStatus.PENDING) {
      throw new BadRequestException(`Cannot start pack order with status ${packOrder.status}`);
    }

    if (!packOrder.packerId) {
      throw new BadRequestException('Packer must be assigned before starting');
    }

    packOrder.status = PackOrderStatus.IN_PROGRESS;
    packOrder.startedAt = new Date();

    const saved = await this.packOrderRepository.save(packOrder);
    this.logger.log(`Started packing order ${saved.packOrderNumber}`);
    
    return this.toDto(saved);
  }

  async packItem(command: PackItemCommand): Promise<PackOrderDto> {
    return this.dataSource.transaction(async (manager) => {
      const packOrder = await manager.findOne(PackOrderEntity, {
        where: { id: command.packOrderId, tenantId: command.tenantId },
      });

      if (!packOrder) {
        throw new NotFoundException(`Pack order ${command.packOrderId} not found`);
      }

      if (packOrder.status !== PackOrderStatus.IN_PROGRESS) {
        throw new BadRequestException('Pack order must be in progress to pack items');
      }

      const items = packOrder.items as any[];
      const itemIndex = items.findIndex(i => i.itemId === command.itemId);
      
      if (itemIndex === -1) {
        throw new NotFoundException(`Item ${command.itemId} not found in pack order`);
      }

      const item = items[itemIndex];
      const newQuantityPacked = (item.quantityPacked || 0) + command.quantityPacked;

      if (newQuantityPacked > item.quantity) {
        throw new BadRequestException(`Cannot pack more than ${item.quantity} items`);
      }

      items[itemIndex] = {
        ...item,
        quantityPacked: newQuantityPacked,
        isPacked: newQuantityPacked >= item.quantity,
      };

      packOrder.items = items;

      if (command.qualityCheck) {
        const qualityChecks = packOrder.qualityChecks as any[] || [];
        qualityChecks.push({
          ...command.qualityCheck,
          id: uuidv4(),
          checkedAt: new Date(),
        });
        packOrder.qualityChecks = qualityChecks;
      }

      const saved = await manager.save(packOrder);
      this.logger.log(`Packed ${command.quantityPacked} of item ${command.itemId}`);
      
      return this.toDto(saved);
    });
  }

  async createPackage(tenantId: string, packOrderId: string, packageData: Partial<PackageDto>): Promise<PackOrderDto> {
    const packOrder = await this.packOrderRepository.findOne({
      where: { id: packOrderId, tenantId },
    });

    if (!packOrder) {
      throw new NotFoundException(`Pack order ${packOrderId} not found`);
    }

    const packages = packOrder.packages as any[] || [];
    const packageNumber = `${packOrder.packOrderNumber}-PKG-${packages.length + 1}`;

    const newPackage: PackageDto = {
      id: uuidv4(),
      packageNumber,
      boxType: packageData.boxType || BoxType.MEDIUM,
      length: packageData.length,
      width: packageData.width,
      height: packageData.height,
      weight: packageData.weight || 0,
      status: PackageStatus.CREATED,
      items: [],
    };

    packages.push(newPackage);
    packOrder.packages = packages;

    const saved = await this.packOrderRepository.save(packOrder);
    this.logger.log(`Created package ${packageNumber} for pack order ${saved.packOrderNumber}`);
    
    return this.toDto(saved);
  }

  async sealPackage(tenantId: string, packOrderId: string, packageId: string): Promise<PackOrderDto> {
    const packOrder = await this.packOrderRepository.findOne({
      where: { id: packOrderId, tenantId },
    });

    if (!packOrder) {
      throw new NotFoundException(`Pack order ${packOrderId} not found`);
    }

    const packages = packOrder.packages as any[];
    const pkgIndex = packages.findIndex(p => p.id === packageId);

    if (pkgIndex === -1) {
      throw new NotFoundException(`Package ${packageId} not found`);
    }

    packages[pkgIndex] = {
      ...packages[pkgIndex],
      status: PackageStatus.SEALED,
    };

    packOrder.packages = packages;
    const saved = await this.packOrderRepository.save(packOrder);
    
    return this.toDto(saved);
  }

  async addQualityCheck(tenantId: string, packOrderId: string, qualityCheck: any): Promise<PackOrderDto> {
    const packOrder = await this.packOrderRepository.findOne({
      where: { id: packOrderId, tenantId },
    });

    if (!packOrder) {
      throw new NotFoundException(`Pack order ${packOrderId} not found`);
    }

    const qualityChecks = packOrder.qualityChecks as any[] || [];
    qualityChecks.push({
      ...qualityCheck,
      id: uuidv4(),
      checkedAt: new Date(),
    });

    packOrder.qualityChecks = qualityChecks;
    const saved = await this.packOrderRepository.save(packOrder);
    
    return this.toDto(saved);
  }

  async completePack(tenantId: string, packOrderId: string): Promise<PackOrderDto> {
    return this.dataSource.transaction(async (manager) => {
      const packOrder = await manager.findOne(PackOrderEntity, {
        where: { id: packOrderId, tenantId },
      });

      if (!packOrder) {
        throw new NotFoundException(`Pack order ${packOrderId} not found`);
      }

      const items = packOrder.items as any[];
      const allItemsPacked = items.every(i => i.isPacked);

      if (!allItemsPacked) {
        throw new BadRequestException('All items must be packed before completion');
      }

      const qualityChecks = packOrder.qualityChecks as any[] || [];
      const failedChecks = qualityChecks.filter(q => q.status === QualityCheckStatus.FAILED);

      if (failedChecks.length > 0) {
        throw new BadRequestException('Cannot complete pack order with failed quality checks');
      }

      packOrder.status = PackOrderStatus.COMPLETED;
      packOrder.completedAt = new Date();

      const packages = packOrder.packages as any[] || [];
      let totalWeight = 0;
      packages.forEach(pkg => {
        totalWeight += pkg.weight || 0;
        pkg.status = PackageStatus.LABELED;
      });
      packOrder.packages = packages;
      packOrder.totalWeight = totalWeight;

      const saved = await manager.save(packOrder);
      this.logger.log(`Completed pack order ${saved.packOrderNumber}`);
      
      return this.toDto(saved);
    });
  }

  async generateShippingLabels(tenantId: string, packOrderId: string): Promise<ShippingLabelDto[]> {
    const packOrder = await this.packOrderRepository.findOne({
      where: { id: packOrderId, tenantId },
    });

    if (!packOrder) {
      throw new NotFoundException(`Pack order ${packOrderId} not found`);
    }

    if (packOrder.status !== PackOrderStatus.COMPLETED) {
      throw new BadRequestException('Pack order must be completed before generating labels');
    }

    const packages = packOrder.packages as any[];
    const labels: ShippingLabelDto[] = [];

    for (const pkg of packages) {
      const trackingNumber = await this.generateTrackingNumber();
      
      labels.push({
        id: uuidv4(),
        trackingNumber,
        carrier: 'DEFAULT_CARRIER',
        labelUrl: `https://labels.example.com/${trackingNumber}.pdf`,
        createdAt: new Date(),
      });

      pkg.trackingNumber = trackingNumber;
      pkg.status = PackageStatus.SHIPPED;
    }

    packOrder.packages = packages;
    await this.packOrderRepository.save(packOrder);
    
    this.logger.log(`Generated ${labels.length} shipping labels for pack order ${packOrder.packOrderNumber}`);
    
    return labels;
  }

  private async generatePackOrderNumber(tenantId: string): Promise<string> {
    const date = new Date();
    const year = date.getFullYear().toString().slice(-2);
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const day = date.getDate().toString().padStart(2, '0');
    
    const count = await this.packOrderRepository.count({
      where: { tenantId },
    });

    const sequence = (count + 1).toString().padStart(6, '0');
    return `PK${year}${month}${day}-${sequence}`;
  }

  private async generateTrackingNumber(): Promise<string> {
    const prefix = 'TRK';
    const timestamp = Date.now().toString(36).toUpperCase();
    const random = Math.random().toString(36).substring(2, 8).toUpperCase();
    return `${prefix}${timestamp}${random}`;
  }

  private toDto(entity: PackOrderEntity): PackOrderDto {
    return {
      id: entity.id,
      tenantId: entity.tenantId,
      packOrderNumber: entity.packOrderNumber,
      pickOrderId: entity.pickOrderId,
      orderId: entity.orderId,
      status: entity.status,
      packerId: entity.packerId,
      packerName: entity.packerName,
      items: entity.items,
      packages: entity.packages,
      qualityChecks: entity.qualityChecks,
      materialsUsed: entity.materialsUsed,
      totalWeight: entity.totalWeight ? Number(entity.totalWeight) : undefined,
      priority: entity.priority,
      specialInstructions: entity.specialInstructions,
      createdAt: entity.createdAt,
      updatedAt: entity.updatedAt,
      startedAt: entity.startedAt,
      completedAt: entity.completedAt,
    };
  }
}
