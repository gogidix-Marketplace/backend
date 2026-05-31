import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';
import { IsOptional, IsString, IsIn, IsArray } from 'class-validator';

export class HandoffRequestDto {
  @ApiProperty()
  @IsString()
  reason: string;

  @ApiPropertyOptional({ default: 'medium' })
  @IsOptional()
  @IsIn(['low', 'medium', 'high', 'urgent'])
  priority?: 'low' | 'medium' | 'high' | 'urgent';

  @ApiPropertyOptional()
  @IsOptional()
  @IsArray()
  @IsString({ each: true })
  requiredSkills?: string[];
}
