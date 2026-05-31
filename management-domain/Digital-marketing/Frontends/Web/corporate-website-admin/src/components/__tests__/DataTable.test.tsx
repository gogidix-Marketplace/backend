import { render, screen, within } from '@testing-library/react';
import DataTable from '../common/DataTable';

describe('DataTable', () => {
  const mockColumns = [
    { id: 'name', label: 'Name' },
    { id: 'email', label: 'Email' },
    { id: 'role', label: 'Role' },
  ];

  const mockRows = [
    { id: '1', name: 'John Doe', email: 'john@example.com', role: 'Admin' },
    { id: '2', name: 'Jane Smith', email: 'jane@example.com', role: 'User' },
  ];

  it('renders table with columns and rows', () => {
    render(
      <DataTable
        columns={mockColumns}
        rows={mockRows}
        total={2}
      />
    );

    expect(screen.getByText('Name')).toBeInTheDocument();
    expect(screen.getByText('Email')).toBeInTheDocument();
    expect(screen.getByText('Role')).toBeInTheDocument();
    expect(screen.getByText('John Doe')).toBeInTheDocument();
    expect(screen.getByText('Jane Smith')).toBeInTheDocument();
  });

  it('renders loading state', () => {
    render(
      <DataTable
        columns={mockColumns}
        rows={[]}
        loading
      />
    );

    expect(screen.getByText('Loading...')).toBeInTheDocument();
  });

  it('renders empty message when no rows', () => {
    render(
      <DataTable
        columns={mockColumns}
        rows={[]}
        emptyMessage="No data available"
      />
    );

    expect(screen.getByText('No data available')).toBeInTheDocument();
  });

  it('calls onEdit when edit button clicked', () => {
    const mockOnEdit = vi.fn();

    render(
      <DataTable
        columns={mockColumns}
        rows={mockRows}
        total={2}
        onEdit={mockOnEdit}
      />
    );

    const firstRow = screen.getByText('John Doe').closest('tr');
    const editButton = within(firstRow!).getAllByRole('button')[0];

    editButton.click();
    expect(mockOnEdit).toHaveBeenCalledWith(mockRows[0]);
  });
});
