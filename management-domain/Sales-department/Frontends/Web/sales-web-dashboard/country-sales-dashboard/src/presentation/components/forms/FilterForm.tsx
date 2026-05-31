// FilterForm Component
// Advanced filtering form

import React, { useState } from 'react';
import { DatePicker, SelectDropdown } from '../common';
import type { DateRange } from '../common';

export interface FilterFieldConfig {
  id: string;
  label: string;
  type: 'text' | 'select' | 'multiselect' | 'date' | 'number';
  options?: Array<{ value: string; label: string }>;
  placeholder?: string;
}

export interface FilterFormProps {
  fields: FilterFieldConfig[];
  values: Record<string, any>;
  onChange: (values: Record<string, any>) => void;
  onClear: () => void;
  onApply: () => void;
  isLoading?: boolean;
  className?: string;
}

export const FilterForm: React.FC<FilterFormProps> = ({
  fields,
  values,
  onChange,
  onClear,
  onApply,
  isLoading = false,
  className = '',
}) => {
  const [localValues, setLocalValues] = useState<Record<string, any>>(values);

  const handleFieldChange = (fieldId: string, value: any) => {
    const newValues = { ...localValues, [fieldId]: value };
    setLocalValues(newValues);
    onChange(newValues);
  };

  const handleClear = () => {
    const clearedValues = fields.reduce((acc, field) => {
      acc[field.id] = field.type === 'multiselect' ? [] : '';
      return acc;
    }, {} as Record<string, any>);
    setLocalValues(clearedValues);
    onChange(clearedValues);
    onClear();
  };

  const activeFiltersCount = Object.entries(localValues).filter(([key, value]) => {
    if (Array.isArray(value)) return value.length > 0;
    return value !== '' && value !== null && value !== undefined;
  }).length;

  return (
    <div className={`filter-form ${className}`}>
      <div className="filter-form-header">
        <h4>Advanced Filters</h4>
        {activeFiltersCount > 0 && (
          <span className="filter-count">{activeFiltersCount} active</span>
        )}
      </div>

      <div className="filter-form-fields">
        {fields.map(field => (
          <div key={field.id} className="filter-form-field">
            <label htmlFor={field.id}>{field.label}</label>

            {field.type === 'text' && (
              <input
                type="text"
                id={field.id}
                value={localValues[field.id] || ''}
                onChange={(e) => handleFieldChange(field.id, e.target.value)}
                placeholder={field.placeholder}
                className="form-input"
                disabled={isLoading}
              />
            )}

            {field.type === 'select' && field.options && (
              <SelectDropdown
                options={field.options}
                value={localValues[field.id] || ''}
                onChange={(value) => handleFieldChange(field.id, value)}
                placeholder={field.placeholder}
                disabled={isLoading}
              />
            )}

            {field.type === 'multiselect' && field.options && (
              <MultiSelect
                options={field.options}
                value={localValues[field.id] || []}
                onChange={(value) => handleFieldChange(field.id, value)}
                placeholder={field.placeholder}
                disabled={isLoading}
              />
            )}

            {field.type === 'date' && (
              <DatePicker
                value={localValues[field.id]}
                onChange={(range) => handleFieldChange(field.id, range)}
                disabled={isLoading}
              />
            )}

            {field.type === 'number' && (
              <input
                type="number"
                id={field.id}
                value={localValues[field.id] || ''}
                onChange={(e) => handleFieldChange(field.id, parseFloat(e.target.value) || 0)}
                placeholder={field.placeholder}
                className="form-input"
                disabled={isLoading}
              />
            )}
          </div>
        ))}
      </div>

      <div className="filter-form-actions">
        <button
          type="button"
          className="btn btn-secondary"
          onClick={handleClear}
          disabled={isLoading || activeFiltersCount === 0}
        >
          Clear All
        </button>
        <button
          type="button"
          className="btn btn-primary"
          onClick={onApply}
          disabled={isLoading}
        >
          Apply Filters
        </button>
      </div>
    </div>
  );
};

// Simple multi-select component for internal use
interface MultiSelectProps {
  options: Array<{ value: string; label: string }>;
  value: string[];
  onChange: (value: string[]) => void;
  placeholder?: string;
  disabled?: boolean;
}

const MultiSelect: React.FC<MultiSelectProps> = ({
  options,
  value,
  onChange,
  placeholder = 'Select...',
  disabled = false,
}) => {
  const [isOpen, setIsOpen] = useState(false);

  const toggleOption = (optionValue: string) => {
    const newValue = value.includes(optionValue)
      ? value.filter(v => v !== optionValue)
      : [...value, optionValue];
    onChange(newValue);
  };

  const selectedLabels = options
    .filter(opt => value.includes(opt.value))
    .map(opt => opt.label);

  return (
    <div className={`multi-select ${isOpen ? 'multi-select-open' : ''}`}>
      <button
        type="button"
        className="multi-select-trigger"
        onClick={() => !disabled && setIsOpen(!isOpen)}
        disabled={disabled}
      >
        <span className="multi-select-value">
          {selectedLabels.length > 0
            ? `${selectedLabels.length} selected`
            : placeholder}
        </span>
        <span className="multi-select-chevron">
          {isOpen ? '\u25B2' : '\u25BC'}
        </span>
      </button>

      {isOpen && (
        <div className="multi-select-dropdown">
          {options.map(option => (
            <label key={option.value} className="multi-select-option">
              <input
                type="checkbox"
                checked={value.includes(option.value)}
                onChange={() => toggleOption(option.value)}
                disabled={disabled}
              />
              <span className="multi-select-label">{option.label}</span>
            </label>
          ))}
        </div>
      )}
    </div>
  );
};

export default FilterForm;
