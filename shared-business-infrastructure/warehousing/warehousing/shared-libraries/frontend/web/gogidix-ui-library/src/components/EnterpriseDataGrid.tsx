import React, { useState, useMemo, useCallback } from 'react'
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TablePagination,
  TableSortLabel,
  Paper,
  TextField,
  Box,
  Chip,
  IconButton,
  Menu,
  MenuItem,
  Button,
  Typography,
  Checkbox,
  FormControlLabel,
  CircularProgress,
  Alert,
  Toolbar,
} from '@mui/material'
import {
  FilterList as FilterIcon,
  Download as ExportIcon,
  Refresh as RefreshIcon,
  ViewColumn as ColumnsIcon,
  Search as SearchIcon,
} from '@mui/icons-material'

export interface DataGridColumn<T = any> {
  id: keyof T
  label: string
  sortable?: boolean
  filterable?: boolean
  width?: number
  align?: 'left' | 'right' | 'center'
  format?: (value: any) => string | React.ReactNode
  render?: (value: any, row: T) => React.ReactNode
}

export interface DataGridFilter {
  column: string
  operator: 'equals' | 'contains' | 'startsWith' | 'endsWith' | 'gt' | 'lt' | 'between'
  value: any
  value2?: any // for between operator
}

export interface DataGridSort {
  column: string
  direction: 'asc' | 'desc'
}

export interface EnterpriseDataGridProps<T = any> {
  data: T[]
  columns: DataGridColumn<T>[]
  loading?: boolean
  error?: string
  totalCount?: number
  pageSize?: number
  page?: number
  sortBy?: DataGridSort
  filters?: DataGridFilter[]
  searchQuery?: string
  selectable?: boolean
  selectedRows?: Set<string | number>
  rowIdField?: keyof T
  
  // Callbacks
  onPageChange?: (page: number) => void
  onPageSizeChange?: (pageSize: number) => void
  onSortChange?: (sort: DataGridSort | null) => void
  onFiltersChange?: (filters: DataGridFilter[]) => void
  onSearchChange?: (query: string) => void
  onRowSelect?: (selectedRows: Set<string | number>) => void
  onExport?: (format: 'csv' | 'excel' | 'pdf') => void
  onRefresh?: () => void
  
  // Customization
  title?: string
  toolbar?: boolean
  pagination?: boolean
  density?: 'compact' | 'standard' | 'comfortable'
  stickyHeader?: boolean
  maxHeight?: number
  
  // Actions
  rowActions?: (row: T) => React.ReactNode
  bulkActions?: (selectedRows: Set<string | number>) => React.ReactNode
}

