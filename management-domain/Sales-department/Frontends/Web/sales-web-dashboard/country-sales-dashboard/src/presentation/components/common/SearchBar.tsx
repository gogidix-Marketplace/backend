// SearchBar Component
// Search functionality with debounced input

import React, { useState, useCallback, useEffect } from 'react';
import { debounce } from '@shared/utils';

export interface SearchBarProps {
  placeholder?: string;
  value?: string;
  onChange?: (value: string) => void;
  onSearch?: (value: string) => void;
  debounceMs?: number;
  isLoading?: boolean;
  disabled?: boolean;
  autoFocus?: boolean;
  showClearButton?: boolean;
  className?: string;
  size?: 'sm' | 'md' | 'lg';
}

export const SearchBar: React.FC<SearchBarProps> = ({
  placeholder = 'Search...',
  value: controlledValue,
  onChange,
  onSearch,
  debounceMs = 300,
  isLoading = false,
  disabled = false,
  autoFocus = false,
  showClearButton = true,
  className = '',
  size = 'md',
}) => {
  const [localValue, setLocalValue] = useState(controlledValue || '');
  const [isFocused, setIsFocused] = useState(false);

  // Update local state when controlled value changes
  useEffect(() => {
    if (controlledValue !== undefined) {
      setLocalValue(controlledValue);
    }
  }, [controlledValue]);

  // Debounced search function
  const debouncedSearch = useCallback(
    debounce((value: string) => {
      onSearch?.(value);
    }, debounceMs),
    [onSearch, debounceMs]
  );

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newValue = e.target.value;
    setLocalValue(newValue);
    onChange?.(newValue);
    debouncedSearch(newValue);
  };

  const handleClear = () => {
    setLocalValue('');
    onChange?.('');
    onSearch?.('');
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSearch?.(localValue);
  };

  const hasValue = localValue.length > 0;

  return (
    <form
      className={`search-bar search-bar-${size} ${isFocused ? 'search-bar-focused' : ''} ${className}`}
      onSubmit={handleSubmit}
    >
      <div className="search-bar-input-wrapper">
        <span className="search-bar-icon">{'\u{1F50D}'}</span>
        <input
          type="text"
          className="search-bar-input"
          placeholder={placeholder}
          value={localValue}
          onChange={handleChange}
          onFocus={() => setIsFocused(true)}
          onBlur={() => setIsFocused(false)}
          disabled={disabled || isLoading}
          autoFocus={autoFocus}
        />
        {showClearButton && hasValue && !isLoading && (
          <button
            type="button"
            className="search-bar-clear"
            onClick={handleClear}
            tabIndex={-1}
          >
            \u2715
          </button>
        )}
        {isLoading && (
          <span className="search-bar-loading">\u231B</span>
        )}
      </div>
    </form>
  );
};

export default SearchBar;
