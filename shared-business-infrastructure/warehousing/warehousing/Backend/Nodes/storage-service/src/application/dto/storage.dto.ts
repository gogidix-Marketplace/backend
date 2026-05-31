import { IsUUID, IsString, IsOptional, IsNumber, IsEnum, IsBoolean, IsObject, Min, Max, ValidateNested } from 'class-validator';
import { Type } from 'class-transformer';
import { ApiProperty, ApiPropertyOptional, PartialType, OmitType } from '@nestjs/swagger';
import { StorageSpaceType, StorageSpaceStatus } from '../../domain/entities/storage.entity';

export class LocationDto {
  @ApiPropertyOptional({ example: 'A' })
  @IsOptional()
  @IsString()
  aisle?: string;

  @ApiPropertyOptional({ example: '01' })
  @IsOptional()
  @IsString()
  rack?: string;

  @ApiPropertyOptional({ example: '2' })
  @IsOptional()
  @IsString()
  level?: string;

  @ApiPropertyOptional({ example: 'B' })
  @IsOptional()
  @IsString()
  position?: string;
}

export class DimensionsDto {
  @ApiProperty({ example: 1.2 })
  @IsNumber()
  @Min(0)
  length: number;

  @ApiProperty({ example: 0.8 })
  @IsNumber()
  @Min(0)
  width: number;

  @ApiProperty({ example: 1.5 })
  @IsNumber()
  @Min(0)
  height: number;

  @ApiPropertyOptional({ example: 'm', default: 'm' })
  @IsOptional()
  @IsString()
  unit?: string;
}

export class StorageSpaceDto {
  @ApiProperty({ example: 'uuid' })
  id: string;

  @ApiProperty({ example: 'uuid' })
  tenantId: string;

  @ApiProperty({ example: 'uuid' })
  warehouseId: string;

  @ApiPropertyOptional({ example: 'uuid' })
  zoneId?: string;

  @ApiProperty({ example: 'A-01-02-B' })
  name: string;

  @ApiPropertyOptional({ example: 'PALLET-001' })
  code?: string;

  @ApiProperty({ enum: StorageSpaceType, example: StorageSpaceType.PALLET })
  spaceType: StorageSpaceType;

  @ApiProperty({ enum: StorageSpaceStatus, example: StorageSpaceStatus.AVAILABLE })
  status: StorageSpaceStatus;

  @ApiProperty({ example: 2.5 })
  capacityVolume: number;

  @ApiProperty({ example: 1000 })
  capacityWeight: number;

  @ApiProperty({ example: 1.5 })
  usedVolume: number;

  @ApiProperty({ example: 500 })
  usedWeight: number;

  @ApiPropertyOptional({ example: 1.2 })
  length?: number;

  @ApiPropertyOptional({ example: 0.8 })
  width?: number;

  @ApiPropertyOptional({ example: 1.5 })
  height?: number;

  @ApiPropertyOptional({ example: 5 })
  maxStackHeight?: number;

  @ApiPropertyOptional({ example: -5 })
  temperatureMin?: number;

  @ApiPropertyOptional({ example: 5 })
  temperatureMax?: number;

  @ApiPropertyOptional({ example: 30 })
  humidityMin?: number;

  @ApiPropertyOptional({ example: 60 })
  humidityMax?: number;

  @ApiPropertyOptional({ type: DimensionsDto })
  dimensions?: DimensionsDto;

  @ApiPropertyOptional({ type: LocationDto })
  location?: LocationDto;

  @ApiPropertyOptional({ example: { heavyLoad: true, fragile: false } })
  attributes?: Record<string, any>;

  @ApiPropertyOptional({ example: 'uuid' })
  currentInventoryId?: string;

  @ApiPropertyOptional({ example: 'uuid' })
  currentProductId?: string;

  @ApiProperty({ example: 1 })
  priority: number;

  @ApiProperty({ example: true })
  isActive: boolean;

  @ApiPropertyOptional({ example: 'Notes about this space' })
  notes?: string;

  @ApiProperty()
  createdAt: Date;

  @ApiProperty()
  updatedAt: Date;

  @ApiProperty({ example: 1.0, description: 'Available volume' })
  availableVolume: number;

  @ApiProperty({ example: 500, description: 'Available weight capacity' })
  availableWeight: number;

  @ApiProperty({ example: 60, description: 'Utilization percentage' })
  utilizationPercent: number;

