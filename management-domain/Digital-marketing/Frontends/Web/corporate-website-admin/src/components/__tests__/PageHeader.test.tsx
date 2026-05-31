import { render, screen } from '@testing-library/react';
import PageHeader from '../common/PageHeader';

describe('PageHeader', () => {
  it('renders title and subtitle', () => {
    render(
      <PageHeader
        title="Test Title"
        subtitle="Test Subtitle"
      />
    );

    expect(screen.getByText('Test Title')).toBeInTheDocument();
    expect(screen.getByText('Test Subtitle')).toBeInTheDocument();
  });

  it('renders action button when provided', () => {
    const mockAction = {
      label: 'Add Item',
      onClick: vi.fn(),
      variant: 'contained' as const,
    };

    render(
      <PageHeader
        title="Test Title"
        action={mockAction}
      />
    );

    const button = screen.getByText('Add Item');
    expect(button).toBeInTheDocument();

    button.click();
    expect(mockAction.onClick).toHaveBeenCalledTimes(1);
  });

  it('does not render action button when not provided', () => {
    render(
      <PageHeader title="Test Title" />
    );

    expect(screen.queryByText('Add Item')).not.toBeInTheDocument();
  });
});
