import { type ClassValue, clsx } from 'clsx';
import { twMerge } from 'tailwind-merge';

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function formatCurrency(amount: number, currency: string = 'USD'): string {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: currency,
  }).format(amount);
}

export function formatNumber(num: number): string {
  return new Intl.NumberFormat('en-US').format(num);
}

export function formatDate(date: string | Date): string {
  const dateObj = typeof date === 'string' ? new Date(date) : date;
  return dateObj.toLocaleDateString('en-US', {
    month: 'short',
    day: 'numeric',
    year: 'numeric',
  });
}

export function getCountryFlag(countryCode: string): string {
  const flags: Record<string, string> = {
    NG: '🇳🇬', KE: '🇰🇪', ZA: '🇿🇦', GH: '🇬🇭', EG: '🇪🇬',
    GB: '🇬🇧', IE: '🇮🇪', DE: '🇩🇪', FR: '🇫🇷', NL: '🇳🇱',
    US: '🇺🇸', CA: '🇨🇦', BR: '🇧🇷', MX: '🇲🇽',
    JP: '🇯🇵', SG: '🇸🇬', IN: '🇮🇳', MY: '🇲🇾', TH: '🇹🇭',
  };
  return flags[countryCode] || '🌍';
}

export function truncate(text: string, maxLength: number): string {
  if (text.length <= maxLength) return text;
  return text.slice(0, maxLength) + '...';
}
