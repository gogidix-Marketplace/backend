import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';
import {
  IsString,
  IsUUID,
  IsOptional,
  IsEnum,
  IsNumber,
  IsArray,
  IsBoolean,
  ValidateNested,
  Min,
  Max,
  IsNotEmpty,
} from 'class-validator';
import { Type } from 'class-transformer';

export enum PickOrderStatus {
  PENDING = 'pending',
  ASSIGNED = 'assigned',
  IN_PROGRESS = 'in_progress',
  COMPLETED = 'completed',
  CANCELLED = 'cancelled',
}

export enum PickItemStatus {
  PENDING = 'pending',
  PICKED = 'picked',
  SKIPPED = 'skipped',
  SHORT = 'short',
}

export enum PickPriority {
  LOW = 'low',
  MEDIUM = 'medium',
  HIGH = 'high',
  URGENT = 'urgent',
}

export enum PickType {
  SINGLE = 'single',
  BATCH = 'batch',
  ZONE = 'zone',
  WAVE = 'wave',
}

export class PickItemDto {
  @ApiProperty({ description: 'Unique identifier for the pick item' })
  @IsUUID()
  id: string;

  @ApiProperty({ description: 'Product SKU' })
  @IsString()
  @IsNotEmpty()
  sku: string;

  @ApiProperty({ description: 'Product name' })
  @IsString()
  @IsNotEmpty()
  productName: string;

  @ApiProperty({ description: 'Quantity to pick' })
  @IsNumber()
  @Min(1)
  quantity: number;

  @ApiProperty({ description: 'Warehouse zone' })
  @IsString()
  @IsNotEmpty()
  zone: string;

  @ApiProperty({ description: 'Aisle location' })
  @IsString()
  @IsNotEmpty()
  aisle: string;

  @ApiProperty({ description: 'Shelf location' })
  @IsString()
  @IsNotEmpty()
  shelf: string;

  @ApiProperty({ description: 'Bin location' })
  @IsOptional()
  @IsString()
  bin?: string;

  @ApiProperty({ enum: PickItemStatus, description: 'Item pick status' })
  @IsEnum(PickItemStatus)
  status: PickItemStatus;

  @ApiPropertyOptional({ description: 'Quantity actually picked' })
  @IsOptional()
  @IsNumber()
  @Min(0)
  pickedQuantity?: number;

  @ApiPropertyOptional({ description: 'Timestamp when item was picked' })
  @IsOptional()
  @IsString()
  pickedAt?: string;

  @ApiPropertyOptional({ description: 'Notes for the pick' })
  @IsOptional()
  @IsString()
  notes?: string;

  @ApiProperty({ description: 'Sort order for pick optimization' })
  @IsNumber()
  sortOrder: number;
}

export class PickOrderDto {
  @ApiProperty({ description: 'Unique identifier for the pick order' })
  @IsUUID()
  id: string;

  @ApiProperty({ description: 'Tenant ID for multi-tenancy' })
  @IsUUID()
  tenantId: string;

  @ApiPropertyOptional({ description: 'Associated order ID' })
  @IsOptional()
  @IsString()
  orderId?: string;

  @ApiProperty({ description: 'Pick order number' })
  @IsString()
  @IsNotEmpty()
  pickOrderNumber: string;

  @ApiProperty({ enum: PickOrderStatus, description: 'Pick order status' })
  @IsEnum(PickOrderStatus)
  status: PickOrderStatus;

  @ApiProperty({ enum: PickType, description: 'Type of picking' })
  @IsEnum(PickType)
  pickType: PickType;

  @ApiProperty({ enum: PickPriority, description: 'Priority level' })
  @IsEnum(PickPriority)
  priority: PickPriority;

