// Utility Functions for Country Sales Dashboard

import { format, formatDistanceToNow, parseISO } from 'date-fns';
import { CURRENCY, DATE_FORMATS } from '../constants';

// ============================================================
// Currency Utilities
// ============================================================

export function formatCurrency(
  amount: number,
  currencyCode: string = CURRENCY.DEFAULT,
  options: { showSymbol?: boolean; decimals?: number } = {}
): string {
  const { showSymbol = true, decimals = 0 } = options;
  const symbol = CURRENCY.SYMBOLS[currencyCode as keyof typeof CURRENCY.SYMBOLS] || currencyCode;

  if (showSymbol) {
    return `${symbol}${amount.toLocaleString('en-US', { minimumFractionDigits: decimals, maximumFractionDigits: decimals })}`;
  }
  return amount.toLocaleString('en-US', { minimumFractionDigits: decimals, maximumFractionDigits: decimals });
}

export function formatCompactCurrency(amount: number, currencyCode: string = CURRENCY.DEFAULT): string {
  const symbol = CURRENCY.SYMBOLS[currencyCode as keyof typeof CURRENCY.SYMBOLS] || currencyCode;

  if (amount >= 1000000) {
    return `${symbol}${(amount / 1000000).toFixed(1)}M`;
  }
  if (amount >= 1000) {
    return `${symbol}${(amount / 1000).toFixed(1)}K`;
  }
  return `${symbol}${amount}`;
}

// ============================================================
// Number Utilities
// ============================================================

export function formatNumber(value: number, decimals: number = 0): string {
  return value.toLocaleString('en-US', { minimumFractionDigits: decimals, maximumFractionDigits: decimals });
}

export function formatPercentage(value: number, decimals: number = 1): string {
  return `${value.toFixed(decimals)}%`;
}

export function formatCompactNumber(value: number): string {
  if (value >= 1000000) {
    return `${(value / 1000000).toFixed(1)}M`;
  }
  if (value >= 1000) {
    return `${(value / 1000).toFixed(1)}K`;
  }
  return value.toString();
}

export function calculatePercentage(value: number, total: number): number {
  if (total === 0) return 0;
  return (value / total) * 100;
}

export function calculateGrowth(current: number, previous: number): number {
  if (previous === 0) return current > 0 ? 100 : 0;
  return ((current - previous) / previous) * 100;
}

// ============================================================
// Date Utilities
// ============================================================

export function formatDate(date: Date | string, formatStr: string = DATE_FORMATS.DISPLAY): string {
  const dateObj = typeof date === 'string' ? parseISO(date) : date;
  return format(dateObj, formatStr);
}

export function formatRelativeTime(date: Date | string): string {
  const dateObj = typeof date === 'string' ? parseISO(date) : date;
  return formatDistanceToNow(dateObj, { addSuffix: true });
}

export function isDateInRange(date: Date | string, start: Date | string, end: Date | string): boolean {
  const dateObj = typeof date === 'string' ? parseISO(date) : date;
  const startObj = typeof start === 'string' ? parseISO(start) : start;
  const endObj = typeof end === 'string' ? parseISO(end) : end;
  return dateObj >= startObj && dateObj <= endObj;
}

export function getPeriodStart(type: 'daily' | 'weekly' | 'monthly' | 'quarterly' | 'yearly'): Date {
  const now = new Date();
  switch (type) {
    case 'daily':
      return new Date(now.setHours(0, 0, 0, 0));
    case 'weekly':
      const dayOfWeek = now.getDay();
      const weekStart = new Date(now);
      weekStart.setDate(now.getDate() - dayOfWeek);
      weekStart.setHours(0, 0, 0, 0);
      return weekStart;
    case 'monthly':
      return new Date(now.getFullYear(), now.getMonth(), 1);
    case 'quarterly':
      const quarter = Math.floor(now.getMonth() / 3);
      return new Date(now.getFullYear(), quarter * 3, 1);
    case 'yearly':
      return new Date(now.getFullYear(), 0, 1);
  }
}

