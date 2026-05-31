import { IsEnum, IsString, IsOptional, IsObject, IsUUID, IsArray } from 'class-validator';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class ResolveDifferenceDto {
  @ApiProperty({ description: 'Resolution action', enum: ['AUTO_RESOLVE', 'MANUALLY_RESOLVE', 'IGNORE', 'ESCALATE'] })
  @IsEnum(['AUTO_RESOLVE', 'MANUALLY_RESOLVE', 'IGNORE', 'ESCALATE'])
  action: 'AUTO_RESOLVE' | 'MANUALLY_RESOLVE' | 'IGNORE' | 'ESCALATE';

  @ApiPropertyOptional({ description: 'Resolution notes' })
  @IsString()
  @IsOptional()
  notes?: string;

  @ApiPropertyOptional({ description: 'Additional resolution data' })
  @IsObject()
  @IsOptional()
  resolutionData?: Record<string, unknown>;
}

export class BulkResolveDifferencesDto {
  @ApiProperty({ description: 'Reconciliation ID' })
  @IsString()
  reconciliationId!: string;

  @ApiProperty({ description: 'Difference IDs to resolve', type: [String] })
  @IsArray()
  @IsUUID('4', { each: true })
  differenceIds: string[];

  @ApiProperty({ description: 'Resolution action', enum: ['AUTO_RESOLVE', 'MANUALLY_RESOLVE', 'IGNORE'] })
  @IsEnum(['AUTO_RESOLVE', 'MANUALLY_RESOLVE', 'IGNORE'])
  action: 'AUTO_RESOLVE' | 'MANUALLY_RESOLVE' | 'IGNORE';

  @ApiPropertyOptional({ description: 'Resolution notes' })
  @IsString()
  @IsOptional()
  notes?: string;
}
