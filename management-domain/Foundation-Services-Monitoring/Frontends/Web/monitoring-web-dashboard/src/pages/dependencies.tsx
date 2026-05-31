import { useState } from 'react'
import { Link } from 'react-router-dom'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { MOCK_SERVICES, MOCK_DEPENDENCIES } from '@shared/stores/mockData'
import { getStatusDotColor } from '@shared/utils/cn'
import {
  Network,
  Server,
  ArrowRight,
  AlertTriangle,
  CheckCircle,
} from 'lucide-react'

interface DependencyNode {
  id: string
  name: string
  status: string
  level: number
}

export default function DependenciesPage() {
  const [selectedService, setSelectedService] = useState<string | null>(null)
  const [viewMode, setViewMode] = useState<'graph' | 'list'>('graph')

  // Build dependency tree
  const buildDependencyTree = () => {
    const nodes: DependencyNode[] = MOCK_SERVICES.map(s => ({
      id: s.id,
      name: s.name,
      status: s.status,
      level: 0,
    }))

    const edges = MOCK_DEPENDENCIES.map(d => ({
      ...d,
      fromService: MOCK_SERVICES.find(s => s.id === d.from)?.name,
      toService: MOCK_SERVICES.find(s => s.id === d.to)?.name,
    }))

    return { nodes, edges }
  }

  const { nodes, edges } = buildDependencyTree()

  // Get services by status for impact analysis
  const getCriticalServices = () => MOCK_SERVICES.filter(s => s.status === 'critical')
  const getDegradedServices = () => MOCK_SERVICES.filter(s => s.status === 'degraded')

  // Calculate impact if a service goes down
  const calculateImpact = (serviceId: string): { direct: number; indirect: number } => {
    const service = MOCK_SERVICES.find(s => s.id === serviceId)
    if (!service) return { direct: 0, indirect: 0 }

    const directImpact = service.dependents.length
    const indirectImpact = MOCK_DEPENDENCIES.filter(d =>
      service.dependents.includes(d.from)
    ).length

    return { direct: directImpact, indirect: indirectImpact }
  }

  const criticalServices = getCriticalServices()
  const degradedServices = getDegradedServices()

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold text-slate-900">Service Dependencies</h1>
          <p className="text-slate-600">Visualize service relationships and impact analysis</p>
        </div>
        <div className="flex items-center gap-2">
          <Button
            variant={viewMode === 'graph' ? 'monitoring' : 'outline'}
            onClick={() => setViewMode('graph')}
          >
            <Network className="mr-2 h-4 w-4" />
            Graph View
          </Button>
          <Button
            variant={viewMode === 'list' ? 'monitoring' : 'outline'}
            onClick={() => setViewMode('list')}
          >
            <Server className="mr-2 h-4 w-4" />
            List View
          </Button>
        </div>
      </div>

      {/* Impact Summary */}
      {(criticalServices.length > 0 || degradedServices.length > 0) && (
        <Card className="border-red-200 bg-red-50 dark:bg-red-900/10">
          <CardHeader>
            <div className="flex items-center gap-2">
              <AlertTriangle className="h-5 w-5 text-red-600" />
              <CardTitle className="text-red-900">Active Service Issues</CardTitle>
            </div>
            <CardDescription className="text-red-700">
              The following service issues may impact dependent services
            </CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            {criticalServices.map(service => {
              const impact = calculateImpact(service.id)
              return (
                <div key={service.id} className="flex items-center justify-between rounded-lg border border-red-200 bg-white p-4">
                  <div className="flex items-center gap-3">
                    <span className={`h-3 w-3 rounded-full ${getStatusDotColor(service.status)}`} />
                    <div>
                      <h4 className="font-semibold text-slate-900">{service.name}</h4>
                      <p className="text-sm text-slate-600">{impact.direct} services directly depend on this service</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <Badge variant="critical">Critical</Badge>
                    <Link to={`/services/${service.id}`}>
                      <Button variant="outline" size="sm">View Details</Button>
                    </Link>
                  </div>
                </div>
              )
            })}
            {degradedServices.map(service => {
              const impact = calculateImpact(service.id)
              return (
                <div key={service.id} className="flex items-center justify-between rounded-lg border border-yellow-200 bg-white p-4">
                  <div className="flex items-center gap-3">
                    <span className={`h-3 w-3 rounded-full ${getStatusDotColor(service.status)}`} />
                    <div>
                      <h4 className="font-semibold text-slate-900">{service.name}</h4>
                      <p className="text-sm text-slate-600">{impact.direct} services directly depend on this service</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    <Badge variant="degraded">Degraded</Badge>
                    <Link to={`/services/${service.id}`}>
                      <Button variant="outline" size="sm">View Details</Button>
                    </Link>
                  </div>
                </div>
              )
            })}
          </CardContent>
        </Card>
      )}

      {viewMode === 'graph' ? (
        <>
          {/* Dependency Graph */}
          <Card>
            <CardHeader>
              <CardTitle>Service Dependency Map</CardTitle>
              <CardDescription>
                Visual representation of service dependencies and data flow
              </CardDescription>
            </CardHeader>
            <CardContent>
              <div className="relative min-h-[600px] bg-slate-50 rounded-lg p-8 overflow-auto">
                {/* Simplified SVG-based dependency visualization */}
                <svg width="100%" height="500" className="min-w-[800px]">
                  {/* Define arrow marker */}
                  <defs>
                    <marker
                      id="arrowhead"
                      markerWidth="10"
                      markerHeight="7"
                      refX="9"
                      refY="3.5"
                      orient="auto"
                    >
                      <polygon points="0 0, 10 3.5, 0 7" fill="#94a3b8" />
                    </marker>
                  </defs>

                  {/* Connection lines */}
                  {edges.map((edge, index) => {
                    const fromNode = nodes.find(n => n.id === edge.from)
                    const toNode = nodes.find(n => n.id === edge.to)
                    if (!fromNode || !toNode) return null

                    // Simple positioning - in real app would use a graph layout library
                    const fromX = 100 + (nodes.indexOf(fromNode) % 5) * 200
                    const fromY = 100 + Math.floor(nodes.indexOf(fromNode) / 5) * 150
                    const toX = 100 + (nodes.indexOf(toNode) % 5) * 200
                    const toY = 100 + Math.floor(nodes.indexOf(toNode) / 5) * 150 + 100

                    return (
                      <g key={index}>
                        <line
                          x1={fromX}
                          y1={fromY}
                          x2={toX}
                          y2={toY}
                          stroke={edge.type === 'synchronous' ? '#0D47A1' : '#F59E0B'}
                          strokeWidth={edge.strength > 0.7 ? 2 : 1}
                          strokeDasharray={edge.type === 'asynchronous' ? '5,5' : '0'}
                          markerEnd="url(#arrowhead)"
                          opacity={0.5}
                        />
                      </g>
                    )
                  })}

                  {/* Service nodes */}
                  {nodes.map((node, index) => {
                    const x = 100 + (index % 5) * 200
                    const y = 100 + Math.floor(index / 5) * 150
                    const selected = selectedService === node.id

                    return (
                      <g key={node.id}>
                        <Link to={`/services/${node.id}`}>
                          <circle
                            cx={x}
                            cy={y}
                            r={selected ? 35 : 30}
                            fill={
                              node.status === 'healthy' ? '#10B981' :
                              node.status === 'degraded' ? '#F59E0B' :
                              node.status === 'critical' ? '#EF4444' :
                              '#94a3b8'
                            }
                            className={selected ? 'cursor-pointer filter drop-shadow-lg' : 'cursor-pointer hover:opacity-80'}
                            onClick={() => setSelectedService(selected ? null : node.id)}
                          />
                          <text
                            x={x}
                            y={y - 45}
                            textAnchor="middle"
                            className="text-xs font-medium fill-slate-700 pointer-events-none"
                          >
                            {node.name}
                          </text>
                          {selected && (
                            <text
                              x={x}
                              y={y + 5}
                              textAnchor="middle"
                              className="text-xs font-semibold fill-white pointer-events-none"
                            >
                              {node.status}
                            </text>
                          )}
                        </Link>
                      </g>
                    )
                  })}
                </svg>

                {/* Legend */}
                <div className="absolute bottom-4 right-4 bg-white rounded-lg border p-3 shadow-sm">
                  <h4 className="text-sm font-semibold mb-2">Legend</h4>
                  <div className="space-y-1 text-xs">
                    <div className="flex items-center gap-2">
                      <div className="w-3 h-3 rounded-full bg-green-500" />
                      <span>Healthy</span>
                    </div>
                    <div className="flex items-center gap-2">
                      <div className="w-3 h-3 rounded-full bg-yellow-500" />
                      <span>Degraded</span>
                    </div>
                    <div className="flex items-center gap-2">
                      <div className="w-3 h-3 rounded-full bg-red-500" />
                      <span>Critical</span>
                    </div>
                    <div className="flex items-center gap-2">
                      <div className="w-8 h-0.5 bg-[#0D47A1]" />
                      <span>Synchronous</span>
                    </div>
                    <div className="flex items-center gap-2">
                      <div className="w-8 h-0.5 bg-[#F59E0B] border-dashed border-t-2" />
                      <span>Asynchronous</span>
                    </div>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </>
      ) : (
        <>
          {/* List View */}
          <div className="grid gap-4">
            {MOCK_SERVICES.map(service => {
              const dependencies = MOCK_DEPENDENCIES.filter(d => d.from === service.id)
              const dependents = MOCK_DEPENDENCIES.filter(d => d.to === service.id)
              const impact = calculateImpact(service.id)

              return (
                <Card key={service.id}>
                  <CardHeader>
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-3">
                        <span className={`h-3 w-3 rounded-full ${getStatusDotColor(service.status)}`} />
                        <CardTitle className="text-lg">{service.name}</CardTitle>
                        <Badge variant={service.status === 'healthy' ? 'healthy' : service.status === 'degraded' ? 'degraded' : 'critical'}>
                          {service.status}
                        </Badge>
                      </div>
                      <Link to={`/services/${service.id}`}>
                        <Button variant="outline" size="sm">View Details</Button>
                      </Link>
                    </div>
                  </CardHeader>
                  <CardContent>
                    <div className="grid md:grid-cols-2 gap-6">
                      <div>
                        <h4 className="text-sm font-semibold text-slate-700 mb-3 flex items-center gap-2">
                          <ArrowRight className="h-4 w-4" />
                          Depends On ({dependencies.length})
                        </h4>
                        {dependencies.length > 0 ? (
                          <div className="space-y-2">
                            {dependencies.map(dep => {
                              const depService = MOCK_SERVICES.find(s => s.id === dep.to)
                              return depService ? (
                                <Link key={dep.to} to={`/services/${dep.to}`}>
                                  <div className="flex items-center justify-between p-2 rounded hover:bg-slate-50">
                                    <div className="flex items-center gap-2">
                                      <span className={`h-2 w-2 rounded-full ${getStatusDotColor(depService.status)}`} />
                                      <span className="text-sm">{depService.name}</span>
                                    </div>
                                    <div className="flex items-center gap-2">
                                      <Badge variant="outline" className="text-xs">
                                        {dep.type === 'synchronous' ? 'Sync' : 'Async'}
                                      </Badge>
                                      <span className="text-xs text-slate-500">
                                        {(dep.strength * 100).toFixed(0)}% critical
                                      </span>
                                    </div>
                                  </div>
                                </Link>
                              ) : null
                            })}
                          </div>
                        ) : (
                          <p className="text-sm text-slate-500">No dependencies</p>
                        )}
                      </div>

                      <div>
                        <h4 className="text-sm font-semibold text-slate-700 mb-3 flex items-center gap-2">
                          <ArrowRight className="h-4 w-4 rotate-180" />
                          Dependents ({dependents.length})
                        </h4>
                        {dependents.length > 0 ? (
                          <div className="space-y-2">
                            {dependents.map(dep => {
                              const depService = MOCK_SERVICES.find(s => s.id === dep.from)
                              return depService ? (
                                <Link key={dep.from} to={`/services/${dep.from}`}>
                                  <div className="flex items-center justify-between p-2 rounded hover:bg-slate-50">
                                    <div className="flex items-center gap-2">
                                      <span className={`h-2 w-2 rounded-full ${getStatusDotColor(depService.status)}`} />
                                      <span className="text-sm">{depService.name}</span>
                                    </div>
                                    <div className="flex items-center gap-2">
                                      <Badge variant="outline" className="text-xs">
                                        {dep.type === 'synchronous' ? 'Sync' : 'Async'}
                                      </Badge>
                                      <span className="text-xs text-slate-500">
                                        {(dep.strength * 100).toFixed(0)}% critical
                                      </span>
                                    </div>
                                  </div>
                                </Link>
                              ) : null
                            })}
                          </div>
                        ) : (
                          <p className="text-sm text-slate-500">No dependents</p>
                        )}
                      </div>
                    </div>

                    {/* Impact warning for critical/degraded services */}
                    {(service.status === 'critical' || service.status === 'degraded') && (
                      <div className={`mt-4 p-3 rounded-lg ${
                        service.status === 'critical' ? 'bg-red-50 border border-red-200' : 'bg-yellow-50 border border-yellow-200'
                      }`}>
                        <div className="flex items-center gap-2 text-sm">
                          <AlertTriangle className={`h-4 w-4 ${service.status === 'critical' ? 'text-red-600' : 'text-yellow-600'}`} />
                          <span className={`font-medium ${service.status === 'critical' ? 'text-red-900' : 'text-yellow-900'}`}>
                            Impact Analysis:
                          </span>
                          <span className={service.status === 'critical' ? 'text-red-700' : 'text-yellow-700'}>
                            {impact.direct} services directly impacted
                          </span>
                        </div>
                      </div>
                    )}
                  </CardContent>
                </Card>
              )
            })}
          </div>
        </>
      )}
    </div>
  )
}
