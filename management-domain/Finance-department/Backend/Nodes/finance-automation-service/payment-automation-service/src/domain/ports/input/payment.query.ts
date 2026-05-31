import { Payment } from '../../models/payment.entity';
import { PaymentBatch } from '../../models/payment-batch.entity';
import { PaymentRule } from '../../models/payment-rule.entity';
import { VendorPayment } from '../../models/vendor-payment.entity';

export interface PaymentFilters {
  status?: string;
  vendorId?: string;
  gateway?: string;
  paymentMethod?: string;
  startDate?: Date;
  endDate?: Date;
  minAmount?: number;
  maxAmount?: number;
  batchId?: string;
  reconciliationStatus?: string;
}

export interface BatchFilters {
  status?: string;
  gateway?: string;
  startDate?: Date;
  endDate?: Date;
  createdBy?: string;
}

export interface RuleFilters {
  ruleType?: string;
  isActive?: boolean;
  createdBy?: string;
}

export interface VendorPaymentFilters {
  isActive?: boolean;
  vendorCode?: string;
  email?: string;
  paymentRating?: string;
}

export interface PaymentStatistics {
  totalPayments: number;
  totalAmount: number;
  statusBreakdown: Record<string, number>;
  gatewayBreakdown: Record<string, number>;
  averageProcessingTime: number;
  successRate: number;
}

export interface BatchStatistics {
  totalBatches: number;
  activeBatches: number;
  completedBatches: number;
  totalPaymentsProcessed: number;
  totalAmountProcessed: number;
}

export interface VendorPaymentStatistics {
  totalVendors: number;
  activeVendors: number;
  totalPaidAmount: number;
  averagePaymentAmount: number;
  ratingDistribution: Record<string, number>;
}

export interface PaymentQueryUseCase {
  findById(paymentId: string): Promise<Payment | null>;
  findByVendorId(vendorId: string): Promise<Payment[]>;
  findByBatchId(batchId: string): Promise<Payment[]>;
  findByStatus(status: string): Promise<Payment[]>;
  findByDateRange(startDate: Date, endDate: Date): Promise<Payment[]>;
  findPendingPayments(): Promise<Payment[]>;
  findScheduledPayments(date?: Date): Promise<Payment[]>;
  findFailedPayments(retryable?: boolean): Promise<Payment[]>;
  findPaymentsByReconciliationStatus(status: string): Promise<Payment[]>;
  search(filters: PaymentFilters): Promise<Payment[]>;
  getStatistics(filters?: PaymentFilters): Promise<PaymentStatistics>;
}

export interface PaymentBatchQueryUseCase {
  findById(batchId: string): Promise<PaymentBatch | null>;
  findPendingBatches(): Promise<PaymentBatch[]>;
  findProcessingBatches(): Promise<PaymentBatch[]>;
  findScheduledBatches(date?: Date): Promise<PaymentBatch[]>;
  findByStatus(status: string): Promise<PaymentBatch[]>;
  search(filters: BatchFilters): Promise<PaymentBatch[]>;
  getStatistics(filters?: BatchFilters): Promise<BatchStatistics>;
}

export interface PaymentRuleQueryUseCase {
  findById(ruleId: string): Promise<PaymentRule | null>;
  findActiveRules(): Promise<PaymentRule[]>;
  findByType(ruleType: string): Promise<PaymentRule[]>;
  search(filters: RuleFilters): Promise<PaymentRule[]>;
}

export interface VendorPaymentQueryUseCase {
  findById(vendorPaymentId: string): Promise<VendorPayment | null>;
  findByVendorId(vendorId: string): Promise<VendorPayment | null>;
  findAllActive(): Promise<VendorPayment[]>;
  findByAutoPayEnabled(): Promise<VendorPayment[]>;
  search(filters: VendorPaymentFilters): Promise<VendorPayment[]>;
  getStatistics(filters?: VendorPaymentFilters): Promise<VendorPaymentStatistics>;
}
