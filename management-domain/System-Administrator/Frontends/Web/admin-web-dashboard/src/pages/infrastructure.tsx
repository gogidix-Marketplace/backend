import { useState } from 'react'
import { Server, Activity, Cpu, HardDrive, Network, Search, RefreshCw, AlertTriangle } from 'lucide-react'
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { Button } from '@shared/components/ui/button'
import { Input } from '@shared/components/ui/input'
import { Progress } from '@shared/components/ui/progress'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@shared/components/ui/table'
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from '@shared/components/ui/tabs'
import { cn } from '@shared/utils/cn'
import { mockSystemHealth, mockMetrics } from '@shared/data/mockData'

function StatusDot({ status }: { status: 'healthy' | 'degraded' | 'down' | 'maintenance' }) {
  const config = {
    healthy: 'bg-green-500',
    degraded: 'bg-yellow-500',
    down: 'bg-red-500',
    maintenance: 'bg-blue-500',
  }
  return (
    <div className={cn('h-2 w-2 rounded-full animate-pulse-dot', config[status])} />
  )
}

function MetricGauge({ metric, icon: Icon }: { metric: typeof mockMetrics[0], icon: any }) {
  const isWarning = metric.current > metric.threshold * 0.8
  const isCritical = metric.current >= metric.threshold

  return (
    <Card>
      <CardHeader className="pb-3">
        <div className="flex items-center justify-between">
          <CardTitle className="text-sm font-medium flex items-center gap-2">
            <Icon className="h-4 w-4" />
            {metric.name}
          </CardTitle>
          <Badge
            variant={isCritical ? 'destructive' : isWarning ? 'warning' : 'success'}
            className="text-xs"
          >
            {isCritical ? 'Critical' : isWarning ? 'Warning' : 'Normal'}
          </Badge>
        </div>
      </CardHeader>
      <CardContent>
        <div className="space-y-4">
          <div>
            <div className="flex items-baseline justify-between">
              <span className="text-2xl font-bold">{metric.current}{metric.unit}</span>
              <span className="text-xs text-muted-foreground">
                Peak: {metric.peak}{metric.unit}
              </span>
            </div>
            <Progress
              value={metric.current}
              max={metric.threshold}
              className="h-2 mt-2"
            />
          </div>
          <div className="flex items-center gap-2 text-xs text-muted-foreground">
            <Activity className="h-3 w-3" />
            <span>Avg: {metric.average}{metric.unit} · Threshold: {metric.threshold}{metric.unit}</span>
          </div>
        </div>
      </CardContent>
    </Card>
  )
}

