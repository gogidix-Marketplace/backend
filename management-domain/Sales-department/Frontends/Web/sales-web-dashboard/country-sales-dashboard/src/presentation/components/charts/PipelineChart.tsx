// PipelineChart Component
// Funnel-style chart for deal pipeline stages

import React from 'react';
import { DEAL_STAGES } from '@shared';
import type { DealStage } from '@domain/types';

export interface PipelineChartProps {
  data: Array<{
    stage: DealStage;
    count: number;
    totalValue: number;
    percentage: number;
  }>;
  height?: number;
  showValues?: boolean;
  className?: string;
}

export const PipelineChart: React.FC<PipelineChartProps> = ({
  data,
  height = 300,
  showValues = true,
  className = '',
}) => {
  if (data.length === 0) {
    return (
      <div className={`pipeline-chart pipeline-chart-empty ${className}`} style={{ height }}>
        <p>No pipeline data available</p>
      </div>
    );
  }

  const maxCount = Math.max(...data.map(d => d.count));
  const stageOrder = Object.keys(DEAL_STAGES) as DealStage[];
  const sortedData = [...data].sort((a, b) =>
    stageOrder.indexOf(a.stage) - stageOrder.indexOf(b.stage)
  );

  return (
    <div className={`pipeline-chart ${className}`}>
      <div className="pipeline-chart-bars">
        {sortedData.map((item, index) => {
          const stageInfo = DEAL_STAGES[item.stage];
          const barWidth = (item.count / maxCount) * 100;
          const isLast = index === sortedData.length - 1;

          return (
            <div key={item.stage} className="pipeline-stage-row">
              <div className="pipeline-stage-info">
                <div
                  className="pipeline-stage-color"
                  style={{ backgroundColor: stageInfo.color }}
                />
                <span className="pipeline-stage-name">{stageInfo.label}</span>
              </div>

              <div className="pipeline-stage-bar-container">
                <div
                  className="pipeline-stage-bar"
                  style={{
                    width: `${barWidth}%`,
                    backgroundColor: `${stageInfo.color}20`,
                    borderColor: stageInfo.color,
                  }}
                >
                  {showValues && (
                    <span className="pipeline-stage-count">{item.count}</span>
                  )}
                </div>
                {!isLast && (
                  <div className="pipeline-stage-arrow">
                    <svg width="20" height="20" viewBox="0 0 20 20" fill="none">
                      <path
                        d="M10 4L16 10L10 16"
                        stroke="#9CA3AF"
                        strokeWidth="2"
                        strokeLinecap="round"
                        strokeLinejoin="round"
                      />
                    </svg>
                  </div>
                )}
              </div>

              <div className="pipeline-stage-stats">
                {showValues && (
                  <>
                    <span className="pipeline-stage-value">
                      {formatCompactCurrency(item.totalValue)}
                    </span>
                    <span className="pipeline-stage-percentage">
                      {item.percentage}%
                    </span>
                  </>
                )}
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
};

function formatCompactCurrency(value: number): string {
  if (value >= 1000000) return `$${(value / 1000000).toFixed(1)}M`;
  if (value >= 1000) return `$${(value / 1000).toFixed(1)}K`;
  return `$${value}`;
}

export default PipelineChart;
