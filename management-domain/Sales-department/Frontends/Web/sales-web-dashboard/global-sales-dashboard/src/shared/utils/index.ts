// Utility Functions

// Formatting utilities
export const formatCurrency = (amount: number, currency = 'USD'): string => {
  const symbols: Record<string, string> = {
    USD: '$',
    NGN: '\u20A6',
    KES: 'KSh',
    GHS: 'GH\u20B5',
    ZAR: 'R',
    ETB: 'Br',
    UGX: 'UGX',
  };

  const symbol = symbols[currency] || '$';

  if (amount >= 1000000) {
    return `${symbol}${(amount / 1000000).toFixed(1)}M`;
  } else if (amount >= 1000) {
    return `${symbol}${(amount / 1000).toFixed(1)}K`;
  }

  return `${symbol}${amount.toLocaleString()}`;
};

export const formatNumber = (num: number, decimals = 0): string => {
  return num.toLocaleString('en-US', {
    minimumFractionDigits: decimals,
    maximumFractionDigits: decimals,
  });
};

export const formatPercentage = (value: number, decimals = 1): string => {
  return `${value.toFixed(decimals)}%`;
};

export const formatChange = (value: number, decimals = 0): string => {
  const sign = value >= 0 ? '+' : '';
  return `${sign}${value.toFixed(deccrements)}%`;
};

// Date utilities
export const formatDate = (date: Date | string, format = 'DISPLAY'): string => {
  const d = typeof date === 'string' ? new Date(date) : date;

  if (isNaN(d.getTime())) return '';

  const formats: Record<string, string> = {
    DISPLAY: d.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' }),
    DISPLAY_WITH_TIME: d.toLocaleString('en-US', { month: 'short', day: 'numeric', year: 'numeric', hour: 'numeric', minute: '2-digit' }),
    SHORT: d.toLocaleDateString('en-US'),
    MONTH_YEAR: d.toLocaleDateString('en-US', { month: 'long', year: 'numeric' }),
    ISO: d.toISOString(),
  };

  return formats[format] || formats.DISPLAY;
};

export const getPeriodRange = (period: string): { start: Date; end: Date } => {
  const now = new Date();
  const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());

  switch (period) {
    case 'today':
      return { start: today, end: now };
    case 'week':
      const weekStart = new Date(today);
      weekStart.setDate(today.getDate() - today.getDay());
      return { start: weekStart, end: now };
    case 'month':
      const monthStart = new Date(now.getFullYear(), now.getMonth(), 1);
      return { start: monthStart, end: now };
    case 'quarter':
      const quarter = Math.floor(now.getMonth() / 3);
      const quarterStart = new Date(now.getFullYear(), quarter * 3, 1);
      return { start: quarterStart, end: now };
    case 'year':
      const yearStart = new Date(now.getFullYear(), 0, 1);
      return { start: yearStart, end: now };
    default:
      return { start: today, end: now };
  }
};

// Validation utilities
export const validateEmail = (email: string): boolean => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return emailRegex.test(email);
};

export const validatePhone = (phone: string): boolean => {
  const phoneRegex = /^\+?[\d\s\-\(\)]+$/;
  return phoneRegex.test(phone) && phone.replace(/\D/g, '').length >= 10;
};

export const validateRequired = (value: any): boolean => {
  if (value === null || value === undefined) return false;
  if (typeof value === 'string') return value.trim().length > 0;
  if (Array.isArray(value)) return value.length > 0;
  return true;
};

// String utilities
export const truncate = (str: string, maxLength: number): string => {
  if (str.length <= maxLength) return str;
  return `${str.substring(0, maxLength)}...`;
};

export const capitalize = (str: string): string => {
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
};

export const titleCase = (str: string): string => {
  return str
    .toLowerCase()
    .split(' ')
    .map(word => word.charAt(0).toUpperCase() + word.slice(1))
    .join(' ');
};

export const generateId = (prefix = 'id'): string => {
  return `${prefix}-${Date.now()}-${Math.random().toString(36).substring(2, 9)}`;
};

