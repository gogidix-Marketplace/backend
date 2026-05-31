import React from 'react';
import { useTranslation } from 'react-i18next';
import {
  Activity,
  Zap,
  Clock,
  CheckCircle,
  AlertTriangle,
  TrendingUp,
} from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '../components/common/Card';
import { Badge } from '../components/common/Badge';
import { Progress } from '../components/common/Progress';
import { BarChart } from '../components/charts/BarChart';
import { DataTable } from '../components/common/DataTable';
import { formatNumber } from '../utils/formatters';
import { mockOperationalMetrics } from '../utils/mock-data';

const operationalData = [
  { name: 'Efficiency', value: 87.3, target: 90, icon: Activity },
  { name: 'Productivity', value: 92.1, target: 95, icon: TrendingUp },
  { name: 'Utilization', value: 78.5, target: 85, icon: Zap },
  { name: 'Quality', value: 96.8, target: 98, icon: CheckCircle },
  { name: 'On-Time Delivery', value: 94.5, target: 95, icon: Clock },
];

const performanceColumns = [
  {
    id: 'region',
    header: 'Region',
    accessor: 'region' as const,
  },
  {
    id: 'efficiency',
    header: 'Efficiency',
    accessor: 'efficiency' as const,
    cell: (row: any) => (
      <div className="flex items-center gap-2">
        <Progress value={row.efficiency} className="w-16" showLabel={false} />
        <span className="text-sm">{row.efficiency}%</span>
      </div>
    ),
  },
  {
    id: 'throughput',
    header: 'Throughput',
    accessor: 'throughput' as const,
    cell: (row: any) => `${row.throughput}%`,
  },
  {
    id: 'quality',
    header: 'Quality',
    accessor: 'quality' as const,
    cell: (row: any) => {
      const variant = row.quality >= 95 ? 'success' : row.quality >= 90 ? 'info' : 'warning';
      return <Badge variant={variant} size="sm">{row.quality}%</Badge>;
    },
  },
];

const performanceData = [
  { region: 'North America', efficiency: 92, throughput: 96, quality: 98 },
  { region: 'Europe', efficiency: 88, throughput: 94, quality: 96 },
  { region: 'Asia Pacific', efficiency: 85, throughput: 91, quality: 94 },
  { region: 'Latin America', efficiency: 78, throughput: 87, quality: 91 },
  { region: 'Middle East', efficiency: 82, throughput: 89, quality: 93 },
];

export const Operations: React.FC = () => {
  const { t } = useTranslation();

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">{t('operations.title')}</h1>
          <p className="text-muted-foreground">Operational performance metrics</p>
        </div>
      </div>

      {/* Summary Cards */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Efficiency</CardTitle>
            <Activity className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{mockOperationalMetrics.efficiency}%</div>
            <Progress value={mockOperationalMetrics.efficiency} className="mt-2" />
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Productivity</CardTitle>
            <TrendingUp className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{mockOperationalMetrics.productivity}%</div>
            <p className="text-xs text-muted-foreground mt-1">+3.2% from last month</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Utilization</CardTitle>
            <Zap className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{mockOperationalMetrics.utilization}%</div>
            <p className="text-xs text-muted-foreground mt-1">Capacity available</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Quality Score</CardTitle>
            <CheckCircle className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{mockOperationalMetrics.quality}%</div>
            <p className="text-xs text-green-600 mt-1">Excellent</p>
          </CardContent>
        </Card>
      </div>

      {/* Operational Metrics */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-5">
        {operationalData.map((item) => {
          const Icon = item.icon;
          return (
            <Card key={item.name}>
              <CardContent className="p-6">
                <div className="flex items-center justify-between mb-4">
                  <Icon className="h-5 w-5 text-primary" />
                  <Badge
                    variant={item.value >= 90 ? 'success' : item.value >= 80 ? 'info' : 'warning'}
                    size="sm"
                  >
                    {item.value >= item.target ? 'On Track' : 'Below Target'}
                  </Badge>
                </div>
                <h3 className="text-sm font-medium text-muted-foreground mb-2">{item.name}</h3>
                <div className="text-2xl font-bold mb-2">{item.value}%</div>
                <Progress value={(item.value / item.target) * 100} size="sm" />
                <p className="text-xs text-muted-foreground mt-2">Target: {item.target}%</p>
              </CardContent>
            </Card>
          );
        })}
      </div>

      {/* Charts */}
      <div className="grid gap-6 md:grid-cols-2">
        <BarChart
          data={performanceData.map(d => ({
            name: d.region,
            value: d.efficiency,
          }))}
          title="Efficiency by Region"
          formatValue="percent"
        />

        <BarChart
          data={performanceData.map(d => ({
            name: d.region,
            value: d.throughput,
          }))}
          title="Throughput by Region"
          formatValue="percent"
        />
      </div>

      {/* Regional Performance Table */}
      <DataTable
        data={performanceData}
        columns={performanceColumns}
        keyField="region"
        title="Regional Operational Performance"
        pagination={{
          pageSize: 10,
          currentPage: 0,
          onPageChange: () => {},
        }}
      />

      {/* Additional Metrics */}
      <div className="grid gap-4 md:grid-cols-3">
        <Card>
          <CardContent className="p-6">
            <h3 className="text-sm font-medium text-muted-foreground mb-2">On-Time Delivery</h3>
            <div className="text-2xl font-bold">{mockOperationalMetrics.onTimeDelivery}%</div>
            <p className="text-xs text-green-600 mt-1">+2.1% improvement</p>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <h3 className="text-sm font-medium text-muted-foreground mb-2">Inventory Turnover</h3>
            <div className="text-2xl font-bold">{mockOperationalMetrics.inventoryTurnover}x</div>
            <p className="text-xs text-muted-foreground mt-1">Per year</p>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <h3 className="text-sm font-medium text-muted-foreground mb-2">Order Fulfillment</h3>
            <div className="text-2xl font-bold">{mockOperationalMetrics.orderFulfillmentTime} days</div>
            <p className="text-xs text-green-600 mt-1">-0.5 days faster</p>
          </CardContent>
        </Card>
      </div>
    </div>
  );
};
