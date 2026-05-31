import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class HealthResponseDto {
  @ApiProperty()
  success: boolean;

  @ApiProperty()
  status: string;

  @ApiProperty()
  timestamp: Date;

  @ApiPropertyOptional()
  services?: {
    mongodb: string;
    redis: string;
    knowledgeBase: string;
  };
}

export class SystemStatusResponseDto {
  @ApiProperty()
  success: boolean;

  @ApiProperty()
  timestamp: Date;

  @ApiPropertyOptional()
  system?: any;

  @ApiPropertyOptional()
  services?: any;
}
