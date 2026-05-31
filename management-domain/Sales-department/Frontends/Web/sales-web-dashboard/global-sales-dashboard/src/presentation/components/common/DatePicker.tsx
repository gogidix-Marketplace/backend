// DatePicker Component
// Provides date range selection functionality

import { useState, useRef, useEffect, type React } from 'react';
import { formatDate } from '@shared/utils';
import './DatePicker.css';

export type DateRange = {
  start: Date | null;
  end: Date | null;
};

export interface DatePickerProps {
  value?: Date | DateRange;
  onChange?: (value: Date | DateRange) => void;
  placeholder?: string;
  variant?: 'single' | 'range';
  size?: 'sm' | 'md' | 'lg';
  disabled?: boolean;
  minDate?: Date;
  maxDate?: Date;
  className?: string;
}

export interface DatePickerRangeProps extends Omit<DatePickerProps, 'value' | 'onChange'> {
  value?: DateRange;
  onChange?: (value: DateRange) => void;
}

// Quick preset options
const PRESETS = [
  { label: 'Today', range: () => {
    const today = new Date();
    return { start: today, end: today };
  }},
  { label: 'This Week', range: () => {
    const now = new Date();
    const start = new Date(now);
    start.setDate(now.getDate() - now.getDay());
    start.setHours(0, 0, 0, 0);
    const end = new Date(start);
    end.setDate(start.getDate() + 6);
    end.setHours(23, 59, 59, 999);
    return { start, end };
  }},
  { label: 'This Month', range: () => {
    const now = new Date();
    const start = new Date(now.getFullYear(), now.getMonth(), 1);
    const end = new Date(now.getFullYear(), now.getMonth() + 1, 0);
    return { start, end };
  }},
  { label: 'This Quarter', range: () => {
    const now = new Date();
    const quarter = Math.floor(now.getMonth() / 3);
    const start = new Date(now.getFullYear(), quarter * 3, 1);
    const end = new Date(now.getFullYear(), (quarter + 1) * 3, 0);
    return { start, end };
  }},
  { label: 'This Year', range: () => {
    const now = new Date();
    const start = new Date(now.getFullYear(), 0, 1);
    const end = new Date(now.getFullYear(), 11, 31);
    return { start, end };
  }},
];

