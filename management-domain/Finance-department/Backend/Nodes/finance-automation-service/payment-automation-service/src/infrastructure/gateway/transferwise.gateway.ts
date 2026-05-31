import { Injectable, Logger } from '@nestjs/common';
import axios, { AxiosInstance } from 'axios';
import {
  PaymentGateway,
  PaymentGatewayRequest,
  PaymentGatewayResponse,
  PaymentGatewayBalance,
  PaymentVerificationResponse,
} from '../../domain/ports/output/payment-gateway.interface';
import { Money } from '../../domain/models/value-objects/money.value-object';

interface TransferWiseConfig {
  apiKey: string;
  baseUrl: string;
  profileId: string;
}

interface TransferWiseTransferResponse {
  id: string;
  status: string;
  reference?: string;
  sourceAmount?: number;
  targetAmount?: number;
  sourceCurrency?: string;
  targetCurrency?: string;
  created?: string;
  completed?: string;
}

@Injectable()
export class TransferWiseGateway implements PaymentGateway {
  private readonly logger = new Logger(TransferWiseGateway.name);
  private readonly client: AxiosInstance;
  private readonly profileId: string;

  constructor(config: TransferWiseConfig) {
    this.client = axios.create({
      baseURL: config.baseUrl,
      headers: {
        'Authorization': `Bearer ${config.apiKey}`,
        'Content-Type': 'application/json',
      },
    });
    this.profileId = config.profileId;
  }

  getName(): string {
    return 'transferwise';
  }

  async processPayment(request: PaymentGatewayRequest): Promise<PaymentGatewayResponse> {
    try {
      this.logger.log(`Processing TransferWise payment of ${request.amount.toFormattedString()}`);

      // First, create a quote
      const quoteResponse = await this.client.post('/v3/quotes', {
        profileId: this.profileId,
        source: request.amount.getCurrency(),
        target: request.amount.getCurrency(),
        sourceAmount: request.amount.amount,
        type: 'BALANCE',
      });

      const quoteId = quoteResponse.data.id;

      // Create a recipient account
      const recipientResponse = await this.client.post('/v1/accounts', {
        profile: this.profileId,
        accountHolderName: request.vendorAccountDetails.vendorName,
        currency: request.amount.getCurrency(),
        type: 'iban',
        details: {
          legalType: 'PRIVATE',
          iban: request.vendorAccountDetails.accountNumber,
        },
      });

      const recipientId = recipientResponse.data.id;

      // Create the transfer
      const transferResponse = await this.client.post('/v1/transfers', {
        targetAccount: recipientId,
        quote: quoteId,
        reference: request.reference || request.vendorAccountDetails.vendorId,
        details: {
          reference: request.reference,
          sourceOfFunds: 'COMPANY',
        },
        customerTransactionId: request.reference,
      });

      this.logger.log(`TransferWise payment initiated: ${transferResponse.data.id}`);

      return {
        success: true,
        transactionId: transferResponse.data.id,
        reference: transferResponse.data.reference,
        status: this.mapTransferWiseStatus(transferResponse.data.status),
        gatewayResponse: transferResponse.data,
        processedAt: new Date(),
      };
    } catch (error: any) {
      this.logger.error(`TransferWise payment failed: ${error.message}`);

      return {
        success: false,
        errorCode: error.response?.data?.code || error.code,
        errorMessage: error.response?.data?.message || error.message,
        gatewayResponse: error.response?.data,
      };
    }
  }

  async verifyPayment(transactionId: string): Promise<PaymentVerificationResponse> {
    try {
      const response = await this.client.get<TransferWiseTransferResponse>(
        `/v1/transfers/${transactionId}`,
      );

      const transfer = response.data;

      return {
        isValid: transfer.status === 'outgoing_payment_sent' || transfer.status === 'funds_converted',
        status: this.mapTransferWiseStatus(transfer.status),
        verifiedAt: new Date(),
      };
    } catch (error: any) {
      this.logger.error(`TransferWise verification failed: ${error.message}`);

      return {
        isValid: false,
        verifiedAt: new Date(),
      };
    }
  }

  async cancelPayment(transactionId: string, reason?: string): Promise<PaymentGatewayResponse> {
    try {
      await this.client.put(`/v1/transfers/${transactionId}/cancel`, {
        reason,
      });

      this.logger.log(`TransferWise payment cancelled: ${transactionId}`);

      return {
        success: true,
        transactionId,
        status: 'CANCELLED',
      };
    } catch (error: any) {
      this.logger.error(`TransferWise cancellation failed: ${error.message}`);

      return {
        success: false,
        errorCode: error.response?.data?.code || error.code,
        errorMessage: error.response?.data?.message || error.message,
      };
    }
  }

  async refundPayment(
    transactionId: string,
    amount?: Money,
    reason?: string,
  ): Promise<PaymentGatewayResponse> {
    try {
      // TransferWise doesn't support traditional refunds
      // Refunds would need to be processed as new transfers in the opposite direction
      this.logger.warn(`TransferWise refund requested for ${transactionId}`);

      return {
        success: false,
        errorMessage: 'TransferWise does not support direct refunds. Process a new transfer instead.',
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

  async getBalance(): Promise<PaymentGatewayBalance> {
    try {
      const response = await this.client.get('/v1/profiles/' + this.profileId + '/balances');
      const balance = response.data[0];

      return {
        available: Money.create(
          parseFloat(balance.amount.value),
          balance.amount.currency.toUpperCase(),
        ),
        currency: balance.amount.currency.toUpperCase(),
      };
    } catch (error: any) {
      this.logger.error(`Failed to retrieve TransferWise balance: ${error.message}`);
      throw error;
    }
  }

  async getTransactionStatus(transactionId: string): Promise<string> {
    try {
      const response = await this.client.get<TransferWiseTransferResponse>(
        `/v1/transfers/${transactionId}`,
      );

      return this.mapTransferWiseStatus(response.data.status);
    } catch (error: any) {
      this.logger.error(`Failed to get transaction status: ${error.message}`);
      return 'UNKNOWN';
    }
  }

  async isHealthy(): Promise<boolean> {
    try {
      await this.client.get('/v1/me');
      return true;
    } catch {
      return false;
    }
  }

  private mapTransferWiseStatus(status: string): string {
    const statusMap: Record<string, string> = {
      incoming_payment_waiting: 'PENDING',
      processing: 'PROCESSING',
      funds_converted: 'COMPLETED',
      outgoing_payment_sent: 'COMPLETED',
      bounce: 'FAILED',
      cancelled: 'CANCELLED',
      lost: 'FAILED',
      failed: 'FAILED',
    };

    return statusMap[status] || 'UNKNOWN';
  }
}
