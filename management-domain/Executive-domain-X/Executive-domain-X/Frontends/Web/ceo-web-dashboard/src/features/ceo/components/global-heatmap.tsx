import { Card, CardContent, CardHeader, CardTitle, CardDescription } from '@shared/components/ui/card'
import { Badge } from '@shared/components/ui/badge'
import { useCrossDomainData } from '@shared/services/api'

export type RegionHealth = 'healthy' | 'warning' | 'critical'

export interface RegionData {
  id: string
  name: string
  health: RegionHealth
  revenue: number
  target: number
  growth: number
  activeIssues: number
}

// Fallback mock data for when API is unavailable
const fallbackRegions: RegionData[] = [
  {
    id: 'na',
    name: 'North America',
    health: 'healthy',
    revenue: 18500000,
    target: 20000000,
    growth: 12.3,
    activeIssues: 0,
  },
  {
    id: 'eu',
    name: 'Europe',
    health: 'critical',
    revenue: 12000000,
    target: 15000000,
    growth: -5.2,
    activeIssues: 3,
  },
  {
    id: 'africa',
    name: 'Africa',
    health: 'warning',
    revenue: 12000000,
    target: 14000000,
    growth: 18.7,
    activeIssues: 1,
  },
]

const healthConfig = {
  healthy: { color: 'bg-green-500', border: 'border-green-500', bgLight: 'bg-green-50 dark:bg-green-950/20', text: 'text-green-700 dark:text-green-400' },
  warning: { color: 'bg-amber-500', border: 'border-amber-500', bgLight: 'bg-amber-50 dark:bg-amber-950/20', text: 'text-amber-700 dark:text-amber-400' },
  critical: { color: 'bg-red-500', border: 'border-red-500', bgLight: 'bg-red-50 dark:bg-red-950/20', text: 'text-red-700 dark:text-red-400' },
}

/**
 * Transform API regional data to RegionData format
 */
function transformApiRegionData(apiData: any): RegionData {
  const revenue = apiData.revenue ?? apiData.totalRevenue ?? 0
  const target = apiData.target ?? apiData.targetRevenue ?? revenue * 1.1
  const growth = apiData.growth ?? apiData.growthRate ?? 0
  const activeIssues = apiData.activeIssues ?? apiData.issues ?? apiData.incidents ?? 0

  // Determine health based on growth and issues
  let health: RegionHealth = 'healthy'
  if (activeIssues > 2 || growth < -5) {
    health = 'critical'
  } else if (activeIssues > 0 || growth < 0) {
    health = 'warning'
  }

  return {
    id: apiData.id ?? apiData.regionId ?? apiData.code?.toLowerCase() ?? 'unknown',
    name: apiData.name ?? apiData.region ?? apiData.regionName ?? 'Unknown',
    health,
    revenue,
    target,
    growth,
    activeIssues,
  }
}

export interface GlobalHeatmapProps {
  onRegionClick?: (regionId: string) => void
  className?: string
  /**
   * Force use of mock data (for testing/demos)
   * @default false - will try API first, fall back to mock on error
   */
  useMockData?: boolean
}

