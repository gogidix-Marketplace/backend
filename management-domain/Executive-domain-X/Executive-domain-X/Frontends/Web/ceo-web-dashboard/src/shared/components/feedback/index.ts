/**
 * Feedback Components
 *
 * Production-grade feedback components for alerts, notifications,
 * confirmations, and loading states
 */

export {
  Alert,
  AlertTitle,
  AlertDescription,
  InlineAlert,
} from './alert'
export type { AlertProps, InlineAlertProps } from './alert'

export {
  Toast,
  ToastContainer,
  ToastProvider,
  useToast,
} from './toast'
export type { ToastProps, ToastContainerProps, ToastVariant } from './toast'

export {
  Snackbar,
  SnackbarContainer,
  SnackbarProvider,
  useSnackbar,
} from './snackbar'
export type { SnackbarProps, SnackbarContainerProps } from './snackbar'

export {
  ConfirmationDialog,
  useConfirmation,
  AlertDialog,
} from './confirmation-dialog'
export type { ConfirmationDialogProps, AlertDialogProps, ConfirmationVariant, UseConfirmationOptions } from './confirmation-dialog'

export {
  ProgressBackdrop,
  InlineProgress,
  Skeleton,
  SkeletonLoader,
} from './progress-backdrop'
export type { ProgressBackdropProps, InlineProgressProps, SkeletonProps, SkeletonLoaderProps } from './progress-backdrop'
