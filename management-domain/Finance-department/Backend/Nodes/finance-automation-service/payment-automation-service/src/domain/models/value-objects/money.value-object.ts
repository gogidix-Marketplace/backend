import { BaseValueObject } from '@shared/base/base.value-object';

export class Money extends BaseValueObject {
  private readonly _amount: number;
  private readonly _currency: string;

  private constructor(amount: number, currency: string) {
    super();
    if (amount < 0) {
      throw new Error('Amount cannot be negative');
    }
    if (!currency || currency.length !== 3) {
      throw new Error('Currency must be a valid ISO 4217 code');
    }
    this._amount = Math.round(amount * 100) / 100; // Round to 2 decimal places
    this._currency = currency.toUpperCase();
  }

  static create(amount: number, currency: string = 'USD'): Money {
    return new Money(amount, currency);
  }

  static zero(currency: string = 'USD'): Money {
    return new Money(0, currency);
  }

  get amount(): number {
    return this._amount;
  }

  get currency(): string {
    return this._currency;
  }

  getCurrency(): string {
    return this._currency;
  }

  add(other: Money): Money {
    if (this._currency !== other._currency) {
      throw new Error('Cannot add money with different currencies');
    }
    return new Money(this._amount + other._amount, this._currency);
  }

  subtract(other: Money): Money {
    if (this._currency !== other._currency) {
      throw new Error('Cannot subtract money with different currencies');
    }
    const result = this._amount - other._amount;
    if (result < 0) {
      throw new Error('Result cannot be negative');
    }
    return new Money(result, this._currency);
  }

  multiply(factor: number): Money {
    if (factor < 0) {
      throw new Error('Factor cannot be negative');
    }
    return new Money(this._amount * factor, this._currency);
  }

  divide(divisor: number): Money {
    if (divisor <= 0) {
      throw new Error('Divisor must be positive');
    }
    return new Money(this._amount / divisor, this._currency);
  }

  isGreaterThan(other: Money): boolean {
    this.ensureSameCurrency(other);
    return this._amount > other._amount;
  }

  isLessThan(other: Money): boolean {
    this.ensureSameCurrency(other);
    return this._amount < other._amount;
  }

  equals(other: Money): boolean {
    if (!(other instanceof Money)) {
      return false;
    }
    return this._amount === other._amount && this._currency === other._currency;
  }

  isZero(): boolean {
    return this._amount === 0;
  }

  isPositive(): boolean {
    return this._amount > 0;
  }

  toFormattedString(locale: string = 'en-US'): string {
    return new Intl.NumberFormat(locale, {
      style: 'currency',
      currency: this._currency,
    }).format(this._amount);
  }

  toJSON(): Record<string, any> {
    return {
      amount: this._amount,
      currency: this._currency,
    };
  }

  static fromJSON(json: Record<string, any>): Money {
    return new Money(json.amount, json.currency);
  }

  private ensureSameCurrency(other: Money): void {
    if (this._currency !== other._currency) {
      throw new Error('Cannot compare money with different currencies');
    }
  }

  toString(): string {
    return `${this._currency} ${this._amount.toFixed(2)}`;
  }
}
