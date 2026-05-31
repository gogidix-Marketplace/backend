// Table Component
// Reusable table with sorting, pagination, and selection

import { useState } from 'react';
import './Table.css';

export interface Column<T> {
  key: string;
  title: string;
  render?: (value: any, row: T, index: number) => React.ReactNode;
  sortable?: boolean;
  width?: string;
  align?: 'left' | 'center' | 'right';
}

interface TableProps<T> {
  columns: Column<T>[];
  data: T[];
  keyField: string;
  loading?: boolean;
  emptyMessage?: string;
  sortable?: boolean;
  selectable?: boolean;
  onRowClick?: (row: T) => void;
  onSort?: (column: string, direction: 'asc' | 'desc') => void;
  onSelect?: (selectedRows: T[]) => void;
  className?: string;
}

export function Table<T extends Record<string, any>>({
  columns,
  data,
  keyField,
  loading = false,
  emptyMessage = 'No data available',
  sortable = false,
  selectable = false,
  onRowClick,
  onSort,
  onSelect,
  className = '',
}: TableProps<T>) {
  const [sortColumn, setSortColumn] = useState<string | null>(null);
  const [sortDirection, setSortDirection] = useState<'asc' | 'desc'>('asc');
  const [selectedRows, setSelectedRows] = useState<Set<string>>(new Set());

  const handleSort = (column: Column<T>) => {
    if (!column.sortable || !sortable) return;

    const newDirection = sortColumn === column.key && sortDirection === 'asc' ? 'desc' : 'asc';
    setSortColumn(column.key);
    setSortDirection(newDirection);
    onSort?.(column.key, newDirection);
  };

  const handleSelectAll = () => {
    if (selectedRows.size === data.length) {
      setSelectedRows(new Set());
    } else {
      setSelectedRows(new Set(data.map(row => String(row[keyField]))));
    }
    onSelect?.(data);
  };

  const handleSelectRow = (row: T) => {
    const rowKey = String(row[keyField]);
    const newSelected = new Set(selectedRows);

    if (newSelected.has(rowKey)) {
      newSelected.delete(rowKey);
    } else {
      newSelected.add(rowKey);
    }

    setSelectedRows(newSelected);

    const selectedData = data.filter(r => newSelected.has(String(r[keyField])));
    onSelect?.(selectedData);
  };

  const getCellValue = (column: Column<T>, row: T, index: number) => {
    const value = row[column.key];
    return column.render ? column.render(value, row, index) : value;
  };

  if (loading) {
    return (
      <div className={`table-container ${className}`}>
        <div className="table-skeleton">
          <div className="table-skeleton-header">
            {columns.map((_, i) => (
              <div key={i} className="table-skeleton-cell"></div>
            ))}
          </div>
          {[1, 2, 3, 4, 5].map((_, i) => (
            <div key={i} className="table-skeleton-row">
              {columns.map((_, j) => (
                <div key={j} className="table-skeleton-cell"></div>
              ))}
            </div>
          ))}
        </div>
      </div>
    );
  }

  return (
    <div className={`table-container ${className}`}>
      <div className="table-wrapper">
        <table className="table">
          <thead>
            <tr>
              {selectable && (
                <th className="table-select-cell">
                  <input
                    type="checkbox"
                    checked={selectedRows.size === data.length && data.length > 0}
                    onChange={handleSelectAll}
                  />
                </th>
              )}
              {columns.map((column) => (
                <th
                  key={column.key}
                  className={`table-th-${column.align || 'left'} ${column.sortable && sortable ? 'table-th-sortable' : ''}`}
                  style={{ width: column.width }}
                  onClick={() => handleSort(column)}
                >
                  <div className="table-header-content">
                    <span>{column.title}</span>
                    {column.sortable && sortable && (
                      <span className="table-sort-indicator">
                        {sortColumn === column.key ? (
                          sortDirection === 'asc' ? '\u2191' : '\u2193'
                        ) : (
                          '\u2195'
                        )}
                      </span>
                    )}
                  </div>
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {data.length === 0 ? (
              <tr>
                <td colSpan={columns.length + (selectable ? 1 : 0)} className="table-empty">
                  {emptyMessage}
                </td>
              </tr>
            ) : (
              data.map((row, index) => {
                const rowKey = String(row[keyField]);
                const isSelected = selectedRows.has(rowKey);

                return (
                  <tr
                    key={rowKey}
                    className={`${isSelected ? 'table-row-selected' : ''} ${onRowClick ? 'table-row-clickable' : ''}`}
                    onClick={() => onRowClick?.(row)}
                  >
                    {selectable && (
                      <td className="table-select-cell" onClick={(e) => e.stopPropagation()}>
                        <input
                          type="checkbox"
                          checked={isSelected}
                          onChange={() => handleSelectRow(row)}
                        />
                      </td>
                    )}
                    {columns.map((column) => (
                      <td
                        key={column.key}
                        className={`table-td-${column.align || 'left'}`}
                      >
                        {getCellValue(column, row, index)}
                      </td>
                    ))}
                  </tr>
                );
              })
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
}
