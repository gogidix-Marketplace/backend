/**
 * Tests for StatCard component
 * Tests cover rendering, props handling, and conditional rendering
 */

import { render, screen } from '@testing-library/react'
import { TrendingUp, TrendingDown } from 'lucide-react'
import StatCard from './StatCard'

describe('StatCard Component', () => {
  const defaultProps = {
    title: 'Total Revenue',
    value: '$12,345',
    icon: TrendingUp,
    color: 'blue' as const
  }

  it('should render the title', () => {
    render(<StatCard {...defaultProps} />)

    expect(screen.getByText('Total Revenue')).toBeInTheDocument()
  })

  it('should render the value', () => {
    render(<StatCard {...defaultProps} value="$12,345" />)

    expect(screen.getByText('$12,345')).toBeInTheDocument()
  })

  it('should render numeric value', () => {
    render(<StatCard {...defaultProps} value={12345} />)

    expect(screen.getByText('12345')).toBeInTheDocument()
  })

  it('should not render change when not provided', () => {
    render(<StatCard {...defaultProps} />)

    expect(screen.queryByText(/from last period/)).not.toBeInTheDocument()
  })

  it('should render positive change in green', () => {
    render(<StatCard {...defaultProps} change={5.5} />)

    const changeElement = screen.getByText('+5.5% from last period')
    expect(changeElement).toBeInTheDocument()
    expect(changeElement).toHaveClass('text-green-600')
  })

  it('should render negative change in red', () => {
    render(<StatCard {...defaultProps} change={-3.2} />)

    const changeElement = screen.getByText('-3.2% from last period')
    expect(changeElement).toBeInTheDocument()
    expect(changeElement).toHaveClass('text-red-600')
  })

  it('should render zero change in default color', () => {
    render(<StatCard {...defaultProps} change={0} />)

    const changeElement = screen.getByText('0% from last period')
    expect(changeElement).toBeInTheDocument()
    expect(changeElement).toHaveClass('text-secondary-600')
  })

  it('should render the icon', () => {
    const { container } = render(<StatCard {...defaultProps} icon={TrendingUp} />)

    const svg = container.querySelector('svg')
    expect(svg).toBeInTheDocument()
  })

  it('should apply correct color classes for blue', () => {
    const { container } = render(<StatCard {...defaultProps} color="blue" icon={TrendingUp} />)

    const iconContainer = container.querySelector('.bg-blue-50')
    expect(iconContainer).toBeInTheDocument()
  })

  it('should apply correct color classes for green', () => {
    const { container } = render(<StatCard {...defaultProps} color="green" icon={TrendingUp} />)

    const iconContainer = container.querySelector('.bg-green-50')
    expect(iconContainer).toBeInTheDocument()
  })

  it('should apply correct color classes for yellow', () => {
    const { container } = render(<StatCard {...defaultProps} color="yellow" icon={TrendingUp} />)

    const iconContainer = container.querySelector('.bg-yellow-50')
    expect(iconContainer).toBeInTheDocument()
  })

  it('should apply correct color classes for red', () => {
    const { container } = render(<StatCard {...defaultProps} color="red" icon={TrendingDown} />)

    const iconContainer = container.querySelector('.bg-red-50')
    expect(iconContainer).toBeInTheDocument()
  })

  it('should default to blue color when not specified', () => {
    const { container } = render(
      <StatCard title="Test" value="100" icon={TrendingUp} />
    )

    const iconContainer = container.querySelector('.bg-blue-50')
    expect(iconContainer).toBeInTheDocument()
  })

  it('should render card structure correctly', () => {
    const { container } = render(<StatCard {...defaultProps} />)

    const card = container.querySelector('.card')
    expect(card).toBeInTheDocument()
  })

  it('should handle large numeric values', () => {
    render(<StatCard {...defaultProps} value={9999999} />)

    expect(screen.getByText('9999999')).toBeInTheDocument()
  })

  it('should handle decimal change values', () => {
    render(<StatCard {...defaultProps} change={2.5} />)

    expect(screen.getByText('+2.5% from last period')).toBeInTheDocument()
  })

  it('should handle very small change values', () => {
    render(<StatCard {...defaultProps} change={0.01} />)

    expect(screen.getByText('+0.01% from last period')).toBeInTheDocument()
  })

  it('should handle very large change values', () => {
    render(<StatCard {...defaultProps} change={999.99} />)

    expect(screen.getByText('+999.99% from last period')).toBeInTheDocument()
  })
})
