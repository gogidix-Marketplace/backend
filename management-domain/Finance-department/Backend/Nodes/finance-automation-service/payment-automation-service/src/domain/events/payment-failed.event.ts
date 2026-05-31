import { Payment } from '../models/payment.entity';

export interface PaymentFailedEventPayload {
  paymentId: string;
  amount: number;
  currency: string;
  vendorId: string;
  vendorName: string;
  paymentMethod: string;
  gateway: string;
  failureReason: string;
  gatewayResponse?: any;
  retryCount: number;
  canRetry: boolean;
  failedAt: string;
  metadata?: Record<string, any>;
  tenantId: string;
  correlationId?: string;
}

export class PaymentFailedEvent {
  readonly eventType = 'PaymentFailed';
  readonly timestamp: Date;
  readonly payload: PaymentFailedEventPayload;

  constructor(payload: PaymentFailedEventPayload) {
    this.timestamp = new Date();
    this.payload = payload;
  }

  static fromPayment(
    payment: Payment,
    failureReason: string,
    gatewayResponse?: any,
    correlationId?: string,
  ): PaymentFailedEvent {
    if (!payment.failedAt) {
      throw new Error('Payment must be failed to create PaymentFailedEvent');
    }

    return new PaymentFailedEvent({
      paymentId: payment.id,
      amount: payment.amount.amount,
      currency: payment.currency,
      vendorId: payment.vendorId,
      vendorName: payment.vendorName,
      paymentMethod: payment.paymentMethod,
      gateway: payment.gateway,
      failureReason,
      gatewayResponse,
      retryCount: payment.retryCount,
      canRetry: payment.canRetry(),
      failedAt: payment.failedAt.toISOString(),
      metadata: payment.metadata,
      tenantId: payment.tenantId,
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

  static fromJSON(json: Record<string, any>): PaymentFailedEvent {
    const event = new PaymentFailedEvent(json.payload);
    (event as any).timestamp = new Date(json.timestamp);
    return event;
  }
}
