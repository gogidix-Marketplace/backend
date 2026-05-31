import { IsString, IsNumber, IsOptional, IsEnum, IsArray, ValidateNested, IsDateString } from 'class-validator';
import { Type } from 'class-transformer';
import { CommissionStatus } from '../../domain/enums/commission-status.enum';
import { CommissionSplitDto } from './create-commission.dto';

/**
 * DTO for updating a commission
 */
export class UpdateCommissionDto {
  @IsString()
  @IsOptional()
  salesRepName?: string;

  @IsNumber()
  @IsOptional()
  salesAmount?: number;

  @IsNumber()
  @IsOptional()
  commissionRate?: number;

  @IsEnum(CommissionStatus)
  @IsOptional()
  status?: CommissionStatus;

  @IsDateString()
  @IsOptional()
  settlementDate?: string;

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => CommissionSplitDto)
  @IsOptional()
  splits?: CommissionSplitDto[];
}

/**
 * DTO for applying adjustment
 */
export class ApplyAdjustmentDto {
  @IsNumber()
  amount!: number;

  @IsString()
  reason!: string;
}

/**
 * DTO for clawback
 */
export class ClawbackDto {
  @IsNumber()
  amount!: number;

  @IsString()
  reason!: string;
}

/**
 * DTO for approval
 */
export class ApproveCommissionDto {
  @IsString()
  @IsOptional()
  approvedBy?: string;
}

/**
 * DTO for payment processing
 */
export class ProcessPaymentDto {
  @IsString()
  paymentMethod!: string;

  @IsString()
  @IsOptional()
  reference?: string;
}
