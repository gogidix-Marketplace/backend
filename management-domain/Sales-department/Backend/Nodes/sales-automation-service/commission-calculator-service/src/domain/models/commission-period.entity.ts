import { BaseEntity } from '../../shared/base/base.entity';
import { CommissionPeriodType, CommissionPeriodStatus } from '../enums/commission-period-type.enum';

/**
 * Commission Period entity
 * Represents a time period for commission calculations
 */
export class CommissionPeriod extends BaseEntity {
  private name: string;
  private periodType: CommissionPeriodType;
  private startDate: Date;
  private endDate: Date;
  private status: CommissionPeriodStatus;
  private processingDate?: Date;
  private tenantId: string;
  private organizationId: string;
  private fiscalYear: number;
  private fiscalQuarter?: number;
  private fiscalMonth?: number;

  constructor(
    name: string,
    periodType: CommissionPeriodType,
    startDate: Date,
    endDate: Date,
    tenantId: string,
    organizationId: string,
  ) {
    super();
    this.name = name;
    this.periodType = periodType;
    this.startDate = startDate;
    this.endDate = endDate;
    this.status = CommissionPeriodStatus.OPEN;
    this.tenantId = tenantId;
    this.organizationId = organizationId;

    // Calculate fiscal year/quarter/month
    this.calculateFiscalPeriods();
  }

  private calculateFiscalPeriods(): void {
    // Assuming fiscal year starts in January
    this.fiscalYear = this.startDate.getFullYear();
    this.fiscalMonth = this.startDate.getMonth() + 1;
    this.fiscalQuarter = Math.ceil(this.fiscalMonth / 3);
  }

  // Status transitions
  open(): void {
    if (this.status !== CommissionPeriodStatus.CLOSED && this.status !== CommissionPeriodStatus.LOCKED) {
      this.status = CommissionPeriodStatus.OPEN;
      this.markAsUpdated();
    }
  }

  startCalculation(): void {
    if (this.status === CommissionPeriodStatus.OPEN) {
      this.status = CommissionPeriodStatus.CALCULATING;
      this.markAsUpdated();
    } else {
      throw new Error(`Cannot start calculation. Period is ${this.status}`);
    }
  }

  close(): void {
    if (this.status === CommissionPeriodStatus.CALCULATING || this.status === CommissionPeriodStatus.OPEN) {
      this.status = CommissionPeriodStatus.CLOSED;
      this.processingDate = new Date();
      this.markAsUpdated();
    } else {
      throw new Error(`Cannot close period. Period is ${this.status}`);
    }
  }

  lock(): void {
    if (this.status === CommissionPeriodStatus.CLOSED) {
      this.status = CommissionPeriodStatus.LOCKED;
      this.markAsUpdated();
    } else {
      throw new Error('Cannot lock period. Period must be closed first');
    }
  }

  reopen(): void {
    if (this.status === CommissionPeriodStatus.CLOSED) {
      this.status = CommissionPeriodStatus.OPEN;
      this.processingDate = undefined;
      this.markAsUpdated();
    } else {
      throw new Error('Cannot reopen period. Period must be closed');
    }
  }

  // Date checks
  containsDate(date: Date): boolean {
    return date >= this.startDate && date <= this.endDate;
  }

  isPastDue(): boolean {
    return new Date() > this.endDate && this.status !== CommissionPeriodStatus.CLOSED && this.status !== CommissionPeriodStatus.LOCKED;
  }

  isFuture(): boolean {
    return this.startDate > new Date();
  }

  // Period duration calculations
  getDurationInDays(): number {
    const diffTime = Math.abs(this.endDate.getTime() - this.startDate.getTime());
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  }

  getDaysRemaining(): number {
    const now = new Date();
    if (now > this.endDate) return 0;
    const diffTime = this.endDate.getTime() - now.getTime();
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  }

  getDaysElapsed(): number {
    const now = new Date();
    if (now < this.startDate) return 0;
    const diffTime = now.getTime() - this.startDate.getTime();
    return Math.ceil(diffTime / (1000 * 60 * 60 * 24));
  }

  getCompletionPercentage(): number {
    const totalDays = this.getDurationInDays();
    const elapsedDays = this.getDaysElapsed();
    return Math.min(100, Math.max(0, (elapsedDays / totalDays) * 100));
  }

