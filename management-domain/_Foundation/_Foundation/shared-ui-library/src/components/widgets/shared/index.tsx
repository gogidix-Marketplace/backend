import * as React from 'react'
import { Bell, AlertTriangle, CheckCircle, X, Plus, FileText, Users, Settings, Calendar } from 'lucide-react'
import { cn } from '@lib/utils'
import { Card } from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Badge } from '@/components/ui/badge'

// Activity Feed Widget
export interface ActivityItem {
  id: string
  type: 'info' | 'success' | 'warning' | 'error'
  icon?: React.ReactNode
  title: string
  description?: string
  timestamp: string
  user?: {
    name: string
    avatar?: string
  }
  actions?: Array<{
    label: string
    onClick: () => void
  }>
}

export interface ActivityFeedProps {
  activities: ActivityItem[]
  maxItems?: number
  className?: string
}

export function ActivityFeed({ activities, maxItems = 10, className }: ActivityFeedProps) {
  const getIcon = (type: string, icon?: React.ReactNode) => {
    if (icon) return icon

    switch (type) {
      case 'success':
        return <CheckCircle className="h-4 w-4 text-green-600" />
      case 'warning':
        return <AlertTriangle className="h-4 w-4 text-yellow-600" />
      case 'error':
        return <X className="h-4 w-4 text-red-600" />
      default:
        return <Bell className="h-4 w-4 text-blue-600" />
    }
  }

  const formatTimestamp = (timestamp: string) => {
    const date = new Date(timestamp)
    const now = new Date()
    const diffMs = now.getTime() - date.getTime()
    const diffMins = Math.floor(diffMs / 60000)

    if (diffMins < 1) return 'Just now'
    if (diffMins < 60) return `${diffMins}m ago`
    if (diffMins < 1440) return `${Math.floor(diffMins / 60)}h ago`
    return date.toLocaleDateString()
  }

  return (
    <Card className={cn('p-4', className)}>
      <div className="flex items-center gap-2 mb-4">
        <Bell className="h-5 w-5 text-primary-600" />
        <h3 className="font-semibold text-gray-900">Recent Activity</h3>
        <Badge variant="secondary" className="ml-auto">
          {activities.length}
        </Badge>
      </div>

      <div className="space-y-4">
        {activities.slice(0, maxItems).map((activity) => (
          <div key={activity.id} className="flex gap-3 pb-3 border-b border-gray-100 last:border-0 last:pb-0">
            {/* Icon */}
            <div className="flex-shrink-0 mt-0.5">
              {getIcon(activity.type, activity.icon)}
            </div>

            {/* Content */}
            <div className="flex-1 min-w-0">
              <div className="flex items-start justify-between gap-2">
                <p className="text-sm font-medium text-gray-900">{activity.title}</p>
                <span className="text-xs text-gray-500 whitespace-nowrap">
                  {formatTimestamp(activity.timestamp)}
                </span>
              </div>
              {activity.description && (
                <p className="text-sm text-gray-600 mt-1">{activity.description}</p>
              )}

              {/* User Info */}
              {activity.user && (
                <div className="flex items-center gap-2 mt-2">
                  <div className="h-6 w-6 rounded-full bg-primary-100 flex items-center justify-center text-xs font-medium text-primary-700">
                    {activity.user.name.split(' ').map((n) => n[0]).join('')}
                  </div>
                  <span className="text-xs text-gray-500">{activity.user.name}</span>
                </div>
              )}

              {/* Actions */}
              {activity.actions && activity.actions.length > 0 && (
                <div className="flex gap-2 mt-3">
                  {activity.actions.map((action, index) => (
                    <Button
                      key={index}
                      variant="ghost"
                      size="sm"
                      onClick={action.onClick}
                      className="h-7 px-2 text-xs"
                    >
                      {action.label}
                    </Button>
                  ))}
                </div>
              )}
            </div>
          </div>
        ))}
      </div>
    </Card>
  )
}

// Quick Actions Widget
export interface QuickAction {
  id: string
  label: string
  icon: React.ReactNode
  onClick: () => void
  variant?: 'default' | 'primary' | 'danger' | 'success'
  disabled?: boolean
}

export interface QuickActionsProps {
  actions: QuickAction[]
  title?: string
  columns?: number
  className?: string
}

export function QuickActions({ actions, title = 'Quick Actions', columns = 4, className }: QuickActionsProps) {
  return (
    <Card className={cn('p-4', className)}>
      <h3 className="font-semibold text-gray-900 mb-4">{title}</h3>
      <div
        className="grid gap-3"
        style={{ gridTemplateColumns: `repeat(${columns}, 1fr)` }}
      >
        {actions.map((action) => (
          <button
            key={action.id}
            onClick={action.onClick}
            disabled={action.disabled}
            className={cn(
              'flex flex-col items-center gap-2 p-3 rounded-lg border transition-all',
              'hover:bg-gray-50 hover:shadow-sm',
              'disabled:opacity-50 disabled:cursor-not-allowed',
              action.variant === 'primary' && 'border-primary-200 bg-primary-50 hover:bg-primary-100',
              action.variant === 'danger' && 'border-red-200 bg-red-50 hover:bg-red-100',
              action.variant === 'success' && 'border-green-200 bg-green-50 hover:bg-green-100'
            )}
          >
            <div className={cn(
              'p-2 rounded-full',
              action.variant === 'primary' && 'bg-primary-100 text-primary-700',
              action.variant === 'danger' && 'bg-red-100 text-red-700',
              action.variant === 'success' && 'bg-green-100 text-green-700',
              !action.variant && 'bg-gray-100 text-gray-700'
            )}>
              {action.icon}
            </div>
            <span className="text-sm font-medium text-gray-700">{action.label}</span>
          </button>
        ))}
      </div>
    </Card>
  )
}

