import * as React from 'react'
import { cn } from '@shared/utils/cn'

/**
 * Form Components - Production-grade form elements
 *
 * Features:
 * - Consistent styling
 * - Error states
 * - Disabled states
 * - Focus states
 * - Accessibility
 */

interface FormProps extends React.FormHTMLAttributes<HTMLFormElement> {
  variant?: 'default' | 'compact' | 'spacious'
}

const Form = React.forwardRef<HTMLFormElement, FormProps>(
  ({ className, variant = 'default', ...props }, ref) => {
    return (
      <form
        ref={ref}
        className={cn(
          'space-y-6',
          variant === 'compact' && 'space-y-4',
          variant === 'spacious' && 'space-y-8',
          className
        )}
        {...props}
      />
    )
  }
)
Form.displayName = 'Form'

interface FormItemProps extends React.HTMLAttributes<HTMLDivElement> {
  orientation?: 'vertical' | 'horizontal'
  labelWidth?: string
}

const FormItem = React.forwardRef<HTMLDivElement, FormItemProps>(
  ({ className, orientation = 'vertical', labelWidth, ...props }, ref) => {
    return (
      <div
        ref={ref}
        className={cn(
          'space-y-2',
          orientation === 'horizontal' && 'flex flex-row items-start gap-4 space-y-0',
          className
        )}
        style={
          orientation === 'horizontal' && labelWidth
            ? ({ '--label-width': labelWidth } as React.CSSProperties)
            : undefined
        }
        {...props}
      />
    )
  }
)
FormItem.displayName = 'FormItem'

interface FormLabelProps extends React.LabelHTMLAttributes<HTMLLabelElement> {
  required?: boolean
  tooltip?: string
  error?: boolean
}

const FormLabel = React.forwardRef<HTMLLabelElement, FormLabelProps>(
  ({ className, children, required, tooltip, error, ...props }, ref) => {
    return (
      <label
        ref={ref}
        className={cn(
          'text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70',
          error && 'text-destructive',
          className
        )}
        {...props}
      >
        {children}
        {required && <span className="text-destructive ml-1">*</span>}
      </label>
    )
  }
)
FormLabel.displayName = 'FormLabel'

interface FormControlProps extends React.HTMLAttributes<HTMLDivElement> {
  error?: boolean
}

const FormControl = React.forwardRef<HTMLDivElement, FormControlProps>(
  ({ className, error, ...props }, ref) => {
    return (
      <div
        ref={ref}
        className={cn('relative', error && 'has-[:not(:placeholder-shown)]:border-destructive', className)}
        {...props}
      />
    )
  }
)
FormControl.displayName = 'FormControl'

interface FormDescriptionProps extends React.HTMLAttributes<HTMLParagraphElement> {}

const FormDescription = React.forwardRef<HTMLParagraphElement, FormDescriptionProps>(
  ({ className, ...props }, ref) => {
    return (
      <p
        ref={ref}
        className={cn('text-sm text-muted-foreground', className)}
        {...props}
      />
    )
  }
)
FormDescription.displayName = 'FormDescription'

interface FormMessageProps extends React.HTMLAttributes<HTMLParagraphElement> {
  variant?: 'default' | 'error' | 'success'
}

const FormMessage = React.forwardRef<HTMLParagraphElement, FormMessageProps>(
  ({ className, children, variant = 'default', ...props }, ref) => {
    if (!children) return null

    return (
      <p
        ref={ref}
        className={cn(
          'text-sm',
          variant === 'error' && 'text-destructive',
          variant === 'success' && 'text-green-600 dark:text-green-400',
          variant === 'default' && 'text-muted-foreground',
          className
        )}
        {...props}
      >
        {children}
      </p>
    )
  }
)
FormMessage.displayName = 'FormMessage'

interface FormHelperProps extends React.HTMLAttributes<HTMLDivElement> {
  error?: string
  hint?: string
}

const FormHelper = React.forwardRef<HTMLDivElement, FormHelperProps>(
  ({ className, error, hint, ...props }, ref) => {
    return (
      <div ref={ref} className={cn('space-y-1', className)} {...props}>
        {error && <FormMessage variant="error">{error}</FormMessage>}
        {hint && !error && <FormDescription>{hint}</FormDescription>}
      </div>
    )
  }
)
FormHelper.displayName = 'FormHelper'

interface FormFieldWrapperProps {
  label?: string
  error?: string
  hint?: string
  required?: boolean
  orientation?: 'vertical' | 'horizontal'
  children: React.ReactNode
}

function FormFieldWrapper({
  label,
  error,
  hint,
  required,
  orientation = 'vertical',
  children,
}: FormFieldWrapperProps) {
  return (
    <FormItem orientation={orientation}>
      {label && <FormLabel required={required}>{label}</FormLabel>}
      <FormControl error={!!error}>{children}</FormControl>
      {(error || hint) && <FormHelper error={error} hint={hint} />}
    </FormItem>
  )
}

export {
  Form,
  FormItem,
  FormLabel,
  FormControl,
  FormDescription,
  FormMessage,
  FormHelper,
  FormFieldWrapper,
}
