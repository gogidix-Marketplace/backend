import * as React from 'react'
import { cn } from '@shared/utils/cn'
import { Button } from '@shared/components/ui/button'
import { Calendar } from '@shared/components/ui/calendar'
import { Popover, PopoverContent, PopoverTrigger } from '@shared/components/ui/popover'
import { format } from 'date-fns'
import { CalendarIcon } from 'lucide-react'

/**
 * FormDatePicker - Date input with calendar
 */

export interface FormDatePickerProps {
  value?: Date
  defaultValue?: Date
  onChange?: (date: Date | undefined) => void
  placeholder?: string
  disabled?: boolean
  min?: Date
  max?: Date
  label?: string
  error?: string
  hint?: string
  required?: boolean
  format?: string
  className?: string
  variant?: 'default' | 'inline'
}

export function FormDatePicker({
  value,
  defaultValue,
  onChange,
  placeholder = 'Pick a date',
  disabled = false,
  min,
  max,
  label,
  error,
  hint,
  required,
  format: formatStr = 'MMM d, yyyy',
  className,
  variant = 'default',
}: FormDatePickerProps) {
  const [selected, setSelected] = React.useState<Date | undefined>(value || defaultValue)
  const [open, setOpen] = React.useState(false)

  const handleSelect = (date: Date | undefined) => {
    setSelected(date)
    onChange?.(date)
    if (variant === 'default') {
      setOpen(false)
    }
  }

  return (
    <div className={cn('space-y-2', className)}>
      {label && (
        <label className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
          {label}
          {required && <span className="text-destructive ml-1">*</span>}
        </label>
      )}

      {variant === 'default' ? (
        <Popover open={open} onOpenChange={setOpen}>
          <PopoverTrigger asChild>
            <Button
              type="button"
              variant="outline"
              className={cn(
                'w-full justify-start text-left font-normal',
                !selected && 'text-muted-foreground',
                error && 'border-destructive text-destructive'
              )}
              disabled={disabled}
            >
              <CalendarIcon className="mr-2 h-4 w-4" />
              {selected ? format(selected, formatStr) : placeholder}
            </Button>
          </PopoverTrigger>
          <PopoverContent className="w-auto p-0" align="start">
            <Calendar
              mode="single"
              selected={selected}
              onSelect={handleSelect}
              disabled={(date) =>
                disabled ||
                (min && date < min) ||
                (max && date > max)
              }
              initialFocus
            />
          </PopoverContent>
        </Popover>
      ) : (
        <div className="rounded-md border p-4">
          <Calendar
            mode="single"
            selected={selected}
            onSelect={handleSelect}
            disabled={(date) =>
              disabled ||
              (min && date < min) ||
              (max && date > max)
            }
            initialFocus
          />
        </div>
      )}

      {error && <p className="text-sm text-destructive">{error}</p>}
      {hint && !error && <p className="text-sm text-muted-foreground">{hint}</p>}
    </div>
  )
}

/**
 * FormDateRangePicker - Date range selection
 */
export interface FormDateRangePickerProps {
  value?: { from: Date; to?: Date }
  defaultValue?: { from: Date; to?: Date }
  onChange?: (range: { from: Date; to?: Date } | undefined) => void
  placeholder?: string
  disabled?: boolean
  label?: string
  error?: string
  hint?: string
  required?: boolean
  format?: string
  className?: string
  numberOfMonths?: number
}

export function FormDateRangePicker({
  value,
  defaultValue,
  onChange,
  placeholder = 'Pick a date range',
  disabled = false,
  label,
  error,
  hint,
  required,
  format: formatStr = 'MMM d, yyyy',
  className,
  numberOfMonths = 2,
}: FormDateRangePickerProps) {
  const [selected, setSelected] = React.useState(value || defaultValue)
  const [open, setOpen] = React.useState(false)

  const handleSelect = (range: { from: Date; to?: Date } | undefined) => {
    setSelected(range)
    onChange?.(range)
    if (range?.to) {
      setOpen(false)
    }
  }

  const displayText = selected
    ? selected.to
      ? `${format(selected.from, formatStr)} - ${format(selected.to, formatStr)}`
      : format(selected.from, formatStr)
    : placeholder

  return (
    <div className={cn('space-y-2', className)}>
      {label && (
        <label className="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
          {label}
          {required && <span className="text-destructive ml-1">*</span>}
        </label>
      )}

      <Popover open={open} onOpenChange={setOpen}>
        <PopoverTrigger asChild>
          <Button
            type="button"
            variant="outline"
            className={cn(
              'w-full justify-start text-left font-normal',
              !selected && 'text-muted-foreground',
              error && 'border-destructive text-destructive'
            )}
            disabled={disabled}
          >
            <CalendarIcon className="mr-2 h-4 w-4" />
            {displayText}
          </Button>
        </PopoverTrigger>
        <PopoverContent className="w-auto p-0" align="start">
          <Calendar
            mode="range"
            selected={selected}
            onSelect={handleSelect}
            numberOfMonths={numberOfMonths}
            disabled={disabled}
            initialFocus
          />
        </PopoverContent>
      </Popover>

      {error && <p className="text-sm text-destructive">{error}</p>}
      {hint && !error && <p className="text-sm text-muted-foreground">{hint}</p>}
    </div>
  )
}
