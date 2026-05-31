import { PaymentBatch } from '../../../domain/models/payment-batch.entity';

export class BatchResponseDto {
  id!: string;
  name!: string;
  description!: string;
  status!: string;
  paymentIds!: string[];
  gateway!: string;
  scheduledAt?: Date;
  processingStartedAt?: Date;
  completedAt?: Date;
  totalAmount!: number;
  currency!: string;
  metadata?: Record<string, any>;
  priority!: number;
  maxConcurrent!: number;
  createdBy!: string;
  createdAt!: Date;
  updatedAt!: Date;
  tenantId!: string;
  version!: number;

  static fromEntity(batch: PaymentBatch): BatchResponseDto {
    const json = batch.toJSON();
    return {
      id: json.id,
      name: json.name,
      description: json.description,
      status: json.status,
      paymentIds: json.paymentIds,
      gateway: json.gateway,
      scheduledAt: json.scheduledAt ? new Date(json.scheduledAt) : undefined,
      processingStartedAt: json.processingStartedAt ? new Date(json.processingStartedAt) : undefined,
      completedAt: json.completedAt ? new Date(json.completedAt) : undefined,
      totalAmount: json.totalAmount.amount,
      currency: json.currency,
      metadata: json.metadata,
      priority: json.priority,
      maxConcurrent: json.maxConcurrent,
      createdBy: json.createdBy,
      createdAt: new Date(json.createdAt),
      updatedAt: new Date(json.updatedAt),
      tenantId: json.tenantId,
      version: json.version,
    };
  }

  static fromEntities(batches: PaymentBatch[]): BatchResponseDto[] {
    return batches.map((b) => BatchResponseDto.fromEntity(b));
  }
}