  @ApiProperty({ example: true })
  isAvailable: boolean;
}

export class CreateStorageSpaceCommand {
  @ApiProperty({ example: 'uuid' })
  @IsUUID()
  tenantId: string;

  @ApiProperty({ example: 'uuid' })
  @IsUUID()
  warehouseId: string;

  @ApiPropertyOptional({ example: 'uuid' })
  @IsOptional()
  @IsUUID()
  zoneId?: string;

  @ApiProperty({ example: 'A-01-02-B' })
  @IsString()
  name: string;

  @ApiPropertyOptional({ example: 'PALLET-001' })
  @IsOptional()
  @IsString()
  code?: string;

  @ApiPropertyOptional({ enum: StorageSpaceType, example: StorageSpaceType.PALLET })
  @IsOptional()
  @IsEnum(StorageSpaceType)
  spaceType?: StorageSpaceType;

  @ApiProperty({ example: 2.5 })
  @IsNumber()
  @Min(0)
  capacityVolume: number;

  @ApiProperty({ example: 1000 })
  @IsNumber()
  @Min(0)
  capacityWeight: number;

  @ApiPropertyOptional({ example: 1.2 })
  @IsOptional()
  @IsNumber()
  @Min(0)
  length?: number;

  @ApiPropertyOptional({ example: 0.8 })
  @IsOptional()
  @IsNumber()
  @Min(0)
  width?: number;

  @ApiPropertyOptional({ example: 1.5 })
  @IsOptional()
  @IsNumber()
  @Min(0)
  height?: number;

  @ApiPropertyOptional({ example: 5 })
  @IsOptional()
  @IsNumber()
  @Min(1)
  maxStackHeight?: number;

  @ApiPropertyOptional({ example: -5 })
  @IsOptional()
  @IsNumber()
  temperatureMin?: number;

  @ApiPropertyOptional({ example: 5 })
  @IsOptional()
  @IsNumber()
  temperatureMax?: number;

  @ApiPropertyOptional({ example: 30 })
  @IsOptional()
  @IsNumber()
  @Min(0)
  @Max(100)
  humidityMin?: number;

  @ApiPropertyOptional({ example: 60 })
  @IsOptional()
  @IsNumber()
  @Min(0)
  @Max(100)
  humidityMax?: number;

  @ApiPropertyOptional({ type: DimensionsDto })
  @IsOptional()
  @ValidateNested()
  @Type(() => DimensionsDto)
  dimensions?: DimensionsDto;

  @ApiPropertyOptional({ type: LocationDto })
  @IsOptional()
  @ValidateNested()
  @Type(() => LocationDto)
  location?: LocationDto;

  @ApiPropertyOptional({ example: { heavyLoad: true } })
  @IsOptional()
  @IsObject()
  attributes?: Record<string, any>;

  @ApiPropertyOptional({ example: 1 })
  @IsOptional()
  @IsNumber()
  @Min(0)
  priority?: number;

  @ApiPropertyOptional({ example: true })
  @IsOptional()
  @IsBoolean()
  isActive?: boolean;

  @ApiPropertyOptional({ example: 'Notes' })
  @IsOptional()
  @IsString()
  notes?: string;

  @ApiPropertyOptional({ example: 'uuid' })
  @IsOptional()
  @IsUUID()
  createdBy?: string;
}

export class UpdateStorageSpaceCommand extends PartialType(
  OmitType(CreateStorageSpaceCommand, ['tenantId', 'warehouseId'] as const)
) {
  @ApiProperty({ example: 'uuid' })
  @IsUUID()
  id: string;

  @ApiProperty({ example: 'uuid' })
  @IsUUID()
  tenantId: string;

  @ApiPropertyOptional({ enum: StorageSpaceStatus })
  @IsOptional()
  @IsEnum(StorageSpaceStatus)
  status?: StorageSpaceStatus;

  @ApiPropertyOptional({ example: 'uuid' })
  @IsOptional()
  @IsUUID()
  updatedBy?: string;
}

export class StorageQueryDto {
  @ApiProperty({ example: 'uuid' })
  @IsUUID()
  tenantId: string;

  @ApiPropertyOptional({ example: 'uuid' })
  @IsOptional()
  @IsUUID()
  warehouseId?: string;

  @ApiPropertyOptional({ example: 'uuid' })
  @IsOptional()
  @IsUUID()
  zoneId?: string;

