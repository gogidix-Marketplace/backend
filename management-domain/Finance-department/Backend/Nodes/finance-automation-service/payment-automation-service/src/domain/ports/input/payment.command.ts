import { Payment } from '../../models/payment.entity';
import { PaymentBatch } from '../../models/payment-batch.entity';
import { PaymentRule } from '../../models/payment-rule.entity';
import { VendorPayment } from '../../models/vendor-payment.entity';
import { Money } from '../../models/value-objects/money.value-object';
import { PaymentMethod } from '../../models/enums/payment-method.enum';

export interface CreatePaymentCommand {
  amount: number;
  currency: string;
  vendorId: string;
  vendorName: string;
  accountId: string;
  paymentMethod: PaymentMethod;
  gateway: string;
  scheduledAt?: Date;
  metadata?: Record<string, any>;
}

export interface UpdatePaymentCommand {
  paymentId: string;
  scheduledAt?: Date;
  metadata?: Record<string, any>;
}

export interface ProcessPaymentCommand {
  paymentId: string;
}

export interface SchedulePaymentCommand {
  paymentId: string;
  scheduledAt: Date;
}

export interface CancelPaymentCommand {
  paymentId: string;
  reason: string;
}

export interface RetryPaymentCommand {
  paymentId: string;
}

export interface CreateBatchCommand {
  name: string;
  description?: string;
  gateway: string;
  currency: string;
  scheduledAt?: Date;
  priority?: number;
  maxConcurrent?: number;
  metadata?: Record<string, any>;
}

export interface AddPaymentToBatchCommand {
  batchId: string;
  paymentId: string;
  amount: number;
  currency: string;
}

export interface ProcessBatchCommand {
  batchId: string;
}

export interface ScheduleBatchCommand {
  batchId: string;
  scheduledAt: Date;
}

export interface CancelBatchCommand {
  batchId: string;
}

export interface CreatePaymentRuleCommand {
  name: string;
  description?: string;
  ruleType: string;
  conditions: any[];
  actions: any[];
  priority?: number;
  effectiveFrom?: Date;
  effectiveTo?: Date;
}

export interface UpdatePaymentRuleCommand {
  ruleId: string;
  name?: string;
  description?: string;
  conditions?: any[];
  actions?: any[];
  priority?: number;
  isActive?: boolean;
}

export interface DeletePaymentRuleCommand {
  ruleId: string;
}

export interface CreateVendorPaymentCommand {
  vendorId: string;
  vendorName: string;
  vendorCode: string;
  email: string;
  phone?: string;
  taxId?: string;
  bankDetails: any;
  currency: string;
  paymentTerms?: number;
  autoPayEnabled?: boolean;
  preferredPaymentMethod?: string;
}

export interface UpdateVendorPaymentCommand {
  vendorPaymentId: string;
  vendorName?: string;
  email?: string;
  phone?: string;
  bankDetails?: any;
  paymentSettings?: any;
}

export interface RecordVendorPaymentCommand {
  vendorPaymentId: string;
  amount: number;
  currency: string;
}

export interface PaymentCommandUseCase {
  createPayment(command: CreatePaymentCommand, context?: any): Promise<Payment>;
  updatePayment(command: UpdatePaymentCommand, context?: any): Promise<Payment>;
  processPayment(command: ProcessPaymentCommand, context?: any): Promise<Payment>;
  schedulePayment(command: SchedulePaymentCommand, context?: any): Promise<Payment>;
  cancelPayment(command: CancelPaymentCommand, context?: any): Promise<Payment>;
  retryPayment(command: RetryPaymentCommand, context?: any): Promise<Payment>;
  getPayment(paymentId: string, context?: any): Promise<Payment>;
  listPayments(filters?: any, context?: any): Promise<Payment[]>;
}

export interface PaymentBatchCommandUseCase {
  createBatch(command: CreateBatchCommand, context?: any): Promise<PaymentBatch>;
  addPaymentToBatch(command: AddPaymentToBatchCommand, context?: any): Promise<PaymentBatch>;
  processBatch(command: ProcessBatchCommand, context?: any): Promise<PaymentBatch>;
  scheduleBatch(command: ScheduleBatchCommand, context?: any): Promise<PaymentBatch>;
  cancelBatch(command: CancelBatchCommand, context?: any): Promise<PaymentBatch>;
  getBatch(batchId: string, context?: any): Promise<PaymentBatch>;
  listBatches(filters?: any, context?: any): Promise<PaymentBatch[]>;
}

export interface PaymentRuleCommandUseCase {
  createRule(command: CreatePaymentRuleCommand, context?: any): Promise<PaymentRule>;
  updateRule(command: UpdatePaymentRuleCommand, context?: any): Promise<PaymentRule>;
  deleteRule(command: DeletePaymentRuleCommand, context?: any): Promise<void>;
  activateRule(ruleId: string, context?: any): Promise<PaymentRule>;
  deactivateRule(ruleId: string, context?: any): Promise<PaymentRule>;
  getRule(ruleId: string, context?: any): Promise<PaymentRule>;
  listRules(filters?: any, context?: any): Promise<PaymentRule[]>;
  evaluateRules(paymentData: any, context?: any): Promise<PaymentRule[]>;
}

export interface VendorPaymentCommandUseCase {
  createVendorPayment(
    command: CreateVendorPaymentCommand,
    context?: any,
  ): Promise<VendorPayment>;
  updateVendorPayment(
    command: UpdateVendorPaymentCommand,
    context?: any,
  ): Promise<VendorPayment>;
  recordPayment(command: RecordVendorPaymentCommand, context?: any): Promise<VendorPayment>;
  getVendorPayment(vendorPaymentId: string, context?: any): Promise<VendorPayment>;
  getVendorPaymentByVendorId(vendorId: string, context?: any): Promise<VendorPayment>;
  listVendorPayments(filters?: any, context?: any): Promise<VendorPayment[]>;
}
