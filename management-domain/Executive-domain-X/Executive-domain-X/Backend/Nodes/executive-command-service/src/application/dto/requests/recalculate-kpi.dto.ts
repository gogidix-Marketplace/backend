import { IsNumber, IsOptional, IsObject } from 'class-validator';

export class RecalculateKpiDto {
  @IsNumber()
  value!: number;

  @IsOptional()
  @IsObject()
  metadata?: Record<string, unknown>;
}