export function getPeriodEnd(type: 'daily' | 'weekly' | 'monthly' | 'quarterly' | 'yearly'): Date {
  const now = new Date();
  switch (type) {
    case 'daily':
      return new Date(now.setHours(23, 59, 59, 999));
    case 'weekly':
      const dayOfWeek = now.getDay();
      const weekEnd = new Date(now);
      weekEnd.setDate(now.getDate() + (6 - dayOfWeek));
      weekEnd.setHours(23, 59, 59, 999);
      return weekEnd;
    case 'monthly':
      return new Date(now.getFullYear(), now.getMonth() + 1, 0, 23, 59, 59, 999);
    case 'quarterly':
      const quarter = Math.floor(now.getMonth() / 3);
      return new Date(now.getFullYear(), quarter * 3 + 3, 0, 23, 59, 59, 999);
    case 'yearly':
      return new Date(now.getFullYear(), 11, 31, 23, 59, 59, 999);
  }
}

// ============================================================
// String Utilities
// ============================================================

export function truncate(str: string, maxLength: number): string {
  if (str.length <= maxLength) return str;
  return `${str.substring(0, maxLength - 3)}...`;
}

export function capitalize(str: string): string {
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
}

export function titleCase(str: string): string {
  return str
    .split(' ')
    .map(word => capitalize(word))
    .join(' ');
}

export function getInitials(firstName: string, lastName: string): string {
  return `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase();
}

export function generateColorFromString(str: string): string {
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash);
  }
  const hue = Math.abs(hash % 360);
  return `hsl(${hue}, 70%, 50%)`;
}

// ============================================================
// Validation Utilities
// ============================================================

export function isValidEmail(email: string): boolean {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
}

export function isValidPhone(phone: string): boolean {
  const phoneRegex = /^\+?[\d\s\-()]+$/;
  return phoneRegex.test(phone) && phone.replace(/\D/g, '').length >= 10;
}

export function isValidUrl(url: string): boolean {
  try {
    new URL(url);
    return true;
  } catch {
    return false;
  }
}

// ============================================================
// Array Utilities
// ============================================================

export function sortBy<T>(
  array: T[],
  key: keyof T,
  order: 'asc' | 'desc' = 'asc'
): T[] {
  return [...array].sort((a, b) => {
    const aValue = a[key];
    const bValue = b[key];

    if (aValue < bValue) return order === 'asc' ? -1 : 1;
    if (aValue > bValue) return order === 'asc' ? 1 : -1;
    return 0;
  });
}

export function groupBy<T>(
  array: T[],
  key: keyof T
): Record<string, T[]> {
  return array.reduce((groups, item) => {
    const groupKey = String(item[key]);
    if (!groups[groupKey]) {
      groups[groupKey] = [];
    }
    groups[groupKey].push(item);
    return groups;
  }, {} as Record<string, T[]>);
}

export function uniqueBy<T>(array: T[], key: keyof T): T[] {
  const seen = new Set();
  return array.filter(item => {
    const keyValue = String(item[key]);
    if (seen.has(keyValue)) return false;
    seen.add(keyValue);
    return true;
  });
}

export function chunk<T>(array: T[], size: number): T[][] {
  const chunks: T[][] = [];
  for (let i = 0; i < array.length; i += size) {
    chunks.push(array.slice(i, i + size));
  }
  return chunks;
}

// ============================================================
// Object Utilities
// ============================================================

export function omit<T extends Record<string, any>, K extends keyof T>(
  obj: T,
  keys: K[]
): Omit<T, K> {
  const result = { ...obj };
  keys.forEach(key => delete result[key]);
  return result;
}

export function pick<T extends Record<string, any>, K extends keyof T>(
  obj: T,
  keys: K[]
): Pick<T, K> {
  return keys.reduce((result, key) => {
    if (key in obj) {
      result[key] = obj[key];
    }
    return result;
  }, {} as Pick<T, K>);
}

export function deepClone<T>(obj: T): T {
  return JSON.parse(JSON.stringify(obj));
}

// ============================================================
// Storage Utilities
// ============================================================

export const storage = {
  get<T>(key: string): T | null {
    try {
      const item = localStorage.getItem(key);
      return item ? JSON.parse(item) : null;
    } catch {
      return null;
    }
  },

  set<T>(key: string, value: T): void {
    try {
      localStorage.setItem(key, JSON.stringify(value));
    } catch (error) {
      console.error('Error saving to localStorage:', error);
    }
  },

  remove(key: string): void {
    try {
      localStorage.removeItem(key);
    } catch (error) {
      console.error('Error removing from localStorage:', error);
    }
  },

  clear(): void {
    try {
      localStorage.clear();
    } catch (error) {
      console.error('Error clearing localStorage:', error);
    }
  },
};

// ============================================================
// Debounce and Throttle
// ============================================================

export function debounce<T extends (...args: any[]) => any>(
  func: T,
  wait: number
): (...args: Parameters<T>) => void {
  let timeout: NodeJS.Timeout | null = null;

  return (...args: Parameters<T>) => {
    if (timeout) clearTimeout(timeout);
    timeout = setTimeout(() => func(...args), wait);
  };
}

export function throttle<T extends (...args: any[]) => any>(
  func: T,
  limit: number
): (...args: Parameters<T>) => void {
  let inThrottle: boolean = false;

  return (...args: Parameters<T>) => {
    if (!inThrottle) {
      func(...args);
      inThrottle = true;
      setTimeout(() => (inThrottle = false), limit);
    }
  };
}

// ============================================================
// Download Utilities
// ============================================================

export function downloadAsJson(data: any, filename: string): void {
  const blob = new Blob([JSON.stringify(data, null, 2)], { type: 'application/json' });
  const url = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  URL.revokeObjectURL(url);
}

export function downloadAsCsv(data: any[], filename: string): void {
  if (data.length === 0) return;

  const headers = Object.keys(data[0]);
  const csvContent = [
    headers.join(','),
    ...data.map(row => headers.map(header => {
      const value = row[header];
      const stringValue = typeof value === 'string' ? value : JSON.stringify(value);
      return `"${stringValue.replace(/"/g, '""')}"`;
    }).join(',')),
  ].join('\n');

  const blob = new Blob([csvContent], { type: 'text/csv' });
  const url = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  URL.revokeObjectURL(url);
}

