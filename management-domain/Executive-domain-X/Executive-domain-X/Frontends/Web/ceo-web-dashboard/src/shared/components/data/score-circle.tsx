import { cn } from '@shared/utils/cn'
import * as React from 'react'

/**
 * ScoreCircle - Circular score (donut) display
 *
 * Features:
 * - Animated progress on mount
 * - Color-coded based on value
 * - Center content display
 * - Size variants
 * - Icon support in center
 */

export interface ScoreCircleProps {
  value: number // 0-100
  size?: number // Pixels
  strokeWidth?: number
  showValue?: boolean
  formatValue?: (value: number) => string
  centerContent?: React.ReactNode
  color?: 'auto' | 'primary' | 'success' | 'warning' | 'danger' | string
  animated?: boolean
  label?: string
  className?: string
}

const getColor = (value: number, color: ScoreCircleProps['color']): string => {
  if (color === 'auto') {
    if (value >= 80) return 'hsl(var(--success))'
    if (value >= 60) return 'hsl(var(--warning))'
    return 'hsl(var(--danger))'
  }
  if (color === 'primary') return 'hsl(var(--primary))'
  if (color === 'success') return 'hsl(var(--success))'
  if (color === 'warning') return 'hsl(var(--warning))'
  if (color === 'danger') return 'hsl(var(--danger))'
  return color as string
}

export function ScoreCircle({
  value,
  size = 120,
  strokeWidth = 10,
  showValue = true,
  formatValue = (v) => Math.round(v).toString(),
  centerContent,
  color = 'auto',
  animated = true,
  label,
  className,
}: ScoreCircleProps) {
  const [displayValue, setDisplayValue] = React.useState(0)
  const strokeColor = getColor(value, color)

  // Animate value on mount
  React.useEffect(() => {
    if (!animated) {
      setDisplayValue(value)
      return
    }

    const duration = 1000
    const steps = 60
    const increment = value / steps
    const interval = duration / steps

    let current = 0
    const timer = setInterval(() => {
      current += increment
      if (current >= value) {
        setDisplayValue(value)
        clearInterval(timer)
      } else {
        setDisplayValue(current)
      }
    }, interval)

    return () => clearInterval(timer)
  }, [value, animated])

  const radius = (size - strokeWidth) / 2
  const circumference = 2 * Math.PI * radius
  const strokeDashoffset = circumference - (displayValue / 100) * circumference
  const center = size / 2

  return (
    <div className={cn('inline-flex flex-col items-center', className)}>
      <svg width={size} height={size} className="transform -rotate-90" viewBox={`0 0 ${size} ${size}`}>
        {/* Background circle */}
        <circle
          cx={center}
          cy={center}
          r={radius}
          fill="none"
          stroke="hsl(var(--muted))"
          strokeWidth={strokeWidth}
          strokeLinecap="round"
        />

        {/* Progress circle */}
        <circle
          cx={center}
          cy={center}
          r={radius}
          fill="none"
          stroke={strokeColor}
          strokeWidth={strokeWidth}
          strokeLinecap="round"
          strokeDasharray={circumference}
          strokeDashoffset={strokeDashoffset}
          style={{
            transition: animated ? 'stroke-dashoffset 1s ease-out' : undefined,
          }}
        />
      </svg>

      {/* Center content */}
      {centerContent || showValue ? (
        <div className="absolute flex flex-col items-center justify-center" style={{ width: size, height: size }}>
          {centerContent || (
            <>
              <span className="text-2xl font-bold tabular-nums" style={{ color: strokeColor }}>
                {formatValue(displayValue)}
              </span>
              {label && <span className="text-xs text-muted-foreground">{label}</span>}
            </>
          )}
        </div>
      ) : null}
    </div>
  )
}

/**
 * ScoreDonut - Variant with inner content area
 */
export interface ScoreDonutProps extends Omit<ScoreCircleProps, 'size'> {
  size?: 'sm' | 'md' | 'lg' | 'xl'
}

const sizeMap = {
  sm: 80,
  md: 120,
  lg: 160,
  xl: 200,
}

export function ScoreDonut({ size = 'md', ...props }: ScoreDonutProps) {
  return <ScoreCircle {...props} size={sizeMap[size]} />
}
