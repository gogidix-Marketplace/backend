import Decimal from 'decimal.js';

/**
 * Money value object
 * Represents monetary values with precise decimal arithmetic
 */
export class Money {
  private readonly amount: Decimal;
  private readonly currency: string;

  constructor(amount: number | string | Decimal, currency: string = 'USD') {
    this.amount = new Decimal(amount);
    this.currency = currency;
  }

  static zero(currency: string = 'USD'): Money {
    return new Money(0, currency);
  }

  add(other: Money): Money {
    this.assertSameCurrency(other);
    return new Money(this.amount.plus(other.amount), this.currency);
  }

  subtract(other: Money): Money {
    this.assertSameCurrency(other);
    return new Money(this.amount.minus(other.amount), this.currency);
  }

  multiply(factor: number): Money {
    return new Money(this.amount.times(factor), this.currency);
  }

  divide(divisor: number): Money {
    return new Money(this.amount.div(divisor), this.currency);
  }

  percentage(percent: number): Money {
    return new Money(this.amount.times(percent).div(100), this.currency);
  }

  compareTo(other: Money): number {
    this.assertSameCurrency(other);
    return this.amount.comparedTo(other.amount);
  }

  greaterThan(other: Money): boolean {
    return this.compareTo(other) > 0;
  }

  lessThan(other: Money): boolean {
    return this.compareTo(other) < 0;
  }

  equals(other: Money): boolean {
    return this.currency === other.currency && this.amount.equals(other.amount);
  }

  isNegative(): boolean {
    return this.amount.isNegative();
  }

  isPositive(): boolean {
    return this.amount.isPositive();
  }

  isZero(): boolean {
    return this.amount.isZero();
  }

  toNumber(): number {
    return this.amount.toNumber();
  }

  toString(): string {
    return `${this.amount.toFixed(2)} ${this.currency}`;
  }

  getCurrency(): string {
    return this.currency;
  }

  getAmount(): Decimal {
    return this.amount;
  }

  toPlainObject(): { amount: number; currency: string } {
    return {
      amount: this.amount.toNumber(),
      currency: this.currency,
    };
  }

  static fromObject(obj: { amount: number; currency: string }): Money {
    return new Money(obj.amount, obj.currency);
  }

  private assertSameCurrency(other: Money): void {
    if (this.currency !== other.currency) {
      throw new Error(`Cannot operate on different currencies: ${this.currency} vs ${other.currency}`);
    }
  }
}
