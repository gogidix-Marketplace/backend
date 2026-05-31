import { IsString, IsUUID, IsOptional, IsEnum, IsNumber, IsArray, ValidateNested, IsBoolean, Min, IsNotEmpty } from 'class-validator';
import { Type } from 'class-transformer';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export enum PackOrderStatus {
  PENDING = 'pending',
  ASSIGNED = 'assigned',
  IN_PROGRESS = 'in_progress',
  COMPLETED = 'completed',
  CANCELLED = 'cancelled',
}

export enum PackageStatus {
  CREATED = 'created',
  SEALED = 'sealed',
  LABELED = 'labeled',
  SHIPPED = 'shipped',
}

export enum QualityCheckStatus {
  PENDING = 'pending',
  PASSED = 'passed',
  FAILED = 'failed',
}

export enum BoxType {
  SMALL = 'small',
  MEDIUM = 'medium',
  LARGE = 'large',
  XLARGE = 'xlarge',
  CUSTOM = 'custom',
}

export class PackItemDto {
  @ApiProperty({ description: 'Item ID' })
  @IsUUID()
  itemId: string;

  @ApiProperty({ description: 'Product ID' })
  @IsUUID()
  productId: string;

  @ApiProperty({ description: 'Product SKU' })
  @IsString()
  sku: string;

  @ApiProperty({ description: 'Product name' })
  @IsString()
  productName: string;

  @ApiProperty({ description: 'Quantity to pack' })
  @IsNumber()
  @Min(1)
  quantity: number;

  @ApiPropertyOptional({ description: 'Quantity packed' })
  @IsNumber()
  @IsOptional()
  quantityPacked?: number;

  @ApiPropertyOptional({ description: 'Weight in grams' })
  @IsNumber()
  @IsOptional()
  weight?: number;

  @ApiPropertyOptional({ description: 'Whether item is fragile' })
  @IsBoolean()
  @IsOptional()
  isFragile?: boolean;

  @ApiPropertyOptional({ description: 'Whether item is packed' })
  @IsBoolean()
  @IsOptional()
  isPacked?: boolean;
}

export class PackageDto {
  @ApiPropertyOptional({ description: 'Package ID' })
  @IsUUID()
  @IsOptional()
  id?: string;

  @ApiProperty({ description: 'Package number' })
  @IsString()
  packageNumber: string;

  @ApiProperty({ description: 'Box type' })
  @IsEnum(BoxType)
  boxType: BoxType;

  @ApiPropertyOptional({ description: 'Package length in cm' })
  @IsNumber()
  @IsOptional()
  length?: number;

  @ApiPropertyOptional({ description: 'Package width in cm' })
  @IsNumber()
  @IsOptional()
  width?: number;

  @ApiPropertyOptional({ description: 'Package height in cm' })
  @IsNumber()
  @IsOptional()
  height?: number;

  @ApiPropertyOptional({ description: 'Package weight in grams' })
  @IsNumber()
  @IsOptional()
  weight?: number;

  @ApiPropertyOptional({ description: 'Package status' })
  @IsEnum(PackageStatus)
  @IsOptional()
  status?: PackageStatus;

  @ApiPropertyOptional({ description: 'Tracking number' })
  @IsString()
  @IsOptional()
  trackingNumber?: string;

  @ApiPropertyOptional({ description: 'Items in package' })
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => PackItemDto)
  @IsOptional()
  items?: PackItemDto[];
}

export class QualityCheckDto {
  @ApiPropertyOptional({ description: 'Check ID' })
  @IsUUID()
  @IsOptional()
  id?: string;

  @ApiProperty({ description: 'Check type' })
  @IsString()
  checkType: string;

  @ApiProperty({ description: 'Check status' })
  @IsEnum(QualityCheckStatus)
  status: QualityCheckStatus;

  @ApiPropertyOptional({ description: 'Check score (0-100)' })
  @IsNumber()
  @IsOptional()
  score?: number;

  @ApiPropertyOptional({ description: 'Check notes' })
  @IsString()
  @IsOptional()
  notes?: string;

  @ApiPropertyOptional({ description: 'Checked by user ID' })
  @IsUUID()
  @IsOptional()
  checkedBy?: string;

