import { DomainException } from './domain.exception';

export class PaymentNotFoundException extends DomainException {
  constructor(paymentId: string) {
    super(
      `Payment with id ${paymentId} not found`,
      'PAYMENT_NOT_FOUND',
      { paymentId },
    );
  }
}

export class PaymentAlreadyProcessedException extends DomainException {
  constructor(paymentId: string) {
    super(
      `Payment ${paymentId} has already been processed`,
      'PAYMENT_ALREADY_PROCESSED',
      { paymentId },
    );
  }
}

export class PaymentValidationException extends DomainException {
  constructor(message: string, details?: Record<string, any>) {
    super(message, 'PAYMENT_VALIDATION_ERROR', details);
  }
}

export class PaymentGatewayException extends DomainException {
  constructor(message: string, gateway: string, details?: Record<string, any>) {
    super(message, 'PAYMENT_GATEWAY_ERROR', { gateway, ...details });
  }
}

export class InsufficientFundsException extends DomainException {
  constructor(amount: number, available: number) {
    super(
      `Insufficient funds. Requested: ${amount}, Available: ${available}`,
      'INSUFFICIENT_FUNDS',
      { amount, available },
    );
  }
}

export class PaymentBatchException extends DomainException {
  constructor(batchId: string, message: string) {
    super(message, 'PAYMENT_BATCH_ERROR', { batchId });
  }
}

export class InvalidPaymentRuleException extends DomainException {
  constructor(ruleId: string, reason: string) {
    super(`Invalid payment rule ${ruleId}: ${reason}`, 'INVALID_PAYMENT_RULE', {
      ruleId,
      reason,
    });
  }
}

export class PaymentScheduleException extends DomainException {
  constructor(scheduleId: string, message: string) {
    super(message, 'PAYMENT_SCHEDULE_ERROR', { scheduleId });
  }
}

export class VendorPaymentException extends DomainException {
  constructor(vendorId: string, message: string) {
    super(message, 'VENDOR_PAYMENT_ERROR', { vendorId });
  }
}
