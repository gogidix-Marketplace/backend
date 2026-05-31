import React from 'react';
import { useKPIMetrics } from '../../hooks/use-api';
import { KPICard, KPICardSkeleton } from './KPICard';

interface KPIGridProps {
  limit?: number;
  className?: string;
}

export const KPIGrid: React.FC<KPIGridProps> = ({ limit, className }) => {
  const { data: metrics, isLoading, error } = useKPIMetrics();

  const displayMetrics = limit ? metrics?.slice(0, limit) : metrics;

  if (isLoading) {
    return (
      <div className={`grid gap-4 md:grid-cols-2 lg:grid-cols-4 ${className}`}>
        {Array.from({ length: limit || 4 }).map((_, i) => (
          <KPICardSkeleton key={i} />
        ))}
      </div>
    );
  }

  if (error) {
    return (
      <div className={`grid gap-4 md:grid-cols-2 lg:grid-cols-4 ${className}`}>
        {Array.from({ length: limit || 4 }).map((_, i) => (
          <div key={i} className="p-6 border rounded-lg bg-destructive/10 text-destructive">
            Error loading KPI
          </div>
        ))}
      </div>
    );
  }

  return (
    <div className={`grid gap-4 md:grid-cols-2 lg:grid-cols-4 ${className}`}>
      {displayMetrics?.map((metric) => (
        <KPICard key={metric.id} metric={metric} />
      ))}
    </div>
  );
};
