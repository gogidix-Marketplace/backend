import { IsString, IsDate, IsOptional, IsObject, validateOrReject } from 'class-validator';
import { Transform } from 'class-transformer';

export class UpdatePaymentRequestDto {
  @IsString()
  paymentId!: string;

  @IsOptional()
  @IsDate()
  @Transform(({ value }) => value ? new Date(value) : undefined)
  scheduledAt?: Date;

  @IsOptional()
  @IsObject()
  metadata?: Record<string, any>;

  async validate(): Promise<void> {
    await validateOrReject(this);
  }
}
