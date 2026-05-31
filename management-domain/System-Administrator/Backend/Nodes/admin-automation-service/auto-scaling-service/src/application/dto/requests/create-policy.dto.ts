import { IsString, IsBoolean, IsOptional, IsNumber, IsArray, ValidateNested, IsEnum, Min, Max } from 'class-validator';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';
import { Type } from 'class-transformer';

export class ScalingRuleDto {
  @ApiProperty() @IsString() metric!: string;
  @ApiProperty() @IsString() operator!: string;
  @ApiProperty() @IsNumber() threshold!: number;
  @ApiProperty() @IsNumber() evaluationPeriods: number = 1;
  @ApiProperty() @IsNumber() adjustment!: number;
  @ApiProperty() @IsString() adjustmentType!: string;
}

export class CreatePolicyDto {
  @ApiProperty() @IsString() name!: string;
  @ApiProperty() @IsString() description!: string;
  @ApiProperty() @IsString() resourceId!: string;
  @ApiProperty() @IsString() cloudProvider!: string;
  @ApiPropertyOptional() @IsOptional() @IsBoolean() enabled?: boolean = true;
  @ApiPropertyOptional() @IsOptional() @IsArray() @ValidateNested({ each: true }) @Type(() => ScalingRuleDto) scaleOutRules?: ScalingRuleDto[];
  @ApiPropertyOptional() @IsOptional() @IsArray() @ValidateNested({ each: true }) @Type(() => ScalingRuleDto) scaleInRules?: ScalingRuleDto[];
  @ApiPropertyOptional() @IsOptional() @IsNumber() cooldownPeriod?: number = 300000;
  @ApiProperty() @IsNumber() @Min(0) minInstances!: number;
  @ApiProperty() @IsNumber() @Min(1) maxInstances!: number;
  @ApiProperty() @IsNumber() @Min(0) currentInstances!: number;
  @ApiProperty() @IsNumber() @Min(0) targetInstances!: number;
}
