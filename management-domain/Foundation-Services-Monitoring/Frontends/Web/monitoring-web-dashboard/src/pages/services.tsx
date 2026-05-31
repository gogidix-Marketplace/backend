import { Link } from 'react-router-dom'
import { useState } from 'react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@shared/components/ui/select'
import { MOCK_SERVICES } from '@shared/stores/mockData'
import { getStatusDotColor } from '@shared/utils/cn'
import { Search, Filter, Server } from 'lucide-react'

export default function ServicesPage() {
  const [searchQuery, setSearchQuery] = useState('')
  const [statusFilter, setStatusFilter] = useState<string>('all')
  const [typeFilter, setTypeFilter] = useState<string>('all')

  const filteredServices = MOCK_SERVICES.filter((service) => {
    const matchesSearch = service.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      service.type.toLowerCase().includes(searchQuery.toLowerCase())
    const matchesStatus = statusFilter === 'all' || service.status === statusFilter
    const matchesType = typeFilter === 'all' || service.type === typeFilter
    return matchesSearch && matchesStatus && matchesType
  })

  const serviceTypes = Array.from(new Set(MOCK_SERVICES.map(s => s.type)))

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div>
          <h1 className="text-3xl font-bold text-slate-900">Services</h1>
          <p className="text-slate-600">Monitor and manage all foundation services</p>
        </div>
      </div>

      {/* Filters */}
      <Card>
        <CardContent className="p-4">
          <div className="flex flex-col sm:flex-row gap-4">
            <div className="relative flex-1">
              <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-slate-400" />
              <Input
                placeholder="Search services..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="pl-9"
              />
            </div>
            <Select value={statusFilter} onValueChange={setStatusFilter}>
              <SelectTrigger className="w-full sm:w-[180px]">
                <SelectValue placeholder="Filter by status" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Statuses</SelectItem>
                <SelectItem value="healthy">Healthy</SelectItem>
                <SelectItem value="degraded">Degraded</SelectItem>
                <SelectItem value="critical">Critical</SelectItem>
                <SelectItem value="unknown">Unknown</SelectItem>
              </SelectContent>
            </Select>
            <Select value={typeFilter} onValueChange={setTypeFilter}>
              <SelectTrigger className="w-full sm:w-[180px]">
                <SelectValue placeholder="Filter by type" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="all">All Types</SelectItem>
                {serviceTypes.map((type) => (
                  <SelectItem key={type} value={type}>
                    {type.replace(/_/g, ' ').replace(/\b\w/g, l => l.toUpperCase())}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
        </CardContent>
      </Card>

      {/* Services Grid */}
      <div className="grid gap-4">
        {filteredServices.map((service) => (
          <Link key={service.id} to={`/services/${service.id}`}>
            <Card className="transition-all hover:shadow-md hover:border-[#0D47A1] cursor-pointer">
              <CardContent className="p-6">
                <div className="flex items-start justify-between">
                  <div className="flex items-start gap-4">
                    <div className={`flex h-12 w-12 items-center justify-center rounded-full ${
                      service.status === 'healthy' ? 'bg-green-100' :
                      service.status === 'degraded' ? 'bg-yellow-100' :
                      service.status === 'critical' ? 'bg-red-100' :
                      'bg-gray-100'
                    }`}>
                      <Server className={`h-6 w-6 ${
                        service.status === 'healthy' ? 'text-green-600' :
                        service.status === 'degraded' ? 'text-yellow-600' :
                        service.status === 'critical' ? 'text-red-600' :
                        'text-gray-600'
                      }`} />
                    </div>
                    <div>
                      <div className="flex items-center gap-2">
                        <h3 className="font-semibold text-lg">{service.name}</h3>
                        <Badge variant={service.status === 'healthy' ? 'healthy' : service.status === 'degraded' ? 'degraded' : 'critical'}>
                          {service.status}
                        </Badge>
                      </div>
                      <p className="text-sm text-slate-600">{service.description}</p>
                      <div className="flex items-center gap-4 mt-2 text-xs text-slate-500">
                        <span>v{service.version}</span>
                        <span>{service.region}</span>
                        <span>{service.type.replace(/_/g, ' ')}</span>
                      </div>
                    </div>
                  </div>
                  <div className="flex items-center gap-8">
                    <div className="text-center">
                      <p className="text-xs text-slate-500">Uptime</p>
                      <p className={`text-lg font-semibold ${service.uptime >= 99.5 ? 'text-green-600' : service.uptime >= 98 ? 'text-yellow-600' : 'text-red-600'}`}>
                        {service.uptime}%
                      </p>
                    </div>
                    <div className="text-center">
                      <p className="text-xs text-slate-500">Response</p>
                      <p className={`text-lg font-semibold ${service.responseTime > 200 ? 'text-yellow-600' : 'text-slate-900'}`}>
                        {service.status === 'critical' ? '-' : `${service.responseTime}ms`}
                      </p>
                    </div>
                    <div className="text-center">
                      <p className="text-xs text-slate-500">Error Rate</p>
                      <p className={`text-lg font-semibold ${service.errorRate > 1 ? 'text-red-600' : service.errorRate > 0.1 ? 'text-yellow-600' : 'text-green-600'}`}>
                        {service.errorRate}%
                      </p>
                    </div>
                    <Button variant="outline" size="sm">View Details</Button>
                  </div>
                </div>
              </CardContent>
            </Card>
          </Link>
        ))}
      </div>

      {filteredServices.length === 0 && (
        <Card>
          <CardContent className="p-12 text-center">
            <Server className="mx-auto h-12 w-12 text-slate-400 mb-4" />
            <h3 className="text-lg font-semibold text-slate-900 mb-2">No services found</h3>
            <p className="text-slate-600">Try adjusting your filters or search query</p>
          </CardContent>
        </Card>
      )}
    </div>
  )
}
