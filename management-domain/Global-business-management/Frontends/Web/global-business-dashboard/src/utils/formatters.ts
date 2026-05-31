import { format, formatDistanceToNow, parseISO, differenceInDays, differenceInHours, differenceInMinutes } from 'date-fns';
import { enUS, es, fr } from 'date-fns/locale';

const dateLocales = {
  en: enUS,
  es,
  fr,
};

export type Locale = keyof typeof dateLocales;

export function formatDate(
  date: string | Date,
  formatStr: string = 'PPP',
  locale: Locale = 'en'
): string {
  const dateObj = typeof date === 'string' ? parseISO(date) : date;
  return format(dateObj, formatStr, { locale: dateLocales[locale] || enUS });
}

export function formatDateTime(
  date: string | Date,
  locale: Locale = 'en'
): string {
  return formatDate(date, 'PPp', locale);
}

export function formatShortDate(
  date: string | Date,
  locale: Locale = 'en'
): string {
  return formatDate(date, 'P', locale);
}

export function formatRelativeTime(
  date: string | Date,
  locale: Locale = 'en',
  addSuffix: boolean = true
): string {
  const dateObj = typeof date === 'string' ? parseISO(date) : date;
  return formatDistanceToNow(dateObj, {
    addSuffix,
    locale: dateLocales[locale] || enUS,
  });
}

export function formatTimeAgo(
  date: string | Date,
  locale: Locale = 'en'
): string {
  return formatRelativeTime(date, locale, true);
}

export function getRelativeTimeLabel(date: string | Date, locale: Locale = 'en'): string {
  const dateObj = typeof date === 'string' ? parseISO(date) : date;
  const now = new Date();

  const minutes = differenceInMinutes(now, dateObj);
  const hours = differenceInHours(now, dateObj);
  const days = differenceInDays(now, dateObj);

  if (minutes < 1) return locale === 'es' ? 'justo ahora' : 'just now';
  if (minutes < 60) return `${minutes} ${locale === 'es' ? 'min' : 'min'} ${locale === 'es' ? 'atrás' : 'ago'}`;
  if (hours < 24) return `${hours} ${locale === 'es' ? 'h' : 'hr'} ${locale === 'es' ? 'atrás' : 'ago'}`;
  if (days < 7) return `${days} ${locale === 'es' ? 'días' : 'day'}${days > 1 ? '' : ''} ${locale === 'es' ? 'atrás' : 'ago'}`;

  return formatRelativeTime(date, locale, true);
}

export function formatNumber(
  value: number,
  locale: string = 'en-US',
  options?: Intl.NumberFormatOptions
): string {
  return new Intl.NumberFormat(locale, options).format(value);
}

export function formatPercent(value: number, decimals: number = 1, locale: string = 'en-US'): string {
  const formatted = new Intl.NumberFormat(locale, {
    style: 'percent',
    minimumFractionDigits: decimals,
    maximumFractionDigits: decimals,
  }).format(value / 100);

  return formatted;
}

export function formatDecimal(value: number, decimals: number = 2, locale: string = 'en-US'): string {
  return new Intl.NumberFormat(locale, {
    minimumFractionDigits: decimals,
    maximumFractionDigits: decimals,
  }).format(value);
}

export function formatCompactNumber(value: number, locale: string = 'en-US'): string {
  return new Intl.NumberFormat(locale, {
    notation: 'compact',
    compactDisplay: 'short',
    maximumFractionDigits: 1,
  }).format(value);
}

export function formatLargeNumber(value: number): string {
  const absValue = Math.abs(value);
  const sign = value < 0 ? '-' : '';

  if (absValue >= 1e12) {
    return `${sign}${(absValue / 1e12).toFixed(1)}T`;
  }
  if (absValue >= 1e9) {
    return `${sign}${(absValue / 1e9).toFixed(1)}B`;
  }
  if (absValue >= 1e6) {
    return `${sign}${(absValue / 1e6).toFixed(1)}M`;
  }
  if (absValue >= 1e3) {
    return `${sign}${(absValue / 1e3).toFixed(1)}K`;
  }

  return `${sign}${absValue.toFixed(0)}`;
}

export function formatPhoneNumber(phone: string): string {
  const cleaned = phone.replace(/\D/g, '');
  if (cleaned.length === 10) {
    return `(${cleaned.slice(0, 3)}) ${cleaned.slice(3, 6)}-${cleaned.slice(6)}`;
  }
  if (cleaned.length === 11 && cleaned[0] === '1') {
    return `+1 (${cleaned.slice(1, 4)}) ${cleaned.slice(4, 7)}-${cleaned.slice(7)}`;
  }
  return phone;
}

export function formatEmailAddress(email: string, maxLength: number = 30): string {
  if (email.length <= maxLength) return email;
  const [username, domain] = email.split('@');
  const truncatedUsername = username.slice(0, maxLength - domain.length - 4);
  return `${truncatedUsername}...@${domain}`;
}

