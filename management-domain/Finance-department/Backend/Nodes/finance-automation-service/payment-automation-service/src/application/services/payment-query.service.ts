import { Injectable } from '@nestjs/common';
import { Payment, PaymentStatus } from '../../domain/models';
import { PaymentRepository } from '../../domain/repositories';
import { PaymentNotFoundException } from '@shared/exceptions';
import { ExecutionContext } from '@shared/context';

export interface PaymentFilters {
  status?: PaymentStatus;
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

export interface PaymentStatistics {
  totalPayments: number;
  totalAmount: number;
  statusBreakdown: Record<string, number>;
  gatewayBreakdown: Record<string, number>;
  averageProcessingTime: number;
  successRate: number;
}

@Injectable()
export class PaymentQueryService {
  constructor(private readonly paymentRepository: PaymentRepository) {}

  async findById(paymentId: string): Promise<Payment | null> {
    return this.paymentRepository.findById(paymentId);
  }

  async findByVendorId(vendorId: string): Promise<Payment[]> {
    return this.paymentRepository.findByVendorId(vendorId);
  }

  async findByBatchId(batchId: string): Promise<Payment[]> {
    return this.paymentRepository.findByBatchId(batchId);
  }

  async findByStatus(status: PaymentStatus): Promise<Payment[]> {
    return this.paymentRepository.findByStatus(status);
  }

  async findByDateRange(startDate: Date, endDate: Date): Promise<Payment[]> {
    const allPayments = await this.paymentRepository.findAll();
    return allPayments.filter(
      (p) => p.createdAt >= startDate && p.createdAt <= endDate,
    );
  }

  async findPendingPayments(): Promise<Payment[]> {
    return this.paymentRepository.findPending();
  }

  async findScheduledPayments(date: Date = new Date()): Promise<Payment[]> {
    return this.paymentRepository.findScheduled(date);
  }

  async findFailedPayments(retryable: boolean = false): Promise<Payment[]> {
    const failedPayments = await this.paymentRepository.findFailed();
    if (retryable) {
      return failedPayments.filter((p) => p.canRetry());
    }
    return failedPayments;
  }

  async findPaymentsByReconciliationStatus(status: string): Promise<Payment[]> {
    const allPayments = await this.paymentRepository.findAll();
    return allPayments.filter((p) => p.reconciliationStatus === status);
  }

  async search(filters: PaymentFilters): Promise<Payment[]> {
    let payments = await this.paymentRepository.findAll(filters);

    if (filters.startDate || filters.endDate) {
      payments = payments.filter((p) => {
        if (filters.startDate && p.createdAt < filters.startDate!) return false;
        if (filters.endDate && p.createdAt > filters.endDate!) return false;
        return true;
      });
    }

    if (filters.minAmount !== undefined || filters.maxAmount !== undefined) {
      payments = payments.filter((p) => {
        const amount = p.amount.amount;
        if (filters.minAmount !== undefined && amount < filters.minAmount) return false;
        if (filters.maxAmount !== undefined && amount > filters.maxAmount) return false;
        return true;
      });
    }

    if (filters.batchId) {
      payments = payments.filter((p) => p.batchId === filters.batchId);
    }

    if (filters.reconciliationStatus) {
      payments = payments.filter((p) => p.reconciliationStatus === filters.reconciliationStatus);
    }

    return payments;
  }

  async getStatistics(filters?: PaymentFilters): Promise<PaymentStatistics> {
    const payments = filters ? await this.search(filters) : await this.paymentRepository.findAll();

    const totalPayments = payments.length;
    const totalAmount = payments.reduce((sum, p) => sum + p.amount.amount, 0);

    const statusBreakdown: Record<string, number> = {};
    const gatewayBreakdown: Record<string, number> = {};

    let totalProcessingTime = 0;
    let processingTimeCount = 0;
    let successCount = 0;

    payments.forEach((p) => {
      // Status breakdown
      const status = p.status;
      statusBreakdown[status] = (statusBreakdown[status] || 0) + 1;

      // Gateway breakdown
      const gateway = p.gateway;
      gatewayBreakdown[gateway] = (gatewayBreakdown[gateway] || 0) + 1;

      // Processing time
      if (p.processedAt && p.createdAt) {
        const processingTime = p.processedAt.getTime() - p.createdAt.getTime();
        totalProcessingTime += processingTime;
        processingTimeCount++;
      }

      // Success rate
      if (p.isCompleted()) {
        successCount++;
      }
    });

    const averageProcessingTime =
      processingTimeCount > 0 ? totalProcessingTime / processingTimeCount : 0;
    const successRate = totalPayments > 0 ? (successCount / totalPayments) * 100 : 0;

    return {
      totalPayments,
      totalAmount,
      statusBreakdown,
      gatewayBreakdown,
      averageProcessingTime,
      successRate,
    };
  }
}
