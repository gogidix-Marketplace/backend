// ChannelROIChart Component
// Displays ROI/ROAS by marketing channel

import './ChannelROIChart.css';

interface ChannelData {
  channel: string;
  spend: number;
  revenue: number;
  roi: number;
  roas: number;
  conversions: number;
}

interface ChannelROIChartProps {
  data: ChannelData[];
  metric?: 'roi' | 'roas';
  height?: number;
  className?: string;
}

export function ChannelROIChart({
  data,
  metric = 'roi',
  height = 300,
  className = '',
}: ChannelROIChartProps) {
  const maxValue = Math.max(...data.map((d) => metric === 'roi' ? d.roi : d.roas));

  return (
    <div className={`channel-roi-chart ${className}`} style={{ height }}>
      <div className="chart-header">
        <h3 className="chart-title">Channel {metric.toUpperCase()} Performance</h3>
        <div className="chart-toggle">
          <button
            className={`toggle-btn ${metric === 'roi' ? 'toggle-btn-active' : ''}`}
            onClick={() => {}}
          >
            ROI
          </button>
          <button
            className={`toggle-btn ${metric === 'roas' ? 'toggle-btn-active' : ''}`}
            onClick={() => {}}
          >
            ROAS
          </button>
        </div>
      </div>

      <div className="chart-body">
        <svg width="100%" height="100%" preserveAspectRatio="none">
          {/* Grid lines */}
          {[0, 0.25, 0.5, 0.75, 1].map((percent) => (
            <line
              key={percent}
              x1="0"
              y1={`${100 - percent * 100}%`}
              x2="100%"
              y2={`${100 - percent * 100}%`}
              stroke="#f3f4f6"
              strokeWidth="1"
            />
          ))}

          {/* Zero line */}
          <line
            x1="0"
            y1="90%"
            x2="100%"
            y2="90%"
            stroke="#d1d5db"
            strokeWidth="2"
          />

          {/* Bars */}
          {data.map((item, index) => {
            const value = metric === 'roi' ? item.roi : item.roas;
            const normalizedValue = value / maxValue;
            const barHeight = Math.max(normalizedValue * 85, 5);
            const x = index * (100 / data.length);
            const barWidth = 60 / data.length;
            const y = 90 - barHeight;
            const isPositive = value >= 0;

            return (
              <g key={index}>
                <rect
                  x={`${x + (100 / data.length - barWidth) / 2}%`}
                  y={`${isPositive ? y : 90}%`}
                  width={`${barWidth}%`}
                  height={`${Math.abs(barHeight)}%`}
                  fill={isPositive ? '#10b981' : '#ef4444'}
                  rx="3"
                  className="chart-bar"
                />
                <text
                  x={`${x + 50 / data.length}%`}
                  y={`${isPositive ? y - 2 : 95}%`}
                  textAnchor="middle"
                  fontSize="11"
                  fill={isPositive ? '#059669' : '#dc2626'}
                  fontWeight="600"
                >
                  {metric === 'roi' ? `${value.toFixed(1)}%` : `${value.toFixed(2)}x`}
                </text>
                <text
                  x={`${x + 50 / data.length}%`}
                  y="98%"
                  textAnchor="middle"
                  fontSize="10"
                  fill="#6b7280"
                >
                  {item.channel.length > 8 ? item.channel.substring(0, 6) + '..' : item.channel}
                </text>
              </g>
            );
          })}
        </svg>
      </div>

      <div className="chart-details">
        {data.map((item, index) => {
          const value = metric === 'roi' ? item.roi : item.roas;
          const isPositive = value >= 0;

          return (
            <div key={index} className="channel-detail">
              <div className="detail-header">
                <span className="detail-channel">{item.channel}</span>
                <span className={`detail-value ${isPositive ? 'detail-positive' : 'detail-negative'}`}>
                  {metric === 'roi' ? `${value.toFixed(1)}%` : `${value.toFixed(2)}x`}
                </span>
              </div>
              <div className="detail-metrics">
                <span className="detail-metric">Spend: ${item.spend.toLocaleString()}</span>
                <span className="detail-metric">Revenue: ${item.revenue.toLocaleString()}</span>
                <span className="detail-metric">Conversions: {item.conversions.toLocaleString()}</span>
              </div>
            </div>
          );
        })}
      </div>
    </div>
  );
}
