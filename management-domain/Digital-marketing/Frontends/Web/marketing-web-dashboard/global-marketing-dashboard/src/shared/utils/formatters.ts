// Utility functions for formatting data

export const formatCurrency = (
  amount: number,
  currency: string = 'USD',
  locale: string = 'en-US'
): string => {
  try {
    return new Intl.NumberFormat(locale, {
      style: 'currency',
      currency,
      minimumFractionDigits: 0,
      maximumFractionDigits: 0,
    }).format(amount);
  } catch {
    return `${currency} ${amount.toLocaleString()}`;
  }
};

export const formatNumber = (
  value: number,
  locale: string = 'en-US'
): string => {
  return new Intl.NumberFormat(locale).format(value);
};

export const formatPercentage = (
  value: number,
  decimals: number = 1
): string => {
  return `${value.toFixed(decimals)}%`;
};

export const formatCompactNumber = (value: number): string => {
  if (value >= 1000000000) {
    return `${(value / 1000000000).toFixed(1)}B`;
  }
  if (value >= 1000000) {
    return `${(value / 1000000).toFixed(1)}M`;
  }
  if (value >= 1000) {
    return `${(value / 1000).toFixed(1)}K`;
  }
  return value.toString();
};

export const formatDate = (
  date: Date,
  locale: string = 'en-US',
  options?: Intl.DateTimeFormatOptions
): string => {
  return new Intl.DateTimeFormat(locale, {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    ...options,
  }).format(date);
};

export const formatDateTime = (
  date: Date,
  locale: string = 'en-US'
): string => {
  return new Intl.DateTimeFormat(locale, {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  }).format(date);
};

export const formatRelativeTime = (date: Date): string => {
  const now = new Date();
  const diffMs = now.getTime() - date.getTime();
  const diffMins = Math.floor(diffMs / 60000);
  const diffHours = Math.floor(diffMs / 3600000);
  const diffDays = Math.floor(diffMs / 86400000);

  if (diffMins < 60) {
    return `${diffMins}m ago`;
  }
  if (diffHours < 24) {
    return `${diffHours}h ago`;
  }
  if (diffDays < 7) {
    return `${diffDays}d ago`;
  }
  return formatDate(date);
};

export const formatDuration = (seconds: number): string => {
  const hours = Math.floor(seconds / 3600);
  const minutes = Math.floor((seconds % 3600) / 60);
  const secs = seconds % 60;

  if (hours > 0) {
    return `${hours}h ${minutes}m`;
  }
  if (minutes > 0) {
    return `${minutes}m ${secs}s`;
  }
  return `${secs}s`;
};

export const formatMetric = (value: number, metric: string): string => {
  switch (metric) {
    case 'currency':
    case 'revenue':
    case 'cost':
    case 'budget':
      return formatCompactNumber(value);
    case 'percentage':
    case 'ctr':
    case 'roas':
      return formatPercentage(value, 1);
    case 'rate':
      return formatPercentage(value, 2);
    default:
      return formatCompactNumber(value);
  }
};

export const calculateTrend = (current: number, previous: number): number => {
  if (previous === 0) return current > 0 ? 100 : 0;
  return ((current - previous) / previous) * 100;
};

export const getTrendDirection = (trend: number): 'up' | 'down' | 'stable' => {
  if (Math.abs(trend) < 0.1) return 'stable';
  return trend > 0 ? 'up' : 'down';
};

export const getTrendIcon = (trend: number): string => {
  if (Math.abs(trend) < 0.1) return '−';
  return trend > 0 ? '↑' : '↓';
};

export const getTrendColor = (trend: number, inverse: boolean = false): string => {
  if (Math.abs(trend) < 0.1) return '#9ca3af';
  if (inverse) {
    return trend > 0 ? '#ef4444' : '#10b981';
  }
  return trend > 0 ? '#10b981' : '#ef4444';
};

export const truncateText = (text: string, maxLength: number): string => {
  if (text.length <= maxLength) return text;
  return text.slice(0, maxLength - 3) + '...';
};

export const capitalizeFirst = (text: string): string => {
  return text.charAt(0).toUpperCase() + text.slice(1);
};

