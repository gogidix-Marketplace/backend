import React, { useState, useMemo } from 'react';
import {
  ChevronUp,
  ChevronDown,
  ChevronsUpDown,
  MoreHorizontal,
  Search,
  Filter,
  Download,
  RefreshCw,
} from 'lucide-react';
import { Card, CardContent } from '../common/Card';
import { Button } from '../common/Button';
import { Input } from '../common/Input';
import { Badge } from '../common/Badge';
import { Select } from '../common/Select';
import { cn } from '../../utils/cn';

export type ColumnSort = { id: string; desc: boolean } | null;

export interface Column<T> {
  id: string;
  header: string;
  accessor?: keyof T | ((row: T) => any);
  cell?: (row: T) => React.ReactNode;
  sortable?: boolean;
  filterable?: boolean;
  width?: number | string;
  className?: string;
}

interface DataTableProps<T> {
  data: T[];
  columns: Column<T>[];
  keyField: keyof T;
  title?: string;
  searchable?: boolean;
  filterable?: boolean;
  sortable?: boolean;
  selectable?: boolean;
  onRowClick?: (row: T) => void;
  onSelectionChange?: (selectedRows: T[]) => void;
  emptyMessage?: string;
  isLoading?: boolean;
  className?: string;
  pagination?: {
    pageSize: number;
    currentPage: number;
    onPageChange: (page: number) => void;
    totalItems?: number;
  };
  onRefresh?: () => void;
  onExport?: () => void;
}

