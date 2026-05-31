import { IsString, IsNumber, IsOptional, IsDateString, Min, IsEnum, IsNotEmpty } from 'class-validator';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class InventoryItemDto {
  @ApiProperty()
  id: string;

  @ApiProperty()
  tenantId: string;

  @ApiProperty()
  warehouseId: string;

  @ApiProperty()
  sku: string;

  @ApiPropertyOptional()
  name?: string;

  @ApiProperty()
  quantity: number;

  @ApiProperty()
  reservedQuantity: number;

  @ApiProperty()
  availableQuantity: number;

  @ApiPropertyOptional()
  reorderPoint?: number;

  @ApiPropertyOptional()
  reorderQuantity?: number;

  @ApiPropertyOptional()
  unitCost?: number;

  @ApiProperty()
  createdAt: Date;

  @ApiProperty()
  updatedAt: Date;
}

export class CreateInventoryItemCommand {
  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  tenantId: string;

  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  warehouseId: string;

  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  sku: string;

  @ApiPropertyOptional()
  @IsString()
  @IsOptional()
  name?: string;

  @ApiProperty()
  @IsNumber()
  @Min(0)
  quantity: number;

  @ApiPropertyOptional()
  @IsNumber()
  @Min(0)
  @IsOptional()
  reorderPoint?: number;

  @ApiPropertyOptional()
  @IsNumber()
  @Min(0)
  @IsOptional()
  reorderQuantity?: number;

  @ApiPropertyOptional()
  @IsNumber()
  @Min(0)
  @IsOptional()
  unitCost?: number;
}

export class UpdateStockCommand {
  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  tenantId: string;

  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  itemId: string;

  @ApiProperty()
  @IsNumber()
  @Min(0)
  quantity: number;

  @ApiPropertyOptional()
  @IsString()
  @IsOptional()
  reason?: string;
}

export enum AdjustmentType {
  INCREASE = 'increase',
  DECREASE = 'decrease',
}

export class AdjustStockCommand {
  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  tenantId: string;

  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  itemId: string;

  @ApiProperty({ enum: AdjustmentType })
  @IsEnum(AdjustmentType)
  adjustmentType: AdjustmentType;

  @ApiProperty()
  @IsNumber()
  @Min(1)
  quantity: number;

  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  reason: string;
}

export class TransferStockCommand {
  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  tenantId: string;

  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  sku: string;

  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  sourceWarehouseId: string;

  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  targetWarehouseId: string;

  @ApiProperty()
  @IsNumber()
  @Min(1)
  quantity: number;

  @ApiPropertyOptional()
  @IsString()
  @IsOptional()
  reference?: string;
}

export enum MovementType {
  RECEIVED = 'received',
  SHIPPED = 'shipped',
  ADJUSTED = 'adjusted',
  TRANSFERRED_IN = 'transferred_in',
  TRANSFERRED_OUT = 'transferred_out',
  RESERVED = 'reserved',
  RELEASED = 'released',
}

export class StockMovementDto {
  @ApiProperty()
  id: string;

  @ApiProperty()
  tenantId: string;

  @ApiProperty()
  itemId: string;

  @ApiProperty()
  sku: string;

  @ApiProperty()
  warehouseId: string;

  @ApiProperty({ enum: MovementType })
  movementType: MovementType;

  @ApiProperty()
  quantity: number;

  @ApiProperty()
  previousQuantity: number;

  @ApiProperty()
  newQuantity: number;

  @ApiPropertyOptional()
  reference?: string;

  @ApiPropertyOptional()
  reason?: string;

  @ApiProperty()
  createdAt: Date;
}

export class InventoryQueryDto {
  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  tenantId: string;

  @ApiPropertyOptional()
  @IsString()
  @IsOptional()
  warehouseId?: string;

  @ApiPropertyOptional()
  @IsString()
  @IsOptional()
  sku?: string;

  @ApiPropertyOptional()
  @IsString()
  @IsOptional()
  name?: string;

  @ApiPropertyOptional()
  @IsNumber()
  @Min(0)
  @IsOptional()
  minQuantity?: number;

  @ApiPropertyOptional()
  @IsNumber()
  @Min(0)
  @IsOptional()
  maxQuantity?: number;

  @ApiPropertyOptional()
  @IsNumber()
  @Min(1)
  @IsOptional()
  page?: number;

  @ApiPropertyOptional()
  @IsNumber()
  @Min(1)
  @IsOptional()
  limit?: number;
}

export class ReserveStockCommand {
  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  tenantId: string;

  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  itemId: string;

  @ApiProperty()
  @IsNumber()
  @Min(1)
  quantity: number;

  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  reference: string;
}

export class ReleaseStockCommand {
  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  tenantId: string;

  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  itemId: string;

  @ApiProperty()
  @IsNumber()
  @Min(1)
  quantity: number;

  @ApiProperty()
  @IsString()
  @IsNotEmpty()
  reference: string;
}
