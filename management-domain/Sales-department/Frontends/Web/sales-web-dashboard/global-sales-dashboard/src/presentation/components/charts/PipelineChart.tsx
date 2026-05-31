// Pipeline Chart Component
// Visualizes sales pipeline by stage and funnel

import { useMemo } from 'react';
import type { PipelineStageSummary, GlobalPipelineSummary } from '@domain/types';
import { formatCurrency, formatPercentage } from '@shared';
import { PIPELINE_STAGES } from '@shared';
import './PipelineChart.css';

export interface PipelineFunnelProps {
  stages: PipelineStageSummary[];
  height?: number;
}

export function PipelineFunnel({ stages, height = 250 }: PipelineFunnelProps) {
  const totalValue = useMemo(() => stages.reduce((sum, s) => sum + s.totalValue, 0), [stages]);

  const maxCount = Math.max(...stages.map(s => s.count));

  return (
    <div className="pipeline-funnel">
      <div className="funnel-stages">
        {stages.map((stage, index) => {
          const widthPercentage = (stage.count / maxCount) * 100;
          const previousStageCount = index > 0 ? stages[index - 1].count : stage.count;
          const conversionRate = previousStageCount > 0
            ? ((stage.count / previousStageCount) * 100).toFixed(0)
            : '100';

          return (
            <div key={stage.stage} className="funnel-stage">
              <div className="funnel-stage-header">
                <span className="stage-name">{stage.stage}</span>
                <span className="stage-count">{stage.count} deals</span>
              </div>
              <div className="funnel-bar-container">
                <div
                  className="funnel-bar"
                  style={{
                    width: `${Math.max(widthPercentage, 10)}%`,
                    backgroundColor: PIPELINE_STAGES[stage.stage.toUpperCase()]?.color || '#9CA3AF',
                  }}
                >
                  <div className="funnel-bar-content">
                    <span className="funnel-value">{formatCurrency(stage.totalValue)}</span>
                    <span className="funnel-percentage">{stage.percentage}%</span>
                  </div>
                </div>
              </div>
              <div className="funnel-stage-footer">
                <span className="weighted-value">
                  Weighted: {formatCurrency(stage.weightedValue)}
                </span>
                {index > 0 && (
                  <span className="conversion-rate">
                    {conversionRate}% conversion
                  </span>
                )}
              </div>
              {index < stages.length - 1 && (
                <div className="funnel-arrow">
                  <svg width="20" height="20" viewBox="0 0 20 20" fill="currentColor">
                    <path d="M10 14l-4-4h8l-4 4z" opacity="0.3" />
                  </svg>
                </div>
              )}
            </div>
          );
        })}
      </div>
    </div>
  );
}

export interface PipelineStageChartProps {
  byStage: PipelineStageSummary[];
  height?: number;
}

export function PipelineStageChart({ byStage, height = 200 }: PipelineStageChartProps) {
  const maxValue = useMemo(() => Math.max(...byStage.map(s => s.totalValue)), [byStage]);
  const chartWidth = 100;
  const chartHeight = 100;
  const barWidth = (chartWidth - 10) / byStage.length;

  return (
    <svg
      width="100%"
      height={height}
      viewBox={`0 0 100 ${chartHeight + 20}`}
      preserveAspectRatio="none"
      className="pipeline-stage-chart"
    >
      {byStage.map((stage, i) => {
        const barHeight = (stage.totalValue / maxValue) * chartHeight;
        const x = 5 + i * barWidth;
        const y = chartHeight - barHeight;
        const color = PIPELINE_STAGES[stage.stage.toUpperCase()]?.color || '#9CA3AF';

        return (
          <g key={stage.stage}>
            {/* Bar */}
            <rect
              x={x + 1}
              y={y}
              width={barWidth - 2}
              height={barHeight}
              fill={color}
              opacity="0.8"
              rx="1"
            />
            {/* Value label */}
            <text
              x={x + barWidth / 2}
              y={y - 2}
              textAnchor="middle"
              fontSize="4"
              fill="#374151"
              fontWeight="600"
            >
              {(stage.totalValue / 1000000).toFixed(1)}M
            </text>
            {/* Stage label */}
            <text
              x={x + barWidth / 2}
              y={chartHeight + 6}
              textAnchor="middle"
              fontSize="3"
              fill="#6B7280"
            >
              {stage.stage.slice(0, 4)}
            </text>
          </g>
        );
      })}
    </svg>
  );
}

export interface PipelineDonutProps {
  summary: GlobalPipelineSummary;
  size?: number;
}

