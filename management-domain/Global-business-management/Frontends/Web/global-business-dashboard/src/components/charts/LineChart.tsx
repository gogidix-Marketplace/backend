import React from 'react';
import {
  LineChart as RechartsLineChart,
  Line,
  Area,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  ReferenceLine,
} from 'recharts';
import { useTranslation } from 'react-i18next';
import { Card, CardContent, CardHeader, CardTitle } from '../common/Card';
import { formatCurrency, formatNumber } from '../../utils/currency';
import { cn } from '../../utils/cn';

interface ChartData {
  period: string;
  value: number;
  target?: number;
  [key: string]: string | number | undefined;
}

interface LineChartProps {
  data: ChartData[];
  title?: string;
  dataKey?: string;
  targetKey?: string;
  xAxisKey?: string;
  color?: string;
  area?: boolean;
  height?: number;
  className?: string;
  formatValue?: 'currency' | 'number' | 'percent';
  currency?: string;
}

export const LineChart: React.FC<LineChartProps> = ({
  data,
  title,
  dataKey = 'value',
  targetKey = 'target',
  xAxisKey = 'period',
  color = '#3b82f6',
  area = false,
  height = 300,
  className,
  formatValue = 'number',
  currency = 'USD',
}) => {
  const { t } = useTranslation();

  const formatTooltipValue = (value: number): string => {
    if (formatValue === 'currency') {
      return formatCurrency(value, currency);
    }
    if (formatValue === 'percent') {
      return `${value.toFixed(1)}%`;
    }
    return formatNumber(value);
  };

  const CustomTooltip = ({ active, payload, label }: any) => {
    if (!active || !payload || !payload.length) return null;

    return (
      <div className="rounded-lg border bg-background p-3 shadow-md">
        <p className="text-sm font-medium mb-2">{label}</p>
        {payload.map((entry: any, index: number) => (
          <p key={index} className="text-sm" style={{ color: entry.color }}>
            {entry.name}: {formatTooltipValue(entry.value)}
          </p>
        ))}
      </div>
    );
  };

  const hasTarget = data.some((d) => d[targetKey] !== undefined);

  return (
    <Card className={cn('', className)}>
      {title && (
        <CardHeader>
          <CardTitle className="text-base">{title}</CardTitle>
        </CardHeader>
      )}
      <CardContent>
        <ResponsiveContainer width="100%" height={height}>
          <RechartsLineChart
            data={data}
            margin={{ top: 5, right: 10, left: 10, bottom: 5 }}
          >
            <defs>
              <linearGradient id={`gradient-${color}`} x1="0" y1="0" x2="0" y2="1">
                <stop offset="5%" stopColor={color} stopOpacity={0.3} />
                <stop offset="95%" stopColor={color} stopOpacity={0} />
              </linearGradient>
            </defs>
            <CartesianGrid strokeDasharray="3 3" className="stroke-muted" />
            <XAxis
              dataKey={xAxisKey}
              className="text-xs"
              stroke="hsl(var(--muted-foreground))"
            />
            <YAxis
              className="text-xs"
              stroke="hsl(var(--muted-foreground))"
              tickFormatter={(value) => {
                if (formatValue === 'currency') {
                  return formatCurrency(value, currency, { showSymbol: false });
                }
                if (formatValue === 'percent') {
                  return `${value}%`;
                }
                return formatNumber(value);
              }}
            />
            <Tooltip content={<CustomTooltip />} />
            <Legend />
            {area ? (
              <Area
                type="monotone"
                dataKey={dataKey}
                stroke={color}
                strokeWidth={2}
                fill={`url(#gradient-${color})`}
                name={t('dashboard.actual')}
              />
            ) : (
              <Line
                type="monotone"
                dataKey={dataKey}
                stroke={color}
                strokeWidth={2}
                dot={{ fill: color, r: 4 }}
                activeDot={{ r: 6, stroke: color, strokeWidth: 2 }}
                name={t('dashboard.actual')}
              />
            )}
            {hasTarget && (
              <Line
                type="monotone"
                dataKey={targetKey}
                stroke="#22c55e"
                strokeWidth={2}
                strokeDasharray="5 5"
                dot={false}
                name={t('dashboard.target')}
              />
            )}
          </RechartsLineChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
};

export const LineChartSkeleton: React.FC<{ title?: string; height?: number }> = ({
  title,
  height = 300,
}) => {
  return (
    <Card>
      {title && (
        <CardHeader>
          <div className="h-5 w-32 bg-muted animate-pulse rounded" />
        </CardHeader>
      )}
      <CardContent>
        <div style={{ height }} className="bg-muted animate-pulse rounded" />
      </CardContent>
    </Card>
  );
};
