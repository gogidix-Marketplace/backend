import { BaseEntity } from '../../shared/base/base.entity';
import {
  CommissionCalculationType,
  CommissionApplicationScope,
  AcceleratorType,
} from '../enums/commission-rule-type.enum';
import { CommissionTier } from './commission-tier.entity';

/**
 * Commission Rule entity
 * Defines how commissions are calculated for sales reps
 */
export class CommissionRule extends BaseEntity {
  private name: string;
  private description?: string;
  private calculationType: CommissionCalculationType;
  private baseRate: number;
  private tiers: CommissionTier[];
  private applicationScope: CommissionApplicationScope;
  private scopeFilters: Record<string, string[]>;
  private acceleratorType: AcceleratorType;
  private acceleratorThreshold?: number;
  private acceleratorMultiplier?: number;
  private capType?: 'NONE' | 'AMOUNT' | 'PERCENTAGE';
  private capValue?: number;
  private isActive: boolean;
  private effectiveDate: Date;
  private expirationDate?: Date;
  private tenantId: string;
  private organizationId: string;
  private productRates: Map<string, number>; // Product ID -> rate
  private customerRates: Map<string, number>; // Customer ID -> rate

  constructor(
    name: string,
    calculationType: CommissionCalculationType,
    baseRate: number,
    tenantId: string,
    organizationId: string,
    effectiveDate: Date = new Date(),
  ) {
    super();
    this.name = name;
    this.calculationType = calculationType;
    this.baseRate = baseRate;
    this.tiers = [];
    this.applicationScope = CommissionApplicationScope.ALL_SALES;
    this.scopeFilters = {};
    this.acceleratorType = AcceleratorType.NONE;
    this.isActive = true;
    this.effectiveDate = effectiveDate;
    this.tenantId = tenantId;
    this.organizationId = organizationId;
    this.productRates = new Map();
    this.customerRates = new Map();
  }

  // Tier management
  addTier(tier: CommissionTier): void {
    // Check for overlapping ranges
    for (const existingTier of this.tiers) {
      if (this.tiersOverlap(existingTier, tier)) {
        throw new Error(`Tier range overlaps with existing tier ${existingTier.getName()}`);
      }
    }
    this.tiers.push(tier);
    this.markAsUpdated();
  }

  removeTier(tierId: string): void {
    this.tiers = this.tiers.filter(t => t.getId() !== tierId);
    this.markAsUpdated();
  }

  getTierForValue(value: number): CommissionTier | null {
    return this.tiers.find(t => t.isInRange(value)) ?? null;
  }

  getAllTiers(): CommissionTier[] {
    return [...this.tiers];
  }

  private tiersOverlap(tier1: CommissionTier, tier2: CommissionTier): boolean {
    const min1 = tier1.getMinThreshold();
    const max1 = tier1.getMaxThreshold() ?? Infinity;
    const min2 = tier2.getMinThreshold();
    const max2 = tier2.getMaxThreshold() ?? Infinity;

    return !(max1 < min2 || max2 < min1);
  }

  // Commission calculation logic
  calculateCommission(saleAmount: number, quotaAttained?: number, productId?: string, customerId?: string): number {
    let rate = this.baseRate;
    let applicableAmount = saleAmount;

    // Check for product-specific rate
    if (productId && this.productRates.has(productId)) {
      rate = this.productRates.get(productId)!;
    }
    // Check for customer-specific rate
    else if (customerId && this.customerRates.has(customerId)) {
      rate = this.customerRates.get(customerId)!;
    }
    // Check tiered commission
    else if (this.calculationType === CommissionCalculationType.TIERED && this.tiers.length > 0) {
      return this.calculateTieredCommission(saleAmount);
    }

    // Apply accelerator if quota-based
    if (quotaAttained !== undefined && this.acceleratorType !== AcceleratorType.NONE) {
      const multiplier = this.calculateAccelerator(quotaAttained);
      rate *= multiplier;
    }

    let commission = (applicableAmount * rate) / 100;

    // Apply cap if configured
    commission = this.applyCap(commission, saleAmount);

    return Math.max(0, commission);
  }

