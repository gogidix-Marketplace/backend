import * as React from 'react'
import { cn } from '@shared/utils/cn'
import { useFormField } from './form-field'

/**
 * FormInput - Input with React Hook Form integration
 */

export interface FormInputProps extends React.InputHTMLAttributes<HTMLInputElement> {
  error?: string
}

const FormInput = React.forwardRef<HTMLInputElement, FormInputProps>(
  ({ className, type, error, ...props }, ref) => {
    return (
      <input
        type={type}
        className={cn(
          'flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background',
          'file:border-0 file:bg-transparent file:text-sm file:font-medium',
          'placeholder:text-muted-foreground',
          'focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2',
          'disabled:cursor-not-allowed disabled:opacity-50',
          error && 'border-destructive focus-visible:ring-destructive',
          className
        )}
        ref={ref}
        {...props}
      />
    )
  }
)
FormInput.displayName = 'FormInput'

export { FormInput }

/**
 * FormTextarea - Textarea with React Hook Form integration
 */
export interface FormTextareaProps extends React.TextareaHTMLAttributes<HTMLTextAreaElement> {
  error?: string
  showCharacterCount?: boolean
  maxLength?: number
}

const FormTextarea = React.forwardRef<HTMLTextAreaElement, FormTextareaProps>(
  ({ className, error, showCharacterCount, maxLength, value, ...props }, ref) => {
    const characterCount = typeof value === 'string' ? value.length : 0

    return (
      <div className="relative">
        <textarea
          className={cn(
            'flex min-h-[80px] w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background',
            'placeholder:text-muted-foreground',
            'focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2',
            'disabled:cursor-not-allowed disabled:opacity-50',
            'resize-vertical',
            error && 'border-destructive focus-visible:ring-destructive',
            showCharacterCount && 'pr-16',
            className
          )}
          ref={ref}
          maxLength={maxLength}
          value={value}
          {...props}
        />
        {showCharacterCount && maxLength && (
          <div className="absolute bottom-2 right-3 text-xs text-muted-foreground">
            {characterCount}/{maxLength}
          </div>
        )}
      </div>
    )
  }
)
FormTextarea.displayName = 'FormTextarea'

export { FormTextarea }