export const EnterpriseDataGrid = <T extends Record<string, any>>({
  data = [],
  columns = [],
  loading = false,
  error,
  totalCount,
  pageSize = 10,
  page = 0,
  sortBy,
  filters = [],
  searchQuery = '',
  selectable = false,
  selectedRows = new Set(),
  rowIdField = 'id' as keyof T,
  
  onPageChange,
  onPageSizeChange,
  onSortChange,
  onFiltersChange,
  onSearchChange,
  onRowSelect,
  onExport,
  onRefresh,
  
  title,
  toolbar = true,
  pagination = true,
  density = 'standard',
  stickyHeader = false,
  maxHeight,
  
  rowActions,
  bulkActions,
}: EnterpriseDataGridProps<T>) => {
  // Local state for UI controls
  const [columnsMenuAnchor, setColumnsMenuAnchor] = useState<null | HTMLElement>(null)
  const [visibleColumns, setVisibleColumns] = useState<Set<string>>(
    new Set(columns.map(col => String(col.id)))
  )
  const [localSearchQuery, setLocalSearchQuery] = useState(searchQuery)
  const [exportMenuAnchor, setExportMenuAnchor] = useState<null | HTMLElement>(null)

  // Memoized filtered columns
  const filteredColumns = useMemo(() => 
    columns.filter(col => visibleColumns.has(String(col.id))),
    [columns, visibleColumns]
  )

  // Row selection handlers
  const handleSelectAll = useCallback((event: React.ChangeEvent<HTMLInputElement>) => {
    if (!onRowSelect) return
    
    if (event.target.checked) {
      const allIds = new Set(data.map(row => row[rowIdField]))
      onRowSelect(allIds)
    } else {
      onRowSelect(new Set())
    }
  }, [data, rowIdField, onRowSelect])

  const handleRowSelect = useCallback((rowId: string | number) => {
    if (!onRowSelect) return
    
    const newSelection = new Set(selectedRows)
    if (newSelection.has(rowId)) {
      newSelection.delete(rowId)
    } else {
      newSelection.add(rowId)
    }
    onRowSelect(newSelection)
  }, [selectedRows, onRowSelect])

  // Sort handler
  const handleSort = useCallback((column: keyof T) => {
    if (!onSortChange) return
    
    let newSort: DataGridSort | null = null
    if (sortBy?.column === column) {
      if (sortBy.direction === 'asc') {
        newSort = { column: String(column), direction: 'desc' }
      } else {
        newSort = null // Remove sort
      }
    } else {
      newSort = { column: String(column), direction: 'asc' }
    }
    
    onSortChange(newSort)
  }, [sortBy, onSortChange])

  // Search handler with debouncing
  const handleSearchChange = useCallback((event: React.ChangeEvent<HTMLInputElement>) => {
    const value = event.target.value
    setLocalSearchQuery(value)
    
    // Simple debounce
    const timeoutId = setTimeout(() => {
      onSearchChange?.(value)
    }, 300)
    
    return () => clearTimeout(timeoutId)
  }, [onSearchChange])

  // Column visibility toggle
  const handleColumnToggle = useCallback((columnId: string) => {
    const newVisible = new Set(visibleColumns)
    if (newVisible.has(columnId)) {
      if (newVisible.size > 1) { // Don't allow hiding all columns
        newVisible.delete(columnId)
      }
    } else {
      newVisible.add(columnId)
    }
    setVisibleColumns(newVisible)
  }, [visibleColumns])

  // Export handlers
  const handleExport = useCallback((format: 'csv' | 'excel' | 'pdf') => {
    onExport?.(format)
    setExportMenuAnchor(null)
  }, [onExport])

  // Density styles
  const getCellPadding = () => {
    switch (density) {
      case 'compact': return '4px 8px'
      case 'comfortable': return '12px 16px'
      default: return '8px 16px'
    }
  }

  const isAllSelected = selectedRows.size > 0 && selectedRows.size === data.length
  const isIndeterminate = selectedRows.size > 0 && selectedRows.size < data.length

  return (
    <Paper sx={{ width: '100%', overflow: 'hidden' }}>
      {/* Toolbar */}
      {toolbar && (
        <Toolbar sx={{ px: 2, py: 1, bgcolor: 'background.default' }}>
          <Box sx={{ display: 'flex', alignItems: 'center', flexGrow: 1 }}>
            {title && (
              <Typography variant="h6" sx={{ mr: 2 }}>
                {title}
              </Typography>
            )}
            
            {selectedRows.size > 0 && (
              <Box sx={{ display: 'flex', alignItems: 'center', mr: 2 }}>
                <Typography variant="body2" sx={{ mr: 1 }}>
                  {selectedRows.size} selected
                </Typography>
                {bulkActions && bulkActions(selectedRows)}
              </Box>
            )}
          </Box>

          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
            {/* Search */}
            <TextField
              size="small"
              placeholder="Search..."
              value={localSearchQuery}
              onChange={handleSearchChange}
              InputProps={{
                startAdornment: <SearchIcon sx={{ mr: 1, color: 'text.secondary' }} />,
              }}
              sx={{ width: 250 }}
            />

            {/* Refresh */}
            {onRefresh && (
              <IconButton onClick={onRefresh} title="Refresh">
                <RefreshIcon />
              </IconButton>
            )}

            {/* Export */}
            {onExport && (
              <>
                <IconButton
                  onClick={(event) => setExportMenuAnchor(event.currentTarget)}
                  title="Export"
                >
                  <ExportIcon />
                </IconButton>
                <Menu
                  anchorEl={exportMenuAnchor}
                  open={Boolean(exportMenuAnchor)}
                  onClose={() => setExportMenuAnchor(null)}
                >
                  <MenuItem onClick={() => handleExport('csv')}>Export CSV</MenuItem>
                  <MenuItem onClick={() => handleExport('excel')}>Export Excel</MenuItem>
                  <MenuItem onClick={() => handleExport('pdf')}>Export PDF</MenuItem>
                </Menu>
              </>
            )}

            {/* Column Visibility */}
            <IconButton
              onClick={(event) => setColumnsMenuAnchor(event.currentTarget)}
              title="Columns"
            >
              <ColumnsIcon />
            </IconButton>
            <Menu
              anchorEl={columnsMenuAnchor}
              open={Boolean(columnsMenuAnchor)}
              onClose={() => setColumnsMenuAnchor(null)}
              PaperProps={{ sx: { maxHeight: 300 } }}
            >
              {columns.map((column) => (
                <MenuItem key={String(column.id)}>
                  <FormControlLabel
                    control={
                      <Checkbox
                        checked={visibleColumns.has(String(column.id))}
                        onChange={() => handleColumnToggle(String(column.id))}
                        size="small"
                      />
                    }
                    label={column.label}
                    sx={{ m: 0 }}
                  />
                </MenuItem>
              ))}
            </Menu>
          </Box>
        </Toolbar>
      )}

      {/* Error Display */}
      {error && (
        <Alert severity="error" sx={{ m: 2 }}>
          {error}
        </Alert>
      )}

      {/* Table Container */}
      <TableContainer sx={{ maxHeight: maxHeight || 'none' }}>
        <Table stickyHeader={stickyHeader} size={density}>
          {/* Table Head */}
          <TableHead>
            <TableRow>
              {selectable && (
                <TableCell padding="checkbox" sx={{ padding: getCellPadding() }}>
                  <Checkbox
                    indeterminate={isIndeterminate}
                    checked={isAllSelected}
                    onChange={handleSelectAll}
                    disabled={loading || data.length === 0}
                  />
                </TableCell>
              )}
              
              {filteredColumns.map((column) => (
                <TableCell
                  key={String(column.id)}
                  align={column.align || 'left'}
                  sx={{
                    padding: getCellPadding(),
                    width: column.width,
                    fontWeight: 600,
                    bgcolor: 'background.paper',
                  }}
                >
                  {column.sortable && onSortChange ? (
                    <TableSortLabel
                      active={sortBy?.column === String(column.id)}
                      direction={sortBy?.direction || 'asc'}
                      onClick={() => handleSort(column.id)}
                    >
                      {column.label}
                    </TableSortLabel>
                  ) : (
                    column.label
                  )}
                </TableCell>
              ))}
              
              {rowActions && (
                <TableCell align="right" sx={{ padding: getCellPadding(), fontWeight: 600 }}>
                  Actions
                </TableCell>
              )}
            </TableRow>
          </TableHead>

          {/* Table Body */}
          <TableBody>
            {loading ? (
              <TableRow>
                <TableCell
                  colSpan={filteredColumns.length + (selectable ? 1 : 0) + (rowActions ? 1 : 0)}
                  align="center"
                  sx={{ py: 4 }}
                >
                  <CircularProgress />
                </TableCell>
              </TableRow>
            ) : data.length === 0 ? (
              <TableRow>
                <TableCell
                  colSpan={filteredColumns.length + (selectable ? 1 : 0) + (rowActions ? 1 : 0)}
                  align="center"
                  sx={{ py: 4 }}
                >
                  <Typography color="text.secondary">
                    No data available
                  </Typography>
                </TableCell>
              </TableRow>
            ) : (
              data.map((row, index) => {
                const rowId = row[rowIdField]
                const isSelected = selectedRows.has(rowId)
                
                return (
                  <TableRow
                    key={rowId || index}
                    hover
                    selected={isSelected}
                    sx={{
                      '&:hover': {
                        bgcolor: 'action.hover',
                      },
                    }}
                  >
                    {selectable && (
                      <TableCell padding="checkbox" sx={{ padding: getCellPadding() }}>
                        <Checkbox
                          checked={isSelected}
                          onChange={() => handleRowSelect(rowId)}
                        />
                      </TableCell>
                    )}
                    
                    {filteredColumns.map((column) => {
                      const value = row[column.id]
                      
                      return (
                        <TableCell
                          key={String(column.id)}
                          align={column.align || 'left'}
                          sx={{ padding: getCellPadding() }}
                        >
                          {column.render
                            ? column.render(value, row)
                            : column.format
                            ? column.format(value)
                            : value
                          }
                        </TableCell>
                      )
                    })}
                    
                    {rowActions && (
                      <TableCell align="right" sx={{ padding: getCellPadding() }}>
                        {rowActions(row)}
                      </TableCell>
                    )}
                  </TableRow>
                )
              })
            )}
          </TableBody>
        </Table>
      </TableContainer>

      {/* Pagination */}
      {pagination && (
        <TablePagination
          component="div"
          count={totalCount || data.length}
          page={page}
          onPageChange={(_, newPage) => onPageChange?.(newPage)}
          rowsPerPage={pageSize}
          onRowsPerPageChange={(event) => onPageSizeChange?.(parseInt(event.target.value, 10))}
          rowsPerPageOptions={[5, 10, 25, 50, 100]}
          showFirstButton
          showLastButton
        />
      )}
    </Paper>
  )
}

// Default export and type exports
export default EnterpriseDataGrid
export type { DataGridColumn, DataGridFilter, DataGridSort }