export default function InfrastructurePage() {
  const [searchQuery, setSearchQuery] = useState('')
  const [selectedTab, setSelectedTab] = useState('overview')

  const filteredServices = mockSystemHealth.filter(service =>
    service.service.toLowerCase().includes(searchQuery.toLowerCase())
  )

  return (
    <div className="space-y-6">
      {/* Page Header */}
      <div className="flex items-center justify-between">
        <div className="page-header">
          <h1 className="page-title">Infrastructure Monitoring</h1>
          <p className="page-description">
            Real-time monitoring of system infrastructure and services
          </p>
        </div>
        <div className="flex gap-2">
          <Button variant="outline">
            <RefreshCw className="mr-2 h-4 w-4" />
            Refresh
          </Button>
          <Button variant="admin">
            Configure Alerts
          </Button>
        </div>
      </div>

      {/* Search */}
      <div className="relative">
        <Search className="absolute left-3 top-1/2 h-4 w-4 -translate-y-1/2 text-muted-foreground" />
        <Input
          placeholder="Search services, servers, containers..."
          className="pl-9"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
        />
      </div>

      {/* Tabs */}
      <Tabs value={selectedTab} onValueChange={setSelectedTab}>
        <TabsList>
          <TabsTrigger value="overview">Overview</TabsTrigger>
          <TabsTrigger value="services">Services</TabsTrigger>
          <TabsTrigger value="servers">Servers</TabsTrigger>
          <TabsTrigger value="containers">Containers</TabsTrigger>
        </TabsList>

        {/* Overview Tab */}
        <TabsContent value="overview">
          <div className="space-y-6">
            {/* Resource Metrics */}
            <div>
              <h3 className="section-title mb-4">Resource Utilization</h3>
              <div className="metric-grid">
                <MetricGauge metric={mockMetrics[0]} icon={Cpu} />
                <MetricGauge metric={mockMetrics[1]} icon={Server} />
                <MetricGauge metric={mockMetrics[2]} icon={HardDrive} />
                <MetricGauge metric={mockMetrics[3]} icon={Network} />
              </div>
            </div>

            {/* Service Health */}
            <Card>
              <CardHeader>
                <CardTitle>Service Health</CardTitle>
                <CardDescription>Real-time status of all core services</CardDescription>
              </CardHeader>
              <CardContent>
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>Status</TableHead>
                      <TableHead>Service</TableHead>
                      <TableHead>Uptime</TableHead>
                      <TableHead>CPU</TableHead>
                      <TableHead>Memory</TableHead>
                      <TableHead>Disk</TableHead>
                      <TableHead>Network</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {filteredServices.map((service) => (
                      <TableRow key={service.id}>
                        <TableCell>
                          <StatusDot status={service.status} />
                        </TableCell>
                        <TableCell className="font-medium">{service.service}</TableCell>
                        <TableCell>
                          <Badge variant={service.uptime > 99 ? 'success' : 'warning'}>
                            {service.uptime}%
                          </Badge>
                        </TableCell>
                        <TableCell>
                          <div className="flex items-center gap-2">
                            <Progress value={service.metrics.cpu} className="h-1.5 w-16" />
                            <span className="text-xs">{service.metrics.cpu}%</span>
                          </div>
                        </TableCell>
                        <TableCell>
                          <div className="flex items-center gap-2">
                            <Progress value={service.metrics.memory} className="h-1.5 w-16" />
                            <span className="text-xs">{service.metrics.memory}%</span>
                          </div>
                        </TableCell>
                        <TableCell>
                          <div className="flex items-center gap-2">
                            <Progress value={service.metrics.disk} className="h-1.5 w-16" />
                            <span className="text-xs">{service.metrics.disk}%</span>
                          </div>
                        </TableCell>
                        <TableCell>
                          <div className="flex items-center gap-2">
                            <Progress value={service.metrics.network} className="h-1.5 w-16" />
                            <span className="text-xs">{service.metrics.network}%</span>
                          </div>
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </CardContent>
            </Card>

            {/* Alerts */}
            <Card className="border-yellow-500 bg-yellow-50 dark:bg-yellow-900/10">
              <CardHeader>
                <div className="flex items-center gap-2">
                  <AlertTriangle className="h-5 w-5 text-yellow-600" />
                  <CardTitle className="text-yellow-900 dark:text-yellow-100">Active Alerts</CardTitle>
                </div>
              </CardHeader>
              <CardContent className="space-y-2">
                <div className="flex items-center justify-between text-sm">
                  <span className="text-yellow-800 dark:text-yellow-200">Cache Layer - High CPU Usage (82%)</span>
                  <Badge variant="warning">Warning</Badge>
                </div>
                <div className="flex items-center justify-between text-sm">
                  <span className="text-yellow-800 dark:text-yellow-200">Email Service - Scheduled Maintenance</span>
                  <Badge variant="info">Info</Badge>
                </div>
              </CardContent>
            </Card>
          </div>
        </TabsContent>

        {/* Services Tab */}
        <TabsContent value="services">
          <Card>
            <CardHeader>
              <CardTitle>All Services</CardTitle>
              <CardDescription>Detailed service metrics and configuration</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Service Name</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Version</TableHead>
                    <TableHead>Replicas</TableHead>
                    <TableHead>Port</TableHead>
                    <TableHead>Last Deployed</TableHead>
                    <TableHead className="text-right">Actions</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {filteredServices.map((service) => (
                    <TableRow key={service.id}>
                      <TableCell className="font-medium">{service.service}</TableCell>
                      <TableCell>
                        <div className="flex items-center gap-2">
                          <StatusDot status={service.status} />
                          <span className="text-sm capitalize">{service.status}</span>
                        </div>
                      </TableCell>
                      <TableCell>v2.4.{Math.floor(Math.random() * 10)}</TableCell>
                      <TableCell>{Math.floor(Math.random() * 4) + 1}/3</TableCell>
                      <TableCell>300{Math.floor(Math.random() * 100)}</TableCell>
                      <TableCell>2 days ago</TableCell>
                      <TableCell className="text-right">
                        <Button variant="ghost" size="sm">Details</Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Servers Tab */}
        <TabsContent value="servers">
          <Card>
            <CardHeader>
              <CardTitle>Server Infrastructure</CardTitle>
              <CardDescription>Physical and virtual server inventory</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Hostname</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Type</TableHead>
                    <TableHead>OS</TableHead>
                    <TableHead>CPU</TableHead>
                    <TableHead>Memory</TableHead>
                    <TableHead>Disk</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {['prod-web-01', 'prod-api-01', 'prod-db-01', 'prod-cache-01', 'staging-all-01'].map((server) => (
                    <TableRow key={server}>
                      <TableCell className="font-medium">{server}</TableCell>
                      <TableCell>
                        <div className="flex items-center gap-2">
                          <StatusDot status={server === 'prod-cache-01' ? 'degraded' : 'healthy'} />
                          <span className="text-sm">
                            {server === 'prod-cache-01' ? 'Degraded' : 'Healthy'}
                          </span>
                        </div>
                      </TableCell>
                      <TableCell>VM</TableCell>
                      <TableCell>Ubuntu 22.04 LTS</TableCell>
                      <TableCell>4 vCPU</TableCell>
                      <TableCell>16 GB</TableCell>
                      <TableCell>500 GB SSD</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>

        {/* Containers Tab */}
        <TabsContent value="containers">
          <Card>
            <CardHeader>
              <CardTitle>Container Instances</CardTitle>
              <CardDescription>Docker and Kubernetes container status</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Container</TableHead>
                    <TableHead>Status</TableHead>
                    <TableHead>Image</TableHead>
                    <TableHead>Restarts</TableHead>
                    <TableHead>CPU Usage</TableHead>
                    <TableHead>Memory Usage</TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {filteredServices.slice(0, 6).map((service, i) => (
                    <TableRow key={i}>
                      <TableCell className="font-medium">
                        {service.service.toLowerCase().replace(/\s+/g, '-')}-{i + 1}
                      </TableCell>
                      <TableCell>
                        <Badge variant={service.status === 'healthy' ? 'success' : 'warning'}>
                          {service.status === 'healthy' ? 'Running' : 'Warning'}
                        </Badge>
                      </TableCell>
                      <TableCell>gogidix/{service.service.toLowerCase().replace(/\s+/g, '-')}:v2.4.0</TableCell>
                      <TableCell>{i === 2 ? '2' : '0'}</TableCell>
                      <TableCell>{Math.floor(Math.random() * 200) + 50}m</TableCell>
                      <TableCell>{Math.floor(Math.random() * 1024) + 256}Mi</TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </TabsContent>
      </Tabs>
    </div>
  )
}
