// DatePicker Component
// Date range selection for filtering

import React, { useState, useRef, useEffect } from 'react';
import { format, formatDistanceToNow, startOfMonth, endOfMonth, subMonths } from 'date-fns';
import { DATE_FORMATS } from '@shared/constants';

export type DatePreset = 'today' | 'week' | 'month' | 'quarter' | 'year' | 'custom';

export interface DateRange {
  start: Date;
  end: Date;
  preset?: DatePreset;
}

export interface DatePickerProps {
  value?: DateRange;
  onChange?: (range: DateRange) => void;
  placeholder?: string;
  disabled?: boolean;
  showPresets?: boolean;
  className?: string;
  size?: 'sm' | 'md' | 'lg';
}

const PRESETS: Array<{ value: DatePreset; label: string; getRange: () => DateRange }> = [
  {
    value: 'today',
    label: 'Today',
    getRange: () => {
      const now = new Date();
      return { start: new Date(now.setHours(0, 0, 0, 0)), end: new Date(now.setHours(23, 59, 59, 999)) };
    },
  },
  {
    value: 'week',
    label: 'This Week',
    getRange: () => {
      const now = new Date();
      const day = now.getDay();
      const start = new Date(now);
      start.setDate(now.getDate() - day);
      start.setHours(0, 0, 0, 0);
      const end = new Date(start);
      end.setDate(start.getDate() + 6);
      end.setHours(23, 59, 59, 999);
      return { start, end };
    },
  },
  {
    value: 'month',
    label: 'This Month',
    getRange: () => ({
      start: startOfMonth(new Date()),
      end: endOfMonth(new Date()),
    }),
  },
  {
    value: 'quarter',
    label: 'This Quarter',
    getRange: () => {
      const now = new Date();
      const quarter = Math.floor(now.getMonth() / 3);
      return {
        start: new Date(now.getFullYear(), quarter * 3, 1),
        end: new Date(now.getFullYear(), quarter * 3 + 3, 0, 23, 59, 59, 999),
      };
    },
  },
  {
    value: 'year',
    label: 'This Year',
    getRange: () => ({
      start: new Date(new Date().getFullYear(), 0, 1),
      end: new Date(new Date().getFullYear(), 11, 31, 23, 59, 59, 999),
    }),
  },
];

export const DatePicker: React.FC<DatePickerProps> = ({
  value,
  onChange,
  placeholder = 'Select date range',
  disabled = false,
  showPresets = true,
  className = '',
  size = 'md',
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [currentPreset, setCurrentPreset] = useState<DatePreset>('month');
  const [customStart, setCustomStart] = useState<string>('');
  const [customEnd, setCustomEnd] = useState<string>('');
  const containerRef = useRef<HTMLDivElement>(null);

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

  const handlePresetClick = (preset: DatePreset, range: DateRange) => {
    setCurrentPreset(preset);
    onChange?.({ ...range, preset });
    setIsOpen(false);
  };

  const handleCustomApply = () => {
    if (customStart && customEnd) {
      const start = new Date(customStart);
      const end = new Date(customEnd);
      onChange?.({ start, end, preset: 'custom' });
      setIsOpen(false);
    }
  };

  const displayValue = value
    ? `${format(value.start, DATE_FORMATS.SHORT)} - ${format(value.end, DATE_FORMATS.SHORT)}`
    : placeholder;

  return (
    <div
      ref={containerRef}
      className={`date-picker date-picker-${size} ${isOpen ? 'date-picker-open' : ''} ${className}`}
    >
      <button
        type="button"
        className="date-picker-trigger"
        onClick={() => !disabled && setIsOpen(!isOpen)}
        disabled={disabled}
      >
        <span className="date-picker-icon">\uD83D\uDDCC</span>
        <span className="date-picker-value">{displayValue}</span>
        <span className={`date-picker-chevron ${isOpen ? 'chevron-up' : 'chevron-down'}`}>
          {isOpen ? '\u25B2' : '\u25BC'}
        </span>
      </button>

      {isOpen && (
        <div className="date-picker-dropdown">
          {showPresets && (
            <div className="date-picker-presets">
              <div className="date-picker-presets-title">Quick Select</div>
              {PRESETS.map(preset => (
                <button
                  key={preset.value}
                  type="button"
                  className={`date-picker-preset ${currentPreset === preset.value ? 'preset-active' : ''}`}
                  onClick={() => handlePresetClick(preset.value, preset.getRange())}
                >
                  {preset.label}
                </button>
              ))}
            </div>
          )}

          <div className="date-picker-custom">
            <div className="date-picker-custom-title">Custom Range</div>
            <div className="date-picker-inputs">
              <div className="date-picker-input-group">
                <label>From</label>
                <input
                  type="date"
                  value={customStart}
                  onChange={(e) => setCustomStart(e.target.value)}
                  className="date-picker-input"
                />
              </div>
              <div className="date-picker-input-group">
                <label>To</label>
                <input
                  type="date"
                  value={customEnd}
                  onChange={(e) => setCustomEnd(e.target.value)}
                  className="date-picker-input"
                  min={customStart}
                />
              </div>
            </div>
            <button
              type="button"
              className="date-picker-apply"
              onClick={handleCustomApply}
              disabled={!customStart || !customEnd}
            >
              Apply Range
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default DatePicker;
