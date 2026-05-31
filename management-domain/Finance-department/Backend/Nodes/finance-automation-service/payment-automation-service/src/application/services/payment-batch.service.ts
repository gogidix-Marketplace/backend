import { Injectable } from '@nestjs/common';
import { PaymentBatch, Money, BatchStatus } from '../../domain/models';
import {
  PaymentBatchRepository,
  PaymentRepository,
  EventPublisher,
  PaymentGateway,
} from '../../domain/ports';
import { BatchProcessedEvent } from '../../domain/events';
import { ExecutionContext } from '@shared/context';
import { PaymentBatchException, PaymentNotFoundException } from '@shared/exceptions';

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

@Injectable()
export class PaymentBatchService {
  constructor(
    private readonly batchRepository: PaymentBatchRepository,
    private readonly paymentRepository: PaymentRepository,
    private readonly eventPublisher: EventPublisher,
    private readonly paymentGateway: PaymentGateway,
  ) {}

  async createBatch(
    command: CreateBatchCommand,
    context: ExecutionContext,
  ): Promise<PaymentBatch> {
    const batch = PaymentBatch.create(
      command.name,
      command.gateway,
      command.currency,
      context.tenantId,
      context.userId,
    );

    if (command.description) {
      batch.setDescription(command.description);
    }

    if (command.scheduledAt) {
      batch.schedule(command.scheduledAt);
    }

    if (command.priority !== undefined) {
      batch.setPriority(command.priority);
    }

    if (command.maxConcurrent !== undefined) {
      batch.setMaxConcurrent(command.maxConcurrent);
    }

    if (command.metadata) {
      batch.updateMetadata(command.metadata);
    }

    return this.batchRepository.save(batch);
  }

  async addPaymentToBatch(
    command: AddPaymentToBatchCommand,
    context: ExecutionContext,
  ): Promise<PaymentBatch> {
    const batch = await this.batchRepository.findById(command.batchId);
    if (!batch) {
      throw new PaymentBatchException(command.batchId, 'Batch not found');
    }

    const payment = await this.paymentRepository.findById(command.paymentId);
    if (!payment) {
      throw new PaymentNotFoundException(command.paymentId);
    }

    const amount = Money.create(command.amount, command.currency);
    batch.addPayment(command.paymentId, amount);
    payment.assignToBatch(command.batchId);

    await this.batchRepository.save(batch);
    await this.paymentRepository.save(payment);

    return batch;
  }

  async processBatch(
    command: ProcessBatchCommand,
    context: ExecutionContext,
  ): Promise<PaymentBatch> {
    const batch = await this.batchRepository.findById(command.batchId);
    if (!batch) {
      throw new PaymentBatchException(command.batchId, 'Batch not found');
    }

    if (batch.isEmpty()) {
      throw new PaymentBatchException(command.batchId, 'Cannot process empty batch');
    }

    batch.startProcessing();
    await this.batchRepository.save(batch);

    const paymentIds = batch.paymentIds;
    let completedCount = 0;
    let failedCount = 0;

    for (const paymentId of paymentIds) {
      const payment = await this.paymentRepository.findById(paymentId);
      if (!payment) continue;

      try {
        const gatewayRequest = {
          amount: payment.amount,
          paymentMethod: payment.paymentMethod,
          vendorAccountDetails: {
            vendorId: payment.vendorId,
            vendorName: payment.vendorName,
            accountId: payment.accountId,
          },
          metadata: { ...payment.metadata, batchId: batch.id },
          description: `Batch payment to ${payment.vendorName}`,
          reference: `${batch.id}-${payment.id}`,
        };

        const gatewayResponse = await this.paymentGateway.processPayment(gatewayRequest);

        if (gatewayResponse.success && gatewayResponse.transactionId) {
          payment.complete(
            gatewayResponse.transactionId,
            gatewayResponse.gatewayResponse,
          );
          completedCount++;
        } else {
          const reason = gatewayResponse.errorMessage || 'Payment failed';
          payment.fail(reason, gatewayResponse.gatewayResponse);
          failedCount++;
        }

        await this.paymentRepository.save(payment);
      } catch (error) {
        const reason = error instanceof Error ? error.message : 'Unknown error';
        payment.fail(reason, { error: reason });
        failedCount++;
        await this.paymentRepository.save(payment);
      }
    }

    const completionRate = completedCount / paymentIds.length;
    const failureRate = failedCount / paymentIds.length;

    if (failedCount === 0) {
      batch.complete();
    } else if (completedCount > 0) {
      batch.markPartiallyCompleted();
    } else {
      batch.fail();
    }

    await this.batchRepository.save(batch);

    const event = BatchProcessedEvent.fromBatch(
      batch,
      completionRate,
      failureRate,
      context.correlationId,
    );
    await this.eventPublisher.publish(event);

    return batch;
  }

  async scheduleBatch(
    command: ScheduleBatchCommand,
    context: ExecutionContext,
  ): Promise<PaymentBatch> {
    const batch = await this.batchRepository.findById(command.batchId);
    if (!batch) {
      throw new PaymentBatchException(command.batchId, 'Batch not found');
    }

    batch.schedule(command.scheduledAt);
    return this.batchRepository.save(batch);
  }

  async cancelBatch(
    command: CancelBatchCommand,
    context: ExecutionContext,
  ): Promise<PaymentBatch> {
    const batch = await this.batchRepository.findById(command.batchId);
    if (!batch) {
      throw new PaymentBatchException(command.batchId, 'Batch not found');
    }

    batch.cancel();

    const paymentIds = batch.paymentIds;
    for (const paymentId of paymentIds) {
      const payment = await this.paymentRepository.findById(paymentId);
      if (payment && !payment.isFinalState()) {
        payment.cancel('Batch cancelled');
        await this.paymentRepository.save(payment);
      }
    }

    return this.batchRepository.save(batch);
  }

  async getBatch(batchId: string, context: ExecutionContext): Promise<PaymentBatch> {
    const batch = await this.batchRepository.findById(batchId);
    if (!batch) {
      throw new PaymentBatchException(batchId, 'Batch not found');
    }
    return batch;
  }

  async listBatches(
    filters: any = {},
    context: ExecutionContext,
  ): Promise<PaymentBatch[]> {
    return this.batchRepository.findAll(filters, context.tenantId);
  }
}
