import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class TranslationResponseDto {
  @ApiProperty()
  translatedText: string;

  @ApiProperty()
  sourceLanguage: string;

  @ApiProperty()
  targetLanguage: string;

  @ApiProperty()
  confidence: number;
}
