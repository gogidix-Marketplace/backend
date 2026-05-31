import { type ClassValue, clsx } from 'clsx';
import { twMerge } from 'tailwind-merge';

/**
 * Utility function to merge Tailwind CSS classes
 */
export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

/**
 * Format a number as currency
 */
export function formatCurrency(amount: number, currency: string = 'USD'): string {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency: currency,
  }).format(amount);
}

/**
 * Format a number with commas
 */
export function formatNumber(num: number): string {
  return new Intl.NumberFormat('en-US').format(num);
}

/**
 * Format a date
 */
export function formatDate(date: string | Date, format: 'short' | 'long' | 'full' = 'short'): string {
  const dateObj = typeof date === 'string' ? new Date(date) : date;

  switch (format) {
    case 'short':
      return dateObj.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
    case 'long':
      return dateObj.toLocaleDateString('en-US', { month: 'long', day: 'numeric', year: 'numeric' });
    case 'full':
      return dateObj.toLocaleDateString('en-US', { weekday: 'long', month: 'long', day: 'numeric', year: 'numeric' });
    default:
      return dateObj.toLocaleDateString();
  }
}

/**
 * Get relative time string (e.g., "2 hours ago")
 */
export function getRelativeTime(date: string | Date): string {
  const dateObj = typeof date === 'string' ? new Date(date) : date;
  const now = new Date();
  const diffMs = now.getTime() - dateObj.getTime();
  const diffMins = Math.floor(diffMs / 60000);
  const diffHours = Math.floor(diffMs / 3600000);
  const diffDays = Math.floor(diffMs / 86400000);

  if (diffMins < 60) {
    return `${diffMins} minute${diffMins !== 1 ? 's' : ''} ago`;
  } else if (diffHours < 24) {
    return `${diffHours} hour${diffHours !== 1 ? 's' : ''} ago`;
  } else if (diffDays < 7) {
    return `${diffDays} day${diffDays !== 1 ? 's' : ''} ago`;
  } else {
    return formatDate(dateObj, 'short');
  }
}

/**
 * Get country flag emoji
 */
export function getCountryFlag(countryCode: string): string {
  const flags: Record<string, string> = {
    US: '🇺🇸', GB: '🇬🇧', DE: '🇩🇪', FR: '🇫🇷', ES: '🇪🇸', IT: '🇮🇹',
    NL: '🇳🇱', PL: '🇵🇱', SE: '🇸🇪', NO: '🇳🇴', DK: '🇩🇰', FI: '🇫🇮',
    CH: '🇨🇭', AT: '🇦🇹', BE: '🇧🇪', IE: '🇮🇪', PT: '🇵🇹', GR: '🇬🇷',
    CZ: '🇨🇿', HU: '🇭🇺', RO: '🇷🇴', BG: '🇧🇬', HR: '🇭🇷', SI: '🇸🇮',
    SK: '🇸🇰', LT: '🇱🇹', LV: '🇱🇻', EE: '🇪🇪', IS: '🇮🇸', LU: '🇱🇺',
    MT: '🇲🇹', CY: '🇨🇾', CA: '🇨🇦', AU: '🇦🇺', NZ: '🇳🇿', JP: '🇯🇵',
    SG: '🇸🇬', IN: '🇮🇳', BR: '🇧🇷', MX: '🇲🇽', AR: '🇦🇷', CL: '🇨🇱',
    CO: '🇨🇴', PE: '🇵🇪', ZA: '🇿🇦', AE: '🇦🇪', SA: '🇸🇦', IL: '🇮🇱',
    TR: '🇹🇷', RU: '🇷🇺', CN: '🇨🇳', KR: '🇰🇷', TH: '🇹🇭', MY: '🇲🇾',
    ID: '🇮🇩', PH: '🇵🇭', VN: '🇻🇳', HK: '🇭🇰', TW: '🇹🇼',
  };
  return flags[countryCode] || '🌍';
}

/**
 * Get status color class
 */
export function getStatusColor(status: string): string {
  switch (status.toLowerCase()) {
    case 'active':
    case 'compliant':
    case 'processed':
      return 'bg-green-100 text-green-800';
    case 'pending':
    case 'warning':
    case 'on-hold':
      return 'bg-yellow-100 text-yellow-800';
    case 'inactive':
    case 'critical':
    case 'failed':
    case 'closed':
      return 'bg-red-100 text-red-800';
    default:
      return 'bg-gray-100 text-gray-800';
  }
}

/**
 * Truncate text
 */
export function truncate(text: string, maxLength: number): string {
  if (text.length <= maxLength) return text;
  return text.slice(0, maxLength) + '...';
}

/**
 * Generate initials from name
 */
export function getInitials(firstName: string, lastName: string): string {
  return `${firstName?.[0] || ''}${lastName?.[0] || ''}`.toUpperCase();
}

/**
 * Validate email
 */
export function isValidEmail(email: string): boolean {
  const emailRegex = /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i;
  return emailRegex.test(email);
}

/**
 * Download data as file
 */
export function downloadAsFile(data: any, filename: string, type: string = 'application/json') {
  const blob = new Blob([JSON.stringify(data, null, 2)], { type });
  const url = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  URL.revokeObjectURL(url);
}
