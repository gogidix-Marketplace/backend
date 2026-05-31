import React, { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { Building2, MapPin, Users, TrendingUp, DollarSign } from 'lucide-react';
import { useRegionalMetrics } from '../hooks/use-api';
import { useDashboardStore } from '../stores/dashboard-store';
import { Card, CardContent, CardHeader, CardTitle } from '../components/common/Card';
import { Badge } from '../components/common/Badge';
import { Button } from '../components/common/Button';
import { BarChart } from '../components/charts/BarChart';
import { DataTable } from '../components/tables/DataTable';
import { Modal, ModalBody, ModalFooter, ModalHeader } from '../components/common/Modal';
import { formatCurrency } from '../utils/currency';
import { mockRegionalMetrics } from '../utils/mock-data';

export const Regions: React.FC = () => {
  const { t } = useTranslation();
  const { currency } = useDashboardStore();
  const { data: regionalData, isLoading } = useRegionalMetrics();
  const [selectedRegion, setSelectedRegion] = useState<any | null>(null);

  const regionColumns = [
    {
      id: 'regionName',
      header: t('regions.region'),
      accessor: 'regionName' as const,
      cell: (row: any) => (
        <div className="flex items-center gap-3">
          <div className="flex items-center justify-center w-10 h-10 rounded-lg bg-primary/10 text-primary">
            <MapPin className="h-5 w-5" />
          </div>
          <div>
            <p className="font-medium">{row.regionName}</p>
            <p className="text-sm text-muted-foreground">{row.regionId}</p>
          </div>
        </div>
      ),
    },
    {
      id: 'revenue',
      header: t('financial.revenue'),
      accessor: 'revenue' as const,
      sortable: true,
      cell: (row: any) => (
        <div>
          <p className="font-medium">{formatCurrency(row.revenue, currency)}</p>
          <p className="text-sm text-muted-foreground">{row.revenueShare}% share</p>
        </div>
      ),
    },
    {
      id: 'growth',
      header: t('dashboard.growth'),
      accessor: 'growth' as const,
      sortable: true,
      cell: (row: any) => (
        <div className="flex items-center gap-2">
          <TrendingUp className={`h-4 w-4 ${row.growth > 10 ? 'text-green-500' : 'text-yellow-500'}`} />
          <span className="font-medium">{row.growth > 0 ? '+' : ''}{row.growth}%</span>
        </div>
      ),
    },
    {
      id: 'customers',
      header: t('customers.title'),
      accessor: 'customers' as const,
      sortable: true,
      cell: (row: any) => (
        <div className="flex items-center gap-2">
          <Users className="h-4 w-4 text-muted-foreground" />
          <span>{row.customers.toLocaleString()}</span>
        </div>
      ),
    },
    {
      id: 'satisfaction',
      header: t('customers.satisfaction'),
      accessor: 'satisfaction' as const,
      cell: (row: any) => (
        <div className="flex items-center gap-2">
          <div className="w-12 h-2 bg-muted rounded-full overflow-hidden">
            <div
              className="h-full bg-green-500"
              style={{ width: `${(row.satisfaction / 5) * 100}%` }}
            />
          </div>
          <span className="text-sm font-medium">{row.satisfaction}/5</span>
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
        };
        const config = variants[row.performance] || variants.average;
        return <Badge variant={config.variant} size="sm">{config.label}</Badge>;
      },
    },
    {
      id: 'actions',
      header: t('tables.actions'),
      cell: (row: any) => (
        <Button variant="ghost" size="sm" onClick={() => setSelectedRegion(row)}>
          {t('tables.viewDetails')}
        </Button>
      ),
    },
  ];

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">{t('regions.title')}</h1>
          <p className="text-muted-foreground">{t('regions.overview')}</p>
        </div>
        <Button>
          <MapPin className="h-4 w-4 mr-2" />
          View Map
        </Button>
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
            <p className="text-xs text-muted-foreground mt-1">+7.4% from last month</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Total Regions</CardTitle>
            <Building2 className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">5</div>
            <p className="text-xs text-muted-foreground mt-1">Active regions</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Total Customers</CardTitle>
            <Users className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">48,756</div>
            <p className="text-xs text-muted-foreground mt-1">+7.8% growth</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Avg Satisfaction</CardTitle>
            <TrendingUp className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">4.5/5</div>
            <p className="text-xs text-muted-foreground mt-1">Across all regions</p>
          </CardContent>
        </Card>
      </div>

      {/* Charts */}
      <div className="grid gap-6 md:grid-cols-2">
        <BarChart
          data={mockRegionalMetrics.map(m => ({
            name: m.regionName,
            value: m.revenue,
          }))}
          title="Revenue by Region"
          formatValue="currency"
          currency={currency}
        />

        <BarChart
          data={mockRegionalMetrics.map(m => ({
            name: m.regionName,
            value: m.growth,
          }))}
          title="Growth Rate by Region"
          formatValue="percent"
          colors={['#22c55e', '#22c55e', '#f59e0b', '#f59e0b', '#ef4444']}
        />
      </div>

      {/* Regions Table */}
      <DataTable
        data={mockRegionalMetrics}
        columns={regionColumns}
        keyField="regionId"
        title="All Regions"
        searchable
        sortable
        selectable
        pagination={{
          pageSize: 10,
          currentPage: 0,
          onPageChange: () => {},
        }}
      />

      {/* Region Detail Modal */}
      <Modal
        isOpen={!!selectedRegion}
        onClose={() => setSelectedRegion(null)}
        title={selectedRegion?.regionName}
        size="lg"
      >
        {selectedRegion && (
          <>
            <ModalBody>
              <div className="grid gap-6 md:grid-cols-2">
                <div>
                  <h3 className="font-semibold mb-4">Financial Metrics</h3>
                  <div className="space-y-3">
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">Revenue</span>
                      <span className="font-medium">{formatCurrency(selectedRegion.revenue, currency)}</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">Profit</span>
                      <span className="font-medium">{formatCurrency(selectedRegion.profit, currency)}</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">Growth</span>
                      <span className="font-medium text-green-600">{selectedRegion.growth}%</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">Revenue Share</span>
                      <span className="font-medium">{selectedRegion.revenueShare}%</span>
                    </div>
                  </div>
                </div>

                <div>
                  <h3 className="font-semibold mb-4">Operational Metrics</h3>
                  <div className="space-y-3">
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">Customers</span>
                      <span className="font-medium">{selectedRegion.customers.toLocaleString()}</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">Satisfaction</span>
                      <span className="font-medium">{selectedRegion.satisfaction}/5</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">Market Penetration</span>
                      <span className="font-medium">{selectedRegion.marketPenetration}%</span>
                    </div>
                    <div className="flex justify-between">
                      <span className="text-muted-foreground">Performance</span>
                      <Badge variant={selectedRegion.performance === 'excellent' ? 'success' : 'info'}>
                        {selectedRegion.performance}
                      </Badge>
                    </div>
                  </div>
                </div>
              </div>
            </ModalBody>
            <ModalFooter>
              <Button variant="outline" onClick={() => setSelectedRegion(null)}>
                Close
              </Button>
              <Button>View Full Report</Button>
            </ModalFooter>
          </>
        )}
      </Modal>
    </div>
  );
};
