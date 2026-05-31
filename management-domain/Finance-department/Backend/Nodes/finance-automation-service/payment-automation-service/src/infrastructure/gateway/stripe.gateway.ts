import { Injectable, Logger } from '@nestjs/common';
import Stripe from 'stripe';
import {
  PaymentGateway,
  PaymentGatewayRequest,
  PaymentGatewayResponse,
  PaymentGatewayBalance,
  PaymentVerificationResponse,
} from '../../domain/ports/output/payment-gateway.interface';
import { Money } from '../../domain/models/value-objects/money.value-object';

@Injectable()
export class StripeGateway implements PaymentGateway {
  private readonly logger = new Logger(StripeGateway.name);
  private readonly stripe: Stripe;

  constructor(private readonly apiKey: string) {
    this.stripe = new Stripe(this.apiKey, {
      apiVersion: '2023-10-16',
      typescript: true,
    });
  }

  getName(): string {
    return 'stripe';
  }

  async processPayment(request: PaymentGatewayRequest): Promise<PaymentGatewayResponse> {
    try {
      this.logger.log(`Processing Stripe payment of ${request.amount.toFormattedString()}`);

      // Create a transfer or payout based on the payment method
      const paymentIntent = await this.stripe.transfers.create({
        amount: Math.round(request.amount.amount * 100), // Stripe uses cents
        currency: request.amount.getCurrency().toLowerCase(),
        destination: request.vendorAccountDetails.accountNumber || 'acct_test',
        metadata: {
          ...request.metadata,
          vendorId: request.vendorAccountDetails.vendorId,
          vendorName: request.vendorAccountDetails.vendorName,
          reference: request.reference,
        },
        description: request.description,
      });

      this.logger.log(`Stripe payment successful: ${paymentIntent.id}`);

      return {
        success: true,
        transactionId: paymentIntent.id,
        reference: paymentIntent.balance_transaction as string,
        status: this.mapStripeStatus('paid'),
        gatewayResponse: paymentIntent,
        processedAt: new Date(),
      };
    } catch (error: any) {
      this.logger.error(`Stripe payment failed: ${error.message}`);

      return {
        success: false,
        errorCode: error.code,
        errorMessage: error.message,
        gatewayResponse: error,
      };
    }
  }

  async verifyPayment(transactionId: string): Promise<PaymentVerificationResponse> {
    try {
      const transfer = await this.stripe.transfers.retrieve(transactionId);
      // Access status property - Stripe Transfer has status but types may vary
      const transferData = transfer as any;

      return {
        isValid: transferData.status === 'paid' || transferData.status === 'complete',
        status: this.mapStripeStatus(transferData.status),
        verifiedAt: new Date(),
      };
    } catch (error: any) {
      this.logger.error(`Stripe verification failed: ${error.message}`);

      return {
        isValid: false,
        verifiedAt: new Date(),
      };
    }
  }

  async cancelPayment(transactionId: string, reason?: string): Promise<PaymentGatewayResponse> {
    try {
      // Stripe transfers generally cannot be cancelled once created
      // This would depend on the specific Stripe product being used
      this.logger.warn(`Stripe payment cancellation requested for ${transactionId}`);

      return {
        success: false,
        errorMessage: 'Stripe transfers cannot be cancelled after creation',
        gatewayResponse: null,
      };
    } catch (error: any) {
      return {
        success: false,
        errorCode: error.code,
        errorMessage: error.message,
      };
    }
  }

  async refundPayment(
    transactionId: string,
    amount?: Money,
    reason?: string,
  ): Promise<PaymentGatewayResponse> {
    try {
      const refundAmount = amount
        ? Math.round(amount.amount * 100)
        : undefined;

      const refund = await this.stripe.refunds.create({
        charge: transactionId,
        amount: refundAmount,
        reason: reason === 'duplicate' ? 'duplicate' : reason === 'fraudulent' ? 'fraudulent' : undefined,
        metadata: { reason },
      });

      this.logger.log(`Stripe refund successful: ${refund.id}`);

      return {
        success: true,
        transactionId: refund.id,
        status: refund.status,
        gatewayResponse: refund,
      };
    } catch (error: any) {
      this.logger.error(`Stripe refund failed: ${error.message}`);

      return {
        success: false,
        errorCode: error.code,
        errorMessage: error.message,
      };
    }
  }

  async getBalance(): Promise<PaymentGatewayBalance> {
    try {
      const balance = await this.stripe.balance.retrieve();

      const available = balance.available[0];
      return {
        available: Money.create(available.amount / 100, available.currency.toUpperCase()),
        currency: available.currency.toUpperCase(),
      };
    } catch (error: any) {
      this.logger.error(`Failed to retrieve Stripe balance: ${error.message}`);
      throw error;
    }
  }

  async getTransactionStatus(transactionId: string): Promise<string> {
    try {
      const transfer = await this.stripe.transfers.retrieve(transactionId);
      const transferData = transfer as any;
      return this.mapStripeStatus(transferData.status);
    } catch (error: any) {
      this.logger.error(`Failed to get transaction status: ${error.message}`);
      return 'UNKNOWN';
    }
  }

  async isHealthy(): Promise<boolean> {
    try {
      await this.stripe.balance.retrieve();
      return true;
    } catch {
      return false;
    }
  }

  private mapStripeStatus(status?: string): string {
    const statusMap: Record<string, string> = {
      paid: 'COMPLETED',
      succeeded: 'COMPLETED',
      pending: 'PENDING',
      processing: 'PROCESSING',
      failed: 'FAILED',
      canceled: 'CANCELLED',
      in_transit: 'PROCESSING',
    };

    return statusMap[status || ''] || 'UNKNOWN';
  }
}
