import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class BotResponseDto {
  @ApiProperty()
  type: string;

  @ApiPropertyOptional()
  text?: string;

  @ApiPropertyOptional()
  quickReplies?: Array<{ title: string; payload: string }>;

  @ApiProperty()
  confidence: number;

  @ApiPropertyOptional()
  intent?: string;

  @ApiProperty()
  language: string;

  @ApiPropertyOptional()
  metadata?: Record<string, unknown>;
}
