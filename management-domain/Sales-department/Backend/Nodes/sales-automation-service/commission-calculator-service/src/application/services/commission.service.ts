import { Injectable, NotFoundException, BadRequestException } from '@nestjs/common';
import { Commission } from '../../domain/models/commission.entity';
import { CommissionRule } from '../../domain/models/commission-rule.entity';
import { CommissionPeriod } from '../../domain/models/commission-period.entity';
import { CommissionStatus } from '../../domain/enums/commission-status.enum';
import { CommissionSplit } from '../../domain/valueobjects/commission-split.value';
import {
  CommissionCalculatedEvent,
  PayoutApprovedEvent,
  PayoutPaidEvent,
  CommissionClawbackEvent,
} from '../../domain/events';
import {
  CommissionRepositoryPort,
  CommissionRuleRepositoryPort,
  CommissionPeriodRepositoryPort,
  EventPublisherPort,
} from '../../domain/ports/out';
import { RequestContextData } from '../../shared/context/request-context';
import { CommissionCalculationException, CommissionAlreadyPaidException, CommissionPeriodClosedException } from '../../shared/exceptions/commission.exception';
import { CreateCommissionDto, ApplyAdjustmentDto, ClawbackDto, ProcessPaymentDto, CalculateCommissionDto } from '../dtos';

/**
 * Commission use case service
 * Implements business logic for commission operations
 */
@Injectable()
export class CommissionService {
  constructor(
    private readonly commissionRepository: CommissionRepositoryPort,
    private readonly commissionRuleRepository: CommissionRuleRepositoryPort,
    private readonly commissionPeriodRepository: CommissionPeriodRepositoryPort,
    private readonly eventPublisher: EventPublisherPort,
  ) {}

  /**
   * Calculate commission for a sales transaction
   */
  async calculateCommission(
    request: CalculateCommissionDto,
    context: RequestContextData,
  ): Promise<Commission> {
    // Get the commission rule
    const rule = await this.commissionRuleRepository.findById(request.ruleId);
    if (!rule) {
      throw new NotFoundException(`Commission rule ${request.ruleId} not found`);
    }

    // Verify period is open
    const period = await this.commissionPeriodRepository.findById(request.periodId);
    if (!period) {
      throw new NotFoundException(`Commission period ${request.periodId} not found`);
    }
    if (period.getStatus() !== 'OPEN' && period.getStatus() !== 'CALCULATING') {
      throw new CommissionPeriodClosedException(request.periodId);
    }

    // Calculate commission using rule
    const commissionAmount = rule.calculateCommission(
      request.salesAmount,
      request.quotaAttained,
      request.productId,
      request.customerId,
    );

    // Create commission entity
    const commission = new Commission(
      request.salesRepId,
      request.salesRepName,
      request.periodId,
      rule.getIdAsString(),
      rule.getName(),
      request.salesAmount,
      rule.getBaseRate(),
      context.tenantId,
      context.organizationId || '',
      request.transactionDate ? new Date(request.transactionDate) : new Date(),
      request.currency || 'USD',
    );

    // Set quota info if provided
    if (request.quotaAttained !== undefined && request.quotaTarget !== undefined) {
      commission.setQuotaInfo(
        request.quotaAttained,
        request.quotaTarget,
        rule.getAcceleratorMultiplier(),
      );
    }

    // Save commission
    const savedCommission = await this.commissionRepository.save(commission);

    // Publish domain event
    const event = new CommissionCalculatedEvent(savedCommission, context.correlationId);
    await this.eventPublisher.publishCommissionCalculated(event);

    return savedCommission;
  }

  /**
   * Create a new commission record
   */
  async createCommission(
    dto: CreateCommissionDto,
    context: RequestContextData,
  ): Promise<Commission> {
    // Verify period exists
    const period = await this.commissionPeriodRepository.findById(dto.periodId);
    if (!period) {
      throw new NotFoundException(`Commission period ${dto.periodId} not found`);
    }

    // Create commission entity
    const commission = new Commission(
      dto.salesRepId,
      dto.salesRepName,
      dto.periodId,
      dto.ruleId,
      dto.ruleName,
      dto.salesAmount,
      dto.commissionRate,
      context.tenantId,
      context.organizationId || '',
      dto.transactionDate ? new Date(dto.transactionDate) : new Date(),
      dto.currency || 'USD',
    );

    // Set quota info if provided
    if (dto.quotaAttained !== undefined && dto.quotaTarget !== undefined) {
      commission.setQuotaInfo(dto.quotaAttained, dto.quotaTarget);
    }

    // Add splits if provided
    if (dto.splits && Array.isArray(dto.splits)) {
      for (const splitDto of dto.splits) {
        const split = new CommissionSplit(
          splitDto.salesRepId,
          splitDto.salesRepName,
          splitDto.percentage,
          splitDto.role,
        );
        commission.addSplit(split);
      }
    }

    // Validate commission
    if (!commission.isValid()) {
      throw new BadRequestException('Invalid commission data');
    }

    // Save commission
    const savedCommission = await this.commissionRepository.save(commission);

    // Publish domain event
    const event = new CommissionCalculatedEvent(savedCommission, context.correlationId);
    await this.eventPublisher.publishCommissionCalculated(event);

    return savedCommission;
  }

  /**
   * Get commission by ID
   */
  async getCommissionById(commissionId: string): Promise<Commission> {
    const commission = await this.commissionRepository.findById(commissionId);
    if (!commission) {
      throw new NotFoundException(`Commission ${commissionId} not found`);
    }
    return commission;
  }

