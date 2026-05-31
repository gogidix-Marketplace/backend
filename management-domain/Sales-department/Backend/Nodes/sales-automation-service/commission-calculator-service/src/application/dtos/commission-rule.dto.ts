import { IsString, IsNumber, IsOptional, IsEnum, IsArray, IsDateString, ValidateNested, IsObject } from 'class-validator';
import { Type } from 'class-transformer';
import { CommissionCalculationType, CommissionApplicationScope, AcceleratorType } from '../../domain/enums/commission-rule-type.enum';

/**
 * DTO for commission tier
 */
export class CommissionTierDto {
  @IsString()
  id!: string;

  @IsString()
  name!: string;

  @IsEnum(['REVENUE_BASED', 'QUOTA_BASED', 'MARGIN_BASED', 'PRODUCT_BASED'])
  tierType!: 'REVENUE_BASED' | 'QUOTA_BASED' | 'MARGIN_BASED' | 'PRODUCT_BASED';

  @IsNumber()
  minThreshold!: number;

  @IsNumber()
  @IsOptional()
  maxThreshold?: number;

  @IsNumber()
  commissionRate!: number;

  @IsNumber()
  @IsOptional()
  fixedAmount?: number;

  @IsNumber()
  @IsOptional()
  multiplier?: number;
}

/**
 * DTO for creating commission rule
 */
export class CreateCommissionRuleDto {
  @IsString()
  name!: string;

  @IsString()
  @IsOptional()
  description?: string;

  @IsEnum(CommissionCalculationType)
  calculationType!: CommissionCalculationType;

  @IsNumber()
  baseRate!: number;

  @IsEnum(CommissionApplicationScope)
  @IsOptional()
  applicationScope?: CommissionApplicationScope;

  @IsObject()
  @IsOptional()
  scopeFilters?: Record<string, string[]>;

  @IsEnum(AcceleratorType)
  @IsOptional()
  acceleratorType?: AcceleratorType;

  @IsNumber()
  @IsOptional()
  acceleratorThreshold?: number;

  @IsNumber()
  @IsOptional()
  acceleratorMultiplier?: number;

  @IsEnum(['NONE', 'AMOUNT', 'PERCENTAGE'])
  @IsOptional()
  capType?: 'NONE' | 'AMOUNT' | 'PERCENTAGE';

  @IsNumber()
  @IsOptional()
  capValue?: number;

  @IsDateString()
  @IsOptional()
  effectiveDate?: string;

  @IsDateString()
  @IsOptional()
  expirationDate?: string;

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => CommissionTierDto)
  @IsOptional()
  tiers?: CommissionTierDto[];
}

/**
 * DTO for updating commission rule
 */
export class UpdateCommissionRuleDto {
  @IsString()
  @IsOptional()
  name?: string;

  @IsString()
  @IsOptional()
  description?: string;

  @IsNumber()
  @IsOptional()
  baseRate?: number;

  @IsEnum(CommissionApplicationScope)
  @IsOptional()
  applicationScope?: CommissionApplicationScope;

  @IsObject()
  @IsOptional()
  scopeFilters?: Record<string, string[]>;

  @IsEnum(AcceleratorType)
  @IsOptional()
  acceleratorType?: AcceleratorType;

  @IsNumber()
  @IsOptional()
  acceleratorThreshold?: number;

  @IsNumber()
  @IsOptional()
  acceleratorMultiplier?: number;

  @IsEnum(['NONE', 'AMOUNT', 'PERCENTAGE'])
  @IsOptional()
  capType?: 'NONE' | 'AMOUNT' | 'PERCENTAGE';

  @IsNumber()
  @IsOptional()
  capValue?: number;

  @IsDateString()
  @IsOptional()
  expirationDate?: string;
}

/**
 * DTO for setting product rate
 */
export class SetProductRateDto {
  @IsString()
  productId!: string;

  @IsNumber()
  rate!: number;
}

/**
 * DTO for setting customer rate
 */
export class SetCustomerRateDto {
  @IsString()
  customerId!: string;

  @IsNumber()
  rate!: number;
}

/**
 * DTO for adding tier
 */
export class AddTierDto {
  @IsString()
  id!: string;

  @IsString()
  name!: string;

  @IsEnum(['REVENUE_BASED', 'QUOTA_BASED', 'MARGIN_BASED', 'PRODUCT_BASED'])
  tierType!: 'REVENUE_BASED' | 'QUOTA_BASED' | 'MARGIN_BASED' | 'PRODUCT_BASED';

  @IsNumber()
  minThreshold!: number;

  @IsNumber()
  @IsOptional()
  maxThreshold?: number;

  @IsNumber()
  commissionRate!: number;

  @IsNumber()
  @IsOptional()
  fixedAmount?: number;

  @IsNumber()
  @IsOptional()
  multiplier?: number;
}
