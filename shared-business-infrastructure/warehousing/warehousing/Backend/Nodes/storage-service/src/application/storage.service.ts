import { Injectable, NotFoundException, ConflictException, BadRequestException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, Like, Between, In } from 'typeorm';
import { v4 as uuidv4 } from 'uuid';
import { StorageSpace, StorageSpaceStatus, StorageSpaceType } from '../domain/entities/storage.entity';
import {
  StorageSpaceDto,
  CreateStorageSpaceCommand,
  UpdateStorageSpaceCommand,
  StorageQueryDto,
  AllocateStorageCommand,
  ReleaseStorageCommand,
  WarehouseUtilizationDto,
  ZoneCapacityDto,
} from './dto/storage.dto';

@Injectable()
export class StorageService {
  constructor(
    @InjectRepository(StorageSpace)
    private readonly storageSpaceRepository: Repository<StorageSpace>,
  ) {}

  async createStorageSpace(command: CreateStorageSpaceCommand): Promise<StorageSpaceDto> {
    const space = this.storageSpaceRepository.create({
      id: uuidv4(),
      tenantId: command.tenantId,
      warehouseId: command.warehouseId,
      zoneId: command.zoneId,
      name: command.name,
      code: command.code,
      spaceType: command.spaceType || StorageSpaceType.PALLET,
      status: StorageSpaceStatus.AVAILABLE,
      capacityVolume: command.capacityVolume,
      capacityWeight: command.capacityWeight,
      usedVolume: 0,
      usedWeight: 0,
      length: command.length,
      width: command.width,
      height: command.height,
      maxStackHeight: command.maxStackHeight,
      temperatureMin: command.temperatureMin,
      temperatureMax: command.temperatureMax,
      humidityMin: command.humidityMin,
      humidityMax: command.humidityMax,
      dimensions: command.dimensions,
      location: command.location,
      attributes: command.attributes,
      priority: command.priority || 0,
      isActive: command.isActive !== undefined ? command.isActive : true,
      notes: command.notes,
      createdBy: command.createdBy,
    });

    const saved = await this.storageSpaceRepository.save(space);
    return this.toDto(saved);
  }

  async getStorageSpace(tenantId: string, spaceId: string): Promise<StorageSpaceDto> {
    const space = await this.storageSpaceRepository.findOne({
      where: { id: spaceId, tenantId },
    });

    if (!space) {
      throw new NotFoundException(`Storage space with id ${spaceId} not found`);
    }

    return this.toDto(space);
  }

  async updateStorageSpace(command: UpdateStorageSpaceCommand): Promise<StorageSpaceDto> {
    const space = await this.storageSpaceRepository.findOne({
      where: { id: command.id, tenantId: command.tenantId },
    });

    if (!space) {
      throw new NotFoundException(`Storage space with id ${command.id} not found`);
    }

    if (command.capacityVolume !== undefined) {
      space.capacityVolume = command.capacityVolume;
    }
    if (command.capacityWeight !== undefined) {
      space.capacityWeight = command.capacityWeight;
    }
    if (command.name !== undefined) {
      space.name = command.name;
    }
    if (command.code !== undefined) {
      space.code = command.code;
    }
    if (command.spaceType !== undefined) {
      space.spaceType = command.spaceType;
    }
    if (command.zoneId !== undefined) {
      space.zoneId = command.zoneId;
    }
    if (command.status !== undefined) {
      space.status = command.status;
    }
    if (command.temperatureMin !== undefined) {
      space.temperatureMin = command.temperatureMin;
    }
    if (command.temperatureMax !== undefined) {
      space.temperatureMax = command.temperatureMax;
    }
    if (command.humidityMin !== undefined) {
      space.humidityMin = command.humidityMin;
    }
    if (command.humidityMax !== undefined) {
      space.humidityMax = command.humidityMax;
    }
    if (command.location !== undefined) {
      if (command.location.aisle && command.location.rack && command.location.level && command.location.position) {
        space.location = {
          aisle: command.location.aisle,
          rack: command.location.rack,
          level: command.location.level,
          position: command.location.position,
        };
      }
    }
    if (command.attributes !== undefined) {
      space.attributes = command.attributes;
    }
    if (command.priority !== undefined) {
      space.priority = command.priority;
    }
    if (command.isActive !== undefined) {
      space.isActive = command.isActive;
    }
    if (command.notes !== undefined) {
      space.notes = command.notes;
    }
    if (command.updatedBy !== undefined) {
      space.updatedBy = command.updatedBy;
    }

    const saved = await this.storageSpaceRepository.save(space);
    return this.toDto(saved);
  }