  @ApiPropertyOptional({ description: 'Check timestamp' })
  @IsOptional()
  checkedAt?: Date;
}

export class PackingMaterialDto {
  @ApiProperty({ description: 'Material ID' })
  @IsUUID()
  id: string;

  @ApiProperty({ description: 'Material name' })
  @IsString()
  name: string;

  @ApiProperty({ description: 'Material type' })
  @IsString()
  type: string;

  @ApiPropertyOptional({ description: 'Material quantity' })
  @IsNumber()
  @IsOptional()
  quantity?: number;

  @ApiPropertyOptional({ description: 'Material unit' })
  @IsString()
  @IsOptional()
  unit?: string;
}

export class PackOrderDto {
  @ApiProperty({ description: 'Pack order ID' })
  @IsUUID()
  id: string;

  @ApiProperty({ description: 'Tenant ID' })
  @IsUUID()
  tenantId: string;

  @ApiProperty({ description: 'Pack order number' })
  @IsString()
  packOrderNumber: string;

  @ApiProperty({ description: 'Pick order ID' })
  @IsUUID()
  pickOrderId: string;

  @ApiPropertyOptional({ description: 'Order ID' })
  @IsUUID()
  @IsOptional()
  orderId?: string;

  @ApiProperty({ description: 'Pack order status' })
  @IsEnum(PackOrderStatus)
  status: PackOrderStatus;

  @ApiPropertyOptional({ description: 'Assigned packer ID' })
  @IsUUID()
  @IsOptional()
  packerId?: string;

  @ApiPropertyOptional({ description: 'Assigned packer name' })
  @IsString()
  @IsOptional()
  packerName?: string;

  @ApiProperty({ description: 'Items to pack' })
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => PackItemDto)
  items: PackItemDto[];

  @ApiPropertyOptional({ description: 'Packages created' })
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => PackageDto)
  @IsOptional()
  packages?: PackageDto[];

  @ApiPropertyOptional({ description: 'Quality checks' })
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => QualityCheckDto)
  @IsOptional()
  qualityChecks?: QualityCheckDto[];

  @ApiPropertyOptional({ description: 'Packing materials used' })
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => PackingMaterialDto)
  @IsOptional()
  materialsUsed?: PackingMaterialDto[];

  @ApiPropertyOptional({ description: 'Total weight in grams' })
  @IsNumber()
  @IsOptional()
  totalWeight?: number;

  @ApiPropertyOptional({ description: 'Priority level (1-5)' })
  @IsNumber()
  @IsOptional()
  priority?: number;

  @ApiPropertyOptional({ description: 'Special instructions' })
  @IsString()
  @IsOptional()
  specialInstructions?: string;

  @ApiPropertyOptional({ description: 'Created at' })
  @IsOptional()
  createdAt?: Date;

  @ApiPropertyOptional({ description: 'Updated at' })
  @IsOptional()
  updatedAt?: Date;

  @ApiPropertyOptional({ description: 'Started at' })
  @IsOptional()
  startedAt?: Date;

  @ApiPropertyOptional({ description: 'Completed at' })
  @IsOptional()
  completedAt?: Date;
}

export class CreatePackOrderCommand {
  @ApiProperty({ description: 'Tenant ID' })
  @IsUUID()
  @IsNotEmpty()
  tenantId: string;

  @ApiProperty({ description: 'Pick order ID' })
  @IsUUID()
  @IsNotEmpty()
  pickOrderId: string;

  @ApiPropertyOptional({ description: 'Order ID' })
  @IsUUID()
  @IsOptional()
  orderId?: string;

  @ApiProperty({ description: 'Items to pack' })
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => PackItemDto)
  items: PackItemDto[];

  @ApiPropertyOptional({ description: 'Priority level (1-5)' })
  @IsNumber()
  @IsOptional()
  priority?: number;

  @ApiPropertyOptional({ description: 'Special instructions' })
  @IsString()
  @IsOptional()
  specialInstructions?: string;
}

export class AssignPackerCommand {
  @ApiProperty({ description: 'Tenant ID' })
  @IsUUID()
  @IsNotEmpty()
  tenantId: string;

  @ApiProperty({ description: 'Pack order ID' })
  @IsUUID()
  @IsNotEmpty()
  packOrderId: string;

