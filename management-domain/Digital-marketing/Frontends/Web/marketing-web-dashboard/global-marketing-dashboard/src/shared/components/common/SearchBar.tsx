// SearchBar Component
// Search input with optional filters

import React, { useState, useRef, useEffect } from 'react';
import './SearchBar.css';

export interface SearchBarProps {
  value: string;
  onChange: (value: string) => void;
  onSearch?: (value: string) => void;
  placeholder?: string;
  size?: 'sm' | 'md' | 'lg';
  disabled?: boolean;
  loading?: boolean;
  clearable?: boolean;
  icon?: React.ReactNode;
  suffix?: React.ReactNode;
  className?: string;
  autoFocus?: boolean;
}

export const SearchBar: React.FC<SearchBarProps> = ({
  value,
  onChange,
  onSearch,
  placeholder = 'Search...',
  size = 'md',
  disabled = false,
  loading = false,
  clearable = true,
  icon,
  suffix,
  className = '',
  autoFocus = false,
}) => {
  const inputRef = useRef<HTMLInputElement>(null);
  const [localValue, setLocalValue] = useState(value);

  useEffect(() => {
    setLocalValue(value);
  }, [value]);

  useEffect(() => {
    if (autoFocus && inputRef.current) {
      inputRef.current.focus();
    }
  }, [autoFocus]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value;
    setLocalValue(newValue);
    onChange(newValue);
  };

  const handleClear = () => {
    setLocalValue('');
    onChange('');
    inputRef.current?.focus();
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Enter' && onSearch) {
      onSearch(localValue);
    }
    if (e.key === 'Escape') {
      handleClear();
    }
  };

  const showClear = clearable && localValue.length > 0 && !disabled && !loading;

  return (
    <div className={`search-bar search-bar--${size} ${disabled ? 'search-bar--disabled' : ''} ${className}`}>
      {icon || (
        <span className="search-bar__icon">
          <svg width="16" height="16" viewBox="0 0 16 16" fill="currentColor">
            <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z" />
          </svg>
        </span>
      )}
      <input
        ref={inputRef}
        type="text"
        className="search-bar__input"
        value={localValue}
        onChange={handleChange}
        onKeyDown={handleKeyDown}
        placeholder={placeholder}
        disabled={disabled}
      />
      {loading && (
        <span className="search-bar__spinner">
          <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
            <circle
              cx="8"
              cy="8"
              r="6"
              stroke="currentColor"
              strokeWidth="2"
              strokeLinecap="round"
              strokeDasharray="24"
              strokeDashoffset="12"
            >
              <animateTransform
                attributeName="transform"
                type="rotate"
                from="0 8 8"
                to="360 8 8"
                dur="1s"
                repeatCount="indefinite"
              />
            </circle>
          </svg>
        </span>
      )}
      {showClear && !loading && (
        <button
          type="button"
          className="search-bar__clear"
          onClick={handleClear}
          aria-label="Clear search"
        >
          <svg width="14" height="14" viewBox="0 0 14 14" fill="currentColor">
            <path d="M7 7l5-5M7 7l-5 5M7 7l5 5M7 7L2 2" stroke="currentColor" strokeWidth="2" strokeLinecap="round" />
          </svg>
        </button>
      )}
      {suffix && !showClear && !loading && (
        <span className="search-bar__suffix">{suffix}</span>
      )}
    </div>
  );
};

export default SearchBar;
