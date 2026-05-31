import React from 'react';
import { cn } from '../../utils/cn';

export interface LoadingProps {
  size?: 'sm' | 'md' | 'lg';
  className?: string;
  text?: string;
}

export const Loading = React.forwardRef<HTMLDivElement, LoadingProps>(
  ({ size = 'md', className, text }, ref) => {
    return (
      <div
        ref={ref}
        className={cn('flex flex-col items-center justify-center gap-4', className)}
      >
        <div
          className={cn('animate-spin rounded-full border-4 border-muted border-t-primary', {
            'h-4 w-4 border-2': size === 'sm',
            'h-8 w-8 border-4': size === 'md',
            'h-12 w-12 border-4': size === 'lg',
          })}
        />
        {text && (
          <p className="text-sm text-muted-foreground animate-pulse">{text}</p>
        )}
      </div>
    );
  }
);

Loading.displayName = 'Loading';

export const LoadingSpinner = ({ size = 'md', className }: { size?: 'sm' | 'md' | 'lg'; className?: string }) => {
  return (
    <div
      className={cn('animate-spin rounded-full border-4 border-muted border-t-primary', {
        'h-4 w-4 border-2': size === 'sm',
        'h-8 w-8 border-4': size === 'md',
        'h-12 w-12 border-4': size === 'lg',
      }, className)}
    />
  );
};

export const LoadingDots = ({ className }: { className?: string }) => {
  return (
    <div className={cn('flex space-x-1', className)}>
      <div className="h-2 w-2 animate-bounce rounded-full bg-primary [animation-delay:-0.3s]" />
      <div className="h-2 w-2 animate-bounce rounded-full bg-primary [animation-delay:-0.15s]" />
      <div className="h-2 w-2 animate-bounce rounded-full bg-primary" />
    </div>
  );
};

export const Skeleton = ({ className, ...props }: React.HTMLAttributes<HTMLDivElement>) => {
  return (
    <div
      className={cn('animate-pulse rounded-md bg-muted', className)}
      {...props}
    />
  );
};

export const CardSkeleton = () => {
  return (
    <div className="rounded-lg border p-6 space-y-4">
      <Skeleton className="h-6 w-1/3" />
      <Skeleton className="h-4 w-full" />
      <Skeleton className="h-4 w-2/3" />
      <div className="flex gap-4 pt-4">
        <Skeleton className="h-10 w-24" />
        <Skeleton className="h-10 w-24" />
      </div>
    </div>
  );
};

export const TableSkeleton = ({ rows = 5 }: { rows?: number }) => {
  return (
    <div className="space-y-3">
      <div className="flex gap-4">
        <Skeleton className="h-10 flex-1" />
        <Skeleton className="h-10 flex-1" />
        <Skeleton className="h-10 flex-1" />
        <Skeleton className="h-10 flex-1" />
      </div>
      {Array.from({ length: rows }).map((_, i) => (
        <div key={i} className="flex gap-4">
          <Skeleton className="h-10 flex-1" />
          <Skeleton className="h-10 flex-1" />
          <Skeleton className="h-10 flex-1" />
          <Skeleton className="h-10 flex-1" />
        </div>
      ))}
    </div>
  );
};
