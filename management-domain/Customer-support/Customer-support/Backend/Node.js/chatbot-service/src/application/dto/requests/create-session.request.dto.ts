import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';
import { IsOptional, IsString, IsObject, IsIn } from 'class-validator';

export class CreateSessionRequestDto {
  @ApiPropertyOptional()
  @IsOptional()
  @IsString()
  customerId?: string;

  @ApiPropertyOptional({ default: 'en' })
  @IsOptional()
  @IsIn(['en', 'es', 'fr', 'de', 'pt', 'zh', 'ja', 'ar'])
  language?: string;

  @ApiPropertyOptional()
  @IsOptional()
  @IsObject()
  metadata?: Record<string, unknown>;
}