  @ApiProperty({ type: [PickItemDto], description: 'Items to pick' })
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => PickItemDto)
  items: PickItemDto[];

  @ApiPropertyOptional({ description: 'Assigned picker ID' })
  @IsOptional()
  @IsUUID()
  pickerId?: string;

  @ApiPropertyOptional({ description: 'Assigned picker name' })
  @IsOptional()
  @IsString()
  pickerName?: string;

  @ApiPropertyOptional({ description: 'Warehouse ID' })
  @IsOptional()
  @IsString()
  warehouseId?: string;

  @ApiPropertyOptional({ description: 'Zone assignment for zone picking' })
  @IsOptional()
  @IsString()
  assignedZone?: string;

  @ApiPropertyOptional({ description: 'Batch ID for batch picking' })
  @IsOptional()
  @IsUUID()
  batchId?: string;

  @ApiPropertyOptional({ description: 'Wave number for wave picking' })
  @IsOptional()
  @IsNumber()
  waveNumber?: number;

  @ApiProperty({ description: 'Estimated time to complete in minutes' })
  @IsNumber()
  @Min(1)
  estimatedTimeMinutes: number;

  @ApiPropertyOptional({ description: 'Actual time taken in minutes' })
  @IsOptional()
  @IsNumber()
  actualTimeMinutes?: number;

  @ApiProperty({ description: 'Total items count' })
  @IsNumber()
  totalItems: number;

  @ApiPropertyOptional({ description: 'Picked items count' })
  @IsOptional()
  @IsNumber()
  pickedItems?: number;

  @ApiPropertyOptional({ description: 'Timestamp when assigned' })
  @IsOptional()
  @IsString()
  assignedAt?: string;

  @ApiPropertyOptional({ description: 'Timestamp when started' })
  @IsOptional()
  @IsString()
  startedAt?: string;

  @ApiPropertyOptional({ description: 'Timestamp when completed' })
  @IsOptional()
  @IsString()
  completedAt?: string;

  @ApiPropertyOptional({ description: 'Pick notes' })
  @IsOptional()
  @IsString()
  notes?: string;

  @ApiProperty({ description: 'Creation timestamp' })
  @IsString()
  createdAt: string;

  @ApiPropertyOptional({ description: 'Last update timestamp' })
  @IsOptional()
  @IsString()
  updatedAt?: string;
}

export class CreatePickItemCommand {
  @ApiProperty({ description: 'Product SKU' })
  @IsString()
  @IsNotEmpty()
  sku: string;

  @ApiProperty({ description: 'Product name' })
  @IsString()
  @IsNotEmpty()
  productName: string;

  @ApiProperty({ description: 'Quantity to pick' })
  @IsNumber()
  @Min(1)
  quantity: number;

  @ApiProperty({ description: 'Warehouse zone' })
  @IsString()
  @IsNotEmpty()
  zone: string;

  @ApiProperty({ description: 'Aisle location' })
  @IsString()
  @IsNotEmpty()
  aisle: string;

  @ApiProperty({ description: 'Shelf location' })
  @IsString()
  @IsNotEmpty()
  shelf: string;

  @ApiPropertyOptional({ description: 'Bin location' })
  @IsOptional()
  @IsString()
  bin?: string;
}

export class CreatePickOrderCommand {
  @ApiProperty({ description: 'Tenant ID for multi-tenancy' })
  @IsUUID()
  tenantId: string;

  @ApiPropertyOptional({ description: 'Associated order ID' })
  @IsOptional()
  @IsString()
  orderId?: string;

  @ApiProperty({ enum: PickType, description: 'Type of picking', default: PickType.SINGLE })
  @IsOptional()
  @IsEnum(PickType)
  pickType?: PickType;

  @ApiProperty({ enum: PickPriority, description: 'Priority level', default: PickPriority.MEDIUM })
  @IsOptional()
  @IsEnum(PickPriority)
  priority?: PickPriority;

  @ApiProperty({ type: [CreatePickItemCommand], description: 'Items to pick' })
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => CreatePickItemCommand)
  items: CreatePickItemCommand[];

  @ApiPropertyOptional({ description: 'Warehouse ID' })
  @IsOptional()
  @IsString()
  warehouseId?: string;

  @ApiPropertyOptional({ description: 'Zone assignment for zone picking' })
  @IsOptional()
  @IsString()
  assignedZone?: string;

  @ApiPropertyOptional({ description: 'Batch ID for batch picking' })
  @IsOptional()
  @IsUUID()
  batchId?: string;

  @ApiPropertyOptional({ description: 'Wave number for wave picking' })
  @IsOptional()
  @IsNumber()
  waveNumber?: number;

  @ApiPropertyOptional({ description: 'Pick notes' })
  @IsOptional()
  @IsString()
  notes?: string;
}

export class AssignPickerCommand {
  @ApiProperty({ description: 'Tenant ID for multi-tenancy' })
  @IsUUID()
  tenantId: string;

  @ApiProperty({ description: 'Pick order ID' })
  @IsUUID()
  pickOrderId: string;

  @ApiProperty({ description: 'Picker user ID' })
  @IsUUID()
  pickerId: string;

  @ApiPropertyOptional({ description: 'Picker name' })
  @IsOptional()
  @IsString()
  pickerName?: string;
}

export class UpdateItemPickCommand {
  @ApiProperty({ description: 'Tenant ID for multi-tenancy' })
  @IsUUID()
  tenantId: string;

  @ApiProperty({ description: 'Pick order ID' })
  @IsUUID()
  pickOrderId: string;

  @ApiProperty({ description: 'Pick item ID' })
  @IsUUID()
  itemId: string;

