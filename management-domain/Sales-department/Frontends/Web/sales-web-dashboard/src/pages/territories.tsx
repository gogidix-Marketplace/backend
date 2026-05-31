import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@shared/components/ui/select'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@shared/components/ui/dialog'
import { Input } from '@shared/components/ui/input'
import { Label } from '@shared/components/ui/label'
import {
  Map,
  Plus,
  Edit,
  Users,
  Briefcase,
  TrendingUp,
  Target,
} from 'lucide-react'
import { formatCurrency, formatNumber, getCountryFlag } from '@shared/utils/cn'
import { mockTerritories } from '@shared/data/mockData'

export default function TerritoriesPage() {
  const [selectedCountry, setSelectedCountry] = useState<string>('all')
  const [selectedTerritory, setSelectedTerritory] = useState<typeof mockTerritories[0] | null>(null)
  const [isCreateDialogOpen, setIsCreateDialogOpen] = useState(false)

  const filteredTerritories = selectedCountry === 'all'
    ? mockTerritories
    : mockTerritories.filter(t => t.country === selectedCountry)

  const totalRevenue = filteredTerritories.reduce((sum, t) => sum + t.revenue, 0)
  const totalTarget = filteredTerritories.reduce((sum, t) => sum + t.target, 0)
  const totalLeads = filteredTerritories.reduce((sum, t) => sum + t.leads, 0)
  const totalOpportunities = filteredTerritories.reduce((sum, t) => sum + t.opportunities, 0)

  const countries = [
    { code: 'all', name: 'All Countries' },
    { code: 'NG', name: 'Nigeria' },
    { code: 'ZA', name: 'South Africa' },
    { code: 'KE', name: 'Kenya' },
  ]

  return (
    <div className="space-y-6">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Territory Management</h1>
          <p className="text-muted-foreground mt-1">
            Manage sales territories and assignments
          </p>
        </div>
        <div className="flex gap-2">
          <Select value={selectedCountry} onValueChange={setSelectedCountry}>
            <SelectTrigger className="w-[200px]">
              <SelectValue placeholder="Filter by country" />
            </SelectTrigger>
            <SelectContent>
              {countries.map(country => (
                <SelectItem key={country.code} value={country.code}>
                  {country.code === 'all' ? country.name : `${getCountryFlag(country.code)} ${country.name}`}
                </SelectItem>
              ))}
            </SelectContent>
          </Select>
          <Dialog open={isCreateDialogOpen} onOpenChange={setIsCreateDialogOpen}>
            <DialogTrigger asChild>
              <Button className="gap-2">
                <Plus className="h-4 w-4" />
                Add Territory
              </Button>
            </DialogTrigger>
            <DialogContent>
              <DialogHeader>
                <DialogTitle>Create New Territory</DialogTitle>
                <DialogDescription>
                  Define a new sales territory with boundaries and assignments
                </DialogDescription>
              </DialogHeader>
              <div className="space-y-4 py-4">
                <div className="space-y-2">
                  <Label htmlFor="name">Territory Name</Label>
                  <Input id="name" placeholder="e.g., Downtown Lagos" />
                </div>
                <div className="space-y-2">
                  <Label htmlFor="country">Country</Label>
                  <Select>
                    <SelectTrigger>
                      <SelectValue placeholder="Select country" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="NG">🇳🇬 Nigeria</SelectItem>
                      <SelectItem value="ZA">🇿🇦 South Africa</SelectItem>
                      <SelectItem value="KE">🇰🇪 Kenya</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="type">Territory Type</Label>
                  <Select>
                    <SelectTrigger>
                      <SelectValue placeholder="Select type" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="geographic">Geographic</SelectItem>
                      <SelectItem value="industry">Industry</SelectItem>
                      <SelectItem value="company_size">Company Size</SelectItem>
                      <SelectItem value="hybrid">Hybrid</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                <div className="space-y-2">
                  <Label htmlFor="quota">Annual Quota</Label>
                  <Input id="quota" type="number" placeholder="500000" />
                </div>
              </div>
              <DialogFooter>
                <Button variant="outline" onClick={() => setIsCreateDialogOpen(false)}>
                  Cancel
                </Button>
                <Button onClick={() => setIsCreateDialogOpen(false)}>
                  Create Territory
                </Button>
              </DialogFooter>
            </DialogContent>
          </Dialog>
        </div>
      </div>

      {/* Stats */}
      <div className="grid gap-4 md:grid-cols-4">
        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Active Territories
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{filteredTerritories.length}</div>
            <p className="text-xs text-muted-foreground mt-1">
              Across {selectedCountry === 'all' ? 'all countries' : countries.find(c => c.code === selectedCountry)?.name}
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Total Revenue
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{formatCurrency(totalRevenue)}</div>
            <p className="text-xs text-green-600 mt-1">
              {Math.round((totalRevenue / totalTarget) * 100)}% of target
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Active Leads
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{totalLeads}</div>
            <p className="text-xs text-muted-foreground mt-1">
              Distributed across territories
            </p>
          </CardContent>
        </Card>

        <Card>
          <CardHeader className="pb-2">
            <CardTitle className="text-sm font-medium text-muted-foreground">
              Open Opportunities
            </CardTitle>
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{totalOpportunities}</div>
            <p className="text-xs text-muted-foreground mt-1">
              In pipeline
            </p>
          </CardContent>
        </Card>
      </div>

      {/* Territories Grid */}
      <div className="grid gap-4 md:grid-cols-2">
        {filteredTerritories.map((territory) => (
          <Card
            key={territory.id}
            className="cursor-pointer hover:shadow-md transition-shadow"
            onClick={() => setSelectedTerritory(territory)}
          >
            <CardHeader>
              <div className="flex items-start justify-between">
                <div>
                  <CardTitle className="flex items-center gap-2">
                    <Map className="h-5 w-5 text-blue-600" />
                    {territory.name}
                  </CardTitle>
                  <CardDescription className="mt-1">
                    {getCountryFlag(territory.country)} {territory.country}
                    {territory.region && ` • ${territory.region}`}
                  </CardDescription>
                </div>
                <Badge variant="outline" className="capitalize">
                  {territory.type}
                </Badge>
              </div>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                {/* Revenue vs Target */}
                <div>
                  <div className="flex items-center justify-between text-sm mb-2">
                    <span className="text-muted-foreground">Revenue vs Target</span>
                    <span className="font-medium">
                      {formatCurrency(territory.revenue)} / {formatCurrency(territory.target)}
                    </span>
                  </div>
                  <div className="h-2 bg-gray-200 rounded-full overflow-hidden">
                    <div
                      className={`h-full transition-all duration-300 ${
                        (territory.revenue / territory.target) >= 1 ? 'bg-green-500' :
                        (territory.revenue / territory.target) >= 0.8 ? 'bg-blue-500' : 'bg-yellow-500'
                      }`}
                      style={{ width: `${Math.min((territory.revenue / territory.target) * 100, 100)}%` }}
                    />
                  </div>
                </div>

                {/* Stats */}
                <div className="grid grid-cols-3 gap-4 text-center">
                  <div>
                    <div className="flex items-center justify-center gap-1 text-muted-foreground">
                      <Users className="h-3 w-3" />
                      <span className="text-xs">Assigned</span>
                    </div>
                    <div className="text-lg font-bold">{territory.assignedTo.length}</div>
                  </div>
                  <div>
                    <div className="flex items-center justify-center gap-1 text-muted-foreground">
                      <Briefcase className="h-3 w-3" />
                      <span className="text-xs">Opps</span>
                    </div>
                    <div className="text-lg font-bold">{territory.opportunities}</div>
                  </div>
                  <div>
                    <div className="flex items-center justify-center gap-1 text-muted-foreground">
                      <Target className="h-3 w-3" />
                      <span className="text-xs">Leads</span>
                    </div>
                    <div className="text-lg font-bold">{territory.leads}</div>
                  </div>
                </div>

                {/* Assigned Reps */}
                <div>
                  <div className="text-xs text-muted-foreground mb-2">Assigned Representatives</div>
                  <div className="flex -space-x-2">
                    {territory.assignedTo.map((rep, idx) => (
                      <div
                        key={rep}
                        className="h-8 w-8 rounded-full bg-blue-100 border-2 border-white flex items-center justify-center text-xs font-medium text-blue-600"
                        title={rep}
                      >
                        {rep.slice(-2).toUpperCase()}
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        ))}
      </div>

      {/* Territory Detail Dialog */}
      <Dialog open={!!selectedTerritory} onOpenChange={() => setSelectedTerritory(null)}>
        <DialogContent className="max-w-2xl">
          {selectedTerritory && (
            <>
              <DialogHeader>
                <DialogTitle className="flex items-center gap-2">
                  <Map className="h-5 w-5 text-blue-600" />
                  {selectedTerritory.name}
                </DialogTitle>
                <DialogDescription>
                  {getCountryFlag(selectedTerritory.country)} {selectedTerritory.country}
                  {selectedTerritory.region && ` • ${selectedTerritory.region}`}
                </DialogDescription>
              </DialogHeader>

              <div className="space-y-6">
                {/* Performance Overview */}
                <div className="grid grid-cols-2 gap-4">
                  <Card>
                    <CardHeader className="pb-2">
                      <CardTitle className="text-sm font-medium text-muted-foreground">
                        Revenue Performance
                      </CardTitle>
                    </CardHeader>
                    <CardContent>
                      <div className="text-2xl font-bold">{formatCurrency(selectedTerritory.revenue)}</div>
                      <div className="flex items-center gap-2 mt-2">
                        <div className="flex-1 h-2 bg-gray-200 rounded-full overflow-hidden">
                          <div
                            className={`h-full ${
                              (selectedTerritory.revenue / selectedTerritory.target) >= 1 ? 'bg-green-500' : 'bg-blue-500'
                            }`}
                            style={{ width: `${Math.min((selectedTerritory.revenue / selectedTerritory.target) * 100, 100)}%` }}
                          />
                        </div>
                        <span className="text-sm font-medium">
                          {Math.round((selectedTerritory.revenue / selectedTerritory.target) * 100)}%
                        </span>
                      </div>
                    </CardContent>
                  </Card>

                  <Card>
                    <CardHeader className="pb-2">
                      <CardTitle className="text-sm font-medium text-muted-foreground">
                        Pipeline Activity
                      </CardTitle>
                    </CardHeader>
                    <CardContent>
                      <div className="grid grid-cols-2 gap-4">
                        <div>
                          <div className="text-2xl font-bold">{selectedTerritory.leads}</div>
                          <div className="text-xs text-muted-foreground">Active Leads</div>
                        </div>
                        <div>
                          <div className="text-2xl font-bold">{selectedTerritory.opportunities}</div>
                          <div className="text-xs text-muted-foreground">Opportunities</div>
                        </div>
                      </div>
                    </CardContent>
                  </Card>
                </div>

                {/* Description */}
                {selectedTerritory.description && (
                  <Card>
                    <CardHeader className="pb-2">
                      <CardTitle className="text-sm font-medium">Description</CardTitle>
                    </CardHeader>
                    <CardContent className="text-sm">
                      {selectedTerritory.description}
                    </CardContent>
                  </Card>
                )}

                {/* Assigned Team */}
                <Card>
                  <CardHeader className="pb-2">
                    <CardTitle className="text-sm font-medium">Assigned Team</CardTitle>
                  </CardHeader>
                  <CardContent>
                    <div className="space-y-2">
                      {selectedTerritory.assignedTo.map((rep) => (
                        <div key={rep} className="flex items-center justify-between p-2 bg-gray-50 rounded">
                          <div className="flex items-center gap-2">
                            <div className="h-8 w-8 rounded-full bg-blue-100 flex items-center justify-center text-xs font-medium text-blue-600">
                              {rep.slice(-2).toUpperCase()}
                            </div>
                            <span className="text-sm font-medium">{rep}</span>
                          </div>
                          <Button size="sm" variant="ghost">
                            <Edit className="h-3 w-3" />
                          </Button>
                        </div>
                      ))}
                    </div>
                  </CardContent>
                </Card>
              </div>

              <DialogFooter>
                <Button variant="outline" onClick={() => setSelectedTerritory(null)}>
                  Close
                </Button>
                <Button>
                  <Edit className="h-4 w-4 mr-2" />
                  Edit Territory
                </Button>
              </DialogFooter>
            </>
          )}
        </DialogContent>
      </Dialog>
    </div>
  )
}
