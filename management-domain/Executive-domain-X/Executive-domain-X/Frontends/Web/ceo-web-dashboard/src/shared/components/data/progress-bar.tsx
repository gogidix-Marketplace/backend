import { cn } from '@shared/utils/cn'
import * as React from 'react'

/**
 * ProgressBar - Labeled progress with percentage
 *
 * Features:
 * - Horizontal and vertical orientation
 * - Striped and animated variants
 * - Color coding by status
 * - Labels and percentage display
 */

export interface ProgressBarProps extends React.HTMLAttributes<HTMLDivElement> {
  value: number // 0-100
  max?: number
  size?: 'sm' | 'md' | 'lg'
  variant?: 'default' | 'success' | 'warning' | 'danger' | 'info'
  orientation?: 'horizontal' | 'vertical'
  striped?: boolean
  animated?: boolean
  showLabel?: boolean
  showPercentage?: boolean
  label?: string
  color?: string // Custom color
}

const sizeClasses = {
  sm: { bar: 'h-1', vertical: 'h-16 w-1', text: 'text-xs' },
  md: { bar: 'h-2', vertical: 'h-24 w-2', text: 'text-sm' },
  lg: { bar: 'h-3', vertical: 'h-32 w-3', text: 'text-base' },
}

const variantClasses = {
  default: 'bg-slate-200 dark:bg-slate-700 [--progress-color:theme(colors.primary)]',
  success: 'bg-slate-200 dark:bg-slate-700 [--progress-color:theme(colors.green.500)]',
  warning: 'bg-slate-200 dark:bg-slate-700 [--progress-color:theme(colors.amber.500)]',
  danger: 'bg-slate-200 dark:bg-slate-700 [--progress-color:theme(colors.red.500)]',
  info: 'bg-slate-200 dark:bg-slate-700 [--progress-color:theme(colors.blue.500)]',
}

export function ProgressBar({
  value,
  max = 100,
  size = 'md',
  variant = 'default',
  orientation = 'horizontal',
  striped = false,
  animated = false,
  showLabel = false,
  showPercentage = false,
  label,
  color,
  className,
  style,
  ...props
}: ProgressBarProps) {
  const percentage = Math.min(Math.max((value / max) * 100, 0), 100)
  const sizeClass = sizeClasses[size]
  const variantClass = variantClasses[variant]

  const barStyle = {
    ...style,
    ...(color && { '--progress-color': color } as React.CSSProperties),
  } as React.CSSProperties

  return (
    <div
      className={cn(
        'relative flex items-center',
        orientation === 'vertical' ? 'flex-col gap-2' : 'flex-row gap-3'
      )}
      {...props}
    >
      {(label || showLabel) && (
        <span className={cn('min-w-fit text-muted-foreground', sizeClass.text)}>
          {label || 'Progress'}
        </span>
      )}

      <div
        className={cn(
          'relative overflow-hidden rounded-full bg-muted',
          orientation === 'horizontal' ? sizeClass.bar : sizeClass.vertical,
          variantClass
        )}
        style={barStyle}
      >
        <div
          className={cn(
            'absolute top-0 left-0 h-full transition-all duration-500 ease-out',
            'bg-[var(--progress-color)]',
            striped && 'bg-[linear-gradient(45deg,rgba(255,255,255,0.15)_25%,transparent_25%,transparent_50%,rgba(255,255,255,0.15)_50%,rgba(255,255,255,0.15)_75%,transparent_75%,transparent)]',
            striped && 'bg-[length:1rem_1rem]',
            animated && striped && 'animate-progress-stripes'
          )}
          style={{
            [orientation === 'horizontal' ? 'width' : 'height']: `${percentage}%`,
          }}
        />
      </div>

      {showPercentage && (
        <span className={cn('min-w-fit font-medium tabular-nums', sizeClass.text)}>
          {Math.round(percentage)}%
        </span>
      )}
    </div>
  )
}
