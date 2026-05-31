import { IsNumber, IsString, IsEnum, IsOptional, IsDate, IsObject, validateOrReject } from 'class-validator';
import { PaymentMethod } from '../../../domain/models/enums/payment-method.enum';
import { Transform } from 'class-transformer';

export class CreatePaymentRequestDto {
  @IsNumber()
  amount!: number;

  @IsString()
  currency!: string;

  @IsString()
  vendorId!: string;

  @IsString()
  vendorName!: string;

  @IsString()
  accountId!: string;

  @IsEnum(PaymentMethod)
  paymentMethod!: PaymentMethod;

  @IsString()
  gateway!: string;

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
