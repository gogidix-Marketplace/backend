import * as React from 'react'
import { format, isValid, parse } from 'date-fns'
import { Calendar as CalendarIcon, ChevronLeft, ChevronRight } from 'lucide-react'
import { cn } from '@lib/utils'
import { Button } from './button'
import { Popover, PopoverContent, PopoverTrigger } from './popover'

export interface DatePickerProps {
  value?: Date | null
  onChange?: (date: Date | null) => void
  placeholder?: string
  className?: string
  disabled?: boolean
  minDate?: Date
  maxDate?: Date
  format?: string
}

export function DatePicker({
  value,
  onChange,
  placeholder = 'Select date',
  className,
  disabled = false,
  minDate,
  maxDate,
  format: formatStr = 'MMM dd, yyyy',
}: DatePickerProps) {
  const [isOpen, setIsOpen] = React.useState(false)
  const [currentMonth, setCurrentMonth] = React.useState(new Date())

  const handleDateSelect = (date: Date) => {
    // Check min/max constraints
    if (minDate && date < minDate) return
    if (maxDate && date > maxDate) return

    onChange?.(date)
    setIsOpen(false)
  }

  const handlePreviousMonth = () => {
    setCurrentMonth(
      new Date(currentMonth.getFullYear(), currentMonth.getMonth() - 1, 1)
    )
  }

  const handleNextMonth = () => {
    setCurrentMonth(
      new Date(currentMonth.getFullYear(), currentMonth.getMonth() + 1, 1)
    )
  }

  const renderCalendar = () => {
    const year = currentMonth.getFullYear()
    const month = currentMonth.getMonth()
    const firstDay = new Date(year, month, 1)
    const lastDay = new Date(year, month + 1, 0)
    const daysInMonth = lastDay.getDate()
    const startingDayOfWeek = firstDay.getDay() // 0 = Sunday

    const days: Date[] = []

    // Add empty cells for days before the first of the month
    for (let i = 0; i < startingDayOfWeek; i++) {
      days.push(new Date(year, month, 1 - (startingDayOfWeek - i)))
    }

    // Add all days of the month
    for (let day = 1; day <= daysInMonth; day++) {
      days.push(new Date(year, month, day))
    }

    // Add cells for the next month to fill the grid (6 rows)
    const totalCells = 42 // 6 rows x 7 days
    for (let i = days.length; i < totalCells; i++) {
      days.push(new Date(year, month + 1, i - daysInMonth + 1))
    }

    return days
  }

  const isSelected = (date: Date) => {
    return value && isValid(date) && isValid(value)
      ? date.toDateString() === value.toDateString()
      : false
  }

  const isCurrentMonth = (date: Date) => {
    return date.getMonth() === currentMonth.getMonth()
  }

  const isDisabled = (date: Date) => {
    if (disabled) return true
    if (minDate && date < minDate) return true
    if (maxDate && date > maxDate) return true
    return !isCurrentMonth(date)
  }

  const weekDays = ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa']

  return (
    <Popover open={isOpen} onOpenChange={setIsOpen}>
      <PopoverTrigger asChild>
        <button
          type="button"
          disabled={disabled}
          className={cn(
            'flex h-10 w-full items-center justify-between rounded-md border border-gray-300 bg-white px-3 py-2 text-sm placeholder:text-gray-400 focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent disabled:cursor-not-allowed disabled:opacity-50',
            className
          )}
        >
          <span className={cn(!value && 'text-gray-400')}>
            {value && isValid(value) ? format(value, formatStr) : placeholder}
          </span>
          <CalendarIcon className="h-4 w-4 text-gray-400" />
        </button>
      </PopoverTrigger>
      <PopoverContent className="w-auto p-0" align="start">
        <div className="p-3">
          {/* Header */}
          <div className="flex items-center justify-between mb-4">
            <button
              type="button"
              onClick={handlePreviousMonth}
              className="p-1 hover:bg-gray-100 rounded"
            >
              <ChevronLeft className="h-4 w-4" />
            </button>
            <div className="font-medium">
              {format(currentMonth, 'MMMM yyyy')}
            </div>
            <button
              type="button"
              onClick={handleNextMonth}
              className="p-1 hover:bg-gray-100 rounded"
            >
              <ChevronRight className="h-4 w-4" />
            </button>
          </div>

          {/* Week day headers */}
          <div className="grid grid-cols-7 gap-1 mb-2">
            {weekDays.map((day) => (
              <div
                key={day}
                className="text-center text-xs font-medium text-gray-500 py-1"
              >
                {day}
              </div>
            ))}
          </div>

          {/* Calendar grid */}
          <div className="grid grid-cols-7 gap-1">
            {renderCalendar().map((date, index) => (
              <button
                key={index}
                type="button"
                onClick={() => handleDateSelect(date)}
                disabled={isDisabled(date)}
                className={cn(
                  'h-8 w-8 text-sm rounded-full flex items-center justify-center',
                  'hover:bg-gray-100 disabled:opacity-30 disabled:cursor-not-allowed',
                  isSelected(date) && 'bg-primary-600 text-white hover:bg-primary-700',
                  !isSelected(date) && isCurrentMonth(date) && 'text-gray-900',
                  !isSelected(date) && !isCurrentMonth(date) && 'text-gray-400'
                )}
              >
                {format(date, 'd')}
              </button>
            ))}
          </div>
        </div>
      </PopoverContent>
    </Popover>
  )
}

