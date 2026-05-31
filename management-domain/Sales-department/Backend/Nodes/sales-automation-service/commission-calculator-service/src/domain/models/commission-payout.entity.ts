import { BaseEntity } from '../../shared/base/base.entity';
import { CommissionStatus } from '../enums/commission-status.enum';

/**
 * Commission Payout Status enumeration
 */
export enum PayoutStatus {
  PENDING = 'PENDING',
  APPROVED = 'APPROVED',
  PROCESSING = 'PROCESSING',
  COMPLETED = 'COMPLETED',
  FAILED = 'FAILED',
  CANCELLED = 'CANCELLED',
}

/**
 * Commission Payout entity
 * Represents a batch payout of commissions to a sales rep
 */
export class CommissionPayout extends BaseEntity {
  private salesRepId: string;
  private salesRepName: string;
  private periodId: string;
  private periodName: string;
  private commissionIds: string[];
  private status: PayoutStatus;
  private grossAmount: number;
  private adjustments: number;
  private taxWithholdings: number;
  private deductions: number;
  private netAmount: number;
  private currency: string;
  private scheduledDate: Date;
  private processedDate?: Date;
  private completedDate?: Date;
  private paymentMethod?: string;
  private paymentReference?: string;
  private failureReason?: string;
  private approvedBy?: string;
  private approvedAt?: Date;
  private tenantId: string;
  private organizationId: string;
  private notes?: string;

  constructor(
    salesRepId: string,
    salesRepName: string,
    periodId: string,
    periodName: string,
    grossAmount: number,
    tenantId: string,
    organizationId: string,
    scheduledDate: Date,
    currency: string = 'USD',
  ) {
    super();
    this.salesRepId = salesRepId;
    this.salesRepName = salesRepName;
    this.periodId = periodId;
    this.periodName = periodName;
    this.commissionIds = [];
    this.status = PayoutStatus.PENDING;
    this.grossAmount = grossAmount;
    this.adjustments = 0;
    this.taxWithholdings = 0;
    this.deductions = 0;
    this.netAmount = grossAmount;
    this.currency = currency;
    this.scheduledDate = scheduledDate;
    this.tenantId = tenantId;
    this.organizationId = organizationId;
  }

  // Status transitions
  approve(approvedBy: string): void {
    if (this.status !== PayoutStatus.PENDING) {
      throw new Error(`Cannot approve payout in status ${this.status}`);
    }
    this.status = PayoutStatus.APPROVED;
    this.approvedBy = approvedBy;
    this.approvedAt = new Date();
    this.markAsUpdated();
  }

  startProcessing(): void {
    if (this.status !== PayoutStatus.APPROVED) {
      throw new Error(`Cannot start processing payout in status ${this.status}`);
    }
    this.status = PayoutStatus.PROCESSING;
    this.processedDate = new Date();
    this.markAsUpdated();
  }

  complete(paymentReference: string): void {
    if (this.status !== PayoutStatus.PROCESSING) {
      throw new Error(`Cannot complete payout in status ${this.status}`);
    }
    this.status = PayoutStatus.COMPLETED;
    this.completedDate = new Date();
    this.paymentReference = paymentReference;
    this.markAsUpdated();
  }

  fail(reason: string): void {
    if (this.status !== PayoutStatus.PROCESSING) {
      throw new Error(`Cannot fail payout in status ${this.status}`);
    }
    this.status = PayoutStatus.FAILED;
    this.failureReason = reason;
    this.markAsUpdated();
  }

  cancel(): void {
    if (this.status === PayoutStatus.COMPLETED || this.status === PayoutStatus.PROCESSING) {
      throw new Error(`Cannot cancel payout in status ${this.status}`);
    }
    this.status = PayoutStatus.CANCELLED;
    this.markAsUpdated();
  }

  // Commission management
  addCommission(commissionId: string): void {
    if (!this.commissionIds.includes(commissionId)) {
      this.commissionIds.push(commissionId);
      this.markAsUpdated();
    }
  }

  removeCommission(commissionId: string): void {
    this.commissionIds = this.commissionIds.filter(id => id !== commissionId);
    this.markAsUpdated();
  }

  getCommissionIds(): string[] {
    return [...this.commissionIds];
  }

  // Financial calculations
  addAdjustment(amount: number): void {
    if (this.status !== PayoutStatus.PENDING) {
      throw new Error('Cannot add adjustments to non-pending payout');
    }
    this.adjustments += amount;
    this.recalculateNetAmount();
    this.markAsUpdated();
  }

  setTaxWithholdings(amount: number): void {
    if (this.status !== PayoutStatus.PENDING) {
      throw new Error('Cannot set tax withholdings for non-pending payout');
    }
    this.taxWithholdings = amount;
    this.recalculateNetAmount();
    this.markAsUpdated();
  }

  setDeductions(amount: number): void {
    if (this.status !== PayoutStatus.PENDING) {
      throw new Error('Cannot set deductions for non-pending payout');
    }
    this.deductions = amount;
    this.recalculateNetAmount();
    this.markAsUpdated();
  }

  private recalculateNetAmount(): void {
    this.netAmount = this.grossAmount + this.adjustments - this.taxWithholdings - this.deductions;
  }

  // Validation
  isValid(): boolean {
    return (
      this.salesRepId.length > 0 &&
      this.grossAmount >= 0 &&
      this.netAmount >= 0 &&
      this.commissionIds.length > 0
    );
  }

