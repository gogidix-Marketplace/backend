import { useParams, useNavigate } from 'react-router-dom'
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Button } from '@shared/components/ui/button'
import { Badge } from '@shared/components/ui/badge'
import { ArrowLeft, TrendingUp, TrendingDown, Settings, Users, Package, DollarSign, AlertCircle } from 'lucide-react'

// Business domain configurations based on shared-business-infrastructure
const domainConfigs: Record<string, {
  name: string
  code: string
  description: string
  color: string
  icon: string
  services: string[]
  kpis: DomainKPI[]
}> = {
  ecommerce: {
    name: 'E-commerce Core',
    code: 'ECOM',
    description: 'Online marketplace, order management, customer service, and digital commerce operations',
    color: '#10b981',
    icon: 'shopping-bag',
    services: ['Order', 'Customer', 'Catalog', 'Inventory', 'Payment', 'Analytics', 'Fulfillment', 'Pricing', 'Promotion', 'Marketplace', 'Loyalty', 'Cart', 'Wishlist', 'Search', 'Vendor', 'Wholesaler', 'Influencer', 'Communication', 'Notification', 'Tracking', 'Discount', 'Integration', 'Tenant'],
    kpis: [
      { name: 'Total Revenue', value: '$15.2M', change: '+18.5%', status: 'ahead' },
      { name: 'Active Orders', value: '12.5K', change: '+12.3%', status: 'on_track' },
      { name: 'Conversion Rate', value: '3.8%', change: '+0.4%', status: 'on_track' },
      { name: 'Active Customers', value: '850K', change: '+22.1%', status: 'ahead' },
    ],
  },
  courier: {
    name: 'Courier Services',
    code: 'COURIER',
    description: 'Last-mile delivery, driver management, dispatch operations, and package tracking',
    color: '#3b82f6',
    icon: 'truck',
    services: ['Dispatch', 'Driver', 'Tracking', 'Partner', 'Pricing', 'Tenant', 'Fleet', 'Public API'],
    kpis: [
      { name: 'Total Revenue', value: '$8.7M', change: '+12.3%', status: 'at_risk' },
      { name: 'Deliveries', value: '125K', change: '+8.5%', status: 'on_track' },
      { name: 'On-Time Rate', value: '94.2%', change: '-1.2%', status: 'at_risk' },
      { name: 'Active Drivers', value: '2,450', change: '+5.8%', status: 'on_track' },
    ],
  },
  warehousing: {
    name: 'Warehousing',
    code: 'WHS',
    description: 'Storage operations, inventory management, and fulfillment centers',
    color: '#f59e0b',
    icon: 'package',
    services: ['Inventory', 'Storage', 'Operations', 'Fulfillment'],
    kpis: [
      { name: 'Total Revenue', value: '$5.4M', change: '+8.2%', status: 'on_track' },
      { name: 'Storage Utilization', value: '78%', change: '+5.1%', status: 'on_track' },
      { name: 'Orders Processed', value: '45.2K', change: '+12.8%', status: 'ahead' },
      { name: 'Active Facilities', value: '12', change: '0%', status: 'on_track' },
    ],
  },
  procurement: {
    name: 'Procurement',
    code: 'PROC',
    description: 'Supplier management, purchase orders, vendor relations, and sourcing',
    color: '#8b5cf6',
    icon: 'wrench',
    services: ['Suppliers', 'Purchase Orders', 'Vendor Management', 'Sourcing'],
    kpis: [
      { name: 'Total Revenue', value: '$4.8M', change: '-2.1%', status: 'behind' },
      { name: 'Active Suppliers', value: '340', change: '+2.5%', status: 'on_track' },
      { name: 'PO Processed', value: '8.5K', change: '-5.2%', status: 'behind' },
      { name: 'Cost Savings', value: '$1.2M', change: '+8.5%', status: 'ahead' },
    ],
  },
  'air-freight': {
    name: 'Air Freight',
    code: 'AIR',
    description: 'Air cargo transportation, expedited shipping, and air logistics',
    color: '#06b6d4',
    icon: 'plane',
    services: ['Cargo', 'Expedited', 'Logistics', 'Tracking'],
    kpis: [
      { name: 'Total Revenue', value: '$3.2M', change: '+15.8%', status: 'ahead' },
      { name: 'Shipments', value: '3.2K', change: '+18.5%', status: 'ahead' },
      { name: 'On-Time Rate', value: '96.5%', change: '+2.1%', status: 'ahead' },
      { name: 'Active Routes', value: '45', change: '+5.0%', status: 'on_track' },
    ],
  },
  'ocean-shipping': {
    name: 'Ocean Shipping',
    code: 'OCEAN',
    description: 'Sea freight, container shipping, and maritime logistics',
    color: '#14b8a6',
    icon: 'ship',
    services: ['Container', 'Freight', 'Port Operations', 'Tracking'],
    kpis: [
      { name: 'Total Revenue', value: '$2.9M', change: '+5.4%', status: 'on_track' },
      { name: 'Containers', value: '1.2K', change: '+3.2%', status: 'on_track' },
      { name: 'Transit Time', value: '18 days', change: '-2 days', status: 'ahead' },
      { name: 'Active Vessels', value: '8', change: '0%', status: 'on_track' },
    ],
  },
  haulage: {
    name: 'Haulage',
    code: 'HAUL',
    description: 'Ground transportation, trucking, and overland logistics',
    color: '#f97316',
    icon: 'container',
    services: ['Trucking', 'Transport', 'Fleet', 'Logistics'],
    kpis: [
      { name: 'Total Revenue', value: '$2.1M', change: '+3.7%', status: 'behind' },
      { name: 'Deliveries', value: '8.5K', change: '+2.1%', status: 'at_risk' },
      { name: 'Fleet Utilization', value: '72%', change: '-3.5%', status: 'behind' },
      { name: 'Active Trucks', value: '120', change: '0%', status: 'on_track' },
    ],
  },
  admin: {
    name: 'Admin Core',
    code: 'ADMIN',
    description: 'Administrative services, user management, and system configuration',
    color: '#64748b',
    icon: 'settings',
    services: ['User Management', 'Authentication', 'Authorization', 'Configuration', 'Audit', 'Notifications'],
    kpis: [
      { name: 'Active Users', value: '5.2K', change: '+8.5%', status: 'on_track' },
      { name: 'System Health', value: '99.9%', change: '0%', status: 'on_track' },
      { name: 'API Calls', value: '2.5M', change: '+22.5%', status: 'on_track' },
      { name: 'Incidents', value: '3', change: '-2', status: 'ahead' },
    ],
  },
}

