import * as React from 'react'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
} from '@shared/components/ui/dialog'
import { Button } from '@shared/components/ui/button'
import { cn } from '@shared/utils/cn'
import {
  AlertTriangle,
  Info,
  AlertCircle,
  CheckCircle2,
  LucideIcon,
} from 'lucide-react'

/**
 * ConfirmationDialog - Yes/no confirmation modal
 *
 * Features:
 * - Icon display based on variant
 * - Customizable title and message
 * - Customizable button labels
 * - Danger variant for destructive actions
 */

export type ConfirmationVariant = 'default' | 'danger' | 'warning' | 'info'

export interface ConfirmationDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  title: string
  description: string
  variant?: ConfirmationVariant
  confirmLabel?: string
  cancelLabel?: string
  onConfirm: () => void
  onCancel?: () => void
  isLoading?: boolean
  icon?: LucideIcon
}

const variantConfig = {
  default: {
    icon: null,
    iconBg: 'bg-muted',
    iconColor: 'text-muted-foreground',
    confirmButton: 'default',
  },
  danger: {
    icon: AlertTriangle,
    iconBg: 'bg-red-100 dark:bg-red-900/20',
    iconColor: 'text-red-600 dark:text-red-400',
    confirmButton: 'destructive',
  },
  warning: {
    icon: AlertTriangle,
    iconBg: 'bg-amber-100 dark:bg-amber-900/20',
    iconColor: 'text-amber-600 dark:text-amber-400',
    confirmButton: 'default',
  },
  info: {
    icon: Info,
    iconBg: 'bg-blue-100 dark:bg-blue-900/20',
    iconColor: 'text-blue-600 dark:text-blue-400',
    confirmButton: 'default',
  },
}

export function ConfirmationDialog({
  open,
  onOpenChange,
  title,
  description,
  variant = 'default',
  confirmLabel = 'Confirm',
  cancelLabel = 'Cancel',
  onConfirm,
  onCancel,
  isLoading = false,
  icon: customIcon,
}: ConfirmationDialogProps) {
  const config = variantConfig[variant]
  const Icon = customIcon || config.icon

  const handleConfirm = () => {
    onConfirm()
    if (!isLoading) {
      onOpenChange(false)
    }
  }

  const handleCancel = () => {
    onCancel?.()
    onOpenChange(false)
  }

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <div className="flex items-start gap-4">
            {Icon && (
              <div className={cn('flex h-10 w-10 flex-shrink-0 items-center justify-center rounded-full', config.iconBg)}>
                <Icon className={cn('h-5 w-5', config.iconColor)} />
              </div>
            )}
            <div className="flex-1 space-y-1">
              <DialogTitle>{title}</DialogTitle>
              <DialogDescription>{description}</DialogDescription>
            </div>
          </div>
        </DialogHeader>

        <DialogFooter className="gap-2 sm:gap-0">
          <Button variant="outline" onClick={handleCancel} disabled={isLoading}>
            {cancelLabel}
          </Button>
          <Button
            variant={config.confirmButton as any}
            onClick={handleConfirm}
            disabled={isLoading}
          >
            {isLoading ? 'Processing...' : confirmLabel}
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  )
}

/**
 * Confirmation Dialog Hook
 */
interface UseConfirmationOptions {
  title: string
  description: string
  variant?: ConfirmationVariant
  confirmLabel?: string
  cancelLabel?: string
}

export function useConfirmation() {
  const [state, setState] = React.useState<{
    open: boolean
    options: UseConfirmationOptions & { onConfirm: () => void }
  }>({
    open: false,
    options: {
      title: '',
      description: '',
      onConfirm: () => {},
    },
  })

  const confirm = (options: UseConfirmationOptions & { onConfirm: () => void }) => {
    return new Promise<boolean>((resolve) => {
      setState({
        open: true,
        options: {
          ...options,
          onConfirm: () => {
            options.onConfirm()
            resolve(true)
            setState((prev) => ({ ...prev, open: false }))
          },
        },
      })
    })
  }

  const handleCancel = () => {
    setState((prev) => ({ ...prev, open: false }))
  }

  const ConfirmationDialogComponent = state.open ? (
    <ConfirmationDialog
      open={state.open}
      onOpenChange={(open) => !open && handleCancel()}
      title={state.options.title}
      description={state.options.description}
      variant={state.options.variant}
      confirmLabel={state.options.confirmLabel}
      cancelLabel={state.options.cancelLabel}
      onConfirm={state.options.onConfirm}
      onCancel={handleCancel}
    />
  ) : null

  return { confirm, ConfirmationDialog: ConfirmationDialogComponent }
}

/**
 * Alert Dialog - Simplified variant for messages only
 */
export interface AlertDialogProps {
  open: boolean
  onOpenChange: (open: boolean) => void
  title: string
  description: string
  variant?: 'success' | 'error' | 'warning' | 'info'
  buttonLabel?: string
}

const alertVariantConfig = {
  success: {
    icon: CheckCircle2,
    iconBg: 'bg-green-100 dark:bg-green-900/20',
    iconColor: 'text-green-600 dark:text-green-400',
  },
  error: {
    icon: AlertCircle,
    iconBg: 'bg-red-100 dark:bg-red-900/20',
    iconColor: 'text-red-600 dark:text-red-400',
  },
  warning: {
    icon: AlertTriangle,
    iconBg: 'bg-amber-100 dark:bg-amber-900/20',
    iconColor: 'text-amber-600 dark:text-amber-400',
  },
  info: {
    icon: Info,
    iconBg: 'bg-blue-100 dark:bg-blue-900/20',
    iconColor: 'text-blue-600 dark:text-blue-400',
  },
}

export function AlertDialog({
  open,
  onOpenChange,
  title,
  description,
  variant = 'info',
  buttonLabel = 'OK',
}: AlertDialogProps) {
  const config = alertVariantConfig[variant]
  const Icon = config.icon

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-md">
        <DialogHeader>
          <div className="flex flex-col items-center text-center space-y-4">
            <div className={cn('flex h-16 w-16 items-center justify-center rounded-full', config.iconBg)}>
              <Icon className={cn('h-8 w-8', config.iconColor)} />
            </div>
            <div className="space-y-2">
              <DialogTitle>{title}</DialogTitle>
              <DialogDescription>{description}</DialogDescription>
            </div>
          </div>
        </DialogHeader>

        <div className="flex justify-center">
          <Button onClick={() => onOpenChange(false)}>{buttonLabel}</Button>
        </div>
      </DialogContent>
    </Dialog>
  )
}
