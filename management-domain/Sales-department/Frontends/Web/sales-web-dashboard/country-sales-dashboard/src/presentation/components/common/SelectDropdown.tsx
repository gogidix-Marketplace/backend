// SelectDropdown Component
// Dropdown selector with search capability

import React, { useState, useRef, useEffect } from 'react';

export interface SelectOption {
  value: string;
  label: string;
  icon?: string;
  disabled?: boolean;
  description?: string;
}

export interface SelectDropdownProps {
  options: SelectOption[];
  value?: string;
  onChange?: (value: string) => void;
  placeholder?: string;
  disabled?: boolean;
  searchable?: boolean;
  clearable?: boolean;
  size?: 'sm' | 'md' | 'lg';
  className?: string;
  maxHeight?: number;
}

export const SelectDropdown: React.FC<SelectDropdownProps> = ({
  options,
  value,
  onChange,
  placeholder = 'Select...',
  disabled = false,
  searchable = false,
  clearable = false,
  size = 'md',
  className = '',
  maxHeight = 300,
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const containerRef = useRef<HTMLDivElement>(null);
  const inputRef = useRef<HTMLInputElement>(null);

  const selectedOption = options.find(opt => opt.value === value);

  // Close dropdown when clicking outside
  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (containerRef.current && !containerRef.current.contains(event.target as Node)) {
        setIsOpen(false);
        setSearchQuery('');
      }
    };

    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  // Focus input when dropdown opens
  useEffect(() => {
    if (isOpen && searchable && inputRef.current) {
      inputRef.current.focus();
    }
  }, [isOpen, searchable]);

  const handleToggle = () => {
    if (!disabled) {
      setIsOpen(!isOpen);
    }
  };

  const handleSelect = (option: SelectOption) => {
    if (!option.disabled) {
      onChange?.(option.value);
      setIsOpen(false);
      setSearchQuery('');
    }
  };

  const handleClear = (e: React.MouseEvent) => {
    e.stopPropagation();
    onChange?.('');
  };

  const filteredOptions = searchable
    ? options.filter(opt =>
        opt.label.toLowerCase().includes(searchQuery.toLowerCase()) ||
        opt.value.toLowerCase().includes(searchQuery.toLowerCase())
      )
    : options;

  const getHighlightedLabel = (label: string) => {
    if (!searchQuery) return label;
    const regex = new RegExp(`(${searchQuery})`, 'gi');
    return label.split(regex).map((part, i) =>
      regex.test(part) ? (
        <mark key={i} className="select-dropdown-highlight">
          {part}
        </mark>
      ) : (
        part
      )
    );
  };

  return (
    <div
      ref={containerRef}
      className={`select-dropdown select-dropdown-${size} ${isOpen ? 'select-dropdown-open' : ''} ${className}`}
    >
      <button
        type="button"
        className="select-dropdown-trigger"
        onClick={handleToggle}
        disabled={disabled}
      >
        <div className="select-dropdown-value">
          {selectedOption?.icon && (
            <span className="select-dropdown-option-icon">{selectedOption.icon}</span>
          )}
          <span className="select-dropdown-label">
            {selectedOption?.label || placeholder}
          </span>
        </div>
        <div className="select-dropdown-actions">
          {clearable && selectedOption && (
            <span
              className="select-dropdown-clear"
              onClick={handleClear}
              role="button"
              tabIndex={-1}
            >
              \u2715
            </span>
          )}
          <span className={`select-dropdown-chevron ${isOpen ? 'chevron-up' : 'chevron-down'}`}>
            {isOpen ? '\u25B2' : '\u25BC'}
          </span>
        </div>
      </button>

      {isOpen && (
        <div className="select-dropdown-menu" style={{ maxHeight: `${maxHeight}px` }}>
          {searchable && (
            <div className="select-dropdown-search">
              <input
                ref={inputRef}
                type="text"
                className="select-dropdown-search-input"
                placeholder="Search..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                onClick={(e) => e.stopPropagation()}
              />
            </div>
          )}

          <div className="select-dropdown-options">
            {filteredOptions.length === 0 ? (
              <div className="select-dropdown-no-results">No results found</div>
            ) : (
              filteredOptions.map(option => (
                <button
                  key={option.value}
                  type="button"
                  className={`select-dropdown-option ${
                    option.value === value ? 'select-dropdown-option-selected' : ''
                  } ${option.disabled ? 'select-dropdown-option-disabled' : ''}`}
                  onClick={() => handleSelect(option)}
                  disabled={option.disabled}
                >
                  {option.icon && (
                    <span className="select-dropdown-option-icon">{option.icon}</span>
                  )}
                  <div className="select-dropdown-option-content">
                    <span className="select-dropdown-option-label">
                      {searchable ? getHighlightedLabel(option.label) : option.label}
                    </span>
                    {option.description && (
                      <span className="select-dropdown-option-description">
                        {option.description}
                      </span>
                    )}
                  </div>
                  {option.value === value && (
                    <span className="select-dropdown-option-check">\u2713</span>
                  )}
                </button>
              ))
            )}
          </div>
        </div>
      )}
    </div>
  );
};

