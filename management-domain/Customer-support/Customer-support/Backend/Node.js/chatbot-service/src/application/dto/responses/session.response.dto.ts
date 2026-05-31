import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class SessionResponseDto {
  @ApiProperty()
  sessionId: string;

  @ApiProperty()
  status: string;

  @ApiProperty()
  language: string;

  @ApiProperty()
  startedAt: Date;

  @ApiPropertyOptional()
  lastActivityAt?: Date;

  @ApiPropertyOptional()
  messageCount?: number;

  @ApiPropertyOptional()
  turnCount?: number;

  @ApiPropertyOptional()
  tags?: string[];

  @ApiPropertyOptional()
  sentiment?: { score: number; label: string };

  @ApiPropertyOptional()
  assignedAgentId?: string;
}
