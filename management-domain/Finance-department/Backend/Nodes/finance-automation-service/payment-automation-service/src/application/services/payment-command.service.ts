import { Injectable, NotFoundException, BadRequestException } from '@nestjs/common';
import {
  Payment,
  Money,
  PaymentMethod,
  PaymentStatus,
} from '../../domain/models';
import {
  PaymentRepository,
  EventPublisher,
  PaymentGateway,
} from '../../domain/ports';
import {
  PaymentInitiatedEvent,
  PaymentCompletedEvent,
  PaymentFailedEvent,
} from '../../domain/events';
import { ExecutionContext } from '@shared/context';
import {
  PaymentNotFoundException,
  PaymentAlreadyProcessedException,
  PaymentValidationException,
  PaymentGatewayException,
} from '@shared/exceptions';

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

@Injectable()
export class PaymentCommandService {
  constructor(
    private readonly paymentRepository: PaymentRepository,
    private readonly eventPublisher: EventPublisher,
    private readonly paymentGateway: PaymentGateway,
  ) {}

  async createPayment(
    command: CreatePaymentCommand,
    context: ExecutionContext,
  ): Promise<Payment> {
    const amount = Money.create(command.amount, command.currency);

    const payment = Payment.create(
      amount,
      command.vendorId,
      command.vendorName,
      command.accountId,
      command.paymentMethod,
      command.gateway,
      context.tenantId,
    );

    if (command.metadata) {
      payment.updateMetadata(command.metadata);
    }

    if (command.scheduledAt) {
      payment.schedule(command.scheduledAt);
    }

    await this.paymentRepository.save(payment);

    const event = PaymentInitiatedEvent.fromPayment(payment, context.correlationId);
    await this.eventPublisher.publish(event);

    return payment;
  }

  async updatePayment(
    command: UpdatePaymentCommand,
    context: ExecutionContext,
  ): Promise<Payment> {
    const payment = await this.paymentRepository.findById(command.paymentId);
    if (!payment) {
      throw new PaymentNotFoundException(command.paymentId);
    }

    if (command.scheduledAt) {
      payment.schedule(command.scheduledAt);
    }

    if (command.metadata) {
      payment.updateMetadata(command.metadata);
    }

    await this.paymentRepository.save(payment);

    return payment;
  }

  async processPayment(
    command: ProcessPaymentCommand,
    context: ExecutionContext,
  ): Promise<Payment> {
    const payment = await this.paymentRepository.findById(command.paymentId);
    if (!payment) {
      throw new PaymentNotFoundException(command.paymentId);
    }

    if (payment.isProcessing() || payment.isCompleted()) {
      throw new PaymentAlreadyProcessedException(command.paymentId);
    }

    payment.process();
    await this.paymentRepository.save(payment);

    try {
      const gatewayRequest = {
        amount: payment.amount,
        paymentMethod: payment.paymentMethod,
        vendorAccountDetails: {
          vendorId: payment.vendorId,
          vendorName: payment.vendorName,
          accountId: payment.accountId,
        },
        metadata: payment.metadata,
        description: `Payment to ${payment.vendorName}`,
        reference: payment.id,
      };

      const gatewayResponse = await this.paymentGateway.processPayment(gatewayRequest);

      if (gatewayResponse.success && gatewayResponse.transactionId) {
        payment.complete(
          gatewayResponse.transactionId,
          gatewayResponse.gatewayResponse,
        );
        await this.paymentRepository.save(payment);

        const event = PaymentCompletedEvent.fromPayment(
          payment,
          gatewayResponse.transactionId,
          gatewayResponse.gatewayResponse,
          context.correlationId,
        );
        await this.eventPublisher.publish(event);
      } else {
        const reason = gatewayResponse.errorMessage || 'Payment failed';
        payment.fail(reason, gatewayResponse.gatewayResponse);
        await this.paymentRepository.save(payment);

        const event = PaymentFailedEvent.fromPayment(
          payment,
          reason,
          gatewayResponse.gatewayResponse,
          context.correlationId,
        );
        await this.eventPublisher.publish(event);
      }
    } catch (error) {
      const reason = error instanceof Error ? error.message : 'Unknown error';
      payment.fail(reason, { error: reason });
      await this.paymentRepository.save(payment);

      const event = PaymentFailedEvent.fromPayment(
        payment,
        reason,
        { error: reason },
        context.correlationId,
      );
      await this.eventPublisher.publish(event);

      throw new PaymentGatewayException(reason, payment.gateway);
    }

    return payment;
  }

  async schedulePayment(
    command: SchedulePaymentCommand,
    context: ExecutionContext,
  ): Promise<Payment> {
    const payment = await this.paymentRepository.findById(command.paymentId);
    if (!payment) {
      throw new PaymentNotFoundException(command.paymentId);
    }

    payment.schedule(command.scheduledAt);
    await this.paymentRepository.save(payment);

    return payment;
  }

  async cancelPayment(
    command: CancelPaymentCommand,
    context: ExecutionContext,
  ): Promise<Payment> {
    const payment = await this.paymentRepository.findById(command.paymentId);
    if (!payment) {
      throw new PaymentNotFoundException(command.paymentId);
    }

    payment.cancel(command.reason);
    await this.paymentRepository.save(payment);

    return payment;
  }

  async retryPayment(
    command: RetryPaymentCommand,
    context: ExecutionContext,
  ): Promise<Payment> {
    const payment = await this.paymentRepository.findById(command.paymentId);
    if (!payment) {
      throw new PaymentNotFoundException(command.paymentId);
    }

    if (!payment.canRetry()) {
      throw new PaymentValidationException(
        'Payment cannot be retried. Maximum retry attempts reached or payment is not in failed state.',
      );
    }

    payment.retry();
    await this.paymentRepository.save(payment);

    return payment;
  }

  async getPayment(paymentId: string, context: ExecutionContext): Promise<Payment> {
    const payment = await this.paymentRepository.findById(paymentId);
    if (!payment) {
      throw new PaymentNotFoundException(paymentId);
    }
    return payment;
  }

  async listPayments(
    filters: any = {},
    context: ExecutionContext,
  ): Promise<Payment[]> {
    return this.paymentRepository.findAll(filters, context.tenantId);
  }
}
