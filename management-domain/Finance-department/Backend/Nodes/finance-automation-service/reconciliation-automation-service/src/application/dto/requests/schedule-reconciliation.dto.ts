import { IsString, IsOptional, IsEnum, IsNotEmpty, IsObject } from 'class-validator';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

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

  @ApiPropertyOptional({ description: 'Bank account ID' })
  @IsString()
  @IsOptional()
  bankAccountId?: string;

  @ApiPropertyOptional({ description: 'Internal account ID' })
  @IsString()
  @IsOptional()
  internalAccountId?: string;

  @ApiPropertyOptional({ description: 'Reconciliation rule IDs', type: [String] })
  @IsString({ each: true })
  @IsOptional()
  ruleIds?: string[];

  @ApiProperty({ description: 'Cron expression for scheduling (e.g., "0 0 * * *" for daily at midnight)' })
  @IsString()
  @IsNotEmpty()
  scheduleExpression!: string;

  @ApiPropertyOptional({ description: 'Timezone for the schedule', example: 'UTC' })
  @IsString()
  @IsOptional()
  timezone?: string;

  @ApiPropertyOptional({ description: 'Additional metadata' })
  @IsObject()
  @IsOptional()
  metadata?: Record<string, unknown>;
}
