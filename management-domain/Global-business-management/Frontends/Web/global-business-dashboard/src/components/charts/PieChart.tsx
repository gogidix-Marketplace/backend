import React from 'react';
import {
  PieChart as RechartsPieChart,
  Pie,
  Cell,
  ResponsiveContainer,
  Tooltip,
  Legend,
} from 'recharts';
import { Card, CardContent, CardHeader, CardTitle } from '../common/Card';
import { formatCurrency, formatNumber } from '../../utils/currency';
import { cn } from '../../utils/cn';

interface ChartData {
  name: string;
  value: number;
  [key: string]: string | number | undefined;
}

interface PieChartProps {
  data: ChartData[];
  title?: string;
  dataKey?: string;
  nameKey?: string;
  colors?: string[];
  height?: number;
  className?: string;
  formatValue?: 'currency' | 'number' | 'percent';
  currency?: string;
  innerRadius?: number;
  outerRadius?: number;
  showLegend?: boolean;
  label?: boolean;
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
  '#6366f1',
  '#84cc16',
];

export const PieChart: React.FC<PieChartProps> = ({
  data,
  title,
  dataKey = 'value',
  nameKey = 'name',
  colors = defaultColors,
  height = 300,
  className,
  formatValue = 'number',
  currency = 'USD',
  innerRadius = 0,
  outerRadius = 80,
  showLegend = true,
  label = false,
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

  const CustomTooltip = ({ active, payload }: any) => {
    if (!active || !payload || !payload.length) return null;

    const data = payload[0];
    const total = payload.reduce((sum: number, entry: any) => sum + entry.value, 0);
    const percentage = total > 0 ? ((data.value / total) * 100).toFixed(1) : 0;

    return (
      <div className="rounded-lg border bg-background p-3 shadow-md">
        <p className="text-sm font-medium">{data.name}</p>
        <p className="text-sm text-muted-foreground">
          {formatTooltipValue(data.value)} ({percentage}%)
        </p>
      </div>
    );
  };

  const CustomLabel = ({ cx, cy, midAngle, innerRadius, outerRadius, percent, index, name }: any) => {
    if (!label) return null;

    const RADIAN = Math.PI / 180;
    const radius = innerRadius + (outerRadius - innerRadius) * 0.5;
    const x = cx + radius * Math.cos(-midAngle * RADIAN);
    const y = cy + radius * Math.sin(-midAngle * RADIAN);

    return (
      <text
        x={x}
        y={y}
        fill="white"
        textAnchor={x > cx ? 'start' : 'end'}
        dominantBaseline="central"
        className="text-xs font-medium"
      >
        {`${(percent * 100).toFixed(0)}%`}
      </text>
    );
  };

  const isDonut = innerRadius > 0;

  return (
    <Card className={cn('', className)}>
      {title && (
        <CardHeader>
          <CardTitle className="text-base">{title}</CardTitle>
        </CardHeader>
      )}
      <CardContent>
        <ResponsiveContainer width="100%" height={height}>
          <RechartsPieChart>
            <Pie
              data={data}
              cx="50%"
              cy="50%"
              labelLine={false}
              label={CustomLabel}
              innerRadius={innerRadius}
              outerRadius={outerRadius}
              paddingAngle={2}
              dataKey={dataKey}
              nameKey={nameKey}
            >
              {data.map((entry, index) => (
                <Cell
                  key={`cell-${index}`}
                  fill={colors[index % colors.length]}
                  stroke="none"
                />
              ))}
            </Pie>
            <Tooltip content={<CustomTooltip />} />
            {showLegend && (
              <Legend
                verticalAlign="bottom"
                height={36}
                iconType="circle"
                formatter={(value, entry: any) => (
                  <span className="text-sm">
                    {value} ({formatTooltipValue(entry.payload.value)})
                  </span>
                )}
              />
            )}
          </RechartsPieChart>
        </ResponsiveContainer>
      </CardContent>
    </Card>
  );
};

export const DonutChart: React.FC<Omit<PieChartProps, 'innerRadius'>> = (props) => {
  return <PieChart {...props} innerRadius={60} />;
};

export const PieChartSkeleton: React.FC<{ title?: string; height?: number }> = ({
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
        <div style={{ height }} className="bg-muted animate-pulse rounded-full mx-auto w-64" />
      </CardContent>
    </Card>
  );
};
