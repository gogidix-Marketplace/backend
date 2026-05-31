import { render, screen } from '@testing-library/react';
import { Card, CardHeader, CardTitle, CardDescription, CardContent, CardFooter } from '../ui/card';

describe('Card Components', () => {
  it('renders Card component', () => {
    const { container } = render(<Card>Card content</Card>);
    expect(container.querySelector('.rounded-lg')).toBeInTheDocument();
  });

  it('renders CardHeader', () => {
    render(<CardHeader>Header content</CardHeader>);
    expect(screen.getByText('Header content')).toBeInTheDocument();
  });

  it('renders CardTitle', () => {
    render(<CardTitle>Test Title</CardTitle>);
    expect(screen.getByText('Test Title')).toBeInTheDocument();
  });

  it('renders CardDescription', () => {
    render(<CardDescription>Test Description</CardDescription>);
    expect(screen.getByText('Test Description')).toHaveClass('text-sm');
  });

  it('renders CardContent', () => {
    render(<CardContent>Content</CardContent>);
    expect(screen.getByText('Content')).toBeInTheDocument();
  });

  it('renders CardFooter', () => {
    const { container } = render(<CardFooter>Footer</CardFooter>);
    expect(container.querySelector('.items-center')).toBeInTheDocument();
  });

  it('renders complete card structure', () => {
    render(
      <Card>
        <CardHeader>
          <CardTitle>Title</CardTitle>
          <CardDescription>Description</CardDescription>
        </CardHeader>
        <CardContent>Content</CardContent>
        <CardFooter>Footer</CardFooter>
      </Card>
    );

    expect(screen.getByText('Title')).toBeInTheDocument();
    expect(screen.getByText('Description')).toBeInTheDocument();
    expect(screen.getByText('Content')).toBeInTheDocument();
    expect(screen.getByText('Footer')).toBeInTheDocument();
  });
});
