import * as React from 'react'
import { cn } from '@lib/utils'

export interface DataGridProps<T> {
  data: T[]
  columns: DataGridColumn<T>[]
  loading?: boolean
  sortable?: boolean
  filterable?: boolean
  selectable?: boolean
  onSelectionChange?: (selectedRows: T[]) => void
  onRowClick?: (row: T) => void
  pagination?: boolean
  pageSize?: number
  currentPage?: number
  onPageChange?: (page: number) => void
  className?: string
}

export interface DataGridColumn<T> {
  id: string
  header: string
  accessor: keyof T | ((row: T) => React.ReactNode)
  cell?: (row: T) => React.ReactNode
  sortable?: boolean
  filterable?: boolean
  width?: string | number
  align?: 'left' | 'center' | 'right'
  className?: string
}

export function DataGrid<T>({
  data,
  columns,
  loading = false,
  sortable = false,
  filterable = false,
  selectable = false,
  onSelectionChange,
  onRowClick,
  pagination = false,
  pageSize = 10,
  currentPage = 1,
  onPageChange,
  className,
}: DataGridProps<T>) {
  const [sortColumn, setSortColumn] = React.useState<string>('')
  const [sortDirection, setSortDirection] = React.useState<'asc' | 'desc'>('asc')
  const [selectedRows, setSelectedRows] = React.useState<Set<number>>(new Set())

  // Pagination logic
  const totalPages = Math.ceil(data.length / pageSize)
  const startIndex = (currentPage - 1) * pageSize
  const endIndex = startIndex + pageSize
  const paginatedData = pagination ? data.slice(startIndex, endIndex) : data

  // Sorting logic
  const sortedData = React.useMemo(() => {
    if (!sortColumn || !sortable) return paginatedData

    return [...paginatedData].sort((a, b) => {
      const column = columns.find((col) => col.id === sortColumn)
      if (!column) return 0

      const aValue = getCellValue(a, column)
      const bValue = getCellValue(b, column)

      if (aValue < bValue) return sortDirection === 'asc' ? -1 : 1
      if (aValue > bValue) return sortDirection === 'asc' ? 1 : -1
      return 0
    })
  }, [paginatedData, sortColumn, sortDirection, columns, sortable])

  const handleSort = (columnId: string) => {
    if (!sortable) return

    const column = columns.find((col) => col.id === columnId)
    if (!column || !column.sortable) return

    if (sortColumn === columnId) {
      setSortDirection(sortDirection === 'asc' ? 'desc' : 'asc')
    } else {
      setSortColumn(columnId)
      setSortDirection('asc')
    }
  }

  const handleSelectAll = (checked: boolean) => {
    if (checked) {
      const allIndices = new Set(sortedData.map((_, index) => startIndex + index))
      setSelectedRows(allIndices)
      onSelectionChange?.(sortedData)
    } else {
      setSelectedRows(new Set())
      onSelectionChange?.([])
    }
  }

  const handleSelectRow = (index: number, checked: boolean) => {
    const newSelected = new Set(selectedRows)
    const actualIndex = startIndex + index

    if (checked) {
      newSelected.add(actualIndex)
    } else {
      newSelected.delete(actualIndex)
    }

    setSelectedRows(newSelected)
    const selectedData = sortedData.filter((_, i) => newSelected.has(startIndex + i))
    onSelectionChange?.(selectedData)
  }

  const isAllSelected = selectedRows.size > 0 && selectedRows.size === sortedData.length
  const isSomeSelected = selectedRows.size > 0 && selectedRows.size < sortedData.length

  return (
    <div className={cn('w-full', className)}>
      {/* Table Container */}
      <div className="overflow-x-auto rounded-lg border border-gray-200">
        <table className="min-w-full divide-y divide-gray-200 bg-white">
          {/* Header */}
          <thead className="bg-gray-50">
            <tr>
              {selectable && (
                <th className="px-4 py-3 w-12">
                  <input
                    type="checkbox"
                    checked={isAllSelected}
                    ref={(input) => {
                      if (isSomeSelected && input) {
                        input.indeterminate = true
                      }
                    }}
                    onChange={(e) => handleSelectAll(e.target.checked)}
                    className="h-4 w-4 rounded border-gray-300 text-primary-600 focus:ring-primary-500"
                  />
                </th>
              )}
              {columns.map((column) => (
                <th
                  key={column.id}
                  onClick={() => handleSort(column.id)}
                  className={cn(
                    'px-4 py-3 text-left text-xs font-medium uppercase text-gray-500',
                    column.sortable && sortable && 'cursor-pointer hover:bg-gray-100',
                    column.align === 'center' && 'text-center',
                    column.align === 'right' && 'text-right',
                    column.className
                  )}
                  style={{ width: column.width }}
                >
                  <div className="flex items-center gap-2">
                    {column.header}
                    {sortable && column.sortable && (
                      <span className="text-gray-400">
                        {sortColumn === column.id ? (
                          sortDirection === 'asc' ? (
                            '↑'
                          ) : (
                            '↓'
                          )
                        ) : (
                          <span className="text-gray-300">↕</span>
                        )}
                      </span>
                    )}
                  </div>
                </th>
              ))}
            </tr>
          </thead>

          {/* Body */}
          <tbody className="divide-y divide-gray-200">
            {loading ? (
              <tr>
                <td
                  colSpan={columns.length + (selectable ? 1 : 0)}
                  className="px-4 py-8 text-center text-gray-500"
                >
                  Loading...
                </td>
              </tr>
            ) : sortedData.length === 0 ? (
              <tr>
                <td
                  colSpan={columns.length + (selectable ? 1 : 0)}
                  className="px-4 py-8 text-center text-gray-500"
                >
                  No data available
                </td>
              </tr>
            ) : (
              sortedData.map((row, index) => {
                const isSelected = selectedRows.has(startIndex + index)
                return (
                  <tr
                    key={startIndex + index}
                    className={cn(
                      'transition-colors',
                      onRowClick && 'cursor-pointer hover:bg-gray-50',
                      isSelected && 'bg-blue-50'
                    )}
                    onClick={() => onRowClick?.(row)}
                  >
                    {selectable && (
                      <td className="px-4 py-3">
                        <input
                          type="checkbox"
                          checked={isSelected}
                          onChange={(e) => {
                            e.stopPropagation()
                            handleSelectRow(index, e.target.checked)
                          }}
                          className="h-4 w-4 rounded border-gray-300 text-primary-600 focus:ring-primary-500"
                        />
                      </td>
                    )}
                    {columns.map((column) => {
                      const value = getCellValue(row, column)
                      return (
                        <td
                          key={column.id}
                          className={cn(
                            'px-4 py-3 text-sm text-gray-900',
                            column.align === 'center' && 'text-center',
                            column.align === 'right' && 'text-right',
                            column.className
                          )}
                        >
                          {column.cell ? column.cell(row) : value}
                        </td>
                      )
                    })}
                  </tr>
                )
              })
            )}
          </tbody>
        </table>
      </div>

      {/* Pagination */}
      {pagination && totalPages > 1 && (
        <div className="flex items-center justify-between px-4 py-3 border-t border-gray-200">
          <div className="text-sm text-gray-700">
            Showing {startIndex + 1} to {Math.min(endIndex, data.length)} of {data.length} entries
          </div>
          <div className="flex items-center gap-2">
            <button
              onClick={() => onPageChange?.(currentPage - 1)}
              disabled={currentPage === 1}
              className="px-3 py-1 text-sm border rounded hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              Previous
            </button>
            <span className="text-sm">
              Page {currentPage} of {totalPages}
            </span>
            <button
              onClick={() => onPageChange?.(currentPage + 1)}
              disabled={currentPage === totalPages}
              className="px-3 py-1 text-sm border rounded hover:bg-gray-50 disabled:opacity-50 disabled:cursor-not-allowed"
            >
              Next
            </button>
          </div>
        </div>
      )}
    </div>
  )
}

function getCellValue<T>(row: T, column: DataGridColumn<T>): React.ReactNode {
  if (typeof column.accessor === 'function') {
    return column.accessor(row)
  }
  return row[column.accessor] as React.ReactNode
}
