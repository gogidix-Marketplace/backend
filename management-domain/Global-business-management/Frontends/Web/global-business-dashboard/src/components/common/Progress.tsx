import React from 'react';
import { cn } from '../../utils/cn';

export interface ProgressProps extends React.HTMLAttributes<HTMLDivElement> {
  value?: number;
  max?: number;
  size?: 'sm' | 'md' | 'lg';
  variant?: 'default' | 'success' | 'warning' | 'destructive';
  showLabel?: boolean;
  label?: string;
}

const Progress = React.forwardRef<HTMLDivElement, ProgressProps>(
  (
    {
      className,
      value = 0,
      max = 100,
      size = 'md',
      variant = 'default',
      showLabel = false,
      label,
      ...props
    },
    ref
  ) => {
    const percentage = Math.min(Math.max((value / max) * 100, 0), 100);

    const variantStyles = {
      default: 'bg-primary',
      success: 'bg-green-500',
      warning: 'bg-yellow-500',
      destructive: 'bg-destructive',
    };

    const sizeStyles = {
      sm: 'h-1',
      md: 'h-2',
      lg: 'h-3',
    };

    return (
      <div ref={ref} className={cn('space-y-1', className)} {...props}>
        {(showLabel || label) && (
          <div className="flex justify-between text-sm">
            {label && <span className="font-medium">{label}</span>}
            {showLabel && (
              <span className="text-muted-foreground">
                {Math.round(percentage)}%
              </span>
            )}
          </div>
        )}
        <div
          className={cn(
            'relative w-full overflow-hidden rounded-full bg-muted',
            sizeStyles[size]
          )}
        >
          <div
            className={cn(
              'h-full transition-all duration-300 ease-in-out',
              variantStyles[variant]
            )}
            style={{ width: `${percentage}%` }}
          />
        </div>
      </div>
    );
  }
);

Progress.displayName = 'Progress';

export { Progress };