  async queryStorageSpaces(query: StorageQueryDto): Promise<{ data: StorageSpaceDto[]; total: number }> {
    const { tenantId, warehouseId, zoneId, spaceType, status, search, page = 1, limit = 20, sortBy = 'name', sortOrder = 'ASC' } = query;

    const queryBuilder = this.storageSpaceRepository.createQueryBuilder('space');
    queryBuilder.where('space.tenantId = :tenantId', { tenantId });

    if (warehouseId) {
      queryBuilder.andWhere('space.warehouseId = :warehouseId', { warehouseId });
    }

    if (zoneId) {
      queryBuilder.andWhere('space.zoneId = :zoneId', { zoneId });
    }

    if (spaceType) {
      queryBuilder.andWhere('space.spaceType = :spaceType', { spaceType });
    }

    if (status) {
      queryBuilder.andWhere('space.status = :status', { status });
    }

    if (search) {
      queryBuilder.andWhere('(space.name ILIKE :search OR space.code ILIKE :search)', { search: `%${search}%` });
    }

    queryBuilder.orderBy(`space.${sortBy}`, sortOrder);
    queryBuilder.skip((page - 1) * limit).take(limit);

    const [spaces, total] = await queryBuilder.getManyAndCount();
    const data = spaces.map(space => this.toDto(space));

    return { data, total };
  }

  async getAvailableSpaces(tenantId: string, warehouseId: string, spaceType?: StorageSpaceType): Promise<StorageSpaceDto[]> {
    const queryBuilder = this.storageSpaceRepository.createQueryBuilder('space');
    queryBuilder.where('space.tenantId = :tenantId', { tenantId });
    queryBuilder.andWhere('space.warehouseId = :warehouseId', { warehouseId });
    queryBuilder.andWhere('space.status = :status', { status: StorageSpaceStatus.AVAILABLE });
    queryBuilder.andWhere('space.isActive = :isActive', { isActive: true });
    queryBuilder.andWhere('space.usedVolume < space.capacityVolume');

    if (spaceType) {
      queryBuilder.andWhere('space.spaceType = :spaceType', { spaceType });
    }

    queryBuilder.orderBy('space.priority', 'DESC');
    queryBuilder.addOrderBy('(space.usedVolume * 1.0 / NULLIF(space.capacityVolume, 0))', 'ASC');

    const spaces = await queryBuilder.getMany();
    return spaces.map(space => this.toDto(space));
  }

  async allocateStorage(command: AllocateStorageCommand): Promise<StorageSpaceDto> {
    const space = await this.storageSpaceRepository.findOne({
      where: { id: command.spaceId, tenantId: command.tenantId },
    });

    if (!space) {
      throw new NotFoundException(`Storage space with id ${command.spaceId} not found`);
    }

    if (space.status !== StorageSpaceStatus.AVAILABLE && space.status !== StorageSpaceStatus.OCCUPIED) {
      throw new ConflictException(`Storage space is ${space.status} and cannot be allocated`);
    }

    const newUsedVolume = space.usedVolume + command.volume;
    const newUsedWeight = space.usedWeight + command.weight;

    if (newUsedVolume > space.capacityVolume) {
      throw new BadRequestException(`Insufficient volume capacity. Available: ${space.availableVolume}, Requested: ${command.volume}`);
    }

    if (newUsedWeight > space.capacityWeight) {
      throw new BadRequestException(`Insufficient weight capacity. Available: ${space.availableWeight}, Requested: ${command.weight}`);
    }

    space.usedVolume = newUsedVolume;
    space.usedWeight = newUsedWeight;
    space.currentInventoryId = command.inventoryId || null;
    space.currentProductId = command.productId || null;
    space.status = space.isFullyOccupied ? StorageSpaceStatus.OCCUPIED : StorageSpaceStatus.OCCUPIED;
    space.updatedBy = command.allocatedBy || null;

    const saved = await this.storageSpaceRepository.save(space);
    return this.toDto(saved);
  }

