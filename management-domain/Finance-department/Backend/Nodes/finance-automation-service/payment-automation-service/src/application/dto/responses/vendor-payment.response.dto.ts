import { VendorPayment } from '../../../domain/models/vendor-payment.entity';

export class VendorPaymentResponseDto {
  id!: string;
  vendorId!: string;
  vendorName!: string;
  vendorCode!: string;
  email!: string;
  phone?: string;
  taxId?: string;
  bankDetails!: any;
  paymentSettings!: any;
  isActive!: boolean;
  totalPaidAmount!: number;
  totalPaymentCount!: number;
  lastPaymentDate?: Date;
  nextPaymentDate?: Date;
  metadata?: Record<string, any>;
  creditScore?: number;
  paymentRating?: string;
  createdAt!: Date;
  updatedAt!: Date;
  tenantId!: string;
  version!: number;

  static fromEntity(vendorPayment: VendorPayment): VendorPaymentResponseDto {
    const json = vendorPayment.toJSON();
    return {
      id: json.id,
      vendorId: json.vendorId,
      vendorName: json.vendorName,
      vendorCode: json.vendorCode,
      email: json.email,
      phone: json.phone,
      taxId: json.taxId,
      bankDetails: json.bankDetails,
      paymentSettings: json.paymentSettings,
      isActive: json.isActive,
      totalPaidAmount: json.totalPaidAmount.amount,
      totalPaymentCount: json.totalPaymentCount,
      lastPaymentDate: json.lastPaymentDate ? new Date(json.lastPaymentDate) : undefined,
      nextPaymentDate: json.nextPaymentDate ? new Date(json.nextPaymentDate) : undefined,
      metadata: json.metadata,
      creditScore: json.creditScore,
      paymentRating: json.paymentRating,
      createdAt: new Date(json.createdAt),
      updatedAt: new Date(json.updatedAt),
      tenantId: json.tenantId,
      version: json.version,
    };
  }

  static fromEntities(vendorPayments: VendorPayment[]): VendorPaymentResponseDto[] {
    return vendorPayments.map((v) => VendorPaymentResponseDto.fromEntity(v));
  }
}
