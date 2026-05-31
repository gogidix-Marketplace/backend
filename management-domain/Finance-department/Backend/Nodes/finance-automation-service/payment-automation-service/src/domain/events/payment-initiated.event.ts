import { Payment } from '../models/payment.entity';

export interface PaymentInitiatedEventPayload {
  paymentId: string;
  amount: number;
  currency: string;
  vendorId: string;
  vendorName: string;
  paymentMethod: string;
  gateway: string;
  scheduledAt?: string;
  metadata?: Record<string, any>;
  tenantId: string;
  correlationId?: string;
}

export class PaymentInitiatedEvent {
  readonly eventType = 'PaymentInitiated';
  readonly timestamp: Date;
  readonly payload: PaymentInitiatedEventPayload;

  constructor(payload: PaymentInitiatedEventPayload) {
    this.timestamp = new Date();
    this.payload = payload;
  }

  static fromPayment(payment: Payment, correlationId?: string): PaymentInitiatedEvent {
    return new PaymentInitiatedEvent({
      paymentId: payment.id,
      amount: payment.amount.amount,
      currency: payment.currency,
      vendorId: payment.vendorId,
      vendorName: payment.vendorName,
      paymentMethod: payment.paymentMethod,
      gateway: payment.gateway,
      scheduledAt: payment.scheduledAt?.toISOString(),
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

  static fromJSON(json: Record<string, any>): PaymentInitiatedEvent {
    const event = new PaymentInitiatedEvent(json.payload);
    (event as any).timestamp = new Date(json.timestamp);
    return event;
  }
}
