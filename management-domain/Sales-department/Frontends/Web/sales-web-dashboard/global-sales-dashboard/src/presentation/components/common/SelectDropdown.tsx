// SelectDropdown Component
// Provides dropdown selection functionality

import { useState, useRef, useEffect, type React } from 'react';
import './SelectDropdown.css';

export interface SelectOption {
  value: string;
  label: string;
  disabled?: boolean;
  icon?: React.ReactNode;
  group?: string;
}

export interface SelectDropdownProps {
  options: SelectOption[];
  value?: string | string[];
  onChange?: (value: string | string[]) => void;
  placeholder?: string;
  size?: 'sm' | 'md' | 'lg';
  variant?: 'default' | 'filled' | 'outlined';
  disabled?: boolean;
  multiple?: boolean;
  searchable?: boolean;
  className?: string;
  error?: string;
  helperText?: string;
  label?: string;
  required?: boolean;
}

export function SelectDropdown({
  options,
  value: controlledValue,
  onChange,
  placeholder = 'Select an option',
  size = 'md',
  variant = 'default',
  disabled = false,
  multiple = false,
  searchable = false,
  className = '',
  error,
  helperText,
  label,
  required = false,
}: SelectDropdownProps): React.ReactElement {
  const [isOpen, setIsOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [highlightedIndex, setHighlightedIndex] = useState(-1);
  const containerRef = useRef<HTMLDivElement>(null);
  const inputRef = useRef<HTMLInputElement>(null);
  const optionsRef = useRef<HTMLDivElement>(null);

  const isMulti = multiple;
  const value = controlledValue !== undefined ? controlledValue : isMulti ? [] : '';

  // Get selected option(s)
  const getSelectedOption = () => {
    if (isMulti) {
      return (value as string[]).map(v => options.find(o => o.value === v)).filter(Boolean);
    }
    return options.find(o => o.value === value);
  };

  const selectedOptions = getSelectedOption();

  // Filter options based on search
  const filteredOptions = searchable
    ? options.filter(option =>
        option.label.toLowerCase().includes(searchQuery.toLowerCase())
      )
    : options;

  // Group options
  const groupedOptions = filteredOptions.reduce<Record<string, SelectOption[]>>((acc, option) => {
    const group = option.group || '__ungrouped__';
    if (!acc[group]) acc[group] = [];
    acc[group].push(option);
    return acc;
  }, {});

  // Handle option click
  const handleOptionClick = (option: SelectOption) => {
    if (option.disabled) return;

    if (isMulti) {
      const currentValue = value as string[];
      const newValue = currentValue.includes(option.value)
        ? currentValue.filter(v => v !== option.value)
        : [...currentValue, option.value];
      onChange?.(newValue);
    } else {
      onChange?.(option.value);
      setIsOpen(false);
    }
  };

  // Handle select button click
  const handleSelectClick = () => {
    if (!disabled) {
      setIsOpen(!isOpen);
      setSearchQuery('');
      setHighlightedIndex(-1);
    }
  };

  // Handle clear button
  const handleClear = (e: React.MouseEvent) => {
    e.stopPropagation();
    if (isMulti) {
      onChange?.([]);
    } else {
      onChange?.('');
    }
  };

  // Remove selected option in multi-select
  const handleRemoveOption = (optionValue: string, e: React.MouseEvent) => {
    e.stopPropagation();
    const currentValue = value as string[];
    onChange?.(currentValue.filter(v => v !== optionValue));
  };

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

  // Keyboard navigation
  useEffect(() => {
    if (!isOpen) return;

    const handleKeyDown = (e: KeyboardEvent) => {
      const flatOptions = Object.values(groupedOptions).flat();
      const optionCount = flatOptions.length;

      switch (e.key) {
        case 'ArrowDown':
          e.preventDefault();
          setHighlightedIndex(prev => (prev + 1) % optionCount);
          break;
        case 'ArrowUp':
          e.preventDefault();
          setHighlightedIndex(prev => (prev - 1 + optionCount) % optionCount);
          break;
        case 'Enter':
          e.preventDefault();
          if (highlightedIndex >= 0 && flatOptions[highlightedIndex]) {
            handleOptionClick(flatOptions[highlightedIndex]);
          }
          break;
        case 'Escape':
          e.preventDefault();
          setIsOpen(false);
          break;
      }
    };

    document.addEventListener('keydown', handleKeyDown);
    return () => document.removeEventListener('keydown', handleKeyDown);
  }, [isOpen, highlightedIndex, groupedOptions]);

  // Scroll highlighted option into view
  useEffect(() => {
    if (highlightedIndex >= 0 && optionsRef.current) {
      const optionElements = optionsRef.current.querySelectorAll('[data-option-index]');
      const highlightedElement = optionElements[highlightedIndex] as HTMLElement;
      highlightedElement?.scrollIntoView({ block: 'nearest' });
    }
  }, [highlightedIndex]);

  // Format display value
  const formatDisplayValue = (): string => {
    if (isMulti) {
      return (selectedOptions as SelectOption[]).map(o => o.label).join(', ');
    }
    return (selectedOptions as SelectOption)?.label || placeholder;
  };

  const hasValue = isMulti
    ? (value as string[]).length > 0
    : value !== '';

  return (
    <div
      className={`select-dropdown ${isOpen ? 'select-dropdown-open' : ''} ${error ? 'select-dropdown-error' : ''} ${className}`}
      ref={containerRef}
    >
      {label && (
        <label className="select-label">
          {label}
          {required && <span className="select-required">*</span>}
        </label>
      )}

      <div
        className={`select-trigger select-trigger-${size} select-trigger-${variant} ${disabled ? 'select-trigger-disabled' : ''}`}
        onClick={handleSelectClick}
      >
        {/* Selected values display */}
        <div className="select-value">
          {isMulti && (selectedOptions as SelectOption[]).length > 0 ? (
            <div className="select-chips">
              {(selectedOptions as SelectOption[]).map(option => (
                <span key={option.value} className="select-chip">
                  {option.icon && <span className="select-chip-icon">{option.icon}</span>}
                  {option.label}
                  <button
                    type="button"
                    className="select-chip-remove"
                    onClick={(e) => handleRemoveOption(option.value, e)}
                  >
                    <svg width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                      <line x1="18" y1="6" x2="6" y2="18" />
                      <line x1="6" y1="6" x2="18" y2="18" />
                    </svg>
                  </button>
                </span>
              ))}
            </div>
          ) : (
            <span className={!hasValue ? 'select-placeholder' : ''}>
              {formatDisplayValue()}
            </span>
          )}
        </div>

        {/* Actions */}
        <div className="select-actions">
          {hasValue && !disabled && (
            <button
              type="button"
              className="select-clear"
              onClick={handleClear}
            >
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <line x1="18" y1="6" x2="6" y2="18" />
                <line x1="6" y1="6" x2="18" y2="18" />
              </svg>
            </button>
          )}
          <svg className={`select-chevron ${isOpen ? 'select-chevron-open' : ''}`} width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
            <polyline points="6 9 12 15 18 9" />
          </svg>
        </div>
      </div>

      {/* Dropdown */}
      {isOpen && (
        <div className="select-dropdown-menu" ref={optionsRef}>
          {/* Search */}
          {searchable && (
            <div className="select-search">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <circle cx="11" cy="11" r="8" />
                <path d="M21 21l-4.35-4.35" />
              </svg>
              <input
                ref={inputRef}
                type="text"
                className="select-search-input"
                placeholder="Search options..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                onClick={(e) => e.stopPropagation()}
                autoFocus
              />
            </div>
          )}

          {/* Options */}
          <div className="select-options">
            {Object.entries(groupedOptions).map(([group, groupOptions]) => (
              <div key={group}>
                {group !== '__ungrouped__' && (
                  <div className="select-group">{group}</div>
                )}
                {groupOptions.map((option, index) => {
                  const isSelected = isMulti
                    ? (value as string[]).includes(option.value)
                    : value === option.value;
                  const flatIndex = Object.entries(groupedOptions)
                    .slice(0, Object.entries(groupedOptions).findIndex(([g]) => g === group))
                    .reduce((sum, [, opts]) => sum + opts.length, 0) + index;

                  return (
                    <button
                      key={option.value}
                      type="button"
                      data-option-index={flatIndex}
                      className={`select-option ${isSelected ? 'select-option-selected' : ''} ${option.disabled ? 'select-option-disabled' : ''} ${highlightedIndex === flatIndex ? 'select-option-highlighted' : ''}`}
                      onClick={() => handleOptionClick(option)}
                      disabled={option.disabled}
                    >
                      {isMulti && (
                        <span className={`select-checkbox ${isSelected ? 'select-checkbox-checked' : ''}`}>
                          {isSelected && (
                            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                              <polyline points="20 6 9 17 4 12" />
                            </svg>
                          )}
                        </span>
                      )}
                      {option.icon && <span className="select-option-icon">{option.icon}</span>}
                      <span className="select-option-label">{option.label}</span>
                    </button>
                  );
                })}
              </div>
            ))}

            {filteredOptions.length === 0 && (
              <div className="select-empty">
                <svg width="32" height="32" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
                  <circle cx="11" cy="11" r="8" />
                  <path d="M21 21l-4.35-4.35" />
                </svg>
                <span>No options found</span>
              </div>
            )}
          </div>
        </div>
      )}

      {/* Helper text */}
      {(error || helperText) && (
        <div className={`select-helper ${error ? 'select-helper-error' : ''}`}>
          {error || helperText}
        </div>
      )}
    </div>
  );
}

export default SelectDropdown;
