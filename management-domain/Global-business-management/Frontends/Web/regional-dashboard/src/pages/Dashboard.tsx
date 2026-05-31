import React, { useState } from 'react';
import { regions, countries, kpiData, drillDownData } from '../utils/mock-data';
import { useRegionalStore } from '../stores/regional-store';
import { Card, CardHeader, CardTitle, CardContent } from '../components/common/Card';
import { Button } from '../components/common/Button';
import { Badge } from '../components/common/Badge';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '../components/common/Tabs';
import { ArrowUpRight, ArrowDownRight, TrendingUp, Users, DollarSign, Globe2, MapPin } from 'lucide-react';
import { cn, formatCurrency, formatNumber, formatPercent } from '../utils/cn';

// Simple Chart Components (inline for this app)
const ProgressBar = ({ value, max = 100, label, color = 'bg-primary' }: any) => (
  <div className="space-y-1">
    <div className="flex justify-between text-sm">
      <span className="text-muted-foreground">{label}</span>
      <span className="font-medium">{formatPercent(value)}</span>
    </div>
    <div className="h-2 w-full bg-muted rounded-full overflow-hidden">
      <div className={cn('h-full transition-all', color)} style={{ width: `${Math.min(value, max)}%` }} />
    </div>
  </div>
);

const RegionalCard = ({ region, onClick }: any) => {
  const getGrowthIcon = (growth: number) =>
    growth > 10 ? (
      <ArrowUpRight className="h-4 w-4 text-green-500" />
    ) : growth > 0 ? (
      <ArrowUpRight className="h-4 w-4 text-yellow-500" />
    ) : (
      <ArrowDownRight className="h-4 w-4 text-red-500" />
    );

  return (
    <Card
      className="cursor-pointer transition-all hover:shadow-md hover:scale-[1.02]"
      onClick={() => onClick(region.id)}
    >
      <CardContent className="p-6">
        <div className="flex items-start justify-between mb-4">
          <div className="flex items-center gap-3">
            <div className="flex items-center justify-center w-12 h-12 rounded-lg bg-primary/10">
              <span className="text-lg font-bold text-primary">{region.code}</span>
            </div>
            <div>
              <h3 className="font-semibold">{region.name}</h3>
              <p className="text-sm text-muted-foreground">{region.manager}</p>
            </div>
          </div>
          {getGrowthIcon(region.growth)}
        </div>

        <div className="space-y-3">
          <div className="flex justify-between items-baseline">
            <span className="text-sm text-muted-foreground">Revenue</span>
            <span className="text-xl font-bold">{formatCurrency(region.revenue)}</span>
          </div>

          <div className="flex justify-between items-baseline">
            <span className="text-sm text-muted-foreground">Customers</span>
            <span className="font-medium">{formatNumber(region.customers)}</span>
          </div>

          <div className="flex justify-between items-center">
            <span className="text-sm text-muted-foreground">Growth</span>
            <Badge variant={region.growth > 10 ? 'success' : region.growth > 5 ? 'info' : 'warning'}>
              +{region.growth}%
            </Badge>
          </div>

          <div className="pt-2 border-t">
            <ProgressBar label="Satisfaction" value={(region.satisfaction / 5) * 100} />
          </div>
        </div>
      </CardContent>
    </Card>
  );
};

const CountryRow = ({ country }: any) => (
  <div className="flex items-center justify-between p-3 border-b last:border-0 hover:bg-accent rounded-lg transition-colors">
    <div className="flex items-center gap-3">
      <span className="text-2xl">{country.flag}</span>
      <div>
        <p className="font-medium">{country.name}</p>
        <p className="text-xs text-muted-foreground">{country.code}</p>
      </div>
    </div>
    <div className="flex items-center gap-6 text-sm">
      <div className="text-right">
        <p className="text-muted-foreground">Revenue</p>
        <p className="font-medium">{formatCurrency(country.revenue)}</p>
      </div>
      <Badge variant={country.growth > 10 ? 'success' : 'info'}>+{country.growth}%</Badge>
    </div>
  </div>
);