  /**
   * Get commissions for a sales rep
   */
  async getCommissionsBySalesRep(
    salesRepId: string,
    context: RequestContextData,
    filters?: {
      status?: CommissionStatus;
      periodId?: string;
      startDate?: Date;
      endDate?: Date;
    },
  ): Promise<Commission[]> {
    const commissions = await this.commissionRepository.findBySalesRepId(salesRepId, {
      ...filters,
    });

    // Filter by tenant
    return commissions.filter(c => c.getTenantId() === context.tenantId);
  }

  /**
   * Submit commission for approval
   */
  async submitForApproval(
    commissionId: string,
    context: RequestContextData,
  ): Promise<Commission> {
    const commission = await this.getCommissionById(commissionId);
    commission.submitForApproval();
    return await this.commissionRepository.save(commission);
  }

  /**
   * Approve commission
   */
  async approveCommission(
    commissionId: string,
    approvedBy: string,
    context: RequestContextData,
  ): Promise<Commission> {
    const commission = await this.getCommissionById(commissionId);
    commission.approve(approvedBy);

    const savedCommission = await this.commissionRepository.save(commission);

    // Publish domain event
    const event = new PayoutApprovedEvent(
      savedCommission.getIdAsString(),
      savedCommission.getSalesRepId(),
      savedCommission.getSalesRepName(),
      savedCommission.getFinalAmount(),
      savedCommission.getCurrency(),
      approvedBy,
      savedCommission.getTenantId(),
      savedCommission.getOrganizationId(),
      context.correlationId,
    );
    await this.eventPublisher.publishPayoutApproved(event);

    return savedCommission;
  }

  /**
   * Apply adjustment to commission
   */
  async applyAdjustment(
    commissionId: string,
    dto: ApplyAdjustmentDto,
    context: RequestContextData,
  ): Promise<Commission> {
    const commission = await this.getCommissionById(commissionId);
    commission.applyAdjustment(dto.amount, dto.reason);
    return await this.commissionRepository.save(commission);
  }

  /**
   * Process commission payment
   */
  async processPayment(
    commissionId: string,
    dto: ProcessPaymentDto,
    context: RequestContextData,
  ): Promise<Commission> {
    const commission = await this.getCommissionById(commissionId);
    commission.pay(dto.paymentMethod, dto.reference);

    const savedCommission = await this.commissionRepository.save(commission);

    // Publish domain event
    const event = new PayoutPaidEvent(
      savedCommission.getIdAsString(),
      savedCommission.getSalesRepId(),
      savedCommission.getSalesRepName(),
      savedCommission.getFinalAmount(),
      savedCommission.getCurrency(),
      dto.paymentMethod,
      dto.reference,
      savedCommission.getTenantId(),
      savedCommission.getOrganizationId(),
      context.correlationId,
    );
    await this.eventPublisher.publishPayoutPaid(event);

    return savedCommission;
  }

  /**
   * Clawback commission
   */
  async clawbackCommission(
    commissionId: string,
    dto: ClawbackDto,
    context: RequestContextData,
  ): Promise<Commission> {
    const commission = await this.getCommissionById(commissionId);
    commission.applyClawback(dto.amount, dto.reason);

    const savedCommission = await this.commissionRepository.save(commission);

    // Publish domain event
    const event = new CommissionClawbackEvent(
      savedCommission.getIdAsString(),
      savedCommission.getSalesRepId(),
      savedCommission.getSalesRepName(),
      dto.amount,
      savedCommission.getCurrency(),
      dto.reason,
      savedCommission.getTenantId(),
      savedCommission.getOrganizationId(),
      context.correlationId,
    );
    await this.eventPublisher.publishCommissionClawback(event);

    return savedCommission;
  }

  /**
   * Calculate commissions for a period
   */
  async calculatePeriodCommissions(
    periodId: string,
    context: RequestContextData,
    salesRepIds?: string[],
  ): Promise<Commission[]> {
    const period = await this.commissionPeriodRepository.findById(periodId);
    if (!period) {
      throw new NotFoundException(`Commission period ${periodId} not found`);
    }

    // Start calculation
    period.startCalculation();
    await this.commissionPeriodRepository.save(period);

    // Get active rules
    const rules = await this.commissionRuleRepository.findActive(
      context.tenantId,
      context.organizationId,
    );

    // This would typically fetch deals/sales data and calculate commissions
    // For now, returning empty array
    const calculatedCommissions: Commission[] = [];

    // Close period after calculation
    period.close();
    await this.commissionPeriodRepository.save(period);

    return calculatedCommissions;
  }

  /**
   * Get commission summary
   */
  async getCommissionSummary(
    context: RequestContextData,
    filters?: {
      salesRepId?: string;
      periodId?: string;
      status?: CommissionStatus;
      startDate?: Date;
      endDate?: Date;
    },
  ): Promise<{
    totalCommissions: number;
    totalAmount: number;
    pendingAmount: number;
    approvedAmount: number;
    paidAmount: number;
    averageCommission: number;
    currency: string;
  }> {
    const summary = await this.commissionRepository.getSummary({
      tenantId: context.tenantId,
      organizationId: context.organizationId,
      ...filters,
    });

    return summary;
  }

  /**
   * Get pending approvals
   */
  async getPendingApprovals(
    context: RequestContextData,
    limit = 50,
  ): Promise<Commission[]> {
    return await this.commissionRepository.findPendingApprovals(context.tenantId, limit);
  }

  /**
   * Get overdue commissions
   */
  async getOverdueCommissions(context: RequestContextData): Promise<Commission[]> {
    return await this.commissionRepository.findOverdue(context.tenantId);
  }
}