  async releaseStorage(command: ReleaseStorageCommand): Promise<StorageSpaceDto> {
    const space = await this.storageSpaceRepository.findOne({
      where: { id: command.spaceId, tenantId: command.tenantId },
    });

    if (!space) {
      throw new NotFoundException(`Storage space with id ${command.spaceId} not found`);
    }

    if (space.status !== StorageSpaceStatus.OCCUPIED) {
      throw new BadRequestException(`Storage space is not occupied`);
    }

    if (command.fullRelease) {
      space.usedVolume = 0;
      space.usedWeight = 0;
      space.currentInventoryId = null;
      space.currentProductId = null;
    } else {
      const volumeToRelease = command.volumeToRelease || 0;
      const weightToRelease = command.weightToRelease || 0;

      space.usedVolume = Math.max(0, space.usedVolume - volumeToRelease);
      space.usedWeight = Math.max(0, space.usedWeight - weightToRelease);

      if (space.usedVolume === 0 && space.usedWeight === 0) {
        space.currentInventoryId = null;
        space.currentProductId = null;
      }
    }

    space.status = space.usedVolume > 0 || space.usedWeight > 0 ? StorageSpaceStatus.OCCUPIED : StorageSpaceStatus.AVAILABLE;
    space.updatedBy = command.releasedBy || null;

    const saved = await this.storageSpaceRepository.save(space);
    return this.toDto(saved);
  }

  async getWarehouseUtilization(tenantId: string, warehouseId: string): Promise<WarehouseUtilizationDto> {
    const spaces = await this.storageSpaceRepository.find({
      where: { tenantId, warehouseId },
    });

    const totalSpaces = spaces.length;
    const availableSpaces = spaces.filter(s => s.status === StorageSpaceStatus.AVAILABLE).length;
    const occupiedSpaces = spaces.filter(s => s.status === StorageSpaceStatus.OCCUPIED).length;
    const reservedSpaces = spaces.filter(s => s.status === StorageSpaceStatus.RESERVED).length;
    const maintenanceSpaces = spaces.filter(s => s.status === StorageSpaceStatus.MAINTENANCE).length;

    const totalCapacityVolume = spaces.reduce((sum, s) => sum + Number(s.capacityVolume), 0);
    const totalUsedVolume = spaces.reduce((sum, s) => sum + Number(s.usedVolume), 0);
    const totalCapacityWeight = spaces.reduce((sum, s) => sum + Number(s.capacityWeight), 0);
    const totalUsedWeight = spaces.reduce((sum, s) => sum + Number(s.usedWeight), 0);

    const volumeUtilizationPercent = totalCapacityVolume > 0 ? Math.round((totalUsedVolume / totalCapacityVolume) * 100 * 100) / 100 : 0;
    const weightUtilizationPercent = totalCapacityWeight > 0 ? Math.round((totalUsedWeight / totalCapacityWeight) * 100 * 100) / 100 : 0;

    const utilizationByType: Record<string, { total: number; used: number; percent: number }> = {};
    
    for (const spaceType of Object.values(StorageSpaceType)) {
      const typeSpaces = spaces.filter(s => s.spaceType === spaceType);
      if (typeSpaces.length > 0) {
        const total = typeSpaces.reduce((sum, s) => sum + Number(s.capacityVolume), 0);
        const used = typeSpaces.reduce((sum, s) => sum + Number(s.usedVolume), 0);
        utilizationByType[spaceType] = {
          total,
          used,
          percent: total > 0 ? Math.round((used / total) * 100 * 100) / 100 : 0,
        };
      }
    }

    return {
      warehouseId,
      totalSpaces,
      availableSpaces,
      occupiedSpaces,
      reservedSpaces,
      maintenanceSpaces,
      totalCapacityVolume,
      totalUsedVolume,
      totalCapacityWeight,
      totalUsedWeight,
      volumeUtilizationPercent,
      weightUtilizationPercent,
      utilizationByType,
    };
  }

