import React from 'react';
import { ChevronLeft, ChevronRight, Globe2, TrendingUp, BarChart3, Users } from 'lucide-react';
import { useRegionalStore } from '../../stores/regional-store';
import { cn } from '../../utils/cn';

export const Sidebar = () => {
  const { sidebarOpen, setSidebarOpen, regions, setSelectedRegion, drillDownLevel } = useRegionalStore();

  return (
    <aside
      className={cn(
        'fixed top-16 left-0 z-20 h-[calc(100vh-4rem)] border-r bg-background transition-all duration-300',
        sidebarOpen ? 'w-64' : 'w-16'
      )}
    >
      <nav className="flex flex-col h-full p-2 space-y-1">
        {/* Navigation Items */}
        <div className="flex-1 space-y-1">
          <button
            className={cn(
              'w-full flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors',
              drillDownLevel === 'global'
                ? 'bg-primary text-primary-foreground'
                : 'text-muted-foreground hover:bg-accent'
            )}
            onClick={() => {
              // Reset filters
              useRegionalStore.getState().resetFilters();
            }}
          >
            <Globe2 className="h-5 w-5 flex-shrink-0" />
            {sidebarOpen && <span>Global View</span>}
          </button>

          {sidebarOpen && (
            <div className="px-3 py-2">
              <p className="text-xs font-semibold text-muted-foreground mb-2">REGIONS</p>
            </div>
          )}

          {regions?.map((region: any) => (
            <button
              key={region.id}
              className={cn(
                'w-full flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors',
                drillDownLevel === 'regional' && region.id === useRegionalStore.getState().selectedRegion
                  ? 'bg-primary text-primary-foreground'
                  : 'text-muted-foreground hover:bg-accent'
              )}
              onClick={() => setSelectedRegion(region.id)}
              title={!sidebarOpen ? region.name : undefined}
            >
              <div className="flex items-center justify-center w-8 h-8 rounded-lg bg-primary/10">
                <span className="text-xs font-bold">{region.code}</span>
              </div>
              {sidebarOpen && (
                <>
                  <span className="flex-1 text-left">{region.name}</span>
                  <span className="text-xs text-muted-foreground">
                    {new Intl.NumberFormat('en-US', {
                      notation: 'compact',
                      style: 'currency',
                      currency: 'USD',
                    }).format(region.revenue)}
                  </span>
                </>
              )}
            </button>
          ))}
        </div>

        {/* Footer Stats */}
        {sidebarOpen && (
          <div className="border-t pt-2 space-y-1">
            <button className="w-full flex items-center gap-3 rounded-lg px-3 py-2 text-sm text-muted-foreground hover:bg-accent">
              <TrendingUp className="h-5 w-5" />
              <span>Compare Regions</span>
            </button>
            <button className="w-full flex items-center gap-3 rounded-lg px-3 py-2 text-sm text-muted-foreground hover:bg-accent">
              <BarChart3 className="h-5 w-5" />
              <span>Reports</span>
            </button>
          </div>
        )}
      </nav>

      {/* Collapse Toggle */}
      <button
        className="absolute -right-3 top-1/2 -translate-y-1/2 h-6 w-6 rounded-full border bg-background shadow-md flex items-center justify-center hover:bg-accent"
        onClick={() => setSidebarOpen(!sidebarOpen)}
      >
        {sidebarOpen ? (
          <ChevronLeft className="h-3 w-3" />
        ) : (
          <ChevronRight className="h-3 w-3" />
        )}
      </button>
    </aside>
  );
};
