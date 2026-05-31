import { IsString, IsOptional, IsNumber, IsBoolean, IsObject, validateOrReject } from 'class-validator';

export interface VendorBankDetailsDto {
  bankName: string;
  accountNumber: string;
  routingNumber?: string;
  swiftCode?: string;
  iban?: string;
  accountType?: 'CHECKING' | 'SAVINGS';
  address?: {
    street: string;
    city: string;
    state: string;
    postalCode: string;
    country: string;
  };
}

export interface PaymentSettingsDto {
  paymentTerms?: number;
  autoPayEnabled?: boolean;
  preferredPaymentMethod?: string;
  minimumPaymentAmount?: number;
  maximumPaymentAmount?: number;
  requireApprovalAbove?: number;
  currency?: string;
}

export class CreateVendorPaymentRequestDto {
  @IsString()
  vendorId!: string;

  @IsString()
  vendorName!: string;

  @IsString()
  vendorCode!: string;

  @IsString()
  email!: string;

  @IsOptional()
  @IsString()
  phone?: string;

  @IsOptional()
  @IsString()
  taxId?: string;

  @IsObject()
  bankDetails!: VendorBankDetailsDto;

  @IsString()
  currency!: string;

  @IsOptional()
  @IsNumber()
  paymentTerms?: number;

  @IsOptional()
  @IsBoolean()
  autoPayEnabled?: boolean;

  @IsOptional()
  @IsString()
  preferredPaymentMethod?: string;

  async validate(): Promise<void> {
    await validateOrReject(this);
  }
}

export class UpdateVendorPaymentRequestDto {
  @IsString()
  vendorPaymentId!: string;

  @IsOptional()
  @IsString()
  vendorName?: string;

  @IsOptional()
  @IsString()
  email?: string;

  @IsOptional()
  @IsString()
  phone?: string;

  @IsOptional()
  @IsObject()
  bankDetails?: Partial<VendorBankDetailsDto>;

  @IsOptional()
  @IsObject()
  paymentSettings?: Partial<PaymentSettingsDto>;

  async validate(): Promise<void> {
    await validateOrReject(this);
  }
}
