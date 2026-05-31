import * as React from 'react'
import { cn } from '@shared/utils/cn'
import { Button } from '@shared/components/ui/button'
import { X } from 'lucide-react'

/**
 * Snackbar - Bottom notification with actions
 *
 * Features:
 * - Appears at bottom of screen
 * - Action buttons
 * - Dismissible
 * - Auto-dismiss option
 * - Stacking for multiple messages
 */

export interface SnackbarProps {
  id: string
  message: string
  action?: {
    label: string
    onClick: () => void
  }
  dismissable?: boolean
  onDismiss?: (id: string) => void
  duration?: number // 0 = no auto-dismiss
  variant?: 'default' | 'success' | 'error' | 'warning'
}

const snackbarVariants = {
  default: 'bg-slate-900 dark:bg-slate-50 text-slate-50 dark:text-slate-900',
  success: 'bg-green-600 dark:bg-green-500 text-white',
  error: 'bg-red-600 dark:bg-red-500 text-white',
  warning: 'bg-amber-600 dark:bg-amber-500 text-white',
}

export function Snackbar({
  message,
  action,
  dismissable = true,
  onDismiss,
  duration = 5000,
  variant = 'default',
}: SnackbarProps) {
  const [visible, setVisible] = React.useState(false)

  React.useEffect(() => {
    // Trigger animation
    requestAnimationFrame(() => setVisible(true))

    if (duration > 0) {
      const timeout = setTimeout(() => {
        handleClose()
      }, duration)
      return () => clearTimeout(timeout)
    }
  }, [duration])

  const handleClose = () => {
    setVisible(false)
    setTimeout(() => onDismiss?.(''), 200) // Wait for exit animation
  }

  const handleAction = () => {
    action?.onClick()
    handleClose()
  }

  return (
    <div
      className={cn(
        'flex items-center gap-3 px-4 py-3 rounded-lg shadow-lg',
        'transition-all duration-200 ease-in-out',
        'transform translate-y-0 opacity-100',
        snackbarVariants[variant],
        !visible && 'translate-y-4 opacity-0'
      )}
    >
      <p className="flex-1 text-sm font-medium">{message}</p>

      {action && (
        <Button
          variant="ghost"
          size="sm"
          onClick={handleAction}
          className="h-7 text-inherit hover:bg-inherit/10"
        >
          {action.label}
        </Button>
      )}

      {dismissable && (
        <button
          onClick={handleClose}
          className="flex-shrink-0 rounded-sm opacity-70 transition-opacity hover:opacity-100"
        >
          <X className="h-4 w-4" />
        </button>
      )}
    </div>
  )
}

/**
 * Snackbar Container
 */
export interface SnackbarContainerProps {
  snackbars: SnackbarProps[]
  onDismiss: (id: string) => void
  maxVisible?: number
}

export function SnackbarContainer({
  snackbars,
  onDismiss,
  maxVisible = 3,
}: SnackbarContainerProps) {
  if (snackbars.length === 0) return null

  const visible = snackbars.slice(0, maxVisible)

  return (
    <div className="fixed bottom-4 left-4 right-4 sm:left-auto sm:right-4 sm:w-96 z-50 flex flex-col gap-2">
      {visible.map((snackbar) => (
        <Snackbar key={snackbar.id} {...snackbar} onDismiss={onDismiss} />
      ))}
    </div>
  )
}

/**
 * Snackbar Context & Hook
 */
interface SnackbarContextValue {
  show: (props: Omit<SnackbarProps, 'id' | 'onDismiss'>) => string
  dismiss: (id: string) => void
  dismissAll: () => void
}

const SnackbarContext = React.createContext<SnackbarContextValue | undefined>(undefined)

export function SnackbarProvider({ children }: { children: React.ReactNode }) {
  const [snackbars, setSnackbars] = React.useState<SnackbarProps[]>([])
  const idCounter = React.useRef(0)

  const show = React.useCallback((props: Omit<SnackbarProps, 'id' | 'onDismiss'>) => {
    const id = `snackbar-${idCounter.current++}`
    const newSnackbar: SnackbarProps = {
      ...props,
      id,
      onDismiss: (dismissId) => {
        setSnackbars((prev) => prev.filter((s) => s.id !== dismissId))
      },
    }
    setSnackbars((prev) => [...prev, newSnackbar])
    return id
  }, [])

  const dismiss = React.useCallback((id: string) => {
    setSnackbars((prev) => prev.filter((s) => s.id !== id))
  }, [])

  const dismissAll = React.useCallback(() => {
    setSnackbars([])
  }, [])

  return (
    <SnackbarContext.Provider value={{ show, dismiss, dismissAll }}>
      {children}
      <SnackbarContainer snackbars={snackbars} onDismiss={dismiss} />
    </SnackbarContext.Provider>
  )
}

export function useSnackbar() {
  const context = React.useContext(SnackbarContext)
  if (!context) {
    throw new Error('useSnackbar must be used within a SnackbarProvider')
  }
  return context
}
