import { type Dashboard } from '../data/dashboards'

interface DashboardCardProps {
  dashboard: Dashboard
  status: 'running' | 'stopped' | 'pending'
  onLaunch: () => void
  onStart: () => void
  style?: React.CSSProperties
}

const DashboardCard = ({ dashboard, status, onLaunch, onStart, style }: DashboardCardProps) => {
  const getStatusText = () => {
    if (status === 'running') return 'Running'
    if (status === 'pending') return 'Starting...'
    return 'Stopped'
  }

  return (
    <div
      className="dashboard-card animate-fade-in"
      style={style}
      onClick={status === 'running' ? onLaunch : undefined}
    >
      {/* Status Badge */}
      <div className="flex justify-between items-start mb-4">
        <div className="dashboard-icon">{dashboard.icon}</div>
        <span className={`status-badge ${status}`}>
          {status === 'pending' && <div className="progress-ring" />}
          {status === 'running' && <span className="w-2 h-2 bg-emerald-400 rounded-full animate-pulse" />}
          {status === 'stopped' && <span className="w-2 h-2 bg-slate-500 rounded-full" />}
          {getStatusText()}
        </span>
      </div>

      {/* Title & Description */}
      <h3 className="text-lg font-semibold text-slate-100 mb-2 group-hover:text-executive-gold transition-colors">
        {dashboard.name}
      </h3>
      <p className="text-slate-400 text-sm mb-4 line-clamp-2">{dashboard.description}</p>

      {/* Port Info */}
      <div className="flex items-center gap-2 text-xs text-slate-500 mb-4">
        <span className="font-mono bg-slate-800 px-2 py-1 rounded">
          :{dashboard.port}
        </span>
        <span className="text-slate-600">|</span>
        <span className="capitalize">{dashboard.category}</span>
      </div>

      {/* Features Tags */}
      <div className="flex flex-wrap gap-1.5 mb-4">
        {dashboard.features.slice(0, 3).map(feature => (
          <span
            key={feature}
            className="px-2 py-1 bg-slate-800/50 text-slate-400 text-xs rounded-md"
          >
            {feature}
          </span>
        ))}
        {dashboard.features.length > 3 && (
          <span className="px-2 py-1 bg-slate-800/50 text-slate-500 text-xs rounded-md">
            +{dashboard.features.length - 3}
          </span>
        )}
      </div>

      {/* Action Button */}
      <button
        onClick={(e) => {
          e.stopPropagation()
          status === 'running' ? onLaunch() : onStart()
        }}
        className={`w-full py-2.5 rounded-lg font-medium transition-all duration-200 ${
          status === 'running'
            ? 'bg-emerald-600 hover:bg-emerald-500 text-white'
            : status === 'pending'
            ? 'bg-amber-600 text-white cursor-wait'
            : 'bg-executive-blue hover:bg-executive-blue-dark text-white'
        }`}
      >
        {status === 'running' ? '🚀 Open Dashboard' : status === 'pending' ? '⏳ Starting...' : '▶️ Start Dashboard'}
      </button>
    </div>
  )
}

export default DashboardCard
