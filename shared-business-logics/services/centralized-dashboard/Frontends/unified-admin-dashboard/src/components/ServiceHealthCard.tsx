import { CheckCircle2, XCircle, AlertTriangle } from 'lucide-react'
import type { ServiceHealth } from '@/lib/api'

interface ServiceHealthCardProps {
  services: Record<string, ServiceHealth>
}

export default function ServiceHealthCard({ services }: ServiceHealthCardProps) {
  const getStatusIcon = (status: string) => {
    switch (status.toUpperCase()) {
      case 'UP':
        return <CheckCircle2 className="h-5 w-5 text-green-500" />
      case 'DOWN':
        return <XCircle className="h-5 w-5 text-red-500" />
      case 'DEGRADED':
        return <AlertTriangle className="h-5 w-5 text-yellow-500" />
      default:
        return <AlertTriangle className="h-5 w-5 text-secondary-400" />
    }
  }

  const getStatusColor = (status: string) => {
    switch (status.toUpperCase()) {
      case 'UP':
        return 'bg-green-50 text-green-700'
      case 'DOWN':
        return 'bg-red-50 text-red-700'
      case 'DEGRADED':
        return 'bg-yellow-50 text-yellow-700'
      default:
        return 'bg-secondary-50 text-secondary-700'
    }
  }

  return (
    <div className="card">
      <h3 className="text-lg font-semibold mb-4">Service Health</h3>
      <div className="space-y-3">
        {Object.entries(services).map(([name, health]) => (
          <div
            key={name}
            className="flex items-center justify-between p-3 rounded-lg bg-secondary-50"
          >
            <div className="flex items-center gap-3">
              {getStatusIcon(health.status)}
              <div>
                <p className="font-medium text-secondary-900">{health.serviceName}</p>
                <p className="text-sm text-secondary-500">{health.baseUrl}</p>
              </div>
            </div>
            <div className="text-right">
              <span className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(health.status)}`}>
                {health.status}
              </span>
              <p className="text-xs text-secondary-500 mt-1">{health.responseTimeMs}ms</p>
            </div>
          </div>
        ))}
      </div>
    </div>
  )
}