interface DomainKPI {
  name: string
  value: string
  change: string
  status: 'on_track' | 'at_risk' | 'behind' | 'ahead'
}

const statusConfig = {
  on_track: { label: 'On Track', color: '#10b981', bgColor: 'bg-emerald-50 dark:bg-emerald-950/20' },
  at_risk: { label: 'At Risk', color: '#f59e0b', bgColor: 'bg-amber-50 dark:bg-amber-950/20' },
  behind: { label: 'Behind', color: '#ef4444', bgColor: 'bg-red-50 dark:bg-red-950/20' },
  ahead: { label: 'Ahead', color: '#3b82f6', bgColor: 'bg-blue-50 dark:bg-blue-950/20' },
}

export default function DomainOverviewPage() {
  const { domainId } = useParams<{ domainId: string }>()
  const navigate = useNavigate()

  const domain = domainConfigs[domainId || ''] || domainConfigs.ecommerce

  return (
    <div className="space-y-5">
      {/* Header */}
      <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-4">
        <div className="flex items-center gap-3">
          <Button
            variant="ghost"
            size="sm"
            onClick={() => navigate('/')}
            className="gap-2"
          >
            <ArrowLeft className="h-4 w-4" />
            Back to Overview
          </Button>
        </div>
        <div className="flex items-center gap-2">
          <Badge
            className="px-3 py-1"
            style={{ backgroundColor: domain.color + '20', color: domain.color, border: domain.color }}
          >
            {domain.code}
          </Badge>
          <Button variant="outline" size="sm" className="gap-2">
            <Settings className="h-4 w-4" />
            Settings
          </Button>
        </div>
      </div>

      {/* Domain Title */}
      <div className="flex items-center gap-4">
        <div
          className="w-16 h-16 rounded-xl flex items-center justify-center"
          style={{ backgroundColor: domain.color + '20' }}
        >
          <div className="w-8 h-8 rounded-lg flex items-center justify-center" style={{ backgroundColor: domain.color }}>
            <span className="text-white font-bold text-lg">{domain.code[0]}</span>
          </div>
        </div>
        <div>
          <h1 className="text-2xl sm:text-3xl font-bold text-slate-900 dark:text-white">{domain.name}</h1>
          <p className="text-sm text-slate-500 dark:text-slate-400 mt-1">{domain.description}</p>
        </div>
      </div>

      {/* KPI Cards */}
      <div className="grid gap-3 sm:gap-4 grid-cols-2 sm:grid-cols-2 md:grid-cols-4">
        {domain.kpis.map((kpi, index) => {
          const status = statusConfig[kpi.status]
          const isPositive = kpi.change.startsWith('+')

          return (
            <Card key={index} className="hover:shadow-lg transition-shadow">
              <CardContent className="p-4">
                <div className="flex items-start justify-between mb-2">
                  <p className="text-xs text-slate-500 uppercase tracking-wide">{kpi.name}</p>
                  <Badge className={`${status.bgColor} text-xs`} style={{ color: status.color, border: status.color }}>
                    {status.label}
                  </Badge>
                </div>
                <p className="text-2xl font-bold text-slate-900 dark:text-white">{kpi.value}</p>
                <div className={`flex items-center gap-1 text-sm mt-1 ${
                  isPositive ? 'text-emerald-600' : 'text-red-600'
                }`}>
                  {isPositive ? <TrendingUp className="h-3.5 w-3.5" /> : <TrendingDown className="h-3.5 w-3.5" />}
                  {kpi.change}
                </div>
              </CardContent>
            </Card>
          )
        })}
      </div>

      {/* Services and Details */}
      <div className="grid gap-4 sm:gap-5 lg:grid-cols-2">
        {/* Services List */}
        <Card>
          <CardHeader>
            <CardTitle className="text-lg flex items-center gap-2">
              <Package className="h-5 w-5" style={{ color: domain.color }} />
              Microservices ({domain.services.length})
            </CardTitle>
            <CardDescription>
              Active services in {domain.name}
            </CardDescription>
          </CardHeader>
          <CardContent>
            <div className="flex flex-wrap gap-2">
              {domain.services.map((service, index) => (
                <Badge
                  key={index}
                  variant="outline"
                  className="px-3 py-1"
                >
                  {service}
                </Badge>
              ))}
            </div>
          </CardContent>
        </Card>

        {/* Quick Actions */}
        <Card>
          <CardHeader>
            <CardTitle className="text-lg flex items-center gap-2">
              <Users className="h-5 w-5" style={{ color: domain.color }} />
              Team & Resources
            </CardTitle>
            <CardDescription>
              Domain team management and resources
            </CardDescription>
          </CardHeader>
          <CardContent className="space-y-3">
            <div className="flex items-center justify-between p-3 rounded-lg border border-slate-200 dark:border-slate-700">
              <div className="flex items-center gap-3">
                <Users className="h-4 w-4 text-slate-500" />
                <span className="text-sm">Team Members</span>
              </div>
              <span className="font-medium">24</span>
            </div>
            <div className="flex items-center justify-between p-3 rounded-lg border border-slate-200 dark:border-slate-700">
              <div className="flex items-center gap-3">
                <DollarSign className="h-4 w-4 text-slate-500" />
                <span className="text-sm">Budget Used</span>
              </div>
              <span className="font-medium">$2.1M / $2.5M</span>
            </div>
            <div className="flex items-center justify-between p-3 rounded-lg border border-slate-200 dark:border-slate-700">
              <div className="flex items-center gap-3">
                <AlertCircle className="h-4 w-4 text-slate-500" />
                <span className="text-sm">Open Incidents</span>
              </div>
              <span className="font-medium text-amber-600">2</span>
            </div>
          </CardContent>
        </Card>
      </div>

      {/* Performance Chart Placeholder */}
      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Performance Overview</CardTitle>
          <CardDescription>
            Domain performance over the last 6 months
          </CardDescription>
        </CardHeader>
        <CardContent>
          <div className="h-64 flex items-center justify-center border-2 border-dashed border-slate-200 dark:border-slate-700 rounded-lg">
            <div className="text-center">
              <TrendingUp className="h-12 w-12 text-slate-400 mx-auto mb-3" />
              <p className="text-slate-500">Performance charts coming soon</p>
              <p className="text-xs text-slate-400 mt-1">Backend API integration pending</p>
            </div>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
