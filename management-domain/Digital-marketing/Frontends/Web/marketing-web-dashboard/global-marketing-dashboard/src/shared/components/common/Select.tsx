// Select Component
// Custom dropdown select component

import React, { useRef, useState } from 'react';
import clickOutside from '../../utils/clickOutside';
import './Select.css';

export interface SelectOption {
  value: string;
  label: string;
  disabled?: boolean;
  icon?: React.ReactNode;
}

export interface SelectProps {
  options: SelectOption[];
  value?: string;
  onChange: (value: string) => void;
  placeholder?: string;
  disabled?: boolean;
  error?: string;
  className?: string;
  size?: 'sm' | 'md' | 'lg';
  searchable?: boolean;
}

export const Select: React.FC<SelectProps> = ({
  options,
  value,
  onChange,
  placeholder = 'Select...',
  disabled = false,
  error,
  className = '',
  size = 'md',
  searchable = false,
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const containerRef = useRef<HTMLDivElement>(null);

  clickOutside(containerRef, () => setIsOpen(false));

  const selectedOption = options.find((opt) => opt.value === value);

  const filteredOptions = searchable
    ? options.filter((opt) =>
        opt.label.toLowerCase().includes(searchTerm.toLowerCase())
      )
    : options;

  const handleSelect = (optionValue: string) => {
    if (!disabled) {
      onChange(optionValue);
      setIsOpen(false);
      setSearchTerm('');
    }
  };

  const toggleOpen = () => {
    if (!disabled) {
      setIsOpen(!isOpen);
    }
  };

  const classes = [
    'select',
    `select--${size}`,
    error && 'select--error',
    disabled && 'select--disabled',
    isOpen && 'select--open',
    className,
  ]
    .filter(Boolean)
    .join(' ');

  return (
    <div ref={containerRef} className={classes}>
      <div
        className="select__trigger"
        onClick={toggleOpen}
        tabIndex={disabled ? -1 : 0}
        role="combobox"
        aria-expanded={isOpen}
        aria-haspopup="listbox"
      >
        <div className="select__value">
          {selectedOption ? (
            <>
              {selectedOption.icon && (
                <span className="select__option-icon">{selectedOption.icon}</span>
              )}
              <span className="select__label">{selectedOption.label}</span>
            </>
          ) : (
            <span className="select__placeholder">{placeholder}</span>
          )}
        </div>
        <span className="select__arrow">
          <svg
            width="16"
            height="16"
            viewBox="0 0 16 16"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
          >
            <path d="M4 6l4 4 4-4" />
          </svg>
        </span>
      </div>

      {isOpen && (
        <div className="select__dropdown" role="listbox">
          {searchable && (
            <div className="select__search">
              <input
                type="text"
                placeholder="Search..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                autoFocus
                onClick={(e) => e.stopPropagation()}
              />
            </div>
          )}
          <div className="select__options">
            {filteredOptions.length === 0 ? (
              <div className="select__no-results">No results found</div>
            ) : (
              filteredOptions.map((option) => (
                <div
                  key={option.value}
                  className={`select__option ${
                    option.value === value ? 'select__option--selected' : ''
                  } ${option.disabled ? 'select__option--disabled' : ''}`}
                  onClick={() => !option.disabled && handleSelect(option.value)}
                  role="option"
                  aria-selected={option.value === value}
                >
                  {option.icon && (
                    <span className="select__option-icon">{option.icon}</span>
                  )}
                  <span className="select__option-label">{option.label}</span>
                  {option.value === value && (
                    <span className="select__option-check">
                      <svg
                        width="16"
                        height="16"
                        viewBox="0 0 16 16"
                        fill="none"
                        stroke="currentColor"
                        strokeWidth="2"
                      >
                        <path d="M3 8l4 4 8-8" />
                      </svg>
                    </span>
                  )}
                </div>
              ))
            )}
          </div>
        </div>
      )}

      {error && <span className="select__error">{error}</span>}
    </div>
  );
};

export interface MultiSelectProps extends Omit<SelectProps, 'value' | 'onChange'> {
  value?: string[];
  onChange: (values: string[]) => void;
}

export const MultiSelect: React.FC<MultiSelectProps> = ({
  options,
  value = [],
  onChange,
  placeholder = 'Select...',
  disabled = false,
  error,
  className = '',
  size = 'md',
  searchable = false,
}) => {
  const [isOpen, setIsOpen] = useState(false);
  const [searchTerm, setSearchTerm] = useState('');
  const containerRef = useRef<HTMLDivElement>(null);

  clickOutside(containerRef, () => setIsOpen(false));

  const selectedOptions = options.filter((opt) => value.includes(opt.value));

  const filteredOptions = searchable
    ? options.filter((opt) =>
        opt.label.toLowerCase().includes(searchTerm.toLowerCase())
      )
    : options;

  const handleToggle = (optionValue: string) => {
    if (!disabled) {
      const newValue = value.includes(optionValue)
        ? value.filter((v) => v !== optionValue)
        : [...value, optionValue];
      onChange(newValue);
    }
  };

  const handleRemove = (optionValue: string, e: React.MouseEvent) => {
    e.stopPropagation();
    if (!disabled) {
      onChange(value.filter((v) => v !== optionValue));
    }
  };

  const classes = [
    'select',
    'select--multi',
    `select--${size}`,
    error && 'select--error',
    disabled && 'select--disabled',
    isOpen && 'select--open',
    className,
  ]
    .filter(Boolean)
    .join(' ');

  return (
    <div ref={containerRef} className={classes}>
      <div
        className="select__trigger"
        onClick={() => !disabled && setIsOpen(!isOpen)}
        tabIndex={disabled ? -1 : 0}
        role="combobox"
        aria-expanded={isOpen}
        aria-haspopup="listbox"
      >
        <div className="select__value select__value--multi">
          {selectedOptions.length === 0 ? (
            <span className="select__placeholder">{placeholder}</span>
          ) : (
            <div className="select__tags">
              {selectedOptions.map((option) => (
                <span key={option.value} className="select__tag">
                  {option.icon && (
                    <span className="select__tag-icon">{option.icon}</span>
                  )}
                  <span className="select__tag-label">{option.label}</span>
                  <button
                    type="button"
                    className="select__tag-remove"
                    onClick={(e) => handleRemove(option.value, e)}
                  >
                    <svg
                      width="12"
                      height="12"
                      viewBox="0 0 12 12"
                      fill="none"
                      stroke="currentColor"
                      strokeWidth="2"
                    >
                      <path d="M3 3l6 6M9 3l-6 6" />
                    </svg>
                  </button>
                </span>
              ))}
            </div>
          )}
        </div>
        <span className="select__arrow">
          <svg
            width="16"
            height="16"
            viewBox="0 0 16 16"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
          >
            <path d="M4 6l4 4 4-4" />
          </svg>
        </span>
      </div>

      {isOpen && (
        <div className="select__dropdown" role="listbox">
          {searchable && (
            <div className="select__search">
              <input
                type="text"
                placeholder="Search..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                autoFocus
                onClick={(e) => e.stopPropagation()}
              />
            </div>
          )}
          <div className="select__options">
            {filteredOptions.length === 0 ? (
              <div className="select__no-results">No results found</div>
            ) : (
              filteredOptions.map((option) => (
                <div
                  key={option.value}
                  className={`select__option ${
                    value.includes(option.value) ? 'select__option--selected' : ''
                  } ${option.disabled ? 'select__option--disabled' : ''}`}
                  onClick={() => !option.disabled && handleToggle(option.value)}
                  role="option"
                  aria-selected={value.includes(option.value)}
                >
                  {option.icon && (
                    <span className="select__option-icon">{option.icon}</span>
                  )}
                  <span className="select__option-label">{option.label}</span>
                  {value.includes(option.value) && (
                    <span className="select__option-check">
                      <svg
                        width="16"
                        height="16"
                        viewBox="0 0 16 16"
                        fill="none"
                        stroke="currentColor"
                        strokeWidth="2"
                      >
                        <path d="M3 8l4 4 8-8" />
                      </svg>
                    </span>
                  )}
                </div>
              ))
            )}
          </div>
        </div>
      )}

      {error && <span className="select__error">{error}</span>}
    </div>
  );
};

export default Select;