  async getWarehouseZones(tenantId: string, warehouseId: string): Promise<{ zoneId: string; zoneName: string; spaceCount: number }[]> {
    const spaces = await this.storageSpaceRepository.find({
      where: { tenantId, warehouseId },
    });

    const zoneMap = new Map<string, { zoneId: string; spaceCount: number }>();

    for (const space of spaces) {
      if (space.zoneId) {
        const existing = zoneMap.get(space.zoneId);
        if (existing) {
          existing.spaceCount++;
        } else {
          zoneMap.set(space.zoneId, { zoneId: space.zoneId, spaceCount: 1 });
        }
      }
    }

    return Array.from(zoneMap.values()).map(z => ({
      ...z,
      zoneName: `Zone ${z.zoneId.substring(0, 8)}`,
    }));
  }

  async getZoneCapacity(tenantId: string, zoneId: string): Promise<ZoneCapacityDto> {
    const spaces = await this.storageSpaceRepository.find({
      where: { tenantId, zoneId },
    });

    if (spaces.length === 0) {
      throw new NotFoundException(`No storage spaces found for zone ${zoneId}`);
    }

    const totalSpaces = spaces.length;
    const availableSpaces = spaces.filter(s => s.status === StorageSpaceStatus.AVAILABLE).length;
    const occupiedSpaces = spaces.filter(s => s.status === StorageSpaceStatus.OCCUPIED).length;
    const totalCapacityVolume = spaces.reduce((sum, s) => sum + Number(s.capacityVolume), 0);
    const usedVolume = spaces.reduce((sum, s) => sum + Number(s.usedVolume), 0);
    const availableVolume = totalCapacityVolume - usedVolume;
    const utilizationPercent = totalCapacityVolume > 0 ? Math.round((usedVolume / totalCapacityVolume) * 100) : 0;

    return {
      zoneId,
      zoneName: `Zone ${zoneId.substring(0, 8)}`,
      totalSpaces,
      availableSpaces,
      occupiedSpaces,
      totalCapacityVolume,
      availableVolume,
      usedVolume,
      utilizationPercent,
      spaces: spaces.map(s => this.toDto(s)),
    };
  }

  private toDto(space: StorageSpace): StorageSpaceDto {
    return {
      id: space.id,
      tenantId: space.tenantId,
      warehouseId: space.warehouseId,
      zoneId: space.zoneId || undefined,
      name: space.name,
      code: space.code || undefined,
      spaceType: space.spaceType,
      status: space.status,
      capacityVolume: Number(space.capacityVolume),
      capacityWeight: Number(space.capacityWeight),
      usedVolume: Number(space.usedVolume),
      usedWeight: Number(space.usedWeight),
      length: space.length ? Number(space.length) : undefined,
      width: space.width ? Number(space.width) : undefined,
      height: space.height ? Number(space.height) : undefined,
      maxStackHeight: space.maxStackHeight,
      temperatureMin: space.temperatureMin ? Number(space.temperatureMin) : undefined,
      temperatureMax: space.temperatureMax ? Number(space.temperatureMax) : undefined,
      humidityMin: space.humidityMin ? Number(space.humidityMin) : undefined,
      humidityMax: space.humidityMax ? Number(space.humidityMax) : undefined,
      dimensions: space.dimensions,
      location: space.location,
      attributes: space.attributes,
      currentInventoryId: space.currentInventoryId || undefined,
      currentProductId: space.currentProductId || undefined,
      priority: space.priority,
      isActive: space.isActive,
      notes: space.notes,
      createdAt: space.createdAt,
      updatedAt: space.updatedAt,
      availableVolume: space.availableVolume,
      availableWeight: space.availableWeight,
      utilizationPercent: space.utilizationPercent,
      isAvailable: space.isAvailable,
    };
  }
}
