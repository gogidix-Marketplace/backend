import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';
import { IsString, IsOptional } from 'class-validator';

export class TranslateRequestDto {
  @ApiProperty()
  @IsString()
  text: string;

  @ApiProperty()
  @IsString()
  targetLanguage: string;

  @ApiPropertyOptional()
  @IsOptional()
  @IsString()
  sourceLanguage?: string;
}
