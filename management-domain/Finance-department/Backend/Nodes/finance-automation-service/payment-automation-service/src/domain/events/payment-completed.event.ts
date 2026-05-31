import { Payment } from '../models/payment.entity';

export interface PaymentCompletedEventPayload {
  paymentId: string;
  amount: number;
  currency: string;
  vendorId: string;
  vendorName: string;
  paymentMethod: string;
  gateway: string;
  externalReference: string;
  gatewayResponse?: any;
  processedAt: string;
  completedAt: string;
  metadata?: Record<string, any>;
  tenantId: string;
  correlationId?: string;
}

export class PaymentCompletedEvent {
  readonly eventType = 'PaymentCompleted';
  readonly timestamp: Date;
  readonly payload: PaymentCompletedEventPayload;

  constructor(payload: PaymentCompletedEventPayload) {
    this.timestamp = new Date();
    this.payload = payload;
  }

  static fromPayment(
    payment: Payment,
    externalReference: string,
    gatewayResponse?: any,
    correlationId?: string,
  ): PaymentCompletedEvent {
    if (!payment.completedAt) {
      throw new Error('Payment must be completed to create PaymentCompletedEvent');
    }

    return new PaymentCompletedEvent({
      paymentId: payment.id,
      amount: payment.amount.amount,
      currency: payment.currency,
      vendorId: payment.vendorId,
      vendorName: payment.vendorName,
      paymentMethod: payment.paymentMethod,
      gateway: payment.gateway,
      externalReference,
      gatewayResponse,
      processedAt: payment.processedAt?.toISOString() || new Date().toISOString(),
      completedAt: payment.completedAt.toISOString(),
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

  static fromJSON(json: Record<string, any>): PaymentCompletedEvent {
    const event = new PaymentCompletedEvent(json.payload);
    (event as any).timestamp = new Date(json.timestamp);
    return event;
  }
}
