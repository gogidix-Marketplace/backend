import * as React from 'react'
import { cn } from '@shared/utils/cn'
import {
  AlertCircle,
  CheckCircle2,
  Info,
  AlertTriangle,
  X,
  LucideIcon,
} from 'lucide-react'

/**
 * Alert - Contextual alert messages
 *
 * Features:
 * - Success, warning, error, info variants
 * - Dismissible
 * - Icon display
 * - Actions support
 */

export interface AlertProps extends React.HTMLAttributes<HTMLDivElement> {
  variant?: 'default' | 'success' | 'warning' | 'destructive' | 'info'
  title?: string
  icon?: LucideIcon
  dismissible?: boolean
  onDismiss?: () => void
  actions?: React.ReactNode
}

const alertVariants = {
  default: {
    container: 'bg-background border-border text-foreground',
    icon: Info,
    iconColor: 'text-muted-foreground',
  },
  info: {
    container: 'bg-blue-50 dark:bg-blue-950/30 border-blue-200 dark:border-blue-800 text-blue-800 dark:text-blue-200',
    icon: Info,
    iconColor: 'text-blue-600 dark:text-blue-400',
  },
  success: {
    container: 'bg-green-50 dark:bg-green-950/30 border-green-200 dark:border-green-800 text-green-800 dark:text-green-200',
    icon: CheckCircle2,
    iconColor: 'text-green-600 dark:text-green-400',
  },
  warning: {
    container: 'bg-amber-50 dark:bg-amber-950/30 border-amber-200 dark:border-amber-800 text-amber-800 dark:text-amber-200',
    icon: AlertTriangle,
    iconColor: 'text-amber-600 dark:text-amber-400',
  },
  destructive: {
    container: 'bg-red-50 dark:bg-red-950/30 border-red-200 dark:border-red-800 text-red-800 dark:text-red-200',
    icon: AlertCircle,
    iconColor: 'text-red-600 dark:text-red-400',
  },
}

export const Alert = React.forwardRef<HTMLDivElement, AlertProps>(
  (
    {
      className,
      variant = 'default',
      title,
      icon: customIcon,
      dismissible = false,
      onDismiss,
      actions,
      children,
      ...props
    },
    ref
  ) => {
    const config = alertVariants[variant]
    const Icon = customIcon || config.icon

    return (
      <div
        ref={ref}
        role="alert"
        className={cn(
          'relative flex items-start gap-3 rounded-lg border p-4',
          config.container,
          className
        )}
        {...props}
      >
        <Icon className={cn('h-5 w-5 flex-shrink-0 mt-0.5', config.iconColor)} />

        <div className="flex-1 space-y-1">
          {title && <p className="font-medium">{title}</p>}
          {children && <div className="text-sm leading-relaxed">{children}</div>}
          {actions && <div className="mt-2">{actions}</div>}
        </div>

        {dismissible && (
          <button
            onClick={onDismiss}
            className={cn(
              'flex-shrink-0 rounded-sm opacity-70 transition-opacity',
              'hover:opacity-100 focus:outline-none focus:ring-2 focus:ring-ring',
              'disabled:pointer-events-none disabled:opacity-50'
            )}
          >
            <X className="h-4 w-4" />
            <span className="sr-only">Dismiss</span>
          </button>
        )}
      </div>
    )
  }
)
Alert.displayName = 'Alert'

/**
 * AlertTitle - Title component for Alert
 */
export const AlertTitle = React.forwardRef<
  HTMLParagraphElement,
  React.HTMLAttributes<HTMLHeadingElement>
>(({ className, ...props }, ref) => (
  <h5
    ref={ref}
    className={cn('mb-1 font-medium leading-none tracking-tight', className)}
    {...props}
  />
))
AlertTitle.displayName = 'AlertTitle'

/**
 * AlertDescription - Description component for Alert
 */
export const AlertDescription = React.forwardRef<
  HTMLParagraphElement,
  React.HTMLAttributes<HTMLParagraphElement>
>(({ className, ...props }, ref) => (
  <div
    ref={ref}
    className={cn('text-sm leading-relaxed', className)}
    {...props}
  />
))
AlertDescription.displayName = 'AlertDescription'

/**
 * InlineAlert - Compact inline variant
 */
export interface InlineAlertProps extends Omit<AlertProps, 'title'> {
  size?: 'sm' | 'md'
}

export const InlineAlert = React.forwardRef<HTMLDivElement, InlineAlertProps>(
  ({ className, variant = 'default', size = 'sm', children, ...props }, ref) => {
    const config = alertVariants[variant]
    const Icon = config.icon

    return (
      <div
        ref={ref}
        className={cn(
          'inline-flex items-center gap-2 rounded-md border px-2 py-1',
          size === 'sm' && 'text-xs',
          size === 'md' && 'text-sm',
          config.container,
          className
        )}
        {...props}
      >
        <Icon className={cn('h-3 w-3 flex-shrink-0', size === 'md' && 'h-4 w-4', config.iconColor)} />
        <span className="line-clamp-1">{children}</span>
      </div>
    )
  }
)
InlineAlert.displayName = 'InlineAlert'
