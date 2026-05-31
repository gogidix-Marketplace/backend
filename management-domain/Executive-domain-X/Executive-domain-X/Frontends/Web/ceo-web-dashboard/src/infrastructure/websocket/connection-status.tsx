import * as React from 'react'
import { Badge } from '@shared/components/ui/badge'
import { useConnectionStatus } from '@infrastructure/websocket/hooks'
import { cn } from '@shared/utils/cn'
import { Loader2, CheckCircle2, XCircle, AlertCircle } from 'lucide-react'

/**
 * Connection Status Indicator
 *
 * Displays the current WebSocket connection status
 * ● Connected (green) - All updates real-time
 * ● Connecting (amber) - Reconnecting...
 * ● Disconnected (red) - Offline mode
 * ● Error (red) - Connection failed
 */

const statusConfig = {
  connecting: {
    label: 'Connecting...',
    color: 'bg-amber-100 text-amber-700 dark:bg-amber-900/20 dark:text-amber-400 border-amber-200 dark:border-amber-800',
    icon: Loader2,
    pulse: true,
  },
  connected: {
    label: 'Live',
    color: 'bg-green-100 text-green-700 dark:bg-green-900/20 dark:text-green-400 border-green-200 dark:border-green-800',
    icon: CheckCircle2,
    pulse: false,
  },
  disconnected: {
    label: 'Offline',
    color: 'bg-slate-100 text-slate-700 dark:bg-slate-900/20 dark:text-slate-400 border-slate-200 dark:border-slate-800',
    icon: XCircle,
    pulse: false,
  },
  error: {
    label: 'Connection Error',
    color: 'bg-red-100 text-red-700 dark:bg-red-900/20 dark:text-red-400 border-red-200 dark:border-red-800',
    icon: AlertCircle,
    pulse: true,
  },
  reconnecting: {
    label: 'Reconnecting...',
    color: 'bg-amber-100 text-amber-700 dark:bg-amber-900/20 dark:text-amber-400 border-amber-200 dark:border-amber-800',
    icon: Loader2,
    pulse: true,
  },
}

export interface ConnectionStatusIndicatorProps {
  className?: string
  showLabel?: boolean
  variant?: 'badge' | 'dot' | 'full'
}

export function ConnectionStatusIndicator({
  className,
  showLabel = true,
  variant = 'badge',
}: ConnectionStatusIndicatorProps) {
  const status = useConnectionStatus()
  const config = statusConfig[status]
  const StatusIcon = config.icon

  if (variant === 'dot') {
    const dotColor = {
      connecting: 'bg-amber-500',
      connected: 'bg-green-500',
      disconnected: 'bg-slate-500',
      error: 'bg-red-500',
      reconnecting: 'bg-amber-500',
    }[status]

    return (
      <div className={cn('flex items-center gap-2', className)}>
        <div className="relative">
          <div className={cn('h-2 w-2 rounded-full', dotColor)} />
          {config.pulse && (
            <div className={cn('absolute inset-0 h-2 w-2 rounded-full', dotColor, 'animate-ping opacity-50')} />
          )}
        </div>
        {showLabel && <span className="text-xs text-muted-foreground">{config.label}</span>}
      </div>
    )
  }

  if (variant === 'full') {
    return (
      <div className={cn('flex items-center gap-2 px-3 py-1.5 rounded-lg border', config.color, className)}>
        <StatusIcon className={cn('h-4 w-4', config.pulse && 'animate-spin')} />
        <span className="text-sm font-medium">{config.label}</span>
      </div>
    )
  }

  // Default badge variant
  return (
    <Badge variant="outline" className={cn(config.color, className)}>
      <StatusIcon className={cn('h-3 w-3 mr-1', config.pulse && 'animate-spin')} />
      {showLabel && config.label}
    </Badge>
  )
}