// Multi-select variant
export interface MultiSelectDropdownProps extends Omit<SelectDropdownProps, 'value' | 'onChange'> {
  value?: string[];
  onChange?: (values: string[]) => void;
}

export const MultiSelectDropdown: React.FC<MultiSelectDropdownProps> = ({
  options,
  value = [],
  onChange,
  placeholder = 'Select...',
  disabled = false,
  searchable = false,
  size = 'md',
  className = '',
  maxHeight = 300,
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const containerRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    const handleClickOutside = (event: MouseEvent) => {
      if (containerRef.current && !containerRef.current.contains(event.target as Node)) {
        setIsOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleToggle = () => {
    if (!disabled) {
      setIsOpen(!isOpen);
    }
  };

  const handleSelect = (option: SelectOption) => {
    if (!option.disabled) {
      const newValue = value.includes(option.value)
        ? value.filter(v => v !== option.value)
        : [...value, option.value];
      onChange?.(newValue);
    }
  };

  const handleClear = () => {
    onChange?.([]);
  };

  const filteredOptions = searchable
    ? options.filter(opt =>
        opt.label.toLowerCase().includes(searchQuery.toLowerCase())
      )
    : options;

  const selectedOptions = options.filter(opt => value.includes(opt.value));

  return (
    <div
      ref={containerRef}
      className={`select-dropdown select-dropdown-multi select-dropdown-${size} ${isOpen ? 'select-dropdown-open' : ''} ${className}`}
    >
      <button
        type="button"
        className="select-dropdown-trigger"
        onClick={handleToggle}
        disabled={disabled}
      >
        <div className="select-dropdown-value">
          {selectedOptions.length > 0 ? (
            <div className="select-dropdown-tags">
              {selectedOptions.slice(0, 2).map(opt => (
                <span key={opt.value} className="select-dropdown-tag">
                  {opt.icon && <span>{opt.icon}</span>}
                  {opt.label}
                </span>
              ))}
              {selectedOptions.length > 2 && (
                <span className="select-dropdown-tag-more">
                  +{selectedOptions.length - 2} more
                </span>
              )}
            </div>
          ) : (
            <span className="select-dropdown-label">{placeholder}</span>
          )}
        </div>
        <div className="select-dropdown-actions">
          {selectedOptions.length > 0 && (
            <span
              className="select-dropdown-clear"
              onClick={handleClear}
              role="button"
              tabIndex={-1}
            >
              \u2715
            </span>
          )}
          <span className={`select-dropdown-chevron ${isOpen ? 'chevron-up' : 'chevron-down'}`}>
            {isOpen ? '\u25B2' : '\u25BC'}
          </span>
        </div>
      </button>

      {isOpen && (
        <div className="select-dropdown-menu" style={{ maxHeight: `${maxHeight}px` }}>
          {searchable && (
            <div className="select-dropdown-search">
              <input
                type="text"
                className="select-dropdown-search-input"
                placeholder="Search..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                onClick={(e) => e.stopPropagation()}
              />
            </div>
          )}

          <div className="select-dropdown-options">
            {filteredOptions.map(option => (
              <button
                key={option.value}
                type="button"
                className={`select-dropdown-option ${
                  value.includes(option.value) ? 'select-dropdown-option-selected' : ''
                } ${option.disabled ? 'select-dropdown-option-disabled' : ''}`}
                onClick={() => handleSelect(option)}
                disabled={option.disabled}
              >
                <span className="select-dropdown-checkbox">
                  {value.includes(option.value) ? '\u2611' : '\u2610'}
                </span>
                {option.icon && (
                  <span className="select-dropdown-option-icon">{option.icon}</span>
                )}
                <span className="select-dropdown-option-label">{option.label}</span>
              </button>
            ))}
          </div>
        </div>
      )}
    </div>
  );
};

export default SelectDropdown;
