// SearchBar Component
// Provides search functionality with debouncing

import { useState, useCallback, useEffect, useRef, type React } from 'react';
import { debounce } from '@shared/utils';
import './SearchBar.css';

export interface SearchBarProps {
  placeholder?: string;
  value?: string;
  onChange?: (value: string) => void;
  onSearch?: (value: string) => void;
  debounceMs?: number;
  size?: 'sm' | 'md' | 'lg';
  variant?: 'default' | 'filled' | 'outlined';
  disabled?: boolean;
  className?: string;
  showClearButton?: boolean;
  autoFocus?: boolean;
}

export function SearchBar({
  placeholder = 'Search...',
  value: controlledValue,
  onChange,
  onSearch,
  debounceMs = 300,
  size = 'md',
  variant = 'default',
  disabled = false,
  className = '',
  showClearButton = true,
  autoFocus = false,
}: SearchBarProps): React.ReactElement {
  const [internalValue, setInternalValue] = useState(controlledValue || '');
  const [isFocused, setIsFocused] = useState(false);
  const inputRef = useRef<HTMLInputElement>(null);

  // Handle controlled/uncontrolled state
  const value = controlledValue !== undefined ? controlledValue : internalValue;

  // Create debounced search function
  const debouncedSearch = useCallback(
    debounce((searchValue: string) => {
      onSearch?.(searchValue);
    }, debounceMs),
    [onSearch, debounceMs]
  );

  // Handle input change
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value;

    if (controlledValue === undefined) {
      setInternalValue(newValue);
    }

    onChange?.(newValue);
    debouncedSearch(newValue);
  };

  // Handle clear button
  const handleClear = () => {
    if (controlledValue === undefined) {
      setInternalValue('');
    }
    onChange?.('');
    onSearch?.('');
    inputRef.current?.focus();
  };

  // Handle keyboard shortcuts
  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === 'Escape') {
      handleClear();
    } else if (e.key === 'Enter') {
      onSearch?.(value);
    }
  };

  // Focus input on mount if autoFocus is true
  useEffect(() => {
    if (autoFocus) {
      inputRef.current?.focus();
    }
  }, [autoFocus]);

  const hasValue = value.trim().length > 0;

  return (
    <div
      className={`search-bar search-bar-${size} search-bar-${variant} ${isFocused ? 'search-bar-focused' : ''} ${disabled ? 'search-bar-disabled' : ''} ${className}`}
    >
      <div className="search-bar-container">
        <svg
          className="search-bar-icon"
          width="20"
          height="20"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          strokeWidth="2"
          strokeLinecap="round"
          strokeLinejoin="round"
        >
          <circle cx="11" cy="11" r="8" />
          <path d="M21 21l-4.35-4.35" />
        </svg>

        <input
          ref={inputRef}
          type="text"
          className="search-bar-input"
          placeholder={placeholder}
          value={value}
          onChange={handleChange}
          onKeyDown={handleKeyDown}
          onFocus={() => setIsFocused(true)}
          onBlur={() => setIsFocused(false)}
          disabled={disabled}
          autoComplete="off"
        />

        {showClearButton && hasValue && !disabled && (
          <button
            type="button"
            className="search-bar-clear"
            onClick={handleClear}
            aria-label="Clear search"
          >
            <svg
              width="16"
              height="16"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              strokeWidth="2"
            >
              <line x1="18" y1="6" x2="6" y2="18" />
              <line x1="6" y1="6" x2="18" y2="18" />
            </svg>
          </button>
        )}
      </div>
    </div>
  );
}

export default SearchBar;