export const getInitials = (firstName: string, lastName: string): string => {
  return `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase();
};

// Array utilities
export const sortBy = <T>(array: T[], key: keyof T, order: 'asc' | 'desc' = 'asc'): T[] => {
  return [...array].sort((a, b) => {
    const aVal = a[key];
    const bVal = b[key];

    if (aVal < bVal) return order === 'asc' ? -1 : 1;
    if (aVal > bVal) return order === 'asc' ? 1 : -1;
    return 0;
  });
};

export const groupBy = <T>(array: T[], key: keyof T): Record<string, T[]> => {
  return array.reduce((result, item) => {
    const groupKey = String(item[key]);
    if (!result[groupKey]) {
      result[groupKey] = [];
    }
    result[groupKey].push(item);
    return result;
  }, {} as Record<string, T[]>);
};

export const chunk = <T>(array: T[], size: number): T[][] => {
  const chunks: T[][] = [];
  for (let i = 0; i < array.length; i += size) {
    chunks.push(array.slice(i, i + size));
  }
  return chunks;
};

export const unique = <T>(array: T[]): T[] => {
  return Array.from(new Set(array));
};

// Calculation utilities
export const calculateGrowth = (current: number, previous: number): number => {
  if (previous === 0) return current > 0 ? 100 : 0;
  return ((current - previous) / previous) * 100;
};

export const calculatePercentage = (value: number, total: number): number => {
  if (total === 0) return 0;
  return (value / total) * 100;
};

export const calculateWeightedValue = (value: number, probability: number): number => {
  return value * (probability / 100);
};

// Color utilities
export const stringToColor = (str: string): string => {
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash);
  }

  const colors = [
    '#2563EB', '#7C3AED', '#10B981', '#F59E0B', '#EF4444',
    '#06B6D4', '#8B5CF6', '#EC4899', '#F97316', '#14B8A6',
  ];

  return colors[Math.abs(hash) % colors.length];
};

export const getStatusColor = (status: string): string => {
  const statusColors: Record<string, string> = {
    on_track: '#10B981',
    at_risk: '#F59E0B',
    off_track: '#EF4444',
    active: '#10B981',
    pending: '#F59E0B',
    inactive: '#6B7280',
    approved: '#10B981',
    rejected: '#EF4444',
    under_review: '#3B82F6',
  };

  return statusColors[status] || '#6B7280';
};

// Download utilities
export const downloadFile = (data: Blob, filename: string): void => {
  const url = window.URL.createObjectURL(data);
  const link = document.createElement('a');
  link.href = url;
  link.download = filename;
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  window.URL.revokeObjectURL(url);
};

export const downloadCSV = (data: any[], filename: string): void => {
  if (data.length === 0) return;

  const headers = Object.keys(data[0]);
  const csvContent = [
    headers.join(','),
    ...data.map(row => headers.map(header => {
      const value = row[header];
      // Escape quotes and wrap in quotes if contains comma
      const stringValue = String(value ?? '');
      if (stringValue.includes(',') || stringValue.includes('"')) {
        return `"${stringValue.replace(/"/g, '""')}"`;
      }
      return stringValue;
    }).join(',')),
  ].join('\n');

  const blob = new Blob([csvContent], { type: 'text/csv' });
  downloadFile(blob, `${filename}.csv`);
};

// Local Storage utilities
export const storage = {
  get: <T>(key: string): T | null => {
    try {
      const item = localStorage.getItem(key);
      return item ? JSON.parse(item) : null;
    } catch {
      return null;
    }
  },

  set: <T>(key: string, value: T): void => {
    try {
      localStorage.setItem(key, JSON.stringify(value));
    } catch (error) {
      console.error('Error saving to localStorage:', error);
    }
  },

  remove: (key: string): void => {
    try {
      localStorage.removeItem(key);
    } catch (error) {
      console.error('Error removing from localStorage:', error);
    }
  },

  clear: (): void => {
    try {
      localStorage.clear();
    } catch (error) {
      console.error('Error clearing localStorage:', error);
    }
  },
};

// Debounce utility
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

// Throttle utility
export const throttle = <T extends (...args: any[]) => any>(
  func: T,
  limit: number
): ((...args: Parameters<T>) => void) => {
  let inThrottle: boolean;

  return (...args: Parameters<T>) => {
    if (!inThrottle) {
      func(...args);
      inThrottle = true;
      setTimeout(() => inThrottle = false, limit);
    }
  };
};

// URL utilities
export const buildQueryString = (params: Record<string, any>): string => {
  const searchParams = new URLSearchParams();
  Object.entries(params).forEach(([key, value]) => {
    if (value !== undefined && value !== null) {
      searchParams.append(key, String(value));
    }
  });
  return searchParams.toString();
};

export const parseQueryString = (search: string): Record<string, string> => {
  const params = new URLSearchParams(search);
  const result: Record<string, string> = {};
  params.forEach((value, key) => {
    result[key] = value;
  });
  return result;
};

// Permission utility
export const hasPermission = (
  userPermissions: string[],
  requiredPermission: string
): boolean => {
  return userPermissions.some(permission => {
    // Exact match
    if (permission === requiredPermission) return true;
    // Wildcard match
    if (permission.endsWith('*')) {
      const prefix = permission.slice(0, -1);
      return requiredPermission.startsWith(prefix);
    }
    return false;
  });
};

export const canAccessCountry = (
  userRegionScope: string | undefined,
  countryRegion: string,
  userRole: string
): boolean => {
  // Global roles can access all countries
  if (userRegionScope === 'GLOBAL' || userRole === 'VP_SALES' || userRole === 'GLOBAL_SALES_DIRECTOR') {
    return true;
  }

  // Regional managers can only access countries in their region
  if (userRegionScope && userRegionScope !== 'GLOBAL') {
    return countryRegion === userRegionScope;
  }

  // Analysts have read-only access based on their region scope
  return userRegionScope === countryRegion || userRegionScope === 'GLOBAL';
};