// Alert Banner Widget
export interface AlertBannerProps {
  type?: 'info' | 'success' | 'warning' | 'error'
  title?: string
  message: string
  dismissible?: boolean
  onDismiss?: () => void
  actions?: Array<{
    label: string
    onClick: () => void
    variant?: 'default' | 'ghost'
  }>
  className?: string
}

export function AlertBanner({
  type = 'info',
  title,
  message,
  dismissible = true,
  onDismiss,
  actions,
  className,
}: AlertBannerProps) {
  const [isDismissed, setIsDismissed] = React.useState(false)

  if (isDismissed) return null

  const getStyling = () => {
    switch (type) {
      case 'success':
        return {
          container: 'bg-green-50 border-green-200',
          icon: 'text-green-600',
          title: 'text-green-900',
          message: 'text-green-700',
        }
      case 'warning':
        return {
          container: 'bg-yellow-50 border-yellow-200',
          icon: 'text-yellow-600',
          title: 'text-yellow-900',
          message: 'text-yellow-700',
        }
      case 'error':
        return {
          container: 'bg-red-50 border-red-200',
          icon: 'text-red-600',
          title: 'text-red-900',
          message: 'text-red-700',
        }
      default:
        return {
          container: 'bg-blue-50 border-blue-200',
          icon: 'text-blue-600',
          title: 'text-blue-900',
          message: 'text-blue-700',
        }
    }
  }

  const getIcon = () => {
    switch (type) {
      case 'success':
        return <CheckCircle className="h-5 w-5" />
      case 'warning':
        return <AlertTriangle className="h-5 w-5" />
      case 'error':
        return <X className="h-5 w-5" />
      default:
        return <Bell className="h-5 w-5" />
    }
  }

  const styles = getStyling()

  return (
    <div className={cn('border rounded-lg p-4', styles.container, className)}>
      <div className="flex items-start gap-3">
        {/* Icon */}
        <div className={cn('flex-shrink-0 mt-0.5', styles.icon)}>
          {getIcon()}
        </div>

        {/* Content */}
        <div className="flex-1 min-w-0">
          {title && (
            <h4 className={cn('font-semibold mb-1', styles.title)}>{title}</h4>
          )}
          <p className={cn('text-sm', styles.message)}>{message}</p>

          {/* Actions */}
          {actions && actions.length > 0 && (
            <div className="flex gap-2 mt-3">
              {actions.map((action, index) => (
                <Button
                  key={index}
                  size="sm"
                  variant={action.variant || 'default'}
                  onClick={action.onClick}
                  className="h-8"
                >
                  {action.label}
                </Button>
              ))}
            </div>
          )}
        </div>

        {/* Dismiss Button */}
        {dismissible && (
          <button
            onClick={() => {
              setIsDismissed(true)
              onDismiss?.()
            }}
            className="flex-shrink-0 text-gray-400 hover:text-gray-600"
          >
            <X className="h-5 w-5" />
          </button>
        )}
      </div>
    </div>
  )
}

// Default Quick Actions for Dashboard
export interface DefaultQuickActionsProps {
  onCreateReport?: () => void
  onAddUser?: () => void
  onNewInitiative?: () => void
  onViewAnalytics?: () => void
  onSettings?: () => void
  className?: string
}

export function DefaultQuickActions({
  onCreateReport,
  onAddUser,
  onNewInitiative,
  onViewAnalytics,
  onSettings,
  className,
}: DefaultQuickActionsProps) {
  const quickActions: QuickAction[] = [
    {
      id: 'report',
      label: 'Create Report',
      icon: <FileText className="h-5 w-5" />,
      onClick: onCreateReport || (() => console.log('Create Report')),
    },
    {
      id: 'user',
      label: 'Add User',
      icon: <Users className="h-5 w-5" />,
      onClick: onAddUser || (() => console.log('Add User')),
    },
    {
      id: 'initiative',
      label: 'New Initiative',
      icon: <Plus className="h-5 w-5" />,
      onClick: onNewInitiative || (() => console.log('New Initiative')),
    },
    {
      id: 'analytics',
      label: 'Analytics',
      icon: <FileText className="h-5 w-5" />,
      onClick: onViewAnalytics || (() => console.log('Analytics')),
    },
    {
      id: 'settings',
      label: 'Settings',
      icon: <Settings className="h-5 w-5" />,
      onClick: onSettings || (() => console.log('Settings')),
    },
    {
      id: 'calendar',
      label: 'Schedule',
      icon: <Calendar className="h-5 w-5" />,
      onClick: () => console.log('Schedule'),
    },
  ]

  return <QuickActions actions={quickActions} className={className} />
}
