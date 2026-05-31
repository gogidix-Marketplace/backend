import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';
import { IsString, IsInt, IsBoolean, IsOptional, Min, Max } from 'class-validator';

export class FeedbackRequestDto {
  @ApiProperty()
  @IsString()
  sessionId: string;

  @ApiProperty({ minimum: 1, maximum: 5 })
  @IsInt()
  @Min(1)
  @Max(5)
  rating: number;

  @ApiPropertyOptional()
  @IsOptional()
  @IsString()
  comment?: string;

  @ApiProperty()
  @IsBoolean()
  resolved: boolean;
}
