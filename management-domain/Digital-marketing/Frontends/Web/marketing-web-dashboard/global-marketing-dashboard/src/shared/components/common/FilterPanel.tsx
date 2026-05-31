// FilterPanel Component
// Collapsible panel for filters

import React, { useState } from 'react';
import { Button } from './Button';
import { SelectDropdown } from './SelectDropdown';
import './FilterPanel.css';

export interface FilterOption {
  label: string;
  value: string;
}

export interface FilterGroup {
  id: string;
  label: string;
  type: 'select' | 'multiselect' | 'date' | 'daterange' | 'text';
  options?: FilterOption[];
  value?: string | string[] | Date | { start: Date; end: Date };
  placeholder?: string;
}

export interface FilterPanelProps {
  filters: FilterGroup[];
  onFilterChange: (filterId: string, value: unknown) => void;
  onClear?: () => void;
  onApply?: () => void;
  className?: string;
  defaultExpanded?: boolean;
  filterCount?: number;
}

export const FilterPanel: React.FC<FilterPanelProps> = ({
  filters,
  onFilterChange,
  onClear,
  onApply,
  className = '',
  defaultExpanded = false,
  filterCount = 0,
}) => {
  const [isExpanded, setIsExpanded] = useState(defaultExpanded);

  const activeFilterCount = filters.filter(f =>
    f.value !== undefined && f.value !== '' && !(Array.isArray(f.value) && f.value.length === 0)
  ).length;

  const handleClear = () => {
    filters.forEach(filter => {
      if (filter.type === 'multiselect') {
        onFilterChange(filter.id, []);
      } else {
        onFilterChange(filter.id, '');
      }
    });
    onClear?.();
  };

  return (
    <div className={`filter-panel ${isExpanded ? 'filter-panel--expanded' : ''} ${className}`}>
      <div
        className="filter-panel__header"
        onClick={() => setIsExpanded(!isExpanded)}
      >
        <div className="filter-panel__title">
          <span className="filter-panel__icon">⚙️</span>
          <span>Filters</span>
          {activeFilterCount > 0 && (
            <span className="filter-panel__count">{activeFilterCount}</span>
          )}
        </div>
        <button
          type="button"
          className={`filter-panel__toggle ${isExpanded ? 'filter-panel__toggle--open' : ''}`}
          aria-label={isExpanded ? 'Collapse filters' : 'Expand filters'}
        >
          <svg width="20" height="20" viewBox="0 0 20 20" fill="currentColor">
            <path
              fillRule="evenodd"
              d="M5.293 7.293a1 1 0 011.414 0L10 10.586l3.293-3.293a1 1 0 111.414 1.414l-4 4a1 1 0 01-1.414 0l-4-4a1 1 0 010-1.414z"
              clipRule="evenodd"
            />
          </svg>
        </button>
      </div>

      {isExpanded && (
        <div className="filter-panel__body">
          <div className="filter-panel__groups">
            {filters.map(filter => (
              <div key={filter.id} className="filter-group">
                <label className="filter-group__label" htmlFor={`filter-${filter.id}`}>
                  {filter.label}
                </label>
                {filter.type === 'select' && (
                  <SelectDropdown
                    id={`filter-${filter.id}`}
                    options={filter.options || []}
                    value={filter.value as string || ''}
                    onChange={(value) => onFilterChange(filter.id, value)}
                    placeholder={filter.placeholder || `Select ${filter.label.toLowerCase()}`}
                  />
                )}
                {filter.type === 'multiselect' && (
                  <SelectDropdown
                    id={`filter-${filter.id}`}
                    options={filter.options || []}
                    value={(filter.value as string[])?.[0] || ''}
                    onChange={(value) => onFilterChange(filter.id, [value])}
                    placeholder={filter.placeholder || `Select ${filter.label.toLowerCase()}`}
                    multi
                  />
                )}
                {filter.type === 'text' && (
                  <input
                    id={`filter-${filter.id}`}
                    type="text"
                    className="filter-group__input"
                    value={filter.value as string || ''}
                    onChange={(e) => onFilterChange(filter.id, e.target.value)}
                    placeholder={filter.placeholder || `Search ${filter.label.toLowerCase()}`}
                  />
                )}
                {filter.type === 'date' && (
                  <input
                    id={`filter-${filter.id}`}
                    type="date"
                    className="filter-group__input"
                    value={filter.value ? new Date(filter.value as Date).toISOString().split('T')[0] : ''}
                    onChange={(e) => onFilterChange(filter.id, e.target.value ? new Date(e.target.value) : undefined)}
                  />
                )}
                {filter.type === 'daterange' && (
                  <div className="filter-group__daterange">
                    <input
                      type="date"
                      className="filter-group__input"
                      value={(filter.value as { start: Date; end: Date })?.start ? new Date((filter.value as { start: Date; end: Date }).start).toISOString().split('T')[0] : ''}
                      onChange={(e) => onFilterChange(filter.id, {
                        ...(filter.value as { start: Date; end: Date }),
                        start: e.target.value ? new Date(e.target.value) : undefined
                      })}
                      placeholder="Start date"
                    />
                    <span className="daterange-separator">to</span>
                    <input
                      type="date"
                      className="filter-group__input"
                      value={(filter.value as { start: Date; end: Date })?.end ? new Date((filter.value as { start: Date; end: Date }).end).toISOString().split('T')[0] : ''}
                      onChange={(e) => onFilterChange(filter.id, {
                        ...(filter.value as { start: Date; end: Date }),
                        end: e.target.value ? new Date(e.target.value) : undefined
                      })}
                      placeholder="End date"
                    />
                  </div>
                )}
              </div>
            ))}
          </div>

          <div className="filter-panel__actions">
            <Button
              variant="secondary"
              size="sm"
              onClick={handleClear}
            >
              Clear All
            </Button>
            <Button
              variant="primary"
              size="sm"
              onClick={onApply}
            >
              Apply Filters
            </Button>
          </div>
        </div>
      )}
    </div>
  );
};

export default FilterPanel;
