import { LucideIcon } from 'lucide-react';
import { cn, getTrendIcon, getTrendColor, formatNumber, formatPercentage } from '../../shared/utils';

interface MetricCardProps {
  title: string;
  value: number | string;
  change?: number;
  trend?: 'up' | 'down' | 'stable';
  icon?: LucideIcon;
  unit?: string;
  decimals?: number;
  isInverted?: boolean;
  className?: string;
}

export function MetricCard({
  title,
  value,
  change,
  trend,
  icon: Icon,
  unit = '',
  decimals = 1,
  isInverted = false,
  className,
}: MetricCardProps) {
  const displayValue = typeof value === 'number' ? formatNumber(value, decimals) : value;
  const displayChange = change !== undefined ? formatPercentage(Math.abs(change), 1) : null;

  return (
    <div className={cn('bg-white rounded-xl p-6 shadow-sm border border-gray-100', className)}>
      <div className="flex items-start justify-between">
        <div className="flex-1">
          <p className="text-sm font-medium text-gray-500 mb-1">{title}</p>
          <div className="flex items-baseline gap-2">
            <p className="text-2xl font-bold text-gray-900">
              {displayValue}
              {unit && <span className="text-lg font-normal text-gray-500 ml-1">{unit}</span>}
            </p>
            {displayChange && trend && (
              <span
                className={cn(
                  'text-sm font-medium flex items-center gap-1',
                  getTrendColor(trend, isInverted)
                )}
              >
                {getTrendIcon(trend)} {displayChange}
              </span>
            )}
          </div>
        </div>
        {Icon && (
          <div className="p-2 bg-primary-50 rounded-lg">
            <Icon className="w-5 h-5 text-primary-600" />
          </div>
        )}
      </div>
    </div>
  );
}
