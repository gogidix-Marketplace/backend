import React from 'react';
import { useTranslation } from 'react-i18next';
import { TrendingUp, DollarSign, Users, BarChart3, ArrowUpRight } from 'lucide-react';
import { useKPIMetrics } from '../hooks/use-api';
import { useDashboardStore } from '../stores/dashboard-store';
import { KPIGrid } from '../components/dashboard/KPIGrid';
import { LineChart, LineChartSkeleton } from '../components/charts/LineChart';
import { BarChart, BarChartSkeleton } from '../components/charts/BarChart';
import { PieChart, DonutChart, PieChartSkeleton } from '../components/charts/PieChart';
import { DataTable } from '../components/tables/DataTable';
import { Card, CardContent, CardHeader, CardTitle } from '../components/common/Card';
import { Badge } from '../components/common/Badge';
import { Button } from '../components/common/Button';
import { formatCurrency } from '../utils/currency';
import { mockRegionalMetrics, mockMarketDistributionData } from '../utils/mock-data';

export const Dashboard: React.FC = () => {
  const { t } = useTranslation();
  const { currency, selectedPeriod } = useDashboardStore();
  const { data: kpiData, isLoading: kpiLoading, refetch } = useKPIMetrics();

  // Regional metrics columns
  const regionColumns = [
    {
      id: 'regionName',
      header: t('regions.region'),
      accessor: 'regionName' as const,
      cell: (row: any) => (
        <div className="font-medium">{row.regionName}</div>
      ),
    },
    {
      id: 'revenue',
      header: t('financial.revenue'),
      accessor: 'revenue' as const,
      cell: (row: any) => formatCurrency(row.revenue, currency),
    },
    {
      id: 'growth',
      header: t('dashboard.growth'),
      accessor: 'growth' as const,
      cell: (row: any) => (
        <Badge variant={row.growth > 10 ? 'success' : row.growth > 5 ? 'info' : 'warning'} size="sm">
          {row.growth > 0 ? '+' : ''}{row.growth}%
        </Badge>
      ),
    },
    {
      id: 'profit',
      header: t('financial.profit'),
      accessor: 'profit' as const,
      cell: (row: any) => formatCurrency(row.profit, currency),
    },
    {
      id: 'customers',
      header: t('customers.title'),
      accessor: 'customers' as const,
      cell: (row: any) => row.customers.toLocaleString(),
    },
    {
      id: 'satisfaction',
      header: t('customers.satisfaction'),
      accessor: 'satisfaction' as const,
      cell: (row: any) => (
        <div className="flex items-center gap-2">
          <div className="w-16 h-2 bg-muted rounded-full overflow-hidden">
            <div
              className="h-full bg-green-500"
              style={{ width: `${(row.satisfaction / 5) * 100}%` }}
            />
          </div>
          <span className="text-sm">{row.satisfaction}/5</span>
        </div>
      ),
    },
    {
      id: 'performance',
      header: t('regions.performance'),
      accessor: 'performance' as const,
      cell: (row: any) => {
        const variants: Record<string, any> = {
          excellent: { variant: 'success', label: 'Excellent' },
          good: { variant: 'info', label: 'Good' },
          average: { variant: 'warning', label: 'Average' },
          'below-average': { variant: 'destructive', label: 'Below Avg' },
          poor: { variant: 'destructive', label: 'Poor' },
        };
        const config = variants[row.performance] || variants.average;
        return <Badge variant={config.variant} size="sm">{config.label}</Badge>;
      },
    },
  ];

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">{t('dashboard.title')}</h1>
          <p className="text-muted-foreground">
            {t('dashboard.overview')} - {t(`dashboard.period.${selectedPeriod}`)}
          </p>
        </div>
        <div className="flex gap-2">
          <Button variant="outline" onClick={() => refetch()}>
            <BarChart3 className="h-4 w-4 mr-2" />
            Refresh
          </Button>
          <Button>
            <ArrowUpRight className="h-4 w-4 mr-2" />
            Export Report
          </Button>
        </div>
      </div>

      {/* KPI Cards */}
      <KPIGrid limit={4} />

      {/* Charts Row */}
      <div className="grid gap-6 md:grid-cols-2">
        {/* Revenue Trend */}
        <LineChart
          data={[
            { period: 'Jan', value: 2450000, target: 2700000 },
            { period: 'Feb', value: 2580000, target: 2750000 },
            { period: 'Mar', value: 2720000, target: 2800000 },
            { period: 'Apr', value: 2650000, target: 2850000 },
            { period: 'May', value: 2780000, target: 2900000 },
            { period: 'Jun', value: 2890000, target: 2950000 },
            { period: 'Jul', value: 2847500, target: 3000000 },
          ]}
          title={t('charts.revenueTrend')}
          area
          color="#3b82f6"
          formatValue="currency"
          currency={currency}
        />

        {/* Regional Performance */}
        <BarChart
          data={mockRegionalMetrics.map(m => ({
            name: m.regionName,
            value: m.revenue,
            growth: m.growth,
          }))}
          title={t('charts.regionalPerformance')}
          horizontal
          formatValue="currency"
          currency={currency}
        />
      </div>

      {/* Secondary Charts Row */}
      <div className="grid gap-6 md:grid-cols-3">
        {/* Market Distribution */}
        <DonutChart
          data={mockMarketDistributionData.map(d => ({
            name: d.name,
            value: d.value,
          }))}
          title={t('charts.marketDistribution')}
          label
        />

        {/* Top Performers */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base flex items-center gap-2">
              <TrendingUp className="h-5 w-5 text-green-500" />
              {t('dashboard.topPerformers')}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {mockRegionalMetrics.slice(0, 4).map((region, index) => (
                <div key={region.id} className="flex items-center gap-4">
                  <div className="flex items-center justify-center w-8 h-8 rounded-full bg-primary/10 text-primary font-semibold text-sm">
                    {index + 1}
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="font-medium truncate">{region.regionName}</p>
                    <p className="text-sm text-muted-foreground">
                      {formatCurrency(region.revenue, currency)}
                    </p>
                  </div>
                  <Badge variant="success" size="sm">
                    +{region.growth}%
                  </Badge>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>

        {/* Quick Stats */}
        <Card>
          <CardHeader>
            <CardTitle className="text-base flex items-center gap-2">
              <Users className="h-5 w-5 text-blue-500" />
              {t('customers.title')}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              <div className="flex items-center justify-between">
                <span className="text-sm text-muted-foreground">Total Customers</span>
                <span className="font-semibold">48,756</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-muted-foreground">New This Month</span>
                <span className="font-semibold text-green-600">+3,522</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-muted-foreground">Retention Rate</span>
                <span className="font-semibold">94.2%</span>
              </div>
              <div className="flex items-center justify-between">
                <span className="text-sm text-muted-foreground">Satisfaction</span>
                <span className="font-semibold">4.6/5</span>
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Regional Data Table */}
      <DataTable
        data={mockRegionalMetrics}
        columns={regionColumns}
        keyField="regionId"
        title={t('regions.title')}
        searchable
        sortable
        pagination={{
          pageSize: 5,
          currentPage: 0,
          onPageChange: () => {},
        }}
      />
    </div>
  );
};
