// Filter Form Component
// Reusable filter form for data tables and lists

import { useState } from 'react';
import { TIME_PERIODS } from '@shared';
import { Button } from '../common/Button';
import './FilterForm.css';

export interface FilterOption {
  label: string;
  value: string;
}

export interface FilterGroup {
  id: string;
  label: string;
  type: 'select' | 'multiselect' | 'date' | 'daterange' | 'search' | 'checkbox';
  options?: FilterOption[];
  placeholder?: string;
}

export interface FilterFormProps {
  groups: FilterGroup[];
  values: Record<string, any>;
  onChange: (values: Record<string, any>) => void;
  onReset?: () => void;
  onApply?: () => void;
  compact?: boolean;
}

export function FilterForm({
  groups,
  values,
  onChange,
  onReset,
  onApply,
  compact = false,
}: FilterFormProps) {
  const [expanded, setExpanded] = useState(!compact);

  const handleChange = (key: string, value: any) => {
    onChange({ ...values, [key]: value });
  };

  const handleMultiSelect = (key: string, optionValue: string) => {
    const current = values[key] || [];
    const newValue = current.includes(optionValue)
      ? current.filter((v: string) => v !== optionValue)
      : [...current, optionValue];
    handleChange(key, newValue);
  };

  const handleReset = () => {
    const resetValues: Record<string, any> = {};
    groups.forEach((group) => {
      resetValues[group.id] = group.type === 'multiselect' ? [] : '';
    });
    onChange(resetValues);
    onReset?.();
  };

  return (
    <div className={`filter-form ${compact ? 'compact' : ''} ${expanded ? 'expanded' : ''}`}>
      <div className="filter-form-header">
        <h3>Filters</h3>
        <button
          className="filter-toggle"
          onClick={() => setExpanded(!expanded)}
          aria-label={expanded ? 'Collapse filters' : 'Expand filters'}
        >
          <svg
            width="20"
            height="20"
            viewBox="0 0 20 20"
            fill="currentColor"
            style={{ transform: expanded ? 'rotate(180deg)' : 'rotate(0deg)' }}
          >
            <path
              fillRule="evenodd"
              d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
              clipRule="evenodd"
            />
          </svg>
        </button>
      </div>

      {expanded && (
        <>
          <div className="filter-form-groups">
            {groups.map((group) => (
              <div key={group.id} className="filter-group">
                <label className="filter-label">{group.label}</label>

                {group.type === 'select' && (
                  <select
                    value={values[group.id] || ''}
                    onChange={(e) => handleChange(group.id, e.target.value)}
                    className="filter-select"
                  >
                    <option value="">{group.placeholder || 'Select...'}</option>
                    {group.options?.map((option) => (
                      <option key={option.value} value={option.value}>
                        {option.label}
                      </option>
                    ))}
                  </select>
                )}

                {group.type === 'multiselect' && (
                  <div className="filter-multiselect">
                    {group.options?.map((option) => {
                      const isSelected = (values[group.id] || []).includes(option.value);
                      return (
                        <button
                          key={option.value}
                          type="button"
                          className={`filter-chip ${isSelected ? 'selected' : ''}`}
                          onClick={() => handleMultiSelect(group.id, option.value)}
                        >
                          {option.label}
                          {isSelected && (
                            <span className="chip-remove">\u2715</span>
                          )}
                        </button>
                      );
                    })}
                  </div>
                )}

                {group.type === 'search' && (
                  <div className="filter-search">
                    <svg
                      width="16"
                      height="16"
                      viewBox="0 0 16 16"
                      fill="currentColor"
                      className="search-icon"
                    >
                      <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z" />
                    </svg>
                    <input
                      type="text"
                      value={values[group.id] || ''}
                      onChange={(e) => handleChange(group.id, e.target.value)}
                      placeholder={group.placeholder || 'Search...'}
                      className="filter-input"
                    />
                    {values[group.id] && (
                      <button
                        className="search-clear"
                        onClick={() => handleChange(group.id, '')}
                        aria-label="Clear search"
                      >
                        \u2715
                      </button>
                    )}
                  </div>
                )}

                {group.type === 'date' && (
                  <input
                    type="date"
                    value={values[group.id] || ''}
                    onChange={(e) => handleChange(group.id, e.target.value)}
                    className="filter-input"
                  />
                )}

                {group.type === 'daterange' && (
                  <div className="filter-daterange">
                    <input
                      type="date"
                      value={values[`${group.id}_from`] || ''}
                      onChange={(e) => handleChange(`${group.id}_from`, e.target.value)}
                      className="filter-input"
                      placeholder="From"
                    />
                    <span className="date-separator">to</span>
                    <input
                      type="date"
                      value={values[`${group.id}_to`] || ''}
                      onChange={(e) => handleChange(`${group.id}_to`, e.target.value)}
                      className="filter-input"
                      placeholder="To"
                    />
                  </div>
                )}

                {group.type === 'checkbox' && (
                  <label className="filter-checkbox">
                    <input
                      type="checkbox"
                      checked={values[group.id] || false}
                      onChange={(e) => handleChange(group.id, e.target.checked)}
                    />
                    <span>{group.placeholder}</span>
                  </label>
                )}
              </div>
            ))}
          </div>

          <div className="filter-form-actions">
            <Button variant="ghost" size="sm" onClick={handleReset}>
              Reset
            </Button>
            <Button variant="primary" size="sm" onClick={onApply}>
              Apply Filters
            </Button>
          </div>
        </>
      )}
    </div>
  );
}

