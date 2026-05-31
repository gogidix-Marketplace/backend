import * as React from 'react'
import { cn } from '@lib/utils'

export interface TimelineItem {
  id: string
  title: string
  description?: string
  timestamp: string
  icon?: React.ReactNode
  status?: 'default' | 'success' | 'warning' | 'error' | 'info'
}

export interface TimelineProps {
  items: TimelineItem[]
  variant?: 'left' | 'right' | 'alternate'
  className?: string
  showConnectionLines?: boolean
}

export function Timeline({
  items,
  variant = 'left',
  className,
  showConnectionLines = true,
}: TimelineProps) {
  const getAlignmentClass = (index: number) => {
    if (variant === 'left') return 'flex-start'
    if (variant === 'right') return 'flex-end'
    return index % 2 === 0 ? 'flex-start' : 'flex-end'
  }

  const getStatusColor = (status?: string) => {
    switch (status) {
      case 'success':
        return 'bg-green-100 text-green-700 border-green-300'
      case 'warning':
        return 'bg-yellow-100 text-yellow-700 border-yellow-300'
      case 'error':
        return 'bg-red-100 text-red-700 border-red-300'
      case 'info':
        return 'bg-blue-100 text-blue-700 border-blue-300'
      default:
        return 'bg-gray-100 text-gray-700 border-gray-300'
    }
  }

  return (
    <div className={cn('relative', className)}>
      {showConnectionLines && (
        <div className="absolute left-4 top-2 bottom-2 w-0.5 bg-gray-200" />
      )}

      <div className="space-y-6">
        {items.map((item, index) => (
          <div
            key={item.id}
            className={cn('flex gap-4', getAlignmentClass(index))}
          >
            {/* Timeline Dot */}
            <div className="relative z-10 flex-shrink-0">
              <div
                className={cn(
                  'flex items-center justify-center w-8 h-8 rounded-full border-2',
                  getStatusColor(item.status)
                )}
              >
                {item.icon}
              </div>
            </div>

            {/* Timeline Content */}
            <div className={cn('flex-1 pb-2')}>
              <div className="bg-white border border-gray-200 rounded-lg p-4 shadow-sm">
                <div className="flex items-start justify-between gap-4">
                  <div className="flex-1">
                    <h4 className="font-medium text-gray-900">{item.title}</h4>
                    {item.description && (
                      <p className="text-sm text-gray-600 mt-1">{item.description}</p>
                    )}
                  </div>
                  <span className="text-xs text-gray-500 whitespace-nowrap">
                    {item.timestamp}
                  </span>
                </div>
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}

// Simple Timeline for Activity Feed
export interface ActivityTimelineProps {
  activities: {
    id: string
    user?: string
    action: string
    target?: string
    timestamp: string
    icon?: React.ReactNode
  }[]
  className?: string
}

export function ActivityTimeline({
  activities,
  className,
}: ActivityTimelineProps) {
  const formatTimestamp = (timestamp: string) => {
    const date = new Date(timestamp)
    const now = new Date()
    const diffMs = now.getTime() - date.getTime()
    const diffMins = Math.floor(diffMs / 60000)
    const diffHours = Math.floor(diffMs / 3600000)
    const diffDays = Math.floor(diffMs / 86400000)

    if (diffMins < 1) return 'Just now'
    if (diffMins < 60) return `${diffMins}m ago`
    if (diffHours < 24) return `${diffHours}h ago`
    if (diffDays < 7) return `${diffDays}d ago`
    return date.toLocaleDateString()
  }

  return (
    <div className={cn('space-y-4', className)}>
      <div className="absolute left-3 top-0 bottom-0 w-0.5 bg-gray-200" />
      {activities.map((activity, index) => (
        <div key={activity.id} className="relative flex gap-3 pl-8">
          {/* Timeline Dot */}
          <div
            className={cn(
              'absolute left-0 w-6 h-6 rounded-full border-2 bg-white flex items-center justify-center z-10',
              'border-gray-300 text-gray-500'
            )}
          >
            {activity.icon}
          </div>

          {/* Content */}
          <div className="flex-1 pb-4">
            <p className="text-sm">
              {activity.user && (
                <span className="font-medium text-gray-900">{activity.user}</span>
              )}{' '}
              <span className="text-gray-600">{activity.action}</span>
              {activity.target && (
                <span className="font-medium text-gray-900"> {activity.target}</span>
              )}
            </p>
            <span className="text-xs text-gray-500">
              {formatTimestamp(activity.timestamp)}
            </span>
          </div>
        </div>
      ))}
    </div>
  )
}
