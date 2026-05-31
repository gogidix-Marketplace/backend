// FilterPanel Component
// Collapsible filtering interface for data tables

import React, { useState } from 'react';

export interface FilterOption {
  label: string;
  value: string;
  count?: number;
}

export interface FilterGroup {
  id: string;
  label: string;
  type: 'checkbox' | 'radio' | 'select' | 'date-range';
  options?: FilterOption[];
  value?: string | string[];
  onChange?: (value: string | string[]) => void;
}

export interface FilterPanelProps {
  groups: FilterGroup[];
  activeFiltersCount?: number;
  onClearAll?: () => void;
  onApply?: () => void;
  isLoading?: boolean;
  className?: string;
}

export const FilterPanel: React.FC<FilterPanelProps> = ({
  groups,
  activeFiltersCount = 0,
  onClearAll,
  onApply,
  isLoading = false,
  className = '',
}) => {
  const [isExpanded, setIsExpanded] = useState(true);
  const [localFilters, setLocalFilters] = useState<Record<string, string | string[]>>({});

  const handleFilterChange = (groupId: string, value: string | string[]) => {
    setLocalFilters(prev => ({ ...prev, [groupId]: value }));
  };

  const handleClearAll = () => {
    setLocalFilters({});
    onClearAll?.();
  };

  const handleApply = () => {
    onApply?.();
  };

  return (
    <div className={`filter-panel ${isExpanded ? 'filter-panel-expanded' : ''} ${className}`}>
      <button
        className="filter-panel-toggle"
        onClick={() => setIsExpanded(!isExpanded)}
      >
        <span className="filter-panel-title">
          Filters
          {activeFiltersCount > 0 && (
            <span className="filter-panel-count">{activeFiltersCount}</span>
          )}
        </span>
        <span className={`filter-panel-chevron ${isExpanded ? 'chevron-up' : 'chevron-down'}`}>
          {isExpanded ? '\u25B2' : '\u25BC'}
        </span>
      </button>

      {isExpanded && (
        <div className="filter-panel-content">
          <div className="filter-panel-groups">
            {groups.map(group => (
              <div key={group.id} className="filter-group">
                <h4 className="filter-group-label">{group.label}</h4>

                {group.type === 'checkbox' && group.options && (
                  <div className="filter-checkboxes">
                    {group.options.map(option => (
                      <label key={option.value} className="filter-checkbox">
                        <input
                          type="checkbox"
                          checked={Array.isArray(localFilters[group.id])
                            ? (localFilters[group.id] as string[]).includes(option.value)
                            : false
                          }
                          onChange={(e) => {
                            const current = Array.isArray(localFilters[group.id])
                              ? localFilters[group.id] as string[]
                              : [];
                            const updated = e.target.checked
                              ? [...current, option.value]
                              : current.filter(v => v !== option.value);
                            handleFilterChange(group.id, updated);
                            group.onChange?.(updated);
                          }}
                          disabled={isLoading}
                        />
                        <span className="filter-checkbox-label">{option.label}</span>
                        {option.count !== undefined && (
                          <span className="filter-checkbox-count">({option.count})</span>
                        )}
                      </label>
                    ))}
                  </div>
                )}

                {group.type === 'radio' && group.options && (
                  <div className="filter-radios">
                    {group.options.map(option => (
                      <label key={option.value} className="filter-radio">
                        <input
                          type="radio"
                          name={group.id}
                          value={option.value}
                          checked={localFilters[group.id] === option.value}
                          onChange={(e) => {
                            handleFilterChange(group.id, e.target.value);
                            group.onChange?.(e.target.value);
                          }}
                          disabled={isLoading}
                        />
                        <span className="filter-radio-label">{option.label}</span>
                        {option.count !== undefined && (
                          <span className="filter-radio-count">({option.count})</span>
                        )}
                      </label>
                    ))}
                  </div>
                )}

                {group.type === 'select' && group.options && (
                  <select
                    className="filter-select"
                    value={localFilters[group.id] as string || ''}
                    onChange={(e) => {
                      handleFilterChange(group.id, e.target.value);
                      group.onChange?.(e.target.value);
                    }}
                    disabled={isLoading}
                  >
                    <option value="">All</option>
                    {group.options.map(option => (
                      <option key={option.value} value={option.value}>
                        {option.label}
                      </option>
                    ))}
                  </select>
                )}

                {group.type === 'date-range' && (
                  <div className="filter-date-range">
                    <input
                      type="date"
                      className="filter-date-input"
                      disabled={isLoading}
                    />
                    <span className="filter-date-separator">to</span>
                    <input
                      type="date"
                      className="filter-date-input"
                      disabled={isLoading}
                    />
                  </div>
                )}
              </div>
            ))}
          </div>

          <div className="filter-panel-actions">
            <button
              className="filter-panel-btn filter-panel-btn-clear"
              onClick={handleClearAll}
              disabled={activeFiltersCount === 0 || isLoading}
            >
              Clear All
            </button>
            <button
              className="filter-panel-btn filter-panel-btn-apply"
              onClick={handleApply}
              disabled={isLoading}
            >
              {isLoading ? 'Applying...' : 'Apply Filters'}
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default FilterPanel;