  // Getters
  getName(): string {
    return this.name;
  }

  getPeriodType(): CommissionPeriodType {
    return this.periodType;
  }

  getStartDate(): Date {
    return this.startDate;
  }

  getEndDate(): Date {
    return this.endDate;
  }

  getStatus(): CommissionPeriodStatus {
    return this.status;
  }

  getProcessingDate(): Date | undefined {
    return this.processingDate;
  }

  getTenantId(): string {
    return this.tenantId;
  }

  getOrganizationId(): string {
    return this.organizationId;
  }

  getFiscalYear(): number {
    return this.fiscalYear;
  }

  getFiscalQuarter(): number | undefined {
    return this.fiscalQuarter;
  }

  getFiscalMonth(): number | undefined {
    return this.fiscalMonth;
  }

  // Setters
  setName(name: string): void {
    this.name = name;
    this.markAsUpdated();
  }

  setDates(startDate: Date, endDate: Date): void {
    if (startDate >= endDate) {
      throw new Error('Start date must be before end date');
    }
    this.startDate = startDate;
    this.endDate = endDate;
    this.calculateFiscalPeriods();
    this.markAsUpdated();
  }

  toObject(): Record<string, unknown> {
    return {
      _id: this.id,
      name: this.name,
      periodType: this.periodType,
      startDate: this.startDate,
      endDate: this.endDate,
      status: this.status,
      processingDate: this.processingDate,
      tenantId: this.tenantId,
      organizationId: this.organizationId,
      fiscalYear: this.fiscalYear,
      fiscalQuarter: this.fiscalQuarter,
      fiscalMonth: this.fiscalMonth,
      durationInDays: this.getDurationInDays(),
      daysRemaining: this.getDaysRemaining(),
      completionPercentage: this.getCompletionPercentage(),
      createdAt: this.createdAtDate,
      updatedAt: this.updatedAtDate,
      version: this.getVersion,
    };
  }

  static fromObject(obj: Record<string, unknown>): CommissionPeriod {
    const period = new CommissionPeriod(
      obj.name as string,
      obj.periodType as CommissionPeriodType,
      new Date(obj.startDate as Date),
      new Date(obj.endDate as Date),
      obj.tenantId as string,
      obj.organizationId as string,
    );

    if (obj.id) period.id = obj.id as string;

    // Set internal state
    (period as any).status = obj.status as CommissionPeriodStatus ?? CommissionPeriodStatus.OPEN;
    (period as any).processingDate = obj.processingDate ? new Date(obj.processingDate as Date) : undefined;
    (period as any).createdAt = obj.createdAt ? new Date(obj.createdAt as Date) : period.createdAtDate;
    (period as any).updatedAt = obj.updatedAt ? new Date(obj.updatedAt as Date) : period.updatedAtDate;
    (period as any).version = obj.version as number ?? 0;

    return period;
  }

  /**
   * Create a monthly period
   */
  static createMonthly(year: number, month: number, tenantId: string, organizationId: string): CommissionPeriod {
    const startDate = new Date(year, month - 1, 1);
    const endDate = new Date(year, month, 0, 23, 59, 59);
    const name = `${year}-${month.toString().padStart(2, '0')}`;

    return new CommissionPeriod(name, CommissionPeriodType.MONTHLY, startDate, endDate, tenantId, organizationId);
  }

  /**
   * Create a quarterly period
   */
  static createQuarterly(year: number, quarter: number, tenantId: string, organizationId: string): CommissionPeriod {
    const startMonth = (quarter - 1) * 3;
    const startDate = new Date(year, startMonth, 1);
    const endDate = new Date(year, startMonth + 3, 0, 23, 59, 59);
    const name = `${year}-Q${quarter}`;

    return new CommissionPeriod(name, CommissionPeriodType.QUARTERLY, startDate, endDate, tenantId, organizationId);
  }

  /**
   * Create an annual period
   */
  static createAnnual(year: number, tenantId: string, organizationId: string): CommissionPeriod {
    const startDate = new Date(year, 0, 1);
    const endDate = new Date(year, 11, 31, 23, 59, 59);
    const name = `${year}`;

    return new CommissionPeriod(name, CommissionPeriodType.ANNUAL, startDate, endDate, tenantId, organizationId);
  }
}
