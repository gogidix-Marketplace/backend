import { Label } from '@shared/components/ui/label'
import { cn } from '@shared/utils/cn'
import { Slot } from '@radix-ui/react-slot'
import { ReactNode } from 'react'

/**
 * FormField - Labeled input wrapper
 *
 * Provides consistent layout for form fields with:
 * - Label
 * - Helper text
 * - Error message
 * - Required indicator
 */

export interface FormFieldProps {
  label?: string
  error?: string
  helperText?: string
  required?: boolean
  className?: string
  children: ReactNode
}

export function FormField({
  label,
  error,
  helperText,
  required,
  className,
  children,
}: FormFieldProps) {
  return (
    <div className={cn('space-y-2', className)}>
      {label && (
        <Label className={cn(error && 'text-destructive')}>
          {label}
          {required && <span className="text-destructive ml-1">*</span>}
        </Label>
      )}
      {children}
      {error && <p className="text-sm text-destructive">{error}</p>}
      {!error && helperText && <p className="text-sm text-muted-foreground">{helperText}</p>}
    </div>
  )
}

/**
 * FormRoot - Form wrapper with validation
 */
import type { ControllerProps, FieldPath, FieldValues } from 'react-hook-form'
import { useFormContext, Controller } from 'react-hook-form'

const Form = useFormContext

interface FormFieldContextValue<
  TFieldValues extends FieldValues = FieldValues,
  TName extends FieldPath<TFieldValues> = FieldPath<TFieldValues>
> {
  name: TName
}

const FormFieldContext = React.createContext<FormFieldContextValue>(
  {} as FormFieldContextValue
)

const FormField = <
  TFieldValues extends FieldValues = FieldValues,
  TName extends FieldPath<TFieldValues> = FieldPath<TFieldValues>
>({
  ...props
}: ControllerProps<TFieldValues, TName>) => {
  return (
    <FormFieldContext.Provider value={{ name: props.name }}>
      <Controller {...props} />
    </FormFieldContext.Provider>
  )
}

export { Form, FormField, useFormContext }
export type { ControllerProps, FieldPath, FieldValues }
