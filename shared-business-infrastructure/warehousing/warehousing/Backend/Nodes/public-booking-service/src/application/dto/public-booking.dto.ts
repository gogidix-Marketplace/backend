import { 
  IsString, 
  IsUUID, 
  IsEmail, 
  IsEnum, 
  IsNumber, 
  IsDateString, 
  IsOptional, 
  IsPhoneNumber,
  IsBoolean,
  Min,
  IsArray,
  ValidateNested,
} from 'class-validator';
import { Type } from 'class-transformer';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export enum BookingStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  PAID = 'PAID',
  ACTIVE = 'ACTIVE',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
  EXPIRED = 'EXPIRED',
}

export enum StorageType {
  STANDARD = 'STANDARD',
  CLIMATE_CONTROLLED = 'CLIMATE_CONTROLLED',
  HAZARDOUS = 'HAZARDOUS',
  COLD_STORAGE = 'COLD_STORAGE',
  HIGH_SECURITY = 'HIGH_SECURITY',
}

export class CustomerInfoDto {
  @ApiProperty()
  @IsString()
  name: string;

  @ApiProperty()
  @IsEmail()
  email: string;

  @ApiPropertyOptional()
  @IsOptional()
  @IsPhoneNumber(null)
  phone?: string;

  @ApiPropertyOptional()
  @IsOptional()
  @IsString()
  company?: string;
}

export class BookingItemDto {
  @ApiProperty()
  @IsString()
  description: string;

  @ApiProperty()
  @IsNumber()
  @Min(0)
  quantity: number;

  @ApiProperty()
  @IsNumber()
  @Min(0)
  volume: number;

  @ApiProperty()
  @IsNumber()
  @Min(0)
  weight: number;

  @ApiPropertyOptional()
  @IsOptional()
  @IsString()
  category?: string;

  @ApiPropertyOptional()
  @IsOptional()
  @IsBoolean()
  hazardous?: boolean;
}

export class WarehousingBookingDto {
  @ApiProperty()
  @IsUUID()
  id: string;

  @ApiProperty()
  @IsString()
  tenantId: string;

  @ApiProperty()
  @IsString()
  referenceNumber: string;

  @ApiProperty()
  @IsUUID()
  warehouseId: string;

  @ApiPropertyOptional()
  @IsOptional()
  @IsString()
  warehouseName?: string;

  @ApiProperty({ enum: StorageType })
  @IsEnum(StorageType)
  storageType: StorageType;

  @ApiProperty()
  @ValidateNested()
  @Type(() => CustomerInfoDto)
  customer: CustomerInfoDto;

  @ApiProperty({ type: [BookingItemDto] })
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => BookingItemDto)
  items: BookingItemDto[];

  @ApiProperty()
  @IsDateString()
  startDate: string;

  @ApiProperty()
  @IsDateString()
  endDate: string;

  @ApiProperty()
  @IsNumber()
  @Min(0)
  totalVolume: number;

  @ApiProperty()
  @IsNumber()
  @Min(0)
  totalWeight: number;

  @ApiProperty()
  @IsNumber()
  @Min(0)
  durationDays: number;

  @ApiProperty()
  @IsNumber()
  @Min(0)
  baseAmount: number;

  @ApiProperty()
  @IsNumber()
  @Min(0)
  taxAmount: number;

  @ApiProperty()
  @IsNumber()
  @Min(0)
  totalAmount: number;

  @ApiPropertyOptional()
  @IsOptional()
  @IsString()
  currency?: string;

  @ApiProperty({ enum: BookingStatus })
  @IsEnum(BookingStatus)
  status: BookingStatus;

  @ApiPropertyOptional()
  @IsOptional()
  @IsString()
  specialInstructions?: string;

  @ApiProperty()
  @IsDateString()
  createdAt: string;

  @ApiProperty()
  @IsDateString()
  updatedAt: string;

  @ApiPropertyOptional()
  @IsOptional()
  @IsDateString()
  confirmedAt?: string;

  @ApiPropertyOptional()
  @IsOptional()
  @IsDateString()
  cancelledAt?: string;

  @ApiPropertyOptional()
  @IsOptional()
  @IsString()
  cancellationReason?: string;
}

export class CreateBookingCommand {
  @ApiProperty()
  @IsString()
  tenantId: string;

  @ApiProperty()
  @IsUUID()
  warehouseId: string;

  @ApiProperty({ enum: StorageType })
  @IsEnum(StorageType)
  storageType: StorageType;

  @ApiProperty()
  @ValidateNested()
  @Type(() => CustomerInfoDto)
  customer: CustomerInfoDto;

