// FilterPanel Component
// Collapsible filter panel

import { useState } from 'react';
import './FilterPanel.css';

interface FilterGroup {
  id: string;
  label: string;
  type: 'checkbox' | 'radio' | 'select' | 'date';
  options?: Array<{ value: string; label: string; count?: number }>;
  value: string | string[];
  onChange: (value: string | string[]) => void;
}

interface FilterPanelProps {
  groups: FilterGroup[];
  onClear: () => void;
  onApply: () => void;
  activeFiltersCount?: number;
  className?: string;
}

export function FilterPanel({
  groups,
  onClear,
  onApply,
  activeFiltersCount = 0,
  className = '',
}: FilterPanelProps) {
  const [isExpanded, setIsExpanded] = useState(true);

  return (
    <div className={`filter-panel ${isExpanded ? 'filter-panel-expanded' : ''} ${className}`}>
      <div className="filter-panel-header">
        <h3 className="filter-panel-title">
          Filters
          {activeFiltersCount > 0 && (
            <span className="filter-panel-count">{activeFiltersCount}</span>
          )}
        </h3>
        <div className="filter-panel-actions">
          <button
            type="button"
            className="filter-panel-clear"
            onClick={onClear}
            disabled={activeFiltersCount === 0}
          >
            Clear
          </button>
          <button
            type="button"
            className="filter-panel-toggle"
            onClick={() => setIsExpanded(!isExpanded)}
          >
            {isExpanded ? '\u25B2' : '\u25BC'}
          </button>
        </div>
      </div>

      {isExpanded && (
        <div className="filter-panel-body">
          {groups.map((group) => (
            <div key={group.id} className="filter-group">
              <label className="filter-group-label">{group.label}</label>
              <div className="filter-group-options">
                {group.type === 'checkbox' && group.options?.map((option) => (
                  <label key={option.value} className="filter-checkbox">
                    <input
                      type="checkbox"
                      name={group.id}
                      value={option.value}
                      checked={Array.isArray(group.value) && group.value.includes(option.value)}
                      onChange={(e) => {
                        const currentValue = Array.isArray(group.value) ? group.value : [];
                        const newValue = e.target.checked
                          ? [...currentValue, option.value]
                          : currentValue.filter(v => v !== option.value);
                        group.onChange(newValue);
                      }}
                    />
                    <span className="filter-checkbox-label">{option.label}</span>
                    {option.count !== undefined && (
                      <span className="filter-count">({option.count})</span>
                    )}
                  </label>
                ))}
                {group.type === 'select' && (
                  <select
                    className="filter-select"
                    value={group.value as string}
                    onChange={(e) => group.onChange(e.target.value)}
                  >
                    <option value="">All</option>
                    {group.options?.map((option) => (
                      <option key={option.value} value={option.value}>
                        {option.label}
                      </option>
                    ))}
                  </select>
                )}
              </div>
            </div>
          ))}
        </div>
      )}

      {isExpanded && (
        <div className="filter-panel-footer">
          <button type="button" className="filter-panel-apply" onClick={onApply}>
            Apply Filters
          </button>
        </div>
      )}
    </div>
  );
}
