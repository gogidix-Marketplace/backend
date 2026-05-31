import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';
import { IsString, IsArray, IsBoolean, IsOptional, IsInt, IsIn, Min, Max, Matches } from 'class-validator';

export class CreateIntentRequestDto {
  @ApiProperty()
  @IsString()
  @Matches(/^[a-z_][a-z0-9_]*$/)
  name: string;

  @ApiProperty()
  @IsIn(['greeting', 'faq', 'order_status', 'product_info', 'support', 'complaint', 'refund', 'billing', 'technical', 'shipping', 'returns', 'account', 'payment', 'general', 'escalation'])
  category: string;

  @ApiProperty()
  @IsString()
  description: string;

  @ApiProperty({ type: [String] })
  @IsArray()
  @IsString({ each: true })
  trainingPhrases: string[];

  @ApiProperty({ type: [String] })
  @IsArray()
  @IsString({ each: true })
  responses: string[];

  @ApiPropertyOptional({ default: 'en' })
  @IsOptional()
  @IsIn(['en', 'es', 'fr', 'de', 'pt', 'zh', 'ja', 'ar'])
  language?: string;

  @ApiPropertyOptional({ default: 0 })
  @IsOptional()
  @IsInt()
  @Min(0)
  @Max(100)
  priority?: number;

  @ApiPropertyOptional({ default: false })
  @IsOptional()
  @IsBoolean()
  requiresHandoff?: boolean;
}