export function GlobalOperationalHeatmap({
  onRegionClick,
  className,
  useMockData = false,
}: GlobalHeatmapProps) {
  // Fetch cross-domain data which includes regional breakdown
  const { data: crossDomainData, loading } = useCrossDomainData(
    {}, // Empty params object to get all domains
    useMockData ? undefined : 60000
  )

  // Transform API data or use fallback
  let regions: RegionData[] = fallbackRegions

  if (!useMockData && crossDomainData?.regions) {
    regions = crossDomainData.regions.map(transformApiRegionData)
  } else if (!useMockData && crossDomainData?.data?.regions) {
    regions = crossDomainData.data.regions.map(transformApiRegionData)
  }

  const totalActiveIssues = regions.reduce((sum, r) => sum + r.activeIssues, 0)

  return (
    <Card className={className}>
      <CardHeader className="pb-3">
        <div className="flex items-center justify-between">
          <div>
            <CardTitle className="text-lg">Global Operational Heatmap</CardTitle>
            <CardDescription className="text-sm">
              {loading && !useMockData ? 'Loading...' : 'Revenue by region with health status'}
            </CardDescription>
          </div>
          {totalActiveIssues > 0 && (
            <Badge variant="destructive" className="text-xs">{totalActiveIssues} Issues</Badge>
          )}
        </div>
      </CardHeader>

      <CardContent className="space-y-3">
        {/* Simplified Map Visualization */}
        {loading && !useMockData ? (
          <div className="h-40 sm:h-48 flex items-center justify-center bg-slate-50 dark:bg-slate-800 rounded-lg">
            <div className="animate-pulse text-muted-foreground">Loading map data...</div>
          </div>
        ) : (
          <div className="relative h-40 sm:h-48 bg-gradient-to-br from-slate-50 to-slate-100 dark:from-slate-800 dark:to-slate-900 rounded-lg overflow-hidden p-4">
            <svg viewBox="0 0 300 120" className="w-full h-full">
              {regions.map((region, index) => {
                const config = healthConfig[region.health]
                const positions = [
                  { x: 40, y: 30 },
                  { x: 130, y: 25 },
                  { x: 140, y: 70 },
                ]
                const pos = positions[index] || { x: 50, y: 50 }

                return (
                  <g key={region.id} onClick={() => onRegionClick?.(region.id)} className="cursor-pointer">
                    <ellipse
                      cx={pos.x}
                      cy={pos.y}
                      rx={35}
                      ry={25}
                      fill={config.color}
                      fillOpacity={0.15}
                      stroke={config.color}
                      strokeWidth={2}
                      className="hover:fill-opacity-30 transition-all"
                    />
                    <text
                      x={pos.x}
                      y={pos.y}
                      textAnchor="middle"
                      dominantBaseline="middle"
                      className="fill-foreground text-[10px] sm:text-xs font-medium pointer-events-none"
                    >
                      {region.name}
                    </text>
                    {region.activeIssues > 0 && (
                      <circle cx={pos.x + 25} cy={pos.y - 15} r="8" fill="#ef4444" className="animate-pulse" />
                    )}
                  </g>
                )
              })}
            </svg>
          </div>
        )}

        {/* Region Cards */}
        <div className="grid gap-2 sm:gap-3 grid-cols-1 sm:grid-cols-3">
          {regions.map((region) => {
            const config = healthConfig[region.health]
            const progress = Math.min((region.revenue / region.target) * 100, 100)

            return (
              <button
                key={region.id}
                onClick={() => onRegionClick?.(region.id)}
                className={`p-3 rounded-lg border-2 transition-all text-left hover:shadow-md ${config.border} bg-white dark:bg-slate-800`}
              >
                <div className="flex items-start justify-between mb-2">
                  <h4 className="font-semibold text-sm">{region.name}</h4>
                  {region.activeIssues > 0 && (
                    <Badge variant="destructive" className="text-xs h-5 min-w-5 flex items-center justify-center p-0">
                      {region.activeIssues}
                    </Badge>
                  )}
                </div>

                <div className="space-y-1.5">
                  <div className="flex items-center justify-between text-xs">
                    <span className="text-muted-foreground">Revenue</span>
                    <span className="font-medium">${(region.revenue / 1000000).toFixed(1)}M</span>
                  </div>
                  <div className="flex items-center justify-between text-xs">
                    <span className="text-muted-foreground">Target</span>
                    <span>${(region.target / 1000000).toFixed(1)}M</span>
                  </div>
                  <div className="flex items-center justify-between text-xs">
                    <span className="text-muted-foreground">Growth</span>
                    <span className={region.growth >= 0 ? 'text-emerald-600' : 'text-red-600'}>
                      {region.growth >= 0 ? '+' : ''}{region.growth}%
                    </span>
                  </div>
                </div>

                {/* Progress Bar */}
                <div className="mt-2 h-1.5 bg-slate-100 dark:bg-slate-700 rounded-full overflow-hidden">
                  <div
                    className={`h-full ${config.color} rounded-full`}
                    style={{ width: `${progress}%` }}
                  />
                </div>
              </button>
            )
          })}
        </div>
      </CardContent>
    </Card>
  )
}
