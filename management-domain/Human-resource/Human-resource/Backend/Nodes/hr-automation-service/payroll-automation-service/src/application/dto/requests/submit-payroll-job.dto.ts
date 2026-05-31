import { IsString, IsArray, IsDateString, IsOptional, IsNumber, Min, Max } from 'class-validator';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class SubmitPayrollJobDto {
  @ApiProperty() @IsString() tenantId!: string;
  @ApiProperty() @IsArray() @IsString({ each: true }) employeeIds: string[];
  @ApiProperty() @IsDateString() periodStart!: string;
  @ApiProperty() @IsDateString() periodEnd!: string;
  @ApiPropertyOptional() @IsOptional() @IsNumber() @Min(1) @Max(10) priority?: number;
  @ApiPropertyOptional() @IsOptional() @IsNumber() delay?: number;
}
