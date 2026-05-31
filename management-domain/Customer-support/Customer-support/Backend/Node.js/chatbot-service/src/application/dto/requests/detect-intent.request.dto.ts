import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';
import { IsString, IsOptional, IsIn } from 'class-validator';

export class DetectIntentRequestDto {
  @ApiProperty()
  @IsString()
  message: string;

  @ApiPropertyOptional({ default: 'en' })
  @IsOptional()
  @IsIn(['en', 'es', 'fr', 'de', 'pt', 'zh', 'ja', 'ar'])
  language?: string;
}