export const kebabToTitle = (text: string): string => {
  return text
    .split('-')
    .map(word => capitalizeFirst(word))
    .join(' ');
};

export const snakeToTitle = (text: string): string => {
  return text
    .split('_')
    .map(word => capitalizeFirst(word))
    .join(' ');
};

export const generateColorScale = (
  baseColor: string,
  steps: number
): string[] => {
  const colors: string[] = [];
  for (let i = 0; i < steps; i++) {
    const opacity = 1 - i * (0.8 / steps);
    colors.push(`${baseColor}${Math.round(opacity * 255).toString(16).padStart(2, '0')}`);
  }
  return colors;
};

export const hexToRgb = (hex: string): { r: number; g: number; b: number } => {
  const result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
  return result
    ? {
        r: parseInt(result[1], 16),
        g: parseInt(result[2], 16),
        b: parseInt(result[3], 16),
      }
    : { r: 0, g: 0, b: 0 };
};

export const getContrastColor = (hexColor: string): string => {
  const rgb = hexToRgb(hexColor);
  const luminance = (0.299 * rgb.r + 0.587 * rgb.g + 0.114 * rgb.b) / 255;
  return luminance > 0.5 ? '#000000' : '#ffffff';
};

export const formatFileSize = (bytes: number): string => {
  if (bytes === 0) return '0 Bytes';
  const k = 1024;
  const sizes = ['Bytes', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return Math.round((bytes / Math.pow(k, i)) * 100) / 100 + ' ' + sizes[i];
};

export const getInitials = (name: string): string => {
  return name
    .split(' ')
    .map(part => part.charAt(0).toUpperCase())
    .slice(0, 2)
    .join('');
};

export const generateId = (): string => {
  return `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`;
};

export const debounce = <T extends (...args: any[]) => any>(
  func: T,
  wait: number
): ((...args: Parameters<T>) => void) => {
  let timeout: NodeJS.Timeout | null = null;
  return (...args: Parameters<T>) => {
    if (timeout) clearTimeout(timeout);
    timeout = setTimeout(() => func(...args), wait);
  };
};

export const throttle = <T extends (...args: any[]) => any>(
  func: T,
  limit: number
): ((...args: Parameters<T>) => void) => {
  let inThrottle: boolean;
  return (...args: Parameters<T>) => {
    if (!inThrottle) {
      func(...args);
      inThrottle = true;
      setTimeout(() => (inThrottle = false), limit);
    }
  };
};

export const cloneDeep = <T>(obj: T): T => {
  return JSON.parse(JSON.stringify(obj));
};

export const groupBy = <T>(
  array: T[],
  key: keyof T
): Record<string, T[]> => {
  return array.reduce((result, item) => {
    const groupKey = String(item[key]);
    if (!result[groupKey]) {
      result[groupKey] = [];
    }
    result[groupKey].push(item);
    return result;
  }, {} as Record<string, T[]>);
};

export const sortBy = <T>(
  array: T[],
  key: keyof T,
  order: 'asc' | 'desc' = 'asc'
): T[] => {
  return [...array].sort((a, b) => {
    const aVal = a[key];
    const bVal = b[key];
    if (aVal < bVal) return order === 'asc' ? -1 : 1;
    if (aVal > bVal) return order === 'asc' ? 1 : -1;
    return 0;
  });
};

export const uniqBy = <T>(array: T[], key: keyof T): T[] => {
  const seen = new Set();
  return array.filter(item => {
    const keyValue = item[key];
    if (seen.has(keyValue)) {
      return false;
    }
    seen.add(keyValue);
    return true;
  });
};

export const chunk = <T>(array: T[], size: number): T[][] => {
  const chunks: T[][] = [];
  for (let i = 0; i < array.length; i += size) {
    chunks.push(array.slice(i, i + size));
  }
  return chunks;
};

export const range = (start: number, end: number): number[] => {
  return Array.from({ length: end - start }, (_, i) => start + i);
};

export const sum = (array: number[]): number => {
  return array.reduce((acc, val) => acc + val, 0);
};

export const average = (array: number[]): number => {
  return array.length > 0 ? sum(array) / array.length : 0;
};

export const max = (array: number[]): number => {
  return Math.max(...array);
};

export const min = (array: number[]): number => {
  return Math.min(...array);
};
