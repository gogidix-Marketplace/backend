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

export function formatTime(date: string | Date): string {
  return new Intl.DateTimeFormat('en-US', {
    hour: '2-digit',
    minute: '2-digit',
  }).format(new Date(date))
}

export function formatDuration(minutes: number): string {
  const hours = Math.floor(minutes / 60)
  const mins = minutes % 60
  if (hours > 0) {
    return `${hours}h ${mins}m`
  }
  return `${mins}m`
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
    open: 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200',
    pending: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200',
    resolved: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200',
    closed: 'bg-gray-100 text-gray-800 dark:bg-gray-900 dark:text-gray-200',
    escalated: 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200',
    in_progress: 'bg-indigo-100 text-indigo-800 dark:bg-indigo-900 dark:text-indigo-200',
    customer_reply: 'bg-purple-100 text-purple-800 dark:bg-purple-900 dark:text-purple-200',
    on_track: 'bg-green-100 text-green-800 dark:bg-green-900 dark:text-green-200',
    at_risk: 'bg-yellow-100 text-yellow-800 dark:bg-yellow-900 dark:text-yellow-200',
    behind: 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200',
    ahead: 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200',
  }
  return statusColors[status] || 'bg-gray-100 text-gray-800'
}

export function getPriorityColor(priority: string): string {
  const priorityColors: Record<string, string> = {
    low: 'bg-slate-100 text-slate-800 dark:bg-slate-800 dark:text-slate-200',
    medium: 'bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200',
    high: 'bg-orange-100 text-orange-800 dark:bg-orange-900 dark:text-orange-200',
    urgent: 'bg-red-100 text-red-800 dark:bg-red-900 dark:text-red-200',
  }
  return priorityColors[priority] || 'bg-gray-100 text-gray-800'
}

export function getSLAStatusColor(status: 'ok' | 'warning' | 'breached'): string {
  const colors = {
    ok: 'text-green-600',
    warning: 'text-yellow-600',
    breached: 'text-red-600',
  }
  return colors[status]
}
