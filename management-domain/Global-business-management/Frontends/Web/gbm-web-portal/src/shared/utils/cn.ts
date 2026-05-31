import { clsx, type ClassValue } from 'clsx'
import { twMerge } from 'tailwind-merge'

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function formatCurrency(value: number, currency: string = 'USD'): string {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency,
  }).format(value)
}

export function formatNumber(value: number): string {
  return new Intl.NumberFormat('en-US').format(value)
}

export function formatPercentage(value: number): string {
  return `${value >= 0 ? '+' : ''}${value.toFixed(1)}%`
}

export function formatDate(date: string | Date): string {
  return new Intl.DateTimeFormat('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
  }).format(new Date(date))
}

export function formatDateTime(date: string | Date): string {
  return new Intl.DateTimeFormat('en-US', {
    year: 'numeric',
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  }).format(new Date(date))
}

export function getTrendIcon(trend: 'up' | 'down' | 'neutral'): string {
  switch (trend) {
    case 'up':
      return '↑'
    case 'down':
      return '↓'
    case 'neutral':
      return '→'
  }
}

export function getStatusColor(status: string): string {
  const statusColors: Record<string, string> = {
    pending: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200',
    in_progress: 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200',
    completed: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200',
    cancelled: 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200',
    failed: 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200',
    approved: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200',
    rejected: 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200',
    on_track: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200',
    at_risk: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200',
    behind: 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200',
    ahead: 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200',
    running: 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200',
  }
  return statusColors[status] || 'bg-gray-100 text-gray-800'
}

export function getRegionName(region: string): string {
  const regionNames: Record<string, string> = {
    'north-america': 'North America',
    'europe': 'Europe',
    'africa-north': 'Africa North',
    'africa-west': 'Africa West',
    'africa-east': 'Africa East',
    'africa-south': 'Africa South',
    'asia-pacific': 'Asia Pacific',
    'latin-america': 'Latin America',
    'middle-east': 'Middle East',
  }
  return regionNames[region] || region
}

export function getCountryName(country: string): string {
  const countryNames: Record<string, string> = {
    'US': 'United States',
    'CA': 'Canada',
    'UK': 'United Kingdom',
    'NG': 'Nigeria',
    'KE': 'Kenya',
    'ZA': 'South Africa',
    'GH': 'Ghana',
    'EG': 'Egypt',
    'IN': 'India',
    'JP': 'Japan',
    'AU': 'Australia',
    'BR': 'Brazil',
    'MX': 'Mexico',
    'AE': 'United Arab Emirates',
    'SA': 'Saudi Arabia',
  }
  return countryNames[country] || country
}

export function getCurrencySymbol(currency: string): string {
  const symbols: Record<string, string> = {
    'USD': '$',
    'EUR': '€',
    'GBP': '£',
    'NGN': '₦',
    'KES': 'KSh',
    'ZAR': 'R',
    'GHS': '₵',
    'EGP': 'E£',
    'INR': '₹',
    'JPY': '¥',
    'AUD': 'A$',
    'BRL': 'R$',
    'MXN': 'MX$',
    'AED': 'د.إ',
    'SAR': '﷼',
  }
  return symbols[currency] || currency
}

export function calculateProgress(value: number, target: number): number {
  if (target === 0) return 0
  return Math.min((value / target) * 100, 100)
}

export function truncateText(text: string, maxLength: number): string {
  if (text.length <= maxLength) return text
  return text.slice(0, maxLength) + '...'
}
