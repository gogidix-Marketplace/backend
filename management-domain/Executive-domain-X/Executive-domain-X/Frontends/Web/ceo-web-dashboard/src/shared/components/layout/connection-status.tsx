import { Badge } from '@shared/components/ui/badge'
import { useConnectionStatus } from '@infrastructure/websocket/hooks'
import { cn } from '@shared/utils/cn'
import { Wifi, WifiOff, Loader2, AlertCircle } from 'lucide-react'

/**
 * ConnectionStatusIndicator
 *
 * Shows WebSocket connection status
 *
 * ● Connected (green) - All updates real-time
 * ● Connecting (amber) - Reconnecting...
 * ● Disconnected (red) - Offline mode
 * ● Error (red) - Connection failed
 */

export interface ConnectionStatusIndicatorProps {
  showLabel?: boolean
  position?: 'header' | 'footer'
  className?: string
}

const statusConfig = {
  connected: {
    label: 'Connected',
    color: 'bg-green-500',
    textColor: 'text-green-600',
    icon: Wifi,
  },
  connecting: {
    label: 'Connecting...',
    color: 'bg-amber-500',
    textColor: 'text-amber-600',
    icon: Loader2,
  },
  reconnecting: {
    label: 'Reconnecting...',
    color: 'bg-amber-500',
    textColor: 'text-amber-600',
    icon: Loader2,
  },
  disconnected: {
    label: 'Disconnected',
    color: 'bg-red-500',
    textColor: 'text-red-600',
    icon: WifiOff,
  },
  error: {
    label: 'Connection Error',
    color: 'bg-red-500',
    textColor: 'text-red-600',
    icon: AlertCircle,
  },
}

export function ConnectionStatusIndicator({
  showLabel = false,
  position = 'header',
  className,
}: ConnectionStatusIndicatorProps) {
  const status = useConnectionStatus()
  const config = statusConfig[status]
  const Icon = config.icon

  return (
    <div className={cn('flex items-center gap-2', className)}>
      <div className="flex items-center gap-1.5">
        <div
          className={cn(
            'h-2 w-2 rounded-full transition-colors',
            config.color,
            status === 'connecting' && 'animate-pulse'
          )}
        />
        {showLabel && (
          <span className={cn('text-xs font-medium', config.textColor)}>
            {config.label}
          </span>
        )}
        <Icon
          className={cn(
            'h-3.5 w-3.5',
            config.textColor,
            status === 'connecting' && 'animate-spin'
          )}
        />
      </div>
    </div>
  )
}

/**
 * Compact version - just the dot
 */
export function ConnectionDot({ className }: { className?: string }) {
  const status = useConnectionStatus()
  const config = statusConfig[status]

  return (
    <div
      className={cn(
        'h-2 w-2 rounded-full transition-colors',
        config.color,
        status === 'connecting' && 'animate-pulse',
        className
      )}
      title={config.label}
    />
  )
}

/**
 * Status bar component for bottom of screen
 */
export function ConnectionStatusBar() {
  const status = useConnectionStatus()
  const config = statusConfig[status]
  const Icon = config.icon

  return (
    <div
      className={cn(
        'fixed bottom-0 left-0 right-0 px-4 py-1.5 border-t text-xs flex items-center justify-between',
        status === 'connected'
          ? 'bg-green-50 border-green-200 dark:bg-green-950 dark:border-green-800'
          : 'bg-red-50 border-red-200 dark:bg-red-950 dark:border-red-800'
      )}
    >
      <div className="flex items-center gap-2">
        <Icon className={cn('h-3 w-3', config.textColor)} />
        <span className={cn('font-medium', config.textColor)}>{config.label}</span>
        {status !== 'connected' && (
          <span className="text-muted-foreground">
            Some features may be limited
          </span>
        )}
      </div>
      <button
        className="text-xs underline hover:no-underline"
        onClick={() => {
          // Trigger reconnect
          const client = require('@infrastructure/websocket/websocket-client').getWebSocketClient()
          client?.reconnect()
        }}
      >
        {status !== 'connected' ? 'Reconnect' : 'Settings'}
      </button>
    </div>
  )
}
