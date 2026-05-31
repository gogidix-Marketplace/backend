// Table Component
// Reusable table component with sorting and filtering

import React, { useState } from 'react';
import './Table.css';

export type SortDirection = 'asc' | 'desc' | null;

export interface Column<T> {
  key: keyof T | string;
  title: string;
  render?: (value: any, row: T, index: number) => React.ReactNode;
  sortable?: boolean;
  width?: string;
  align?: 'left' | 'center' | 'right';
}

export interface TableProps<T> {
  columns: Column<T>[];
  data: T[];
  keyField: keyof T;
  onRowClick?: (row: T) => void;
  sortable?: boolean;
  className?: string;
  emptyMessage?: string;
  loading?: boolean;
  selectable?: boolean;
  onSelectionChange?: (selectedRows: T[]) => void;
}

export function Table<T extends Record<string, any>>({
  columns,
  data,
  keyField,
  onRowClick,
  sortable = true,
  className = '',
  emptyMessage = 'No data available',
  loading = false,
  selectable = false,
  onSelectionChange,
}: TableProps<T>) {
  const [sortColumn, setSortColumn] = useState<string>('');
  const [sortDirection, setSortDirection] = useState<SortDirection>(null);
  const [selectedRows, setSelectedRows] = useState<Set<string | number>>(new Set());

  const handleSort = (columnKey: string) => {
    if (!sortable) return;

    const column = columns.find((c) => c.key === columnKey);
    if (!column || !column.sortable) return;

    let newDirection: SortDirection = 'asc';
    if (sortColumn === columnKey) {
      if (sortDirection === 'asc') {
        newDirection = 'desc';
      } else if (sortDirection === 'desc') {
        newDirection = null;
      }
    }

    setSortColumn(columnKey);
    setSortDirection(newDirection);
  };

  const getSortedData = () => {
    if (!sortColumn || !sortDirection) {
      return data;
    }

    return [...data].sort((a, b) => {
      const aVal = a[sortColumn];
      const bVal = b[sortColumn];

      if (aVal === undefined || aVal === null) return 1;
      if (bVal === undefined || bVal === null) return -1;

      if (typeof aVal === 'number' && typeof bVal === 'number') {
        return sortDirection === 'asc' ? aVal - bVal : bVal - aVal;
      }

      const aStr = String(aVal).toLowerCase();
      const bStr = String(bVal).toLowerCase();

      if (sortDirection === 'asc') {
        return aStr.localeCompare(bStr);
      } else {
        return bStr.localeCompare(aStr);
      }
    });
  };

  const handleSelectRow = (row: T) => {
    const keyValue = row[keyField];
    const newSelection = new Set(selectedRows);

    if (newSelection.has(keyValue)) {
      newSelection.delete(keyValue);
    } else {
      newSelection.add(keyValue);
    }

    setSelectedRows(newSelection);

    if (onSelectionChange) {
      const selectedData = data.filter((r) => newSelection.has(r[keyField]));
      onSelectionChange(selectedData);
    }
  };

  const handleSelectAll = () => {
    if (selectedRows.size === data.length) {
      setSelectedRows(new Set());
      onSelectionChange?.([]);
    } else {
      const allKeys = new Set(data.map((r) => r[keyField]));
      setSelectedRows(allKeys);
      onSelectionChange?.([...data]);
    }
  };

  const sortedData = getSortedData();

  return (
    <div className={`table-container ${className}`}>
      <div className="table-wrapper">
        <table className="table">
          <thead>
            <tr>
              {selectable && (
                <th className="table__checkbox-cell">
                  <input
                    type="checkbox"
                    checked={selectedRows.size === data.length && data.length > 0}
                    onChange={handleSelectAll}
                  />
                </th>
              )}
              {columns.map((column) => (
                <th
                  key={String(column.key)}
                  style={{ width: column.width, textAlign: column.align }}
                  className={
                    sortable && column.sortable
                      ? 'table__sortable-header'
                      : 'table__header'
                  }
                  onClick={() => handleSort(String(column.key))}
                >
                  <div className="table__header-content">
                    <span>{column.title}</span>
                    {sortable && column.sortable && (
                      <span className="table__sort-icon">
                        {sortColumn === column.key ? (
                          sortDirection === 'asc' ? (
                            '↑'
                          ) : sortDirection === 'desc' ? (
                            '↓'
                          ) : (
                            '↕'
                          )
                        ) : (
                          '↕'
                        )}
                      </span>
                    )}
                  </div>
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {loading ? (
              <tr>
                <td
                  colSpan={columns.length + (selectable ? 1 : 0)}
                  className="table__loading-cell"
                >
                  <div className="table__loading">
                    <div className="table__spinner"></div>
                    <span>Loading...</span>
                  </div>
                </td>
              </tr>
            ) : sortedData.length === 0 ? (
              <tr>
                <td
                  colSpan={columns.length + (selectable ? 1 : 0)}
                  className="table__empty-cell"
                >
                  {emptyMessage}
                </td>
              </tr>
            ) : (
              sortedData.map((row, rowIndex) => {
                const keyValue = row[keyField];
                const isSelected = selectedRows.has(keyValue);

                return (
                  <tr
                    key={keyValue as string}
                    className={`${onRowClick ? 'table__clickable-row' : ''} ${
                      isSelected ? 'table__selected-row' : ''
                    }`}
                    onClick={() => onRowClick?.(row)}
                  >
                    {selectable && (
                      <td className="table__checkbox-cell">
                        <input
                          type="checkbox"
                          checked={isSelected}
                          onChange={() => handleSelectRow(row)}
                          onClick={(e) => e.stopPropagation()}
                        />
                      </td>
                    )}
                    {columns.map((column) => (
                      <td
                        key={String(column.key)}
                        style={{ textAlign: column.align }}
                      >
                        {column.render
                          ? column.render(row[column.key as keyof T], row, rowIndex)
                          : String(row[column.key as keyof T] ?? '')}
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

export default Table;
