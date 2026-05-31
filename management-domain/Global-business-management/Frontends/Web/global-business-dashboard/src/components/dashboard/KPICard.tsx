import React from 'react';
import { useTranslation } from 'react-i18next';
import {
  TrendingUp,
  TrendingDown,
  Minus,
  DollarSign,
  Users,
  Percent,
  Activity,
} from 'lucide-react';
import { KPIMetric } from '../../types';
import { cn } from '../../utils/cn';
import { Card, CardContent, CardHeader, CardTitle } from '../common/Card';
import { Badge } from '../common/Badge';
import { formatCurrency, formatNumber, formatPercent } from '../../utils/currency';

interface KPICardProps {
  metric: KPIMetric;
  onClick?: () => void;
  className?: string;
}

const categoryIcons: Record<string, React.ComponentType<{ className?: string }>> = {
  revenue: DollarSign,
  profit: DollarSign,
  growth: TrendingUp,
  customer: Users,
  satisfaction: Activity,
  operational: Activity,
  financial: DollarSign,
  compliance: Activity,
};

export const KPICard: React.FC<KPICardProps> = ({ metric, onClick, className }) => {
  const { t } = useTranslation();
  const Icon = categoryIcons[metric.category] || Activity;
  const isPositive = metric.changeType === 'increase';
  const isNeutral = metric.changeType === 'neutral';

  const formatValue = (value: number, unit: string): string => {
    if (unit === '%') return formatPercent(value);
    if (unit === 'USD' || unit === 'EUR' || unit === 'GBP') return formatCurrency(value, unit);
    if (unit === '/5') return value.toFixed(1);
    return formatNumber(value);
  };

  return (
    <Card
      className={cn('cursor-pointer transition-all hover:shadow-md', className)}
      onClick={onClick}
      padding="md"
    >
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <CardTitle className="text-sm font-medium">
          {t(`kpi.${metric.name}`)}
        </CardTitle>
        <Icon className="h-4 w-4 text-muted-foreground" />
      </CardHeader>
      <CardContent>
        <div className="text-2xl font-bold">{formatValue(metric.value, metric.unit)}</div>
        <div className="flex items-center gap-2 mt-2">
          <Badge
            variant={isPositive ? 'success' : isNeutral ? 'secondary' : 'destructive'}
            size="sm"
            className="gap-1"
          >
            {isPositive ? (
              <TrendingUp className="h-3 w-3" />
            ) : isNeutral ? (
              <Minus className="h-3 w-3" />
            ) : (
              <TrendingDown className="h-3 w-3" />
            )}
            {isPositive ? '+' : ''}{metric.change.toFixed(1)}%
          </Badge>
          <span className="text-xs text-muted-foreground">
            {t('dashboard.comparedTo')} {t('dashboard.period.month')}
          </span>
        </div>
        {metric.target && (
          <div className="mt-3">
            <div className="flex justify-between text-xs text-muted-foreground mb-1">
              <span>{t('dashboard.target')}</span>
              <span>{formatValue(metric.target, metric.unit)}</span>
            </div>
            <div className="h-1.5 w-full bg-muted rounded-full overflow-hidden">
              <div
                className={cn(
                  'h-full transition-all duration-500',
                  (metric.value / metric.target) >= 1 ? 'bg-green-500' : 'bg-primary'
                )}
                style={{
                  width: `${Math.min((metric.value / metric.target) * 100, 100)}%`,
                }}
              />
            </div>
          </div>
        )}
      </CardContent>
    </Card>
  );
};

export const KPICardSkeleton: React.FC = () => {
  return (
    <Card padding="md">
      <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
        <div className="h-4 w-24 bg-muted animate-pulse rounded" />
        <div className="h-4 w-4 bg-muted animate-pulse rounded" />
      </CardHeader>
      <CardContent>
        <div className="h-8 w-32 bg-muted animate-pulse rounded mb-2" />
        <div className="h-5 w-20 bg-muted animate-pulse rounded" />
      </CardContent>
    </Card>
  );
};
