import { Payment } from '../../../domain/models/payment.entity';

export class PaymentResponseDto {
  id!: string;
  status!: string;
  amount!: number;
  currency!: string;
  paymentMethod!: string;
  gateway!: string;
  vendorId!: string;
  vendorName!: string;
  accountId!: string;
  scheduledAt?: Date;
  processedAt?: Date;
  completedAt?: Date;
  failedAt?: Date;
  cancelledAt?: Date;
  metadata?: Record<string, any>;
  externalReference?: string;
  failureReason?: string;
  retryCount!: number;
  maxRetries!: number;
  batchId?: string;
  reconciliationStatus!: string;
  reconciledAt?: Date;
  createdAt!: Date;
  updatedAt!: Date;
  tenantId!: string;
  version!: number;

  static fromEntity(payment: Payment): PaymentResponseDto {
    const json = payment.toJSON();
    return {
      id: json.id,
      status: json.status,
      amount: json.amount.amount,
      currency: json.currency,
      paymentMethod: json.paymentMethod,
      gateway: json.gateway,
      vendorId: json.vendorId,
      vendorName: json.vendorName,
      accountId: json.accountId,
      scheduledAt: json.scheduledAt ? new Date(json.scheduledAt) : undefined,
      processedAt: json.processedAt ? new Date(json.processedAt) : undefined,
      completedAt: json.completedAt ? new Date(json.completedAt) : undefined,
      failedAt: json.failedAt ? new Date(json.failedAt) : undefined,
      cancelledAt: json.cancelledAt ? new Date(json.cancelledAt) : undefined,
      metadata: json.metadata,
      externalReference: json.externalReference,
      failureReason: json.failureReason,
      retryCount: json.retryCount,
      maxRetries: json.maxRetries,
      batchId: json.batchId,
      reconciliationStatus: json.reconciliationStatus,
      reconciledAt: json.reconciledAt ? new Date(json.reconciledAt) : undefined,
      createdAt: new Date(json.createdAt),
      updatedAt: new Date(json.updatedAt),
      tenantId: json.tenantId,
      version: json.version,
    };
  }

  static fromEntities(payments: Payment[]): PaymentResponseDto[] {
    return payments.map((p) => PaymentResponseDto.fromEntity(p));
  }
}
