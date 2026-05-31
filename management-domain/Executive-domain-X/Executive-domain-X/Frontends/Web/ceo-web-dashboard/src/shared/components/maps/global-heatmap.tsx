import { Card, CardContent, CardHeader, CardTitle } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { cn } from '@shared/utils/cn'

/**
 * GlobalOperationalHeatmap Component
 *
 * Interactive world map with region markers
 * Color-coded regions: healthy (green), warning (amber), critical (red)
 * Click region for detailed country breakdown
 * Badge showing active issues per region
 */

export interface Region {
  id: string
  name: string
  status: 'healthy' | 'warning' | 'critical'
  countries: number
  activeIssues: number
  revenue: number
  coordinates: { x: number; y: number } // Simplified coordinates for demo
}

export interface GlobalHeatmapProps {
  regions?: Region[]
  onRegionClick?: (region: Region) => void
  loading?: boolean
}

const defaultRegions: Region[] = [
  {
    id: 'na',
    name: 'North America',
    status: 'healthy',
    countries: 2,
    activeIssues: 0,
    revenue: 18500000,
    coordinates: { x: 20, y: 30 },
  },
  {
    id: 'eu',
    name: 'Europe',
    status: 'warning',
    countries: 5,
    activeIssues: 2,
    revenue: 12000000,
    coordinates: { x: 48, y: 25 },
  },
  {
    id: 'ng',
    name: 'Nigeria',
    status: 'healthy',
    countries: 1,
    activeIssues: 0,
    revenue: 7800000,
    coordinates: { x: 48, y: 55 },
  },
  {
    id: 'ke',
    name: 'Kenya',
    status: 'healthy',
    countries: 1,
    activeIssues: 0,
    revenue: 3200000,
    coordinates: { x: 55, y: 60 },
  },
  {
    id: 'za',
    name: 'South Africa',
    status: 'critical',
    countries: 1,
    activeIssues: 3,
    revenue: 1000000,
    coordinates: { x: 52, y: 75 },
  },
]

const statusColors = {
  healthy: 'bg-green-500',
  warning: 'bg-amber-500',
  critical: 'bg-red-500',
}

const statusGlow = {
  healthy: 'shadow-green-500/50',
  warning: 'shadow-amber-500/50',
  critical: 'shadow-red-500/50',
}

export function GlobalHeatmap({
  regions = defaultRegions,
  onRegionClick,
  loading = false,
}: GlobalHeatmapProps) {
  if (loading) {
    return (
      <Card>
        <CardHeader>
          <CardTitle>Global Operational Heatmap</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="h-64 flex items-center justify-center">
            <div className="h-8 w-8 animate-spin rounded-full border-4 border-primary border-t-transparent" />
          </div>
        </CardContent>
      </Card>
    )
  }

  const healthyCount = regions.filter((r) => r.status === 'healthy').length
  const warningCount = regions.filter((r) => r.status === 'warning').length
  const criticalCount = regions.filter((r) => r.status === 'critical').length

  return (
    <Card>
      <CardHeader>
        <div className="flex items-center justify-between">
          <CardTitle>Global Operational Status</CardTitle>
          <div className="flex items-center gap-2">
            <Badge variant="outline" className="text-green-600 border-green-600">
              {healthyCount} Healthy
            </Badge>
            <Badge variant="outline" className="text-amber-600 border-amber-600">
              {warningCount} Warning
            </Badge>
            <Badge variant="outline" className="text-red-600 border-red-600">
              {criticalCount} Critical
            </Badge>
          </div>
        </div>
      </CardHeader>
      <CardContent>
        {/* Simplified world map visualization */}
        <div className="relative h-64 bg-slate-100 dark:bg-slate-800 rounded-lg overflow-hidden">
          {/* World map outline (simplified SVG) */}
          <svg
            viewBox="0 0 100 80"
            className="w-full h-full"
            preserveAspectRatio="xMidYMid meet"
          >
            {/* Simplified continent outlines */}
            <path
              d="M10,20 Q30,15 50,20 Q70,15 90,25 L85,50 Q70,55 50,50 Q30,55 15,50 Z"
              fill="none"
              stroke="currentColor"
              strokeWidth="0.5"
              className="text-muted-foreground/30"
            />

            {/* Region markers */}
            {regions.map((region) => (
              <g
                key={region.id}
                className="cursor-pointer group"
                onClick={() => onRegionClick?.(region)}
              >
                {/* Pulsing effect for active issues */}
                {region.activeIssues > 0 && (
                  <circle
                    cx={region.coordinates.x}
                    cy={region.coordinates.y}
                    r="8"
                    className={`fill-current ${statusColors[region.status]} opacity-20 animate-ping`}
                  />
                )}

                {/* Main marker */}
                <circle
                  cx={region.coordinates.x}
                  cy={region.coordinates.y}
                  r="4"
                  className={cn(
                    'fill-current transition-transform group-hover:scale-125',
                    statusColors[region.status],
                    `shadow-lg ${statusGlow[region.status]}`
                  )}
                />

                {/* Issue badge */}
                {region.activeIssues > 0 && (
                  <text
                    x={region.coordinates.x + 6}
                    y={region.coordinates.y - 6}
                    className="text-[3px] font-bold fill-red-600"
                  >
                    {region.activeIssues}
                  </text>
                )}

                {/* Tooltip on hover */}
                <title>
                  {region.name}
                  {region.activeIssues > 0 && ` - ${region.activeIssues} active issues`}
                </title>
              </g>
            ))}
          </svg>

          {/* Legend */}
          <div className="absolute bottom-2 left-2 flex flex-col gap-1 bg-background/80 backdrop-blur-sm p-2 rounded text-xs">
            <div className="flex items-center gap-2">
              <div className="w-3 h-3 rounded-full bg-green-500" />
              <span>Healthy</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-3 h-3 rounded-full bg-amber-500" />
              <span>Warning</span>
            </div>
            <div className="flex items-center gap-2">
              <div className="w-3 h-3 rounded-full bg-red-500" />
              <span>Critical</span>
            </div>
          </div>
        </div>

        {/* Region list below map */}
        <div className="mt-4 grid grid-cols-2 sm:grid-cols-3 gap-2">
          {regions.map((region) => (
            <button
              key={region.id}
              onClick={() => onRegionClick?.(region)}
              className={cn(
                'flex items-center justify-between p-2 rounded-lg border transition-colors hover:bg-slate-50 dark:hover:bg-slate-800',
                region.status === 'healthy' && 'border-green-200 dark:border-green-800',
                region.status === 'warning' && 'border-amber-200 dark:border-amber-800',
                region.status === 'critical' && 'border-red-200 dark:border-red-800'
              )}
            >
              <span className="text-sm font-medium">{region.name}</span>
              {region.activeIssues > 0 && (
                <Badge variant="destructive" className="text-xs">
                  {region.activeIssues}
                </Badge>
              )}
            </button>
          ))}
        </div>
      </CardContent>
    </Card>
  )
}
