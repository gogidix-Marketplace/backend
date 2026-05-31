import React from 'react';
import { useTranslation } from 'react-i18next';
import { useCountries, useRegions } from '../hooks/use-api';
import { Card, CardContent, CardHeader, CardTitle } from '../components/common/Card';
import { DataTable } from '../components/tables/DataTable';
import { Select } from '../components/common/Select';
import { Badge } from '../components/common/Badge';
import { formatCurrency, formatNumber } from '../utils/currency';
import { mockCountries } from '../utils/mock-data';

export const Countries: React.FC = () => {
  const { t } = useTranslation();
  const { data: countries, isLoading } = useCountries();
  const { data: regions } = useRegions();
  const { currency } = useDashboardStore();

  const countryColumns = [
    {
      id: 'flag',
      header: '',
      accessor: 'flag' as const,
      cell: (row: any) => (
        <span className="text-2xl">{row.flag}</span>
      ),
    },
    {
      id: 'name',
      header: t('countries.title'),
      accessor: 'name' as const,
      sortable: true,
      cell: (row: any) => (
        <div>
          <p className="font-medium">{row.name}</p>
          <p className="text-sm text-muted-foreground">{row.code}</p>
        </div>
      ),
    },
    {
      id: 'region',
      header: t('regions.region'),
      accessor: 'regionId' as const,
      filterable: true,
      cell: (row: any) => {
        const region = regions?.find((r: any) => r.id === row.regionId);
        return region?.name || '-';
      },
    },
    {
      id: 'currency',
      header: t('countries.currency'),
      accessor: 'currency' as const,
      cell: (row: any) => (
        <Badge variant="outline" size="sm">{row.currency}</Badge>
      ),
    },
    {
      id: 'population',
      header: t('countries.population'),
      accessor: 'population' as const,
      sortable: true,
      cell: (row: any) => formatNumber(row.population),
    },
    {
      id: 'gdp',
      header: t('countries.gdp'),
      accessor: 'gdp' as const,
      sortable: true,
      cell: (row: any) => formatCurrency(row.gdp, 'USD', { showSymbol: false }) + 'B',
    },
    {
      id: 'status',
      header: t('common.status'),
      cell: () => (
        <Badge variant="success" size="sm">Active</Badge>
      ),
    },
  ];

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">{t('countries.title')}</h1>
          <p className="text-muted-foreground">{t('countries.overview')}</p>
        </div>
      </div>

      {/* Summary Stats */}
      <div className="grid gap-4 md:grid-cols-3">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium">Total Countries</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{mockCountries.length}</div>
            <p className="text-xs text-muted-foreground mt-1">Across {regions?.length || 5} regions</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium">Total Population</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">2.2B</div>
            <p className="text-xs text-muted-foreground mt-1">Combined market reach</p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium">Combined GDP</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">$67T</div>
            <p className="text-xs text-muted-foreground mt-1">Market size</p>
          </CardContent>
        </Card>
      </div>

      {/* Countries Table */}
      <DataTable
        data={mockCountries}
        columns={countryColumns}
        keyField="id"
        title="All Countries"
        searchable
        sortable
        selectable
        pagination={{
          pageSize: 10,
          currentPage: 0,
          onPageChange: () => {},
        }}
      />
    </div>
  );
};
