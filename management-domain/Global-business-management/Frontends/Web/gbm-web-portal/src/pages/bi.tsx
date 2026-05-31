import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import { cn, formatCurrency } from '@shared/utils/cn'
import { gbmDashboardsData } from '@shared/mock-data'
import { BarChart3, LayoutDashboard, Plus, Edit, Trash2, Eye, Settings, Filter } from 'lucide-react'

export default function BIPage() {
  const [viewMode, setViewMode] = useState<'dashboards' | 'explorer' | 'create'>('dashboards')
  const [selectedDashboard, setSelectedDashboard] = useState<string | null>(null)

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="page-header flex items-center justify-between">
        <div>
          <h1 className="page-title">Business Intelligence</h1>
          <p className="page-description">
            Interactive dashboards and data exploration tools
          </p>
        </div>
        <div className="flex gap-2">
          <Button variant={viewMode === 'dashboards' ? 'gbm' : 'outline'} onClick={() => setViewMode('dashboards')}>
            <LayoutDashboard className="mr-2 h-4 w-4" />
            Dashboards
          </Button>
          <Button variant={viewMode === 'explorer' ? 'gbm' : 'outline'} onClick={() => setViewMode('explorer')}>
            <BarChart3 className="mr-2 h-4 w-4" />
            Data Explorer
          </Button>
          <Button className="gap-2" onClick={() => setViewMode('create')}>
            <Plus className="h-4 w-4" />
            Create Dashboard
          </Button>
        </div>
      </div>

      {viewMode === 'dashboards' && (
        <>
          {/* Filter Bar */}
          <Card>
            <CardContent className="pt-6">
              <div className="flex items-center gap-4">
                <Input placeholder="Search dashboards..." className="flex-1" />
                <select className="flex h-10 rounded-md border border-input bg-background px-3 py-2 text-sm">
                  <option>All Types</option>
                  <option>Executive</option>
                  <option>Regional</option>
                  <option>Country</option>
                  <option>Custom</option>
                </select>
                <select className="flex h-10 rounded-md border border-input bg-background px-3 py-2 text-sm">
                  <option>All Owners</option>
                  <option>My Dashboards</option>
                  <option>Public</option>
                </select>
              </div>
            </CardContent>
          </Card>

          {/* Dashboard Grid */}
          <div className="grid gap-6 md:grid-cols-2 lg:grid-cols-3">
            {gbmDashboardsData.map((dashboard) => (
              <Card key={dashboard.id} className="cursor-pointer hover:shadow-lg transition-shadow">
                <CardHeader>
                  <div className="flex items-start justify-between">
                    <div>
                      <CardTitle className="text-base">{dashboard.name}</CardTitle>
                      <CardDescription className="mt-1">{dashboard.description}</CardDescription>
                    </div>
                    <Badge variant="outline">{dashboard.type}</Badge>
                  </div>
                </CardHeader>
                <CardContent>
                  {/* Preview Area */}
                  <div className="h-32 bg-slate-100 dark:bg-slate-800 rounded mb-4 flex items-center justify-center">
                    <div className="grid grid-cols-3 gap-2 w-3/4">
                      <div className="h-12 bg-slate-200 dark:bg-slate-700 rounded"></div>
                      <div className="h-12 bg-slate-200 dark:bg-slate-700 rounded col-span-2"></div>
                      <div className="h-8 bg-slate-200 dark:bg-slate-700 rounded col-span-3"></div>
                    </div>
                  </div>
                  <div className="flex items-center justify-between text-sm text-muted-foreground mb-4">
                    <span>By {dashboard.owner}</span>
                    <span>{dashboard.widgets.length} widgets</span>
                  </div>
                  <div className="flex items-center justify-between">
                    <div className="flex items-center gap-2">
                      {dashboard.isPublic && (
                        <Badge variant="success" className="text-xs">Public</Badge>
                      )}
                    </div>
                    <div className="flex items-center gap-2">
                      <Button variant="ghost" size="icon">
                        <Eye className="h-4 w-4" />
                      </Button>
                      <Button variant="ghost" size="icon">
                        <Edit className="h-4 w-4" />
                      </Button>
                    </div>
                  </div>
                </CardContent>
              </Card>
            ))}
            {/* Add New Dashboard Card */}
            <Card
              className="cursor-pointer border-dashed hover:border-[#1E88E5] transition-colors flex items-center justify-center min-h-[250px]"
              onClick={() => setViewMode('create')}
            >
              <CardContent className="text-center">
                <Plus className="h-12 w-12 mx-auto mb-4 text-muted-foreground" />
                <p className="font-medium">Create New Dashboard</p>
                <p className="text-sm text-muted-foreground">Build custom visualizations</p>
              </CardContent>
            </Card>
          </div>
        </>
      )}

      {viewMode === 'explorer' && (
        <>
          <Card>
            <CardHeader>
              <CardTitle>Data Explorer</CardTitle>
              <CardDescription>Build custom queries and explore your data</CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              {/* Query Builder */}
              <div className="space-y-4">
                <div className="flex items-center gap-2">
                  <Filter className="h-4 w-4 text-muted-foreground" />
                  <Label>Query Builder</Label>
                </div>
                <div className="grid gap-4 md:grid-cols-3">
                  <div className="space-y-2">
                    <Label>Metrics</Label>
                    <select className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm">
                      <option>Revenue</option>
                      <option>Growth Rate</option>
                      <option>Customer Count</option>
                      <option>Market Share</option>
                    </select>
                  </div>
                  <div className="space-y-2">
                    <Label>Dimension</Label>
                    <select className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm">
                      <option>By Region</option>
                      <option>By Country</option>
                      <option>By Time</option>
                      <option>By Product</option>
                    </select>
                  </div>
                  <div className="space-y-2">
                    <Label>Time Range</Label>
                    <select className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm">
                      <option>Last 30 days</option>
                      <option>Last 90 days</option>
                      <option>This quarter</option>
                      <option>This year</option>
                    </select>
                  </div>
                </div>
                <div className="flex items-center gap-4">
                  <Button className="gap-2">
                    <BarChart3 className="h-4 w-4" />
                    Run Query
                  </Button>
                  <Button variant="outline">Save Query</Button>
                </div>
              </div>

              {/* Results Preview */}
              <div className="border rounded-lg p-6">
                <div className="flex items-center justify-between mb-4">
                  <h3 className="font-medium">Query Results</h3>
                  <Button variant="outline" size="sm">Export</Button>
                </div>
                <div className="h-64 flex items-center justify-center text-muted-foreground">
                  <div className="text-center">
                    <BarChart3 className="h-12 w-12 mx-auto mb-4 opacity-50" />
                    <p>Run a query to see results</p>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </>
      )}

      {viewMode === 'create' && (
        <>
          <Card>
            <CardHeader>
              <CardTitle>Create Dashboard</CardTitle>
              <CardDescription>Design a new custom dashboard</CardDescription>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid gap-2">
                <Label>Dashboard Name</Label>
                <Input placeholder="Enter dashboard name" />
              </div>
              <div className="grid gap-2">
                <Label>Description</Label>
                <Input placeholder="Brief description" />
              </div>
              <div className="grid gap-2">
                <Label>Dashboard Type</Label>
                <select className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm">
                  <option>Executive</option>
                  <option>Regional</option>
                  <option>Country</option>
                  <option>Custom</option>
                </select>
              </div>
              <div className="flex items-center gap-2">
                <input type="checkbox" id="public" className="rounded" />
                <Label htmlFor="public">Make this dashboard public</Label>
              </div>
              <div className="border-t pt-4">
                <Label className="mb-2 block">Add Widgets</Label>
                <div className="grid gap-2 md:grid-cols-4">
                  {[
                    { type: 'metric', label: 'Metric Card' },
                    { type: 'chart', label: 'Chart' },
                    { type: 'table', label: 'Data Table' },
                    { type: 'gauge', label: 'Gauge' },
                    { type: 'trend', label: 'Trend' },
                    { type: 'map', label: 'Map' },
                  ].map((widget) => (
                    <Button key={widget.type} variant="outline" size="sm" className="w-full">
                      + {widget.label}
                    </Button>
                  ))}
                </div>
              </div>
              <div className="flex justify-end gap-4 pt-4">
                <Button variant="outline" onClick={() => setViewMode('dashboards')}>Cancel</Button>
                <Button>Create Dashboard</Button>
              </div>
            </CardContent>
          </Card>
        </>
      )}
    </div>
  )
}
