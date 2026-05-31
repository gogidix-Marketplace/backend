import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import { cn, formatCurrency, formatPercentage, calculateProgress, getStatusColor, getRegionName } from '@shared/utils/cn'
import { regionalMetricsData, countryMetricsData } from '@shared/mock-data'
import { Search, MapPin, TrendingUp, TrendingDown, ArrowUpRight, ArrowDownRight, Filter } from 'lucide-react'
import type { Region } from '@shared/types'

export default function RegionalAnalyticsPage() {
  const [selectedRegion, setSelectedRegion] = useState<string | null>(null)
  const [searchQuery, setSearchQuery] = useState('')

  const filteredCountries = selectedRegion
    ? countryMetricsData.filter(c => c.region === selectedRegion)
    : countryMetricsData

  const searchedCountries = filteredCountries.filter(
    c => c.countryName.toLowerCase().includes(searchQuery.toLowerCase())
  )

  const totalRevenue = searchedCountries.reduce((sum, c) => sum + c.totalRevenue, 0)
  const totalTarget = searchedCountries.reduce((sum, c) => sum + c.revenueTarget, 0)

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header">
        <h1 className="page-title">Regional Analytics</h1>
        <p className="page-description">
          Detailed performance analysis by region and country
        </p>
      </div>

      {/* Region Filter */}
      <Card>
        <CardHeader>
          <CardTitle>Filter by Region</CardTitle>
          <CardDescription>Select a region to view detailed analytics</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="flex flex-wrap gap-2">
            <Button
              variant={selectedRegion === null ? 'gbm' : 'outline'}
              size="sm"
              onClick={() => setSelectedRegion(null)}
            >
              All Regions
            </Button>
            {regionalMetricsData.map((region) => (
              <Button
                key={region.id}
                variant={selectedRegion === region.region ? 'gbm' : 'outline'}
                size="sm"
                onClick={() => setSelectedRegion(region.region)}
              >
                {region.regionName}
              </Button>
            ))}
          </div>
        </CardContent>
      </Card>

      {/* Summary Cards */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium">Total Revenue</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{formatCurrency(totalRevenue)}</div>
            <p className="text-xs text-muted-foreground">
              of {formatCurrency(totalTarget)} target
            </p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium">Countries</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{searchedCountries.length}</div>
            <p className="text-xs text-muted-foreground">
              active markets
            </p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium">Avg Growth</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {formatPercentage(searchedCountries.reduce((sum, c) => sum + c.growthRate, 0) / searchedCountries.length || 0)}
            </div>
            <p className="text-xs text-muted-foreground">
              regional average
            </p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="pb-3">
            <CardTitle className="text-sm font-medium">On Track</CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              {searchedCountries.filter(c => c.status === 'on_track' || c.status === 'ahead').length}
            </div>
            <p className="text-xs text-muted-foreground">
              of {searchedCountries.length} countries
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Country Details Table */}
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <div>
              <CardTitle>Country Performance Details</CardTitle>
              <CardDescription>
                {selectedRegion ? `${getRegionName(selectedRegion)} - ` : 'All Regions - '}
                {searchedCountries.length} countries
              </CardDescription>
            </div>
            <div className="flex items-center gap-2">
              <div className="relative">
                <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
                <Input
                  type="search"
                  placeholder="Search countries..."
                  className="pl-9 w-64"
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                />
              </div>
            </div>
          </div>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Country</TableHead>
                <TableHead>Currency</TableHead>
                <TableHead>Local Revenue</TableHead>
                <TableHead>USD Revenue</TableHead>
                <TableHead>Progress</TableHead>
                <TableHead>Growth</TableHead>
                <TableHead>Customers</TableHead>
                <TableHead>Businesses</TableHead>
                <TableHead>Status</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {searchedCountries.map((country) => {
                const progress = calculateProgress(country.totalRevenue, country.revenueTarget)
                return (
                  <TableRow key={country.id}>
                    <TableCell className="font-medium">
                      <div className="flex items-center gap-2">
                        <MapPin className="h-4 w-4 text-muted-foreground" />
                        {country.countryName}
                      </div>
                    </TableCell>
                    <TableCell>
                      <Badge variant="outline">{country.currency}</Badge>
                    </TableCell>
                    <TableCell>
                      {country.localCurrencyRevenue.toLocaleString()} {country.currency}
                    </TableCell>
                    <TableCell>{formatCurrency(country.totalRevenue)}</TableCell>
                    <TableCell>
                      <div className="flex items-center gap-2">
                        <div className="h-2 w-24 overflow-hidden rounded-full bg-slate-200 dark:bg-slate-800">
                          <div
                            className={cn(
                              'h-full',
                              progress >= 100 ? 'bg-green-500' : progress >= 80 ? 'bg-blue-500' : 'bg-yellow-500'
                            )}
                            style={{ width: `${Math.min(progress, 100)}%` }}
                          />
                        </div>
                        <span className="text-xs">{Math.round(progress)}%</span>
                      </div>
                    </TableCell>
                    <TableCell>
                      <span
                        className={cn(
                          'flex items-center gap-1 text-sm',
                          country.growthRate > 0 ? 'text-green-600' : 'text-red-600'
                        )}
                      >
                        {country.growthRate > 0 ? (
                          <ArrowUpRight className="h-4 w-4" />
                        ) : (
                          <ArrowDownRight className="h-4 w-4" />
                        )}
                        {formatPercentage(country.growthRate)}
                      </span>
                    </TableCell>
                    <TableCell>{country.customerCount.toLocaleString()}</TableCell>
                    <TableCell>{country.activeBusinesses.toLocaleString()}</TableCell>
                    <TableCell>
                      <Badge className={getStatusColor(country.status)}>
                        {country.status.replace('_', ' ')}
                      </Badge>
                    </TableCell>
                  </TableRow>
                )
              })}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      {/* Regional Comparison Chart Placeholder */}
      <Card>
        <CardHeader>
          <CardTitle>Revenue Comparison by Region</CardTitle>
          <CardDescription>Visual comparison of regional performance</CardDescription>
        </CardHeader>
        <CardContent>
          <div className="flex h-64 items-end justify-around gap-4">
            {regionalMetricsData.map((region) => {
              const maxHeight = Math.max(...regionalMetricsData.map(r => r.totalRevenue))
              const height = (region.totalRevenue / maxHeight) * 100
              return (
                <div key={region.id} className="flex flex-col items-center gap-2 flex-1">
                  <div className="w-full bg-slate-200 dark:bg-slate-800 rounded-t-lg relative" style={{ height: `${height * 2}px` }}>
                    <div className={cn(
                      'absolute bottom-0 w-full rounded-t-lg transition-all',
                      region.status === 'ahead' ? 'bg-green-500' :
                      region.status === 'on_track' ? 'bg-blue-500' :
                      region.status === 'at_risk' ? 'bg-yellow-500' : 'bg-red-500'
                    )} style={{ height: '100%' }} />
                  </div>
                  <div className="text-xs text-center font-medium truncate w-full">
                    {region.regionName.split(' ')[0]}
                  </div>
                  <div className="text-xs text-muted-foreground">
                    {formatCurrency(region.totalRevenue)}
                  </div>
                </div>
              )
            })}
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
