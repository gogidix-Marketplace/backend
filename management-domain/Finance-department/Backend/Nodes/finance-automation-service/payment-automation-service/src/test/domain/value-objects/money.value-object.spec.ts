import { Money } from '../../../domain/models/value-objects/money.value-object';

describe('Money Value Object', () => {
  describe('Creation', () => {
    it('should create money with valid amount and currency', () => {
      const money = Money.create(100.50, 'USD');

      expect(money.amount).toBe(100.50);
      expect(money.currency).toBe('USD');
    });

    it('should create money with default currency', () => {
      const money = Money.create(50);

      expect(money.amount).toBe(50);
      expect(money.currency).toBe('USD');
    });

    it('should create zero money', () => {
      const money = Money.zero('EUR');

      expect(money.amount).toBe(0);
      expect(money.currency).toBe('EUR');
    });

    it('should round amount to 2 decimal places', () => {
      const money = Money.create(100.456, 'USD');

      expect(money.amount).toBe(100.46);
    });

    it('should throw error for negative amount', () => {
      expect(() => {
        Money.create(-10, 'USD');
      }).toThrow('Amount cannot be negative');
    });

    it('should throw error for invalid currency code', () => {
      expect(() => {
        Money.create(100, 'US');
      }).toThrow('Currency must be a valid ISO 4217 code');
    });

    it('should throw error for empty currency', () => {
      expect(() => {
        Money.create(100, '');
      }).toThrow('Currency must be a valid ISO 4217 code');
    });

    it('should convert currency to uppercase', () => {
      const money = Money.create(100, 'usd');

      expect(money.currency).toBe('USD');
    });
  });

  describe('Arithmetic Operations', () => {
    let money1: Money;
    let money2: Money;

    beforeEach(() => {
      money1 = Money.create(100, 'USD');
      money2 = Money.create(50, 'USD');
    });

    it('should add two money amounts', () => {
      const result = money1.add(money2);

      expect(result.amount).toBe(150);
      expect(result.currency).toBe('USD');
    });

    it('should throw error when adding different currencies', () => {
      const eur = Money.create(50, 'EUR');

      expect(() => {
        money1.add(eur);
      }).toThrow('Cannot add money with different currencies');
    });

    it('should subtract two money amounts', () => {
      const result = money1.subtract(money2);

      expect(result.amount).toBe(50);
      expect(result.currency).toBe('USD');
    });

    it('should throw error when subtracting different currencies', () => {
      const eur = Money.create(50, 'EUR');

      expect(() => {
        money1.subtract(eur);
      }).toThrow('Cannot subtract money with different currencies');
    });

    it('should throw error when subtraction results in negative', () => {
      expect(() => {
        money2.subtract(money1);
      }).toThrow('Result cannot be negative');
    });

    it('should multiply money by factor', () => {
      const result = money1.multiply(2);

      expect(result.amount).toBe(200);
      expect(result.currency).toBe('USD');
    });

    it('should throw error when multiplying by negative factor', () => {
      expect(() => {
        money1.multiply(-1);
      }).toThrow('Factor cannot be negative');
    });

    it('should divide money by divisor', () => {
      const result = money1.divide(2);

      expect(result.amount).toBe(50);
      expect(result.currency).toBe('USD');
    });

    it('should throw error when dividing by zero', () => {
      expect(() => {
        money1.divide(0);
      }).toThrow('Divisor must be positive');
    });

    it('should throw error when dividing by negative number', () => {
      expect(() => {
        money1.divide(-1);
      }).toThrow('Divisor must be positive');
    });
  });

  describe('Comparison Operations', () => {
    let money1: Money;
    let money2: Money;
    let money3: Money;

    beforeEach(() => {
      money1 = Money.create(100, 'USD');
      money2 = Money.create(50, 'USD');
      money3 = Money.create(100, 'USD');
    });

    it('should check if money is greater than another', () => {
      expect(money1.isGreaterThan(money2)).toBe(true);
      expect(money2.isGreaterThan(money1)).toBe(false);
    });

    it('should throw error when comparing different currencies', () => {
      const eur = Money.create(50, 'EUR');

      expect(() => {
        money1.isGreaterThan(eur);
      }).toThrow('Cannot compare money with different currencies');
    });

    it('should check if money is less than another', () => {
      expect(money2.isLessThan(money1)).toBe(true);
      expect(money1.isLessThan(money2)).toBe(false);
    });

    it('should check equality of money', () => {
      expect(money1.equals(money3)).toBe(true);
      expect(money1.equals(money2)).toBe(false);
    });

    it('should return false when comparing with non-money object', () => {
      expect(money1.equals({ amount: 100, currency: 'USD' } as any)).toBe(false);
    });

    it('should check if money is zero', () => {
      const zeroMoney = Money.zero('USD');

      expect(zeroMoney.isZero()).toBe(true);
      expect(money1.isZero()).toBe(false);
    });

    it('should check if money is positive', () => {
      expect(money1.isPositive()).toBe(true);
      expect(Money.zero('USD').isPositive()).toBe(false);
    });
  });

  describe('String Formatting', () => {
    it('should format money to string', () => {
      const money = Money.create(100.50, 'USD');

      expect(money.toString()).toBe('USD 100.50');
    });

    it('should format money to localized string', () => {
      const money = Money.create(1000.50, 'USD');

      expect(money.toFormattedString('en-US')).toBe('$1,000.50');
    });

    it('should format money to EUR locale', () => {
      const money = Money.create(1000.50, 'EUR');

      expect(money.toFormattedString('de-DE')).toContain('1.000,50');
    });
  });

  describe('JSON Serialization', () => {
    it('should convert money to JSON', () => {
      const money = Money.create(100.50, 'USD');
      const json = money.toJSON();

      expect(json).toEqual({
        amount: 100.50,
        currency: 'USD'
      });
    });

    it('should create money from JSON', () => {
      const json = {
        amount: 100.50,
        currency: 'USD'
      };

      const money = Money.fromJSON(json);

      expect(money.amount).toBe(100.50);
      expect(money.currency).toBe('USD');
    });
  });

  describe('Edge Cases', () => {
    it('should handle very small amounts', () => {
      const money = Money.create(0.01, 'USD');

      expect(money.amount).toBe(0.01);
      expect(money.isPositive()).toBe(true);
    });

    it('should handle very large amounts', () => {
      const money = Money.create(999999999.99, 'USD');

      expect(money.amount).toBe(999999999.99);
    });

    it('should handle rounding correctly', () => {
      const money = Money.create(100.995, 'USD');

      expect(money.amount).toBe(101); // Round half up from .995
    });

    it('should handle multiple operations', () => {
      const money1 = Money.create(100, 'USD');
      const money2 = Money.create(50, 'USD');
      const money3 = Money.create(25, 'USD');

      const result = money1.add(money2).subtract(money3);

      expect(result.amount).toBe(125);
    });
  });

  describe('Different Currencies', () => {
    it('should create money with EUR', () => {
      const money = Money.create(100, 'EUR');

      expect(money.currency).toBe('EUR');
    });

    it('should create money with GBP', () => {
      const money = Money.create(100, 'GBP');

      expect(money.currency).toBe('GBP');
    });

    it('should create money with JPY', () => {
      const money = Money.create(1000, 'JPY');

      expect(money.currency).toBe('JPY');
    });
  });

  describe('Getters', () => {
    it('should get amount', () => {
      const money = Money.create(100.50, 'USD');

      expect(money.amount).toBe(100.50);
    });

    it('should get currency', () => {
      const money = Money.create(100.50, 'EUR');

      expect(money.currency).toBe('EUR');
    });

    it('should get currency using getCurrency method', () => {
      const money = Money.create(100.50, 'GBP');

      expect(money.getCurrency()).toBe('GBP');
    });
  });
});
