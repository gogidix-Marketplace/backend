import './MetricCard.css'

interface MetricCardProps {
  title: string
  value: string
  change?: number
  changeType?: 'increase' | 'decrease' | 'neutral'
  target?: string
  attainment?: number
  unit?: string
  icon?: React.ReactNode
}

export function MetricCard({
  title,
  value,
  change,
  changeType = 'neutral',
  target,
  attainment,
  unit = '',
  icon,
}: MetricCardProps) {
  return (
    <div className="metric-card">
      <div className="metric-card-header">
        <span className="metric-card-title">{title}</span>
        {icon && <span className="metric-card-icon">{icon}</span>}
      </div>
      <div className="metric-card-value">
        {value}
        {unit && <span className="metric-card-unit">{unit}</span>}
      </div>
      {change !== undefined && (
        <div className={`metric-card-change ${changeType === 'increase' ? 'positive' : changeType === 'decrease' ? 'negative' : 'neutral'}`}>
          <span className="change-icon">{changeType === 'increase' ? '\u2191' : changeType === 'decrease' ? '\u2193' : '\u2192'}</span>
          <span className="change-value">{Math.abs(change)}%</span>
        </div>
      )}
      {target && (
        <div className="metric-card-target">
          <span className="target-label">Target: {target}</span>
        </div>
      )}
      {attainment !== undefined && (
        <div className="metric-card-progress">
          <div className="progress-bar">
            <div
              className={`progress-fill ${attainment >= 100 ? 'success' : attainment >= 75 ? 'warning' : 'danger'}`}
              style={{ width: `${Math.min(attainment, 100)}%` }}
            />
          </div>
          <span className="progress-label">{attainment}%</span>
        </div>
      )}
    </div>
  )
}