  @ApiProperty({ type: [BookingItemDto] })
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => BookingItemDto)
  items: BookingItemDto[];

  @ApiProperty()
  @IsDateString()
  startDate: string;

  @ApiProperty()
  @IsDateString()
  endDate: string;

  @ApiPropertyOptional()
  @IsOptional()
  @IsString()
  specialInstructions?: string;

  @ApiPropertyOptional()
  @IsOptional()
  @IsString()
  promotionalCode?: string;
}

export class ConfirmBookingCommand {
  @ApiProperty()
  @IsString()
  tenantId: string;

  @ApiProperty()
  @IsUUID()
  bookingId: string;

  @ApiPropertyOptional()
  @IsOptional()
  @IsString()
  paymentMethodId?: string;

  @ApiPropertyOptional()
  @IsOptional()
  @IsString()
  paymentIntentId?: string;
}

export class CancelBookingCommand {
  @ApiProperty()
  @IsString()
  tenantId: string;

  @ApiProperty()
  @IsUUID()
  bookingId: string;

  @ApiProperty()
  @IsString()
  cancellationReason: string;

  @ApiPropertyOptional()
  @IsOptional()
  @IsBoolean()
  requestRefund?: boolean;
}

export class BookingQueryDto {
  @ApiProperty()
  @IsString()
  tenantId: string;

  @ApiPropertyOptional()
  @IsOptional()
  @IsEnum(BookingStatus)
  status?: BookingStatus;

  @ApiPropertyOptional()
  @IsOptional()
  @IsDateString()
  startDateFrom?: string;

  @ApiPropertyOptional()
  @IsOptional()
  @IsDateString()
  startDateTo?: string;

  @ApiPropertyOptional()
  @IsOptional()
  @IsNumber()
  @Min(1)
  page?: number;

  @ApiPropertyOptional()
  @IsOptional()
  @IsNumber()
  @Min(1)
  limit?: number;
}

export class AvailabilityQueryDto {
  @ApiProperty()
  @IsString()
  tenantId: string;

  @ApiProperty()
  @IsUUID()
  warehouseId: string;

  @ApiProperty({ enum: StorageType })
  @IsEnum(StorageType)
  storageType: StorageType;

  @ApiProperty()
  @IsDateString()
  startDate: string;

  @ApiProperty()
  @IsDateString()
  endDate: string;

  @ApiProperty()
  @IsNumber()
  @Min(0)
  requiredVolume: number;
}

export class AvailabilityResultDto {
  @ApiProperty()
  available: boolean;

  @ApiProperty()
  availableVolume: number;

  @ApiProperty()
  totalVolume: number;

  @ApiProperty()
  utilizationPercentage: number;

  @ApiPropertyOptional()
  @IsOptional()
  alternativeDates?: Array<{
    startDate: string;
    endDate: string;
    availableVolume: number;
  }>;
}

export class StorageTypeDto {
  @ApiProperty()
  @IsString()
  code: string;

  @ApiProperty()
  @IsString()
  name: string;

  @ApiPropertyOptional()
  @IsOptional()
  @IsString()
  description?: string;

  @ApiProperty()
  @IsNumber()
  availableVolume: number;

  @ApiProperty()
  @IsNumber()
  totalVolume: number;

  @ApiProperty()
  @IsBoolean()
  available: boolean;

  @ApiProperty()
  @IsNumber()
  basePricePerDay: number;

  @ApiProperty()
  @IsNumber()
  pricePerCubicMeter: number;
}

export class PricingDto {
  @ApiProperty()
  @IsString()
  storageType: string;

  @ApiProperty()
  @IsString()
  name: string;

  @ApiProperty()
  @IsNumber()
  basePricePerDay: number;

  @ApiProperty()
  @IsNumber()
  pricePerCubicMeterPerDay: number;

  @ApiProperty()
  @IsNumber()
  minimumDays: number;

  @ApiProperty()
  @IsNumber()
  maximumDays: number;

  @ApiProperty()
  @IsNumber()
  discountWeekly: number;

  @ApiProperty()
  @IsNumber()
  discountMonthly: number;

  @ApiProperty()
  @IsNumber()
  taxRate: number;

  @ApiPropertyOptional()
  @IsOptional()
  @IsArray()
  @IsString({ each: true })
  features?: string[];
}

export class BookingEstimateDto {
  @ApiProperty()
  @IsNumber()
  baseAmount: number;

  @ApiProperty()
  @IsNumber()
  volumeCharge: number;

  @ApiProperty()
  @IsNumber()
  durationDiscount: number;

  @ApiProperty()
  @IsNumber()
  subtotal: number;

  @ApiProperty()
  @IsNumber()
  taxAmount: number;

  @ApiProperty()
  @IsNumber()
  totalAmount: number;

  @ApiProperty()
  @IsString()
  currency: string;

  @ApiProperty()
  breakdown: {
    dailyRate: number;
    volumeRate: number;
    days: number;
    volume: number;
    appliedDiscounts: string[];
  };
}


