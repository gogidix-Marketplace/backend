import * as React from 'react'
import { cn } from '@shared/utils/cn'
import {
  CheckCircle2,
  AlertCircle,
  AlertTriangle,
  Info,
  X,
  Loader2,
} from 'lucide-react'

/**
 * Toast - Temporary notification messages
 *
 * Features:
 * - Auto-dismiss after timeout
 * - Manual dismiss
 * - Multiple variants
 * - Action buttons
 * - Progress indicator for auto-dismiss
 */

export type ToastVariant = 'default' | 'success' | 'error' | 'warning' | 'info' | 'loading'

export interface ToastProps {
  id: string
  variant?: ToastVariant
  title?: string
  message: string
  duration?: number // ms, 0 = no auto-dismiss
  action?: {
    label: string
    onClick: () => void
  }
  onDismiss?: (id: string) => void
  showProgress?: boolean
  position?: 'top-left' | 'top-right' | 'bottom-left' | 'bottom-right' | 'top-center' | 'bottom-center'
}

const toastVariants = {
  default: {
    container: 'bg-background border-border text-foreground',
    icon: null,
  },
  success: {
    container: 'bg-white dark:bg-slate-900 border-green-500',
    icon: CheckCircle2,
    iconColor: 'text-green-600 dark:text-green-400',
  },
  error: {
    container: 'bg-white dark:bg-slate-900 border-red-500',
    icon: AlertCircle,
    iconColor: 'text-red-600 dark:text-red-400',
  },
  warning: {
    container: 'bg-white dark:bg-slate-900 border-amber-500',
    icon: AlertTriangle,
    iconColor: 'text-amber-600 dark:text-amber-400',
  },
  info: {
    container: 'bg-white dark:bg-slate-900 border-blue-500',
    icon: Info,
    iconColor: 'text-blue-600 dark:text-blue-400',
  },
  loading: {
    container: 'bg-white dark:bg-slate-900 border-primary',
    icon: Loader2,
    iconColor: 'text-primary animate-spin',
  },
}

export function Toast({
  variant = 'default',
  title,
  message,
  duration = 5000,
  action,
  onDismiss,
  showProgress = true,
}: ToastProps) {
  const [remaining, setRemaining] = React.useState(duration)
  const [isPaused, setIsPaused] = React.useState(false)
  const timeoutRef = React.useRef<ReturnType<typeof setTimeout>>()
  const intervalRef = React.useRef<ReturnType<typeof setInterval>>()

  React.useEffect(() => {
    if (duration === 0) return

    timeoutRef.current = setTimeout(() => {
      if (onDismiss) onDismiss('')
    }, duration)

    return () => clearTimeout(timeoutRef.current)
  }, [duration, onDismiss])

  React.useEffect(() => {
    if (duration === 0 || isPaused) return

    intervalRef.current = setInterval(() => {
      setRemaining((prev) => {
        if (prev <= 100) {
          clearInterval(intervalRef.current)
          return 0
        }
        return prev - 100
      })
    }, 100)

    return () => clearInterval(intervalRef.current)
  }, [duration, isPaused])

  const handleMouseEnter = () => setIsPaused(true)
  const handleMouseLeave = () => setIsPaused(false)

  const config = toastVariants[variant]
  const Icon = config.icon

  return (
    <div
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
      className={cn(
        'flex w-full max-w-sm items-start gap-3 rounded-lg border-l-4 p-4 shadow-lg animate-in slide-in-from-right',
        config.container
      )}
    >
      {Icon && <Icon className={cn('h-5 w-5 flex-shrink-0 mt-0.5', config.iconColor)} />}

      <div className="flex-1 space-y-1">
        {title && <p className="font-semibold text-sm">{title}</p>}
        <p className="text-sm leading-relaxed">{message}</p>
        {action && (
          <button
            onClick={action.onClick}
            className="text-sm font-medium text-primary hover:underline mt-1"
          >
            {action.label}
          </button>
        )}
      </div>

      {onDismiss && (
        <button
          onClick={() => onDismiss('')}
          className="flex-shrink-0 rounded-sm opacity-70 transition-opacity hover:opacity-100"
        >
          <X className="h-4 w-4" />
        </button>
      )}

      {showProgress && duration > 0 && (
        <div className="absolute bottom-0 left-0 h-0.5 bg-muted">
          <div
            className="h-full bg-primary transition-all duration-100 ease-linear"
            style={{ width: `${(remaining / duration) * 100}%` }}
          />
        </div>
      )}
    </div>
  )
}

/**
 * Toast Container - Manages multiple toasts
 */
export interface ToastContainerProps {
  toasts: ToastProps[]
  onDismiss: (id: string) => void
  position?: ToastProps['position']
}

export function ToastContainer({ toasts, onDismiss, position = 'top-right' }: ToastContainerProps) {
  if (toasts.length === 0) return null

  const positionClasses = {
    'top-left': 'top-4 left-4',
    'top-right': 'top-4 right-4',
    'top-center': 'top-4 left-1/2 -translate-x-1/2',
    'bottom-left': 'bottom-4 left-4',
    'bottom-right': 'bottom-4 right-4',
    'bottom-center': 'bottom-4 left-1/2 -translate-x-1/2',
  }

  return (
    <div
      className={cn(
        'fixed z-50 flex flex-col gap-2 p-4 pointer-events-none',
        positionClasses[position || 'top-right'],
        position?.includes('bottom') && 'flex-col-reverse'
      )}
    >
      {toasts.map((toast) => (
        <div key={toast.id} className="pointer-events-auto">
          <Toast {...toast} onDismiss={onDismiss} />
        </div>
      ))}
    </div>
  )
}

/**
 * Toast Context & Hook for easy usage
 */
interface ToastContextValue {
  toasts: ToastProps[]
  toast: (props: Omit<ToastProps, 'id' | 'onDismiss'>) => string
  dismiss: (id: string) => void
  dismissAll: () => void
}

const ToastContext = React.createContext<ToastContextValue | undefined>(undefined)

export function ToastProvider({ children }: { children: React.ReactNode }) {
  const [toasts, setToasts] = React.useState<ToastProps[]>([])
  const idCounter = React.useRef(0)

  const toast = React.useCallback((props: Omit<ToastProps, 'id' | 'onDismiss'>) => {
    const id = `toast-${idCounter.current++}`
    const newToast: ToastProps = {
      ...props,
      id,
      onDismiss: (dismissId) => {
        setToasts((prev) => prev.filter((t) => t.id !== dismissId))
        props.onDismiss?.(dismissId)
      },
    }
    setToasts((prev) => [...prev, newToast])
    return id
  }, [])

  const dismiss = React.useCallback((id: string) => {
    setToasts((prev) => prev.filter((t) => t.id !== id))
  }, [])

  const dismissAll = React.useCallback(() => {
    setToasts([])
  }, [])

  return (
    <ToastContext.Provider value={{ toasts, toast, dismiss, dismissAll }}>
      {children}
      <ToastContainer toasts={toasts} onDismiss={dismiss} position="top-right" />
    </ToastContext.Provider>
  )
}

export function useToast() {
  const context = React.useContext(ToastContext)
  if (!context) {
    throw new Error('useToast must be used within a ToastProvider')
  }
  return context
}
