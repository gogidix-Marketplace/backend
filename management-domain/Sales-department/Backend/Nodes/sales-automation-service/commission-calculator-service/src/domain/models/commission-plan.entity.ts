import { BaseEntity } from '../../shared/base/base.entity';
import { CommissionCalculationType } from '../enums/commission-rule-type.enum';

/**
 * Commission Plan Type enumeration
 */
export enum CommissionPlanType {
  FLAT_RATE = 'FLAT_RATE',
  TIERED = 'TIERED',
  ACCELERATOR = 'ACCELERATOR',
  MATRIX = 'MATRIX',
  GRADUATED = 'GRADUATED',
}

/**
 * Commission Plan entity
 * Represents a comprehensive commission plan for sales reps
 */
export class CommissionPlan extends BaseEntity {
  private name: string;
  private description?: string;
  private planType: CommissionPlanType;
  private baseCommissionRate: number;
  private tiers: CommissionPlanTier[];
  private accelerators: CommissionAccelerator[];
  private matrix?: CommissionMatrix;
  private isActive: boolean;
  private effectiveDate: Date;
  private expirationDate?: Date;
  private tenantId: string;
  private organizationId: string;
  private assignedSalesRepIds: string[];
  private assignedTeamIds: string[];
  private currency: string;
  private minGuarantee?: number;
  private maxCap?: number;
  private drawType?: 'NONE' | 'RECOVERABLE' | 'NON_RECOVERABLE';
  private drawAmount?: number;
  private paymentTerms: PaymentTerms;

  constructor(
    name: string,
    planType: CommissionPlanType,
    baseCommissionRate: number,
    tenantId: string,
    organizationId: string,
    effectiveDate: Date = new Date(),
  ) {
    super();
    this.name = name;
    this.planType = planType;
    this.baseCommissionRate = baseCommissionRate;
    this.tiers = [];
    this.accelerators = [];
    this.isActive = true;
    this.effectiveDate = effectiveDate;
    this.tenantId = tenantId;
    this.organizationId = organizationId;
    this.assignedSalesRepIds = [];
    this.assignedTeamIds = [];
    this.currency = 'USD';
    this.paymentTerms = {
      paymentFrequency: 'MONTHLY',
      paymentDelayDays: 30,
    };
  }

  // Tier management
  addTier(tier: CommissionPlanTier): void {
    this.validateTier(tier);
    this.tiers.push(tier);
    this.markAsUpdated();
  }

  removeTier(tierId: string): void {
    this.tiers = this.tiers.filter(t => t.id !== tierId);
    this.markAsUpdated();
  }

  getTierForValue(value: number): CommissionPlanTier | undefined {
    return this.tiers.find(t => value >= t.minThreshold && (t.maxThreshold === null || value <= t.maxThreshold));
  }

  private validateTier(tier: CommissionPlanTier): void {
    for (const existingTier of this.tiers) {
      if (this.tiersOverlap(existingTier, tier)) {
        throw new Error(`Tier range overlaps with existing tier`);
      }
    }
  }

  private tiersOverlap(tier1: CommissionPlanTier, tier2: CommissionPlanTier): boolean {
    const min1 = tier1.minThreshold;
    const max1 = tier1.maxThreshold ?? Infinity;
    const min2 = tier2.minThreshold;
    const max2 = tier2.maxThreshold ?? Infinity;
    return !(max1 < min2 || max2 < min1);
  }

  // Accelerator management
  addAccelerator(accelerator: CommissionAccelerator): void {
    this.accelerators.push(accelerator);
    this.markAsUpdated();
  }

  removeAccelerator(acceleratorId: string): void {
    this.accelerators = this.accelerators.filter(a => a.id !== acceleratorId);
    this.markAsUpdated();
  }

  getAcceleratorForQuota(quotaAttained: number, quotaTarget: number): CommissionAccelerator | undefined {
    const percentage = (quotaAttained / quotaTarget) * 100;
    return this.accelerators.find(a => percentage >= a.thresholdPercentage);
  }

  // Assignment management
  assignSalesRep(salesRepId: string): void {
    if (!this.assignedSalesRepIds.includes(salesRepId)) {
      this.assignedSalesRepIds.push(salesRepId);
      this.markAsUpdated();
    }
  }

  unassignSalesRep(salesRepId: string): void {
    this.assignedSalesRepIds = this.assignedSalesRepIds.filter(id => id !== salesRepId);
    this.markAsUpdated();
  }

  assignTeam(teamId: string): void {
    if (!this.assignedTeamIds.includes(teamId)) {
      this.assignedTeamIds.push(teamId);
      this.markAsUpdated();
    }
  }

  unassignTeam(teamId: string): void {
    this.assignedTeamIds = this.assignedTeamIds.filter(id => id !== teamId);
    this.markAsUpdated();
  }

  // Activation
  activate(): void {
    this.isActive = true;
    this.markAsUpdated();
  }

  deactivate(): void {
    this.isActive = false;
    this.markAsUpdated();
  }

  isActivePlan(): boolean {
    if (!this.isActive) return false;
    const now = new Date();
    if (now < this.effectiveDate) return false;
    if (this.expirationDate && now > this.expirationDate) return false;
    return true;
  }

  // Payment terms
  setPaymentTerms(terms: PaymentTerms): void {
    this.paymentTerms = terms;
    this.markAsUpdated();
  }

  // Getters
  getName(): string {
    return this.name;
  }

  getDescription(): string | undefined {
    return this.description;
  }

  getPlanType(): CommissionPlanType {
    return this.planType;
  }

  getBaseCommissionRate(): number {
    return this.baseCommissionRate;
  }

  getTiers(): CommissionPlanTier[] {
    return [...this.tiers];
  }

