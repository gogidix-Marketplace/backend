import React from 'react';
import { useTranslation } from 'react-i18next';
import { Users, UserPlus, UserMinus, Heart, MessageSquare } from 'lucide-react';
import { Card, CardContent, CardHeader, CardTitle } from '../components/common/Card';
import { Badge } from '../components/common/Badge';
import { LineChart } from '../components/charts/LineChart';
import { BarChart } from '../components/charts/BarChart';
import { DataTable } from '../components/common/DataTable';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '../components/common/Tabs';
import { formatNumber } from '../utils/formatters';
import { mockCustomerMetrics } from '../utils/mock-data';

const customerTrendData = [
  { period: 'Jan', value: 42000, newCustomers: 2800, churned: 450 },
  { period: 'Feb', value: 43500, newCustomers: 3100, churned: 520 },
  { period: 'Mar', value: 45100, newCustomers: 3300, churned: 480 },
  { period: 'Apr', value: 46500, newCustomers: 2900, churned: 610 },
  { period: 'May', value: 47800, newCustomers: 3500, churned: 540 },
  { period: 'Jun', value: 48756, newCustomers: 3522, churned: 890 },
];

const segmentColumns = [
  {
    id: 'segment',
    header: 'Segment',
    accessor: 'segment' as const,
    cell: (row: any) => <span className="font-medium">{row.segment}</span>,
  },
  {
    id: 'customers',
    header: 'Customers',
    accessor: 'count' as const,
    cell: (row: any) => formatNumber(row.count),
  },
  {
    id: 'revenue',
    header: 'Revenue',
    accessor: 'revenue' as const,
    cell: (row: any) => `$${(row.revenue / 1000000).toFixed(1)}M`,
  },
  {
    id: 'growth',
    header: 'Growth',
    accessor: 'growth' as const,
    cell: (row: any) => (
      <Badge variant={row.growth > 10 ? 'success' : 'info'} size="sm">
        +{row.growth}%
      </Badge>
    ),
  },
  {
    id: 'satisfaction',
    header: 'Satisfaction',
    accessor: 'satisfaction' as const,
    cell: (row: any) => (
      <div className="flex items-center gap-2">
        <Heart className="h-4 w-4 text-red-500 fill-red-500" />
        <span>{row.satisfaction}/5</span>
      </div>
    ),
  },
];

const feedbackData = [
  { id: 1, customer: 'Acme Corp', rating: 5, comment: 'Excellent service and support!', date: '2024-01-15' },
  { id: 2, customer: 'TechStart Inc', rating: 4, comment: 'Good product, minor issues with UI.', date: '2024-01-14' },
  { id: 3, customer: 'Global Logistics', rating: 5, comment: 'Helped us streamline operations.', date: '2024-01-13' },
];

const feedbackColumns = [
  { id: 'customer', header: 'Customer', accessor: 'customer' as const },
  {
    id: 'rating',
    header: 'Rating',
    accessor: 'rating' as const,
    cell: (row: any) => (
      <div className="flex">
        {Array.from({ length: 5 }).map((_, i) => (
          <Star
            key={i}
            className={`h-4 w-4 ${i < row.rating ? 'fill-yellow-400 text-yellow-400' : 'text-gray-300'}`}
          />
        ))}
      </div>
    ),
  },
  { id: 'comment', header: 'Comment', accessor: 'comment' as const },
  { id: 'date', header: 'Date', accessor: 'date' as const },
];

const Star = ({ className }: { className?: string }) => (
  <svg
    xmlns="http://www.w3.org/2000/svg"
    viewBox="0 0 24 24"
    fill="currentColor"
    className={className}
  >
    <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z" />
  </svg>
);

