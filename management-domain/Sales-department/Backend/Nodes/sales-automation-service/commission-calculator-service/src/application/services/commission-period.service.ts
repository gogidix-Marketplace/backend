import { Injectable, NotFoundException, BadRequestException } from '@nestjs/common';
import { CommissionPeriod } from '../../domain/models/commission-period.entity';
import { CommissionPeriodType, CommissionPeriodStatus } from '../../domain/enums/commission-period-type.enum';
import { CommissionPeriodClosedEvent } from '../../domain/events';
import { CommissionPeriodRepositoryPort, EventPublisherPort } from '../../domain/ports/out';
import { RequestContextData } from '../../shared/context/request-context';
import { CommissionPeriodClosedException } from '../../shared/exceptions/commission.exception';
import { CreateCommissionPeriodDto, UpdateCommissionPeriodDto } from '../dtos';

/**
 * Commission Period use case service
 */
@Injectable()
export class CommissionPeriodService {
  constructor(
    private readonly commissionPeriodRepository: CommissionPeriodRepositoryPort,
    private readonly eventPublisher: EventPublisherPort,
  ) {}

  /**
   * Create a new commission period
   */
  async createPeriod(
    dto: CreateCommissionPeriodDto,
    context: RequestContextData,
  ): Promise<CommissionPeriod> {
    const startDate = new Date(dto.startDate);
    const endDate = new Date(dto.endDate);

    if (startDate >= endDate) {
      throw new BadRequestException('Start date must be before end date');
    }

    const period = new CommissionPeriod(
      dto.name,
      dto.periodType,
      startDate,
      endDate,
      context.tenantId,
      context.organizationId || '',
    );

    return await this.commissionPeriodRepository.save(period);
  }

  /**
   * Get period by ID
   */
  async getPeriodById(
    periodId: string,
    context: RequestContextData,
  ): Promise<CommissionPeriod> {
    const period = await this.commissionPeriodRepository.findById(periodId);

    if (!period) {
      throw new NotFoundException(`Commission period ${periodId} not found`);
    }

    // Verify tenant access
    if (period.getTenantId() !== context.tenantId) {
      throw new NotFoundException(`Commission period ${periodId} not found`);
    }

    return period;
  }

  /**
   * Get active periods
   */
  async getActivePeriods(
    context: RequestContextData,
  ): Promise<CommissionPeriod[]> {
    return await this.commissionPeriodRepository.findActive(
      context.tenantId,
      context.organizationId,
    );
  }

  /**
   * Get periods by type
   */
  async getPeriodsByType(
    type: CommissionPeriodType,
    context: RequestContextData,
  ): Promise<CommissionPeriod[]> {
    return await this.commissionPeriodRepository.findByType(
      context.tenantId,
      type,
    );
  }

  /**
   * Get period for a specific date
   */
  async getPeriodForDate(
    date: Date,
    context: RequestContextData,
  ): Promise<CommissionPeriod | null> {
    return await this.commissionPeriodRepository.findForDate(
      context.tenantId,
      date,
    );
  }

  /**
   * Close period
   */
  async closePeriod(
    periodId: string,
    context: RequestContextData,
  ): Promise<CommissionPeriod> {
    const period = await this.getPeriodById(periodId, context);

    try {
      period.close();
    } catch (error) {
      throw new BadRequestException(error.message);
    }

    const savedPeriod = await this.commissionPeriodRepository.save(period);

    // Publish domain event
    const event = new CommissionPeriodClosedEvent(
      savedPeriod.getIdAsString(),
      savedPeriod.getName(),
      savedPeriod.getStartDate(),
      savedPeriod.getEndDate(),
      0, // totalCommissions - would be calculated
      0, // totalAmount - would be calculated
      'USD',
      savedPeriod.getTenantId(),
      savedPeriod.getOrganizationId(),
      context.correlationId,
    );
    await this.eventPublisher.publishCommissionPeriodClosed(event);

    return savedPeriod;
  }

  /**
   * Lock period
   */
  async lockPeriod(
    periodId: string,
    context: RequestContextData,
  ): Promise<CommissionPeriod> {
    const period = await this.getPeriodById(periodId, context);

    try {
      period.lock();
    } catch (error) {
      throw new BadRequestException(error.message);
    }

    return await this.commissionPeriodRepository.save(period);
  }

  /**
   * Reopen period
   */
  async reopenPeriod(
    periodId: string,
    context: RequestContextData,
  ): Promise<CommissionPeriod> {
    const period = await this.getPeriodById(periodId, context);

    try {
      period.reopen();
    } catch (error) {
      throw new BadRequestException(error.message);
    }

    return await this.commissionPeriodRepository.save(period);
  }

  /**
   * Start calculation for period
   */
  async startCalculation(
    periodId: string,
    context: RequestContextData,
  ): Promise<CommissionPeriod> {
    const period = await this.getPeriodById(periodId, context);

    try {
      period.startCalculation();
    } catch (error) {
      throw new BadRequestException(error.message);
    }

    return await this.commissionPeriodRepository.save(period);
  }

  /**
   * Update period
   */
  async updatePeriod(
    periodId: string,
    dto: UpdateCommissionPeriodDto,
    context: RequestContextData,
  ): Promise<CommissionPeriod> {
    const period = await this.getPeriodById(periodId, context);

    if (dto.name !== undefined) {
      period.setName(dto.name);
    }

    if (dto.startDate !== undefined && dto.endDate !== undefined) {
      const startDate = new Date(dto.startDate);
      const endDate = new Date(dto.endDate);

      if (startDate >= endDate) {
        throw new BadRequestException('Start date must be before end date');
      }

      try {
        period.setDates(startDate, endDate);
      } catch (error) {
        throw new BadRequestException(error.message);
      }
    }

    return await this.commissionPeriodRepository.save(period);
  }

  /**
   * Delete period
   */
  async deletePeriod(
    periodId: string,
    context: RequestContextData,
  ): Promise<void> {
    const period = await this.getPeriodById(periodId, context);

    if (period.getStatus() === CommissionPeriodStatus.LOCKED) {
      throw new BadRequestException('Cannot delete a locked period');
    }

    await this.commissionPeriodRepository.delete(periodId);
  }

  /**
   * Create monthly period
   */
  async createMonthlyPeriod(
    year: number,
    month: number,
    context: RequestContextData,
  ): Promise<CommissionPeriod> {
    const period = CommissionPeriod.createMonthly(
      year,
      month,
      context.tenantId,
      context.organizationId || '',
    );

    return await this.commissionPeriodRepository.save(period);
  }

  /**
   * Create quarterly period
   */
  async createQuarterlyPeriod(
    year: number,
    quarter: number,
    context: RequestContextData,
  ): Promise<CommissionPeriod> {
    const period = CommissionPeriod.createQuarterly(
      year,
      quarter,
      context.tenantId,
      context.organizationId || '',
    );

    return await this.commissionPeriodRepository.save(period);
  }

  /**
   * Create annual period
   */
  async createAnnualPeriod(
    year: number,
    context: RequestContextData,
  ): Promise<CommissionPeriod> {
    const period = CommissionPeriod.createAnnual(
      year,
      context.tenantId,
      context.organizationId || '',
    );

    return await this.commissionPeriodRepository.save(period);
  }
}