// ============================================================
// Status Color Utilities
// ============================================================

export function getStatusColor(status: string): string {
  const statusColors: Record<string, string> = {
    // Deal stages
    PROSPECTING: '#9CA3AF',
    QUALIFICATION: '#3B82F6',
    PROPOSAL: '#8B5CF6',
    NEGOTIATION: '#F59E0B',
    CLOSING: '#10B981',
    WON: '#059669',
    LOST: '#EF4444',

    // Lead status
    NEW: '#3B82F6',
    CONTACTED: '#8B5CF6',
    QUALIFIED: '#10B981',
    CONVERTED: '#059669',
    UNQUALIFIED: '#F59E0B',
    LOST: '#EF4444',

    // Customer status
    active: '#10B981',
    at_risk: '#F59E0B',
    churned: '#EF4444',
    prospect: '#3B82F6',

    // Partner status
    PENDING: '#F59E0B',
    ACTIVE: '#10B981',
    SUSPENDED: '#EF4444',
    TERMINATED: '#6B7280',

    // Team status
    active: '#10B981',
    inactive: '#6B7280',
    pending: '#F59E0B',
  };

  return statusColors[status] || '#6B7280';
}

export function getTrendIcon(trend: 'up' | 'down' | 'neutral'): string {
  const icons = {
    up: '\u2197',
    down: '\u2198',
    neutral: '\u2192',
  };
  return icons[trend];
}

export function getTrendColor(trend: 'up' | 'down' | 'neutral'): string {
  const colors = {
    up: '#10B981',
    down: '#EF4444',
    neutral: '#6B7280',
  };
  return colors[trend];
}
