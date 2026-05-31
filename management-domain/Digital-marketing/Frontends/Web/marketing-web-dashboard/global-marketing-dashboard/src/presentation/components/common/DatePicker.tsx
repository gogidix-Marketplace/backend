// DatePicker Component
// Date range picker for filtering

import './DatePicker.css';

interface DatePickerProps {
  label?: string;
  startDate?: Date;
  endDate?: Date;
  onChange?: (start: Date | undefined, end: Date | undefined) => void;
  presets?: Array<{ label: string; days: number }>;
  className?: string;
}

export function DatePicker({
  label,
  startDate,
  endDate,
  onChange,
  presets = [
    { label: 'Last 7 days', days: 7 },
    { label: 'Last 30 days', days: 30 },
    { label: 'Last 90 days', days: 90 },
    { label: 'This year', days: 365 },
  ],
  className = '',
}: DatePickerProps) {
  const handleStartDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const date = e.target.value ? new Date(e.target.value) : undefined;
    onChange?.(date, endDate);
  };

  const handleEndDateChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const date = e.target.value ? new Date(e.target.value) : undefined;
    onChange?.(startDate, date);
  };

  const handlePresetClick = (days: number) => {
    const end = new Date();
    const start = new Date();
    start.setDate(start.getDate() - days);
    onChange?.(start, end);
  };

  const formatDateForInput = (date?: Date) => {
    if (!date) return '';
    return date.toISOString().split('T')[0];
  };

  return (
    <div className={`date-picker ${className}`}>
      {label && <label className="date-picker-label">{label}</label>}
      <div className="date-picker-inputs">
        <div className="date-picker-input-group">
          <span className="date-picker-input-label">From</span>
          <input
            type="date"
            className="date-picker-input"
            value={formatDateForInput(startDate)}
            onChange={handleStartDateChange}
          />
        </div>
        <div className="date-picker-input-group">
          <span className="date-picker-input-label">To</span>
          <input
            type="date"
            className="date-picker-input"
            value={formatDateForInput(endDate)}
            onChange={handleEndDateChange}
          />
        </div>
      </div>
      {presets && presets.length > 0 && (
        <div className="date-picker-presets">
          {presets.map((preset) => (
            <button
              key={preset.label}
              type="button"
              className="date-picker-preset"
              onClick={() => handlePresetClick(preset.days)}
            >
              {preset.label}
            </button>
          ))}
        </div>
      )}
    </div>
  );
}
