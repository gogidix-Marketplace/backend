import React from 'react';
import {
  Box,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  TablePagination,
  Checkbox,
  Toolbar,
  Typography,
  IconButton,
  Tooltip,
} from '@mui/material';
import {
  Delete as DeleteIcon,
  Edit as EditIcon,
  Visibility as ViewIcon,
} from '@mui/icons-material';

export interface Column {
  id: string;
  label: string;
  width?: number | string;
  align?: 'left' | 'right' | 'center';
  render?: (value: any, row: any) => React.ReactNode;
}

interface DataTableProps {
  columns: Column[];
  rows: any[];
  total?: number;
  page?: number;
  rowsPerPage?: number;
  onPageChange?: (page: number) => void;
  onRowsPerPageChange?: (rowsPerPage: number) => void;
  selectable?: boolean;
  selected?: string[];
  onSelectionChange?: (selected: string[]) => void;
  onEdit?: (row: any) => void;
  onDelete?: (row: any) => void;
  onView?: (row: any) => void;
  loading?: boolean;
  emptyMessage?: string;
}

const DataTable: React.FC<DataTableProps> = ({
  columns,
  rows,
  total = rows.length,
  page = 0,
  rowsPerPage = 10,
  onPageChange,
  onRowsPerPageChange,
  selectable = false,
  selected = [],
  onSelectionChange,
  onEdit,
  onDelete,
  onView,
  loading = false,
  emptyMessage = 'No data found',
}) => {
  const handleSelectAllClick = (event: React.ChangeEvent<HTMLInputElement>) => {
    if (event.target.checked) {
      const newSelected = rows.map((row) => row.id);
      onSelectionChange?.(newSelected);
    } else {
      onSelectionChange?.([]);
    }
  };

  const handleClick = (event: React.MouseEvent<unknown>, id: string) => {
    const selectedIndex = selected.indexOf(id);
    let newSelected: string[] = [];

    if (selectedIndex === -1) {
      newSelected = newSelected.concat(selected, id);
    } else if (selectedIndex === 0) {
      newSelected = newSelected.concat(selected.slice(1));
    } else if (selectedIndex === selected.length - 1) {
      newSelected = newSelected.concat(selected.slice(0, -1));
    } else if (selectedIndex > 0) {
      newSelected = newSelected.concat(
        selected.slice(0, selectedIndex),
        selected.slice(selectedIndex + 1)
      );
    }

    onSelectionChange?.(newSelected);
  };

  const isSelected = (id: string) => selected.indexOf(id) !== -1;

  return (
    <Paper>
      {selected.length > 0 && (
        <Toolbar
          sx={{
            pl: { sm: 2 },
            pr: { xs: 1, sm: 1 },
            bgcolor: 'action.selected',
          }}
        >
          <Typography sx={{ flex: '1 1 100%' }} color="inherit" variant="subtitle1">
            {selected.length} selected
          </Typography>
          {onDelete && selected.length === 1 && (
            <Tooltip title="Delete">
              <IconButton onClick={() => onDelete(rows.find((r) => r.id === selected[0]))}>
                <DeleteIcon />
              </IconButton>
            </Tooltip>
          )}
        </Toolbar>
      )}
      <TableContainer>
        <Table stickyHeader>
          <TableHead>
            <TableRow>
              {selectable && (
                <TableCell padding="checkbox">
                  <Checkbox
                    indeterminate={
                      selected.length > 0 && selected.length < rows.length
                    }
                    checked={
                      rows.length > 0 && selected.length === rows.length
                    }
                    onChange={handleSelectAllClick}
                  />
                </TableCell>
              )}
              {columns.map((column) => (
                <TableCell
                  key={column.id}
                  align={column.align || 'left'}
                  style={{ width: column.width }}
                >
                  {column.label}
                </TableCell>
              ))}
              {(onEdit || onDelete || onView) && (
                <TableCell align="right">Actions</TableCell>
              )}
            </TableRow>
          </TableHead>
          <TableBody>
            {loading ? (
              <TableRow>
                <TableCell
                  colSpan={
                    columns.length +
                    (selectable ? 1 : 0) +
                    (onEdit || onDelete || onView ? 1 : 0)
                  }
                  align="center"
                >
                  <Typography variant="body2" color="text.secondary">
                    Loading...
                  </Typography>
                </TableCell>
              </TableRow>
            ) : rows.length === 0 ? (
              <TableRow>
                <TableCell
                  colSpan={
                    columns.length +
                    (selectable ? 1 : 0) +
                    (onEdit || onDelete || onView ? 1 : 0)
                  }
                  align="center"
                >
                  <Typography variant="body2" color="text.secondary">
                    {emptyMessage}
                  </Typography>
                </TableCell>
              </TableRow>
            ) : (
              rows.map((row) => {
                const isItemSelected = selectable ? isSelected(row.id) : false;
                return (
                  <TableRow
                    key={row.id}
                    hover
                    onClick={(event) => selectable && handleClick(event, row.id)}
                    role={selectable ? 'checkbox' : undefined}
                    aria-checked={isItemSelected}
                    selected={isItemSelected}
                    sx={{ cursor: selectable ? 'pointer' : 'default' }}
                  >
                    {selectable && (
                      <TableCell padding="checkbox">
                        <Checkbox checked={isItemSelected} />
                      </TableCell>
                    )}
                    {columns.map((column) => (
                      <TableCell key={column.id} align={column.align || 'left'}>
                        {column.render
                          ? column.render(row[column.id], row)
                          : row[column.id]}
                      </TableCell>
                    ))}
                    {(onEdit || onDelete || onView) && (
                      <TableCell align="right">
                        {onView && (
                          <Tooltip title="View">
                            <IconButton
                              size="small"
                              onClick={(e) => {
                                e.stopPropagation();
                                onView(row);
                              }}
                            >
                              <ViewIcon fontSize="small" />
                            </IconButton>
                          </Tooltip>
                        )}
                        {onEdit && (
                          <Tooltip title="Edit">
                            <IconButton
                              size="small"
                              onClick={(e) => {
                                e.stopPropagation();
                                onEdit(row);
                              }}
                            >
                              <EditIcon fontSize="small" />
                            </IconButton>
                          </Tooltip>
                        )}
                        {onDelete && (
                          <Tooltip title="Delete">
                            <IconButton
                              size="small"
                              color="error"
                              onClick={(e) => {
                                e.stopPropagation();
                                if (
                                  window.confirm(
                                    'Are you sure you want to delete this item?'
                                  )
                                ) {
                                  onDelete(row);
                                }
                              }}
                            >
                              <DeleteIcon fontSize="small" />
                            </IconButton>
                          </Tooltip>
                        )}
                      </TableCell>
                    )}
                  </TableRow>
                );
              })
            )}
          </TableBody>
        </Table>
      </TableContainer>
      {onPageChange && (
        <TablePagination
          rowsPerPageOptions={[5, 10, 25, 50]}
          component="div"
          count={total}
          rowsPerPage={rowsPerPage}
          page={page}
          onPageChange={(_, newPage) => onPageChange(newPage)}
          onRowsPerPageChange={(e) => {
            onRowsPerPageChange(parseInt(e.target.value, 10));
          }}
        />
      )}
    </Paper>
  );
};

export default DataTable;