  @ApiPropertyOptional({ enum: StorageSpaceType })
  @IsOptional()
  @IsEnum(StorageSpaceType)
  spaceType?: StorageSpaceType;

  @ApiPropertyOptional({ enum: StorageSpaceStatus })
  @IsOptional()
  @IsEnum(StorageSpaceStatus)
  status?: StorageSpaceStatus;

  @ApiPropertyOptional({ example: 'A-01' })
  @IsOptional()
  @IsString()
  search?: string;

  @ApiPropertyOptional({ example: 1, default: 1 })
  @IsOptional()
  @IsNumber()
  @Min(1)
  page?: number;

  @ApiPropertyOptional({ example: 20, default: 20 })
  @IsOptional()
  @IsNumber()
  @Min(1)
  @Max(100)
  limit?: number;

  @ApiPropertyOptional({ example: 'name', default: 'name' })
  @IsOptional()
  @IsString()
  sortBy?: string;

  @ApiPropertyOptional({ example: 'ASC', default: 'ASC' })
  @IsOptional()
  @IsString()
  sortOrder?: 'ASC' | 'DESC';
}

export class AllocateStorageCommand {
  @ApiProperty({ example: 'uuid' })
  @IsUUID()
  tenantId: string;

  @ApiProperty({ example: 'uuid' })
  @IsUUID()
  spaceId: string;

  @ApiProperty({ example: 'uuid' })
  @IsUUID()
  inventoryId: string;

  @ApiPropertyOptional({ example: 'uuid' })
  @IsOptional()
  @IsUUID()
  productId?: string;

  @ApiProperty({ example: 0.5 })
  @IsNumber()
  @Min(0)
  volume: number;

  @ApiProperty({ example: 100 })
  @IsNumber()
  @Min(0)
  weight: number;

  @ApiPropertyOptional({ example: 'uuid' })
  @IsOptional()
  @IsUUID()
  allocatedBy?: string;
}

export class ReleaseStorageCommand {
  @ApiProperty({ example: 'uuid' })
  @IsUUID()
  tenantId: string;

  @ApiProperty({ example: 'uuid' })
  @IsUUID()
  spaceId: string;

  @ApiPropertyOptional({ example: 0 })
  @IsOptional()
  @IsNumber()
  @Min(0)
  volumeToRelease?: number;

  @ApiPropertyOptional({ example: 0 })
  @IsOptional()
  @IsNumber()
  @Min(0)
  weightToRelease?: number;

  @ApiPropertyOptional({ example: true, description: 'Release entire space' })
  @IsOptional()
  @IsBoolean()
  fullRelease?: boolean;

  @ApiPropertyOptional({ example: 'uuid' })
  @IsOptional()
  @IsUUID()
  releasedBy?: string;
}

export class WarehouseUtilizationDto {
  @ApiProperty()
  warehouseId: string;

  @ApiProperty()
  totalSpaces: number;

  @ApiProperty()
  availableSpaces: number;

  @ApiProperty()
  occupiedSpaces: number;

  @ApiProperty()
  reservedSpaces: number;

  @ApiProperty()
  maintenanceSpaces: number;

  @ApiProperty()
  totalCapacityVolume: number;

  @ApiProperty()
  totalUsedVolume: number;

  @ApiProperty()
  totalCapacityWeight: number;

  @ApiProperty()
  totalUsedWeight: number;

  @ApiProperty({ example: 65.5 })
  volumeUtilizationPercent: number;

  @ApiProperty({ example: 45.2 })
  weightUtilizationPercent: number;

  @ApiProperty({ type: Object, description: 'Utilization by space type' })
  utilizationByType: Record<string, { total: number; used: number; percent: number }>;
}

export class ZoneCapacityDto {
  @ApiProperty()
  zoneId: string;

  @ApiProperty()
  zoneName: string;

  @ApiProperty()
  totalSpaces: number;

  @ApiProperty()
  availableSpaces: number;

  @ApiProperty()
  occupiedSpaces: number;

  @ApiProperty()
  totalCapacityVolume: number;

  @ApiProperty()
  availableVolume: number;

  @ApiProperty()
  usedVolume: number;

  @ApiProperty()
  utilizationPercent: number;

  @ApiProperty({ type: [StorageSpaceDto] })
  spaces: StorageSpaceDto[];
}
