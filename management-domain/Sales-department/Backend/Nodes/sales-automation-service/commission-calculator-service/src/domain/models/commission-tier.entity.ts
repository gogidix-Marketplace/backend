import { CommissionTierType } from '../enums/commission-rule-type.enum';

/**
 * Commission Tier entity
 * Represents a tier in a tiered commission structure
 */
export class CommissionTier {
  private readonly id: string;
  private name: string;
  private tierType: CommissionTierType;
  private minThreshold: number;
  private maxThreshold: number | null; // null means unlimited
  private commissionRate: number;
  private fixedAmount?: number;
  private multiplier?: number;

  constructor(
    id: string,
    name: string,
    tierType: CommissionTierType,
    minThreshold: number,
    commissionRate: number,
    maxThreshold?: number,
  ) {
    this.id = id;
    this.name = name;
    this.tierType = tierType;
    this.minThreshold = minThreshold;
    this.maxThreshold = maxThreshold ?? null;
    this.commissionRate = commissionRate;

    this.validate();
  }

  private validate(): void {
    if (this.minThreshold < 0) {
      throw new Error('Minimum threshold cannot be negative');
    }
    if (this.maxThreshold !== null && this.maxThreshold <= this.minThreshold) {
      throw new Error('Maximum threshold must be greater than minimum threshold');
    }
    if (this.commissionRate < 0) {
      throw new Error('Commission rate cannot be negative');
    }
  }

  /**
   * Check if a value falls within this tier
   */
  isInRange(value: number): boolean {
    return value >= this.minThreshold && (this.maxThreshold === null || value <= this.maxThreshold);
  }

  /**
   * Calculate commission for this tier
   * Returns the portion of value that falls within this tier and the commission for it
   */
  calculate(value: number): { applicableAmount: number; commission: number } {
    if (!this.isInRange(value)) {
      return { applicableAmount: 0, commission: 0 };
    }

    const applicableAmount = this.maxThreshold === null
      ? value - this.minThreshold
      : Math.min(value, this.maxThreshold) - this.minThreshold;

    const commission = this.fixedAmount
      ? this.fixedAmount
      : (applicableAmount * this.commissionRate) / 100;

    return {
      applicableAmount: Math.max(0, applicableAmount),
      commission: Math.max(0, commission) * (this.multiplier || 1),
    };
  }

  getId(): string {
    return this.id;
  }

  getName(): string {
    return this.name;
  }

  getTierType(): CommissionTierType {
    return this.tierType;
  }

  getMinThreshold(): number {
    return this.minThreshold;
  }

  getMaxThreshold(): number | null {
    return this.maxThreshold;
  }

  getCommissionRate(): number {
    return this.commissionRate;
  }

  getFixedAmount(): number | undefined {
    return this.fixedAmount;
  }

  getMultiplier(): number | undefined {
    return this.multiplier;
  }

  setFixedAmount(amount: number): void {
    this.fixedAmount = amount;
  }

  setMultiplier(multiplier: number): void {
    this.multiplier = multiplier;
  }

  toObject(): {
    id: string;
    name: string;
    tierType: string;
    minThreshold: number;
    maxThreshold: number | null;
    commissionRate: number;
    fixedAmount?: number;
    multiplier?: number;
  } {
    return {
      id: this.id,
      name: this.name,
      tierType: this.tierType,
      minThreshold: this.minThreshold,
      maxThreshold: this.maxThreshold,
      commissionRate: this.commissionRate,
      fixedAmount: this.fixedAmount,
      multiplier: this.multiplier,
    };
  }

  static fromObject(obj: any): CommissionTier {
    const tier = new CommissionTier(
      obj.id,
      obj.name,
      obj.tierType as CommissionTierType,
      obj.minThreshold,
      obj.commissionRate,
      obj.maxThreshold ?? undefined,
    );

    if (obj.fixedAmount !== undefined) {
      tier.setFixedAmount(obj.fixedAmount);
    }
    if (obj.multiplier !== undefined) {
      tier.setMultiplier(obj.multiplier);
    }

    return tier;
  }
}