export function DataTable<T extends Record<string, any>>({
  data,
  columns,
  keyField,
  title,
  searchable = true,
  filterable = true,
  sortable = true,
  selectable = false,
  onRowClick,
  onSelectionChange,
  emptyMessage = 'No data available',
  isLoading = false,
  className,
  pagination,
  onRefresh,
  onExport,
}: DataTableProps<T>) {
  const [searchQuery, setSearchQuery] = useState('');
  const [sortColumn, setSortColumn] = useState<string | null>(null);
  const [sortDesc, setSortDesc] = useState(false);
  const [selectedRows, setSelectedRows] = useState<Set<string | number>>(new Set());

  // Filter and sort data
  const processedData = useMemo(() => {
    let result = [...data];

    // Apply search filter
    if (searchQuery) {
      const query = searchQuery.toLowerCase();
      result = result.filter((row) =>
        columns.some((col) => {
          const value = col.accessor
            ? typeof col.accessor === 'function'
              ? col.accessor(row)
              : row[col.accessor]
            : row[col.id];
          return String(value).toLowerCase().includes(query);
        })
      );
    }

    // Apply sorting
    if (sortColumn && sortable) {
      result.sort((a, b) => {
        const column = columns.find((col) => col.id === sortColumn);
        if (!column) return 0;

        const aValue = column.accessor
          ? typeof column.accessor === 'function'
            ? column.accessor(a)
            : a[column.accessor as keyof T]
          : a[sortColumn];

        const bValue = column.accessor
          ? typeof column.accessor === 'function'
            ? column.accessor(b)
            : b[column.accessor as keyof T]
          : b[sortColumn];

        if (aValue === bValue) return 0;
        if (aValue == null) return 1;
        if (bValue == null) return -1;

        const comparison = aValue > bValue ? 1 : -1;
        return sortDesc ? -comparison : comparison;
      });
    }

    return result;
  }, [data, searchQuery, sortColumn, sortDesc, columns, sortable]);

  // Pagination
  const paginatedData = useMemo(() => {
    if (!pagination) return processedData;

    const startIndex = pagination.currentPage * pagination.pageSize;
    return processedData.slice(startIndex, startIndex + pagination.pageSize);
  }, [processedData, pagination]);

  const totalPages = pagination
    ? Math.ceil(
        (pagination.totalItems ?? processedData.length) / pagination.pageSize
      )
    : 1;

  // Handle sort
  const handleSort = (columnId: string) => {
    if (!sortable) return;

    if (sortColumn === columnId) {
      if (sortDesc) {
        setSortColumn(null);
        setSortDesc(false);
      } else {
        setSortDesc(true);
      }
    } else {
      setSortColumn(columnId);
      setSortDesc(false);
    }
  };

  // Handle row selection
  const handleRowSelect = (row: T) => {
    const keyValue = row[keyField];
    const newSelected = new Set(selectedRows);

    if (newSelected.has(keyValue)) {
      newSelected.delete(keyValue);
    } else {
      newSelected.add(keyValue);
    }

    setSelectedRows(newSelected);

    if (onSelectionChange) {
      const selectedData = data.filter((r) => newSelected.has(r[keyField]));
      onSelectionChange(selectedData);
    }
  };

  const handleSelectAll = () => {
    if (selectedRows.size === paginatedData.length) {
      setSelectedRows(new Set());
    } else {
      const newSelected = new Set(paginatedData.map((r) => r[keyField]));
      setSelectedRows(newSelected);
    }
  };

  // Get cell value
  const getCellValue = (row: T, column: Column<T>) => {
    if (column.cell) return column.cell(row);

    const value = column.accessor
      ? typeof column.accessor === 'function'
        ? column.accessor(row)
        : row[column.accessor as keyof T]
      : row[column.id];

    return value;
  };

  if (isLoading) {
    return (
      <Card className={className}>
        <CardContent className="p-6">
          <div className="space-y-4">
            <div className="h-8 w-48 bg-muted animate-pulse rounded" />
            <div className="space-y-2">
              {[1, 2, 3, 4, 5].map((i) => (
                <div key={i} className="h-12 bg-muted animate-pulse rounded" />
              ))}
            </div>
          </div>
        </CardContent>
      </Card>
    );
  }

  return (
    <Card className={className}>
      <div className="p-6 border-b">
        <div className="flex items-center justify-between">
          <div>
            {title && <h3 className="text-lg font-semibold">{title}</h3>}
            <p className="text-sm text-muted-foreground">
              {processedData.length} {processedData.length === 1 ? 'result' : 'results'}
            </p>
          </div>
          <div className="flex gap-2">
            {onRefresh && (
              <Button variant="outline" size="sm" onClick={onRefresh}>
                <RefreshCw className="h-4 w-4" />
              </Button>
            )}
            {onExport && (
              <Button variant="outline" size="sm" onClick={onExport}>
                <Download className="h-4 w-4 mr-2" />
                Export
              </Button>
            )}
          </div>
        </div>

        {searchable && (
          <div className="mt-4">
            <div className="relative max-w-sm">
              <Search className="absolute left-3 top-1/2 -translate-y-1/2 h-4 w-4 text-muted-foreground" />
              <Input
                placeholder="Search..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-10"
              />
            </div>
          </div>
        )}
      </div>

      <div className="overflow-x-auto">
        <table className="w-full">
          <thead className="bg-muted/50">
            <tr>
              {selectable && (
                <th className="p-4 w-12">
                  <input
                    type="checkbox"
                    checked={selectedRows.size === paginatedData.length && paginatedData.length > 0}
                    onChange={handleSelectAll}
                    className="rounded border-input"
                  />
                </th>
              )}
              {columns.map((column) => (
                <th
                  key={column.id}
                  className={cn(
                    'p-4 text-left font-medium text-sm text-muted-foreground whitespace-nowrap',
                    column.sortable && sortable && 'cursor-pointer hover:text-foreground',
                    column.className
                  )}
                  style={{ width: column.width }}
                  onClick={() => column.sortable && handleSort(column.id)}
                >
                  <div className="flex items-center gap-2">
                    {column.header}
                    {column.sortable && sortable && (
                      <span className="flex">
                        {sortColumn === column.id ? (
                          sortDesc ? (
                            <ChevronDown className="h-4 w-4" />
                          ) : (
                            <ChevronUp className="h-4 w-4" />
                          )
                        ) : (
                          <ChevronsUpDown className="h-4 w-4 opacity-50" />
                        )}
                      </span>
                    )}
                  </div>
                </th>
              ))}
            </tr>
          </thead>
          <tbody>
            {paginatedData.length === 0 ? (
              <tr>
                <td
                  colSpan={columns.length + (selectable ? 1 : 0)}
                  className="p-8 text-center text-muted-foreground"
                >
                  {emptyMessage}
                </td>
              </tr>
            ) : (
              paginatedData.map((row, rowIndex) => (
                <tr
                  key={row[keyField] ?? rowIndex}
                  className={cn(
                    'border-t transition-colors',
                    onRowClick && 'cursor-pointer hover:bg-muted/50'
                  )}
                  onClick={() => onRowClick?.(row)}
                >
                  {selectable && (
                    <td className="p-4" onClick={(e) => e.stopPropagation()}>
                      <input
                        type="checkbox"
                        checked={selectedRows.has(row[keyField])}
                        onChange={() => handleRowSelect(row)}
                        className="rounded border-input"
                      />
                    </td>
                  )}
                  {columns.map((column) => (
                    <td
                      key={column.id}
                      className={cn('p-4 text-sm', column.className)}
                    >
                      {getCellValue(row, column)}
                    </td>
                  ))}
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {pagination && totalPages > 1 && (
        <div className="p-4 border-t flex items-center justify-between">
          <p className="text-sm text-muted-foreground">
            Page {pagination.currentPage + 1} of {totalPages}
          </p>
          <div className="flex gap-2">
            <Button
              variant="outline"
              size="sm"
              onClick={() => pagination.onPageChange(pagination.currentPage - 1)}
              disabled={pagination.currentPage === 0}
            >
              Previous
            </Button>
            <Button
              variant="outline"
              size="sm"
              onClick={() => pagination.onPageChange(pagination.currentPage + 1)}
              disabled={pagination.currentPage >= totalPages - 1}
            >
              Next
            </Button>
          </div>
        </div>
      )}
    </Card>
  );
}
