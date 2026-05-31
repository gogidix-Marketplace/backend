import React, { useState, HTMLAttributes } from 'react';
import { cn } from '../../utils/cn';

export interface TooltipProps extends HTMLAttributes<HTMLDivElement> {
  content: React.ReactNode;
  placement?: 'top' | 'bottom' | 'left' | 'right';
  delay?: number;
}

const Tooltip = React.forwardRef<HTMLDivElement, TooltipProps>(
  ({ children, content, placement = 'top', delay = 200, className, ...props }, ref) => {
    const [isVisible, setIsVisible] = useState(false);
    const [timeoutId, setTimeoutId] = useState<NodeJS.Timeout | null>(null);

    const showTooltip = () => {
      const id = setTimeout(() => setIsVisible(true), delay);
      setTimeoutId(id);
    };

    const hideTooltip = () => {
      if (timeoutId) {
        clearTimeout(timeoutId);
      }
      setIsVisible(false);
    };

    const placementStyles = {
      top: 'bottom-full left-1/2 -translate-x-1/2 mb-2',
      bottom: 'top-full left-1/2 -translate-x-1/2 mt-2',
      left: 'right-full top-1/2 -translate-y-1/2 mr-2',
      right: 'left-full top-1/2 -translate-y-1/2 ml-2',
    };

    return (
      <div
        ref={ref}
        className={cn('relative inline-block', className)}
        onMouseEnter={showTooltip}
        onMouseLeave={hideTooltip}
        onFocus={showTooltip}
        onBlur={hideTooltip}
        {...props}
      >
        {children}
        {isVisible && (
          <div
            className={cn(
              'absolute z-50 max-w-xs rounded-md bg-foreground px-3 py-1.5 text-xs text-background shadow-lg animate-in fade-in zoom-in-95 duration-200',
              placementStyles[placement]
            )}
          >
            {content}
            <div className="absolute inset-0 -z-10">
              <div
                className={cn('absolute bg-foreground', {
                  'top-full left-1/2 -translate-x-1/2 h-2 w-2 rotate-45': placement === 'top',
                  'bottom-full left-1/2 -translate-x-1/2 h-2 w-2 rotate-45': placement === 'bottom',
                  'right-full top-1/2 -translate-y-1/2 h-2 w-2 rotate-45': placement === 'left',
                  'left-full top-1/2 -translate-y-1/2 h-2 w-2 rotate-45': placement === 'right',
                })}
              />
            </div>
          </div>
        )}
      </div>
    );
  }
);

Tooltip.displayName = 'Tooltip';

export { Tooltip };
