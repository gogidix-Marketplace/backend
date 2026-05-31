import { Money } from '../../models/value-objects/money.value-object';
import { PaymentMethod } from '../../models/enums/payment-method.enum';

export interface PaymentGatewayRequest {
  amount: Money;
  paymentMethod: PaymentMethod;
  vendorAccountDetails: any;
  metadata?: Record<string, any>;
  description?: string;
  reference?: string;
}

export interface PaymentGatewayResponse {
  success: boolean;
  transactionId?: string;
  reference?: string;
  status?: string;
  gatewayResponse?: any;
  errorCode?: string;
  errorMessage?: string;
  processedAt?: Date;
  fee?: Money;
}

export interface PaymentGatewayBalance {
  available: Money;
  currency: string;
}

export interface PaymentVerificationResponse {
  isValid: boolean;
  status?: string;
  amount?: Money;
  verifiedAt?: Date;
}

export interface PaymentGateway {
  getName(): string;
  processPayment(request: PaymentGatewayRequest): Promise<PaymentGatewayResponse>;
  verifyPayment(transactionId: string): Promise<PaymentVerificationResponse>;
  cancelPayment(transactionId: string, reason?: string): Promise<PaymentGatewayResponse>;
  refundPayment(transactionId: string, amount?: Money, reason?: string): Promise<PaymentGatewayResponse>;
  getBalance(): Promise<PaymentGatewayBalance>;
  getTransactionStatus(transactionId: string): Promise<string>;
  isHealthy(): Promise<boolean>;
}

export interface PaymentGatewayFactory {
  createGateway(gatewayType: string, config: any): PaymentGateway;
  getAvailableGateways(): string[];
}
