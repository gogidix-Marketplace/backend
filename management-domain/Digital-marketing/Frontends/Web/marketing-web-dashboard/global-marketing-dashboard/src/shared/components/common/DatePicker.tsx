// DatePicker Component
// Date range and single date picker

import React, { useState, useRef, useEffect } from 'react';
import { format, isValid, parse } from 'date-fns';
import './DatePicker.css';

export interface DatePickerProps {
  value?: Date;
  onChange: (date: Date | undefined) => void;
  placeholder?: string;
  disabled?: boolean;
  minDate?: Date;
  maxDate?: Date;
  format?: string;
  size?: 'sm' | 'md' | 'lg';
  className?: string;
}

export const DatePicker: React.FC<DatePickerProps> = ({
  value,
  onChange,
  placeholder = 'Select date',
  disabled = false,
  minDate,
  maxDate,
  formatStr = 'MMM d, yyyy',
  size = 'md',
  className = '',
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [inputValue, setInputValue] = useState(
    value ? format(value, formatStr) : ''
  );
  const [currentMonth, setCurrentMonth] = useState(value || new Date());
  const pickerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    setInputValue(value ? format(value, formatStr) : '');
  }, [value, formatStr]);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (pickerRef.current && !pickerRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setInputValue(value);

    const parsed = parse(value, formatStr, new Date());
    if (isValid(parsed)) {
      onChange(parsed);
    }
  };

  const handleDateSelect = (date: Date) => {
    if (minDate && date < minDate) return;
    if (maxDate && date > maxDate) return;

    onChange(date);
    setIsOpen(false);
  };

  const getDaysInMonth = (date: Date) => {
    const year = date.getFullYear();
    const month = date.getMonth();
    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);
    const daysInMonth = lastDay.getDate();
    const startDayOfWeek = firstDay.getDay();

    return { daysInMonth, startDayOfWeek };
  };

  const { daysInMonth, startDayOfWeek } = getDaysInMonth(currentMonth);
  const today = new Date();

  const renderDays = () => {
    const days = [];
    const dayNames = ['Su', 'Mo', 'Tu', 'We', 'Th', 'Fr', 'Sa'];

    // Day headers
    days.push(
      dayNames.map((day) => (
        <div key={day} className="datepicker__day-name">
          {day}
        </div>
      ))
    );

    // Empty cells before first day
    for (let i = 0; i < startDayOfWeek; i++) {
      days.push(<div key={`empty-${i}`} className="datepicker__day empty" />);
    }

    // Days of month
    for (let day = 1; day <= daysInMonth; day++) {
      const date = new Date(currentMonth.getFullYear(), currentMonth.getMonth(), day);
      const isSelected = value && date.toDateString() === value.toDateString();
      const isToday = date.toDateString() === today.toDateString();
      const isDisabled = (minDate && date < minDate) || (maxDate && date > maxDate);

      days.push(
        <button
          key={day}
          type="button"
          className={`datepicker__day ${isSelected ? 'datepicker__day--selected' : ''} ${
            isToday ? 'datepicker__day--today' : ''
          } ${isDisabled ? 'datepicker__day--disabled' : ''}`}
          onClick={() => !isDisabled && handleDateSelect(date)}
          disabled={isDisabled}
        >
          {day}
        </button>
      );
    }

    return days;
  };

  const navigateMonth = (direction: 'prev' | 'next') => {
    setCurrentMonth(prev => {
      const newDate = new Date(prev);
      if (direction === 'prev') {
        newDate.setMonth(newDate.getMonth() - 1);
      } else {
        newDate.setMonth(newDate.getMonth() + 1);
      }
      return newDate;
    });
  };

  return (
    <div ref={pickerRef} className={`datepicker ${className}`}>
      <div className={`datepicker__input datepicker__input--${size}`}>
        <input
          type="text"
          value={inputValue}
          onChange={handleInputChange}
          placeholder={placeholder}
          disabled={disabled}
          onFocus={() => !disabled && setIsOpen(true)}
          className="datepicker__field"
        />
        <button
          type="button"
          className="datepicker__toggle"
          onClick={() => !disabled && setIsOpen(!isOpen)}
          disabled={disabled}
        >
          <svg width="16" height="16" viewBox="0 0 16 16" fill="currentColor">
            <path d="M5 3a2 2 0 0 0-2 2v1h10V5a2 2 0 0 0-2-2H5zm8-1a3 3 0 0 1 3 3v9a3 3 0 0 1-3 3H5a3 3 0 0 1-3-3V5a3 3 0 0 1 3-3h8z" />
          </svg>
        </button>
      </div>

      {isOpen && (
        <div className="datepicker__popup">
          <div className="datepicker__header">
            <button
              type="button"
              className="datepicker__nav"
              onClick={() => navigateMonth('prev')}
            >
              ‹
            </button>
            <span className="datepicker__month-year">
              {format(currentMonth, 'MMMM yyyy')}
            </span>
            <button
              type="button"
              className="datepicker__nav"
              onClick={() => navigateMonth('next')}
            >
              ›
            </button>
          </div>
          <div className="datepicker__days">{renderDays()}</div>
          <div className="datepicker__footer">
            <button
              type="button"
              className="datepicker__today-btn"
              onClick={() => handleDateSelect(new Date())}
            >
              Today
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default DatePicker;
