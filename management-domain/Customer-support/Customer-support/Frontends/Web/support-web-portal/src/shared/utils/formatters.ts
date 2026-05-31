/**
 * Shared Utility: Formatters
 * Common formatting functions
 */

export function formatNumber(value: number, decimals: number = 1): string {
  if (value >= 1000000) {
    return (value / 1000000).toFixed(decimals) + 'M';
  }
  if (value >= 1000) {
    return (value / 1000).toFixed(decimals) + 'K';
  }
  return value.toFixed(decimals);
}

export function formatPercentage(value: number, decimals: number = 1): string {
  return value.toFixed(decimals) + '%';
}

export function formatDuration(hours: number): string {
  if (hours < 1) {
    const minutes = Math.round(hours * 60);
    return `${minutes}m`;
  }
  if (hours < 24) {
    const h = Math.floor(hours);
    const m = Math.round((hours - h) * 60);
    return m > 0 ? `${h}h ${m}m` : `${h}h`;
  }
  const days = Math.floor(hours / 24);
  const h = Math.round(hours % 24);
  return h > 0 ? `${days}d ${h}h` : `${days}d`;
}

export function formatDate(date: Date | string): string {
  const d = typeof date === 'string' ? new Date(date) : date;
  return d.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
}

export function formatDateTime(date: Date | string): string {
  const d = typeof date === 'string' ? new Date(date) : date;
  return d.toLocaleString('en-US', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  });
}

export function getTrendIcon(trend: 'up' | 'down' | 'stable'): string {
  switch (trend) {
    case 'up':
      return '↑';
    case 'down':
      return '↓';
    case 'stable':
      return '→';
  }
}

export function getTrendColor(trend: 'up' | 'down' | 'stable', isInverted: boolean = false): string {
  if (isInverted) {
    switch (trend) {
      case 'up':
        return 'text-danger-500';
      case 'down':
        return 'text-success-500';
      case 'stable':
        return 'text-gray-500';
    }
  }
  switch (trend) {
    case 'up':
      return 'text-success-500';
    case 'down':
      return 'text-danger-500';
    case 'stable':
      return 'text-gray-500';
  }
}
