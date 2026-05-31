import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class IntentDetectionResponseDto {
  @ApiProperty()
  intent: string;

  @ApiProperty()
  category: string;

  @ApiProperty()
  confidence: number;

  @ApiProperty()
  confidenceLevel: string;

  @ApiProperty({ type: [Object] })
  entities: Array<{ type: string; value: string; confidence: number }>;

  @ApiProperty()
  sentiment: { score: number; label: string };

  @ApiProperty()
  language: string;

  @ApiProperty()
  requiresHandoff: boolean;

  @ApiProperty({ type: [String] })
  suggestedResponses: string[];
}
