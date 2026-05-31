import { IsString, IsNumber, validateOrReject } from 'class-validator';

export class AddPaymentToBatchRequestDto {
  @IsString()
  batchId!: string;

  @IsString()
  paymentId!: string;

  @IsNumber()
  amount!: number;

  @IsString()
  currency!: string;

  async validate(): Promise<void> {
    await validateOrReject(this);
  }
}
