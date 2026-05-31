import { BaseEntity } from '../../shared/base/base.entity';
import { CommissionStatus, CommissionStatusTransitions } from '../enums/commission-status.enum';
import { CommissionSplit } from '../valueobjects/commission-split.value';
import { Money } from '../valueobjects/money.value';

/**
 * Commission entity
 * Represents a commission earned by a sales rep
 */
export class Commission extends BaseEntity {
  private salesRepId: string;
  private salesRepName: string;
  private periodId: string;
  private ruleId: string;
  private ruleName: string;
  private status: CommissionStatus;
  private salesAmount: number;
  private commissionRate: number;
  private calculatedAmount: number;
  private adjustedAmount: number;
  private finalAmount: number;
  private currency: string;
  private transactionDate: Date;
  settlementDate?: Date;
  private paymentDate?: Date;
  private splits: CommissionSplit[];
  private adjustmentReason?: string;
  private clawbackAmount: number;
  private clawbackReason?: string;
  private clawbackDate?: Date;
  private quotaAttained?: number;
  private quotaTarget?: number;
  private acceleratorApplied?: number;
  private productSales: Array<{ productId: string; productName: string; amount: number; commission: number }>;
  private tenantId: string;
  private organizationId: string;
  private approvedBy?: string;
  private approvedAt?: Date;
  private paidVia?: string;
  private paymentReference?: string;

  constructor(
    salesRepId: string,
    salesRepName: string,
    periodId: string,
    ruleId: string,
    ruleName: string,
    salesAmount: number,
    commissionRate: number,
    tenantId: string,
    organizationId: string,
    transactionDate: Date = new Date(),
    currency: string = 'USD',
  ) {
    super();
    this.salesRepId = salesRepId;
    this.salesRepName = salesRepName;
    this.periodId = periodId;
    this.ruleId = ruleId;
    this.ruleName = ruleName;
    this.salesAmount = salesAmount;
    this.commissionRate = commissionRate;
    this.calculatedAmount = (salesAmount * commissionRate) / 100;
    this.adjustedAmount = 0;
    this.finalAmount = this.calculatedAmount;
    this.status = CommissionStatus.DRAFT;
    this.currency = currency;
    this.transactionDate = transactionDate;
    this.splits = [];
    this.clawbackAmount = 0;
    this.productSales = [];
    this.tenantId = tenantId;
    this.organizationId = organizationId;
  }

  // Status transitions
  transitionTo(status: CommissionStatus, reason?: string): void {
    const allowedTransitions = CommissionStatusTransitions[this.status];
    if (!allowedTransitions.includes(status)) {
      throw new Error(
        `Invalid status transition from ${this.status} to ${status}. ` +
        `Allowed transitions: ${allowedTransitions.join(', ')}`,
      );
    }

    this.status = status;

    if (status === CommissionStatus.PAID) {
      this.paymentDate = new Date();
    }

    this.markAsUpdated();
  }

  submitForApproval(): void {
    if (this.status !== CommissionStatus.DRAFT) {
      throw new Error('Only draft commissions can be submitted for approval');
    }
    this.transitionTo(CommissionStatus.PENDING);
  }

  approve(approvedBy: string): void {
    if (this.status !== CommissionStatus.PENDING && this.status !== CommissionStatus.CALCULATED) {
      throw new Error('Commission must be pending or calculated to be approved');
    }
    this.approvedBy = approvedBy;
    this.approvedAt = new Date();
    this.transitionTo(CommissionStatus.APPROVED);
  }

  pay(paymentMethod: string, reference?: string): void {
    if (this.status !== CommissionStatus.APPROVED) {
      throw new Error('Commission must be approved before payment');
    }
    this.paidVia = paymentMethod;
    this.paymentReference = reference;
    this.transitionTo(CommissionStatus.PAID);
  }

  // Commission calculations
  calculateFinalAmount(): void {
    this.finalAmount = Math.max(0, this.calculatedAmount + this.adjustedAmount - this.clawbackAmount);
    this.markAsUpdated();
  }

  applyAdjustment(amount: number, reason: string): void {
    if (this.status === CommissionStatus.PAID) {
      throw new Error('Cannot adjust paid commission');
    }
    if (this.status === CommissionStatus.CLAWED_BACK) {
      throw new Error('Cannot adjust clawed back commission');
    }

    this.adjustedAmount = amount;
    this.adjustmentReason = reason;
    this.calculateFinalAmount();
    this.markAsUpdated();
  }

  clearAdjustment(): void {
    this.adjustedAmount = 0;
    this.adjustmentReason = undefined;
    this.calculateFinalAmount();
    this.markAsUpdated();
  }

  applyClawback(amount: number, reason: string): void {
    if (this.status !== CommissionStatus.PAID) {
      throw new Error('Can only claw back paid commissions');
    }
    if (amount > this.finalAmount) {
      throw new Error('Clawback amount cannot exceed commission amount');
    }

    this.clawbackAmount = amount;
    this.clawbackReason = reason;
    this.clawbackDate = new Date();
    this.transitionTo(CommissionStatus.CLAWED_BACK);
  }

