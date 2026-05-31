import { IsString, IsBoolean, IsOptional, IsObject } from 'class-validator';
import { ApiPropertyOptional } from '@nestjs/swagger';

export class UpdateSourceDto {
  @ApiPropertyOptional() @IsOptional() @IsString() name?: string;
  @ApiPropertyOptional() @IsOptional() @IsString() description?: string;
  @ApiPropertyOptional() @IsOptional() @IsBoolean() enabled?: boolean;
  @ApiPropertyOptional() @IsOptional() @IsObject() config?: Record<string, unknown>;
  @ApiPropertyOptional() @IsOptional() @IsObject() parsing?: Record<string, unknown>;
  @ApiPropertyOptional() @IsOptional() @IsObject() retention?: Record<string, unknown>;
}
