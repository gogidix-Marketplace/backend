import { IsString, IsOptional } from 'class-validator';
import { ApiPropertyOptional } from '@nestjs/swagger';

export class VerifyMatchDto {
  @ApiPropertyOptional({ description: 'Verification notes' })
  @IsString()
  @IsOptional()
  notes?: string;
}