  getAccelerators(): CommissionAccelerator[] {
    return [...this.accelerators];
  }

  getMatrix(): CommissionMatrix | undefined {
    return this.matrix;
  }

  getAssignedSalesRepIds(): string[] {
    return [...this.assignedSalesRepIds];
  }

  getAssignedTeamIds(): string[] {
    return [...this.assignedTeamIds];
  }

  getCurrency(): string {
    return this.currency;
  }

  getMinGuarantee(): number | undefined {
    return this.minGuarantee;
  }

  getMaxCap(): number | undefined {
    return this.maxCap;
  }

  getPaymentTerms(): PaymentTerms {
    return { ...this.paymentTerms };
  }

  // Setters
  setName(name: string): void {
    this.name = name;
    this.markAsUpdated();
  }

  setDescription(description: string): void {
    this.description = description;
    this.markAsUpdated();
  }

  setBaseCommissionRate(rate: number): void {
    if (rate < 0) {
      throw new Error('Base commission rate cannot be negative');
    }
    this.baseCommissionRate = rate;
    this.markAsUpdated();
  }

  setMatrix(matrix: CommissionMatrix): void {
    this.matrix = matrix;
    this.markAsUpdated();
  }

  setCurrency(currency: string): void {
    this.currency = currency;
    this.markAsUpdated();
  }

  setMinGuarantee(amount: number): void {
    this.minGuarantee = amount;
    this.markAsUpdated();
  }

  setMaxCap(amount: number): void {
    this.maxCap = amount;
    this.markAsUpdated();
  }

  setExpirationDate(date: Date): void {
    this.expirationDate = date;
    this.markAsUpdated();
  }

  toObject(): Record<string, unknown> {
    return {
      _id: this.id,
      name: this.name,
      description: this.description,
      planType: this.planType,
      baseCommissionRate: this.baseCommissionRate,
      tiers: this.tiers,
      accelerators: this.accelerators,
      matrix: this.matrix,
      isActive: this.isActive,
      effectiveDate: this.effectiveDate,
      expirationDate: this.expirationDate,
      tenantId: this.tenantId,
      organizationId: this.organizationId,
      assignedSalesRepIds: this.assignedSalesRepIds,
      assignedTeamIds: this.assignedTeamIds,
      currency: this.currency,
      minGuarantee: this.minGuarantee,
      maxCap: this.maxCap,
      drawType: this.drawType,
      drawAmount: this.drawAmount,
      paymentTerms: this.paymentTerms,
      createdAt: this.createdAtDate,
      updatedAt: this.updatedAtDate,
      version: this.getVersion,
    };
  }

  static fromObject(obj: Record<string, unknown>): CommissionPlan {
    const plan = new CommissionPlan(
      obj.name as string,
      obj.planType as CommissionPlanType,
      obj.baseCommissionRate as number,
      obj.tenantId as string,
      obj.organizationId as string,
      new Date(obj.effectiveDate as Date),
    );

    if (obj.id) plan.id = obj.id as string;
    if (obj.description) plan.setDescription(obj.description as string);
    if (obj.expirationDate) plan.setExpirationDate(new Date(obj.expirationDate as Date));
    if (obj.currency) plan.setCurrency(obj.currency as string);
    if (obj.minGuarantee) plan.setMinGuarantee(obj.minGuarantee as number);
    if (obj.maxCap) plan.setMaxCap(obj.maxCap as number);
    if (obj.matrix) plan.setMatrix(obj.matrix as CommissionMatrix);
    if (obj.tiers && Array.isArray(obj.tiers)) {
      (obj.tiers as CommissionPlanTier[]).forEach(tier => plan.addTier(tier));
    }
    if (obj.accelerators && Array.isArray(obj.accelerators)) {
      (obj.accelerators as CommissionAccelerator[]).forEach(acc => plan.addAccelerator(acc));
    }
    if (obj.assignedSalesRepIds && Array.isArray(obj.assignedSalesRepIds)) {
      (obj.assignedSalesRepIds as string[]).forEach(id => plan.assignSalesRep(id));
    }
    if (obj.assignedTeamIds && Array.isArray(obj.assignedTeamIds)) {
      (obj.assignedTeamIds as string[]).forEach(id => plan.assignTeam(id));
    }

    (plan as any).isActive = obj.isActive ?? true;
    (plan as any).createdAt = obj.createdAt ? new Date(obj.createdAt as Date) : plan.createdAtDate;
    (plan as any).updatedAt = obj.updatedAt ? new Date(obj.updatedAt as Date) : plan.updatedAtDate;
    (plan as any).version = obj.version as number ?? 0;

    return plan;
  }
}

/**
 * Commission plan tier
 */
export interface CommissionPlanTier {
  id: string;
  name: string;
  minThreshold: number;
  maxThreshold: number | null;
  commissionRate: number;
  fixedAmount?: number;
}

/**
 * Commission accelerator
 */
export interface CommissionAccelerator {
  id: string;
  name: string;
  thresholdPercentage: number;
  multiplier: number;
  type: 'LINEAR' | 'STEP_UP' | 'RETROACTIVE';
}

/**
 * Commission matrix for product/customer based calculations
 */
export interface CommissionMatrix {
  rows: MatrixRow[];
  defaultRate: number;
}

export interface MatrixRow {
  productId?: string;
  productCategory?: string;
  customerId?: string;
  customerSegment?: string;
  region?: string;
  rate: number;
}

/**
 * Payment terms
 */
export interface PaymentTerms {
  paymentFrequency: 'WEEKLY' | 'BI_WEEKLY' | 'MONTHLY' | 'QUARTERLY';
  paymentDelayDays: number;
  paymentMethod?: string;
}