  isOverdue(): boolean {
    return new Date() > this.scheduledDate && this.status !== PayoutStatus.COMPLETED;
  }

  // Getters
  getSalesRepId(): string {
    return this.salesRepId;
  }

  getSalesRepName(): string {
    return this.salesRepName;
  }

  getPeriodId(): string {
    return this.periodId;
  }

  getPeriodName(): string {
    return this.periodName;
  }

  getStatus(): PayoutStatus {
    return this.status;
  }

  getGrossAmount(): number {
    return this.grossAmount;
  }

  getAdjustments(): number {
    return this.adjustments;
  }

  getTaxWithholdings(): number {
    return this.taxWithholdings;
  }

  getDeductions(): number {
    return this.deductions;
  }

  getNetAmount(): number {
    return this.netAmount;
  }

  getCurrency(): string {
    return this.currency;
  }

  getScheduledDate(): Date {
    return this.scheduledDate;
  }

  getProcessedDate(): Date | undefined {
    return this.processedDate;
  }

  getCompletedDate(): Date | undefined {
    return this.completedDate;
  }

  getPaymentMethod(): string | undefined {
    return this.paymentMethod;
  }

  getPaymentReference(): string | undefined {
    return this.paymentReference;
  }

  getFailureReason(): string | undefined {
    return this.failureReason;
  }

  getApprovedBy(): string | undefined {
    return this.approvedBy;
  }

  getApprovedAt(): Date | undefined {
    return this.approvedAt;
  }

  getTenantId(): string {
    return this.tenantId;
  }

  getOrganizationId(): string {
    return this.organizationId;
  }

  // Setters
  setSalesRepName(name: string): void {
    this.salesRepName = name;
    this.markAsUpdated();
  }

  setPaymentMethod(method: string): void {
    this.paymentMethod = method;
    this.markAsUpdated();
  }

  setNotes(notes: string): void {
    this.notes = notes;
    this.markAsUpdated();
  }

  setScheduledDate(date: Date): void {
    this.scheduledDate = date;
    this.markAsUpdated();
  }

  toObject(): Record<string, unknown> {
    return {
      _id: this.id,
      salesRepId: this.salesRepId,
      salesRepName: this.salesRepName,
      periodId: this.periodId,
      periodName: this.periodName,
      commissionIds: this.commissionIds,
      status: this.status,
      grossAmount: this.grossAmount,
      adjustments: this.adjustments,
      taxWithholdings: this.taxWithholdings,
      deductions: this.deductions,
      netAmount: this.netAmount,
      currency: this.currency,
      scheduledDate: this.scheduledDate,
      processedDate: this.processedDate,
      completedDate: this.completedDate,
      paymentMethod: this.paymentMethod,
      paymentReference: this.paymentReference,
      failureReason: this.failureReason,
      approvedBy: this.approvedBy,
      approvedAt: this.approvedAt,
      tenantId: this.tenantId,
      organizationId: this.organizationId,
      notes: this.notes,
      isOverdue: this.isOverdue(),
      commissionCount: this.commissionIds.length,
      createdAt: this.createdAtDate,
      updatedAt: this.updatedAtDate,
      version: this.getVersion,
    };
  }

  static fromObject(obj: Record<string, unknown>): CommissionPayout {
    const payout = new CommissionPayout(
      obj.salesRepId as string,
      obj.salesRepName as string,
      obj.periodId as string,
      obj.periodName as string,
      obj.grossAmount as number,
      obj.tenantId as string,
      obj.organizationId as string,
      new Date(obj.scheduledDate as Date),
      obj.currency as string ?? 'USD',
    );

    if (obj.id) payout.id = obj.id as string;

    // Set internal state
    (payout as any).status = obj.status as PayoutStatus ?? PayoutStatus.PENDING;
    (payout as any).adjustments = obj.adjustments as number ?? 0;
    (payout as any).taxWithholdings = obj.taxWithholdings as number ?? 0;
    (payout as any).deductions = obj.deductions as number ?? 0;
    (payout as any).netAmount = obj.netAmount as number ?? payout.getNetAmount();
    (payout as any).processedDate = obj.processedDate ? new Date(obj.processedDate as Date) : undefined;
    (payout as any).completedDate = obj.completedDate ? new Date(obj.completedDate as Date) : undefined;
    (payout as any).paymentMethod = obj.paymentMethod as string | undefined;
    (payout as any).paymentReference = obj.paymentReference as string | undefined;
    (payout as any).failureReason = obj.failureReason as string | undefined;
    (payout as any).approvedBy = obj.approvedBy as string | undefined;
    (payout as any).approvedAt = obj.approvedAt ? new Date(obj.approvedAt as Date) : undefined;
    (payout as any).notes = obj.notes as string | undefined;
    (payout as any).createdAt = obj.createdAt ? new Date(obj.createdAt as Date) : payout.createdAtDate;
    (payout as any).updatedAt = obj.updatedAt ? new Date(obj.updatedAt as Date) : payout.updatedAtDate;
    (payout as any).version = obj.version as number ?? 0;

    // Add commission IDs
    if (obj.commissionIds && Array.isArray(obj.commissionIds)) {
      (obj.commissionIds as string[]).forEach(id => payout.addCommission(id));
    }

    return payout;
  }
}
