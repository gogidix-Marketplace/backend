import { clsx, type ClassValue } from 'clsx'
import { twMerge } from 'tailwind-merge'

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs))
}

export function formatNumber(value: number): string {
  return new Intl.NumberFormat('en-US').format(value)
}

export function formatCurrency(value: number, currency: string = 'USD'): string {
  return new Intl.NumberFormat('en-US', {
    style: 'currency',
    currency,
  }).format(value)
}

export function formatPercentage(value: number, decimals: number = 1): string {
  return `${value >= 0 ? '+' : ''}${value.toFixed(decimals)}%`
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

export function formatTime(date: string | Date): string {
  return new Intl.DateTimeFormat('en-US', {
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit',
  }).format(new Date(date))
}

export function formatRelativeTime(date: string | Date): string {
  const now = new Date()
  const then = new Date(date)
  const diffMs = now.getTime() - then.getTime()
  const diffSecs = Math.floor(diffMs / 1000)
  const diffMins = Math.floor(diffSecs / 60)
  const diffHours = Math.floor(diffMins / 60)
  const diffDays = Math.floor(diffHours / 24)

  if (diffSecs < 60) return 'just now'
  if (diffMins < 60) return `${diffMins}m ago`
  if (diffHours < 24) return `${diffHours}h ago`
  if (diffDays < 7) return `${diffDays}d ago`
  return formatDate(date)
}

export function formatDuration(ms: number): string {
  const seconds = Math.floor(ms / 1000)
  const minutes = Math.floor(seconds / 60)
  const hours = Math.floor(minutes / 60)
  const days = Math.floor(hours / 24)

  if (days > 0) return `${days}d ${hours % 24}h`
  if (hours > 0) return `${hours}h ${minutes % 60}m`
  if (minutes > 0) return `${minutes}m ${seconds % 60}s`
  return `${seconds}s`
}

export function formatResponseTime(ms: number): string {
  if (ms < 1000) return `${Math.round(ms)}ms`
  return `${(ms / 1000).toFixed(2)}s`
}

export function formatBytes(bytes: number): string {
  const units = ['B', 'KB', 'MB', 'GB', 'TB']
  let size = bytes
  let unitIndex = 0

  while (size >= 1024 && unitIndex < units.length - 1) {
    size /= 1024
    unitIndex++
  }

  return `${size.toFixed(unitIndex === 0 ? 0 : 1)} ${units[unitIndex]}`
}

export function formatRequestsPerSecond(rps: number): string {
  if (rps < 1000) return `${rps.toFixed(1)} req/s`
  return `${(rps / 1000).toFixed(1)}k req/s`
}

export function getStatusColor(status: string): string {
  const statusColors: Record<string, string> = {
    healthy: 'text-green-600 bg-green-50 dark:bg-green-900/20 dark:text-green-400',
    degraded: 'text-yellow-600 bg-yellow-50 dark:bg-yellow-900/20 dark:text-yellow-400',
    critical: 'text-red-600 bg-red-50 dark:bg-red-900/20 dark:text-red-400',
    unknown: 'text-gray-600 bg-gray-50 dark:bg-gray-900/20 dark:text-gray-400',
    active: 'text-blue-600 bg-blue-50 dark:bg-blue-900/20 dark:text-blue-400',
    acknowledged: 'text-yellow-600 bg-yellow-50 dark:bg-yellow-900/20 dark:text-yellow-400',
    resolved: 'text-green-600 bg-green-50 dark:bg-green-900/20 dark:text-green-400',
    dismissed: 'text-gray-600 bg-gray-50 dark:bg-gray-900/20 dark:text-gray-400',
    open: 'text-red-600 bg-red-50 dark:bg-red-900/20 dark:text-red-400',
    investigating: 'text-yellow-600 bg-yellow-50 dark:bg-yellow-900/20 dark:text-yellow-400',
    monitoring: 'text-blue-600 bg-blue-50 dark:bg-blue-900/20 dark:text-blue-400',
  }
  return statusColors[status] || 'text-gray-600 bg-gray-50'
}

export function getSeverityColor(severity: string): string {
  const severityColors: Record<string, string> = {
    info: 'text-blue-600 bg-blue-50 dark:bg-blue-900/20 dark:text-blue-400',
    warning: 'text-yellow-600 bg-yellow-50 dark:bg-yellow-900/20 dark:text-yellow-400',
    error: 'text-orange-600 bg-orange-50 dark:bg-orange-900/20 dark:text-orange-400',
    critical: 'text-red-600 bg-red-50 dark:bg-red-900/20 dark:text-red-400',
  }
  return severityColors[severity] || 'text-gray-600 bg-gray-50'
}

export function getStatusDotColor(status: string): string {
  const statusColors: Record<string, string> = {
    healthy: 'bg-green-500',
    degraded: 'bg-yellow-500',
    critical: 'bg-red-500',
    unknown: 'bg-gray-400',
  }
  return statusColors[status] || 'bg-gray-400'
}

export function getSeverityDotColor(severity: string): string {
  const severityColors: Record<string, string> = {
    info: 'bg-blue-500',
    warning: 'bg-yellow-500',
    error: 'bg-orange-500',
    critical: 'bg-red-500',
  }
  return severityColors[severity] || 'bg-gray-400'
}

export function calculatePercentage(value: number, total: number): number {
  if (total === 0) return 0
  return (value / total) * 100
}

export function truncateText(text: string, maxLength: number): string {
  if (text.length <= maxLength) return text
  return `${text.substring(0, maxLength)}...`
}