  private calculateTieredCommission(saleAmount: number): number {
    let totalCommission = 0;
    let remainingAmount = saleAmount;

    // Sort tiers by threshold
    const sortedTiers = [...this.tiers].sort((a, b) => a.getMinThreshold() - b.getMinThreshold());

    for (const tier of sortedTiers) {
      if (remainingAmount <= 0) break;

      const result = tier.calculate(saleAmount);
      if (result.applicableAmount > 0) {
        totalCommission += result.commission;
        if (tier.getMaxThreshold() !== null) {
          remainingAmount -= Math.min(remainingAmount, tier.getMaxThreshold() - tier.getMinThreshold());
        }
      }
    }

    return totalCommission;
  }

  private calculateAccelerator(quotaAttained: number): number {
    if (!this.acceleratorThreshold || !this.acceleratorMultiplier) {
      return 1;
    }

    if (quotaAttained < this.acceleratorThreshold) {
      return 1;
    }

    switch (this.acceleratorType) {
      case AcceleratorType.LINEAR:
        // Linear increase based on how much quota is exceeded
        const excessRatio = (quotaAttained - this.acceleratorThreshold) / this.acceleratorThreshold;
        return 1 + (this.acceleratorMultiplier - 1) * Math.min(excessRatio, 1);

      case AcceleratorType.STEP_UP:
        // Step up once threshold is passed
        return this.acceleratorMultiplier;

      case AcceleratorType.RETROACTIVE:
        // Retroactive - all sales get the multiplier
        return this.acceleratorMultiplier;

      default:
        return 1;
    }
  }

  private applyCap(commission: number, saleAmount: number): number {
    if (this.capType === 'NONE' || !this.capValue) {
      return commission;
    }

    if (this.capType === 'AMOUNT') {
      return Math.min(commission, this.capValue);
    }

    if (this.capType === 'PERCENTAGE') {
      const maxCommission = (saleAmount * this.capValue) / 100;
      return Math.min(commission, maxCommission);
    }

    return commission;
  }

  // Product and customer specific rates
  setProductRate(productId: string, rate: number): void {
    this.productRates.set(productId, rate);
    this.markAsUpdated();
  }

  removeProductRate(productId: string): void {
    this.productRates.delete(productId);
    this.markAsUpdated();
  }

  getProductRate(productId: string): number | undefined {
    return this.productRates.get(productId);
  }

  setCustomerRate(customerId: string, rate: number): void {
    this.customerRates.set(customerId, rate);
    this.markAsUpdated();
  }

  removeCustomerRate(customerId: string): void {
    this.customerRates.delete(customerId);
    this.markAsUpdated();
  }

  // Activation and deactivation
  activate(): void {
    this.isActive = true;
    this.markAsUpdated();
  }

  deactivate(): void {
    this.isActive = false;
    this.markAsUpdated();
  }

  isActiveRule(): boolean {
    if (!this.isActive) return false;
    const now = new Date();
    if (now < this.effectiveDate) return false;
    if (this.expirationDate && now > this.expirationDate) return false;
    return true;
  }

  // Getters
  getName(): string {
    return this.name;
  }

  getDescription(): string | undefined {
    return this.description;
  }

  getCalculationType(): CommissionCalculationType {
    return this.calculationType;
  }

  getBaseRate(): number {
    return this.baseRate;
  }

  getApplicationScope(): CommissionApplicationScope {
    return this.applicationScope;
  }

  getScopeFilters(): Record<string, string[]> {
    return { ...this.scopeFilters };
  }

  getAcceleratorType(): AcceleratorType {
    return this.acceleratorType;
  }

  getAcceleratorThreshold(): number | undefined {
    return this.acceleratorThreshold;
  }

  getAcceleratorMultiplier(): number | undefined {
    return this.acceleratorMultiplier;
  }

  getCapType(): 'NONE' | 'AMOUNT' | 'PERCENTAGE' | undefined {
    return this.capType;
  }

  getCapValue(): number | undefined {
    return this.capValue;
  }

  getEffectiveDate(): Date {
    return this.effectiveDate;
  }

  getExpirationDate(): Date | undefined {
    return this.expirationDate;
  }

