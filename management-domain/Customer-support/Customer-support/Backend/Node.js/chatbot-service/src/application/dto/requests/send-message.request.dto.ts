import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';
import { IsOptional, IsString, IsObject, IsIn, IsLength } from 'class-validator';

export class SendMessageRequestDto {
  @ApiPropertyOptional()
  @IsOptional()
  @IsString()
  sessionId?: string;

  @ApiProperty()
  @IsString()
  message: string;

  @ApiPropertyOptional({ default: 'en' })
  @IsOptional()
  @IsIn(['en', 'es', 'fr', 'de', 'pt', 'zh', 'ja', 'ar'])
  language?: string;

  @ApiPropertyOptional()
  @IsOptional()
  @IsObject()
  metadata?: Record<string, unknown>;

  @ApiPropertyOptional()
  @IsOptional()
  @IsString()
  customerId?: string;
}
