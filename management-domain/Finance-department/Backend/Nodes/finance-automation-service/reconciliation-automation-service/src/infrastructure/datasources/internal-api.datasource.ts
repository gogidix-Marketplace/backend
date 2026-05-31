import { Injectable, Logger } from '@nestjs/common';
import axios, { AxiosInstance } from 'axios';
import { IDataSource, TransactionData, GetTransactionsParams, InternalAccount, Balance } from '../../domain/ports/output/data-source.interface';

@Injectable()
export class InternalApiDataSource implements IDataSource {
  private readonly logger = new Logger(InternalApiDataSource.name);
  private readonly client: AxiosInstance;

  constructor() {
    const baseURL = process.env.INTERNAL_API_URL || 'http://localhost:8081';
    const apiKey = process.env.INTERNAL_API_KEY;

    this.client = axios.create({
      baseURL,
      headers: {
        'Content-Type': 'application/json',
        ...(apiKey && { 'X-API-Key': apiKey }),
      },
      timeout: 30000,
    });
  }

  async getBankTransactions(params: GetTransactionsParams): Promise<TransactionData[]> {
    throw new Error('Use BankApiDataSource for bank transactions');
  }

  async getInternalTransactions(params: GetTransactionsParams): Promise<TransactionData[]> {
    try {
      this.logger.debug(`Fetching internal transactions for account: ${params.accountId}`);

      const response = await this.client.post('/transactions/search', {
        accountId: params.accountId,
        startDate: params.startDate?.toISOString(),
        endDate: params.endDate?.toISOString(),
        minAmount: params.minAmount,
        maxAmount: params.maxAmount,
        currency: params.currency,
        reference: params.reference,
        limit: params.limit || 1000,
        offset: params.offset || 0,
      });

      return response.data.transactions.map((tx: any) => this.mapToTransactionData(tx, params.tenantId));
    } catch (error) {
      this.logger.error('Failed to fetch internal transactions', error.stack);
      throw error;
    }
  }

  async getBankAccountById(accountId: string, tenantId: string): Promise<any> {
    throw new Error('Use BankApiDataSource for bank accounts');
  }

  async getInternalAccountById(accountId: string, tenantId: string): Promise<InternalAccount | null> {
    try {
      const response = await this.client.get(`/accounts/${accountId}`);

      return {
        id: response.data.id,
        tenantId,
        accountNumber: response.data.accountNumber,
        accountName: response.data.accountName,
        accountType: response.data.accountType,
        currency: response.data.currency,
        balance: response.data.balance,
        isActive: response.data.isActive,
      };
    } catch (error) {
      if (axios.isAxiosError(error) && error.response?.status === 404) {
        return null;
      }
      throw error;
    }
  }

  async getTransactionById(transactionId: string, source: 'bank' | 'internal', tenantId: string): Promise<TransactionData | null> {
    if (source !== 'internal') {
      throw new Error('Use BankApiDataSource for bank transactions');
    }

    try {
      const response = await this.client.get(`/transactions/${transactionId}`);
      return this.mapToTransactionData(response.data, tenantId);
    } catch (error) {
      if (axios.isAxiosError(error) && error.response?.status === 404) {
        return null;
      }
      throw error;
    }
  }

  async reconcileDifference(differenceId: string, resolution: any): Promise<void> {
    try {
      this.logger.debug(`Reconciling difference: ${differenceId}`);

      await this.client.post(`/differences/${differenceId}/reconcile`, {
        action: resolution.action,
        notes: resolution.notes,
        resolvedBy: resolution.resolvedBy,
        adjustmentAmount: resolution.adjustmentAmount,
      });

      this.logger.debug(`Successfully reconciled difference: ${differenceId}`);
    } catch (error) {
      this.logger.error(`Failed to reconcile difference: ${differenceId}`, error.stack);
      throw error;
    }
  }

  async getBalance(accountId: string, source: 'bank' | 'internal', asOfDate?: Date): Promise<Balance> {
    if (source !== 'internal') {
      throw new Error('Use BankApiDataSource for bank balances');
    }

    const response = await this.client.get(`/accounts/${accountId}/balance`, {
      params: { asOfDate: asOfDate?.toISOString() },
    });

    return {
      amount: response.data.amount,
      currency: response.data.currency,
      asOfDate: new Date(response.data.asOfDate),
    };
  }

  private mapToTransactionData(data: any, tenantId: string): TransactionData {
    return {
      id: data.id,
      amount: parseFloat(data.amount),
      currency: data.currency,
      date: new Date(data.date),
      type: data.type || 'CREDIT',
      reference: data.reference,
      description: data.description,
      counterparty: data.counterparty,
      category: data.category,
      status: data.status || 'COMPLETED',
      metadata: data.metadata,
      accountId: data.accountId,
      tenantId,
      createdAt: new Date(data.createdAt),
      updatedAt: new Date(data.updatedAt),
    };
  }
}
