import { PaymentBatch } from '../models/payment-batch.entity';
import { BatchStatus } from '../models/enums/batch-status.enum';

export interface BatchProcessedEventPayload {
  batchId: string;
  batchName: string;
  status: BatchStatus;
  totalPayments: number;
  totalAmount: number;
  currency: string;
  gateway: string;
  completedPayments?: number;
  failedPayments?: number;
  processedAt?: string;
  completedAt?: string;
  summary?: {
    totalPayments: number;
    totalAmount: number;
    completedPayments: number;
    failedPayments: number;
    pendingPayments: number;
  };
  metadata?: Record<string, any>;
  tenantId: string;
  correlationId?: string;
}

export class BatchProcessedEvent {
  readonly eventType = 'BatchProcessed';
  readonly timestamp: Date;
  readonly payload: BatchProcessedEventPayload;

  constructor(payload: BatchProcessedEventPayload) {
    this.timestamp = new Date();
    this.payload = payload;
  }

  static fromBatch(
    batch: PaymentBatch,
    completionRate: number,
    failureRate: number,
    correlationId?: string,
  ): BatchProcessedEvent {
    const summary = batch.getSummary(completionRate, failureRate);

    return new BatchProcessedEvent({
      batchId: batch.id,
      batchName: batch.name,
      status: batch.status,
      totalPayments: batch.getPaymentCount(),
      totalAmount: batch.totalAmount.amount,
      currency: batch.currency,
      gateway: batch.gateway,
      completedPayments: summary.completedPayments,
      failedPayments: summary.failedPayments,
      processedAt: batch.processingStartedAt?.toISOString(),
      completedAt: batch.completedAt?.toISOString(),
      summary: {
        totalPayments: summary.totalPayments,
        totalAmount: summary.totalAmount.amount,
        completedPayments: summary.completedPayments,
        failedPayments: summary.failedPayments,
        pendingPayments: summary.pendingPayments,
      },
      metadata: batch.metadata,
      tenantId: batch.tenantId,
      correlationId,
    });
  }

  toJSON(): Record<string, any> {
    return {
      eventType: this.eventType,
      timestamp: this.timestamp.toISOString(),
      payload: this.payload,
    };
  }

  static fromJSON(json: Record<string, any>): BatchProcessedEvent {
    const event = new BatchProcessedEvent(json.payload);
    (event as any).timestamp = new Date(json.timestamp);
    return event;
  }
}
