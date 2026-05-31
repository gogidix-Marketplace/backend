import { IsString, IsBoolean, IsOptional, IsNumber, IsArray, ValidateNested } from 'class-validator';
import { ApiPropertyOptional } from '@nestjs/swagger';
import { Type } from 'class-transformer';

export class UpdatePolicyDto {
  @ApiPropertyOptional() @IsOptional() @IsString() name?: string;
  @ApiPropertyOptional() @IsOptional() @IsString() description?: string;
  @ApiPropertyOptional() @IsOptional() @IsBoolean() enabled?: boolean;
  @ApiPropertyOptional() @IsOptional() @IsArray() scaleOutRules?: any[];
  @ApiPropertyOptional() @IsOptional() @IsArray() scaleInRules?: any[];
  @ApiPropertyOptional() @IsOptional() @IsNumber() cooldownPeriod?: number;
  @ApiPropertyOptional() @IsOptional() @IsNumber() minInstances?: number;
  @ApiPropertyOptional() @IsOptional() @IsNumber() maxInstances?: number;
}
