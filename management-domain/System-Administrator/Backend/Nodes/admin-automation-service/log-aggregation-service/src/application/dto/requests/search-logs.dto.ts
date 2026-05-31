import { IsString, IsOptional, IsArray, IsDateString, IsNumber, IsEnum } from 'class-validator';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';
import { Transform } from 'class-transformer';

export class SearchLogsDto {
  @ApiPropertyOptional() @IsOptional() @IsString() query?: string;
  @ApiPropertyOptional() @IsOptional() @IsArray() level?: string[];
  @ApiPropertyOptional() @IsOptional() @IsArray() sources?: string[];
  @ApiPropertyOptional() @IsOptional() @IsArray() services?: string[];
  @ApiPropertyOptional() @IsOptional() @IsArray() hosts?: string[];
  @ApiPropertyOptional() @IsOptional() @IsArray() tags?: string[];
  @ApiPropertyOptional() @IsOptional() @IsDateString() startTime?: string;
  @ApiPropertyOptional() @IsOptional() @IsDateString() endTime?: string;
  @ApiPropertyOptional() @IsOptional() @Transform(({ value }) => parseInt(value)) @IsNumber() limit?: number = 100;
  @ApiPropertyOptional() @IsOptional() @Transform(({ value }) => parseInt(value)) @IsNumber() offset?: number = 0;
  @ApiPropertyOptional() @IsOptional() @IsEnum(['asc', 'desc']) sort?: 'asc' | 'desc' = 'desc';
}
