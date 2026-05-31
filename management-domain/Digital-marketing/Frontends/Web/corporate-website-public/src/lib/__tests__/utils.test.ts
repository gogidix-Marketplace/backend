import { cn, formatDate, formatNumber, truncate, slugify } from '../utils';

describe('Utils', () => {
  describe('cn', () => {
    it('merges class names correctly', () => {
      expect(cn('foo', 'bar')).toBe('foo bar');
    });

    it('handles conditional classes', () => {
      expect(cn('foo', false && 'bar', 'baz')).toBe('foo baz');
    });

    it('merges Tailwind classes correctly', () => {
      expect(cn('px-4 py-2', 'px-2')).toBe('py-2 px-2');
    });
  });

  describe('formatDate', () => {
    it('formats date string correctly', () => {
      const date = new Date('2024-12-25');
      expect(formatDate(date)).toContain('2024');
    });

    it('uses provided locale', () => {
      const date = new Date('2024-12-25');
      expect(formatDate(date, 'fr-FR')).toBeTruthy();
    });
  });

  describe('formatNumber', () => {
    it('formats number correctly', () => {
      expect(formatNumber(1234.56)).toContain('1,234');
    });

    it('uses provided locale', () => {
      expect(formatNumber(1234, 'de-DE')).toContain('1.234');
    });
  });

  describe('truncate', () => {
    it('truncates long strings', () => {
      expect(truncate('Hello World', 5)).toBe('Hello...');
    });

    it('does not truncate short strings', () => {
      expect(truncate('Hi', 5)).toBe('Hi');
    });
  });

  describe('slugify', () => {
    it('converts string to slug', () => {
      expect(slugify('Hello World')).toBe('hello-world');
    });

    it('removes special characters', () => {
      expect(slugify('Hello @#$ World')).toBe('hello-world');
    });

    it('handles multiple spaces', () => {
      expect(slugify('Hello    World')).toBe('hello-world');
    });
  });
});
