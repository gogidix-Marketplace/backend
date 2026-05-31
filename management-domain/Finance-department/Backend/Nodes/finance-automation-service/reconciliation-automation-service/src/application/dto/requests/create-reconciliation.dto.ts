import { IsString, IsOptional, IsEnum, IsDateString, IsArray, IsUUID, IsObject, IsNotEmpty, IsNumber } from 'class-validator';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class CreateReconciliationDto {
  @ApiProperty({ description: 'Reconciliation name' })
  @IsString()
  @IsNotEmpty()
  name!: string;

  @ApiPropertyOptional({ description: 'Reconciliation description' })
  @IsString()
  @IsOptional()
  description?: string;

  @ApiProperty({ description: 'Data source type', enum: ['BANK', 'INTERNAL', 'BOTH'] })
  @IsEnum(['BANK', 'INTERNAL', 'BOTH'])
  dataSourceType: 'BANK' | 'INTERNAL' | 'BOTH';

  @ApiPropertyOptional({ description: 'Bank account ID' })
  @IsString()
  @IsOptional()
  bankAccountId?: string;

  @ApiPropertyOptional({ description: 'Internal account ID' })
  @IsString()
  @IsOptional()
  internalAccountId?: string;

  @ApiPropertyOptional({ description: 'Reconciliation rule IDs', type: [String] })
  @IsArray()
  @IsString({ each: true })
  @IsOptional()
  ruleIds?: string[];

  @ApiPropertyOptional({ description: 'Start date for transaction range' })
  @IsDateString()
  @IsOptional()
  startDate?: string;

  @ApiPropertyOptional({ description: 'End date for transaction range' })
  @IsDateString()
  @IsOptional()
  endDate?: string;

  @ApiPropertyOptional({ description: 'Additional metadata' })
  @IsObject()
  @IsOptional()
  metadata?: Record<string, unknown>;
}

export class ScheduleReconciliationDto {
  @ApiProperty({ description: 'Reconciliation name' })
  @IsString()
  @IsNotEmpty()
  name!: string;

  @ApiPropertyOptional({ description: 'Reconciliation description' })
  @IsString()
  @IsOptional()
  description?: string;

  @ApiProperty({ description: 'Data source type', enum: ['BANK', 'INTERNAL', 'BOTH'] })
  @IsEnum(['BANK', 'INTERNAL', 'BOTH'])
  dataSourceType: 'BANK' | 'INTERNAL' | 'BOTH';

  @ApiProperty({ description: 'Cron expression for scheduling' })
  @IsString()
  @IsNotEmpty()
  scheduleExpression!: string;

  @ApiPropertyOptional({ description: 'Timezone' })
  @IsString()
  @IsOptional()
  timezone?: string;

  @ApiPropertyOptional({ description: 'Bank account ID' })
  @IsString()
  @IsOptional()
  bankAccountId?: string;

  @ApiPropertyOptional({ description: 'Internal account ID' })
  @IsString()
  @IsOptional()
  internalAccountId?: string;

  @ApiPropertyOptional({ description: 'Reconciliation rule IDs', type: [String] })
  @IsArray()
  @IsString({ each: true })
  @IsOptional()
  ruleIds?: string[];

  @ApiPropertyOptional({ description: 'Additional metadata' })
  @IsObject()
  @IsOptional()
  metadata?: Record<string, unknown>;
}

export class SearchReconciliationsDto {
  @ApiPropertyOptional({ description: 'Search term' })
  @IsString()
  @IsOptional()
  searchTerm?: string;

  @ApiPropertyOptional({ description: 'Status filter', enum: ['PENDING', 'RUNNING', 'COMPLETED', 'FAILED', 'PARTIAL'] })
  @IsEnum(['PENDING', 'RUNNING', 'COMPLETED', 'FAILED', 'PARTIAL'])
  @IsOptional()
  status?: string;

  @ApiPropertyOptional({ description: 'Data source type filter', enum: ['BANK', 'INTERNAL', 'BOTH'] })
  @IsEnum(['BANK', 'INTERNAL', 'BOTH'])
  @IsOptional()
  dataSourceType?: string;

  @ApiPropertyOptional({ description: 'Start date filter' })
  @IsDateString()
  @IsOptional()
  startDate?: string;

  @ApiPropertyOptional({ description: 'End date filter' })
  @IsDateString()
  @IsOptional()
  endDate?: string;

  @ApiPropertyOptional({ description: 'Page number' })
  @IsNumber()
  @IsOptional()
  page?: number;

  @ApiPropertyOptional({ description: 'Page size' })
  @IsNumber()
  @IsOptional()
  limit?: number;
}