  // Split commission management
  addSplit(split: CommissionSplit): void {
    if (this.status !== CommissionStatus.DRAFT && this.status !== CommissionStatus.PENDING) {
      throw new Error('Can only add splits to draft or pending commissions');
    }
    this.splits.push(split);
    this.markAsUpdated();
  }

  removeSplit(salesRepId: string): void {
    if (this.status !== CommissionStatus.DRAFT && this.status !== CommissionStatus.PENDING) {
      throw new Error('Can only remove splits from draft or pending commissions');
    }
    this.splits = this.splits.filter(s => s.getSalesRepId() !== salesRepId);
    this.markAsUpdated();
  }

  getSplits(): CommissionSplit[] {
    return [...this.splits];
  }

  hasSplit(): boolean {
    return this.splits.length > 0;
  }

  calculateSplitAmount(salesRepId: string): number {
    const split = this.splits.find(s => s.getSalesRepId() === salesRepId);
    return split ? split.calculateShare(this.finalAmount) : 0;
  }

  // Quota and accelerator
  setQuotaInfo(attained: number, target: number, accelerator?: number): void {
    this.quotaAttained = attained;
    this.quotaTarget = target;
    this.acceleratorApplied = accelerator;
    this.markAsUpdated();
  }

  getQuotaPercentage(): number | undefined {
    if (this.quotaTarget !== undefined && this.quotaAttained !== undefined) {
      return (this.quotaAttained / this.quotaTarget) * 100;
    }
    return undefined;
  }

  // Product sales tracking
  addProductSale(productId: string, productName: string, amount: number, commission: number): void {
    this.productSales.push({ productId, productName, amount, commission });
    this.markAsUpdated();
  }

  getProductSales(): Array<{ productId: string; productName: string; amount: number; commission: number }> {
    return [...this.productSales];
  }

  // Settlement calculations
  setSettlementDate(date: Date): void {
    this.settlementDate = date;
    this.markAsUpdated();
  }

  isOverdue(): boolean {
    if (this.settlementDate && this.status !== CommissionStatus.PAID) {
      return new Date() > this.settlementDate;
    }
    return false;
  }