export function PipelineDonut({ summary, size = 200 }: PipelineDonutProps) {
  const center = size / 2;
  const radius = size / 2 - 10;
  const circumference = 2 * Math.PI * radius;

  const stageColors = {
    New: '#9CA3AF',
    Qualified: '#3B82F6',
    Proposal: '#8B5CF6',
    Negotiating: '#F59E0B',
    Closing: '#10B981',
  };

  let currentAngle = -90;

  return (
    <div className="pipeline-donut" style={{ width: size, height: size }}>
      <svg width={size} height={size} viewBox={`0 0 ${size} ${size}`}>
        <circle
          cx={center}
          cy={center}
          r={radius}
          fill="none"
          stroke="#E5E7EB"
          strokeWidth="20"
        />
        {summary.byStage.map((stage, index) => {
          const segmentAngle = (stage.percentage / 100) * 360;
          const dashArray = (segmentAngle / 360) * circumference;
          const dashOffset = -1 * (currentAngle + 90) / 360 * circumference;
          currentAngle += segmentAngle;

          return (
            <circle
              key={stage.stage}
              cx={center}
              cy={center}
              r={radius}
              fill="none"
              stroke={stageColors[stage.stage] || '#9CA3AF'}
              strokeWidth="20"
              strokeDasharray={`${dashArray} ${circumference - dashArray}`}
              strokeDashoffset={dashOffset}
              transform={`rotate(-90 ${center} ${center})`}
              className="donut-segment"
            >
              <title>{stage.stage}: {stage.percentage}%</title>
            </circle>
          );
        })}
        <text
          x={center}
          y={center - 5}
          textAnchor="middle"
          fontSize="24"
          fontWeight="bold"
          fill="#1F2937"
        >
          {summary.totalDeals}
        </text>
        <text
          x={center}
          y={center + 15}
          textAnchor="middle"
          fontSize="12"
          fill="#6B7280"
        >
          Total Deals
        </text>
      </svg>
    </div>
  );
}

export interface PipelineVelocityChartProps {
  velocity: {
    avgDaysInStage: Record<string, number>;
    overallCycle: number;
    cycleTarget: number;
    cycleVariance: number;
  };
  stages: string[];
  height?: number;
}

export function PipelineVelocityChart({ velocity, stages, height = 150 }: PipelineVelocityChartProps) {
  const maxDays = Math.max(
    ...Object.values(velocity.avgDaysInStage).filter(v => v > 0),
    velocity.cycleTarget
  );

  return (
    <div className="pipeline-velocity-chart">
      <div className="velocity-header">
        <h4>Sales Cycle Velocity</h4>
        <div className="velocity-summary">
          <span className="velocity-current">{velocity.overallCycle} days</span>
          <span className="velocity-target">Target: {velocity.cycleTarget} days</span>
          <span className={`velocity-variance ${velocity.cycleVariance <= 0 ? 'positive' : 'negative'}`}>
            {velocity.cycleVariance > 0 ? '+' : ''}{velocity.cycleVariance} days
          </span>
        </div>
      </div>
      <svg
        width="100%"
        height={height}
        viewBox={`0 0 100 ${height}`}
        preserveAspectRatio="none"
        className="velocity-bars"
      >
        {stages.map((stage, i) => {
          const days = velocity.avgDaysInStage[stage] || 0;
          const barWidth = 100 / stages.length;
          const barHeight = (days / maxDays) * (height - 30);
          const x = i * barWidth;
          const y = height - 20 - barHeight;

          return (
            <g key={stage}>
              <rect
                x={x + 2}
                y={y}
                width={barWidth - 4}
                height={barHeight}
                fill={days > 0 ? '#2563EB' : '#E5E7EB'}
                opacity="0.8"
                rx="1"
              />
              {days > 0 && (
                <text
                  x={x + barWidth / 2}
                  y={y - 2}
                  textAnchor="middle"
                  fontSize="4"
                  fill="#374151"
                  fontWeight="600"
                >
                  {days}d
                </text>
              )}
              <text
                x={x + barWidth / 2}
                y={height - 5}
                textAnchor="middle"
                fontSize="3"
                fill="#6B7280"
              >
                {stage.slice(0, 3)}
              </text>
            </g>
          );
        })}
        {/* Target line */}
        <line
          x1="0"
          y1={height - 20 - (velocity.cycleTarget / maxDays) * (height - 30)}
          x2="100"
          y2={height - 20 - (velocity.cycleTarget / maxDays) * (height - 30)}
          stroke="#10B981"
          strokeWidth="0.5"
          strokeDasharray="2,2"
        />
      </svg>
    </div>
  );
}

// Pipeline by Country Heatmap
export interface PipelineCountryHeatmapProps {
  byCountry: GlobalPipelineSummary['byCountry'];
  stages: string[];
}

export function PipelineCountryHeatmap({ byCountry, stages }: PipelineCountryHeatmapProps) {
  const maxValue = Math.max(
    ...byCountry.flatMap(c => Object.values(c.stageDistribution))
  );

  return (
    <div className="pipeline-heatmap">
      <table className="heatmap-table">
        <thead>
          <tr>
            <th>Country</th>
            {stages.map(stage => (
              <th key={stage}>{stage}</th>
            ))}
          </tr>
        </thead>
        <tbody>
          {byCountry.map(country => (
            <tr key={country.country.code}>
              <td className="country-cell">
                <span>{country.country.flag}</span>
                <span>{country.country.name}</span>
              </td>
              {stages.map(stage => {
                const value = country.stageDistribution[stage] || 0;
                const intensity = value / maxValue;
                const hue = 220 - intensity * 40; // Blue to dark blue
                return (
                  <td
                    key={stage}
                    className="heatmap-cell"
                    style={{
                      backgroundColor: `hsla(${hue}, 70%, ${50 + intensity * 20}%, ${intensity * 0.9 + 0.1})`,
                    }}
                  >
                    <span className="heatmap-value">{value}</span>
                  </td>
                );
              })}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
