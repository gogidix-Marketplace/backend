/**
 * Commission split value object
 * Represents how commission is distributed among multiple sales reps
 */
export class CommissionSplit {
  constructor(
    private readonly salesRepId: string,
    private readonly salesRepName: string,
    private readonly percentage: number,
    private readonly role: 'PRIMARY' | 'SECONDARY' | 'SUPPORT',
  ) {
    if (percentage <= 0 || percentage > 100) {
      throw new Error('Percentage must be between 0 and 100');
    }
  }

  getSalesRepId(): string {
    return this.salesRepId;
  }

  getSalesRepName(): string {
    return this.salesRepName;
  }

  getPercentage(): number {
    return this.percentage;
  }

  getRole(): string {
    return this.role;
  }

  calculateShare(totalAmount: number): number {
    return (totalAmount * this.percentage) / 100;
  }

  toObject(): {
    salesRepId: string;
    salesRepName: string;
    percentage: number;
    role: string;
  } {
    return {
      salesRepId: this.salesRepId,
      salesRepName: this.salesRepName,
      percentage: this.percentage,
      role: this.role,
    };
  }

  static fromObject(obj: {
    salesRepId: string;
    salesRepName: string;
    percentage: number;
    role: string;
  }): CommissionSplit {
    return new CommissionSplit(
      obj.salesRepId,
      obj.salesRepName,
      obj.percentage,
      obj.role as 'PRIMARY' | 'SECONDARY' | 'SUPPORT',
    );
  }
}

/**
 * Validates that splits add up to 100%
 */
export function validateSplitsTotal(splits: CommissionSplit[]): void {
  const total = splits.reduce((sum, split) => sum + split.getPercentage(), 0);
  if (Math.abs(total - 100) > 0.01) {
    throw new Error(`Commission splits must total 100%, got ${total}%`);
  }
}
