import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class JobStatusResponseDto {
  @ApiProperty() jobId!: string;
  @ApiProperty() state!: string;
  @ApiPropertyOptional() progress!: number;
  @ApiPropertyOptional() data: any;
  @ApiPropertyOptional() processedOn!: number;
  @ApiPropertyOptional() finishedOn!: number;
  @ApiPropertyOptional() failedReason!: string;
}