  // Validation
  isValid(): boolean {
    return (
      this.salesRepId.length > 0 &&
      this.salesAmount >= 0 &&
      this.commissionRate >= 0 &&
      this.calculatedAmount >= 0 &&
      this.finalAmount >= 0
    );
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

  getRuleId(): string {
    return this.ruleId;
  }

  getRuleName(): string {
    return this.ruleName;
  }

  getStatus(): CommissionStatus {
    return this.status;
  }

  getSalesAmount(): number {
    return this.salesAmount;
  }

  getCommissionRate(): number {
    return this.commissionRate;
  }

  getCalculatedAmount(): number {
    return this.calculatedAmount;
  }

  getAdjustedAmount(): number {
    return this.adjustedAmount;
  }

  getFinalAmount(): number {
    return this.finalAmount;
  }

  getCurrency(): string {
    return this.currency;
  }

  getTransactionDate(): Date {
    return this.transactionDate;
  }

  getSettlementDate(): Date | undefined {
    return this.settlementDate;
  }

  getPaymentDate(): Date | undefined {
    return this.paymentDate;
  }

  getAdjustmentReason(): string | undefined {
    return this.adjustmentReason;
  }

  getClawbackAmount(): number {
    return this.clawbackAmount;
  }

  getClawbackReason(): string | undefined {
    return this.clawbackReason;
  }

  getClawbackDate(): Date | undefined {
    return this.clawbackDate;
  }

  getQuotaAttained(): number | undefined {
    return this.quotaAttained;
  }

  getQuotaTarget(): number | undefined {
    return this.quotaTarget;
  }

  getAcceleratorApplied(): number | undefined {
    return this.acceleratorApplied;
  }

  getTenantId(): string {
    return this.tenantId;
  }

  getOrganizationId(): string {
    return this.organizationId;
  }

  getApprovedBy(): string | undefined {
    return this.approvedBy;
  }

  getApprovedAt(): Date | undefined {
    return this.approvedAt;
  }

  getPaidVia(): string | undefined {
    return this.paidVia;
  }

  getPaymentReference(): string | undefined {
    return this.paymentReference;
  }

  // Setters
  setSalesRepName(name: string): void {
    this.salesRepName = name;
    this.markAsUpdated();
  }

  setSalesAmount(amount: number): void {
    if (amount < 0) {
      throw new Error('Sales amount cannot be negative');
    }
    this.salesAmount = amount;
    this.calculatedAmount = (amount * this.commissionRate) / 100;
    this.calculateFinalAmount();
    this.markAsUpdated();
  }

  setCommissionRate(rate: number): void {
    if (rate < 0) {
      throw new Error('Commission rate cannot be negative');
    }
    this.commissionRate = rate;
    this.calculatedAmount = (this.salesAmount * rate) / 100;
    this.calculateFinalAmount();
    this.markAsUpdated();
  }

  setCurrency(currency: string): void {
    if (!/^[A-Z]{3}$/.test(currency)) {
      throw new Error('Currency must be a valid ISO 4217 code');
    }
    this.currency = currency;
    this.markAsUpdated();
  }

  toObject(): Record<string, unknown> {
    return {
      _id: this.id,
      salesRepId: this.salesRepId,
      salesRepName: this.salesRepName,
      periodId: this.periodId,
      ruleId: this.ruleId,
      ruleName: this.ruleName,
      status: this.status,
      salesAmount: this.salesAmount,
      commissionRate: this.commissionRate,
      calculatedAmount: this.calculatedAmount,
      adjustedAmount: this.adjustedAmount,
      finalAmount: this.finalAmount,
      currency: this.currency,
      transactionDate: this.transactionDate,
      settlementDate: this.settlementDate,
      paymentDate: this.paymentDate,
      splits: this.splits.map(s => s.toObject()),
      adjustmentReason: this.adjustmentReason,
      clawbackAmount: this.clawbackAmount,
      clawbackReason: this.clawbackReason,
      clawbackDate: this.clawbackDate,
      quotaAttained: this.quotaAttained,
      quotaTarget: this.quotaTarget,
      quotaPercentage: this.getQuotaPercentage(),
      acceleratorApplied: this.acceleratorApplied,
      productSales: this.productSales,
      tenantId: this.tenantId,
      organizationId: this.organizationId,
      approvedBy: this.approvedBy,
      approvedAt: this.approvedAt,
      paidVia: this.paidVia,
      paymentReference: this.paymentReference,
      isOverdue: this.isOverdue(),
      hasSplit: this.hasSplit(),
      createdAt: this.createdAtDate,
      updatedAt: this.updatedAtDate,
      version: this.getVersion,
    };
  }

  static fromObject(obj: Record<string, unknown>): Commission {
    const commission = new Commission(
      obj.salesRepId as string,
      obj.salesRepName as string,
      obj.periodId as string,
      obj.ruleId as string,
      obj.ruleName as string,
      obj.salesAmount as number,
      obj.commissionRate as number,
      obj.tenantId as string,
      obj.organizationId as string,
      obj.transactionDate ? new Date(obj.transactionDate as Date) : new Date(),
      obj.currency as string ?? 'USD',
    );

    if (obj.id) commission.id = obj.id as string;

    // Set internal state
    (commission as any).status = obj.status as CommissionStatus ?? CommissionStatus.DRAFT;
    (commission as any).calculatedAmount = obj.calculatedAmount as number ?? commission.getCalculatedAmount();
    (commission as any).adjustedAmount = obj.adjustedAmount as number ?? 0;
    (commission as any).finalAmount = obj.finalAmount as number ?? commission.getFinalAmount();
    (commission as any).settlementDate = obj.settlementDate ? new Date(obj.settlementDate as Date) : undefined;
    (commission as any).paymentDate = obj.paymentDate ? new Date(obj.paymentDate as Date) : undefined;
    (commission as any).adjustmentReason = obj.adjustmentReason as string | undefined;
    (commission as any).clawbackAmount = obj.clawbackAmount as number ?? 0;
    (commission as any).clawbackReason = obj.clawbackReason as string | undefined;
    (commission as any).clawbackDate = obj.clawbackDate ? new Date(obj.clawbackDate as Date) : undefined;
    (commission as any).quotaAttained = obj.quotaAttained as number | undefined;
    (commission as any).quotaTarget = obj.quotaTarget as number | undefined;
    (commission as any).acceleratorApplied = obj.acceleratorApplied as number | undefined;
    (commission as any).approvedBy = obj.approvedBy as string | undefined;
    (commission as any).approvedAt = obj.approvedAt ? new Date(obj.approvedAt as Date) : undefined;
    (commission as any).paidVia = obj.paidVia as string | undefined;
    (commission as any).paymentReference = obj.paymentReference as string | undefined;
    (commission as any).createdAt = obj.createdAt ? new Date(obj.createdAt as Date) : commission.createdAtDate;
    (commission as any).updatedAt = obj.updatedAt ? new Date(obj.updatedAt as Date) : commission.updatedAtDate;
    (commission as any).version = obj.version as number ?? 0;

    // Add splits
    if (obj.splits && Array.isArray(obj.splits)) {
      (obj.splits as any[]).forEach(splitObj => {
        commission.addSplit(CommissionSplit.fromObject(splitObj));
      });
    }

    // Add product sales
    if (obj.productSales && Array.isArray(obj.productSales)) {
      (obj.productSales as any[]).forEach(ps => {
        commission.addProductSale(ps.productId, ps.productName, ps.amount, ps.commission);
      });
    }

    return commission;
  }
}
