import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import { cn, formatCurrency, getCountryName, getStatusColor } from '@shared/utils/cn'
import { countryMetricsData, currenciesData } from '@shared/mock-data'
import { Search, Plus, Edit, MapPin, Settings, Globe, DollarSign, CheckCircle, XCircle } from 'lucide-react'
import type { Country } from '@shared/types'

export default function CountryManagementPage() {
  const [selectedCountry, setSelectedCountry] = useState<Country | null>(null)
  const [searchQuery, setSearchQuery] = useState('')
  const [viewMode, setViewMode] = useState<'list' | 'details'>('list')

  const filteredCountries = countryMetricsData.filter(
    c => c.countryName.toLowerCase().includes(searchQuery.toLowerCase())
  )

  const selectedCountryData = selectedCountry
    ? countryMetricsData.find(c => c.country === selectedCountry)
    : null

  const countrySettings = {
    businessHours: { start: '08:00', end: '18:00' },
    timezone: 'Africa/Lagos',
    dateFormat: 'DD/MM/YYYY',
    weekendDays: ['Saturday', 'Sunday'],
    taxRate: 7.5,
    currency: 'NGN',
  }

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header flex items-center justify-between">
        <div>
          <h1 className="page-title">Country Management</h1>
          <p className="page-description">
            Configure country-specific settings and manage localizations
          </p>
        </div>
        <Button className="gap-2">
          <Plus className="h-4 w-4" />
          Add Country
        </Button>
      </div>

      {viewMode === 'list' ? (
        <>
          {/* Search and Filter */}
          <Card>
            <CardContent className="pt-6">
              <div className="flex items-center gap-4">
                <div className="relative flex-1">
                  <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-muted-foreground" />
                  <Input
                    type="search"
                    placeholder="Search countries..."
                    className="pl-9"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                  />
                </div>
                <Button variant="outline">
                  <Globe className="mr-2 h-4 w-4" />
                  Filter by Region
                </Button>
              </div>
            </CardContent>
          </Card>

          {/* Countries Table */}
          <Card>
            <CardHeader>
              <CardTitle>Active Countries</CardTitle>
              <CardDescription>{filteredCountries.length} countries configured</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Country</TableHead>
                    <TableHead>Region</TableHead>
                    <TableHead>Currency</TableHead>
                    <TableHead>Revenue (MTD)</TableHead>
                    <TableHead>Customers</TableHead>
                    <TableHead>Businesses</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {filteredCountries.map((country) => (
                    <TableRow key={country.id}>
                      <TableCell className="font-medium">
                        <div className="flex items-center gap-2">
                          <MapPin className="h-4 w-4 text-muted-foreground" />
                          {country.countryName}
                        </div>
                      </TableCell>
                      <TableCell>
                        <Badge variant="outline">{country.region}</Badge>
                      </TableCell>
                      <TableCell>
                        <div className="flex items-center gap-1">
                          <DollarSign className="h-3 w-3 text-muted-foreground" />
                          {country.currency}
                        </div>
                      </TableCell>
                      <TableCell>{formatCurrency(country.totalRevenue)}</TableCell>
                      <TableCell>{country.customerCount.toLocaleString()}</TableCell>
                      <TableCell>{country.activeBusinesses.toLocaleString()}</TableCell>
                      <TableCell>
                        <Badge className={getStatusColor(country.status)}>
                          {country.status.replace('_', ' ')}
                        </Badge>
                      </TableCell>
                      <TableCell>
                        <div className="flex items-center gap-2">
                          <Button
                            variant="ghost"
                            size="icon"
                            onClick={() => {
                              setSelectedCountry(country.country)
                              setViewMode('details')
                            }}
                          >
                            <Settings className="h-4 w-4" />
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </>
      ) : (
        <>
          {/* Country Details View */}
          <div className="flex items-center gap-4">
            <Button variant="outline" onClick={() => setViewMode('list')}>
              Back to List
            </Button>
            <h2 className="text-xl font-semibold">
              {selectedCountryData?.countryName} - Configuration
            </h2>
          </div>

          <div className="grid gap-6 md:grid-cols-2">
            {/* Basic Settings */}
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Settings className="h-5 w-5" />
                  Basic Settings
                </CardTitle>
                <CardDescription>Core configuration for this country</CardDescription>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid gap-2">
                  <Label>Country Name</Label>
                  <Input defaultValue={selectedCountryData?.countryName} />
                </div>
                <div className="grid gap-2">
                  <Label>Region</Label>
                  <Input defaultValue={selectedCountryData?.region} />
                </div>
                <div className="grid gap-2">
                  <Label>ISO Code</Label>
                  <Input defaultValue={selectedCountry} />
                </div>
                <div className="grid gap-2">
                  <Label>Status</Label>
                  <div className="flex items-center gap-2">
                    <CheckCircle className="h-4 w-4 text-green-500" />
                    <span>Active</span>
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* Currency Settings */}
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <DollarSign className="h-5 w-5" />
                  Currency Settings
                </CardTitle>
                <CardDescription>Local currency configuration</CardDescription>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid gap-2">
                  <Label>Default Currency</Label>
                  <Input defaultValue={selectedCountryData?.currency} />
                </div>
                <div className="grid gap-2">
                  <Label>Exchange Rate (to USD)</Label>
                  <Input
                    type="number"
                    defaultValue={currenciesData.find(c => c.code === selectedCountryData?.currency)?.exchangeRate || 1}
                  />
                </div>
                <div className="grid gap-2">
                  <Label>Tax Rate (%)</Label>
                  <Input type="number" defaultValue={countrySettings.taxRate} />
                </div>
                <div className="grid gap-2">
                  <Label>Number Format</Label>
                  <Input defaultValue="123,456.78" />
                </div>
              </CardContent>
            </Card>

            {/* Localization Settings */}
            <Card>
              <CardHeader>
                <CardTitle className="flex items-center gap-2">
                  <Globe className="h-5 w-5" />
                  Localization
                </CardTitle>
                <CardDescription>Regional formatting and language settings</CardDescription>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="grid gap-2">
                  <Label>Timezone</Label>
                  <Input defaultValue={countrySettings.timezone} />
                </div>
                <div className="grid gap-2">
                  <Label>Date Format</Label>
                  <Input defaultValue={countrySettings.dateFormat} />
                </div>
                <div className="grid gap-2">
                  <Label>Business Hours</Label>
                  <div className="flex gap-2">
                    <Input defaultValue={countrySettings.businessHours.start} />
                    <span className="flex items-center">to</span>
                    <Input defaultValue={countrySettings.businessHours.end} />
                  </div>
                </div>
                <div className="grid gap-2">
                  <Label>Weekend Days</Label>
                  <div className="flex gap-2">
                    {countrySettings.weekendDays.map(day => (
                      <Badge key={day} variant="outline">{day}</Badge>
                    ))}
                  </div>
                </div>
              </CardContent>
            </Card>

            {/* Supported Languages */}
            <Card>
              <CardHeader>
                <CardTitle>Supported Languages</CardTitle>
                <CardDescription>Languages available for this country</CardDescription>
              </CardHeader>
              <CardContent className="space-y-4">
                <div className="space-y-2">
                  <div className="flex items-center justify-between p-2 border rounded">
                    <div className="flex items-center gap-2">
                      <span>🇬🇧</span>
                      <span>English</span>
                    </div>
                    <CheckCircle className="h-4 w-4 text-green-500" />
                  </div>
                  {selectedCountry === 'NG' && (
                    <div className="flex items-center justify-between p-2 border rounded">
                      <div className="flex items-center gap-2">
                        <span>🇳🇬</span>
                        <span>Pidgin English</span>
                      </div>
                      <CheckCircle className="h-4 w-4 text-green-500" />
                    </div>
                  )}
                  {selectedCountry === 'KE' && (
                    <div className="flex items-center justify-between p-2 border rounded">
                      <div className="flex items-center gap-2">
                        <span>🇰🇪</span>
                        <span>Swahili</span>
                      </div>
                      <CheckCircle className="h-4 w-4 text-green-500" />
                    </div>
                  )}
                </div>
                <Button variant="outline" size="sm" className="w-full">
                  <Plus className="mr-2 h-4 w-4" />
                  Add Language
                </Button>
              </CardContent>
            </Card>
          </div>

          {/* Actions */}
          <Card>
            <CardContent className="pt-6">
              <div className="flex justify-end gap-4">
                <Button variant="outline">Cancel</Button>
                <Button>Save Changes</Button>
              </div>
            </CardContent>
          </Card>
        </>
      )}
    </div>
  )
}