export function DatePicker({
  value,
  onChange,
  placeholder = 'Select date',
  variant = 'single',
  size = 'md',
  disabled = false,
  minDate,
  maxDate,
  className = '',
}: DatePickerProps): React.ReactElement {
  const [isOpen, setIsOpen] = useState(false);
  const [currentMonth, setCurrentMonth] = useState(new Date());
  const containerRef = useRef<HTMLDivElement>(null);

  const isRange = variant === 'range';
  const dateRange = isRange ? (value as DateRange) : null;
  const singleDate = !isRange ? (value as Date) : null;

  // Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (containerRef.current && !containerRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  // Navigate to previous month
  const prevMonth = () => {
    setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() - 1));
  };

  // Navigate to next month
  const nextMonth = () => {
    setCurrentMonth(new Date(currentMonth.getFullYear(), currentMonth.getMonth() + 1));
  };

  // Check if date is disabled
  const isDateDisabled = (date: Date): boolean => {
    if (disabled) return true;
    if (minDate && date < minDate) return true;
    if (maxDate && date > maxDate) return true;
    return false;
  };

  // Check if date is selected
  const isSelected = (date: Date): boolean => {
    if (isRange && dateRange) {
      return (!dateRange.start || date >= dateRange.start) &&
             (!dateRange.end || date <= dateRange.end);
    }
    return singleDate ? date.toDateString() === singleDate.toDateString() : false;
  };

  // Check if date is in range
  const isInRange = (date: Date): boolean => {
    if (!isRange || !dateRange || !dateRange.start || !dateRange.end) return false;
    return date >= dateRange.start && date <= dateRange.end;
  };

  // Handle date click
  const handleDateClick = (date: Date) => {
    if (isDateDisabled(date)) return;

    if (isRange) {
      const newRange = { ...dateRange };

      if (!dateRange.start || (dateRange.start && dateRange.end)) {
        // Start new range
        newRange.start = date;
        newRange.end = null;
      } else if (date < dateRange.start) {
        // Clicked before start date
        newRange.start = date;
      } else {
        // Set end date
        newRange.end = date;
      }

      onChange?.(newRange);

      // Auto-close when range is complete
      if (newRange.start && newRange.end) {
        setIsOpen(false);
      }
    } else {
      onChange?.(date);
      setIsOpen(false);
    }
  };

  // Handle preset click
  const handlePresetClick = (preset: typeof PRESETS[0]) => {
    const range = preset.range();
    onChange?.(isRange ? range : range.start);
    setIsOpen(false);
  };

  // Generate calendar days
  const generateCalendarDays = () => {
    const year = currentMonth.getFullYear();
    const month = currentMonth.getMonth();

    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const startDay = firstDay.getDay();
    const totalDays = lastDay.getDate();

    const days = [];

    // Empty cells for days before first day of month
    for (let i = 0; i < startDay; i++) {
      days.push(null);
    }

    // Days of the month
    for (let day = 1; day <= totalDays; day++) {
      days.push(new Date(year, month, day));
    }

    return days;
  };

  const days = generateCalendarDays();

  // Format display value
  const formatDisplayValue = (): string => {
    if (isRange) {
      if (!dateRange?.start) return placeholder;
      if (!dateRange?.end) return `${formatDate(dateRange.start)} - End`;
      return `${formatDate(dateRange.start)} - ${formatDate(dateRange.end)}`;
    }
    return singleDate ? formatDate(singleDate) : placeholder;
  };

  return (
    <div
      ref={containerRef}
      className={`date-picker date-picker-${size} ${isOpen ? 'date-picker-open' : ''} ${disabled ? 'date-picker-disabled' : ''} ${className}`}
    >
      <button
        type="button"
        className="date-picker-trigger"
        onClick={() => !disabled && setIsOpen(!isOpen)}
        disabled={disabled}
      >
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
          <rect x="3" y="4" width="18" height="18" rx="2" ry="2" />
          <line x1="16" y1="2" x2="16" y2="6" />
          <line x1="8" y1="2" x2="8" y2="6" />
          <line x1="3" y1="10" x2="21" y2="10" />
        </svg>
        <span>{formatDisplayValue()}</span>
        <svg className="date-picker-chevron" width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
          <polyline points="6 9 12 15 18 9" />
        </svg>
      </button>

      {isOpen && (
        <div className="date-picker-dropdown">
          {/* Calendar Header */}
          <div className="date-picker-header">
            <button
              type="button"
              className="date-picker-nav"
              onClick={prevMonth}
            >
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <polyline points="15 18 9 12 15 6" />
              </svg>
            </button>
            <span className="date-picker-title">
              {currentMonth.toLocaleDateString('en-US', { month: 'long', year: 'numeric' })}
            </span>
            <button
              type="button"
              className="date-picker-nav"
              onClick={nextMonth}
            >
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <polyline points="9 18 15 12 9 6" />
              </svg>
            </button>
          </div>

          {/* Weekday Headers */}
          <div className="date-picker-weekdays">
            {['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'].map(day => (
              <div key={day} className="date-picker-weekday">{day}</div>
            ))}
          </div>

          {/* Calendar Days */}
          <div className="date-picker-days">
            {days.map((date, index) => (
              <button
                key={index}
                type="button"
                className={`date-picker-day ${
                  date ? 'date-picker-day-active' : 'date-picker-day-empty'
                } ${
                  date && isSelected(date) ? 'date-picker-day-selected' : ''
                } ${
                  date && isInRange(date) ? 'date-picker-day-in-range' : ''
                } ${
                  date && isDateDisabled(date) ? 'date-picker-day-disabled' : ''
                }`}
                onClick={() => date && handleDateClick(date)}
                disabled={!date || isDateDisabled(date)}
              >
                {date?.getDate()}
              </button>
            ))}
          </div>

          {/* Quick presets */}
          {isRange && (
            <div className="date-picker-presets">
              <span className="presets-label">Quick Select:</span>
              {PRESETS.map(preset => (
                <button
                  key={preset.label}
                  type="button"
                  className="preset-button"
                  onClick={() => handlePresetClick(preset)}
                >
                  {preset.label}
                </button>
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default DatePicker;
