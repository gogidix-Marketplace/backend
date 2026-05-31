import { UUID } from 'crypto';

export interface IDataSource {
  getBankTransactions(params: GetTransactionsParams): Promise<TransactionData[]>;
  getInternalTransactions(params: GetTransactionsParams): Promise<TransactionData[]>;
  getBankAccountById(accountId: string, tenantId: string): Promise<BankAccount | null>;
  getInternalAccountById(accountId: string, tenantId: string): Promise<InternalAccount | null>;
  getTransactionById(transactionId: string, source: 'bank' | 'internal', tenantId: string): Promise<TransactionData | null>;
  reconcileDifference(differenceId: UUID, resolution: ResolutionData): Promise<void>;
  getBalance(accountId: string, source: 'bank' | 'internal', asOfDate?: Date): Promise<Balance>;
}

export interface GetTransactionsParams {
  tenantId: string;
  accountId?: string;
  startDate?: Date;
  endDate?: Date;
  minAmount?: number;
  maxAmount?: number;
  currency?: string;
  reference?: string;
  limit?: number;
  offset?: number;
}

export interface TransactionData {
  id: string;
  amount: number;
  currency: string;
  date: Date;
  type: 'CREDIT' | 'DEBIT';
  reference?: string;
  description?: string;
  counterparty?: string;
  category?: string;
  status: 'PENDING' | 'COMPLETED' | 'FAILED' | 'CANCELLED';
  metadata?: Record<string, unknown>;
  accountId: string;
  tenantId: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface BankAccount {
  id: string;
  tenantId: string;
  accountNumber: string;
  accountType: string;
  bankName: string;
  currency: string;
  balance: number;
  lastSyncAt: Date;
  isActive: boolean;
}

export interface InternalAccount {
  id: string;
  tenantId: string;
  accountNumber: string;
  accountName: string;
  accountType: string;
  currency: string;
  balance: number;
  isActive: boolean;
}

export interface Balance {
  amount: number;
  currency: string;
  asOfDate: Date;
}

export interface ResolutionData {
  action: 'AUTO_RESOLVE' | 'MANUALLY_RESOLVE';
  differenceId: UUID;
  notes?: string;
  resolvedBy?: string;
  adjustmentAmount?: number;
}