export const Dashboard = () => {
  const {
    drillDownLevel,
    selectedRegion,
    selectedCountry,
    setSelectedRegion,
    setSelectedCountry,
  } = useRegionalStore();

  const currentRegion = regions.find((r: any) => r.id === selectedRegion);
  const currentCountry = selectedCountry
    ? Object.values(countries).find((c: any) => c.id === selectedCountry)
    : null;

  // Global View
  if (drillDownLevel === 'global') {
    return (
      <div className="space-y-6">
        {/* Header Stats */}
        <div className="grid gap-4 md:grid-cols-4">
          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground">Total Revenue</p>
                  <p className="text-2xl font-bold">{formatCurrency(kpiData.totalRevenue)}</p>
                </div>
                <DollarSign className="h-8 w-8 text-green-500" />
              </div>
              <p className="text-xs text-green-600 mt-2">+{kpiData.revenueGrowth}% from last period</p>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground">Total Customers</p>
                  <p className="text-2xl font-bold">{formatNumber(kpiData.totalCustomers)}</p>
                </div>
                <Users className="h-8 w-8 text-blue-500" />
              </div>
              <p className="text-xs text-green-600 mt-2">+{kpiData.customerGrowth}% growth</p>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground">Avg Satisfaction</p>
                  <p className="text-2xl font-bold">{kpiData.avgSatisfaction}/5</p>
                </div>
                <TrendingUp className="h-8 w-8 text-purple-500" />
              </div>
              <p className="text-xs text-muted-foreground mt-2">Across all regions</p>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <div className="flex items-center justify-between">
                <div>
                  <p className="text-sm text-muted-foreground">Top Region</p>
                  <p className="text-lg font-bold">{kpiData.topRegion}</p>
                </div>
                <Globe2 className="h-8 w-8 text-orange-500" />
              </div>
              <p className="text-xs text-muted-foreground mt-2">By revenue</p>
            </CardContent>
          </Card>
        </div>

        {/* Regional Cards */}
        <div>
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-xl font-semibold">Regional Overview</h2>
            <Button variant="outline" size="sm">
              <MapPin className="h-4 w-4 mr-2" />
              View Map
            </Button>
          </div>
          <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
            {regions.map((region: any) => (
              <RegionalCard key={region.id} region={region} onClick={setSelectedRegion} />
            ))}
          </div>
        </div>

        {/* Comparison Table */}
        <Card>
          <CardHeader>
            <CardTitle>Revenue Comparison</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-4">
              {regions.map((region: any) => (
                <div key={region.id} className="space-y-2">
                  <div className="flex justify-between text-sm">
                    <span className="font-medium">{region.name}</span>
                    <span>{formatCurrency(region.revenue)}</span>
                  </div>
                  <div className="h-3 w-full bg-muted rounded-full overflow-hidden">
                    <div
                      className="h-full bg-primary"
                      style={{ width: `${(region.revenue / kpiData.totalRevenue) * 100}%` }}
                    />
                  </div>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    );
  }

  // Regional View
  if (drillDownLevel === 'regional' && currentRegion) {
    const regionCountries = Object.values(countries).filter(
      (c: any) => c.regionId === currentRegion.id
    );

    return (
      <div className="space-y-6">
        {/* Region Header */}
        <div className="flex items-center justify-between">
          <div>
            <h2 className="text-2xl font-bold">{currentRegion.name}</h2>
            <p className="text-muted-foreground">Regional Manager: {currentRegion.manager}</p>
          </div>
          <div className="flex gap-2">
            <Badge variant="success" size="md">Top Performer</Badge>
            <Badge variant="info" size="md">+{currentRegion.growth}% Growth</Badge>
          </div>
        </div>

        {/* KPI Cards */}
        <div className="grid gap-4 md:grid-cols-4">
          <Card>
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground">Revenue</p>
              <p className="text-2xl font-bold">{formatCurrency(currentRegion.revenue)}</p>
              <p className="text-xs text-green-600 mt-1">Share: {((currentRegion.revenue / kpiData.totalRevenue) * 100).toFixed(1)}%</p>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground">Profit</p>
              <p className="text-2xl font-bold">{formatCurrency(currentRegion.profit)}</p>
              <p className="text-xs text-muted-foreground mt-1">Margin: {((currentRegion.profit / currentRegion.revenue) * 100).toFixed(1)}%</p>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground">Customers</p>
              <p className="text-2xl font-bold">{formatNumber(currentRegion.customers)}</p>
              <p className="text-xs text-green-600 mt-1">Growing</p>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground">Satisfaction</p>
              <p className="text-2xl font-bold">{currentRegion.satisfaction}/5</p>
              <p className="text-xs text-green-600 mt-1">Excellent</p>
            </CardContent>
          </Card>
        </div>

        {/* Countries */}
        <Card>
          <CardHeader>
            <CardTitle>Countries in {currentRegion.name}</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="space-y-1">
              {regionCountries.map((country: any) => (
                <div
                  key={country.id}
                  className="cursor-pointer"
                  onClick={() => setSelectedCountry(country.id)}
                >
                  <CountryRow country={country} />
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    );
  }

  // Country View
  if (drillDownLevel === 'country' && currentCountry) {
    return (
      <div className="space-y-6">
        <div className="flex items-center gap-4">
          <span className="text-5xl">{currentCountry.flag}</span>
          <div>
            <h2 className="text-2xl font-bold">{currentCountry.name}</h2>
            <p className="text-muted-foreground">{currentCountry.code} - {currentCountry.currency}</p>
          </div>
        </div>

        <div className="grid gap-4 md:grid-cols-3">
          <Card>
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground">Revenue</p>
              <p className="text-2xl font-bold">{formatCurrency(currentCountry.revenue)}</p>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground">Customers</p>
              <p className="text-2xl font-bold">{formatNumber(currentCountry.customers)}</p>
            </CardContent>
          </Card>

          <Card>
            <CardContent className="p-6">
              <p className="text-sm text-muted-foreground">Growth</p>
              <p className="text-2xl font-bold text-green-600">+{currentCountry.growth}%</p>
            </CardContent>
          </Card>
        </div>

        <Card>
          <CardHeader>
            <CardTitle>Major Cities</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="grid gap-2 md:grid-cols-3">
              {currentCountry.cities.map((city: string, i: number) => (
                <div key={i} className="p-3 border rounded-lg text-center">
                  <MapPin className="h-4 w-4 mx-auto mb-1 text-primary" />
                  <p className="font-medium">{city}</p>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </div>
    );
  }

  return null;
};