  @ApiProperty({ description: 'Packer user ID' })
  @IsUUID()
  @IsNotEmpty()
  packerId: string;

  @ApiPropertyOptional({ description: 'Packer name' })
  @IsString()
  @IsOptional()
  packerName?: string;
}

export class PackItemCommand {
  @ApiProperty({ description: 'Tenant ID' })
  @IsUUID()
  @IsNotEmpty()
  tenantId: string;

  @ApiProperty({ description: 'Pack order ID' })
  @IsUUID()
  @IsNotEmpty()
  packOrderId: string;

  @ApiProperty({ description: 'Item ID' })
  @IsUUID()
  @IsNotEmpty()
  itemId: string;

  @ApiProperty({ description: 'Quantity packed' })
  @IsNumber()
  @Min(1)
  quantityPacked: number;

  @ApiPropertyOptional({ description: 'Package ID to add item to' })
  @IsUUID()
  @IsOptional()
  packageId?: string;

  @ApiPropertyOptional({ description: 'Quality check data' })
  @ValidateNested()
  @Type(() => QualityCheckDto)
  @IsOptional()
  qualityCheck?: QualityCheckDto;
}

export class CreatePackageCommand {
  @ApiProperty({ description: 'Tenant ID' })
  @IsUUID()
  @IsNotEmpty()
  tenantId: string;

  @ApiProperty({ description: 'Pack order ID' })
  @IsUUID()
  @IsNotEmpty()
  packOrderId: string;

  @ApiProperty({ description: 'Box type' })
  @IsEnum(BoxType)
  boxType: BoxType;

  @ApiPropertyOptional({ description: 'Package length in cm' })
  @IsNumber()
  @IsOptional()
  length?: number;

  @ApiPropertyOptional({ description: 'Package width in cm' })
  @IsNumber()
  @IsOptional()
  width?: number;

  @ApiPropertyOptional({ description: 'Package height in cm' })
  @IsOptional()
  height?: number;

  @ApiPropertyOptional({ description: 'Package weight in grams' })
  @IsNumber()
  @IsOptional()
  weight?: number;
}

export class QualityCheckCommand {
  @ApiProperty({ description: 'Tenant ID' })
  @IsUUID()
  @IsNotEmpty()
  tenantId: string;

  @ApiProperty({ description: 'Pack order ID' })
  @IsUUID()
  @IsNotEmpty()
  packOrderId: string;

  @ApiProperty({ description: 'Quality check data' })
  @ValidateNested()
  @Type(() => QualityCheckDto)
  qualityCheck: QualityCheckDto;
}

export class PackQueryDto {
  @ApiPropertyOptional({ description: 'Tenant ID' })
  @IsUUID()
  @IsOptional()
  tenantId?: string;

  @ApiPropertyOptional({ description: 'Status filter' })
  @IsEnum(PackOrderStatus)
  @IsOptional()
  status?: PackOrderStatus;

  @ApiPropertyOptional({ description: 'Packer ID filter' })
  @IsUUID()
  @IsOptional()
  packerId?: string;

  @ApiPropertyOptional({ description: 'Page number' })
  @IsNumber()
  @IsOptional()
  page?: number;

  @ApiPropertyOptional({ description: 'Page size' })
  @IsNumber()
  @IsOptional()
  limit?: number;

  @ApiPropertyOptional({ description: 'Search term' })
  @IsString()
  @IsOptional()
  search?: string;

  @ApiPropertyOptional({ description: 'From date' })
  @IsOptional()
  fromDate?: Date;

  @ApiPropertyOptional({ description: 'To date' })
  @IsOptional()
  toDate?: Date;
}

export class ShippingLabelDto {
  @ApiProperty({ description: 'Label ID' })
  @IsUUID()
  id: string;

  @ApiProperty({ description: 'Tracking number' })
  @IsString()
  trackingNumber: string;

  @ApiProperty({ description: 'Carrier' })
  @IsString()
  carrier: string;

  @ApiPropertyOptional({ description: 'Label URL' })
  @IsString()
  @IsOptional()
  labelUrl?: string;

  @ApiPropertyOptional({ description: 'Label data (base64)' })
  @IsString()
  @IsOptional()
  labelData?: string;

  @ApiProperty({ description: 'Created at' })
  createdAt: Date;
}
