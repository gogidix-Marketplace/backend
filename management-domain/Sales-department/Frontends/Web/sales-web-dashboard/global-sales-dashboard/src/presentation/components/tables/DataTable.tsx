// DataTable Component
// Generic data table with sorting and filtering

import { useState, useMemo, type React } from 'react';
import type { SortOrder } from '@shared/types';
import './DataTable.css';

export interface Column<T> {
  key: string;
  label: string;
  sortable?: boolean;
  filterable?: boolean;
  render?: (value: any, row: T, index: number) => React.ReactNode;
  format?: (value: any) => string;
  width?: string;
  align?: 'left' | 'center' | 'right';
}

export interface DataTableProps<T> {
  columns: Column<T>[];
  data: T[];
  onRowClick?: (row: T, index: number) => void;
  className?: string;
  emptyMessage?: string;
  isLoading?: boolean;
  pageSize?: number;
  selectable?: boolean;
  onSelectionChange?: (selectedRows: T[]) => void;
  rowKey?: keyof T | ((row: T) => string);
  stickyHeader?: boolean;
  compact?: boolean;
}

export function DataTable<T extends Record<string, any>>({
  columns,
  data,
  onRowClick,
  className = '',
  emptyMessage = 'No data available',
  isLoading = false,
  pageSize,
  selectable = false,
  onSelectionChange,
  rowKey = 'id',
  stickyHeader = true,
  compact = false,
}: DataTableProps<T>): React.ReactElement {
  const [sortColumn, setSortColumn] = useState<string>('');
  const [sortOrder, setSortOrder] = useState<SortOrder>('asc');
  const [filters, setFilters] = useState<Record<string, string>>({});
  const [selectedRows, setSelectedRows] = useState<Set<string | number>>(new Set());
  const [currentPage, setCurrentPage] = useState(1);

  // Get row key
  const getRowKey = (row: T, index: number): string | number => {
    if (typeof rowKey === 'function') {
      return rowKey(row);
    }
    return row[rowKey] || index;
  };

  // Sort data
  const sortedData = useMemo(() => {
    if (!sortColumn) return data;

    return [...data].sort((a, b) => {
      const aVal = a[sortColumn];
      const bVal = b[sortColumn];

      if (typeof aVal === 'number' && typeof bVal === 'number') {
        return sortOrder === 'asc' ? aVal - bVal : bVal - aVal;
      }

      const aStr = String(aVal || '').toLowerCase();
      const bStr = String(bVal || '').toLowerCase();

      if (sortOrder === 'asc') {
        return aStr.localeCompare(bStr);
      }
      return bStr.localeCompare(aStr);
    });
  }, [data, sortColumn, sortOrder]);

  // Filter data
  const filteredData = useMemo(() => {
    return sortedData.filter(row => {
      return Object.entries(filters).every(([key, value]) => {
        if (!value) return true;
        const rowValue = String(row[key] || '').toLowerCase();
        return rowValue.includes(value.toLowerCase());
      });
    });
  }, [sortedData, filters]);

  // Pagination
  const paginatedData = useMemo(() => {
    if (!pageSize) return filteredData;
    const start = (currentPage - 1) * pageSize;
    return filteredData.slice(start, start + pageSize);
  }, [filteredData, currentPage, pageSize]);

  const totalPages = pageSize ? Math.ceil(filteredData.length / pageSize) : 1;

  // Handle sort
  const handleSort = (column: string) => {
    if (sortColumn === column) {
      setSortOrder(sortOrder === 'asc' ? 'desc' : 'asc');
    } else {
      setSortColumn(column);
      setSortOrder('asc');
    }
  };

  // Handle filter
  const handleFilter = (column: string, value: string) => {
    setFilters(prev => ({ ...prev, [column]: value }));
    setCurrentPage(1);
  };

  // Handle row selection
  const handleRowSelect = (rowKey: string | number, checked: boolean) => {
    const newSelection = new Set(selectedRows);
    if (checked) {
      newSelection.add(rowKey);
    } else {
      newSelection.delete(rowKey);
    }
    setSelectedRows(newSelection);

    if (onSelectionChange) {
      const selectedData = data.filter(row => newSelection.has(getRowKey(row, data.indexOf(row))));
      onSelectionChange(selectedData);
    }
  };

  // Handle select all
  const handleSelectAll = (checked: boolean) => {
    const newSelection = new Set<string | number>();
    if (checked) {
      filteredData.forEach((row, index) => {
        newSelection.add(getRowKey(row, index));
      });
    }
    setSelectedRows(newSelection);

    if (onSelectionChange) {
      onSelectionChange(checked ? [...filteredData] : []);
    }
  };

  const allSelected = filteredData.length > 0 && selectedRows.size === filteredData.length;
  const someSelected = selectedRows.size > 0 && selectedRows.size < filteredData.length;

  const renderSortIcon = (column: string) => {
    if (sortColumn !== column) return <span className="sort-icon">↕</span>;
    return <span className="sort-icon">{sortOrder === 'asc' ? '↑' : '↓'}</span>;
  };

  // Get cell value
  const getCellValue = (row: T, column: Column<T>, index: number): React.ReactNode => {
    const value = row[column.key];

    if (column.render) {
      return column.render(value, row, index);
    }

    if (column.format) {
      return column.format(value);
    }

    return value;
  };

  if (isLoading) {
    return (
      <div className={`data-table data-table-loading ${className}`}>
        <div className="data-table-skeleton">
          {Array.from({ length: 5 }).map((_, i) => (
            <div key={i} className="skeleton-row">
              {Array.from({ length: columns.length }).map((_, j) => (
                <div key={j} className="skeleton-cell" style={{ width: `${100 / columns.length}%` }}></div>
              ))}
            </div>
          ))}
        </div>
      </div>
    );
  }

  return (
    <div className={`data-table ${compact ? 'data-table-compact' : ''} ${stickyHeader ? 'data-table-sticky' : ''} ${className}`}>
      {/* Filters row */}
      <div className="data-table-filters">
        {columns.filter(col => col.filterable).map(col => (
          <input
            key={col.key}
            type="text"
            placeholder={`Filter by ${col.label}...`}
            value={filters[col.key] || ''}
            onChange={(e) => handleFilter(col.key, e.target.value)}
            className="data-table-filter-input"
          />
        ))}
      </div>

      {/* Table */}
      <div className="data-table-container">
        <table className="data-table-element">
          <thead className={stickyHeader ? 'sticky-header' : ''}>
            <tr>
              {selectable && (
                <th className="select-column">
                  <input
                    type="checkbox"
                    checked={allSelected}
                    ref={input => {
                      if (input) {
                        input.indeterminate = someSelected && !allSelected;
                      }
                    }}
                    onChange={(e) => handleSelectAll(e.target.checked)}
                  />
                </th>
              )}
              {columns.map(col => (
                <th
                  key={col.key}
                  style={{ width: col.width, textAlign: col.align }}
                  className={col.sortable ? 'sortable' : ''}
                  onClick={() => col.sortable && handleSort(col.key)}
                >
                  {col.label}
                  {col.sortable && renderSortIcon(col.key)}
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {paginatedData.length === 0 ? (
              <tr>
                <td colSpan={columns.length + (selectable ? 1 : 0)} className="data-table-empty">
                  {emptyMessage}
                </td>
              </tr>
            ) : (
              paginatedData.map((row, rowIndex) => {
                const key = getRowKey(row, rowIndex);
                return (
                  <tr
                    key={key}
                    className={onRowClick ? 'clickable' : ''}
                    onClick={() => onRowClick?.(row, rowIndex)}
                  >
                    {selectable && (
                      <td className="select-column">
                        <input
                          type="checkbox"
                          checked={selectedRows.has(key)}
                          onChange={(e) => handleRowSelect(key, e.target.checked)}
                          onClick={(e) => e.stopPropagation()}
                        />
                      </td>
                    )}
                    {columns.map(col => (
                      <td
                        key={col.key}
                        style={{ width: col.width, textAlign: col.align }}
                        className="data-table-cell"
                      >
                        {getCellValue(row, col, rowIndex)}
                      </td>
                    ))}
                  </tr>
                );
              })
            )}
          </tbody>
        </table>
      </div>

      {/* Pagination */}
      {pageSize && totalPages > 1 && (
        <div className="data-table-pagination">
          <button
            className="pagination-btn"
            onClick={() => setCurrentPage(p => Math.max(1, p - 1))}
            disabled={currentPage === 1}
          >
            Previous
          </button>
          <span className="pagination-info">
            Page {currentPage} of {totalPages} ({filteredData.length} records)
          </span>
          <button
            className="pagination-btn"
            onClick={() => setCurrentPage(p => Math.min(totalPages, p + 1))}
            disabled={currentPage === totalPages}
          >
            Next
          </button>
        </div>
      )}
    </div>
  );
}

export default DataTable;
