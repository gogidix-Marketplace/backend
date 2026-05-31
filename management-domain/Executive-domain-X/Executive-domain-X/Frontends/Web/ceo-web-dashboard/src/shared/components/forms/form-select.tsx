import * as React from 'react'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
  SelectGroup,
  SelectLabel,
  SelectSeparator,
} from '@shared/components/ui/select'
import { cn } from '@shared/utils/cn'

/**
 * FormSelect - Enhanced select with form integration
 */

export interface FormSelectOption {
  value: string
  label: string
  disabled?: boolean
  icon?: React.ReactNode
}

export interface FormSelectGroup {
  label: string
  options: FormSelectOption[]
}

export interface FormSelectProps {
  value?: string
  defaultValue?: string
  onValueChange?: (value: string) => void
  placeholder?: string
  disabled?: boolean
  options: FormSelectOption[] | FormSelectGroup[]
  label?: string
  error?: string
  hint?: string
  required?: boolean
  className?: string
  variant?: 'default' | 'outline' | 'filled'
}

function isGroup(options: FormSelectOption[] | FormSelectGroup[]): options is FormSelectGroup[] {
  return options.length > 0 && 'options' in options[0]
}

export function FormSelect({
  value,
  defaultValue,
  onValueChange,
  placeholder = 'Select...',
  disabled = false,
  options,
  label,
  error,
  hint,
  required,
  className,
  variant = 'default',
}: FormSelectProps) {
  return (
    <div className={cn('space-y-2', className)}>
      {label && (
        <label className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
          {label}
          {required && <span className="text-destructive ml-1">*</span>}
        </label>
      )}
      <Select value={value} defaultValue={defaultValue} onValueChange={onValueChange} disabled={disabled}>
        <SelectTrigger
          className={cn(
            error && 'border-destructive focus-visible:ring-destructive',
            variant === 'filled' && 'bg-muted'
          )}
        >
          <SelectValue placeholder={placeholder} />
        </SelectTrigger>
        <SelectContent>
          {isGroup(options) ? (
            options.map((group) => (
              <React.Fragment key={group.label}>
                <SelectGroup>
                  <SelectLabel>{group.label}</SelectLabel>
                  {group.options.map((option) => (
                    <SelectItem
                      key={option.value}
                      value={option.value}
                      disabled={option.disabled}
                    >
                      {option.icon && (
                        <span className="mr-2 inline-flex h-4 w-4 items-center justify-center">
                          {option.icon}
                        </span>
                      )}
                      {option.label}
                    </SelectItem>
                  ))}
                </SelectGroup>
                <SelectSeparator />
              </React.Fragment>
            ))
          ) : (
            options.map((option) => (
              <SelectItem key={option.value} value={option.value} disabled={option.disabled}>
                {option.icon && (
                  <span className="mr-2 inline-flex h-4 w-4 items-center justify-center">
                    {option.icon}
                  </span>
                )}
                {option.label}
              </SelectItem>
            ))
          )}
        </SelectContent>
      </Select>
      {error && <p className="text-sm text-destructive">{error}</p>}
      {hint && !error && <p className="text-sm text-muted-foreground">{hint}</p>}
    </div>
  )
}

/**
 * FormMultiSelect - Multi-select dropdown
 */
export interface FormMultiSelectProps {
  values: string[]
  onChange: (values: string[]) => void
  options: FormSelectOption[]
  placeholder?: string
  disabled?: boolean
  label?: string
  error?: string
  hint?: string
  maxDisplay?: number
  className?: string
}

export function FormMultiSelect({
  values,
  onChange,
  options,
  placeholder = 'Select...',
  disabled = false,
  label,
  error,
  hint,
  maxDisplay = 3,
  className,
}: FormMultiSelectProps) {
  const [open, setOpen] = React.useState(false)

  const selectedOptions = options.filter((opt) => values.includes(opt.value))
  const displayText =
    selectedOptions.length === 0
      ? placeholder
      : selectedOptions.length <= maxDisplay
      ? selectedOptions.map((o) => o.label).join(', ')
      : `${selectedOptions.slice(0, maxDisplay).map((o) => o.label).join(', ')} +${selectedOptions.length - maxDisplay} more`

  const toggleValue = (value: string) => {
    if (values.includes(value)) {
      onChange(values.filter((v) => v !== value))
    } else {
      onChange([...values, value])
    }
  }

  return (
    <div className={cn('space-y-2', className)}>
      {label && (
        <label className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
          {label}
        </label>
      )}
      <div className="relative">
        <button
          type="button"
          onClick={() => setOpen(!open)}
          disabled={disabled}
          className={cn(
            'flex h-10 w-full items-center justify-between rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background placeholder:text-muted-foreground focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2 disabled:cursor-not-allowed disabled:opacity-50',
            error && 'border-destructive focus:ring-destructive',
            open && 'ring-2 ring-ring ring-offset-2'
          )}
        >
          <span className="truncate">{displayText}</span>
          <svg
            className={cn('h-4 w-4 opacity-50 transition-transform', open && 'rotate-180')}
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
          >
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
          </svg>
        </button>

        {open && (
          <div className="absolute z-50 mt-1 max-h-60 w-full overflow-auto rounded-md border bg-popover p-1 shadow-md animate-in fade-in-0 zoom-in-95">
            {options.map((option) => (
              <button
                key={option.value}
                type="button"
                onClick={() => toggleValue(option.value)}
                disabled={option.disabled}
                className={cn(
                  'relative flex w-full cursor-pointer select-none items-center rounded-sm px-2 py-1.5 text-sm outline-none hover:bg-accent hover:text-accent-foreground',
                  values.includes(option.value) && 'bg-accent',
                  option.disabled && 'cursor-not-allowed opacity-50'
                )}
              >
                <span className="mr-2 flex h-4 w-4 items-center justify-center">
                  {values.includes(option.value) && (
                    <svg className="h-3 w-3" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={3} d="M5 13l4 4L19 7" />
                    </svg>
                  )}
                </span>
                {option.icon && <span className="mr-2">{option.icon}</span>}
                {option.label}
              </button>
            ))}
          </div>
        )}
      </div>
      {error && <p className="text-sm text-destructive">{error}</p>}
      {hint && !error && <p className="text-sm text-muted-foreground">{hint}</p>}
    </div>
  )
}
