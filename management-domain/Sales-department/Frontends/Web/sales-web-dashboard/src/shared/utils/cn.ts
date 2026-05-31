import { clsx, type ClassValue } from 'clsx'
import { twMerge } from 'tailwind-merge'

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function formatCurrency(value: number, currency: string = 'USD'): string {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency,
    minimumFractionDigits: 0,
    maximumFractionDigits: 0,
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

export function getStageColor(stage: string): string {
  const stageColors: Record<string, string> = {
    prospecting: 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200',
    qualification: 'bg-cyan-100 text-cyan-800 dark:bg-cyan-900 dark:text-cyan-200',
    proposal: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200',
    negotiation: 'bg-orange-100 text-orange-800 dark:bg-orange-900 dark:text-orange-200',
    closed_won: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200',
    closed_lost: 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200',
  }
  return stageColors[stage] || 'bg-gray-100 text-gray-800'
}

export function getPriorityColor(priority: string): string {
  const priorityColors: Record<string, string> = {
    low: 'bg-gray-100 text-gray-800',
    medium: 'bg-blue-100 text-blue-800',
    high: 'bg-orange-100 text-orange-800',
    urgent: 'bg-red-100 text-red-800',
  }
  return priorityColors[priority] || 'bg-gray-100 text-gray-800'
}

export function getLeadStatusColor(status: string): string {
  const statusColors: Record<string, string> = {
    new: 'bg-purple-100 text-purple-800',
    contacted: 'bg-blue-100 text-blue-800',
    qualified: 'bg-green-100 text-green-800',
    unqualified: 'bg-gray-100 text-gray-800',
    converted: 'bg-teal-100 text-teal-800',
    lost: 'bg-red-100 text-red-800',
  }
  return statusColors[status] || 'bg-gray-100 text-gray-800'
}

export function getCountryFlag(countryCode: string): string {
  const flags: Record<string, string> = {
    NG: '🇳🇬',
    KE: '🇰🇪',
    GH: '🇬🇭',
    ZA: '🇿🇦',
    ET: '🇪🇹',
    UG: '🇺🇬',
    TZ: '🇹🇿',
    RW: '🇷🇼',
    CI: '🇨🇮',
    SN: '🇸🇳',
  }
  return flags[countryCode] || '🌍'
}
