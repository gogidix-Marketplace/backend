import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class IntentResponseDto {
  @ApiPropertyOptional()
  _id?: string;

  @ApiProperty()
  name: string;

  @ApiProperty()
  category: string;

  @ApiProperty()
  description: string;

  @ApiProperty({ type: [String] })
  trainingPhrases: string[];

  @ApiProperty({ type: [String] })
  responses: string[];

  @ApiPropertyOptional()
  language?: string;

  @ApiProperty()
  isActive: boolean;

  @ApiPropertyOptional()
  priority?: number;

  @ApiPropertyOptional()
  requiresHandoff?: boolean;
}