  @ApiProperty({ enum: PickItemStatus, description: 'New item status' })
  @IsEnum(PickItemStatus)
  status: PickItemStatus;

  @ApiPropertyOptional({ description: 'Quantity picked' })
  @IsOptional()
  @IsNumber()
  @Min(0)
  pickedQuantity?: number;

  @ApiPropertyOptional({ description: 'Notes for the pick' })
  @IsOptional()
  @IsString()
  notes?: string;
}

export class PickQueryDto {
  @ApiPropertyOptional({ description: 'Tenant ID for multi-tenancy' })
  @IsOptional()
  @IsUUID()
  tenantId?: string;

  @ApiPropertyOptional({ enum: PickOrderStatus, description: 'Filter by status' })
  @IsOptional()
  @IsEnum(PickOrderStatus)
  status?: PickOrderStatus;

  @ApiPropertyOptional({ enum: PickType, description: 'Filter by pick type' })
  @IsOptional()
  @IsEnum(PickType)
  pickType?: PickType;

  @ApiPropertyOptional({ enum: PickPriority, description: 'Filter by priority' })
  @IsOptional()
  @IsEnum(PickPriority)
  priority?: PickPriority;

  @ApiPropertyOptional({ description: 'Filter by picker ID' })
  @IsOptional()
  @IsUUID()
  pickerId?: string;

  @ApiPropertyOptional({ description: 'Filter by warehouse ID' })
  @IsOptional()
  @IsString()
  warehouseId?: string;

  @ApiPropertyOptional({ description: 'Filter by zone' })
  @IsOptional()
  @IsString()
  zone?: string;

  @ApiPropertyOptional({ description: 'Filter by batch ID' })
  @IsOptional()
  @IsUUID()
  batchId?: string;

  @ApiPropertyOptional({ description: 'Page number', default: 1 })
  @IsOptional()
  @IsNumber()
  @Min(1)
  @Type(() => Number)
  page?: number;

  @ApiPropertyOptional({ description: 'Page size', default: 20 })
  @IsOptional()
  @IsNumber()
  @Min(1)
  @Max(100)
  @Type(() => Number)
  limit?: number;

  @ApiPropertyOptional({ description: 'Sort by field' })
  @IsOptional()
  @IsString()
  sortBy?: string;

  @ApiPropertyOptional({ description: 'Sort order', enum: ['ASC', 'DESC'] })
  @IsOptional()
  @IsString()
  sortOrder?: 'ASC' | 'DESC';
}

export class BatchPickDto {
  @ApiProperty({ description: 'Batch ID' })
  @IsUUID()
  id: string;

  @ApiProperty({ description: 'Tenant ID' })
  @IsUUID()
  tenantId: string;

  @ApiProperty({ description: 'Batch number' })
  @IsString()
  batchNumber: string;

  @ApiProperty({ type: [PickOrderDto], description: 'Pick orders in batch' })
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => PickOrderDto)
  pickOrders: PickOrderDto[];

  @ApiProperty({ description: 'Total items in batch' })
  @IsNumber()
  totalItems: number;

  @ApiPropertyOptional({ description: 'Assigned picker ID' })
  @IsOptional()
  @IsUUID()
  pickerId?: string;

  @ApiProperty({ description: 'Batch status' })
  @IsEnum(PickOrderStatus)
  status: PickOrderStatus;

  @ApiProperty({ description: 'Creation timestamp' })
  @IsString()
  createdAt: string;
}

export class ZoneOptimizationDto {
  @ApiProperty({ description: 'Zone ID' })
  @IsString()
  zoneId: string;

  @ApiProperty({ description: 'Zone name' })
  @IsString()
  zoneName: string;

  @ApiProperty({ description: 'Number of pending picks' })
  @IsNumber()
  pendingPicks: number;

  @ApiProperty({ description: 'Estimated completion time in minutes' })
  @IsNumber()
  estimatedTimeMinutes: number;

  @ApiPropertyOptional({ description: 'Suggested picker ID' })
  @IsOptional()
  @IsUUID()
  suggestedPickerId?: string;

  @ApiPropertyOptional({ description: 'Priority score' })
  @IsOptional()
  @IsNumber()
  priorityScore?: number;
}

export class CreateBatchPickCommand {
  @ApiProperty({ description: 'Tenant ID' })
  @IsUUID()
  tenantId: string;

  @ApiProperty({ description: 'Pick order IDs to batch' })
  @IsArray()
  @IsUUID('4', { each: true })
  pickOrderIds: string[];

  @ApiPropertyOptional({ description: 'Picker ID to assign' })
  @IsOptional()
  @IsUUID()
  pickerId?: string;
}