export const Customers: React.FC = () => {
  const { t } = useTranslation();

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">{t('customers.title')}</h1>
          <p className="text-muted-foreground">Customer analytics and satisfaction metrics</p>
        </div>
      </div>

      {/* Summary Cards */}
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Total Customers</CardTitle>
            <Users className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{formatNumber(mockCustomerMetrics.total)}</div>
            <p className="text-xs text-muted-foreground mt-1">Active customers</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">New Customers</CardTitle>
            <UserPlus className="h-4 w-4 text-green-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">+{formatNumber(mockCustomerMetrics.new)}</div>
            <p className="text-xs text-muted-foreground mt-1">This month</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Churned</CardTitle>
            <UserMinus className="h-4 w-4 text-red-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold text-red-600">{formatNumber(mockCustomerMetrics.churned)}</div>
            <p className="text-xs text-muted-foreground mt-1">{mockCustomerMetrics.churnRate}% churn rate</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="flex flex-row items-center justify-between pb-2">
            <CardTitle className="text-sm font-medium">Satisfaction</CardTitle>
            <Heart className="h-4 w-4 text-red-500 fill-red-500" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">4.6/5</div>
            <p className="text-xs text-muted-foreground mt-1">Average rating</p>
          </CardContent>
        </Card>
      </div>

      {/* Charts */}
      <div className="grid gap-6 md:grid-cols-2">
        <LineChart
          data={customerTrendData}
          title="Customer Growth Trend"
          area
          formatValue="number"
        />

        <BarChart
          data={mockCustomerMetrics.segments.map(s => ({
            name: s.segment,
            value: s.count,
          }))}
          title="Customer Distribution"
          formatValue="number"
        />
      </div>

      {/* Additional Metrics */}
      <div className="grid gap-4 md:grid-cols-3">
        <Card>
          <CardContent className="p-6">
            <h3 className="text-sm font-medium text-muted-foreground mb-2">Retention Rate</h3>
            <div className="text-2xl font-bold text-green-600">{mockCustomerMetrics.retentionRate}%</div>
            <p className="text-xs text-muted-foreground mt-1">Industry avg: 85%</p>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <h3 className="text-sm font-medium text-muted-foreground mb-2">Acquisition Cost</h3>
            <div className="text-2xl font-bold">${mockCustomerMetrics.acquisitionCost}</div>
            <p className="text-xs text-muted-foreground mt-1">Per new customer</p>
          </CardContent>
        </Card>

        <Card>
          <CardContent className="p-6">
            <h3 className="text-sm font-medium text-muted-foreground mb-2">Lifetime Value</h3>
            <div className="text-2xl font-bold">${formatNumber(mockCustomerMetrics.lifetimeValue)}</div>
            <p className="text-xs text-muted-foreground mt-1">Average LTV</p>
          </CardContent>
        </Card>
      </div>

      {/* Tabs */}
      <Tabs defaultValue="segments">
        <TabsList>
          <TabsTrigger value="segments">Segments</TabsTrigger>
          <TabsTrigger value="feedback">Feedback</TabsTrigger>
          <TabsTrigger value="surveys">Surveys</TabsTrigger>
        </TabsList>

        <TabsContent value="segments" className="mt-6">
          <DataTable
            data={mockCustomerMetrics.segments}
            columns={segmentColumns}
            keyField="segment"
            title="Customer Segments"
          />
        </TabsContent>

        <TabsContent value="feedback" className="mt-6">
          <Card>
            <CardHeader>
              <CardTitle className="flex items-center gap-2">
                <MessageSquare className="h-5 w-5" />
                Recent Feedback
              </CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {feedbackData.map((feedback) => (
                  <div key={feedback.id} className="p-4 border rounded-lg">
                    <div className="flex items-start justify-between mb-2">
                      <span className="font-medium">{feedback.customer}</span>
                      <div className="flex">
                        {Array.from({ length: 5 }).map((_, i) => (
                          <Star
                            key={i}
                            className={`h-4 w-4 ${i < feedback.rating ? 'fill-yellow-400 text-yellow-400' : 'text-gray-300'}`}
                          />
                        ))}
                      </div>
                    </div>
                    <p className="text-sm text-muted-foreground">{feedback.comment}</p>
                    <p className="text-xs text-muted-foreground mt-2">{feedback.date}</p>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </TabsContent>

        <TabsContent value="surveys" className="mt-6">
          <Card>
            <CardContent className="p-12 text-center">
              <MessageSquare className="h-12 w-12 mx-auto mb-4 text-muted-foreground" />
              <h3 className="text-lg font-semibold mb-2">No Active Surveys</h3>
              <p className="text-muted-foreground mb-4">
                Create customer surveys to gather valuable feedback.
              </p>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  );
};
