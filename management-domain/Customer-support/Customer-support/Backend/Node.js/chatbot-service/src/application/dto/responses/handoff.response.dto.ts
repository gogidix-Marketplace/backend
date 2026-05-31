import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class HandoffStatusResponseDto {
  @ApiProperty()
  status: string;

  @ApiPropertyOptional()
  agentId?: string;

  @ApiPropertyOptional()
  queuePosition?: number;

  @ApiPropertyOptional()
  estimatedWaitTime?: number;
}
