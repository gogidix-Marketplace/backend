import { Payment } from '../models/payment.entity';
import { PaymentBatch } from '../models/payment-batch.entity';
import { PaymentRule } from '../models/payment-rule.entity';
import { VendorPayment } from '../models/vendor-payment.entity';

export interface PaymentRepository {
  save(payment: Payment): Promise<Payment>;
  findById(id: string): Promise<Payment | null>;
  findByTenantId(tenantId: string): Promise<Payment[]>;
  findByVendorId(vendorId: string): Promise<Payment[]>;
  findByBatchId(batchId: string): Promise<Payment[]>;
  findByStatus(status: string, tenantId?: string): Promise<Payment[]>;
  findPending(tenantId?: string): Promise<Payment[]>;
  findScheduled(date?: Date, tenantId?: string): Promise<Payment[]>;
  findFailed(retryable?: boolean, tenantId?: string): Promise<Payment[]>;
  findAll(filters?: any, tenantId?: string): Promise<Payment[]>;
  delete(id: string): Promise<void>;
  exists(id: string): Promise<boolean>;
  count(filters?: any, tenantId?: string): Promise<number>;
}

export interface PaymentBatchRepository {
  save(batch: PaymentBatch): Promise<PaymentBatch>;
  findById(id: string): Promise<PaymentBatch | null>;
  findByTenantId(tenantId: string): Promise<PaymentBatch[]>;
  findByStatus(status: string, tenantId?: string): Promise<PaymentBatch[]>;
  findPending(tenantId?: string): Promise<PaymentBatch[]>;
  findProcessing(tenantId?: string): Promise<PaymentBatch[]>;
  findScheduled(date?: Date, tenantId?: string): Promise<PaymentBatch[]>;
  findAll(filters?: any, tenantId?: string): Promise<PaymentBatch[]>;
  delete(id: string): Promise<void>;
  exists(id: string): Promise<boolean>;
  count(filters?: any, tenantId?: string): Promise<number>;
}

export interface PaymentRuleRepository {
  save(rule: PaymentRule): Promise<PaymentRule>;
  findById(id: string): Promise<PaymentRule | null>;
  findByTenantId(tenantId: string): Promise<PaymentRule[]>;
  findActive(tenantId?: string): Promise<PaymentRule[]>;
  findByType(ruleType: string, tenantId?: string): Promise<PaymentRule[]>;
  findAll(filters?: any, tenantId?: string): Promise<PaymentRule[]>;
  delete(id: string): Promise<void>;
  exists(id: string): Promise<boolean>;
  count(filters?: any, tenantId?: string): Promise<number>;
}

export interface VendorPaymentRepository {
  save(vendorPayment: VendorPayment): Promise<VendorPayment>;
  findById(id: string): Promise<VendorPayment | null>;
  findByVendorId(vendorId: string): Promise<VendorPayment | null>;
  findByTenantId(tenantId: string): Promise<VendorPayment[]>;
  findActive(tenantId?: string): Promise<VendorPayment[]>;
  findByAutoPayEnabled(tenantId?: string): Promise<VendorPayment[]>;
  findByVendorCode(vendorCode: string, tenantId?: string): Promise<VendorPayment | null>;
  findAll(filters?: any, tenantId?: string): Promise<VendorPayment[]>;
  delete(id: string): Promise<void>;
  exists(id: string): Promise<boolean>;
  count(filters?: any, tenantId?: string): Promise<number>;
}
