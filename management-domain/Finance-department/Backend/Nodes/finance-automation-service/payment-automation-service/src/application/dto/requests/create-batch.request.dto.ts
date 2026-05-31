import { IsString, IsNumber, IsOptional, IsDate, IsObject, validateOrReject } from 'class-validator';
import { Transform } from 'class-transformer';

export class CreateBatchRequestDto {
  @IsString()
  name!: string;

  @IsOptional()
  @IsString()
  description?: string;

  @IsString()
  gateway!: string;

  @IsString()
  currency!: string;

  @IsOptional()
  @IsDate()
  @Transform(({ value }) => value ? new Date(value) : undefined)
  scheduledAt?: Date;

  @IsOptional()
  @IsNumber()
  priority?: number;

  @IsOptional()
  @IsNumber()
  maxConcurrent?: number;

  @IsOptional()
  @IsObject()
  metadata?: Record<string, any>;

  async validate(): Promise<void> {
    await validateOrReject(this);
  }
}