// DateRangePicker component
export interface DateRangePickerProps {
  value?: { from?: Date; to?: Date }
  onChange?: (range: { from?: Date; to?: Date } | undefined) => void
  placeholder?: string
  className?: string
  disabled?: boolean
  minDate?: Date
  maxDate?: Date
  format?: string
}

export function DateRangePicker({
  value,
  onChange,
  placeholder = 'Select date range',
  className,
  disabled = false,
  minDate,
  maxDate,
  format: formatStr = 'MMM dd, yyyy',
}: DateRangePickerProps) {
  const [isOpen, setIsOpen] = React.useState(false)
  const [currentMonth, setCurrentMonth] = React.useState(new Date())

  const handleDateSelect = (date: Date) => {
    // Check min/max constraints
    if (minDate && date < minDate) return
    if (maxDate && date > maxDate) return

    if (!value?.from || (value?.from && value?.to)) {
      // Start new range
      onChange?.({ from: date, to: undefined })
    } else if (date < value.from) {
      // Selected date is before start date, reset range
      onChange?.({ from: date, to: undefined })
    } else {
      // Complete range
      onChange?.({ ...value, to: date })
    }
  }

  const handlePreviousMonth = () => {
    setCurrentMonth(
      new Date(currentMonth.getFullYear(), currentMonth.getMonth() - 1, 1)
    )
  }

  const handleNextMonth = () => {
    setCurrentMonth(
      new Date(currentMonth.getFullYear(), currentMonth.getMonth() + 1, 1)
    )
  }

  const renderCalendar = () => {
    const year = currentMonth.getFullYear()
    const month = currentMonth.getMonth()
    const firstDay = new Date(year, month, 1)
    const lastDay = new Date(year, month + 1, 0)
    const daysInMonth = lastDay.getDate()
    const startingDayOfWeek = firstDay.getDay()

    const days: Date[] = []

    for (let i = 0; i < startingDayOfWeek; i++) {
      days.push(new Date(year, month, 1 - (startingDayOfWeek - i)))
    }

    for (let day = 1; day <= daysInMonth; day++) {
      days.push(new Date(year, month, day))
    }

    const totalCells = 42
    for (let i = days.length; i < totalCells; i++) {
      days.push(new Date(year, month + 1, i - daysInMonth + 1))
    }

    return days
  }

  const isInRange = (date: Date) => {
    if (!value?.from || !value?.to) return false
    return date >= value.from && date <= value.to
  }

  const isStartDate = (date: Date) => {
    return value?.from && date.toDateString() === value.from.toDateString()
  }

  const isEndDate = (date: Date) => {
    return value?.to && date.toDateString() === value.to.toDateString()
  }

  const isCurrentMonth = (date: Date) => {
    return date.getMonth() === currentMonth.getMonth()
  }

  const isDisabled = (date: Date) => {
    if (disabled) return true
    if (minDate && date < minDate) return true
    if (maxDate && date > maxDate) return true
    return !isCurrentMonth(date)
  }

  const weekDays = ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa']

  const displayValue = () => {
    if (!value?.from) return placeholder
    if (!value?.to)
      return format(value.from, formatStr)
    return `${format(value.from, formatStr)} - ${format(value.to, formatStr)}`
  }

  return (
    <Popover open={isOpen} onOpenChange={setIsOpen}>
      <PopoverTrigger asChild>
        <button
          type="button"
          disabled={disabled}
          className={cn(
            'flex h-10 w-full items-center justify-between rounded-md border border-gray-300 bg-white px-3 py-2 text-sm placeholder:text-gray-400 focus:outline-none focus:ring-2 focus:ring-primary-500 focus:border-transparent disabled:cursor-not-allowed disabled:opacity-50',
            className
          )}
        >
          <span className={cn(!value?.from && 'text-gray-400')}>
            {displayValue()}
          </span>
          <CalendarIcon className="h-4 w-4 text-gray-400" />
        </button>
      </PopoverTrigger>
      <PopoverContent className="w-auto p-0" align="start">
        <div className="p-3">
          {/* Header */}
          <div className="flex items-center justify-between mb-4">
            <button
              type="button"
              onClick={handlePreviousMonth}
              className="p-1 hover:bg-gray-100 rounded"
            >
              <ChevronLeft className="h-4 w-4" />
            </button>
            <div className="font-medium">
              {format(currentMonth, 'MMMM yyyy')}
            </div>
            <button
              type="button"
              onClick={handleNextMonth}
              className="p-1 hover:bg-gray-100 rounded"
            >
              <ChevronRight className="h-4 w-4" />
            </button>
          </div>

          {/* Week day headers */}
          <div className="grid grid-cols-7 gap-1 mb-2">
            {weekDays.map((day) => (
              <div
                key={day}
                className="text-center text-xs font-medium text-gray-500 py-1"
              >
                {day}
              </div>
            ))}
          </div>

          {/* Calendar grid */}
          <div className="grid grid-cols-7 gap-1">
            {renderCalendar().map((date, index) => (
              <button
                key={index}
                type="button"
                onClick={() => handleDateSelect(date)}
                disabled={isDisabled(date)}
                className={cn(
                  'h-8 w-8 text-sm rounded-full flex items-center justify-center',
                  'hover:bg-gray-100 disabled:opacity-30 disabled:cursor-not-allowed',
                  isInRange(date) && 'bg-primary-100 text-primary-700',
                  (isStartDate(date) || isEndDate(date)) && 'bg-primary-600 text-white hover:bg-primary-700',
                  !isInRange(date) && !isStartDate(date) && !isEndDate(date) && isCurrentMonth(date) && 'text-gray-900',
                  !isInRange(date) && !isStartDate(date) && !isEndDate(date) && !isCurrentMonth(date) && 'text-gray-400'
                )}
              >
                {format(date, 'd')}
              </button>
            ))}
          </div>

          {/* Clear button */}
          {value?.from && (
            <div className="mt-3 pt-3 border-t border-gray-200">
              <button
                type="button"
                onClick={() => onChange?.(undefined)}
                className="text-sm text-gray-600 hover:text-gray-900"
              >
                Clear
              </button>
            </div>
          )}
        </div>
      </PopoverContent>
    </Popover>
  )
}
