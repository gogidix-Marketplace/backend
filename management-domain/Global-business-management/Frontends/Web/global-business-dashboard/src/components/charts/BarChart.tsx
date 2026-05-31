import React from 'react';
import {
  BarChart as RechartsBarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
  Cell,
} from 'recharts';
import { Card, CardContent, CardHeader, CardTitle } from '../common/Card';
import { formatCurrency, formatNumber } from '../../utils/currency';
import { cn } from '../../utils/cn';

interface ChartData {
  name: string;
  value: number;
  [key: string]: string | number | undefined;
}

interface BarChartProps {
  data: ChartData[];
  title?: string;
  dataKey?: string;
  xAxisKey?: string;
  horizontal?: boolean;
  colors?: string[];
  height?: number;
  className?: string;
  formatValue?: 'currency' | 'number' | 'percent';
  currency?: string;
  showLegend?: boolean;
}

const defaultColors = [
  '#3b82f6',
  '#22c55e',
  '#f59e0b',
  '#ef4444',
  '#8b5cf6',
  '#ec4899',
  '#14b8a6',
  '#f97316',
];

export const BarChart: React.FC<BarChartProps> = ({
  data,
  title,
  dataKey = 'value',
  xAxisKey = 'name',
  horizontal = false,
  colors = defaultColors,
  height = 300,
  className,
  formatValue = 'number',
  currency = 'USD',
  showLegend = true,
}) => {
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

  const ChartComponent = horizontal ? RechartsBarChart : RechartsBarChart;

  return (
    <Card className={cn('', className)}>
      {title && (
        <CardHeader>
          <CardTitle className="text-base">{title}</CardTitle>
        </CardHeader>
      )}
      <CardContent>
        <ResponsiveContainer width="100%" height={height}>
          <ChartComponent
            data={data}
            layout={horizontal ? 'vertical' : 'horizontal'}
            margin={{ top: 5, right: 10, left: 10, bottom: 5 }}
          >
            <CartesianGrid strokeDasharray="3 3" className="stroke-muted" />
            <XAxis
              dataKey={horizontal ? dataKey : xAxisKey}
              className="text-xs"
              stroke="hsl(var(--muted-foreground))"
              type={horizontal ? 'number' : 'category'}
              tickFormatter={horizontal ? (value) => formatTooltipValue(value) : undefined}
            />
            <YAxis
              className="text-xs"
              stroke="hsl(var(--muted-foreground))"
              type={horizontal ? 'category' : 'number'}
              tickFormatter={(value) => {
                if (horizontal) return value;
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
            {showLegend && <Legend />}
            <Bar
              dataKey={horizontal ? xAxisKey : dataKey}
              name={xAxisKey}
              radius={[4, 4, 0, 0]}
            >
              {data.map((_, index) => (
                <Cell key={`cell-${index}`} fill={colors[index % colors.length]} />
              ))}
            </Bar>
          </ChartComponent>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
};

export const BarChartSkeleton: React.FC<{ title?: string; height?: number }> = ({
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