  getTenantId(): string {
    return this.tenantId;
  }

  getOrganizationId(): string {
    return this.organizationId;
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

  setBaseRate(rate: number): void {
    if (rate < 0) {
      throw new Error('Base rate cannot be negative');
    }
    this.baseRate = rate;
    this.markAsUpdated();
  }

  setApplicationScope(scope: CommissionApplicationScope): void {
    this.applicationScope = scope;
    this.markAsUpdated();
  }

  setScopeFilters(filters: Record<string, string[]>): void {
    this.scopeFilters = { ...filters };
    this.markAsUpdated();
  }

  setAccelerator(type: AcceleratorType, threshold?: number, multiplier?: number): void {
    this.acceleratorType = type;
    this.acceleratorThreshold = threshold;
    this.acceleratorMultiplier = multiplier;
    this.markAsUpdated();
  }

  setCap(type: 'NONE' | 'AMOUNT' | 'PERCENTAGE', value?: number): void {
    this.capType = type;
    this.capValue = value;
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
      calculationType: this.calculationType,
      baseRate: this.baseRate,
      tiers: this.tiers.map(t => t.toObject()),
      applicationScope: this.applicationScope,
      scopeFilters: this.scopeFilters,
      acceleratorType: this.acceleratorType,
      acceleratorThreshold: this.acceleratorThreshold,
      acceleratorMultiplier: this.acceleratorMultiplier,
      capType: this.capType,
      capValue: this.capValue,
      isActive: this.isActive,
      effectiveDate: this.effectiveDate,
      expirationDate: this.expirationDate,
      tenantId: this.tenantId,
      organizationId: this.organizationId,
      productRates: Array.from(this.productRates.entries()),
      customerRates: Array.from(this.customerRates.entries()),
      createdAt: this.createdAtDate,
      updatedAt: this.updatedAtDate,
      version: this.getVersion,
    };
  }

  static fromObject(obj: Record<string, unknown>): CommissionRule {
    const rule = new CommissionRule(
      obj.name as string,
      obj.calculationType as CommissionCalculationType,
      obj.baseRate as number,
      obj.tenantId as string,
      obj.organizationId as string,
      new Date(obj.effectiveDate as Date),
    );

    if (obj.id) rule.id = obj.id as string;
    if (obj.description) rule.setDescription(obj.description as string);
    if (obj.applicationScope) rule.setApplicationScope(obj.applicationScope as CommissionApplicationScope);
    if (obj.scopeFilters) rule.setScopeFilters(obj.scopeFilters as Record<string, string[]>);
    if (obj.expirationDate) rule.setExpirationDate(new Date(obj.expirationDate as Date));

    if (obj.acceleratorType) {
      rule.setAccelerator(
        obj.acceleratorType as AcceleratorType,
        obj.acceleratorThreshold as number | undefined,
        obj.acceleratorMultiplier as number | undefined,
      );
    }

    if (obj.capType) {
      rule.setCap(obj.capType as 'NONE' | 'AMOUNT' | 'PERCENTAGE', obj.capValue as number | undefined);
    }

    if (obj.tiers && Array.isArray(obj.tiers)) {
      (obj.tiers as any[]).forEach(tierObj => {
        rule.addTier(CommissionTier.fromObject(tierObj));
      });
    }

    if (obj.productRates && Array.isArray(obj.productRates)) {
      (obj.productRates as [string, number][]).forEach(([productId, rate]) => {
        rule.setProductRate(productId, rate);
      });
    }

    if (obj.customerRates && Array.isArray(obj.customerRates)) {
      (obj.customerRates as [string, number][]).forEach(([customerId, rate]) => {
        rule.setCustomerRate(customerId, rate);
      });
    }

    // Set internal state
    (rule as any).isActive = obj.isActive ?? true;
    (rule as any).createdAt = obj.createdAt ? new Date(obj.createdAt as Date) : rule.createdAtDate;
    (rule as any).updatedAt = obj.updatedAt ? new Date(obj.updatedAt as Date) : rule.updatedAtDate;
    (rule as any).version = obj.version as number ?? 0;

    return rule;
  }
}
