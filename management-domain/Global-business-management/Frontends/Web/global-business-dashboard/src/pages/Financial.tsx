import React from 'react';
import { useTranslation } from 'react-i18next';
import { DollarSign, TrendingUp, TrendingDown, PieChart, Wallet } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '../components/common/Card';
import { Badge } from '../components/common/Badge';
import { Progress } from '../components/common/Progress';
import { LineChart } from '../components/charts/LineChart';
import { BarChart } from '../components/charts/BarChart';
import { PieChart } from '../components/charts/PieChart';
import { DataTable } from '../components/tables/DataTable';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '../components/common/Tabs';
import { formatCurrency, formatPercent } from '../utils/currency';
import { mockExpenseBreakdownData } from '../utils/mock-data';

const revenueData = [
  { period: 'Jan', value: 2450000, target: 2700000 },
  { period: 'Feb', value: 2580000, target: 2750000 },
  { period: 'Mar', value: 2720000, target: 2800000 },
  { period: 'Apr', value: 2650000, target: 2850000 },
  { period: 'May', value: 2780000, target: 2900000 },
  { period: 'Jun', value: 2890000, target: 2950000 },
  { period: 'Jul', value: 2847500, target: 3000000 },
];

const financialMetrics = [
  { label: 'EBITDA', value: '$11.25M', change: '+7.14%', positive: true },
  { label: 'Operating Cash Flow', value: '$9.75M', change: '+5.2%', positive: true },
  { label: 'Free Cash Flow', value: '$7.2M', change: '+3.8%', positive: true },
  { label: 'Debt to Equity', value: '0.42', change: '-2.3%', positive: true },
];

const expenseColumns = [
  {
    id: 'category',
    header: 'Category',
    accessor: 'name' as const,
    cell: (row: any) => <span className="font-medium">{row.name}</span>,
  },
  {
    id: 'amount',
    header: 'Amount',
    accessor: 'value' as const,
    cell: (row: any) => formatCurrency(row.value, 'USD'),
  },
  {
    id: 'percentage',
    header: '% of Total',
    accessor: 'percentage' as const,
    cell: (row: any) => (
      <div className="flex items-center gap-2">
        <div className="w-16 h-2 bg-muted rounded-full overflow-hidden">
          <div className="h-full bg-primary" style={{ width: `${row.percentage}%` }} />
        </div>
        <span className="text-sm">{row.percentage}%</span>
      </div>
    ),
  },
];

export const Financial: React.FC = () => {
  const { t } = useTranslation();
  const { currency } = useDashboardStore();

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">{t('financial.title')}</h1>
          <p className="text-muted-foreground">Financial performance and metrics</p>
        </div>
      </div>

      {/* Summary Cards */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Total Revenue</CardTitle>
            <DollarSign className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{formatCurrency(28475000, currency)}</div>
            <p className="text-xs text-muted-foreground mt-1 flex items-center gap-1">
              <TrendingUp className="h-3 w-3 text-green-500" />
              +7.4% from last period
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Net Profit</CardTitle>
            <Wallet className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{formatCurrency(8425000, currency)}</div>
            <p className="text-xs text-muted-foreground mt-1 flex items-center gap-1">
              <TrendingUp className="h-3 w-3 text-green-500" />
              +5.97% from last period
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Profit Margin</CardTitle>
            <TrendingUp className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">29.6%</div>
            <p className="text-xs text-muted-foreground mt-1 flex items-center gap-1">
              <TrendingDown className="h-3 w-3 text-red-500" />
              -1.33% from last period
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Expenses</CardTitle>
            <PieChart className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{formatCurrency(20050000, currency)}</div>
            <p className="text-xs text-muted-foreground mt-1">70.4% of revenue</p>
          </CardContent>
        </Card>
      </div>

      {/* Charts */}
      <div className="grid gap-6 md:grid-cols-2">
        <LineChart
          data={revenueData}
          title="Revenue Trend"
          area
          formatValue="currency"
          currency={currency}
        />

        <PieChart
          data={mockExpenseBreakdownData}
          title="Expense Breakdown"
          label
          formatValue="currency"
          currency={currency}
        />
      </div>

      {/* Financial Metrics */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        {financialMetrics.map((metric) => (
          <Card key={metric.label}>
            <CardContent className="p-6">
              <div className="flex items-center justify-between mb-2">
                <span className="text-sm text-muted-foreground">{metric.label}</span>
                <Badge variant={metric.positive ? 'success' : 'destructive'} size="sm">
                  {metric.change}
                </Badge>
              </div>
              <div className="text-xl font-bold">{metric.value}</div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Tabs */}
      <Tabs defaultValue="expenses">
        <TabsList>
          <TabsTrigger value="expenses">Expenses</TabsTrigger>
          <TabsTrigger value="budget">Budget vs Actual</TabsTrigger>
          <TabsTrigger value="forecast">Forecast</TabsTrigger>
        </TabsList>

        <TabsContent value="expenses" className="mt-6">
          <DataTable
            data={mockExpenseBreakdownData}
            columns={expenseColumns}
            keyField="name"
            title="Expense Breakdown"
          />
        </TabsContent>

        <TabsContent value="budget" className="mt-6">
          <Card>
            <CardHeader>
              <CardTitle>Budget vs Actual</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {['Revenue', 'Operating Expenses', 'Marketing', 'R&D', 'G&A'].map((item, i) => {
                  const actual = Math.random() * 30 + 70;
                  const budget = 100;
                  return (
                    <div key={item} className="space-y-2">
                      <div className="flex items-center justify-between text-sm">
                        <span className="font-medium">{item}</span>
                        <span className={actual >= budget ? 'text-green-600' : 'text-red-600'}>
                          {actual >= budget ? '+' : '-'}{Math.abs(actual - budget).toFixed(1)}%
                        </span>
                      </div>
                      <Progress value={actual} showLabel />
                    </div>
                  );
                })}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="forecast" className="mt-6">
          <BarChart
            data={[
              { name: 'Q1', value: 6500000, target: 7000000 },
              { name: 'Q2', value: 7200000, target: 7500000 },
              { name: 'Q3', value: 7800000, target: 8000000 },
              { name: 'Q4', value: 8500000, target: 9000000 },
            ]}
            title="Revenue Forecast"
            formatValue="currency"
            currency={currency}
          />
        </TabsContent>
      </Tabs>
    </div>
  );
};
