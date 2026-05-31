import { type ClassValue, clsx } from 'clsx';
import { twMerge } from 'tailwind-merge';
import { format, formatDistanceToNow } from 'date-fns';

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

export function formatDate(date: string | Date, formatStr = 'MMM dd, yyyy HH:mm') {
  if (!date) return '-';
  const d = typeof date === 'string' ? new Date(date) : date;
  return format(d, formatStr);
}

export function formatRelativeTime(date: string | Date) {
  if (!date) return '-';
  const d = typeof date === 'string' ? new Date(date) : date;
  return formatDistanceToNow(d, { addSuffix: true });
}

export function formatMinutes(minutes: number): string {
  if (minutes < 60) return `${minutes}m`;
  const hours = Math.floor(minutes / 60);
  const mins = minutes % 60;
  return mins > 0 ? `${hours}h ${mins}m` : `${hours}h`;
}

export function formatHours(hours: number): string {
  if (hours < 24) return `${hours}h`;
  const days = Math.floor(hours / 24);
  const hrs = hours % 24;
  return hrs > 0 ? `${days}d ${hrs}h` : `${days}d`;
}

export function getPriorityColor(priority: string): string {
  switch (priority) {
    case 'CRITICAL':
      return 'text-danger-600 bg-danger-50 border-danger-200';
    case 'HIGH':
      return 'text-warning-600 bg-warning-50 border-warning-200';
    case 'MEDIUM':
      return 'text-primary-600 bg-primary-50 border-primary-200';
    case 'LOW':
      return 'text-gray-600 bg-gray-50 border-gray-200';
    default:
      return 'text-gray-600 bg-gray-50 border-gray-200';
  }
}

export function getStatusColor(status: string): string {
  switch (status) {
    case 'OPEN':
      return 'text-blue-600 bg-blue-50 border-blue-200';
    case 'IN_PROGRESS':
      return 'text-yellow-600 bg-yellow-50 border-yellow-200';
    case 'RESOLVED':
      return 'text-green-600 bg-green-50 border-green-200';
    case 'CLOSED':
      return 'text-gray-600 bg-gray-50 border-gray-200';
    case 'ESCALATED':
      return 'text-purple-600 bg-purple-50 border-purple-200';
    case 'PENDING_CUSTOMER':
      return 'text-orange-600 bg-orange-50 border-orange-200';
    default:
      return 'text-gray-600 bg-gray-50 border-gray-200';
  }
}

export function getSLAStatusColor(status: string): string {
  switch (status) {
    case 'COMPLIANT':
      return 'text-success-600 bg-success-50';
    case 'AT_RISK':
      return 'text-warning-600 bg-warning-50';
    case 'BREACHED':
      return 'text-danger-600 bg-danger-50';
    default:
      return 'text-gray-600 bg-gray-50';
  }
}

export function getAgentStatusColor(status: string): string {
  switch (status) {
    case 'AVAILABLE':
      return 'text-success-600 bg-success-50';
    case 'BUSY':
      return 'text-warning-600 bg-warning-50';
    case 'AWAY':
    case 'ON_BREAK':
      return 'text-gray-600 bg-gray-50';
    case 'IN_MEETING':
      return 'text-purple-600 bg-purple-50';
    case 'OFFLINE':
      return 'text-danger-600 bg-danger-50';
    default:
      return 'text-gray-600 bg-gray-50';
  }
}

export function getInitials(name: string): string {
  return name
    .split(' ')
    .map((n) => n[0])
    .join('')
    .toUpperCase()
    .slice(0, 2);
}

export function calculatePercentage(value: number, total: number): number {
  if (total === 0) return 0;
  return Math.round((value / total) * 100);
}

export function truncateText(text: string, maxLength: number): string {
  if (text.length <= maxLength) return text;
  return text.slice(0, maxLength) + '...';
}

export function generateId(): string {
  return Math.random().toString(36).substr(2, 9);
}