export function truncateText(text: string, maxLength: number = 50, suffix: string = '...'): string {
  if (text.length <= maxLength) return text;
  return text.slice(0, maxLength - suffix.length) + suffix;
}

export function capitalize(text: string): string {
  return text.charAt(0).toUpperCase() + text.slice(1).toLowerCase();
}

export function capitalizeWords(text: string): string {
  return text.replace(/\b\w/g, (char) => char.toUpperCase());
}

export function camelCaseToWords(text: string): string {
  return text
    .replace(/([A-Z])/g, ' $1')
    .replace(/^./, (str) => str.toUpperCase())
    .trim();
}

export function snakeCaseToWords(text: string): string {
  return text
    .split('_')
    .map((word) => capitalize(word))
    .join(' ');
}

export function kebabCaseToWords(text: string): string {
  return text
    .split('-')
    .map((word) => capitalize(word))
    .join(' ');
}

export function toCamelCase(text: string): string {
  return text
    .replace(/[-_\s]+(.)?/g, (_, char) => (char ? char.toUpperCase() : ''))
    .replace(/^(.)/, (char) => char.toLowerCase());
}

export function toKebabCase(text: string): string {
  return text
    .replace(/([a-z])([A-Z])/g, '$1-$2')
    .replace(/[\s_]+/g, '-')
    .toLowerCase();
}

export function toSnakeCase(text: string): string {
  return text
    .replace(/([a-z])([A-Z])/g, '$1_$2')
    .replace(/[\s-]+/g, '_')
    .toLowerCase();
}

export function getInitials(name: string, maxLength: number = 2): string {
  const parts = name.trim().split(/\s+/);
  if (parts.length === 1) {
    return parts[0].slice(0, maxLength).toUpperCase();
  }
  return parts
    .slice(0, maxLength)
    .map((part) => part[0].toUpperCase())
    .join('');
}

export function formatFileSize(bytes: number, locale: string = 'en-US'): string {
  const units = ['bytes', 'KB', 'MB', 'GB', 'TB'];
  let size = bytes;
  let unitIndex = 0;

  while (size >= 1024 && unitIndex < units.length - 1) {
    size /= 1024;
    unitIndex++;
  }

  return `${formatNumber(size, locale, { maximumFractionDigits: 1 })} ${units[unitIndex]}`;
}

export function formatDuration(seconds: number, locale: Locale = 'en'): string {
  const hours = Math.floor(seconds / 3600);
  const minutes = Math.floor((seconds % 3600) / 60);
  const secs = Math.floor(seconds % 60);

  const parts: string[] = [];
  if (hours > 0) {
    parts.push(`${hours}${locale === 'es' ? 'h' : 'hr'}`);
  }
  if (minutes > 0) {
    parts.push(`${minutes}${locale === 'es' ? 'm' : 'min'}`);
  }
  if (secs > 0 || parts.length === 0) {
    parts.push(`${secs}${locale === 'es' ? 's' : 'sec'}`);
  }

  return parts.join(' ');
}

export function formatAddress(
  street: string,
  city: string,
  state?: string,
  postalCode?: string,
  country?: string
): string {
  const parts = [street, city].filter(Boolean);
  if (state) parts.push(state);
  if (postalCode) parts.push(postalCode);
  if (country) parts.push(country);
  return parts.join(', ');
}

export function sanitizeHTML(html: string): string {
  const temp = document.createElement('div');
  temp.textContent = html;
  return temp.innerHTML;
}

export function highlightMatch(text: string, query: string): string {
  if (!query) return text;
  const regex = new RegExp(`(${query.replace(/[.*+?^${}()|[\]\\]/g, '\\$&')})`, 'gi');
  return text.replace(regex, '<mark>$1</mark>');
}

export function formatMasked(value: string, visibleChars: number = 4): string {
  if (value.length <= visibleChars) return value;
  return '*'.repeat(value.length - visibleChars) + value.slice(-visibleChars);
}

export function formatList(items: string[], locale: string = 'en'): string {
  if (items.length === 0) return '';
  if (items.length === 1) return items[0];
  if (items.length === 2) {
    return locale === 'es' ? `${items[0]} y ${items[1]}` : `${items[0]} and ${items[1]}`;
  }

  const lastItem = items[items.length - 1];
  const otherItems = items.slice(0, -1);
  const separator = locale === 'es' ? ', y ' : ', and ';

  return `${otherItems.join(', ')}${separator}${lastItem}`;
}

export function getOrdinal(num: number, locale: string = 'en'): string {
  if (locale === 'es') {
    return `${num}º`;
  }

  const suffixes = ['th', 'st', 'nd', 'rd'];
  const v = num % 100;
  return num + (suffixes[(v - 20) % 10] || suffixes[v] || suffixes[0]);
}