// Preset filter components
export interface PeriodFilterProps {
  value: string;
  onChange: (value: string) => void;
}

export function PeriodFilter({ value, onChange }: PeriodFilterProps) {
  return (
    <div className="period-filter">
      <label className="period-label">Period:</label>
      <select value={value} onChange={(e) => onChange(e.target.value)} className="period-select">
        {TIME_PERIODS.map((period) => (
          <option key={period.value} value={period.value}>
            {period.label}
          </option>
        ))}
      </select>
    </div>
  );
}

export interface CountryFilterProps {
  countries: Array<{ code: string; name: string; flag: string }>;
  selected: string[];
  onChange: (selected: string[]) => void;
}

export function CountryFilter({ countries, selected, onChange }: CountryFilterProps) {
  const toggleCountry = (code: string) => {
    const newSelected = selected.includes(code)
      ? selected.filter((c) => c !== code)
      : [...selected, code];
    onChange(newSelected);
  };

  return (
    <div className="country-filter">
      <label className="country-label">Countries:</label>
      <div className="country-chips">
        {countries.map((country) => {
          const isSelected = selected.includes(country.code);
          return (
            <button
              key={country.code}
              type="button"
              className={`country-chip ${isSelected ? 'selected' : ''}`}
              onClick={() => toggleCountry(country.code)}
            >
              <span className="country-flag">{country.flag}</span>
              <span className="country-name">{country.name}</span>
            </button>
          );
        })}
      </div>
    </div>
  );
}

export interface StatusFilterProps {
  options: Array<{ value: string; label: string; color?: string }>;
  selected: string[];
  onChange: (selected: string[]) => void;
}

export function StatusFilter({ options, selected, onChange }: StatusFilterProps) {
  const toggleStatus = (value: string) => {
    const newSelected = selected.includes(value)
      ? selected.filter((s) => s !== value)
      : [...selected, value];
    onChange(newSelected);
  };

  return (
    <div className="status-filter">
      <label className="status-label">Status:</label>
      <div className="status-chips">
        {options.map((option) => {
          const isSelected = selected.includes(option.value);
          return (
            <button
              key={option.value}
              type="button"
              className={`status-chip ${isSelected ? 'selected' : ''}`}
              style={{
                backgroundColor: isSelected ? option.color : undefined,
                color: isSelected ? 'white' : undefined,
              }}
              onClick={() => toggleStatus(option.value)}
            >
              {option.label}
            </button>
          );
        })}
      </div>
    </div>
  );
}
