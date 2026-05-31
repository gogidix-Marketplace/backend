import React from 'react';
import { cn } from '../../utils/cn';

interface BadgeProps extends React.HTMLAttributes<HTMLDivElement> {
  variant?: 'default' | 'success' | 'warning' | 'destructive' | 'info';
  size?: 'sm' | 'md';
}

export const Badge = React.forwardRef<HTMLDivElement, BadgeProps>(
  ({ className, variant = 'default', size = 'md', children, ...props }, ref) => {
    return (
      <div
        ref={ref}
        className={cn(
          'inline-flex items-center rounded-full font-semibold',
          {
            'bg-primary text-primary-foreground': variant === 'default',
            'bg-green-500 text-white': variant === 'success',
            'bg-yellow-500 text-white': variant === 'warning',
            'bg-destructive text-destructive-foreground': variant === 'destructive',
            'bg-blue-500 text-white': variant === 'info',
            'px-2 py-0.5 text-xs': size === 'sm',
            'px-2.5 py-1 text-sm': size === 'md',
          },
          className
        )}
        {...props}
      >
        {children}
      </div>
    );
  }
);

Badge.displayName = 'Badge';
