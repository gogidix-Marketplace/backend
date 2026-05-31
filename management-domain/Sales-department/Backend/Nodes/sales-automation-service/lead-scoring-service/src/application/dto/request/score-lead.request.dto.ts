import { IsString, IsNotEmpty, IsOptional, IsObject, ValidateNested } from 'class-validator';
import { Type } from 'class-transformer';

export class ScoreLeadRequestDto {
  @IsString()
  @IsNotEmpty()
  leadId!: string;

  @IsString()
  @IsNotEmpty()
  tenantId!: string;

  @IsString()
  @IsOptional()
  scoreModelId?: string;

  @IsString()
  @IsOptional()
  variantId?: string;

  @IsObject()
  @IsOptional()
  leadData?: Record<string, any>;
}

export class BatchScoreLeadRequestDto {
  @IsString()
  @IsNotEmpty()
  tenantId!: string;

  @IsString()
  @IsNotEmpty()
  leadIds!: string[];

  @IsString()
  @IsOptional()
  scoreModelId?: string;
}

export class RescoreLeadsRequestDto {
  @IsString()
  @IsNotEmpty()
  tenantId!: string;

  @IsString()
  @IsOptional()
  scoreModelId?: string;

  @IsString()
  @IsOptional()
  fromDate?: string;
}
