import * as React from 'react'
import { cn } from '@shared/utils/cn'
import { Loader2 } from 'lucide-react'

/**
 * ProgressBackdrop - Full-screen loading overlay
 *
 * Features:
 * - Full-screen overlay with blur
 * - Centered loading indicator
 * - Optional progress bar
 * - Optional message
 * - Prevents background interaction
 */

export interface ProgressBackdropProps {
  open: boolean
  message?: string
  progress?: number // 0-100, undefined = indeterminate
  variant?: 'default' | 'logo'
  className?: string
}

export function ProgressBackdrop({
  open,
  message,
  progress,
  variant = 'default',
  className,
}: ProgressBackdropProps) {
  if (!open) return null

  return (
    <div
      className={cn(
        'fixed inset-0 z-50 flex items-center justify-center',
        'bg-background/80 backdrop-blur-sm',
        'transition-opacity duration-200',
        open ? 'opacity-100' : 'opacity-0 pointer-events-none',
        className
      )}
    >
      <div className="flex flex-col items-center gap-4 p-6 rounded-lg bg-card shadow-lg border max-w-sm">
        {variant === 'default' ? (
          <div className="relative">
            <Loader2 className="h-12 w-12 animate-spin text-primary" />
            {progress !== undefined && (
              <svg className="absolute inset-0 -rotate-90" viewBox="0 0 48 48">
                <circle
                  cx="24"
                  cy="24"
                  r="20"
                  fill="none"
                  stroke="hsl(var(--primary))"
                  strokeWidth="4"
                  strokeDasharray={`${progress * 1.256} 125.6`}
                  strokeLinecap="round"
                  className="opacity-30"
                />
              </svg>
            )}
          </div>
        ) : (
          <div className="flex h-16 w-16 items-center justify-center rounded-xl bg-gradient-to-br from-[#0D47A1] to-[#1565C0] shadow-lg">
            <span className="text-3xl font-bold text-white">G</span>
          </div>
        )}

        {message && (
          <p className="text-center text-sm text-muted-foreground">{message}</p>
        )}

        {progress !== undefined && (
          <div className="w-full space-y-1">
            <div className="flex justify-between text-xs text-muted-foreground">
              <span>Processing...</span>
              <span>{Math.round(progress)}%</span>
            </div>
            <div className="h-2 w-full overflow-hidden rounded-full bg-slate-200 dark:bg-slate-700">
              <div
                className="h-full bg-primary transition-all duration-300"
                style={{ width: `${progress}%` }}
              />
            </div>
          </div>
        )}
      </div>
    </div>
  )
}

/**
 * InlineProgress - Smaller inline loading indicator
 */
export interface InlineProgressProps {
  isLoading: boolean
  message?: string
  size?: 'sm' | 'md' | 'lg'
  className?: string
}

export function InlineProgress({
  isLoading,
  message,
  size = 'md',
  className,
}: InlineProgressProps) {
  if (!isLoading) return null

  const sizeClasses = {
    sm: 'h-4 w-4',
    md: 'h-6 w-6',
    lg: 'h-8 w-8',
  }

  return (
    <div className={cn('flex items-center gap-2', className)}>
      <Loader2 className={cn('animate-spin text-primary', sizeClasses[size])} />
      {message && <span className="text-sm text-muted-foreground">{message}</span>}
    </div>
  )
}

/**
 * Skeleton Loading - Content placeholder
 */
export interface SkeletonProps extends React.HTMLAttributes<HTMLDivElement> {
  variant?: 'text' | 'circular' | 'rectangular' | 'rounded'
  width?: string | number
  height?: string | number
  animation?: 'pulse' | 'wave' | 'none'
}

export function Skeleton({
  variant = 'rectangular',
  width,
  height,
  animation = 'pulse',
  className,
  ...props
}: SkeletonProps) {
  const variantClasses = {
    text: 'rounded',
    circular: 'rounded-full',
    rectangular: 'rounded-sm',
    rounded: 'rounded-md',
  }

  const animationClasses = {
    pulse: 'animate-pulse',
    wave: 'animate-shimmer',
    none: '',
  }

  return (
    <div
      className={cn(
        'bg-slate-200 dark:bg-slate-700',
        variantClasses[variant],
        animationClasses[animation],
        className
      )}
      style={{ width, height }}
      {...props}
    />
  )
}

/**
 * SkeletonLoader - Multiple skeleton items for page loading
 */
export interface SkeletonLoaderProps {
  type?: 'list' | 'card' | 'table' | 'chart'
  count?: number
  className?: string
}

export function SkeletonLoader({
  type = 'list',
  count = 3,
  className,
}: SkeletonLoaderProps) {
  const renderListItem = () => (
    <div className="flex items-center gap-4 p-4 space-y-0">
      <Skeleton variant="circular" width={40} height={40} />
      <div className="space-y-2 flex-1">
        <Skeleton variant="text" width="60%" height={16} />
        <Skeleton variant="text" width="40%" height={14} />
      </div>
    </div>
  )

  const renderCard = () => (
    <div className="p-6 space-y-4 rounded-lg border">
      <Skeleton variant="rectangular" width="100%" height={120} />
      <div className="space-y-2">
        <Skeleton variant="text" width="30%" height={20} />
        <Skeleton variant="text" width="100%" height={16} />
        <Skeleton variant="text" width="80%" height={16} />
      </div>
    </div>
  )

  const renderTable = () => (
    <div className="space-y-2">
      <div className="flex gap-4 p-2">
        {[1, 2, 3, 4].map((i) => (
          <Skeleton key={i} variant="text" width={`${100 / 4}%`} height={16} />
        ))}
      </div>
      {Array.from({ length: 5 }).map((_, i) => (
        <div key={i} className="flex gap-4 p-2 border-t">
          {[1, 2, 3, 4].map((j) => (
            <Skeleton key={j} variant="text" width={`${100 / 4}%`} height={14} />
          ))}
        </div>
      ))}
    </div>
  )

  const renderChart = () => (
    <div className="p-6 space-y-4 rounded-lg border h-64">
      <Skeleton variant="text" width="30%" height={24} />
      <div className="flex items-end gap-2 h-40">
        {Array.from({ length: 12 }).map((_, i) => (
          <Skeleton
            key={i}
            variant="rectangular"
            width="8%"
            height={`${30 + Math.random() * 70}%`}
            className="rounded-t"
          />
        ))}
      </div>
    </div>
  )

  const renderType = () => {
    switch (type) {
      case 'card':
        return renderCard()
      case 'table':
        return renderTable()
      case 'chart':
        return renderChart()
      default:
        return renderListItem()
    }
  }

  return (
    <div className={cn('space-y-4', className)}>
      {Array.from({ length: count }).map((_, i) => (
        <div key={i}>{renderType()}</div>
      ))}
    </div>
  )
}
